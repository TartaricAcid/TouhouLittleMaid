---
title: 自定义 Tool
createTime: 2026/04/07 00:31:45
permalink: /wiki/dev/ai/tool/
icon: ri:tools-line
---

Tool（工具）是向模型暴露的一个可执行操作。

当模型判断需要执行某个游戏内操作时，它会发起一个 Function Call，游戏代码收到后调用对应 Tool 的执行逻辑，再把结果返回给模型，对话由此继续。

自 1.5.2 起，`ITool` 额外支持异步阻塞执行，适合处理外部查询、长耗时操作，同时还可以在工具调用期间给玩家展示不同的提醒内容，优化玩家的体验。

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

向 `root` 对象添加各字段即可。

`addProperties(name, parameter)` 默认将参数设为**必填**；如需设为可选，传入第三个参数
`false`：

```java
root.addProperties("optional_field", someParameter, false);
```

可用的参数类型见下方“参数类型一览”。
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

::: field name="onCallAsync(String toolCallId, T result, LLMCallback callback, LLMClient client)" type="CompletableFuture&lt;LLMCallback&gt;"

1.5.2 起可用，用于异步执行 Tool。

- 该接口实际调用了同步方法 `onCall(...)`，故修改此方法后，原有的同步实现将不再被调用
- 当 Tool 需要访问外部服务、执行长耗时任务，或者希望在完成后再继续下一轮对话时，可以重写此方法
- `client` 是当前使用的 LLM 客户端实例，必要时可以用它发起额外对话请求

:::

::: field name="invocationSummary(T result)" type="String"
生成本次调用的简短摘要，用于 UI 界面和历史记录展示。不会传给模型。

- 默认实现直接返回 `id()`，因此这不是必需覆写的方法
- 当 `invocationSummaryComponent(...)` 返回空 `Component` 时，会回退到这里的字符串摘要
  :::

::: field name="invocationSummaryComponent(T result)" type="Component"
自 1.5.2 起可用，用于添加可翻译、带样式的调用摘要。

- 返回**非空** `Component` 时，会覆盖 `invocationSummary(...)`
- 适合用于聊天气泡中的本地化文本展示，让玩家看到更自然的工具调用提示

:::

::::

## 三、1.5.2 新增能力示例

如果你的 Tool 需要执行较慢的查询任务，或者希望在聊天气泡中显示本地化摘要，可以像下面这样只覆写新增的方法：

```java
@Override
public CompletableFuture<LLMCallback> onCallAsync(
        String toolCallId, Result result, 
        LLMCallback callback, LLMClient client
) {
    // 这里依然在主线程，可以安全地访问游戏世界和实体状态
    EntityMaid maid = callback.getMaid();
    
    // 使用 runAsync 将耗时逻辑切离主线程，避免游戏画面或服务器卡死
    return CompletableFuture.runAsync(() -> {
        // 在这里执行你的耗时逻辑，比如查询 Wiki、调用外部 API 或进行复杂的路径计算
        // 但是注意，这里的代码不在主线程，注意多线程访问问题
        Thread.sleep(3000);
    })
    // 设置总计超时时间，防止外部请求挂起导致 AI 助手长时间无响应
    .orTimeout(5, TimeUnit.SECONDS)
    // 使用 handleAsync 无论前面的逻辑是成功、报错还是超时，都会执行此回调
    // 它是确保 CompletableFuture 一定能正确完成（Complete）的关键兜底
    .handleAsync((unused, throwable) -> {
        CompletableFuture<LLMCallback> future = new CompletableFuture<>();

        // 耗时任务结束或触发异常，必须回到主线程修改游戏世界或获取最终状态
        // 这是为了保证 Minecraft 线程安全，避免在异步线程直接操作
        callback.runOnServerThread(() -> {
            if (throwable != null) {
                // 如果异步逻辑中途出错（如超时），将错误信息反馈给模型，让其知道任务失败的原因
                future.complete(callback.addToolResult("Error message...", toolCallId));
            } else {
                // 逻辑顺利执行完毕，将最终结果写回 callback 中，让模型继续对话
                future.complete(callback.addToolResult("Success message...", toolCallId));
            }
        });

        return future;
    }, Runnable::run).thenCompose(f -> f); // 将嵌套的 Future 平铺，确保返回正确的泛型类型
}

@Override
public Component invocationSummaryComponent(Result result) {
    return Component.translatable("tool.mymod.my_tool.summary", result.action());
}
```

如果你不覆写 `onCallAsync(...)`，系统会自动回退到同步的 `onCall(...)`。

如果你不覆写 `invocationSummaryComponent(...)`，则会使用 `invocationSummary(...)` 返回的纯文本摘要。

### LLMCallback 工具方法

为了方便异步调用，或者刷新工具调用时的提示信息，`LLMCallback` 提供了以下实用方法：

- `isOnServerThread()`：判断当前代码是否在主线程执行
- `runOnServerThread(Runnable task)`：如果当前在主线程，直接执行 `task`；如果不在主线程，则将 `task` 提交到主线程执行
- `refreshWaitingChatBubble(Component summary)`：手动刷新玩家看到的聊天气泡提示（比如“正在查询 Wiki...”），需要注意此方法只能在主线程调用

## 四、参数类型一览

|       **类**        | **对应 JSON 类型** |                                         **常用方法**                                          |
|:------------------:|:--------------:|:-----------------------------------------------------------------------------------------:|
| `StringParameter`  |    `string`    | `setDescription()`, `addEnumValues()`, `setMinLength()`, `setMaxLength()`, `setPattern()` |
| `IntegerParameter` |   `integer`    |                           `setDescription()`, `addEnumValues()`                           |
| `NumberParameter`  |    `number`    |                                    `setDescription()`                                     |
|  `BoolParameter`   |   `boolean`    |                                    `setDescription()`                                     |
| `ObjectParameter`  |    `object`    |           `addProperties(name, param)`, `addProperties(name, param, required)`            |
|  `ArrayParameter`  |    `array`     |                                    `setDescription()`                                     |

所有参数类型均继承自 `Parameter`，支持 `setTitle()`、`setDescription()`、`setDefaultValue()`。

## 五、错误处理

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

## 六、注册 Tool

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

## 七、旧版 IFunctionCall

在 1.5.1 之前，附属开发者使用 `IFunctionCall` 接口来扩展女仆的 Function Call 能力，并通过 `registerAIFunctionCall()` 进行注册。

**自 1.5.1 起，`IFunctionCall` 接口和 `registerAIFunctionCall()` 方法已经废弃，并将在后续版本中移除。**

::: warning 请尽快迁移
如果你的附属仍在使用旧的 `IFunctionCall` 接口，请尽快将其迁移为 `ITool<T>`：

- 将 `IFunctionCall` 实现类改写为 `ITool<T>` 实现类
- 在 `ILittleMaid.registerAITool()` 中注册，而不是 `registerAIFunctionCall()`

:::

## 八、完整示例

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
