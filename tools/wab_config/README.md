# WAB 配置目录（T01 / B1 落地）

`config_catalog.json` 是 18 大项 126 个配置键的**唯一真相源**：只有这里允许声明键名、类型、默认值、作用端、AI 标注、冲突标记与中文提示文案。业务代码里禁止再写字面量默认值。

## 三个仓库各有一份，内容必须字节一致

| 仓库 | 分支 | 生成物 |
|---|---|---|
| `WarmAsBefore`（宿主） | `master` | `Services/Core/ConfigCatalog.cs`、`plugins/wab_minecraft/plugin.json` 的 `settings.fields` |
| `TouhouLittleMaid-WAB` Forge 1.20.1 | `ci/wab-1.20-testkit` | `compat/wab/config/WabConfigKeys.java`、`WabConfigDefaults.java`、`WabConflictPrompts.java` |
| `TouhouLittleMaid-WAB` NeoForge 1.21.1 | `ci/wab-1.21` | 同上，三份文件与 1.20 线**字节一致** |

模组双线的版本差异只允许留在 `WabPlatform.java`，不进这三个生成文件。

## 命令

```bash
# 重新生成（写文件）
python tools/wab_config/gen_config_sources.py --root .

# 只比对不写（CI 用）；退出码 0 = 一致
python tools/wab_config/gen_config_sources.py --root . --check

# 完整门禁：目录结构 + 生成一致性 + 保守默认值 + plugin.json / Java 生成物 + 双线字节比对
python tools/wab_config/verify_config_catalog.py --root .
python tools/wab_config/verify_config_catalog.py --root . --peer ../TouhouLittleMaid-WAB
```

脚本只用 Python 标准库，Windows / Linux 都能跑。

## 门禁拦什么

1. **结构**：126 行、键唯一、类型与作用域合法、默认值具体、47 个冲突项全部带中文提示。
2. **生成一致性**：以 `--check` 重跑生成，diff 必须为空——手改生成物会立刻被抓到。
3. **保守默认值**：AI 主动类开关必须 `false`、概率类默认 ≤0.1、会改世界的功能默认关闭；8 个保护性开关（农耕强制显式指定、岩浆船仅濒死、冲突让路、能力检测等）**必须保持 `true`**，被误关也报错。
4. **宿主**：现有 24 项键名与默认值逐字保留，字段无重复，下拉项 `default` 必须落在 `options` 内；当前共 147 项（24 原有 + 123 追加）。
5. **模组**：126 个键常量、47 条冲突提示；`--peer` 比对两条线的生成物字节一致。

## 下发链路（为什么扩展 plugin.json 就够了）

宿主 `MaidBridgeService.BuildPluginConfigJson()` 会把 `plugin.json` 的 `settings.fields` 原样下发成 `fields` 帧，模组 `WabRemoteConfig` 整表替换后由游戏内设置页渲染。所以追加字段会自动长出开关；`BuildSettingsJson()` 的既有 18 个 camelCase 键保持不变，新增键一律 snake_case。

> 注意：`plugin.json` 原先被 `/plugins/` 整体忽略，已加精确例外单独放行这一个文件——CI checkout 后必须能读到它，否则门禁会失败。
