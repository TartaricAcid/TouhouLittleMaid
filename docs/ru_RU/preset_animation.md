

# Preset Animation

There are many default animations built in the mod, you only need:
1. Set the correct name for the group you want to animate in Blockbench.
2. Call the animation reference of the path in the `animation` field of the `maid_model.json` file. :::tip The `tlm-utils` plugin can automatically call the animation script reference based on the group name. :::

## Maid's Preset Animation
Preset animation for maid only, pay attention to the case of the group name.
### `head`
|                    Animation Reference Path                     |                                Effect                                |
|:---------------------------------------------------------------:|:--------------------------------------------------------------------:|
|   `touhou_little_maid:animation/maid/default/head/default.js`   |                    The maid's head up or turning                     |
|     `touhou_little_maid:animation/maid/default/head/beg.js`     |       When the player holds the cake, the maid tilts her head        |
| `touhou_little_maid:animation/maid/default/head/music_shake.js` | The maid shook her head while holding a portable audio to play music |

### `headExtraA headExtraB headExtraC`
|                 Animation Reference Path                  |                            Effect                            |
|:---------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/head/extra.js` | There are normal head up or turning, but no beg, music shake |

### `armLeft`

The game will automatically render left-handed items based on this group name, no additional animation references are required.

|                  Animation Reference Path                  |                   Effect                    |
|:----------------------------------------------------------:|:-------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/default.js` |      The maid's arm swing when walking      |
|  `touhou_little_maid:animation/maid/default/arm/swing.js`  |     The maid's arm swing when use hand      |
| `touhou_little_maid:animation/maid/default/sit/default.js` | When the maid is on sitting, cross her arms |

### `armLeftVertical`
|                  Animation Reference Path                   |                              Effect                              |
|:-----------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/vertical.js` | When walking, the group is always perpendicular to the `armLeft` |

### `armLeftExtraA`
|                 Animation Reference Path                 |                         Effect                          |
|:--------------------------------------------------------:|:-------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/extra.js` | Same as the `armLeft`, but not render the holding items |

### `armRight`
|                  Animation Reference Path                  |                   Effect                    |
|:----------------------------------------------------------:|:-------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/default.js` |      The maid's arm swing when walking      |
|  `touhou_little_maid:animation/maid/default/arm/swing.js`  |     The maid's arm swing when use hand      |
| `touhou_little_maid:animation/maid/default/sit/default.js` | When the maid is on sitting, cross her arms |

### `armRightVertical`
|                  Animation Reference Path                   |                              Effect                              |
|:-----------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/vertical.js` | When walking, the group is always perpendicular to the`armRight` |

### `armRightExtraA`
|                 Animation Reference Path                 |                          Effect                          |
|:--------------------------------------------------------:|:--------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/arm/extra.js` | Same as the `armRight`, but not render the holding items |

### `legLeft`
|                  Animation Reference Path                  |                     Effect                      |
|:----------------------------------------------------------:|:-----------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/default.js` |     The maid swings her legs while walking      |
| `touhou_little_maid:animation/maid/default/sit/default.js` | The maid swings her legs when she is on sitting |

### `legLeftVertical`
|                  Animation Reference Path                   |                             Effect                              |
|:-----------------------------------------------------------:|:---------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/vertical.js` | When walking, the group is always perpendicular to the`legLeft` |

### `legLeftExtraA`
|                 Animation Reference Path                 |                        Effect                        |
|:--------------------------------------------------------:|:----------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/extra.js` | Same as the `legLeft`, but no effect of sitting down |

### `legRight`
|                  Animation Reference Path                  |                     Effect                      |
|:----------------------------------------------------------:|:-----------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/default.js` |     The maid swings her legs while walking      |
| `touhou_little_maid:animation/maid/default/sit/default.js` | The maid swings her legs when she is on sitting |

### `legRightVertical`
|                  Animation Reference Path                   |                              Effect                              |
|:-----------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/vertical.js` | When walking, the group is always perpendicular to the`legRight` |

