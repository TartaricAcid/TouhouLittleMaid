# 自定义 JavaScript 动画
通过 JavaScript 文件，你可以为女仆或坐垫添加自定义动画。

- 本 wiki 适用于 Touhou Little Maid 模组 `1.12.2` 和 `1.16.5` 版本；
- 需要对 JavaScript 基本语法有简单的了解；
- 需要高中及以上数学知识，尤其是对三角函数和极坐标的理解。
- 文本编辑软件推荐 VSCode，相关文本文件均需要用 UTF-8 无 BOM 编码进行存储。

## 基本格式

动画脚本放置在文件夹任意位置均可，只需要在对应模型字段处声明动画文件位置即可。 我建议将它放入 `animation` 文件夹中。

下面是通用模板：

```js
// 当你需要使用 GlWrapper 时才需要声明这一段代码
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
     /**
     * @param entity 需要应用动画的实体对象
     * @param limbSwing 实体在行走过程中的速度（可以理解为汽车的速度表）
     * @param limbSwingAmount 实体行走的总里程数（可以理解为汽车的里程表）
     * @param ageInTicks 实体的 tick 时间，一个从 0 开始一直增加的数值
     * @param netHeadYaw 实体头部的偏航
     * @param headPitch 实体头部的俯仰
     * @param scale 实体缩放参数，默认为 0.0625，未发现任何使用的地方
     * @param modelMap 为一个 map，存储了该模型所有的骨骼
     */
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // 相关动画的书写
    }
})
```

这里我们举一个简单的例子，当前模型有一个带有名为 `rotation` 的骨骼，我们想要把让这个骨骼绕着 X 轴持续的做旋转运动，运动的速度大约为每 tick 1 度（也就是 18 秒转一圈），我们可以这样写动画。

```javascript
// 当你需要使用 GlWrapper 时才需要声明这一段代码
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // 先从 modelMap 中尝试获取名为 rotation 的骨骼
        rotation = modelMap.get("rotation");
        // 以防万一，我们做个简单的判定，确保此骨骼一定存在
        if (rotation != undefined) {
            // 通过骨骼的 setRotateAngleX 函数设置其 X 轴角度
            // ageInTicks 为实体的 tick 时间，一个从 0 开始一直增加的数值
            // 通过取余运算（也就是 % 符号）将这个数限定在 0~360 之间
            // 因为该方法只接收弧度值，所以需要乘以 0.017453292 转换成对应弧度
            // 这样我们就实现了每 tick 旋转 1 度的动画
            rotation.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }
    }
})
```

现在我们再进行一个更加复杂的运动，我们有一个名为 `wing` 的骨骼，我们想要其能够持续的来回摆动。

摆动围绕 Y 轴，摆动角度在 `-20°~40°` 之间，每 5 秒做一次完整的往复运动。

这一块恰好需要用到高中所学的三角函数知识，这一块选取正弦或者余弦均可，我们使用正弦函数。

```javascript
// 当你需要使用 GlWrapper 时才需要声明这一段代码
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
        headPitch, scale, modelMap) {
        // 先从 modelMap 中尝试获取名为 wing 的骨骼
        wing = modelMap.get("wing");
        // 以防万一，我们做个简单的判定，确保此骨骼一定存在
        if (wing != undefined) {
            // 每 5 秒完整的往复一次，也就是 100 tick
            // 通过乘法和求余来实现这个功能
            var time = (ageInTicks * 3.6) % 360;
            // 这一块调用了 JavaScript 的 Math 函数
            // 构建正弦函数，获得数值为 -20~40 的周期函数
            var func = 30 * Math.sin(time * 0.017453292) + 10;
            // 最后进行参数的应用
            wing.setRotateAngleY(func * 0.017453292);
        }
    }
})
```

其他复杂的运动均可通过相关函数来实现。

## 游戏内热重载功能

因为干巴巴的函数式并不能确定该动画是否表现正确，我们添加了游戏内的动画热重载功能。

加载模型资源包后，只需使用以下命令即可重新加载所有动画数据。
- 1.12.2: `/maid_res reload`
- 1.16.5: `/tlm pack reload`

## 函数文档

