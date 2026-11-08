# 车万女仆 · WAB 版（NeoForge 1.21.1）

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.1%20NeoForge-4A7C59" alt="1.21.1 NeoForge">
  <img src="https://img.shields.io/badge/分支-已与原版完全分化-B98A96" alt="Diverged">
  <img src="https://img.shields.io/badge/配套-温暖如初%20WAB-8A4A56" alt="WarmAsBefore">
  <img src="https://img.shields.io/badge/许可-MIT%20%2F%20CC%20BY--NC--SA%204.0-green" alt="License">
</p>

> **这是车万女仆的一个完全分化的分支**，为《温暖如初（WarmAsBefore，简称 WAB）》提供 AI 桥接。
> `modId`、注册名、模型与玩法与原版保持一致，所以它能**直接顶替官方 jar**；
> 但代码已经和上游分道扬镳——这里加的东西，原版没有。

- 本仓库：`gongchanwangzilin/TouhouLittleMaid-WAB`（本分支 `ci/wab-1.21`）
- 上游：`TartaricAcid/TouhouLittleMaid`（MIT / CC BY-NC-SA 4.0）
- 配套应用：温暖如初（Windows / Android，本地优先的 AI 角色陪伴应用）

---

## 这一版加了什么

不连 WAB 时行为与原版一致；连上之后才是完全体。

| 模块 | 能力 |
|---|---|
| **AI 接管** | 绑定 WAB 角色的女仆，游戏内聊天由 WAB 的人来答，带记忆、性格与上下文 |
| **WAB 手机** | 手机饰品右键开屏：联系人 / 消息 / 发送；戴手机的女仆自动进通讯录 |
| **语音** | TTS 合成与 STT 听写借 WAB 的嗓子与耳朵，音色按角色设置 |
| **感知** | `state` 帧：位置、维度、血量、饱食、任务、爱憎、是否戴手机；`event` 帧：互动、受伤、死亡、复活、主人睡觉、同伴搭话、投喂、干完活、天气时段 |
| **看画面** | 需要时截图交给 WAB 识图；没有识图端点时用大模型兜底 |
| **工具** | 11 个 `maid_*` 工具：`status / scan / go / act / hold / gather / see / plan / backup / bind / feed_rules` |
| **场景思考** | 被打、死亡、复活、主人睡觉、同伴搭话、天气、时段、被互动，各自可勾选、带冷却 |
| **日程与自发** | 日程随存档走；自发行为与主动思考间隔可调 |
| **投喂与手感** | 口味表（喜爱/挑食/吃腻）、饿时讨食、摸头讨摸、怕黑、好感 HUD、头顶数值、详情页 |
| **性能** | 女仆周围强制加载（半径 0–8，离开即释放，20 tick 对账） |
| **兼容** | 爱憎分明反射兼容层：装了就读，没装退回原生 |

> 强制加载与附属《万法皆通·女仆饰品锚定核心》功能重叠，开启后该饰品会失效，游戏内会给出警告。

---

## 安装

1. Minecraft **1.21.1 + NeoForge 21.x**
2. 把 `touhoulittlemaid-1.5.3-neoforge+mc1.21.1-wab.1-all.jar` 放进 `mods/`
   - 已有官方车万女仆 jar 请删掉（`modId` 相同会冲突）
3. 进游戏后在设置 → 模组设置里改桥接参数

## 与 WAB 连接

WAB 插件 `wab_minecraft` 填 TCP 端口（默认 27321），需要时打开局域网；游戏侧戴手机上号，绑定角色后聊天与语音都走 WAB。

---

## 构建

需要 **JDK 21**。

```bash
./gradlew build -x test
```

产物：`build/libs/touhoulittlemaid-1.5.3-neoforge+mc1.21.1-wab.1-all.jar`。

CI：推送到 `ci/wab-1.21` 会自动编译并上传 artifact。

## 分支说明

| 分支 | 用途 |
|---|---|
| `ci/wab-1.21` | 1.21.1 NeoForge 发布线（本 README 所在） |
| `ci/wab-1.20-testkit` | 1.20.1 Forge 发布线 |

---

## 许可与致谢

- 上游代码与素材：`TartaricAcid/TouhouLittleMaid`，MIT / CC BY-NC-SA 4.0
- 模型、音效、贴图版权归原作者与贡献者
- 本分支新增桥接层同属上述许可，不作商业分发

### Credits

TartaricAcid 与车万女仆全体贡献者 / MMM & Verclene（Little Maid Mob）/ ZUN（Touhou Project）/
あみたろの声素材工房 / xechat-idea、xqwlight、xqbase（棋类 AI）

## 反馈

本分支问题请提到本仓库 Issues，并附 `latest.log` 与 WAB 的 `bridge_diag.log`。