### `legRightExtraA`
|                 Animation Reference Path                 |                        Effect                         |
|:--------------------------------------------------------:|:-----------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/leg/extra.js` | Same as the `legRight`, but no effect of sitting down |

### `wingLeft`
|                  Animation Reference Path                   |               Effect                |
|:-----------------------------------------------------------:|:-----------------------------------:|
| `touhou_little_maid:animation/maid/default/wing/default.js` | Wings reciprocating swing animation |

### `wingRight`
|                  Animation Reference Path                   |               Effect                |
|:-----------------------------------------------------------:|:-----------------------------------:|
| `touhou_little_maid:animation/maid/default/wing/default.js` | Wings reciprocating swing animation |

### `ahoge`
|                Animation Reference Path                 |       Effect       |
|:-------------------------------------------------------:|:------------------:|
| `touhou_little_maid:animation/maid/default/head/beg.js` | Ahoge swing in beg |

### `blink`
|                 Animation Reference Path                  |       Effect       |
|:---------------------------------------------------------:|:------------------:|
| `touhou_little_maid:animation/maid/default/head/blink.js` | Blinking animation |

### `_bink`
|                     Animation Reference Path                      |                              Effect                               |
|:-----------------------------------------------------------------:|:-----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/head/reverse_blink.js` | An animation that is the exact opposite of the blinking animation |

### `hurtBlink`
|                 Animation Reference Path                 |                 Effect                  |
|:--------------------------------------------------------:|:---------------------------------------:|
| `touhou_little_maid:animation/maid/default/head/hurt.js` | Blink animation only shown when injured |

### `tail`
|                  Animation Reference Path                   |        Effect        |
|:-----------------------------------------------------------:|:--------------------:|
| `touhou_little_maid:animation/maid/default/tail/default.js` | Tail swing animation |

### `sittingRotationSkirt`
|                     Animation Reference Path                      |                          Effect                           |
|:-----------------------------------------------------------------:|:---------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/sit/skirt_rotation.js` | Give the group a rotation angle while the maid is sitting |

### `sittingHiddenSkirt`
|                    Animation Reference Path                     |                             Effect                             |
|:---------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/sit/skirt_hidden.js` | When the maid  is sitting, hide the group. Else show the group |

### `_sittingHiddenSkirt`
|                    Animation Reference Path                     |                             Effect                             |
|:---------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/sit/skirt_hidden.js` | When the maid  is sitting, show the group. Else hide the group |

### `helmet`
|                   Animation Reference Path                   |                                 Effect                                 |
|:------------------------------------------------------------:|:----------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/default.js` | This group is only displayed when the maid is equipped with the helmet |

### `chestPlate` `chestPlateLeft` `chestPlateMiddle` `chestPlateRight`
|                   Animation Reference Path                   |                                   Effect                                    |
|:------------------------------------------------------------:|:---------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/default.js` | This group is only displayed when the maid is equipped with the chest plate |

### `leggings` `leggingsLeft` `leggingsMiddle` `leggingsRight`
|                   Animation Reference Path                   |                                  Effect                                  |
|:------------------------------------------------------------:|:------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/default.js` | This group is only displayed when the maid is equipped with the leggings |

### `bootsLeft` `bootsRight`
|                   Animation Reference Path                   |                                Effect                                 |
|:------------------------------------------------------------:|:---------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/default.js` | This group is only displayed when the maid is equipped with the boots |

### `_helmet`
|                   Animation Reference Path                   |                               Effect                                |
|:------------------------------------------------------------:|:-------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/reverse.js` | This group is only hidden when the maid is equipped with the helmet |

### `_chestPlate` `_chestPlateLeft` `_chestPlateMiddle` `_chestPlateRight`
|                   Animation Reference Path                   |                                  Effect                                  |
|:------------------------------------------------------------:|:------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/reverse.js` | This group is only hidden when the maid is equipped with the chest plate |

### `_leggings` `_leggingsLeft` `_leggingsMiddle` `_leggingsRight`
|                   Animation Reference Path                   |                                Effect                                 |
|:------------------------------------------------------------:|:---------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/reverse.js` | This group is only hidden when the maid is equipped with the leggings |

