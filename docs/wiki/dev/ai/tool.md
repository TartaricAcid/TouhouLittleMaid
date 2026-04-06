---
title: 自定义 Tool
createTime: 2026/04/07 00:31:45
permalink: /wiki/dev/ai/tool/
icon: ri:tools-line
---

Tool（工具）是向模型暴露的一个可执行操作。

当模型判断需要执行某个游戏内操作时，它会发起一个 Function Call，游戏代码收到后调用对应 Tool 的执行逻辑，再把结果返回给模型，对话由此继续。

---

## 一、实现 ITool 接口

所有自定义 Tool 都需要实现 `ITool<T>` 接口，其中泛型 `T` 是该 Tool 的**参数对象类型**（即模型传入的参数被解码后的 Java
类型）。

```java
public class MyTool implements ITool<MyTool.Result> {
    // 参数解码器：使用 Mojang Codec 描述如何将模型传入的 JSON 参数解析为 Result
    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("action").forGetter(Result::action)
            ).apply(instance, Result::new));

    @Override
    public String id() {
        // Tool 的唯一标识，建议用小写字母、数字和下划线，语义化命名
        return "my_tool";
    }

    @Override
    public String summary(EntityMaid maid) {
        // 告诉模型这个 Tool 的用途，以及何时应该调用它
        // 用英文写，模型理解效果更好
        return "Use this tool when the user asks you to do something special.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        // 描述这个 Tool 接受哪些参数
        StringParameter action = StringParameter.create()
                .setDescription("The action to perform")
                .addEnumValues("greet", "farewell");
        root.addProperties("action", action);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolCallId, Result result, LLMCallback callback) {
        // 执行 Tool 的实际逻辑
        String message = switch (result.action()) {
            case "greet" -> "Hello! I greeted the player.";
            case "farewell" -> "Goodbye! I said farewell to the player.";
            default -> ITool.invalidParam(
                        "action", 
                        List.of("greet", "farewell"),
                        "Unknown action: " + result.action()
                    );
        };
        // 必须调用 addToolResult() 将结果返回给模型，否则对话无法继续
        return callback.addToolResult(message, toolCallId);
    }

    @Override
    public String invocationSummary(Result result) {
        // 用于 UI 或历史记录中显示本次调用的简短摘要
        return "my_tool { " + result.action() + " }";
    }

    // 参数对象，推荐使用 record
    public record Result(String action) {}
}
```

---

## 二、接口方法说明

:::: field-group

::: field name="id()" type="String" required
Tool 的唯一标识符。

- 建议仅使用小写字母、数字和下划线
- 不能与其他已注册的 Tool 重复
- 会作为 Function Call 的名称传给模型，因此要有语义，便于模型理解和调试
  :::

::: field name="summary(EntityMaid maid)" type="String" required
返回对该 Tool 的简短说明，帮助模型判断是否应该调用它。

- **建议用英文书写**，大多数模型对英文提示词的理解效果更好
- 应清楚说明：这个 Tool 做什么、什么情况下应该用、什么情况下不该用
  :::

::: field name="parameters(ObjectParameter root, EntityMaid maid)" type="Parameter" required
构建该 Tool 的参数定义（JSON Schema 风格）。

向 `root` 对象添加各字段即可。`addProperties(name, parameter)` 默认将参数设为**必填**；如需设为可选，传入第三个参数
`false`：

```java
root.addProperties("optional_field", someParameter, false);
```

可用的参数类型见下方"参数类型一览"。
:::

::: field name="codec()" type="Codec<T>" required
返回用于将模型传入的 JSON 参数解码为 `T` 类型对象的 Codec。

通常使用 `RecordCodecBuilder` 构建，与参数定义中的字段名保持一致。
:::

::: field name="onCall(String toolCallId, T result, LLMCallback callback)" type="LLMCallback" required
Tool 被调用时的实际执行逻辑。

- `toolCallId`：模型发来的调用 ID，需要原样传给 `addToolResult()`
- `result`：解码后的参数对象
- `callback`：当前回调，通过它返回结果并获取 `EntityMaid` 实例

**必须**在此方法中调用 `callback.addToolResult(message, toolCallId)` 并返回，否则对话会卡住。

返回的 `LLMCallback` 可以是原样传入的（大多数情况），也可以是新建的（需要特殊控制流时）。
:::

::: field name="invocationSummary(T result)" type="String" required
生成本次调用的简短摘要，用于 UI 界面和历史记录展示。不会传给模型。
:::

::: field name="trigger(EntityMaid maid, ChatCompletion chatCompletion)" type="boolean" optional
动态控制该 Tool 是否在当前上下文下暴露给模型。默认返回 `true`（始终暴露）。

如果你的 Tool 只在特定条件下才有意义（例如只有装备了某件物品时才可用），可以在这里判断并返回 `false` 以避免模型错误触发。
:::

::::

---

## 三、参数类型一览

