package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

/**
 * 把 WAB 发来的命令路由到具体执行器。
 * 一次性动作（status/scan/act/hold/bind 语义之外的即时动作）当帧做完；
 * 长时间的采集任务（gather）开一个 {@link WabActions.Job}，由 {@link #tick()} 每帧驱动。
 * 每个女仆同一时间只挂一个 job，新任务会先取消旧任务。
 */
public final class WabDispatcher {
    private final WabBridge bridge;
    private final Map<java.util.UUID, WabActions.Job> jobs = new HashMap<>();

    public WabDispatcher(WabBridge bridge) {
        this.bridge = bridge;
    }

    public void dispatch(WabSession from, String id, String method, JsonObject params) {
        EntityMaid maid = bridge.resolveMaid(WabJson.str(params, "maid"));
        if (method.equals("status") || method.equals("scan")) {
            if (method.equals("status") && maid != null) {
                JsonObject data = WabSensors.status(bridge, maid);
                from.sendResult(id, true, data, null, null);
            } else {
                EntityMaid scanMaid = maid == null ? bridge.ownedMaids().stream().findFirst().orElse(null) : maid;
                if (scanMaid == null) {
                    from.sendResult(id, false, null, "no_maid", "没有可以观察的女仆。");
                } else {
                    JsonObject data = WabSensors.scan(bridge, scanMaid, params);
                    from.sendResult(id, true, data, null, null);
                }
            }
            return;
        }

        if (method.equals("see")) {
            // 她「眼前」有什么：文字先回，照片是客户端那一趟来回，只能随后单独送一帧。
            EntityMaid watcher = maid == null ? bridge.ownedMaids().stream().findFirst().orElse(null) : maid;
            if (watcher == null) {
                from.sendResult(id, false, null, "no_maid", "没有可以观察的女仆。");
                return;
            }
            JsonObject data = WabSensors.see(bridge, watcher, params);
            if (WabJson.bool(params, "photo", false)) {
                int width = WabJson.integer(params, "photo_width", 640);
                int request = bridge.requestPhoto(watcher, width);
                if (request > 0) {
                    data.addProperty("photoRequest", request);
                    data.addProperty("photoNote", "画面正从主人的客户端取，取到后另送一帧 photo。");
                } else {
                    data.addProperty("photoError", "这会儿取不到画面：主人不在、客户端没连，或上一张还没回来。");
                }
            }
            from.sendResult(id, true, data, null, null);
            return;
        }

        if (method.equals("backup")) {
            if (maid == null) {
                from.sendResult(id, false, null, "maid_not_found", "找不到这个女仆。");
                return;
            }
            from.sendResult(id, true, WabStorage.snapshot(maid), null, null);
            return;
        }

        if (method.equals("bind")) {
            // 绑定关系由 WAB 侧维护，游戏这边只需要收到 bind 帧时更新（WabBridge.onBind），
            // 主动的 bind 命令直接回一个 ok，表示游戏端不需要额外操作。
            from.sendResult(id, true, new JsonObject(), null, null);
            return;
        }

        if (method.equals("plan")) {
            if (maid == null) {
                from.sendResult(id, false, null, "maid_not_found", "找不到这个女仆。");
                return;
            }
            respond(from, id, WabActions.plan(bridge, maid, params));
            return;
        }

        if (method.equals("chat")) {
            // 聊天由 WAB 侧驱动，游戏这边只负责把话递过去（WabBridge.onChat），不处理这里的命令。
            from.sendResult(id, true, new JsonObject(), null, null);
            return;
        }

        if (method.equals("go") || method.equals("act") || method.equals("attack")
                || method.equals("gather") || method.equals("hold")) {
            if (!bridge.allowWorldActions() && WabGate.commandNeedsWorldActions(method, params)) {
                from.sendResult(id, false, null, "world_actions_disabled", "游戏端没有开放世界操作权限。");
                return;
            }
            if (maid == null) {
                from.sendResult(id, false, null, "maid_not_found", "找不到这个女仆。");
                return;
            }
            switch (method) {
                case "go" -> respond(from, id, WabActions.go(bridge, maid, params));
                case "act" -> respond(from, id, WabActions.act(bridge, maid, params));
                case "attack" -> respond(from, id, WabActions.attack(bridge, maid, params));
                case "hold" -> respond(from, id, WabActions.hold(bridge, maid, params));
                case "gather" -> {
                    String gatherAction = WabJson.str(params, "action").toLowerCase(Locale.ROOT);
                    if ("collect".equals(gatherAction)) {
                        respond(from, id, WabActions.collect(bridge, maid, params));
                        return;
                    }
                    if ("fill".equals(gatherAction)) {
                        respond(from, id, WabActions.fill(bridge, maid, params));
                        return;
                    }
                    cancelJob(maid, "被新的采集任务取代");
                    WabActions.Responder responder = new WabActions.Responder() {
                        @Override
                        public void success(JsonObject data) {
                            from.sendResult(id, true, data, null, null);
                        }

                        @Override
                        public void failure(String code, String message) {
                            from.sendResult(id, false, null, code, message);
                        }
                    };
                    WabActions.Job job = WabActions.startGather(bridge, maid, params, 500, false, responder);
                    jobs.put(maid.getUUID(), job);
                }
                default -> from.sendResult(id, false, null, "unknown_method", "不认识的方法：" + method);
            }
            return;
        }

        from.sendResult(id, false, null, "unknown_method", "不认识的方法：" + method);
    }

    /**
     * 一次性动作的统一回执：null 表示「做完了，没有额外数据」；
     * 带 ok=false 的按失败送出；其余整个对象当 data 原样送回——她那句「正在靠近 ××」就靠这条通道回到 AI 手里。
     */
    private void respond(WabSession from, String id, JsonObject result) {
        if (result == null) {
            from.sendResult(id, true, new JsonObject(), null, null);
            return;
        }
        if (result.has("ok") && !WabJson.bool(result, "ok", true)) {
            from.sendResult(id, false, null, WabJson.str(result, "code", "command_failed"),
                    WabJson.str(result, "error", "命令没有成功。"));
            return;
        }
        JsonObject data = WabJson.obj(result, "data");
        from.sendResult(id, true, data.size() > 0 ? data : result, null, null);
    }

    public boolean hasJob(EntityMaid maid) {
        WabActions.Job job = jobs.get(maid.getUUID());
        return job != null && job.isActive();
    }

    public void noteJob(WabActions.Job job) {
        jobs.put(job.maid.getUUID(), job);
    }

    public void cancelJob(EntityMaid maid, String reason) {
        WabActions.Job job = jobs.remove(maid.getUUID());
        if (job != null) job.cancel(reason);
    }

    public void cancelAll(String reason) {
        for (WabActions.Job job : new java.util.ArrayList<>(jobs.values())) {
            // 必须走 cancel：只有它会把任务压下去的 guiOpening 还原，不然她到停服都不会再动。
            if (job != null) job.cancel(reason);
        }
        jobs.clear();
    }

    public void tick() {
        for (java.util.UUID key : new java.util.ArrayList<>(jobs.keySet())) {
            WabActions.Job job = jobs.get(key);
            if (job == null || !job.isActive()) {
                jobs.remove(key);
                continue;
            }
            job.tick();
            if (!job.isActive()) {
                jobs.remove(key);
                // 干完一件活（采集等），按概率向她主人讨摸摸。
                WabHeadPat.maybeRequest(job.maid);
            }
        }
    }
}
