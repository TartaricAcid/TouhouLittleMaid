---
title: AI 聊天功能概述
createTime: 2026/04/07 00:30:11
permalink: /wiki/dev/ai/overview/
icon: ri:robot-2-line
---

1.5.1 版本为女仆模组带来了全新的 AI 聊天架构。相比之前的版本，这套系统的设计更加系统化，也给附属开发者提供了丰富的扩展点。

在此基础上，1.5.2 进一步补充了 `ITool` 的异步执行能力，支持异步阻塞执行后，再继续进行 LLM 对话。

在开始之前，有必要先解释几个核心概念。因为这套设计深度借鉴了当代大语言模型（LLM）的使用范式，如果你之前没有接触过相关内容，可能会感到陌生。

## 一、核心概念

如果你已经了解 LLM、Function Call、RAG 等概念，可以跳过这一节。

::: card title="LLM（大语言模型）" icon="ri:brain-line"

女仆的 AI 聊天功能依赖于外部的大语言模型（Large Language Model），例如 GPT、DeepSeek 等，玩家需要自行配置 API 才能使用。

女仆模组本身不包含任何 AI 模型，只负责跟模型“沟通”：告诉它女仆现在的状态是什么，让它决定该说什么话、执行什么行为。
:::

::: card title="Function Call（工具调用）" icon="ri:function-line"

现代 LLM 支持一种叫做 Function Call（也叫 Tool Call）的机制：开发者可以向模型注册若干“工具”，每个工具有自己的名字、描述和参数定义。

当模型认为需要执行某个操作时，它会返回一个结构化的调用请求。游戏代码收到后执行对应逻辑，再把结果返回给模型，模型继续生成后续内容。

这套机制让女仆可以真正“做事”：比如切换工作模式、跟随玩家，而不只是聊天。
:::

::: card title="Skill（技能/提示词包）" icon="ri:book-open-line"

如果说 Tool 是女仆能做的动作，那么 Skill 就是让模型“知道该怎么用这些动作”的说明书。

每个 Skill 本质上是一段文本，写在 Markdown 文件里。在被触发时会注入到对话上下文中，告诉模型当前应当遵循哪些行为规范或执行哪些步骤。
:::

::: card title="Context（上下文）" icon="ri:database-2-line"

模型在回应时，需要了解女仆和世界的当前状态——比如血量、所处的维度、附近有哪些生物等。这些信息统称为“上下文”。

为了避免一次性把所有信息都塞给模型（既浪费 token 又容易混乱），上下文被按分类组织。

部分常用信息会自动注入在玩家发送信息里，其余信息由模型通过 `query_game_context` Tool 按需查询。
:::


## 二、内置 Tool

模组默认注册了以下几个 Tool，覆盖了最常用的数个女仆功能：

|      **Tool ID**       |            **功能描述**            |
|:----------------------:|:------------------------------:|
|      `use_skill`       | 触发一个已注册的 Skill，执行对应的提示词注入或知识检索 |
|  `query_game_context`  |    按分类查询游戏上下文（如装备、位置、附近实体等）    |
| `switch_follow_state`  |          切换女仆的跟随/驻守状态          |
|   `switch_work_task`   |     切换女仆的工作模式（支持攻击任务的目标指定）     |
|   `switch_schedule`    |     切换女仆的日程安排（白天/夜晚/全天工作）      |
|      `switch_sit`      |           切换女仆的坐/站状态           |
| `query_minecraft_wiki` |      查询 Minecraft Wiki 资料      |


## 三、内置 Context

模组默认注册了以下几个上下文分类。

标记为“自动注入”的分类会在每条用户消息中以 `<context>` 标签包裹后自动追加，无需模型主动查询：

|     **分类 ID**     |      **内容说明**       | **注入方式** |
|:-----------------:|:-------------------:|:--------:|
|     `status`      | 女仆自身状态（血量、工作模式、日程等） |   自动注入   |
|      `world`      | 世界状态（时间、天气、维度、生物群系） |   自动注入   |
|    `equipment`    |       装备与背包物品       |   按需查询   |
|      `user`       |  玩家信息（姓名、血量、主手物品等）  |   按需查询   |
|     `effects`     |      女仆当前的状态效果      |   按需查询   |
|    `position`     |     女仆与玩家的坐标和距离     |   按需查询   |
| `nearby_entities` |  附近的生物列表（最多 20 个）   |   按需查询   |


## 四、扩展入口

作为附属开发者，你可以通过实现 `ILittleMaid` 接口来扩展 AI 聊天功能。

```java
@LittleMaidExtension
public class LittleMaidCompat implements ILittleMaid {
    /**
     * 注册自定义 Tool，让模型可以执行你定义的游戏内操作
     */
    @Override
    public void registerAITool(ToolRegister register) {
        register.register(new MyCustomTool());
    }

    /**
     * 注册自定义上下文，让模型可以查询到更多游戏信息
     */
    @Override
    public void registerAIMaidContext(GameContextRegister register) {
        // 注册一个新的上下文分类 "my_mod"，并提供对应的查询实现 MyCustomContext
        register.registerCategory("my_mod", "My mod related information", false);
        // Context 必须依赖某个分类        
        register.registerContext("my_mod", new MyCustomContext());
    }
}
```

::: warning 注意
`registerAIFunctionCall` 方法自 1.5.1 起已经废弃，请改用 `registerAITool` 和 Skill 机制。
:::

各扩展点的详细说明请参考后续章节：

- [编写 Skill](/wiki/dev/ai/skill/)：如何通过 Markdown 文件为女仆编写行为指引
- [自定义 Tool](/wiki/dev/ai/tool/)：如何让模型执行你的自定义操作
- [自定义 Context](/wiki/dev/ai/context/)：如何向模型暴露更多游戏信息
