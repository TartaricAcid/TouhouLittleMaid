# 自定义 JavaScript 动画
通过 JavaScript 文件，你可以为女仆或坐垫添加自定义动画。

- 本 wiki 适用于 Touhou Little Maid 模组 `1.12.2` 和 `1.16.5` 版本；
- 需要对 JavaScript 基本语法有简单的了解；
- 需要高中及以上数学知识，尤其是对三角函数和极坐标的理解。
- 文本编辑软件推荐 VSCode，相关文本文件均需要用 UTF-8 无 BOM 编码进行存储。

## 基本格式

Animation script can be put in any location of the folder, you only need to call the file path on the corresponding models. I recommend putting it in the `animation` folder. 我建议将它放入 `animation` 文件夹中。

下面是通用模板：

```js
// This call is only needed when you need to use GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    /**
     * @param entity Entity that requires the corresponding animation
     * @param limbSwing The walking speed of the entity (think of it as the speedometer of a car)
     * @param limbSwingAmount The total walking distance of the entity (think of it as the odometer of a car)
     * @param ageInTicks The tick time of an entity, the value that constantly increase from 0
     * @param netHeadYaw The yaw for the head of the entity
     * @param headPitch The pitch for the head of the entity
     * @param scale Param for scaling the entity, default is 0.0625, no use found.
     * @param modelMap The group of the model saved for a hashmap
     */
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // Script for the model
    }
})
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
|        `getHelmet()`         |  `String`  |           After maid wears helmet, returns helmet item's registry name           |
|      `getChestPlate()`       |  `String`  |       After maid wears chestplate, returns chestplate item's registry name       |
|       `getLeggings()`        |  `String`  |         After maid wears leggings, returns leggings item's registry name         |
|         `getBoots()`         |  `String`  |            After maid wears boots, returns boots item's registry name            |
|        `isBegging()`         | `boolean`  |                         Whether maid is in begging mode                          |
|      `isSwingingArms()`      | `boolean`  |             If maid is using arms, this function will return `true`              |
|     `getSwingProgress()`     |  `float`   |                             Get maid's swinging time                             |
|         `isRiding()`         | `boolean`  |                                    女仆是否处于骑乘模式                                    |
|        `isSitting()`         | `boolean`  |                                    女仆是否处于待命模式                                    |
|    ~~`isHoldTrolley()`~~     | `boolean`  |                Whether maid is carrying trolley or other entities                |
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
|        `getHealth()`         |  `float`   |                                Get maid's health                                 |
|       `getMaxHealth()`       |  `float`   |                                    获取女仆的最大生命值                                    |
|         `isSleep()`          | `boolean`  |                              Whether maid is sleep                               |
|     `getFavorability()`      |   `int`    |                           Get the maid's favorability                            |
|        `isOnGround()`        | `boolean`  |                          Whether the maid is on ground                           |
|      `getArmorValue()`       |  `double`  |                              Get maid's armor value                              |
|         `getSeed()`          |   `long`   |    Get a fixed value, each entity is different, similar to the entity's UUID     |

#### Chair

|     Function name     | Return value |                  Note                  |
|:---------------------:|:------------:|:--------------------------------------:|
|  `isRidingPlayer()`   |  `boolean`   | Whether the chair is sit by the player |
|   `hasPassenger()`    |  `boolean`   |    Whether the chair has passenger     |
|  `getPassengerYaw()`  |   `float`    |       Get chair passenger's yaw        |
|      `getYaw()`       |   `float`    |            Get chair's yaw             |
| `getPassengerPitch()` |   `float`    |      Get chair passenger's pitch       |
|    ~~`getDim()`~~     |    `int`     |           Get chair's dim id           |
|     `getWorld()`      |   `World`    |         Get chair's world data         |
|      `getSeed()`      |    `long`    |     获取一个固定值，每个实体都是不同的，类似于该实体的 UUID     |

#### 世界
|  Function name   | Return value |               Note               |
|:----------------:|:------------:|:--------------------------------:|
| `getWorldTime()` |    `long`    | Get world's time (tick, 0-24000) |
|    `isDay()`     |  `boolean`   |     Whether the world is day     |
|   `isNight()`    |  `boolean`   |    Whether the world is night    |
|  `isRaining()`   |  `boolean`   |   Whether the world is raining   |
| `isThundering()` |  `boolean`   | Whether the world is thundering  |



### `limbSwing` & `limbSwingAmount` Parameter

These are floating points, `limbSwing` is the walking speed of the entity (think of it as the speedometer of a car), `limbSwingAmount` is the total walking distance of the entity (think of it as the odometer of a car).

These two data are mainly used on the rotation of the legs and limbs, Minecraft vanilla uses these two basic data to calculate the animation of the arm and leg swinging.

`Math.cos(limbSwing * 0.6662) * limbSwingAmount`(left hand)

`-Math.cos(limbSwing * 0.6662) * limbSwingAmount`(right hand)

`Math.cos(limbSwing * 0.6662) * limbSwingAmount * 1.4`(left leg)

`-Math.cos(limbSwing * 0.6662) * limbSwingAmount * 1.4`(right leg)

Changing the value `0.6662` will control the frequency of the swing, multiplied by the coeffecient of the formula (for example, the leg uses `1.4` as the coeffecient) to change the amplitude of the swing.

Using the vanilla Minecraft formula for arm and leg swinging can make a more natural swinging animation.



### `ageInTicks` Parameter

Floating point, a variable that self-increase from 0 every tick, a self-changing parameter that's used in most animation function.



### `netHeadYaw` and `headPitch` Parameter

Both are floating point, and are degrees value (this is how vanilla Minecraft is designed).

Normally this parameter can be used as a rotation angle, you just need to change it into radian.

```javascript
head.setRotateAngleX(headPitch * 0.017453292);
head.setRotateAngleY(netHeadYaw * 0.017453292);
```

> If the coeffiecient in this section is set to be larger than '0.017453292', there may have some error issue.


### `scale` Parameter

Floating point, fixed at 0.0625.

A value that has unknown meaning.



### modelMap Parameter

A Map that saves group, using string as keys.

You can get the corresponding group through `modelMap.get("xxx")`. If there is no group that matches the name, return `undefined` If there is no group that matches the name, return `undefined`

Let's say we want to get the target group `head`:

```javascript
head = modelMap.get("head");
```
Then we can set various parameter using this `head` group to make animation.

Of course, as a precaution, it's best to set a check for this group, to make sure it's existed.

```javascript
head = modelMap.get("head");
if (head != undefined) {
    // Making various animation
}
```



### Group

We can get various group via `modelMap.get("xxx")`, the following are the functions that can be used to the obtained group.

|             Function name             | Return value |                       Note                        |
|:-------------------------------------:|:------------:|:-------------------------------------------------:|
| `setRotateAngleX(float rotateAngleX)` |     None     |              Set the group's X angle              |
| `setRotateAngleY(float rotateAngleY)` |     None     |              Set the group's Y angle              |
| `setRotateAngleZ(float rotateAngleZ)` |     None     |              Set the group's Z angle              |
|      `setOffsetX(float offsetX)`      |     None     |       Set the group's X coordianate offset        |
|      `setOffsetY(float offsetY)`      |     None     |       Set the group's Y coordianate offset        |
|      `setOffsetZ(float offsetZ)`      |     None     |       Set the group's Z coordianate offset        |
|      `setHidden(boolean hidden)`      |     None     |            Set if the group is hidden             |
|          `getRotateAngleX()`          |   `float`    |            Obtain the group's X angle             |
|          `getRotateAngleY()`          |   `float`    |            Obtain the group's Y angle             |
|          `getRotateAngleZ()`          |   `float`    |            Obtain the group's Z angle             |
|        `getInitRotateAngleX()`        |   `float`    | Get ModelRenderer's initialization x rotate angle |
|        `getInitRotateAngleY()`        |   `float`    | Get ModelRenderer's initialization y rotate angle |
|        `getInitRotateAngleZ()`        |   `float`    | Get ModelRenderer's initialization z rotate angle |
|            `getOffsetX()`             |   `float`    |      Obtain the group's X coordianate offset      |
|            `getOffsetY()`             |   `float`    |      Obtain the group's Y coordianate offset      |
|            `getOffsetZ()`             |   `float`    |      Obtain the group's Y coordianate offset      |
|             `isHidden()`              |  `boolean`   |           Check if the group is hidden            |



### GlWrapper

On the top of the script we used a tool called `GlWrapper`, that can make various translation, rotation and scaling operations.

|                  Function name                   | Return value |                                       Note                                       |
|:------------------------------------------------:|:------------:|:--------------------------------------------------------------------------------:|
|      `translate(float x, float y, float z)`      |     None     |                        Move the entity to coordiate x y z                        |
| `rotate(float angle, float x, float y, float z)` |     None     | Using a straight line`(0, 0, 0) (x, y, z)` as axis, rotate it by `angle` degree. |
|        `scale(float x, float y, float z)`        |     None     |                    Scale entity on three axis by x y z times                     |