### `_bootsLeft` `_bootsRight`
|                   Animation Reference Path                   |                               Effect                               |
|:------------------------------------------------------------:|:------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/reverse.js` | This group is only hidden when the maid is equipped with the boots |

### `backpackLevelEmpty`
|                       Animation Reference Path                       |                                    Effect                                     |
|:--------------------------------------------------------------------:|:-----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack_level.js` | This group will only be shown when the maid is not equipped with any backpack |

### `backpackLevelSmall`
|                       Animation Reference Path                       |                                   Effect                                    |
|:--------------------------------------------------------------------:|:---------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack_level.js` | This group will only be shown when the maid is equipped with small backpack |

### `backpackLevelMiddle`
|                       Animation Reference Path                       |                                    Effect                                    |
|:--------------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack_level.js` | This group will only be shown when the maid is equipped with middle backpack |

### `backpackLevelBig`
|                       Animation Reference Path                       |                                  Effect                                   |
|:--------------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack_level.js` | This group will only be shown when the maid is equipped with big backpack |

### `backpackShow`
|                    Animation Reference Path                    |                                  Effect                                   |
|:--------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack.js` | This group will only be shown when the maid is equipped with any backpack |

### `backpackHidden`
|                    Animation Reference Path                    |                                   Effect                                   |
|:--------------------------------------------------------------:|:--------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/backpack.js` | This group will only be hidden when the maid is equipped with any backpack |

### `sasimonoShow`
|                    Animation Reference Path                    |                                  Effect                                   |
|:--------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/sasimono.js` | This group will only be shown when the maid is equipped with the sasimono |

### `sasimonoHidden`
|                    Animation Reference Path                    |                                   Effect                                   |
|:--------------------------------------------------------------:|:--------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/status/sasimono.js` | This group will only be hidden when the maid is equipped with the sasimono |

### `helmetWeatherRainging`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                         Effect                         |
|:--------------------------------------------------------------------:|:------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/weather/raining.js` | This group is only displayed when the maid in the rain |

### `chestPlateWeatherRainging` `chestPlateLeftWeatherRainging` `chestPlateMiddleWeatherRainging` `chestPlateRightWeatherRainging`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                         Effect                         |
|:--------------------------------------------------------------------:|:------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/weather/raining.js` | This group is only displayed when the maid in the rain |

### `leggingsWeatherRainging` `leggingsLeftWeatherRainging` `leggingsMiddleWeatherRainging` `leggingsRightWeatherRainging`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                         Effect                         |
|:--------------------------------------------------------------------:|:------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/weather/raining.js` | This group is only displayed when the maid in the rain |

### `bootsLeftWeatherRainging` `bootsRightWeatherRainging`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                         Effect                         |
|:--------------------------------------------------------------------:|:------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/weather/raining.js` | This group is only displayed when the maid in the rain |

### `attackHidden`
|                  Animation Reference Path                  |                                  Effect                                   |
|:----------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/attack.js` | This group is only hidden when the maid is in the corresponding work task |

### `attackShow`
|                  Animation Reference Path                  |                                    Effect                                    |
|:----------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/attack.js` | This group is only displayed when the maid is in the corresponding work task |

### `cocoaHidden`
|                 Animation Reference Path                  |                                  Effect                                   |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/cocoa.js` | This group is only hidden when the maid is in the corresponding work task |

### `cocoaShow`
|                 Animation Reference Path                  |                                    Effect                                    |
|:---------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/cocoa.js` | This group is only displayed when the maid is in the corresponding work task |

### `danmakuAttackHidden`
|                      Animation Reference Path                      |                                  Effect                                   |
|:------------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/danmaku_attack.js` | This group is only hidden when the maid is in the corresponding work task |

