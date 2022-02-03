

# 预设动画

有许多默认动画已经建立在模组中，你只需要：
1. 设置您想要在 Blockbench 中动画组的正确名称。
2. 在 `maid_model.json` 文件的 `动画（animation）` 字段中调用路径的动画参考值。

> `tlm-utils` 插件可以自动调用基于群组名称的动画脚本引用 您只需要点击 `重新分析动画` 按钮。 ![img](https://i.imgur.com/iyCKwMx.gif)

Blockbech插件提供了所有可用的预设动画，您可以右键点击outliner 界面打开预设动画菜单   
![img](https://i.imgur.com/N17PbiE.gif)


## 定位组
渲染定位，基于组的枢轴点（the pivot point）可以为空。
### `左手臂位置（armLeftPositioningBone）`

左手物品的位置，其父组必须是 `armLeft`

### `右手臂位置（armRightPositioningBone）`

右手物品的位置，其父组必须是 `armRight`

### `背包位置（backpackPositioningBone）`

背包肩带位置，它必须是根组（the root group）