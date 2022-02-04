# 自定义 JavaScript 动画
通过 JavaScript 文件，您可以为女仆或坐垫添加自定义动画。

- This wiki is suitable for Touhou Little Maid mod in `1.12.2` and `1.16.5`:
- Basic understanding for JavaScript language；
- Some high school mathematical knowledge, especially towards Trigonometric function and polar coordinates;
- For editing script software, VSCode is recommended, all related script files requires to be saved using UTF-8 without BOM.

## Basic Format

Animation script can be put in any location of the folder, you only need to call the file path on the corresponding models. I recommend putting it in the `animation` folder.

Below is the general template：

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
```

Here we have a simple example, current model has a group named `rotation` we want this group to make a rotational movement around the X axis, the movement speed is around 1 degree every tick (18 sec/r), we can write it as below.

```javascript
// This call is only needed when you need to use GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // First obtain a group named 'rotation' from modelMap
        rotation = modelMap.get("rotation");
        // Just in case, we make a simple check to make sure this group existed
        if (rotation != undefined) {
            // Through the function setRotateAngleX in the group, we set its X axis angle
            // ageInTicks is tick time for the entity, a value that constantly increases starting from 0
            // Through remainder operator (which is % sign), set the value between 0~360
            // Since this method only accepts radian, we need to multiply it by 0.017453292 to convert into radian
            // And with that we achieved the animation of rotating 1 degree every tick
            rotation.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }
    }
})
```

Now we add another more complex motion, we have a group named `wing`, and we want a constant back and forth oscillating motion.

Oscillate around Y axis, at a degree between `-20°~40°`, and one cycle is completed every 5 second.

Trigonometry function fits our need, as you can use sine or cosine for this, we will be using sine function.

```javascript
// This call is only needed when you need to use GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
        headPitch, scale, modelMap) {
        // First obtain a group named 'wing' from modelMap
        wing = modelMap.get("wing");
        // Just in case, we make a simple check to make sure this group existed
        if (wing != undefined) {
            // One complete cycle every 5 second, which is 100 tick
            // Using multiplication and remainder operator we can achieve this requirement
            var time = (ageInTicks * 3.6) % 360;
            // This is using the Math function in JavaScript
            // Construct sine function, and obtain a periodic function between -20°~40°
            var func = 30 * Math.sin(time * 0.017453292) + 10;
            // Since this method only accepts radian, we need to multiply it by 0.017453292 to convert into radian
            wing.setRotateAngleY(func * 0.017453292);
        }
    }
})
```

All other complex motion can be achieved through the related functions.

## Hot Reload

Since you can't determine if the animation is correct just by looking at the functions, we added a function to hot reload the animation ingame.

After you load the model resource pack you made, just use the following command can reload all animation's data.
- 1.12.2: `/maid_res reload`
- 1.16.5: `/tlm pack reload`

## Function Documentation

Strikethrough style means that the method is deprecated in 1.16, you can visit the [link here](https://github.com/TartaricAcid/TouhouLittleMaid/tree/1.16.5/src/main/java/com/github/tartaricacid/touhoulittlemaid/api/animation) to view the source code API.

### Entity Parameter

Depending on the target of the added animation, the function that can be used by `entity` differs as well.

#### Maid

|        Function name         | Return value |                                       Note                                       |
|:----------------------------:|:------------:|:--------------------------------------------------------------------------------:|
|        `hasHelmet()`         |  `boolean`   |                     After maid wears helmet, returns `true`                      |
|      `hasChestPlate()`       |  `boolean`   |                   After maid wears chestplate, returns `true`                    |
|       `hasLeggings()`        |  `boolean`   |                    After maid wears leggings, returns `true`                     |
|         `hasBoots()`         |  `boolean`   |                      After maid wears boots, returns `true`                      |
|        `getHelmet()`         |   `String`   |           After maid wears helmet, returns helmet item's registry name           |
|      `getChestPlate()`       |   `String`   |       After maid wears chestplate, returns chestplate item's registry name       |
|       `getLeggings()`        |   `String`   |         After maid wears leggings, returns leggings item's registry name         |
|         `getBoots()`         |   `String`   |            After maid wears boots, returns boots item's registry name            |
|        `isBegging()`         |  `boolean`   |                         Whether maid is in begging mode                          |
|      `isSwingingArms()`      |  `boolean`   |             If maid is using arms, this function will return `true`              |
|     `getSwingProgress()`     |   `float`    |                             Get maid's swinging time                             |
|         `isRiding()`         |  `boolean`   |                          Whether maid is in riding mode                          |
|        `isSitting()`         |  `boolean`   |                         Whether maid is in standby mode                          |
|    ~~`isHoldTrolley()`~~     |  `boolean`   |                Whether maid is carrying trolley or other entities                |
| ~~`isRidingMarisaBroom()`~~  |  `boolean`   |                       Whether maid is riding Marisa Broom                        |
|    ~~`isRidingPlayer()`~~    |  `boolean`   |                          Whether maid is riding player                           |
|    ~~`isHoldVehicle()`~~     |  `boolean`   |                          Whether maid is riding vehicle                          |
| ~~`isPortableAudioPlay()`~~  |  `boolean`   |                 Whether the maid hold portable audio and play it                 |
|       `hasBackpack()`        |  `boolean`   |                          Whether maid wearing backpack                           |
|     `getBackpackLevel()`     |    `int`     |                            Get maid's backpack level                             |
|     ~~`hasSasimono()`~~      |  `boolean`   |                          Whether maid wearing sasimono                           |
|     `isSwingLeftHand()`      |  `boolean`   | Whether the maid is swinging left or right arm, return `false` if it's the right |
| ~~`getLeftHandRotation()`~~  |  `float[3]`  |                          Get the left arm rotation data                          |
| ~~`getRightHandRotation()`~~ |  `float[3]`  |                         Get the right arm rotation data                          |
|        ~~`getDim()`~~        |    `int`     |                      Get the dimension where the maid is in                      |
|         `getWorld()`         |   `World`    |                              Get maid's world data                               |
|         `getTask()`          |   `String`   |                Get maid's task, such as `attack`, `ranged_attack`                |
|     `hasItemMainhand()`      |  `boolean`   |                          Whether maid has mainhand item                          |
|      `hasItemOffhand()`      |  `boolean`   |                          Whether maid has offhand item                           |
|     `getItemMainhand()`      |   `String`   |                      Get maid mainhand item's registry name                      |
|      `getItemOffhand()`      |   `String`   |                      Get maid offhand item's registry name                       |
|         `inWater()`          |  `boolean`   |                              Whether maid in water                               |
|          `inRain()`          |  `boolean`   |                               Whether maid in rain                               |
|        `getAtBiome()`        |   `String`   |                          Get maid's biome register name                          |
|    ~~`getAtBiomeTemp()`~~    |   `String`   |                        Get maid's biome temperature enum                         |
|          `onHurt()`          |  `boolean`   |                           Whether the maid is on hurt                            |
|        `getHealth()`         |   `float`    |                                Get maid's health                                 |
|       `getMaxHealth()`       |   `float`    |                              Get maid's max health                               |
|         `isSleep()`          |  `boolean`   |                              Whether maid is sleep                               |
|     `getFavorability()`      |    `int`     |                           Get the maid's favorability                            |
|        `isOnGround()`        |  `boolean`   |                          Whether the maid is on ground                           |
|      `getArmorValue()`       |   `double`   |                              Get maid's armor value                              |
|         `getSeed()`          |    `long`    |    Get a fixed value, each entity is different, similar to the entity's UUID     |

#### Chair

|     Function name     | Return value |                                   Note                                    |
|:---------------------:|:------------:|:-------------------------------------------------------------------------:|
|  `isRidingPlayer()`   |  `boolean`   |                  Whether the chair is sit by the player                   |
|   `hasPassenger()`    |  `boolean`   |                      Whether the chair has passenger                      |
|  `getPassengerYaw()`  |   `float`    |                         Get chair passenger's yaw                         |
|      `getYaw()`       |   `float`    |                              Get chair's yaw                              |
| `getPassengerPitch()` |   `float`    |                        Get chair passenger's pitch                        |
|    ~~`getDim()`~~     |    `int`     |                            Get chair's dim id                             |
|     `getWorld()`      |   `World`    |                          Get chair's world data                           |
|      `getSeed()`      |    `long`    | Get a fixed value, each entity is different, similar to the entity's UUID |

#### World
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

You can get the corresponding group through `modelMap.get("xxx")`. If there is no group that matches the name, return `undefined`

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