### `danmakuAttackShow`
|                      Animation Reference Path                      |                                    Effect                                    |
|:------------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/danmaku_attack.js` | This group is only displayed when the maid is in the corresponding work task |

### `extinguishingHidden`
|                     Animation Reference Path                      |                                  Effect                                   |
|:-----------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/extinguishing.js` | This group is only hidden when the maid is in the corresponding work task |

### `extinguishingShow`
|                     Animation Reference Path                      |                                    Effect                                    |
|:-----------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/extinguishing.js` | This group is only displayed when the maid is in the corresponding work task |

### `farmHidden`
|                 Animation Reference Path                 |                                  Effect                                   |
|:--------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/farm.js` | This group is only hidden when the maid is in the corresponding work task |

### `farmShow`
|                 Animation Reference Path                 |                                    Effect                                    |
|:--------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/farm.js` | This group is only displayed when the maid is in the corresponding work task |

### `feedHidden`
|                 Animation Reference Path                 |                                  Effect                                   |
|:--------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/feed.js` | This group is only hidden when the maid is in the corresponding work task |

### `feedShow`
|                 Animation Reference Path                 |                                    Effect                                    |
|:--------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/feed.js` | This group is only displayed when the maid is in the corresponding work task |

### `feedAnimalHidden`
|                    Animation Reference Path                     |                                  Effect                                   |
|:---------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/feed_animal.js` | This group is only hidden when the maid is in the corresponding work task |

### `feedAnimalShow`
|                    Animation Reference Path                     |                                    Effect                                    |
|:---------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/feed_animal.js` | This group is only displayed when the maid is in the corresponding work task |

### `grassHidden`
|                 Animation Reference Path                  |                                  Effect                                   |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/grass.js` | This group is only hidden when the maid is in the corresponding work task |

### `grassShow`
|                 Animation Reference Path                  |                                    Effect                                    |
|:---------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/grass.js` | This group is only displayed when the maid is in the corresponding work task |

### `idleHidden`
|                 Animation Reference Path                 |                                  Effect                                   |
|:--------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/idle.js` | This group is only hidden when the maid is in the corresponding work task |

### `idleShow`
|                 Animation Reference Path                 |                                    Effect                                    |
|:--------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/idle.js` | This group is only displayed when the maid is in the corresponding work task |

### `melonHidden`
|                 Animation Reference Path                  |                                  Effect                                   |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/melon.js` | This group is only hidden when the maid is in the corresponding work task |

### `melonShow`
|                 Animation Reference Path                  |                                    Effect                                    |
|:---------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/melon.js` | This group is only displayed when the maid is in the corresponding work task |

### `milkHidden`
|                 Animation Reference Path                 |                                  Effect                                   |
|:--------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/milk.js` | This group is only hidden when the maid is in the corresponding work task |

### `milkShow`
|                 Animation Reference Path                 |                                    Effect                                    |
|:--------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/milk.js` | This group is only displayed when the maid is in the corresponding work task |

### `rangedAttackHidden`
|                     Animation Reference Path                      |                                  Effect                                   |
|:-----------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/ranged_attack.js` | This group is only hidden when the maid is in the corresponding work task |

### `rangedAttackShow`
|                     Animation Reference Path                      |                                    Effect                                    |
|:-----------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/ranged_attack.js` | This group is only displayed when the maid is in the corresponding work task |

### `shearsHidden`
|                  Animation Reference Path                  |                                  Effect                                   |
|:----------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/shears.js` | This group is only hidden when the maid is in the corresponding work task |

### `shearsShow`
|                  Animation Reference Path                  |                                    Effect                                    |
|:----------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/shears.js` | This group is only displayed when the maid is in the corresponding work task |

### `snowHidden`
|                 Animation Reference Path                 |                                  Effect                                   |
|:--------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/snow.js` | This group is only hidden when the maid is in the corresponding work task |

### `snowShow`
|                 Animation Reference Path                 |                                    Effect                                    |
|:--------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/snow.js` | This group is only displayed when the maid is in the corresponding work task |

### `sugarCaneHidden`
|                    Animation Reference Path                    |                                  Effect                                   |
|:--------------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/sugar_cane.js` | This group is only hidden when the maid is in the corresponding work task |

