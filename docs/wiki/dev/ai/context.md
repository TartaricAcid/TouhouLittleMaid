---
title: 自定义 Context
createTime: 2026/04/07 00:32:30
permalink: /wiki/dev/ai/context/
icon: ri:database-2-line
---

Context（上下文）是女仆当前状态和游戏世界信息的结构化描述，供模型在生成回复时参考。

每个 Context 项是一个键值对，例如 `"Self health" → "15.0/20.0 (max)"`。

通过注册自定义 Context，你可以让模型在需要时查询到你的模组所提供的游戏信息。


## 一、Context 的两种注入方式

Context 分为两种注入方式，由所属分类（Category）的 `promptContext` 属性决定：

### 自动注入（promptContext = true）

每次玩家向女仆发送消息时，属于“自动注入分类”的所有 Context 项会被**自动追加**到玩家消息的头部，用 `<context>`
标签包裹，无需模型主动请求：

```
<context>
- Self health: 15.0/20.0 (max)
- Is following: yes
- Schedule: DAY
...
</context>
玩家的实际消息内容
```

::: warning 谨慎使用自动注入
自动注入的 Context 在**每一轮**对话中都会消耗 token。如果注入的内容过多或与对话无关，不仅浪费费用，还会“污染”
模型的上下文，导致它在不恰当的时机做出错误决策。

只有满足以下条件的 Context 才适合设为自动注入：

- 内容**简短**（几行以内）
- 几乎每轮对话都**会被用到**
- 对模型理解当前情境**至关重要**

:::

### 按需查询（promptContext = false）

该分类的 Context 项不会自动添加，只有当模型主动调用 `query_game_context` Tool 并指定分类 ID 时，才会返回该分类下的所有
Context 项。


## 二、Category 的设计意图

Context 必须先归属于一个**分类（Category）**，才能被注册和使用，这个设计是刻意为之的。

模型在查询上下文时，是以**分类**为单位进行的，而不是逐条查询。将相关的 Context 项归到同一分类，可以让模型在一次 Tool
调用中获取一组语义相关的信息（如“装备”分类下的主手、副手、盔甲），而不是把所有信息堆在一起让模型自己筛选。

这样做的好处：

- 每次注入的信息量可控，避免无关信息干扰模型判断
- 模型可以根据当前对话的需要，有选择地查询某个分类
- 分类的摘要（`categorySummary`）帮助模型理解这个分类里有什么，判断是否需要查询

::: tip
**必须先调用 `registerCategory()` 注册分类，才能调用 `registerContext()` 注册 Context 项。**

顺序颠倒会抛出异常。
:::


## 三、实现 IMaidContext

每个 Context 项都需要实现 `IMaidContext` 接口。推荐继承 `AbstractMaidContext` 以省去 `key()` 和 `label()` 的实现：

```java
public class MyModLevelContext extends AbstractMaidContext {
    public MyModLevelContext() {
        // 第一个参数是唯一 key，第二个是展示给模型看的 label（建议英文）
        super("mymod_level", "My mod level");
    }

    @Override
    public String getValue(EntityMaid maid) {
        // 返回当前值，内容会以 "- My mod level: <value>" 的格式传给模型
        int level = MyModCapability.getLevel(maid);
        return String.valueOf(level);
    }
}
```

### 接口方法说明

:::: field-group

::: field name="key()" type="String" required
Context 项的唯一键。

- 全局唯一，不能与其他已注册的 Context 重复（重复会抛出异常）
- 不暴露给模型，仅用于内部寻址
  :::

::: field name="label()" type="String" required
Context 项的展示标签，会以 `"- <label>: <value>"` 的格式传给模型。

- 建议使用英文，大多数模型对英文标签的理解效果更好
- 应简洁且语义明确，例如 `"Self health"`、`"Current task"`
  :::

::: field name="getValue(EntityMaid maid)" type="String" required
获取 Context 项的当前值。

- 在每次对话时被调用，应尽量轻量，避免复杂计算
:::

::::


## 四、注册 Context

在 `ILittleMaid` 实现类中重写 `registerAIMaidContext()`，**先注册分类，再注册 Context 项**：

```java
@LittleMaidExtension
public class MyModCompat implements ILittleMaid {
    @Override
    public void registerAIMaidContext(GameContextRegister register) {
        // 第一步：注册分类
        // 参数：分类 ID、分类摘要（给模型看）、是否为自动注入分类
        register.registerCategory("mymod_status", "My mod related status", false);

        // 第二步：将 Context 项注册到已存在的分类中
        register.registerContext("mymod_status", new MyModLevelContext());
        register.registerContext("mymod_status", new MyModOtherContext());
    }
}
```

### registerCategory 参数说明

:::: field-group

::: field name="categoryId" type="String" required
分类的唯一标识符。建议加上模组 ID 前缀，例如 `mymod_status`。
:::

::: field name="categorySummary" type="String" required
分类的摘要描述，展示给模型用于判断是否需要查询这个分类（仅对 `promptContext = false` 的分类有效，自动注入的分类不会出现在工具列表中）。

建议用英文，例如 `"My mod maid status: level and fatigue"`。
:::

::: field name="promptContext" type="boolean" required

- `true`：该分类的所有 Context 项在每条用户消息中自动注入（以 `<context>` 标签包裹），不出现在 `query_game_context` 的可选分类列表中
- `false`：仅当模型主动通过 `query_game_context` Tool 查询时才提供，分类摘要会显示在工具的可选列表中
  :::

::::


## 五、完整示例

下面是一个完整示例，假设你的模组提供“女仆等级”和“女仆疲劳度”两个状态。等级和疲劳度在对话中经常被引用，因此设为自动注入：

```java
// Context 项 1：女仆等级
public class MaidLevelContext extends AbstractMaidContext {
    public MaidLevelContext() {
        super("mymod_maid_level", "Maid level");
    }

    @Override
    public String getValue(EntityMaid maid) {
        return String.valueOf(MyModData.getLevel(maid));
    }
}

// Context 项 2：女仆疲劳度
public class MaidFatigueContext extends AbstractMaidContext {
    public MaidFatigueContext() {
        super("mymod_fatigue", "Fatigue");
    }

    @Override
    public String getValue(EntityMaid maid) {
        int fatigue = MyModData.getFatigue(maid);
        int maxFatigue = MyModData.getMaxFatigue(maid);
        return "%d/%d".formatted(fatigue, maxFatigue);
    }
}

// 注册
@LittleMaidExtension
public class MyModCompat implements ILittleMaid {
    @Override
    public void registerAIMaidContext(GameContextRegister register) {
        // promptContext = true：等级和疲劳度较简短，且对话中经常涉及，设为自动注入
        register.registerCategory("mymod_status", "My mod maid status (level and fatigue)", true);
        register.registerContext("mymod_status", new MaidLevelContext());
        register.registerContext("mymod_status", new MaidFatigueContext());
    }
}
```

注册后，每次玩家发送消息时，模型收到的消息前会自动附上：

```
<context>
- Maid level: 5
- Fatigue: 30/100
</context>
```