删除线样式的表示此函数已在 1.16 中被废弃，你可以访问[此处连接](https://github.com/TartaricAcid/TouhouLittleMaid/tree/1.16.5/src/main/java/com/github/tartaricacid/touhoulittlemaid/api/animation)查看源码 API。

### 实体参数

依据附加动画的对象不同，`entity` 参数可用的函数也不相同。

#### 女仆

|             函数名              |    返回值     |                                        备注                                        |
|:----------------------------:|:----------:|:--------------------------------------------------------------------------------:|
|        `hasHelmet()`         | `boolean`  |                                女仆穿戴头盔后，返回 `true`                                 |
|      `hasChestPlate()`       | `boolean`  |                                女仆穿戴胸甲后，返回 `true`                                 |
|       `hasLeggings()`        | `boolean`  |                                女仆穿戴护腿后，返回 `true`                                 |
|         `hasBoots()`         | `boolean`  |                                女仆穿戴靴子后，返回 `true`                                 |
|        `getHelmet()`         |  `String`  |                              当女仆佩戴头盔后，返回头盔物品的注册名称。                               |
|      `getChestPlate()`       |  `String`  |                              当女仆佩戴胸甲后，返回胸甲物品的注册名称。                               |
|       `getLeggings()`        |  `String`  |                              当女仆佩戴护腿后，返回护腿物品的注册名称。                               |
|         `getBoots()`         |  `String`  |                              当女仆佩戴靴子后，返回头盔物品的注册名称。                               |
|        `isBegging()`         | `boolean`  |                                    女仆是否处于祈求模式                                    |
|      `isSwingingArms()`      | `boolean`  |             If maid is using arms, this function will return `true`              |
|     `getSwingProgress()`     |  `float`   |                             Get maid's swinging time                             |
|         `isRiding()`         | `boolean`  |                                    女仆是否处于骑乘模式                                    |
|        `isSitting()`         | `boolean`  |                                    女仆是否处于待命模式                                    |
|    ~~`isHoldTrolley()`~~     | `boolean`  |                                 女仆是否携带手推车或其他实体。                                  |
| ~~`isRidingMarisaBroom()`~~  | `boolean`  |                       Whether maid is riding Marisa Broom                        |
|    ~~`isRidingPlayer()`~~    | `boolean`  |                          Whether maid is riding player                           |
|    ~~`isHoldVehicle()`~~     | `boolean`  |                          Whether maid is riding vehicle                          |
| ~~`isPortableAudioPlay()`~~  | `boolean`  |                 Whether the maid hold portable audio and play it                 |
|       `hasBackpack()`        | `boolean`  |                          Whether maid wearing backpack                           |
|     `getBackpackLevel()`     |   `int`    |                            Get maid's backpack level                             |
|     ~~`hasSasimono()`~~      | `boolean`  |                          Whether maid wearing sasimono                           |
|     `isSwingLeftHand()`      | `boolean`  | Whether the maid is swinging left or right arm, return `false` if it's the right |
| ~~`getLeftHandRotation()`~~  | `float[3]` |                          Get the left arm rotation data                          |
| ~~`getRightHandRotation()`~~ | `float[3]` |                         Get the right arm rotation data                          |
|        ~~`getDim()`~~        |   `int`    |                      Get the dimension where the maid is in                      |
|         `getWorld()`         |  `World`   |                              Get maid's world data                               |
|         `getTask()`          |  `String`  |                Get maid's task, such as `attack`, `ranged_attack`                |
|     `hasItemMainhand()`      | `boolean`  |                          Whether maid has mainhand item                          |
|      `hasItemOffhand()`      | `boolean`  |                          Whether maid has offhand item                           |
|     `getItemMainhand()`      |  `String`  |                      Get maid mainhand item's registry name                      |
|      `getItemOffhand()`      |  `String`  |                      Get maid offhand item's registry name                       |
|         `inWater()`          | `boolean`  |                              Whether maid in water                               |
|          `inRain()`          | `boolean`  |                               Whether maid in rain                               |
|        `getAtBiome()`        |  `String`  |                          Get maid's biome register name                          |
|    ~~`getAtBiomeTemp()`~~    |  `String`  |                        Get maid's biome temperature enum                         |
|          `onHurt()`          | `boolean`  |                           Whether the maid is on hurt                            |
|        `getHealth()`         |  `float`   |                                     获取女仆的生命值                                     |
|       `getMaxHealth()`       |  `float`   |                                    获取女仆的最大生命值                                    |
|         `isSleep()`          | `boolean`  |                              Whether maid is sleep                               |
|     `getFavorability()`      |   `int`    |                           Get the maid's favorability                            |
|        `isOnGround()`        | `boolean`  |                          Whether the maid is on ground                           |
|      `getArmorValue()`       |  `double`  |                              Get maid's armor value                              |
|         `getSeed()`          |   `long`   |                          获取一个固定值，每个实体都是不同的，类似于该实体的 UUID                          |

#### 坐垫

|          函数名          |    返回值    |               备注               |
|:---------------------:|:---------:|:------------------------------:|
|  `isRidingPlayer()`   | `boolean` |           坐垫是否被玩家所坐            |
|   `hasPassenger()`    | `boolean` |            坐垫是否有乘客             |
|  `getPassengerYaw()`  |  `float`  |   Get chair passenger's yaw    |
|      `getYaw()`       |  `float`  |        Get chair's yaw         |
| `getPassengerPitch()` |  `float`  |  Get chair passenger's pitch   |
|    ~~`getDim()`~~     |   `int`   |       Get chair's dim id       |
|     `getWorld()`      |  `World`  |     Get chair's world data     |
|      `getSeed()`      |  `long`   | 获取一个固定值，每个实体都是不同的，类似于该实体的 UUID |

#### 世界
|       函数名        |    返回值    |           备注            |
|:----------------:|:---------:|:-----------------------:|
| `getWorldTime()` |  `long`   | 获取当前世界的时间（tick，0-24000） |
|    `isDay()`     | `boolean` |        当前世界是否为白天        |
|   `isNight()`    | `boolean` |        当前世界是否为黑夜        |
|  `isRaining()`   | `boolean` |        当前世界是否在降雨        |
| `isThundering()` | `boolean` |       当前世界是否处于雷雨中       |



### `limbSwing` 和 `limbSwingAmount` 参数

这些都是浮点数，`limbSwing`是实体的行走速度（可以将其视为汽车的速度计），`limbSwingAmount`是实体的总行走距离（可以将其视为汽车的里程表）。

这两个数据主要用于腿部和肢体的旋转，Minecraft原版使用这两个基本数据来计算手臂和腿部摆动的动画。

`Math.cos(limbSwing * 0.6662) * limbSwingAmount`(left hand)

`-Math.cos(limbSwing * 0.6662) * limbSwingAmount`(right hand)

`Math.cos(limbSwing * 0.6662) * limbSwingAmount * 1.4`(left leg)

`-Math.cos(limbSwing * 0.6662) * limbSwingAmount * 1.4`(right leg)

改变值`0.6662`将控制摆动的频率，乘以公式的系数 (例如，腿部使用`1.4`作为系数) 来改变摆动的幅度。

使用原版Minecraft的手臂和腿摆动公式可以使摆动动画更加自然。



### `ageInTicks` 参数

浮点数，一个从 0 开始每 tick 都会自加的变量，大多数动画中都会用到的自变量。



### `netHeadYaw` 和 `headPitch` 参数

二者均为浮点数，单位为角度（这是原版 Minecraft 所设计的）。

通常此参数可以用作旋转角度，你只需要将其更改为弧度。

```javascript
head.setRotateAngleX(headPitch * 0.017453292);
head.setRotateAngleY(netHeadYaw * 0.017453292);
```

> 如果在此部分设置的系数大于'0.017453292'，可能会出现一些错误问题。


### `scale` 参数

浮点数，固定为 0.0625。

一个含义不明的值。



### modelMap 参数

一个使用字符串作为键的保存组的 Map。

您可以通过`modelMap.get("xxx")`获取相应的组。 如果没有与名称匹配的组，返回`undefined`

假设我们想获取目标组`head`：

```javascript
head = modelMap.get("head");
```
然后我们可以使用这个`head`组来设置各种参数，制作动画。

当然，为了预防起见，最好对该组进行检查，以确保它存在。

```javascript
head = modelMap.get("head");
if (head != undefined) {
    // 制作各种动画
}
```



### 组

我们可以通过`modelMap.get("xxx")`获取各种组，以下是可用于获取到的组的函数列表。

|                  函数名                  |    返回值    |              备注               |
|:-------------------------------------:|:---------:|:-----------------------------:|
| `setRotateAngleX(float rotateAngleX)` |     无     |          设置组的 X 轴角度           |
| `setRotateAngleY(float rotateAngleY)` |     无     |          设置组的 Y 轴角度           |
| `setRotateAngleZ(float rotateAngleZ)` |     无     |          设置组的 Z 轴角度           |
|      `setOffsetX(float offsetX)`      |     无     |         设置组的 X 轴坐标偏移          |
|      `setOffsetY(float offsetY)`      |     无     |         设置组的 Y 轴坐标偏移          |
|      `setOffsetZ(float offsetZ)`      |     无     |         设置组的 Z 轴坐标偏移          |
|      `setHidden(boolean hidden)`      |     无     |            设置组是否隐藏            |
|          `getRotateAngleX()`          |  `float`  |          获取组的 X 轴角度           |
|          `getRotateAngleY()`          |  `float`  |          获取组的 Y 轴角度           |
|          `getRotateAngleZ()`          |  `float`  |          获取组的 Z 轴角度           |
|        `getInitRotateAngleX()`        |  `float`  | 获取 ModelRenderer 的初始化 X 轴旋转角度 |
|        `getInitRotateAngleY()`        |  `float`  | 获取 ModelRenderer 的初始化 Y 轴旋转角度 |
|        `getInitRotateAngleZ()`        |  `float`  | 获取 ModelRenderer 的初始化 Z 轴旋转角度 |
|            `getOffsetX()`             |  `float`  |         获取组的 X 轴坐标偏移          |
|            `getOffsetY()`             |  `float`  |         获取组的 Y 轴坐标偏移          |
|            `getOffsetZ()`             |  `float`  |         获取组的 Z 轴坐标偏移          |
|             `isHidden()`              | `boolean` |            检查组是否隐藏            |



### GlWrapper

在脚本的最开头，我们使用了一个叫做 `GlWrapper` 的工具，它可以进行各种平移、旋转和缩放操作。

|                       函数名                        | 返回值 |                      备注                      |
|:------------------------------------------------:|:---:|:--------------------------------------------:|
|      `translate(float x, float y, float z)`      |  无  |                将实体移动至坐标 x y z                |
| `rotate(float angle, float x, float y, float z)` |  无  | 以直线 `(0, 0, 0) (x, y, z)` 为轴，将其旋转 `angle` 度。 |
|        `scale(float x, float y, float z)`        |  无  |              将实体在三条轴上缩放 x y z 倍              |