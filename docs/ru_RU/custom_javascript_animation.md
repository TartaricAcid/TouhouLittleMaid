# Пользовательская анимация JavaScript

С помощью JavaScript-файлов вы можете добавлять пользовательские анимации для горничной или кресла.

- Этот вики подходит для Touhou Little Maid `1.12.2` и `1.16.5`:
- Базовое понимание языка JavaScript;
- Немного математических знаний из средней/старшей школы, в частности тригонометрических функций и полярных координат;
- Для редактирования скриптов, рекомендуется использовать VSCode с сохранением в кодировке UTF-8 без BOM.

## Базовый формат

Скрипт с анимацией может быть помещён в любую папку, вам нужно только указать путь к файлу для соответствующих моделей. Я рекомендую поместить его в папку `animation`.

Ниже приведен общий шаблон：

```js
// Это вызывается только если вы хотите использовать GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    /**
     * @param entity Сущность/моб, к которой применяется данная анимация
     * @param limbSwing Скорость движения моба (как спидометр в машине)
     * @param limbSwingAmount Пройденная мобом дистанция (как счётчик пробега в машине)
     * @param ageInTicks Возраст моба в тиках, значение постоянно увеличивается от 0
     * @param netHeadYaw Поворот головы моба
     * @param headPitch Наклон головы моба
     * @param scale Значение масштабирования моба. По умолчанию - 0.0625. Применений не найдено.
     * @param modelMap Группа модели, сохраненная для хэш-карты
     */
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // Скрипт для модели
    }
})
```

Нам нужно, чтобы эта группа вращалась вдоль оси X со скоростью 1 градус каждый тик (18 секунд на 1 оборот).

```javascript
// Это вызывается только если вам нужно использовать GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                          headPitch, scale, modelMap) {
        // Сперва определим грумму 'rotation' из modelMap
        rotation = modelMap.get("rotation");
        // На всякий случай, сделаем простую проверку наличия этой группы у модели
        if (rotation != undefined) {
            // С помощью setRotateAngleX, мы установим угол вращения по оси X
            // ageInTicks - это возраст сущности, значение, постоянно увеличивающееся от 0
            // С помощью оператора % мы найдём остаток от деленияв промежутке от 0 до 360
            // Так как функция принимает только радианы, умножим полученное значение на 0.017453292 чтобы превратить градусы в радианы
            // И так мы получим анимацию вращения на 1 градус каждый тик
            rotation.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }
    }
})
```

Теперь мы добавим еще одно сложное движение, у нас есть группа с именем `wing`, и мы хотим, чтобы крылья постоянно двигались вперёд-назад.

Поворот будет по оси Y на угол от `-20° до 40°`, и цикл будет повторяться каждые 5 секунд.

Тригонометрическая функция соответствует нашим нуждам, так как вы можете использовать синус или косинус, мы будем использовать функцию синуса.

```javascript
// Это вызывается только если вам нужно использовать GlWrapper
var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
        headPitch, scale, modelMap) {
        // Сперва найдём группу 'wing' из modelMap
        wing = modelMap.get("wing");
        // На всякий случай сделаем простую проверку наличия этой группы у модели
        if (wing != undefined) {
            // Один цикл займёт 5 секунд, что равно 100 тикам
            // Используя умножение и остаток от деления мы получим желаемое
            var time = (ageInTicks * 3.6) % 360;
            // Теперь используется функция Math из JavaScript
            // Используя синус, мы получим периодическую функцию в интервале от -20° до 40°
            var func = 30 * Math.sin(time * 0.017453292) + 10;
            // Так как функция принимает только радианы, умножим полученное значение на 0.017453292 чтобы превратить градусы в радианы
            wing.setRotateAngleY(func * 0.017453292);
        }
    }
})
```

Все остальные сложные движения могут быть получены с помощью соответствующих фунций.

## Hot Reload

Так как вы не можете понять, правильно ли написана анимация, только лишь посмотрев на код, мы добавили возможность быстрой перезагрузки анимации в игре.

После того, как вы загрузите созданный вами набор ресурсов модели, просто используйте следующую команду для перезагрузки всех данных анимации.

- 1.12.2: `/maid_res reload`
- 1.16.5: `/tlm pack reload`

## Документация по функциям