### `sugarCaneShow`
|                    Animation Reference Path                    |                                    Effect                                    |
|:--------------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/sugar_cane.js` | This group is only displayed when the maid is in the corresponding work task |

### `torchHidden`
|                 Animation Reference Path                  |                                  Effect                                   |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/torch.js` | This group is only hidden when the maid is in the corresponding work task |

### `torchShow`
|                 Animation Reference Path                  |                                    Effect                                    |
|:---------------------------------------------------------:|:----------------------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/task/torch.js` | This group is only displayed when the maid is in the corresponding work task |

### `helmetTempCold`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/cold.js` | This group is only displayed when the maid in the cold biome |

### `chestPlateTempCold` `chestPlateLeftTempCold` `chestPlateMiddleTempCold` `chestPlateRightTempCold`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/cold.js` | This group is only displayed when the maid in the cold biome |

### `leggingsTempCold` `leggingsLeftTempCold` `leggingsMiddleTempCold` `leggingsRightTempCold`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/cold.js` | This group is only displayed when the maid in the cold biome |

### `bootsLeftTempCold` `bootsRightTempCold`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/cold.js` | This group is only displayed when the maid in the cold biome |

### `helmetTempMedium`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                     Animation Reference Path                     |                             Effect                             |
|:----------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/medium.js` | This group is only displayed when the maid in the medium biome |

### `chestPlateTempMedium` `chestPlateLeftTempMedium` `chestPlateMiddleTempMedium` `chestPlateRightTempMedium`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                     Animation Reference Path                     |                             Effect                             |
|:----------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/medium.js` | This group is only displayed when the maid in the medium biome |

### `leggingsTempMedium` `leggingsLeftTempMedium` `leggingsMiddleTempMedium` `leggingsRightTempMedium`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                     Animation Reference Path                     |                             Effect                             |
|:----------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/medium.js` | This group is only displayed when the maid in the medium biome |

### `bootsLeftTempMedium` `bootsRightTempMedium`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                     Animation Reference Path                     |                             Effect                             |
|:----------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/medium.js` | This group is only displayed when the maid in the medium biome |

### `helmetTempOcean`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                     |                            Effect                             |
|:---------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/ocean.js` | This group is only displayed when the maid in the ocean biome |

### `chestPlateTempOcean` `chestPlateLeftTempOcean` `chestPlateMiddleTempOcean` `chestPlateRightTempOcean`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                     |                            Effect                             |
|:---------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/ocean.js` | This group is only displayed when the maid in the ocean biome |

### `leggingsTempOcean` `leggingsLeftTempOcean` `leggingsMiddleTempOcean` `leggingsRightTempOcean`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                     |                            Effect                             |
|:---------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/ocean.js` | This group is only displayed when the maid in the ocean biome |

### `bootsLeftTempOcean` `bootsRightTempOcean`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                     |                            Effect                             |
|:---------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/ocean.js` | This group is only displayed when the maid in the ocean biome |

### `helmetTempWarm`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/warm.js` | This group is only displayed when the maid in the warm biome |

### `chestPlateTempWarm` `chestPlateLeftTempWarm` `chestPlateMiddleTempWarm` `chestPlateRightTempWarm`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/warm.js` | This group is only displayed when the maid in the warm biome |

### `leggingsTempWarm` `leggingsLeftTempWarm` `leggingsMiddleTempWarm` `leggingsRightTempWarm`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/warm.js` | This group is only displayed when the maid in the warm biome |

### `bootsLeftTempWarm` `bootsRightTempWarm`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                    Animation Reference Path                    |                            Effect                            |
|:--------------------------------------------------------------:|:------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/temp/warm.js` | This group is only displayed when the maid in the warm biome |

### `helmetValueFull`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                            Effect                             |
|:---------------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_full.js` | Only displayed when the maid's armor value is greater than 15 |

