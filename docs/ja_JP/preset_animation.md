

# プリセット アニメーション

There are many default animations built in the mod, you only need:
1. Blockbenchでアニメーション化するグループの名前を設定します。
2. Call the animation reference of the path in the `animation` field of the `maid_model.json` file.

> The `tlm-utils` plugin can automatically call the animation script reference based on the group name, you just need to click the `Re-Analyze Animation` button. ![img](https://i.imgur.com/iyCKwMx.gif)

The Blockbench plugin provides all available preset animations, you can right-click in the outliner interface to open the preset animation menu   
![img](https://i.imgur.com/N17PbiE.gif)


## Positioning Group
For rendering positioning, based on the pivot point of the group, can be empty.
### `armLeftPositioningBone`

Position the left-handed item, its parent group must be `armLeft`

### `armRightPositioningBone`

Position the right-handed item, its parent group must be `armRight`

### `backpackPositioningBone`

Position the backpack shoulder strap, it must be the root group