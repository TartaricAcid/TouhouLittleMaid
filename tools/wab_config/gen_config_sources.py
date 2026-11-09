#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""WAB 配置单一真相源 -> 三仓库生成物（T01 / B1）。

唯一允许声明键名与默认值的地方是 tools/wab_config/config_catalog.json。
本脚本从它生成：
  · 宿主（--kind host）：Services/Core/ConfigCatalog.cs、plugins/wab_minecraft/plugin.json 的 settings.fields
  · 模组（--kind mod）：compat/wab/config/ 下 WabConfigKeys.java / WabConfigDefaults.java / WabConflictPrompts.java
两条模组线生成物必须字节一致；差异只留在 WabPlatform.java，不在这些文件里。

用法：
  python gen_config_sources.py                 # 写入生成物
  python gen_config_sources.py --check        # 只比对，不写；供 CI 与 verify_config_catalog.py 调用
  python gen_config_sources.py --root /path   # 指定仓库根，默认由脚本位置推断

只用 Python 标准库，无第三方依赖。
"""
import argparse
import io
import json
import os
import re
import sys

CATALOG_REL = os.path.join("tools", "wab_config", "config_catalog.json")
JAVA_PACKAGE = "com.github.tartaricacid.touhoulittlemaid.compat.wab.config"
JAVA_DIR_REL = os.path.join("src", "main", "java", "com", "github", "tartaricacid",
                            "touhoulittlemaid", "compat", "wab", "config")
HOST_CS_REL = os.path.join("Services", "Core", "ConfigCatalog.cs")
PLUGIN_REL = os.path.join("plugins", "wab_minecraft", "plugin.json")

# PRD 备注里格式不统一的少数下拉项，在这里显式钉死，避免靠运行时猜测。
OPTION_OVERRIDES = {
    "item_pickup_filter_mode": ["item_id", "name", "attribute", "durability", "enchant"],
    "suffocation_path_mode": ["auto", "air", "dig"],
    "comfort_probability_source": ["config", "ai"],
    "backup_conflict_policy": ["wab_wins", "mod_wins", "newest"],
    "headpat_reaction_tier": ["1", "2", "3"],
    "plan_report_mode": ["none", "stepwise", "summary"],
    "plan_planner": ["code", "ai"],
    "autonomy_interval_mode": ["fixed", "random_range"],
    "maid_chat_channel": ["bubble", "chat_bar", "both"],
    "ai_heat_source": ["code", "ai", "manual"],
    "think_mode": ["off", "fast", "deep", "auto"],
    "love_words_source": ["builtin", "ai", "both"],
    "wav_think_mode_select": ["off", "fast", "deep", "auto"],
    "ai_param_format": ["openai_flat", "nested_by_role", "vendor_ext", "widget_binding"],
    "ai_param_widget": ["auto", "slider", "button", "text", "select"],
    "model_package": ["custom", "gpt", "claude", "openai_compatible", "deepseek"],
    "workbuddy_effort_level": ["low", "medium", "high"],
    "small_model_tool_format": ["xml", "json_brace"],
    "small_model_parse_fallback": ["plain_chat", "retry", "deny"],
    "display_target": ["main", "phone"],
    "phone_nav_third_action": ["settings", "tasks"],
}

# 下拉项中文显示名；未列出的用键本身，不编造。
OPTION_LABELS = {
    "code": "代码规则", "ai": "交给 AI", "config": "用配置值", "auto": "自动",
    "fixed": "固定间隔", "random_range": "随机区间", "off": "关闭",
    "fast": "快速", "deep": "深度", "builtin": "内置词条", "both": "两者都要",
    "none": "不报告", "stepwise": "逐步报告", "summary": "只回汇总",
    "1": "一档", "2": "二档", "3": "三档",
    "main": "主窗口", "phone": "手机窗口", "settings": "设置", "tasks": "任务（保留）",
    "custom": "自定义", "gpt": "GPT 套餐", "claude": "Claude 套餐",
    "openai_compatible": "OpenAI 兼容端点", "deepseek": "DeepSeek 套餐",
    "low": "低", "medium": "中", "high": "高",
    "xml": "XML", "json_brace": "花括号 JSON",
    "plain_chat": "当普通对话", "retry": "重试一次", "deny": "拒绝执行",
    "wab_wins": "以 WAB 为准", "mod_wins": "以模组为准", "newest": "取最新一份",
    "item_id": "物品 ID", "name": "名称", "attribute": "属性",
    "durability": "耐久", "enchant": "附魔",
    "bubble": "面对面气泡", "chat_bar": "聊天栏", "air": "先找空气", "dig": "挖掘逃生",
    "slider": "滑动条", "button": "按钮", "text": "文本框", "select": "下拉框",
    "openai_flat": "扁平兼容", "nested_by_role": "按用途嵌套",
    "vendor_ext": "厂商扩展块", "widget_binding": "控件绑定", "manual": "手动",
}

VALID_TYPES = {"switch", "select", "number", "text", "multiselect"}
VALID_SCOPES = {"模组", "WAB 宿主", "双端", "游戏内", "插件"}
VALID_AI = {"纯代码", "可选AI", "可选AI_AI驱动"}


def read_text(path):
    with io.open(path, "r", encoding="utf-8") as f:
        return f.read()


def java_string(value):
    """Java 字符串字面量：中文转 \\uXXXX，避免源文件编码差异。"""
    return json.dumps(value, ensure_ascii=True)


def cs_string(value):
    """C# 字符串字面量：直接 UTF-8 中文，转义引号与反斜杠。"""
    return json.dumps(value, ensure_ascii=False)