### `chestPlateValueFull` `chestPlateLeftValueFull` `chestPlateMiddleValueFull` `chestPlateRightValueFull`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                            Effect                             |
|:---------------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_full.js` | Only displayed when the maid's armor value is greater than 15 |

### `leggingsValueFull` `leggingsLeftValueFull` `leggingsMiddleValueFull` `leggingsRightValueFull`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                            Effect                             |
|:---------------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_full.js` | Only displayed when the maid's armor value is greater than 15 |

### `bootsLeftValueFull` `bootsRightValueFull`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                            Effect                             |
|:---------------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_full.js` | Only displayed when the maid's armor value is greater than 15 |

### `helmetValueHigh`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                              Effect                               |
|:---------------------------------------------------------------------:|:-----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_high.js` | It will only be displayed when the maid's armor value is in 10~15 |

### `chestPlateValueHigh` `chestPlateLeftValueHigh` `chestPlateMiddleValueHigh` `chestPlateRightValueHigh`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                              Effect                               |
|:---------------------------------------------------------------------:|:-----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_high.js` | It will only be displayed when the maid's armor value is in 10~15 |

### `leggingsValueHigh` `leggingsLeftValueHigh` `leggingsMiddleValueHigh` `leggingsRightValueHigh`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                              Effect                               |
|:---------------------------------------------------------------------:|:-----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_high.js` | It will only be displayed when the maid's armor value is in 10~15 |

### `bootsLeftValueHigh` `bootsRightValueHigh`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                        |                              Effect                               |
|:---------------------------------------------------------------------:|:-----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_high.js` | It will only be displayed when the maid's armor value is in 10~15 |

### `helmetValueNormal`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                        Animation Reference Path                         |                              Effect                              |
|:-----------------------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_normal.js` | It will only be displayed when the maid's armor value is in 5~10 |

### `chestPlateValueNormal` `chestPlateLeftValueNormal` `chestPlateMiddleValueNormal` `chestPlateRightValueNormal`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                        Animation Reference Path                         |                              Effect                              |
|:-----------------------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_normal.js` | It will only be displayed when the maid's armor value is in 5~10 |

### `leggingsValueNormal` `leggingsLeftValueNormal` `leggingsMiddleValueNormal` `leggingsRightValueNormal`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                        Animation Reference Path                         |                              Effect                              |
|:-----------------------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_normal.js` | It will only be displayed when the maid's armor value is in 5~10 |

### `bootsLeftValueNormal` `bootsRightValueNormal`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                        Animation Reference Path                         |                              Effect                              |
|:-----------------------------------------------------------------------:|:----------------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_normal.js` | It will only be displayed when the maid's armor value is in 5~10 |

### `helmetValueLow`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                          Effect                           |
|:--------------------------------------------------------------------:|:---------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_low.js` | Only displayed when the maid's armor value is less than 5 |

### `chestPlateValueLow` `chestPlateLeftValueLow` `chestPlateMiddleValueLow` `chestPlateRightValueLow`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                          Effect                           |
|:--------------------------------------------------------------------:|:---------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_low.js` | Only displayed when the maid's armor value is less than 5 |

### `leggingsValueLow` `leggingsLeftValueLow` `leggingsMiddleValueLow` `leggingsRightValueLow`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                          Effect                           |
|:--------------------------------------------------------------------:|:---------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_low.js` | Only displayed when the maid's armor value is less than 5 |

### `bootsLeftValueLow` `bootsRightValueLow`

It has nothing to do with whether the armor is equipped or not, just describe its role.

|                       Animation Reference Path                       |                          Effect                           |
|:--------------------------------------------------------------------:|:---------------------------------------------------------:|
| `touhou_little_maid:animation/maid/default/armor/value/value_low.js` | Only displayed when the maid's armor value is less than 5 |

## Chair's Preset Animation
Preset animation for chair only, pay attention to the case of the group name.
### `passengerRotationYaw`
|                  Animation Reference Path                  |                                   Effect                                   |
|:----------------------------------------------------------:|:--------------------------------------------------------------------------:|
| `touhou_little_maid:animation/chair/passenger/rotation.js` | According to the passenger’s yaw, set the corresponding yaw for this group |

