# 车万女仆 · WAB 版

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1%20Forge-4A7C59" alt="1.20.1 Forge">
  <img src="https://img.shields.io/badge/分支-已与原版完全分化-B98A96" alt="Diverged">
  <img src="https://img.shields.io/badge/配套-温暖如初%20WAB-8A4A56" alt="WarmAsBefore">
  <img src="https://img.shields.io/badge/许可-MIT%20%2F%20CC%20BY--NC--SA%204.0-green" alt="License">
</p>

> **这是车万女仆的一个完全分化的分支**，为《温暖如初（WarmAsBefore，简称 WAB）》提供 AI 桥接。
> `modId`、注册名、模型与玩法与原版保持一致，所以它能**直接顶替官方 jar** 放进 `mods/`；
> 但代码已经和上游分道扬镳——这里加的东西，原版没有。

- 本仓库：`gongchanwangzilin/TouhouLittleMaid-WAB`
- 上游：`TartaricAcid/TouhouLittleMaid`（MIT / CC BY-NC-SA 4.0，代码与素材版权归原作者）
- 配套应用：温暖如初（Windows / Android，本地优先的 AI 角色陪伴应用）

---

## 这一版加了什么

不连 WAB 时，它的行为与原版一致；连上之后，才是完全体。

| 模块 | 能力 |
|---|---|
| **AI 接管** | 绑定了 WAB 角色的女仆，游戏内聊天由 WAB 那边的人来答，带记忆、性格与上下文；游戏侧不再调自己的 LLM |
| **WAB 手机** | 手机饰品右键开屏：联系人 / 消息 / 发送；戴手机的女仆自动进通讯录；可显示在主窗口或独立手机窗口 |
| **语音** | TTS 合成与 STT 听写借 WAB 的嗓子和耳朵，音色、参考音全按角色自己的设置 |
| **感知** | `state` 帧上报位置、维度、血量、饱食、当前任务、爱憎数值、是否戴手机；`event` 帧上报互动、受伤、死亡、复活、主人睡觉、同伴搭话、投喂、干完活、天气时段 |
| **看画面** | 需要时请求主人客户端截图，交给 WAB 识图；WAB 没配识图端点时用大模型兜底 |
| **工具** | 11 个 `maid_*` 工具：`status / scan / go / act / hold / gather / see / plan / backup / bind / feed_rules`，由 AI 自己决定做什么 |
| **场景思考** | 被打、死亡、复活、主人睡觉、同伴搭话、天气变、时段变、被互动——每类可单独勾选，带冷却；她用自己的性格想一次 |
| **日程与自发** | 日程写进女仆持久数据、跟着存档走；自发行为与主动思考间隔可调 |
| **投喂** | 口味表（喜爱 / 挑食 / 吃腻）、饿时讨食、按口味给不同反应 |
| **手感** | 摸头讨摸、怕黑特质、好感 HUD、头顶数值、详情分页屏 |
| **性能** | 女仆周围强制加载（半径 0–8，人走即释放，20 tick 对账） |
| **兼容** | 「爱憎分明（Love & Loathe）」反射兼容层：装了就读它的数值，没装安静退回原生 |

### 强制加载与「锚定核心」

开启「女仆周围强制加载」后，车万女仆附属《万法皆通·女仆饰品锚定核心》会因功能重叠而失效。
游戏内与设置页都会给出警告——两者不要同时用。

---

## 安装

1. Minecraft **1.20.1 + Forge 47.x**
2. 把 `touhoulittlemaid-1.5.3-forge+mc1.20.1-wab.2-all.jar` 放进 `mods/`
   - 如果已有官方车万女仆 jar，**删掉它**：`modId` 相同，两个同时放会冲突
3. （可选）`cloth-config` —— 用来在游戏内改桥接设置
4. 启动游戏，进入世界后由 WAB 发起连接

> 只想要原版玩法：不装 / 不连 WAB 即可，桥接层所有改动都以「连上了」为前提，断桥会清空临时状态。

## 与 WAB 连接

- WAB 侧：插件 `wab_minecraft` 里填 TCP 端口（默认 27321），需要局域网时打开「允许局域网连接」
- 游戏侧：女仆戴 WAB 手机 → 自动同步联系人；绑定角色后聊天与语音都走 WAB
- 连接是 UDP 发现 + HMAC 配对 + TCP 长连接，断线自动重连

## 游戏内设置（24 项）

启用桥接 / TCP 端口 / 允许局域网 / 自动建角色 / 允许世界动作 / 允许动作与语录 /
允许看一眼画面 / 借 WAB 的声音 / 聊天判好感 / 主动思考间隔 / 她会为什么事想一次（多选）/
思考冷却 / 陪睡爱语 / 允许切存档 / 自动打开绑定存档 / Galgame 界面 / 手机显示位置 /
快照间隔 / 自动补口味 / 饿时讨食 / 讨食阈值 / 数值越高越饿 / 强制加载 / 强制加载半径

入口：设置 → 模组设置 → 车万女仆（Cloth Config）。

---

## 构建

需要 **JDK 17**。

```bash
./gradlew build -x test
```

产物：`build/libs/touhoulittlemaid-1.5.3-forge+mc1.20.1-wab.2-all.jar`（`-all` 才是可装载的完整包）。

CI：推送到 `ci/wab-1.20-testkit` 分支会自动编译并把 jar 传到 Actions artifact。

## 分支说明

| 分支 | 用途 |
|---|---|
| `ci/wab-1.20-testkit` | 1.20.1 Forge 的发布线（本 README 所在） |
| `1.20` | 跟随上游的基线，不含 WAB 改动 |

---

## 已知限制

- **附属模组**：按 `modId = touhou_little_maid` 依赖，所以本分支可以正常被附属加载；但附属若校验上游版本号，带 `-wab.2` 这类后缀的版本可能不被识别
- **不支持热切换**：从原版 jar 换到本分支（或反向）请重启游戏
- **服务端**：桥接只在单人/集成服务器或你自己的服务器上有意义；专用服务器不装 WAB 时行为等于原版

## 许可与致谢

- 上游代码与素材：`TartaricAcid/TouhouLittleMaid`，MIT / CC BY-NC-SA 4.0
- 模型、音效、贴图等资源版权归原作者与贡献者所有
- 本分支新增的桥接层同属上述许可；不以商业目的分发

### Credits

- TartaricAcid 及车万女仆全体贡献者：上游模组
- MMM / Verclene：Little Maid Mob
- ZUN：Touhou Project
- 音效：あみたろの声素材工房 / LittleMaid PEKO Sound
- 棋类 AI：xechat-idea / xqwlight / xqbase
- 以及所有提交过 issue、帮忙测试的人

## 反馈

- 本分支问题：请提到本仓库 Issues，并附 `latest.log` 与 WAB 的 `bridge_diag.log`
- 上游玩法问题：请提到 `TartaricAcid/TouhouLittleMaid`
