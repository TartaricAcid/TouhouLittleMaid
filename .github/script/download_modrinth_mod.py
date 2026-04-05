"""
从 Modrinth 批量下载指定模组的最新版本

Modrinth API 文档:
    https://docs.modrinth.com/api/operations/tags/versions/
"""

import hashlib
import json
import logging
import urllib.error
import urllib.parse
import urllib.request
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path

# ============================================================
#  全局配置 —— 按需修改下面的变量即可
# ============================================================

# 需要下载的模组 ID 或 slug 列表
# 你可以在 modrinth 的模组页面 URL 中找到 slug，例如:
# https://modrinth.com/mod/cloth-config  -> slug 为 "cloth-config"
MOD_IDS: list[str] = [
    "kubejs", "rhino", "architectury-api",  # KubeJS 及其依赖
    "jei",  # JEI
    "patchouli",  # Patchouli 手册
    "jade", "the-one-probe",  # 信息提示
    "citadel", "domestication-innovation",  # 驯化革新及前置
    "slashblade-resharped",  # 拔刀剑
    "iron-chests",  # 铁箱子
    "immersive-melodies",  # 沉浸式奏乐
    "kotlin-for-forge", "libipn", "inventory-profiles-next",  # IPN 及前置
    "timeless-and-classics-zero",  # TaCZ 永恒枪械工坊
    "superb-warfare",  # 卓越前线
    "curios",  # Curios
    "aquaculture",  # 水产养殖
    "create",  # 机械动力
    "sophisticated-core", "sophisticated-backpacks",  # 精妙背包
    "kaleidoscope-cookery",  # 森罗厨房
    "geckolib",  # GeckoLib
    "farmers-delight",  # 农夫乐事
    "common-network", "just-more-cakes",  # 更多蛋糕
    # =========
    # 附属联动模组
    # =========
    "maidsoul-kitchen",  # 女仆厨房
    "maid-storage-manager",  # 女仆仓管
    "maid-useful-task",  # 女仆实用任务
    "touhou-little-maid-spell",  # 车万女仆：万法皆通
    # "umapyoi", "uma_maid", "mmlib",  # 马儿小女仆及前置，目前有服务端加载 bug
    "true-power-of-maid", "mrqx`s-slashblade-core", "true-power",  # 车万女仆：真正的力量及前置
    "winefoxs_spellbooks", "irons-spells-n-spellbooks", "playeranimator", "irons-lib",  # 酒狐的魔法书及前置
    "maidusehandcrank",  # 女仆摇曲柄
    "telepathic-maid", "diligentstalker",  # 电波女仆及前置
    "maids-return",  # 女仆归心
    # "touhou-little-maid-epistalove", "contact-loverekindled", "oelib",  # 车万女仆：纸短情长及前置，目前有 LLM 兼容问题
    "spice-of-life-maid",  # 生活调味料：酒狐萝贝
    "spice-of-life-maids-dream",  # 生活调味料：女仆之梦
    "maid-restaurant",  # 女仆餐厅
    "touhou-little-maid-beacon",  # 车万女仆：移动信标
    "tamablefairy",  # 车万女仆：收容异变
    "maidsconstruct", "tinkers-construct", "mantle",  # 女仆匠魂及前置
]

# Minecraft 版本，例如 "1.20.1"
MINECRAFT_VERSION: str = "1.20.1"

# 模组加载器，例如 "forge" / "neoforge" / "fabric" / "quilt"
LOADER: str = "forge"

# Modrinth API 请求时携带的 User-Agent
USER_AGENT: str = "github.com/TartaricAcid/TouhouLittleMaid (baka943@qq.com)"

# Modrinth API 基础地址（一般无需修改）
API_BASE: str = "https://api.modrinth.com/v2"

# 并发下载线程数
MAX_WORKERS: int = 8

# ============================================================
#  内部常量 / 路径推算
# ============================================================

# 脚本所在目录:  <project_root>/.github/script/
_SCRIPT_DIR = Path(__file__).resolve().parent
# 项目根目录:    <project_root>
_PROJECT_ROOT = _SCRIPT_DIR.parent.parent
# 下载目标目录:  <project_root>/run/mods
_MODS_DIR = _PROJECT_ROOT / "run/mods"

# 日志配置（Minecraft 风格: [HH:MM:SS] [线程/级别] [来源]: 消息）
logging.basicConfig(
    level=logging.INFO,
    format="[%(asctime)s] [MainThread/%(levelname)s] [Downloader]: %(message)s",
    datefmt="%H:%M:%S",
)
logger = logging.getLogger("Downloader")


