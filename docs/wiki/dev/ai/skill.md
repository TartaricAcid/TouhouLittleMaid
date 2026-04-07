---
title: 编写 Skill
createTime: 2026/04/07 00:31:00
permalink: /wiki/dev/ai/skill/
icon: ri:book-open-line
---

Skill（技能）是一段写在 Markdown 文件里的提示词，在被触发时会注入到对话上下文中，告诉模型当前应当遵循哪些行为规范或执行哪些步骤。

Skill 本质上是玩家、模组作者或整合包作者对女仆 AI 行为的“编程”，通过自然语言告诉模型该做什么、怎么做。

---

## 一、Skill 提供途径

Skill 可以通过两种途径提供给游戏：

### 配置文件目录（推荐用于个人/整合包）

将 Skill 文件放在游戏的配置文件夹下：

::: file-tree

- config
    - touhou_little_maid
        - skills
            - <skill-name>
                - skill.md

:::

`<skill-name>` 是你起的文件夹名称（建议与 Skill 的 `name` 字段保持一致）。游戏启动时会自动扫描并加载该目录下所有的
`skill.md` 文件。

如果 Skill 需要附带多语言参考内容（仅 `knowledge` 类型使用），可以在同一文件夹下创建 `references` 子目录：

::: file-tree

- config
    - touhou_little_maid
        - skills
            - my-skill
                - skill.md
                - references
                    - zh_cn.md # 简体中文
                    - en_us.md # 英文，如果没有找到对应语言的文件，会把该文件作为备选

:::

::: tip
游戏会在首次启动时自动创建 `config/touhou_little_maid/skills/` 目录，你只需要在里面创建子目录并放入 `skill.md` 即可。
:::

### 数据包

也可以通过数据包制作 Skill，数据包中的 Skill 文件放在：

::: file-tree

- data
    - touhou_little_maid # 命名空间必须是 touhou_little_maid
        - skills
            - <skill-name>
                - skill.md

:::

::: warning
受 Minecraft 的设计限制，文件夹和文件名只能使用：小写英文字母、数字、连字符（`-`）和下划线（`_`）。
:::

数据包中的 Skill 优先级**高于**配置文件目录中的同名 Skill。

---

## 二、skill.md 文件格式

每个 `skill.md` 文件由两部分组成：

1. **YAML Front Matter**（用 `---` 包裹的头部）：声明 Skill 的元信息
2. **Body**（头部之后的正文）：Skill 的实际内容，注入给模型时原样使用

```markdown
---
name: my-skill-name
description: 这个 Skill 的用途描述，供模型判断是否应该调用
metadata:
  tlm-type: knowledge
---

# My Skill

在这里写 Skill 的正文内容。
支持任意 Markdown 格式，也可以是纯文本指令。

例如：当玩家要求你唱歌时，先说“好的，演唱开始！”，然后输出一段歌词。
```

### Front Matter 字段说明

:::: field-group

::: field name="name" type="String" required
Skill 的唯一名称。

- 仅允许小写字母（`a-z`）、数字（`0-9`）和连字符（`-`）
- 不能以连字符开头或结尾
- 最多 64 个字符

模型通过 `use_skill` Tool 调用时，使用的就是这个名称。
:::

::: field name="description" type="String" required
描述这个 Skill 的用途，以及何时应该使用它。

- 最多 1024 个字符
- 不能为空

这段描述会展示给模型，帮助它判断当前情境下是否应该调用此 Skill。

写得越清晰，模型触发 Skill 的时机就越准确。
:::

::: field name="metadata" type="Map<String, String>" optional
可选的键值对元数据，用于控制 Skill 的特殊行为。

目前支持的键：

|   **键**    |    **值**    |              **作用**              |
|:----------:|:-----------:|:--------------------------------:|
| `tlm-type` | `knowledge` | 将此 Skill 标记为知识库类型，触发时会启动 RAG 子对话 |

:::

::::

---

## 三、普通 Skill 与 knowledge 类型 Skill

### 普通 Skill

触发时，body 内容直接注入到当前对话上下文中。适合用于：

- 给模型注入角色扮演规则（“你现在是一名厨师……”）
- 给模型提供步骤指引（“当玩家要求你做某件事时，按以下步骤……”）
- 临时覆盖模型的默认行为

```markdown
---
name: cook-mode
description: 当玩家要求女仆下厨或者做饭时使用
---

你现在进入了厨师模式。请用活泼的语气回应玩家的烹饪相关请求，
可以介绍食材搭配、烹饪技巧，并为玩家推荐 Minecraft 中可以合成的食物。
```

### knowledge 类型 Skill（RAG）

::: tip 什么是 RAG？
RAG（Retrieval-Augmented Generation，检索增强生成）是一种让模型先从外部知识库中检索相关信息、再基于检索结果生成回答的技术。

对于体积较大的知识文档，直接全部塞进上下文既浪费 token 又可能超出模型的上下文窗口限制，RAG 可以先筛选出与当前问题相关的片段再注入。
:::

当 `metadata` 中设置 `tlm-type: knowledge` 时，触发该 Skill 后会启动一个**子对话**：

1. 模型先读取 `body` 内容（通常是对知识库的说明）
2. 模型再根据玩家当前使用的语言，读取 `references` 目录下对应的语言文件（如 `zh_cn.md`）；找不到时，`en_us.md` 的内容单独作为备选
3. 根据玩家的问题，从中提取最相关的信息，拼接在 `body` 之后返回给主对话

### references 目录

`references` 子目录下的文件使用**语言代码**命名，格式为 `<language_code>.md`，例如：

|  **文件名**   | **对应语言** |
|:----------:|:--------:|
| `zh_cn.md` |   简体中文   |
| `en_us.md` |    英语    |
| `ja_jp.md` |    日语    |

触发 Skill 时，模型会自动选取与当前玩家语言匹配的文件，将其内容与 `body` 拼接后作为知识源。这允许你为不同语言的玩家提供不同的本地化知识内容。

::: file-tree

- config
    - touhou_little_maid
        - skills
            - mod-encyclopedia
                - skill.md # body：对知识库的说明和指引
                - references
                    - zh_cn.md # 简体中文版的知识文档
                    - en_us.md # 英文版的知识文档

:::

对应的 `skill.md`：

```markdown
---
name: mod-encyclopedia
description: 当玩家询问关于本模组的详细信息、配方、或者机制说明时使用
metadata:
  tlm-type: knowledge
---

你是一个模组百科助手，请根据以下参考资料回答玩家的问题。
回答时请简洁明了，不需要引用原文，直接给出答案即可。
参考资料...
```

---

## 四、注意事项

::: warning

- `name` 字段重复时，数据包中的 Skill 会覆盖配置文件中的同名 Skill
- `name`、`description`、`body` 三个字段都不能为空，否则该文件会被跳过并在日志中输出警告
:::

::: tip
Skill 的 body 没有格式限制——可以是 Markdown 标题、列表、纯文本指令，甚至是伪代码。

模型通常能理解各种格式的文本，选择你觉得最清晰的写法即可。
:::