Зачёркнутый стиль означает, что данные функции устарели в 1.16, вы можете посетить [эту ссылку](https://github.com/TartaricAcid/TouhouLittleMaid/tree/1.16.5/src/main/java/com/github/tartaricacid/touhoulittlemaid/api/animation) чтобы увидеть исходный код.

### Параметры сущностей

В зависимости от цели, к которой применяется анимация, функции, используемые `сущностью`, также могут отличаться друг от друга.

#### Maid

|         Function name        |     Return value    |                                        Note                                        |
| :--------------------------: | :-----------------: | :--------------------------------------------------------------------------------: |
|         `hasHelmet()`        |      `boolean`      |                    Если горничная надела шлем, возвращает `true`                   |
|       `hasChestPlate()`      |      `boolean`      |                 Есди горничная надела нагрудник, возвращает `true`                 |
|        `hasLeggings()`       |      `boolean`      |                   Если горничная надела поножи, возвращает `true`                  |
|         `hasBoots()`         |      `boolean`      |                  Если горничная надела ботинки, возвращает `true`                  |
|         `getHelmet()`        |       `String`      |        Если горничная надела шлем, возвращает имя шлема из реестра предметов       |
|       `getChestPlate()`      |       `String`      |   Если горничная надела нагрудник, возвращает имя нагрудника из реестра предметов  |
|        `getLeggings()`       |       `String`      |      Если горничная надела поножи, возвращает имя поножей из реестра предметов     |
|         `getBoots()`         |       `String`      |     Если горничная надела ботинки, возвращает имя ботинок из реестра предметов     |
|         `isBegging()`        |      `boolean`      |                           Whether maid is in begging mode                          |
|      `isSwingingArms()`      |      `boolean`      |              Если горничная двигает руками, эта функция вернет `true`              |
|     `getSwingProgress()`     |       `float`       |                              Get maid's swinging time                              |
|         `isRiding()`         |      `boolean`      |                           Whether maid is in riding mode                           |
|         `isSitting()`        |      `boolean`      |                           Whether maid is in standby mode                          |
|     ~~`isHoldTrolley()`~~    |      `boolean`      |                   Держит ли горничная тележку или другие объекты                   |
|  ~~`isRidingMarisaBroom()`~~ |      `boolean`      |                         Whether maid is riding Marisa Broom                        |
|    ~~`isRidingPlayer()`~~    |      `boolean`      |                            Whether maid is riding player                           |
|     ~~`isHoldVehicle()`~~    |      `boolean`      |                           Whether maid is riding vehicle                           |
|  ~~`isPortableAudioPlay()`~~ |      `boolean`      |                  Whether the maid hold portable audio and play it                  |
|        `hasBackpack()`       |      `boolean`      |                            Whether maid wearing backpack                           |
|     `getBackpackLevel()`     |        `int`        |                               Вернёт уровень рюкзака                               |
|      ~~`hasSasimono()`~~     |      `boolean`      |                            Whether maid wearing sasimono                           |
|      `isSwingLeftHand()`     |      `boolean`      |           Использует ли горничная левую руку, вернёт `false`, если правую          |
|  ~~`getLeftHandRotation()`~~ | `дробное число [3]` |                          Вернёт данные вращения левой руки                         |
| ~~`getRightHandRotation()`~~ | `дробное число [3]` |                         Вернёт данные вращения правой руки                         |
|        ~~`getDim()`~~        |        `int`        |                       Get the dimension where the maid is in                       |
|         `getWorld()`         |        `Мир`        |                                Get maid's world data                               |
|          `getTask()`         |       `String`      |            Вернёт занятие горничной, такое как `attack`, `ranged_attack`           |
|      `hasItemMainhand()`     |      `boolean`      |                           Whether maid has mainhand item                           |
|      `hasItemOffhand()`      |      `boolean`      |                            Whether maid has offhand item                           |
|      `getItemMainhand()`     |       `String`      |  Вернёт имя предмета, находящегося в главной руке горничной, из реестра предметов  |
|      `getItemOffhand()`      |       `String`      |                        Get maid offhand item's registry name                       |
|          `inWater()`         |      `boolean`      |                                Whether maid in water                               |
|          `inRain()`          |      `boolean`      |                               Под дождём ли горничная                              |
|        `getAtBiome()`        |       `String`      |                           Get maid's biome register name                           |
|    ~~`getAtBiomeTemp()`~~    |       `String`      |                          Get maid's biome temperature enum                         |
|          `onHurt()`          |      `boolean`      |                             Whether the maid is on hurt                            |
|         `getHealth()`        |       `float`       |                                  Get maid's health                                 |
|       `getMaxHealth()`       |       `float`       |                                Get maid's max health                               |
|          `isSleep()`         |      `boolean`      |                                Whether maid is sleep                               |
|      `getFavorability()`     |        `int`        |                             Get the maid's favorability                            |
|        `isOnGround()`        |      `boolean`      |                            Whether the maid is on ground                           |
|       `getArmorValue()`      |       `double`      |                               Get maid's armor value                               |
|          `getSeed()`         |        `long`       | Вернёт фиксированное значение, уникальное для каждой сущности, схожее с UUID мобов |

#### Сидение

|     Function name     | Return value |                                        Note                                        |
| :-------------------: | :----------: | :--------------------------------------------------------------------------------: |
|   `isRidingPlayer()`  |   `boolean`  |                           Сидит ли за этим сидением игрок                          |
|    `hasPassenger()`   |   `boolean`  |                           Имеет ли это сидение пассажира                           |
|  `getPassengerYaw()`  |    `float`   |                              Get chair passenger's yaw                             |
|       `getYaw()`      |    `float`   |                               Вернёт поворот сидения                               |
| `getPassengerPitch()` |    `float`   |                             Get chair passenger's pitch                            |
|     ~~`getDim()`~~    |     `int`    |                                 Get chair's dim id                                 |
|      `getWorld()`     |     `Мир`    |                               Get chair's world data                               |
|      `getSeed()`      |    `long`    | Вернёт фиксированное значение, уникальное для каждой сущности, схожее с UUID мобов |

#### Мир

|   Function name  | Return value |                         Note                        |
| :--------------: | :----------: | :-------------------------------------------------: |
| `getWorldTime()` |    `long`    | Get world's time (tick, 0-24000) |
|     `isDay()`    |   `boolean`  |               Whether the world is day              |
|    `isNight()`   |   `boolean`  |              Whether the world is night             |
|   `isRaining()`  |   `boolean`  |                    Идёт ли дождь                    |
| `isThundering()` |   `boolean`  |           Whether the world is thundering           |

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

|             Function name             | Return value |                        Note                       |
| :-----------------------------------: | :----------: | :-----------------------------------------------: |
| `setRotateAngleX(float rotateAngleX)` |     None     |              Set the group's X angle              |
| `setRotateAngleY(float rotateAngleY)` |     None     |              Set the group's Y angle              |
| `setRotateAngleZ(float rotateAngleZ)` |     None     |              Set the group's Z angle              |
|      `setOffsetX(float offsetX)`      |     None     |        Set the group's X coordianate offset       |
|      `setOffsetY(float offsetY)`      |     None     |        Set the group's Y coordianate offset       |
|      `setOffsetZ(float offsetZ)`      |     None     |        Set the group's Z coordianate offset       |
|      `setHidden(boolean hidden)`      |     None     |             Set if the group is hidden            |
|          `getRotateAngleX()`          |    `float`   |             Obtain the group's X angle            |
|          `getRotateAngleY()`          |    `float`   |             Obtain the group's Y angle            |
|          `getRotateAngleZ()`          |    `float`   |             Obtain the group's Z angle            |
|        `getInitRotateAngleX()`        |    `float`   | Get ModelRenderer's initialization x rotate angle |
|        `getInitRotateAngleY()`        |    `float`   | Get ModelRenderer's initialization y rotate angle |
|        `getInitRotateAngleZ()`        |    `float`   | Get ModelRenderer's initialization z rotate angle |
|             `getOffsetX()`            |    `float`   |      Obtain the group's X coordianate offset      |
|             `getOffsetY()`            |    `float`   |      Obtain the group's Y coordianate offset      |
|             `getOffsetZ()`            |    `float`   |      Obtain the group's Y coordianate offset      |
|              `isHidden()`             |   `boolean`  |            Check if the group is hidden           |

### GlWrapper

On the top of the script we used a tool called `GlWrapper`, that can make various translation, rotation and scaling operations.

|                   Function name                  | Return value |                                               Note                                               |
| :----------------------------------------------: | :----------: | :----------------------------------------------------------------------------------------------: |
|      `translate(float x, float y, float z)`      |     None     |                                Move the entity to coordiate x y z                                |
| `rotate(float angle, float x, float y, float z)` |     None     | Using a straight line`(0, 0, 0) (x, y, z)` as axis, rotate it by `angle` degree. |
|        `scale(float x, float y, float z)`        |     None     |                             Scale entity on three axis by x y z times                            |