|       **类**        | **对应 JSON 类型** |                                           **常用方法**                                           |
|:------------------:|:--------------:|:--------------------------------------------------------------------------------------------:|
| `StringParameter`  |    `string`    | `setDescription()`, `addEnumValues()`, `setMinLength()`, `setMaxLength()`,    `setPattern()` |
| `IntegerParameter` |   `integer`    |                            `setDescription()`, `addEnumValues()`                             |
| `NumberParameter`  |    `number`    |                                      `setDescription()`                                      |
|  `BoolParameter`   |   `boolean`    |                                      `setDescription()`                                      |
| `ObjectParameter`  |    `object`    |             `addProperties(name, param)`, `addProperties(name, param, required)`             |
|  `ArrayParameter`  |    `array`     |                                      `setDescription()`                                      |

所有参数类型均继承自 `Parameter`，支持 `setTitle()`、`setDescription()`、`setDefaultValue()`。

---

## 四、静态工具方法 `invalidParam`

当模型传入了无效参数时，可以用 `ITool.invalidParam()` 生成一条规范的错误提示，要求模型重新生成调用：

```java
// 当参数值不在允许范围内时：
String msg = ITool.invalidParam(
    "action",                             // 参数名
    List.of("greet", "farewell"),         // 合法值列表
    "Unknown action: " + result.action()  // 出错原因
);
return callback.addToolResult(msg, toolCallId);
```

生成的消息格式为：

```
Invalid parameter: Unknown action: xxx. 
Correct usage: action: choose one of [greet, farewell]
```

---

## 五、注册 Tool

在实现了 `ILittleMaid` 接口的扩展类中，重写 `registerAITool()` 方法：

```java
@LittleMaidExtension
public class MyModCompat implements ILittleMaid {
    @Override
    public void registerAITool(ToolRegister register) {
        register.register(new MyTool());
    }
}
```

::: warning
Tool 的 `id()` 必须全局唯一。

如果与内置 Tool 或其他附属的 Tool 重名，后注册的会覆盖先注册的（具体顺序取决于加载顺序，不要依赖此行为）。

:::

---

## 六、关于已废弃的 IFunctionCall

在 1.5.1 之前，附属开发者使用 `IFunctionCall` 接口来扩展女仆的 Function Call 能力，并通过 `registerAIFunctionCall()` 进行注册。

**自 1.5.1 起，`IFunctionCall` 接口和 `registerAIFunctionCall()` 方法已经废弃，并将在后续版本中移除。**

::: warning 请尽快迁移
如果你的附属仍在使用旧的 `IFunctionCall` 接口，请尽快将其迁移为 `ITool<T>`：

- 将 `IFunctionCall` 实现类改写为 `ITool<T>` 实现类
- 在 `ILittleMaid.registerAITool()` 中注册，而不是 `registerAIFunctionCall()`

:::

---

## 七、完整示例

下面是一个实际可用的示例 Tool，功能是根据女仆的主手物品向玩家说一句话：

::: tabs

@tab 1.20.1

```java
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class DescribeMainHandTool implements ITool<DescribeMainHandTool.Result> {
    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("style").forGetter(Result::style)
            ).apply(instance, Result::new));

    @Override
    public String id() {
        return "describe_main_hand";
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Describe the item in the maid's main hand with a given speaking style.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter style = StringParameter.create()
                .setDescription("The speaking style")
                .addEnumValues("formal", "casual", "poetic");
        root.addProperties("style", style);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolCallId, Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        ItemStack stack = maid.getMainHandItem();

        String itemName = stack.isEmpty() ? "nothing"
                : ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();

        String msg = "Main hand item: %s. Describe it in %s style.".formatted(itemName, result.style());
        return callback.addToolResult(msg, toolCallId);
    }

    @Override
    public String invocationSummary(Result result) {
        return "describe_main_hand { " + result.style() + " }";
    }

    public record Result(String style) {}
}
```

@tab 1.21.1

```java
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.LLMCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DescribeMainHandTool implements ITool<DescribeMainHandTool.Result> {
    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("style").forGetter(Result::style)
            ).apply(instance, Result::new));

    @Override
    public String id() {
        return "describe_main_hand";
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Describe the item in the maid's main hand with a given speaking style.";
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        StringParameter style = StringParameter.create()
                .setDescription("The speaking style")
                .addEnumValues("formal", "casual", "poetic");
        root.addProperties("style", style);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public LLMCallback onCall(String toolCallId, Result result, LLMCallback callback) {
        EntityMaid maid = callback.getMaid();
        ItemStack stack = maid.getMainHandItem();

        String itemName = stack.isEmpty() ? "nothing"
                : BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();

        String msg = "Main hand item: %s. Describe it in %s style.".formatted(itemName, result.style());
        return callback.addToolResult(msg, toolCallId);
    }

    @Override
    public String invocationSummary(Result result) {
        return "describe_main_hand { " + result.style() + " }";
    }

    public record Result(String style) {}
}
```

:::

两版本的唯一差异在于物品注册表的访问方式：

- 1.20.1（Forge）：`ForgeRegistries.ITEMS.getKey(item)`
- 1.21.1（NeoForge）：`BuiltInRegistries.ITEM.getKey(item)`