### `passengerRotationPitch`
|                  Animation Reference Path                  |                                     Effect                                     |
|:----------------------------------------------------------:|:------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/chair/passenger/rotation.js` | According to the passenger’s pitch, set the corresponding pitch for this group |

### `passengerHidden`
|                 Animation Reference Path                 |                       Эффект                        |
|:--------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/chair/passenger/hidden.js` | When the chair has a passenger, the group is hidden |

### `passengerShow`
|                 Animation Reference Path                 |                         Эффект                         |
|:--------------------------------------------------------:|:------------------------------------------------------:|
| `touhou_little_maid:animation/chair/passenger/hidden.js` | When the chair has a passenger, the group is displayed |

## All Preset Animation
Preset animation for all, pay attention to the case of the group name.
### `sinFloat` `cosFloat` `_sinFloat` `_cosFloat`
|               Animation Reference Path               |                             Эффект                              |
|:----------------------------------------------------:|:---------------------------------------------------------------:|
| `touhou_little_maid:animation/base/float/default.js` | According to the pivot point of the group, it float up and down |

### `overWorldHidden`
|                 Animation Reference Path                 |                       Эффект                        |
|:--------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Hide the group when the entity is in the over world |

### `overWorldShow`
|                 Animation Reference Path                 |                       Эффект                        |
|:--------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Show the group when the entity is in the over world |

### `netherWorldHidden`
|                 Animation Reference Path                 |                        Эффект                         |
|:--------------------------------------------------------:|:-----------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Hide the group when the entity is in the nether world |

### `netherWorldShow`
|                 Animation Reference Path                 |                        Эффект                         |
|:--------------------------------------------------------:|:-----------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Show the group when the entity is in the nether world |

### `endWorldHidden`
|                 Animation Reference Path                 |                       Эффект                       |
|:--------------------------------------------------------:|:--------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Hide the group when the entity is in the end world |

### `endWorldShow`
|                 Animation Reference Path                 |                       Эффект                       |
|:--------------------------------------------------------:|:--------------------------------------------------:|
| `touhou_little_maid:animation/base/dimension/default.js` | Show the group when the entity is in the end world |

### `xRotationLowA` `xRotationLowB` `xRotationLowC` `xRotationLowD` `xRotationLowE`
|                  Animation Reference Path                   |                            Эффект                             |
|:-----------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/x_low_speed.js` | Pivot point as center, X direction of rotation axis, 72 sec/r |

### `yRotationLowA` `yRotationLowB` `yRotationLowC` `yRotationLowD` `yRotationLowE`
|                  Animation Reference Path                   |                            Эффект                             |
|:-----------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/y_low_speed.js` | Pivot point as center, Y direction of rotation axis, 72 sec/r |

### `zRotationLowA` `zRotationLowB` `zRotationLowC` `zRotationLowD` `zRotationLowE`
|                  Animation Reference Path                   |                            Эффект                             |
|:-----------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/z_low_speed.js` | Pivot point as center, Z direction of rotation axis, 72 sec/r |

### `xRotationNormalA` `xRotationNormalB` `xRotationNormalC` `xRotationNormalD` `xRotationNormalE`
|                    Animation Reference Path                    |                            Эффект                             |
|:--------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/x_normal_speed.js` | Pivot point as center, X direction of rotation axis, 18 sec/r |

### `yRotationNormalA` `yRotationNormalB` `yRotationNormalC` `yRotationNormalD` `yRotationNormalE`
|                    Animation Reference Path                    |                            Эффект                             |
|:--------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/y_normal_speed.js` | Pivot point as center, Y direction of rotation axis, 18 sec/r |

### `zRotationNormalA` `zRotationNormalB` `zRotationNormalC` `zRotationNormalD` `zRotationNormalE`
|                    Animation Reference Path                    |                            Эффект                             |
|:--------------------------------------------------------------:|:-------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/z_normal_speed.js` | Pivot point as center, Z direction of rotation axis, 18 sec/r |