# ============================================================
#  工具函数
# ============================================================

def _format_size(size_bytes: int) -> str:
    """将字节数格式化为人类可读的大小"""
    if size_bytes <= 0:
        return "大小未知"
    for unit in ("B", "KB", "MB", "GB"):
        if size_bytes < 1024:
            return f"{size_bytes:.1f} {unit}"
        size_bytes /= 1024
    return f"{size_bytes:.1f} TB"


def _api_get(path: str, params: dict | None = None) -> dict | list:
    """
    发送 GET 请求到 Modrinth API 并返回解析后的 JSON。
    path 示例: "/project/cloth-config/version"
    """
    url = API_BASE + path
    if params:
        url += "?" + urllib.parse.urlencode(params)

    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read().decode("utf-8"))


def download_mod(mod_id: str) -> bool:
    """
    下载指定模组在当前 Minecraft 版本 + 加载器下的最新版本。
    返回 True 表示成功，False 表示跳过或失败。
    """
    # ---- 1. 查询版本列表 ----
    params = {
        "loaders": json.dumps([LOADER]),
        "game_versions": json.dumps([MINECRAFT_VERSION]),
    }
    try:
        versions = _api_get(f"/project/{urllib.parse.quote(mod_id, safe='')}/version", params)
    except Exception as e:
        logger.error(f"{mod_id} — 查询失败: {e}")
        return False

    if not versions:
        logger.warning(f"{mod_id} — 无可用版本 ({MINECRAFT_VERSION}/{LOADER})")
        return False

    latest = versions[0]
    version_number = latest.get("version_number", "unknown")

    # ---- 2. 选取主文件 ----
    files = latest.get("files", [])
    if not files:
        logger.error(f"{mod_id} {version_number} — 版本无附件")
        return False

    primary = next((f for f in files if f.get("primary")), files[0])
    file_url: str = primary["url"]
    file_name: str = primary["filename"]
    file_size: int = primary.get("size", 0)
    file_sha1: str = primary.get("hashes", {}).get("sha1", "")

    # ---- 3. 跳过已存在的文件 ----
    dest = _MODS_DIR / file_name
    if dest.exists() and file_size and dest.stat().st_size == file_size:
        logger.info(f"{mod_id} {version_number} — 已存在，跳过 ({file_name})")
        return True

    # ---- 4. 下载文件 ----
    try:
        req = urllib.request.Request(file_url, headers={"User-Agent": USER_AGENT})
        with urllib.request.urlopen(req, timeout=120) as resp:
            data = resp.read()
        dest.write_bytes(data)
    except Exception as e:
        logger.error(f"{mod_id} {version_number} — 下载失败: {e}")
        return False

    # ---- 5. 校验 SHA-1 ----
    if file_sha1:
        actual_sha1 = hashlib.sha1(dest.read_bytes()).hexdigest()
        if actual_sha1 != file_sha1:
            logger.error(f"{mod_id} {version_number} — SHA-1 校验失败")
            dest.unlink(missing_ok=True)
            return False

    logger.info(f"{mod_id} {version_number} — {file_name} ({_format_size(file_size)})")
    return True


# ============================================================
#  主入口
# ============================================================

def main() -> None:
    logger.info(
        f"配置: MC {MINECRAFT_VERSION} / {LOADER} | 模组数 {len(MOD_IDS)} | 线程数 {MAX_WORKERS} | 目标 {_MODS_DIR}")

    # 确保目标目录存在
    _MODS_DIR.mkdir(parents=True, exist_ok=True)

    success_count = 0
    fail_list: list[str] = []

    # 多线程并发下载
    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        future_to_mod = {executor.submit(download_mod, mod_id): mod_id for mod_id in MOD_IDS}
        for future in as_completed(future_to_mod):
            mod_id = future_to_mod[future]
            try:
                ok = future.result()
            except Exception as e:
                logger.error(f"{mod_id} — 未捕获异常: {e}")
                ok = False
            if ok:
                success_count += 1
            else:
                fail_list.append(mod_id)

    # ---- 汇总 ----
    if fail_list:
        logger.warning(f"完成: {success_count}/{len(MOD_IDS)} 成功，失败: {', '.join(fail_list)}")
    else:
        logger.info(f"完成: {success_count}/{len(MOD_IDS)} 全部成功")


if __name__ == "__main__":
    main()
