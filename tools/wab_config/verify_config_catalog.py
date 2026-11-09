#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""T01 配置一致性门禁（CI 用）。

检查四件事，任何一件不通过就退出码 1：
  1. config_catalog.json 结构：126 行、键唯一、类型/作用端/AI 标注合法、默认值具体、冲突项都有中文提示
  2. 生成物与目录一致：以 --check 模式重跑 gen_config_sources.py，diff 必须为空
  3. 保守默认值：AI 主动类开关必须 false、概率类必须 ≤0.1、会改世界的功能默认关闭
  4. 宿主 plugin.json 现有 24 项逐字保留、无重复键、下拉项 default 落在 options 内；
     模组 Java 生成物含 126 个键常量与 47 条冲突提示

可选 --peer <另一条模组线的仓库根>：逐字节比对两线的三份 Java 生成物。
只用 Python 标准库。
"""
import argparse
import hashlib
import io
import json
import os
import subprocess
import sys

CATALOG_REL = os.path.join("tools", "wab_config", "config_catalog.json")
PLUGIN_REL = os.path.join("plugins", "wab_minecraft", "plugin.json")
JAVA_FILES = ["WabConfigKeys.java", "WabConfigDefaults.java", "WabConflictPrompts.java",
              "WabConfigRuntime.java", "WabRules.java"]
JAVA_DIR_REL = os.path.join("src", "main", "java", "com", "github", "tartaricacid",
                            "touhoulittlemaid", "compat", "wab", "config")

# 现有 24 项的默认值基线：本轮只允许追加，不允许改这些值。
BASELINE_DEFAULTS = {
    "enabled": "true", "port": "27322", "lan": "false", "auto_create_characters": "false",
    "allow_world_actions": "true", "allow_gestures": "true", "allow_photo": "true",
    "allow_voice": "true", "chat_affection": "true", "autonomy_pulse_minutes": "0",
    "think_scenes": "interaction", "think_cooldown_seconds": "90", "love_words": "true",
    "auto_switch_save": "false", "auto_open_bound_save": "true", "world_mode": "galgame",
    "display_target": "main", "backup_minutes": "10", "auto_taste": "true",
    "hunger_beg": "true", "hunger_threshold": "14", "hunger_higher_is_hungry": "true",
    "force_load_chunks": "false", "force_load_radius": "2",
}

# 用户点名要保守的功能（默认关闭）。
WORLD_RISK_PREFIXES = (
    "item_drop", "item_equip", "item_pickup", "container_write", "farming",
    "mlg_water", "suffocation", "lava_boat", "comfort_seek", "sneak_eat",
    "headpat", "autonomy", "chat_round", "chat_invisible", "plan_execution",
    "small_model_compat", "chess_cry", "catchphrase_ai", "love_words_ai",
    "ai_heat", "workbuddy_", "think_mode",
)
# 这些前缀里有些是「模式/概率/数字」而非开关，只对 switch 类型套用关闭规则。

# 保护性开关：名字落在风险前缀里，但语义上 true 才更保守（强制校验 / 让路 / 能力检测 / 纯表现）。
# 双向校验：它们必须保持 true，被误改成 false 同样报错，避免把「保护」当成「功能」关掉。
PROTECTIVE_TRUE = {
    "farming_require_explicit_spec": "农耕必须显式指定四要素，true 是强制校验",
    "lava_boat_emergency_only": "岩浆船仅濒死可用，true 收窄触发条件",
    "headpat_player_refuse_enabled": "玩家可拒绝摸头，true 把控制权交给玩家",
    "headpat_sad_on_refuse": "拒绝后的伤心表情，纯表现不改世界",
    "headpat_sake_no_bend": "小酒壶不俯身，表现例外",
    "headpat_conflict_guard": "检测到附属摸头时让路",
    "sneak_eat_conflict_guard": "检测到原版偷吃动画时让路",
    "workbuddy_capability_check": "模型能力检测，不支持时隐藏控件、不发无效请求",
}


def read_text(path):
    with io.open(path, "r", encoding="utf-8") as f:
        return f.read()


def sha256(path):
    h = hashlib.sha256()
    with io.open(path, "rb") as f:
        h.update(f.read())
    return h.hexdigest()


def fail(errors, message):
    errors.append(message)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", default=None)
    parser.add_argument("--kind", choices=["host", "mod"], default=None)
    parser.add_argument("--peer", default=None, help="另一条模组线仓库根，用于双线字节比对")
    args = parser.parse_args()

    script_dir = os.path.dirname(os.path.abspath(__file__))
    root = os.path.abspath(args.root or os.path.dirname(os.path.dirname(script_dir)))
    errors = []
    stats = {"root": root}

    # ---- 1. 目录结构 ----
    catalog_path = os.path.join(root, CATALOG_REL)
    if not os.path.isfile(catalog_path):
        print(json.dumps({"status": "failed", "errors": ["缺少 config_catalog.json"]},
                         ensure_ascii=False, indent=2))
        return 1
    catalog = json.loads(read_text(catalog_path))
    rows = catalog["rows"]
    stats["rowCount"] = len(rows)
    stats["catalogVersion"] = catalog["catalogVersion"]
    keys = [r["key"] for r in rows]
    if len(keys) != len(set(keys)):
        fail(errors, "配置键重复")
    if len(rows) != catalog["rowCount"]:
        fail(errors, "rowCount 与 rows 长度不一致")
    conflict_rows = [r for r in rows if r["conflict"]]
    stats["conflictRows"] = len(conflict_rows)
    stats["promptCovered"] = sum(1 for r in conflict_rows if r["promptText"].strip())
    if stats["promptCovered"] != len(conflict_rows):
        fail(errors, "有冲突项缺中文提示文案")
    for r in rows:
        if "待定" in str(r["default"]) or str(r["default"]).strip() == "":
            fail(errors, "默认值不具体: " + r["key"])
        if r["aiTag"] not in ("纯代码", "可选AI", "可选AI_AI驱动"):
            fail(errors, "AI 标注非法: " + r["key"])
        if r["scope"] not in ("模组", "WAB 宿主", "双端", "游戏内", "插件"):
            fail(errors, "作用端非法: " + r["key"])

    # ---- 3. 保守默认值 ----
    for r in rows:
        key, kind, default = r["key"], r["type"], str(r["defaultValue"])
        if kind == "switch" and r["aiTag"] != "纯代码" and default.lower() != "false":
            fail(errors, "AI 主动类开关默认应为 false: %s = %s" % (key, default))
        if ("probability" in key or "chance" in key) and kind == "number":
            try:
                if float(default) > 0.1:
                    fail(errors, "概率默认超过 0.1: %s = %s" % (key, default))
            except ValueError:
                fail(errors, "概率默认值无法解析: %s = %s" % (key, default))
        if kind == "switch" and key.startswith(WORLD_RISK_PREFIXES):
            if key in PROTECTIVE_TRUE:
                if default.lower() != "true":
                    fail(errors, "保护性开关被关掉（%s）: %s = %s"
                         % (PROTECTIVE_TRUE[key], key, default))
            elif default.lower() != "false":
                fail(errors, "会改世界/会打断的功能默认应为 false: %s = %s" % (key, default))

    # ---- 2. 生成物一致性 ----
    gen = os.path.join(script_dir, "gen_config_sources.py")
    cmd = [sys.executable, gen, "--root", root, "--check"]
    if args.kind:
        cmd += ["--kind", args.kind]
    proc = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8")
    try:
        gen_result = json.loads(proc.stdout)
    except Exception:
        gen_result = {"consistent": False, "changed": ["<无法解析生成脚本输出>"]}
        fail(errors, "生成脚本输出无法解析: " + (proc.stderr or "")[:300])
    stats["generated"] = gen_result.get("generated", [])
    stats["changed"] = gen_result.get("changed", [])
    if not gen_result.get("consistent", False) or proc.returncode != 0:
        fail(errors, "生成物与配置目录不一致: " + ", ".join(gen_result.get("changed", [])))

    # ---- 4a. 宿主 plugin.json ----
    kind = args.kind
    if kind is None:
        kind = "host" if os.path.isfile(os.path.join(root, "WarmAsBefore.csproj")) else "mod"
    stats["kind"] = kind
    if kind == "host":
        plugin_path = os.path.join(root, PLUGIN_REL)
        plugin = json.loads(read_text(plugin_path))
        fields = plugin["settings"]["fields"]
        stats["pluginFields"] = len(fields)
        by_key = {f["key"]: f for f in fields}
        if len(by_key) != len(fields):
            fail(errors, "plugin.json 字段键重复")
        for key, expected in BASELINE_DEFAULTS.items():
            if key not in by_key:
                fail(errors, "现有字段丢失: " + key)
            elif str(by_key[key].get("default")) != expected:
                fail(errors, "现有字段默认值被改: %s %s -> %s"
                     % (key, expected, by_key[key].get("default")))
        for f in fields:
            if f.get("type") in ("select", "multiselect"):
                opts = f.get("options", [])
                if not opts:
                    fail(errors, "下拉项缺 options: " + f["key"])
                elif str(f.get("default")) not in [str(o) for o in opts]:
                    fail(errors, "下拉项 default 不在 options 内: %s = %s"
                         % (f["key"], f.get("default")))
        missing = [r["key"] for r in rows if r["key"] not in by_key]
        stats["catalogKeysMissingInPlugin"] = len(missing)
        if missing:
            fail(errors, "目录里有 %d 个键未出现在 plugin.json: %s"
                 % (len(missing), ", ".join(missing[:5])))
    else:
        java_dir = os.path.join(root, JAVA_DIR_REL)
        for name in JAVA_FILES:
            if not os.path.isfile(os.path.join(java_dir, name)):
                fail(errors, "缺少 Java 生成物: " + name)
        keys_java = os.path.join(java_dir, "WabConfigKeys.java")
        if os.path.isfile(keys_java):
            text = read_text(keys_java)
            stats["javaKeyConstants"] = text.count("public static final String ")
            if stats["javaKeyConstants"] != len(rows):
                fail(errors, "Java 键常量数量与目录不一致: %d" % stats["javaKeyConstants"])
        prompts_java = os.path.join(java_dir, "WabConflictPrompts.java")
        if os.path.isfile(prompts_java):
            stats["javaPrompts"] = read_text(prompts_java).count("PROMPTS.put(")
            if stats["javaPrompts"] != len(conflict_rows):
                fail(errors, "Java 冲突提示条数与目录不一致: %d" % stats["javaPrompts"])

    # ---- 4b. 双线字节一致 ----
    if args.peer:
        peer = os.path.abspath(args.peer)
        stats["peer"] = peer
        pairs = [(os.path.join(JAVA_DIR_REL, name), name) for name in JAVA_FILES] if kind == "mod" else []
        shared = ["tools/wab_config/config_catalog.json", "tools/wab_config/gen_config_sources.py",
                  "tools/wab_config/verify_config_catalog.py", "tools/wab_config/README.md",
                  "tools/wab_rules_selftest/RulesSelfTest.java"]
        pairs += [(rel.replace("/", os.sep), rel.replace("/", os.sep)) for rel in shared]
        for rel, label in pairs:
            a, b = os.path.join(root, rel), os.path.join(peer, rel)
            if not (os.path.isfile(a) and os.path.isfile(b)):
                fail(errors, "双线比对缺文件: " + label)
                continue
            if sha256(a) != sha256(b):
                fail(errors, "双线生成物字节不一致: " + label)

    result = {"status": "passed" if not errors else "failed", "errors": errors, "stats": stats}
    print(json.dumps(result, ensure_ascii=False, indent=2))
    return 0 if not errors else 1


if __name__ == "__main__":
    sys.exit(main())