def const_name(key):
    return re.sub(r"[^A-Z0-9_]", "_", key.upper())


def default_literal(row):
    """按解析后的默认值生成 Java 字面量与类型名。"""
    v = row["defaultValue"]
    if isinstance(v, bool):
        return "boolean", "true" if v else "false"
    if isinstance(v, int):
        return "int", str(v)
    if isinstance(v, float):
        return "double", repr(v)
    return "String", java_string(str(v))


def default_string(row):
    """plugin.json 的 default 一律字符串（与现有 24 项格式一致）。"""
    v = row["defaultValue"]
    if isinstance(v, bool):
        return "true" if v else "false"
    if isinstance(v, float):
        return repr(v)
    return str(v)


def options_for(row):
    if row["key"] in OPTION_OVERRIDES:
        return list(OPTION_OVERRIDES[row["key"]])
    if row["type"] != "select":
        return []
    m = re.search(r"选项\s*([^；;。]+)", row["note"])
    if not m:
        return []
    out = []
    for token in m.group(1).split("/"):
        token = re.sub(r"[（(].*$", "", token).strip()
        token = re.sub(r"\s+", "_", token)
        if token:
            out.append(token)
    return out


def load_catalog(root):
    path = os.path.join(root, CATALOG_REL)
    catalog = json.loads(read_text(path))
    rows = catalog["rows"]
    assert catalog["rowCount"] == len(rows), "rowCount 与 rows 长度不一致"
    keys = [r["key"] for r in rows]
    assert len(keys) == len(set(keys)), "配置键重复"
    for r in rows:
        assert r["type"] in VALID_TYPES, "非法类型: " + r["key"]
        assert r["scope"] in VALID_SCOPES, "非法作用端: " + r["key"]
        assert r["aiTag"] in VALID_AI, "非法 AI 标注: " + r["key"]
        assert "待定" not in str(r["default"]), "默认值待定: " + r["key"]
        assert isinstance(r["conflict"], bool), "conflict 非布尔: " + r["key"]
    return catalog


# ---------------------------------------------------------------- Java
def java_keys(catalog):
    out = ["package %s;" % JAVA_PACKAGE, "",
           "/** 由 config_catalog.json 生成，禁止手改。版本 %s，共 %d 个键。 */"
           % (catalog["catalogVersion"], catalog["rowCount"]),
           "public final class WabConfigKeys {",
           "    private WabConfigKeys() {", "    }", ""]
    for r in catalog["rows"]:
        out.append("    public static final String %s = %s;"
                   % (const_name(r["key"]), java_string(r["key"])))
    out += ["}", ""]
    return "\n".join(out)


