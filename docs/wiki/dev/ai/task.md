---
title: 工作模式适配
createTime: 2026/04/07 00:33:15
permalink: /wiki/dev/ai/task/
icon: ri:list-settings-line
---

`IMaidTask` 接口中有两个与 AI 聊天相关的方法，让工作模式可以更好地与 AI 的 Function Call 机制协作。

如果你的附属提供了自定义工作模式，建议根据实际情况实现这两个方法。

---

## 一、getMaidActionSummary()

```java
@ApiStatus.AvailableSince("1.5.1")
default String getMaidActionSummary() {
    return getUid().getPath();
}
```

`getMaidActionSummary()` 返回一段对当前工作模式的**英文摘要说明**，用于构建 `switch_work_task` Tool 的参数描述。

当模型收到可用工作模式列表时，列表中每个模式会附带一行说明：

```
- mymod:my_task: A short summary describing what this task does and when to use it
```

### 为什么需要覆写？

默认实现只返回 `getUid().getPath()`（即 ResourceLocation 的路径部分），例如 `my_task`。仅靠名称，模型往往无法判断这个任务的实际用途，可能会在不恰当的时机触发。

覆写后，可以明确告诉模型：

- 这个任务做什么
- 什么情况下应该切换到这个任务
- 有哪些前置条件或限制

### 写法建议

```java
@Override
public String getMaidActionSummary() {
    // 用英文，简洁清晰地描述任务用途和触发条件
    return """
        Mine ores and stone in a designated area.
        Requires a pickaxe in main hand.""";
}
```

::: tip
摘要应该面向模型而不是面向玩家，不需要翻译成中文，也不需要华丽的描述——清晰、准确即可。
:::

---

## 二、onFunctionCallSwitch()

```java
@ApiStatus.AvailableSince("1.4.7")
default FunctionCallSwitchResult onFunctionCallSwitch(EntityMaid maid) {
    return FunctionCallSwitchResult.OK;
}
```

`onFunctionCallSwitch()` 在模型通过 `switch_work_task` Tool 切换到此任务时被触发。

默认实现直接返回 `OK`，不做任何处理。如果你的任务需要特定的物品或前置条件，可以在这里检查并尝试自动准备。

### 返回值说明

|           枚举值           |      含义      |                          模型收到的结果                           |
|:-----------------------:|:------------:|:----------------------------------------------------------:|
|          `OK`           |  切换成功，一切就绪   |                  `Switched to task <id>`                   |
|       `NO_CHANGE`       | 已处于该任务，无需变更  |                   `Already on task <id>`                   |
| `MISSING_REQUIRED_ITEM` | 已切换，但缺少关键物品  |  `Switched to task <id>, but a required item is missing`   |
|      `PARTIAL_OK`       | 切换完成但仅部分满足条件 | `Switched to task <id>, but some requirements are missing` |

### 示例：检查主手是否有合适的工具

```java
@Override
public FunctionCallSwitchResult onFunctionCallSwitch(EntityMaid maid) {
    // 检查主手是否有镐子
    ItemStack mainHand = maid.getMainHandItem();
    if (!mainHand.isEmpty() && mainHand.getItem() instanceof PickaxeItem) {
        return FunctionCallSwitchResult.OK;
    }

    // 尝试从背包中找到镐子并装备
    for (int i = 0; i < maid.getAvailableSlots(SlotType.HAND).size(); i++) {
        ItemStack stack = maid.getItemBySlot(EquipmentSlot.MAINHAND);
        // ... 背包搜索逻辑 ...
    }

    // 找不到合适的工具
    return FunctionCallSwitchResult.MISSING_REQUIRED_ITEM;
}
```

::: warning
`onFunctionCallSwitch()` 在**服务端**被调用，执行逻辑时请不要修改客户端状态，也不要做耗时操作。这个钩子是同步的，会直接影响对话回复的延迟。
:::

---

## 三、完整示例

下面是一个综合了两个方法的自定义工作模式片段：

```java
public class MiningTask implements IMaidTask {
    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("mymod", "mining");
    }

    // ... 其他必要方法 ...

    @Override
    public String getMaidActionSummary() {
        return "Mine ores and stone blocks in a defined area. "
                + "Requires a pickaxe in main hand to work. "
                + "Use this when the user wants the maid to dig or mine.";
    }

    @Override
    public FunctionCallSwitchResult onFunctionCallSwitch(EntityMaid maid) {
        ItemStack mainHand = maid.getMainHandItem();
        if (isPickaxe(mainHand)) {
            return FunctionCallSwitchResult.OK;
        }

        // 尝试从背包找镐子
        boolean found = tryEquipPickaxeFromInventory(maid);
        if (found) {
            return FunctionCallSwitchResult.OK;
        }

        return FunctionCallSwitchResult.MISSING_REQUIRED_ITEM;
    }

    private boolean isPickaxe(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof PickaxeItem;
    }

    private boolean tryEquipPickaxeFromInventory(EntityMaid maid) {
        // 实现背包搜索逻辑...
        return false;
    }
}
```