### `xRotationHighA` `xRotationHighB` `xRotationHighC` `xRotationHighD` `xRotationHighE`
|                   Animation Reference Path                   |                             Эффект                             |
|:------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/x_high_speed.js` | Pivot point as center, X direction of rotation axis, 4.5 sec/r |

### `yRotationHighA` `yRotationHighB` `yRotationHighC` `yRotationHighD` `yRotationHighE`
|                   Animation Reference Path                   |                             Эффект                             |
|:------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/y_high_speed.js` | Pivot point as center, Y direction of rotation axis, 4.5 sec/r |

### `zRotationHighA` `zRotationHighB` `zRotationHighC` `zRotationHighD` `zRotationHighE`
|                   Animation Reference Path                   |                             Effect                             |
|:------------------------------------------------------------:|:--------------------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/z_high_speed.js` | Pivot point as center, Z direction of rotation axis, 4.5 sec/r |

### `xReciprocate`
|                  Animation Reference Path                   |                       Эффект                        |
|:-----------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/reciprocate.js` | Pivot point as center, reciprocate swing at X angle |

### `yReciprocate`
|                  Animation Reference Path                   |                       Эффект                        |
|:-----------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/reciprocate.js` | Pivot point as center, reciprocate swing at Y angle |

### `zReciprocate`
|                  Animation Reference Path                   |                       Эффект                        |
|:-----------------------------------------------------------:|:---------------------------------------------------:|
| `touhou_little_maid:animation/base/rotation/reciprocate.js` | Pivot point as center, reciprocate swing at Z angle |

### `dayShow`
|                   Animation Reference Path                   |           Эффект           |
|:------------------------------------------------------------:|:--------------------------:|
| `touhou_little_maid:animation/base/time/day_night_hidden.js` | Show the group when in day |

### `nightShow`
|                   Animation Reference Path                   |            Эффект            |
|:------------------------------------------------------------:|:----------------------------:|
| `touhou_little_maid:animation/base/time/day_night_hidden.js` | Show the group when in night |

### `gameHourRotationX`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the X angle of the group according to the time in the game |

### `gameMinuteRotationX`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the X angle of the group according to the time in the game |

### `gameHourRotationY`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the Y angle of the group according to the time in the game |

### `gameMinuteRotationY`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the Y angle of the group according to the time in the game |

### `gameHourRotationZ`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the Z angle of the group according to the time in the game |

### `gameMinuteRotationZ`
|                 Animation Reference Path                  |                                                 Эффект                                                  |
|:---------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/game_rotation.js` | Used to make watch pointer animation, rotate the Z angle of the group according to the time in the game |

### `systemHourRotationX`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the X angle of the group according to the time in the system |

### `systemMinuteRotationX`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the X angle of the group according to the time in the system |

### `systemSecondRotationX`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the X angle of the group according to the time in the system |

### `systemHourRotationY`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Y angle of the group according to the time in the system |

### `systemMinuteRotationY`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Y angle of the group according to the time in the system |

### `systemSecondRotationY`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Y angle of the group according to the time in the system |

### `systemHourRotationZ`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Z angle of the group according to the time in the system |

### `systemMinuteRotationZ`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Z angle of the group according to the time in the system |

### `systemSecondRotationZ`
|                  Animation Reference Path                   |                                                  Эффект                                                   |
|:-----------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|
| `touhou_little_maid:animation/base/time/system_rotation.js` | Used to make watch pointer animation, rotate the Z angle of the group according to the time in the system |

## Positioning Group
For rendering positioning, based on the pivot point of the group, can be empty.
### `armLeftPositioningBone`

Position the left-handed item, its parent group must be `armLeft`

### `armRightPositioningBone`

Position the right-handed item, its parent group must be `armRight`

### `backpackPositioningBone`

Позиция лямки рюкзака, он должен быть в корневой группе