def java_defaults(catalog):
    out = ["package %s;" % JAVA_PACKAGE, "",
           "import java.util.HashMap;", "import java.util.Map;", "",
           "/** 由 config_catalog.json 生成，禁止手改。业务代码禁止另写字面量默认值。 */",
           "public final class WabConfigDefaults {",
           "    private WabConfigDefaults() {", "    }", ""]
    for r in catalog["rows"]:
        jtype, literal = default_literal(r)
        out.append("    public static final %s %s = %s;" % (jtype, const_name(r["key"]), literal))
    out += ["",
            "    private static final Map<String, Object> DEFAULTS = new HashMap<>();",
            "",
            "    static {"]
    for r in catalog["rows"]:
        _, literal = default_literal(r)
        out.append("        DEFAULTS.put(%s, %s);" % (java_string(r["key"]), literal))
    out += ["    }", "",
            "    /** 按 key 取默认值；未知键返回 null，调用方必须自己判断，不给隐式兜底。 */",
            "    public static Object of(String key) {",
            "        return DEFAULTS.get(key);",
            "    }",
            "}", ""]
    return "\n".join(out)


def java_prompts(catalog):
    conflict_rows = [r for r in catalog["rows"] if r["conflict"]]
    out = ["package %s;" % JAVA_PACKAGE, "",
           "import java.util.HashMap;", "import java.util.Map;", "",
           "/** 由 config_catalog.json 生成，禁止手改。冲突提示中文文案唯一来源（%d 键）。 */"
           % len(conflict_rows),
           "public final class WabConflictPrompts {",
           "    private WabConflictPrompts() {", "    }", "",
           "    private static final Map<String, String> PROMPTS = new HashMap<>();",
           "", "    static {"]
    for r in conflict_rows:
        out.append("        PROMPTS.put(%s, %s);"
                   % (java_string(r["key"]), java_string(r["promptText"])))
    out += ["    }", "",
            "    public static String of(String key) {",
            "        return PROMPTS.getOrDefault(key, \"\");",
            "    }",
            "}", ""]
    return "\n".join(out)


# ---------------------------------------------------------------- C#
def cs_catalog(catalog):
    out = ["using System.Collections.Generic;", "",
           "namespace WarmAsBefore.Services.Core;", "",
           "/// <summary>由 tools/wab_config/config_catalog.json 生成，禁止手改。</summary>",
           "public static class ConfigCatalog", "{",
           "    public const string Version = %s;" % cs_string(catalog["catalogVersion"]),
           "    public const int RowCount = %d;" % catalog["rowCount"], "",
           "    public sealed record Entry(string Name, string Type, string Default, string Scope,",
           "                              string AiTag, bool Conflict, string ConflictTarget, string PromptText);",
           "",
           "    public static readonly IReadOnlyDictionary<string, Entry> Entries =",
           "        new Dictionary<string, Entry>", "        {"]
    for r in catalog["rows"]:
        out.append("            [%s] = new Entry(%s, %s, %s, %s, %s, %s, %s, %s),"
                   % (cs_string(r["key"]), cs_string(r["name"]), cs_string(r["type"]),
                      cs_string(default_string(r)), cs_string(r["scope"]), cs_string(r["aiTag"]),
                      "true" if r["conflict"] else "false",
                      cs_string(r["conflictTarget"]), cs_string(r["promptText"])))
    out += ["        };", "",
            "    /// <summary>默认值的唯一入口；未知键返回空串，由调用方显式处理。</summary>",
            "    public static string Default(string key) =>",
            "        Entries.TryGetValue(key, out var entry) ? entry.Default : string.Empty;",
            "",
            "    /// <summary>是否需要冲突检测。</summary>",
            "    public static bool NeedsConflictCheck(string key) =>",
            "        Entries.TryGetValue(key, out var entry) && entry.Conflict;",
            "}", ""]
    return "\n".join(out)


# ---------------------------------------------------------------- plugin.json
def build_fields(catalog, existing_fields):
    """保留现有字段对象原样；目录里没出现的键追加。重叠键不重复、不改值。"""
    existing_keys = {f["key"] for f in existing_fields}
    fields = list(existing_fields)
    for r in catalog["rows"]:
        if r["key"] in existing_keys:
            continue
        field = {
            "key": r["key"],
            "label": r["name"],
            "type": r["type"],
            "default": default_string(r),
            "help": r["note"],
        }
        opts = options_for(r)
        if r["type"] in ("select", "multiselect") and opts:
            field["options"] = opts
            field["option_labels"] = {o: OPTION_LABELS.get(o, o) for o in opts}
        field["group"] = r["group"]
        field["scope"] = r["scope"]
        field["ai_tag"] = r["aiTag"]
        field["conflict_flag"] = r["conflict"]
        fields.append(field)
    return fields


def dump_plugin(plugin):
    """保持原有排版：顶层紧凑，fields 数组每项一行，便于 diff 只显示新增行。"""
    lines = ["{"]
    keys = list(plugin.keys())
    for i, key in enumerate(keys):
        value = plugin[key]
        comma = "," if i < len(keys) - 1 else ""
        if key == "settings" and isinstance(value, dict):
            lines.append('  "settings": {')
            skeys = list(value.keys())
            for j, skey in enumerate(skeys):
                sval = value[skey]
                scomma = "," if j < len(skeys) - 1 else ""
                if skey == "fields" and isinstance(sval, list):
                    lines.append('    "fields": [')
                    for k, field in enumerate(sval):
                        fcomma = "," if k < len(sval) - 1 else ""
                        lines.append("      " + json.dumps(field, ensure_ascii=False,
                                                          separators=(", ", ": ")) + fcomma)
                    lines.append("    ]" + scomma)
                else:
                    lines.append("    " + json.dumps(skey, ensure_ascii=False) + ": "
                                 + json.dumps(sval, ensure_ascii=False, separators=(", ", ": ")) + scomma)
            lines.append("  }" + comma)
        else:
            lines.append("  " + json.dumps(key, ensure_ascii=False) + ": "
                         + json.dumps(value, ensure_ascii=False, separators=(", ", ": ")) + comma)
    lines.append("}")
    return "\n".join(lines) + "\n"


def detect_kind(root):
    if os.path.isfile(os.path.join(root, "WarmAsBefore.csproj")):
        return "host"
    if os.path.isdir(os.path.join(root, "src", "main", "java")):
        return "mod"
    raise RuntimeError("无法识别仓库类型: " + root)


def generated(root, kind, catalog):
    files = {}
    if kind == "mod":
        base = os.path.join(root, JAVA_DIR_REL)
        files[os.path.join(base, "WabConfigKeys.java")] = java_keys(catalog)
        files[os.path.join(base, "WabConfigDefaults.java")] = java_defaults(catalog)
        files[os.path.join(base, "WabConflictPrompts.java")] = java_prompts(catalog)
    else:
        files[os.path.join(root, HOST_CS_REL)] = cs_catalog(catalog)
        plugin_path = os.path.join(root, PLUGIN_REL)
        plugin = json.loads(read_text(plugin_path))
        plugin["settings"]["fields"] = build_fields(catalog, plugin["settings"]["fields"])
        files[plugin_path] = dump_plugin(plugin)
    return files


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", default=None)
    parser.add_argument("--kind", choices=["host", "mod"], default=None)
    parser.add_argument("--check", action="store_true", help="只比对不写入")
    args = parser.parse_args()

    script_dir = os.path.dirname(os.path.abspath(__file__))
    root = os.path.abspath(args.root or os.path.dirname(os.path.dirname(script_dir)))
    kind = args.kind or detect_kind(root)
    catalog = load_catalog(root)
    files = generated(root, kind, catalog)

    diffs = []
    for path, content in files.items():
        current = read_text(path) if os.path.isfile(path) else None
        if current == content:
            continue
        diffs.append(path)
        if not args.check:
            os.makedirs(os.path.dirname(path), exist_ok=True)
            with io.open(path, "w", encoding="utf-8", newline="\n") as f:
                f.write(content)

    result = {
        "root": root,
        "kind": kind,
        "catalogVersion": catalog["catalogVersion"],
        "rowCount": catalog["rowCount"],
        "generated": [os.path.relpath(p, root).replace("\\", "/") for p in files],
        "changed": [os.path.relpath(p, root).replace("\\", "/") for p in diffs],
        "mode": "check" if args.check else "write",
        "consistent": not diffs,
    }
    print(json.dumps(result, ensure_ascii=False, indent=2))
    return 0 if (not diffs or not args.check) else 1


if __name__ == "__main__":
    sys.exit(main())
