package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAndItemTransformEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模组侧的桥接门面：持有服务器、会话、女仆缓存与主动行为。
 * 网络线程只读写 JSON；所有世界操作都回到服务器线程执行。
 */
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WabBridge {
    private static final String PROTOCOL = "1";
    /**
     * 桥自己的构建号。必须单独有：我们的 jar 顶替上游，modVersion 就得跟上游同号（1.5.3-forge+mc1.20.1），
     * 否则宿主拿到的是「车万女仆的版本」，永远看不出用户装的桥是上个月那一份还是今天这一份。
     * 加协议无关的新字段、新事件时 +0.1；改线上帧的语义才动 PROTOCOL。
     */
    private static final String BRIDGE_BUILD = "1.6.0";
    private static final String MAGIC = "WAB-MAID";
    /** 协议承诺的上限：单次会话的女仆数、事件冷却缓存、世界绑定。 */
    private static final int MAX_MAIDS = 256;
    private static final int MAX_EVENTS = 128;
    private static final int MAX_BINDINGS = 64;
    private static WabBridge instance;

    private final MinecraftServer server;
    private final String instanceId;
    private final WabDiscovery discovery;
    private final WabDispatcher dispatcher;
    private final WabAutonomy autonomy;

    private volatile WabSession session;
    private ServerPlayer owner;
    private String ownerName = "";
    private String worldName = "";
    /** 上一轮扫描数出来的「每个玩家名下有几只女仆」：主人认这个，不认谁先服。见 pickOwner。 */
    private final Map<UUID, Integer> maidsByOwner = new HashMap<>();

    private final Map<UUID, EntityMaid> maids = new ConcurrentHashMap<>();
    private final Map<UUID, WabHud.Stats> stats = new ConcurrentHashMap<>();
    private final Map<UUID, String> boundCharacters = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastEventAt = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> followTargets = new HashMap<>();
    private volatile JsonArray beaconMaids = new JsonArray();
    private volatile boolean allowWorldActions = true;
    private volatile boolean allowGestures = true;
    /** 宿主在 welcome 里宣告的语音能力；断线就归零，模组立刻回到自己的 TTS/STT。 */
    private volatile boolean voiceTts;
    private volatile boolean voiceStt;
    /** 交给 WAB 作答的聊天：女仆 UUID → 那句「思考中」气泡的 id，回话后换成正文，失败就撤掉。 */
    private final Map<UUID, Long> pendingReplies = new ConcurrentHashMap<>();
    /** 死过的女仆：她的 UUID → 死时候的名字和时间。复活在原版里没有事件，只能自己认，见 onMaidTransform 与 noteReappear。 */
    private final Map<UUID, Mourned> mourned = new ConcurrentHashMap<>();
    /** 从胶片里爬出来的是全新实体（UUID 整个换掉）：ToMaid 事件先把「这个新 UUID 是谁回来了」钉住。 */
    private final Map<UUID, Mourned> reviving = new ConcurrentHashMap<>();
    /** 认尸不认太久：过窗的死亡不再等复活，免得撞上之后新收的女仆。 */
    private static final long MOURN_WINDOW_MS = 20 * 60 * 1000L;
    /** 上一对女仆互相搭话的时间，键是两个 UUID 排序后拼起来的串。 */
    private final Map<String, Long> pairChats = new ConcurrentHashMap<>();
    /** 一对女仆这一轮已经你来我往了几句。冷场过冷却就归零。 */
    private final Map<String, Integer> pairHops = new ConcurrentHashMap<>();
    private static final int MAX_PAIR_HOPS = 4;
    /** 发出去的拍照请求：requestId → 女仆 + 发出时间，超时也要给 WAB 一个交代。 */
    private final Map<Integer, PendingPhoto> pendingPhotos = new ConcurrentHashMap<>();
    private int photoSequence = 1;

    /** 一张照片最多多少字节。超了就明说「拍不下来」，不能拿 4MB 的行上限当许可。 */
    private static final int MAX_PHOTO_BYTES = 1_200_000;
    private static final long PHOTO_TIMEOUT_MS = 8000;

    private record PendingPhoto(UUID maid, long sentAt) {
    }

    /** 一条「等她回来」的记录：原来叫什么、什么时候记的、死那个的 UUID 是哪个。 */
    private record Mourned(String name, long at, UUID deadId) {
    }

    private int tickCounter;
    private long lastStateAt;
    private String lastStateHash = "";
    private boolean ownerWasSleeping;

    private WabBridge(MinecraftServer server) {
        this.server = server;
        this.instanceId = makeInstanceId(server);
        this.discovery = new WabDiscovery(this);
        this.dispatcher = new WabDispatcher(this);
        this.autonomy = new WabAutonomy(this);
        refreshOwner();
        this.discovery.start();
        TouhouLittleMaid.LOGGER.info("[WAB] bridge ready, instance={}, world={}, owner={}", instanceId, worldName, ownerName);
    }

    public static WabBridge get() {
        return instance;
    }

    public MinecraftServer server() {
        return server;
    }

    public String instanceId() {
        return instanceId;
    }

    public WabSession session() {
        return session;
    }

    public boolean connected() {
        WabSession current = session;
        return current != null && !current.isClosed();
    }

    public ServerPlayer owner() {
        return owner;
    }

    public String ownerName() {
        return ownerName;
    }

    public String worldName() {
        return worldName;
    }

    public boolean allowWorldActions() {
        return allowWorldActions;
    }

    public boolean allowGestures() {
        return allowGestures;
    }

    /** WAB 现在能不能合成语音；会话断了就当不能，模组立刻回到自己的实现。 */
    public boolean voiceTts() {
        return voiceTts && connected();
    }

    /** WAB 现在能不能听写；会话断了就当不能。 */
    public boolean voiceStt() {
        return voiceStt && connected();
    }

    /** 这具身体是不是绑了 WAB 角色。绑了才继承角色自己的 LLM 与嗓子。 */
    public boolean isBound(EntityMaid maid) {
        return maid != null && boundCharacters.containsKey(maid.getUUID());
    }

    public Collection<UUID> boundMaidIds() {
        return boundCharacters.keySet();
    }

    public WabDispatcher dispatcher() {
        return dispatcher;
    }

    public WabAutonomy autonomy() {
        return autonomy;
    }

    public Collection<EntityMaid> ownedMaids() {
        return maids.values();
    }

    public WabHud.Stats stats(UUID maidId) {
        return stats.get(maidId);
    }

    public String boundCharacter(UUID maidId) {
        return boundCharacters.getOrDefault(maidId, "");
    }

    public boolean isOwned(EntityMaid maid) {
        return owner != null && maid.isTame() && owner.getUUID().equals(maid.getOwnerUUID());
    }

    public EntityMaid resolveMaid(String raw) {
        String text = raw == null ? "" : raw.trim();
        if (text.isEmpty()) return primaryMaid();
        try {
            EntityMaid byId = maids.get(UUID.fromString(text));
            if (byId != null && byId.isAlive()) return byId;
        } catch (IllegalArgumentException ignored) {
        }
        for (EntityMaid maid : maids.values()) {
            if (maid.isAlive() && cleanName(maid).equalsIgnoreCase(text)) return maid;
        }
        for (EntityMaid maid : maids.values()) {
            if (maid.isAlive() && maid.getName().getString().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                return maid;
            }
        }
        return null;
    }

    private EntityMaid primaryMaid() {
        return maids.values().stream()
                .filter(EntityMaid::isAlive)
                .filter(this::isOwned)
                .sorted(Comparator
                        .comparing((EntityMaid maid) -> !boundCharacters.containsKey(maid.getUUID()))
                        .thenComparing(WabBridge::cleanName, String.CASE_INSENSITIVE_ORDER))
                .findFirst().orElse(null);
    }

    public static String cleanName(EntityMaid maid) {
        String name = maid.getName().getString();
        StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '§') {
                i++;
                continue;
            }
            sb.append(c);
        }
        return sb.toString().trim();
    }

    public void attach(WabSession incoming, JsonObject welcome) {
        WabSession old = session;
        if (old != null && old != incoming) old.close("replaced by a new session");
        session = incoming;
        server.execute(() -> {
            onWelcome(incoming, welcome);
            incoming.startReader();
        });
    }

    void detach(WabSession expected) {
        if (session != expected) return;
        session = null;
        TouhouLittleMaid.LOGGER.info("[WAB] session closed, return to discovery");
        // close() 可能是读线程、写线程或超时判定叫的，而下面要碰实体和 HashMap，必须挪回服务器线程。
        // 断线不等于她会自动停下：跟随表还在的话每 20 tick 重新挤到主人身边，
        // 采集任务会一路挖到 tick 预算用完——「WAB 没跑 = 上游行为」这条就不成立了。
        server.execute(() -> {
            dispatcher.cancelAll("WAB 断开了");
            voiceTts = false;
            voiceStt = false;
            // 宿主发下来的插件字段表跟着作废：不然设置页还画着一排点得动、却再也发不出去的开关。
            WabRemoteConfig.clear();
            WabVoice.reset(this);
            clearPendingReplies();
            for (Map.Entry<UUID, UUID> entry : new HashMap<>(followTargets).entrySet()) {
                EntityMaid maid = maids.get(entry.getKey());
                if (maid == null) continue;
                clearFollow(maid);
                if (maid.isAlive()) maid.getNavigation().stop();
            }
            followTargets.clear();
        });
    }

    void handleFrame(WabSession from, JsonObject frame) {
        if (from != session) return;
        String type = WabJson.str(frame, "type").toLowerCase(Locale.ROOT);
        switch (type) {
            case "welcome" -> onWelcome(from, frame);
            case "bind" -> onBind(frame);
            case "stats" -> onStats(frame);
            case "chat_reply" -> onChatReply(frame);
            case "tts_reply" -> WabVoice.onTtsReply(this, frame);
            case "tts_audio" -> WabVoice.onTtsAudio(this, frame);
            case "stt_reply" -> WabVoice.onSttReply(this, frame);
            case "settings" ->
                    // WAB 在设置页改了插件设置后只推这一帧、不断线；不接的话游戏侧的闸门会一直停在握手时的值。
                    applyRemote(frame);
            case "command" -> {
                String id = WabJson.str(frame, "id");
                String method = WabJson.str(frame, "method").toLowerCase(Locale.ROOT);
                JsonObject params = WabJson.obj(frame, "params");
                try {
                    dispatcher.dispatch(from, id, method, params);
                } catch (Exception e) {
                    // 坏数据不该把服务器线程带进去：回一帧错误，调用方至少能立刻拿到结果。
                    TouhouLittleMaid.LOGGER.warn("[WAB] command {} failed: {}", method, e.toString());
                    from.sendResult(id, false, null, "internal_error", "游戏端执行这条命令时出了错。");
                }
            }
            case "error" -> {
                String code = WabJson.str(frame, "code");
                String message = WabJson.str(frame, "message");
                TouhouLittleMaid.LOGGER.warn("[WAB] WAB error {} {}", code, message);
                // 游戏里改了插件设置被宿主拒绝：这句得让主人看见，否则滑块弹回去却不知道为什么不生效。
                if ("config_rejected".equals(code)) {
                    ServerPlayer player = currentOwner();
                    if (player != null) {
                        player.displayClientMessage(Component.literal("WarmAsBefore 没接受这项设置：" + message)
                                .withStyle(ChatFormatting.GRAY), false);
                    }
                }
            }
            default -> {
            }
        }
    }

    private void onWelcome(WabSession from, JsonObject welcome) {
        applyRemote(welcome);
        boundCharacters.clear();
        JsonObject bound = WabJson.obj(welcome, "bound");
        for (String maidId : bound.keySet()) {
            try {
                rememberCharacter(UUID.fromString(maidId), WabJson.str(bound, maidId));
            } catch (IllegalArgumentException ignored) {
            }
        }
        TouhouLittleMaid.LOGGER.info("[WAB] handshake ok, save={}, character={}, bound={}",
                WabJson.str(welcome, "saveId"), WabJson.str(welcome, "characterId"), boundCharacters.size());
        updateBeaconMaids();
        WabVoice.pushClientState(this);
        sendState(true);
        if (owner != null) {
            owner.displayClientMessage(Component.literal("温暖如初已连接").withStyle(ChatFormatting.GRAY), true);
        }
    }

    /**
     * welcome 与 settings 帧共用一份收口：宿主改完插件设置推的 settings 帧，形状跟握手时几乎一样
     * （settings + voice + fields），分开写两遍迟早漏掉其中一样。
     * 老版 WAB 不发 voice / fields，缺省就保持现值——绑了角色也仍用游戏自己的语音，设置页只留一行说明。
     */
    private void applyRemote(JsonObject frame) {
        JsonObject settings = WabJson.obj(frame, "settings");
        // 老宿主把开关直接摊在帧根上，新版才收进 settings——两种形状都认，别因为一次升级就把闸门冻在旧值。
        applySettings(settings.size() > 0 ? settings : frame);
        if (frame.has("voice") && applyVoice(WabJson.obj(frame, "voice"))) {
            WabVoice.pushClientState(this);
        }
        if (frame.has("fields")) {
            WabRemoteConfig.apply(WabJson.arr(frame, "fields"));
        }
    }

    /** 语音能力位只在真的变了才惊动客户端——每次握手都推一遍状态会白白重排一次女仆名册。 */
    private boolean applyVoice(JsonObject voice) {
        boolean tts = WabJson.bool(voice, "tts", voiceTts);
        boolean stt = WabJson.bool(voice, "stt", voiceStt);
        if (tts == voiceTts && stt == voiceStt) return false;
        voiceTts = tts;
        voiceStt = stt;
        return true;
    }

    /** 只认这两个开关，缺省保持当前值。 */
    private void applySettings(JsonObject settings) {
        boolean changed = false;
        if (settings.has("allowWorldActions")) {
            allowWorldActions = WabJson.bool(settings, "allowWorldActions", allowWorldActions);
            changed = true;
        }
        if (settings.has("allowGestures")) {
            allowGestures = WabJson.bool(settings, "allowGestures", allowGestures);
            changed = true;
        }
        if (changed && !allowWorldActions) {
            // 关掉世界操作的那一刻，正在跑的采集也得停：闸门只拦新指令的话，日程会继续挖下去。
            server.execute(() -> dispatcher.cancelAll("WAB 关闭了世界操作"));
        }
    }

    /** 绑定表按协议封顶 64 个世界绑定，超出后只保留先到的。 */
    private void rememberCharacter(UUID maidId, String character) {
        if (character == null || character.isEmpty()) return;
        if (!boundCharacters.containsKey(maidId) && boundCharacters.size() >= MAX_BINDINGS) return;
        boundCharacters.put(maidId, character);
    }

    private void onBind(JsonObject bind) {
        JsonObject bound = WabJson.obj(bind, "bindings");
        boundCharacters.clear();
        for (String maidId : bound.keySet()) {
            try {
                rememberCharacter(UUID.fromString(maidId), WabJson.str(bound, maidId));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (owner != null && WabJson.arr(bind, "notes").size() > 0) {
            owner.displayClientMessage(Component.literal(WabJson.str(bind, "notes")).withStyle(ChatFormatting.GRAY), false);
        }
        WabVoice.pushClientState(this);
    }

    private void onStats(JsonObject frame) {
        UUID maidId = uuid(WabJson.str(frame, "maid"));
        if (maidId == null) return;
        WabHud.Stats value = new WabHud.Stats();
        value.character = WabJson.str(frame, "character");
        value.affection = WabJson.integer(frame, "affection", 0);
        value.affectionPoints = WabJson.integer(frame, "affectionPoints", 0);
        value.trust = WabJson.integer(frame, "trust", 0);
        value.energy = WabJson.integer(frame, "energy", 0);
        value.mood = WabJson.integer(frame, "mood", 0);
        value.receivedAt = System.currentTimeMillis();
        stats.put(maidId, value);
        String boundBefore = boundCharacters.get(maidId);
        rememberCharacter(maidId, value.character);
        String boundAfter = boundCharacters.get(maidId);
        // 角色是在对话里才认上的（auto-bind）时，客户端也要立刻知道这具身体改走 WAB 的语音。
        if (boundAfter != null && !boundAfter.equals(boundBefore)) WabVoice.pushClientState(this);
    }

    private void onChatReply(JsonObject frame) {
        String maidId = WabJson.str(frame, "maid");
        String text = WabJson.str(frame, "text");
        EntityMaid maid = resolveMaid(maidId);
        UUID maidUuid = maid == null ? uuid(maidId) : maid.getUUID();
        Long waiting = maidUuid == null ? null : pendingReplies.remove(maidUuid);
        long bubble = waiting == null ? -1 : waiting;
        if (!WabJson.bool(frame, "ok", true)) {
            if (maid != null && bubble >= 0) maid.getChatBubbleManager().removeChatBubble(bubble);
            String error = WabJson.str(frame, "error", "她没能回应。");
            ServerPlayer player = currentOwner();
            if (player != null) player.sendSystemMessage(Component.literal(error).withStyle(ChatFormatting.GRAY));
            return;
        }
        if (text.isEmpty()) return;
        if (maid != null && maid.level() instanceof ServerLevel) {
            maid.getChatBubbleManager().addLLMChatText(text, bubble);
            // 绑了 WAB 角色的女仆，这句话也用角色自己的嗓子念出来；音色由宿主按角色决定。
            WabVoice.speak(this, maid, text);
        } else {
            ServerPlayer player = currentOwner();
            if (player != null) {
                player.sendSystemMessage(Component.literal("<" + (maid == null ? "女仆" : cleanName(maid)) + "> ")
                        .append(Component.literal(text)).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private void tick() {
        tickCounter++;
        refreshOwner();
        rebuildMaids();

        WabSession current = session;
        if (current != null) {
            if (!current.isClosed()) current.tick();
            else session = null;
        }

        dispatcher.tick();
        tickFollow();
        autonomy.tick();
        tickPhotos();
        WabVoice.tick(this);
        if (tickCounter % 200 == 0) tickPairChat();
        if (tickCounter % 10 == 0) tickOwnerSleep();

        if (tickCounter % 40 == 0) {
            updateBeaconMaids();
            if (connected()) WabHud.tick(this);
        }
        if (tickCounter % 100 == 0 && connected()) sendState(false);
    }

    private void tickFollow() {
        if (followTargets.isEmpty() || tickCounter % 20 != 0) return;
        followTargets.entrySet().removeIf(entry -> {
            EntityMaid maid = maids.get(entry.getKey());
            ServerPlayer target = server.getPlayerList().getPlayer(entry.getValue());
            if (maid == null || !maid.isAlive() || target == null || maid.level() != target.level()) return true;
            if (dispatcher.hasJob(maid)) return false;
            double distance = maid.distanceToSqr(target);
            if (distance > 9) maid.getNavigation().moveTo(target, 0.9);
            else if (distance < 4) maid.getNavigation().stop();
            return false;
        });
    }

    public void setFollow(EntityMaid maid, ServerPlayer target) {
        followTargets.put(maid.getUUID(), target.getUUID());
    }

    public void clearFollow(EntityMaid maid) {
        followTargets.remove(maid.getUUID());
    }

    private void refreshOwner() {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        ServerPlayer picked = players.isEmpty() ? null : pickOwner(players);
        if (picked != null) {
            ownerName = picked.getGameProfile().getName();
            worldName = server.getWorldData().getLevelName();
            if (owner == null || !owner.getUUID().equals(picked.getUUID())) {
                boolean hadOwner = owner != null;
                owner = picked;
                WabSession current = session;
                if (hadOwner && current != null) current.close("owner changed");
                updateBeaconMaids();
            }
        } else {
            owner = null;
        }
    }

    /**
     * 主人认「谁名下的女仆最多」，一样多就继续认现在这个，别一会儿认张三一会儿认李四。
     * 原来取的是「第一个进服的玩家」：多人服上那个人多半不是开着 WAB 的，认错了人，
     * 女仆名册（按主人筛）、聊天路由、喂食、陪着睡觉就全落到一个陌生人身上。
     * 一轮女仆都没数到才退回老取法——那时桥里本来没有女仆可错位，单人也照旧能配对。
     */
    private ServerPlayer pickOwner(List<ServerPlayer> players) {
        int most = 0;
        for (ServerPlayer player : players) {
            most = Math.max(most, maidsByOwner.getOrDefault(player.getUUID(), 0));
        }
        if (most <= 0) return players.get(0);
        ServerPlayer current = owner;
        if (current != null && maidsByOwner.getOrDefault(current.getUUID(), 0) == most
                && server.getPlayerList().getPlayer(current.getUUID()) != null) {
            return current;
        }
        for (ServerPlayer player : players) {
            if (maidsByOwner.getOrDefault(player.getUUID(), 0) == most) return player;
        }
        return players.get(0);
    }

    public ServerPlayer currentOwner() {
        if (owner != null && !owner.isRemoved()) return owner;
        return ownerName.isEmpty() ? null : server.getPlayerList().getPlayerByName(ownerName);
    }

    private void rebuildMaids() {
        if (tickCounter % 20 != 0) return;
        Map<UUID, EntityMaid> fresh = new HashMap<>();
        UUID ownerId = owner == null ? null : owner.getUUID();
        Map<UUID, Integer> counts = new HashMap<>();
        for (ServerLevel level : server.getAllLevels()) {
            // 这一趟不带主人过滤地扫：份数既是下一轮认主人用的，也顺手筛出这次的名册。
            for (EntityMaid maid : level.getEntities(EntityMaid.TYPE, e -> e.isAlive() && e.isTame())) {
                // 协议封顶：一次会话最多认 256 个女仆，超出部分不参与桥。
                if (fresh.size() >= MAX_MAIDS) break;
                UUID keeper = maid.getOwnerUUID();
                if (keeper != null) counts.merge(keeper, 1, Integer::sum);
                if (ownerId == null || ownerId.equals(keeper)) fresh.put(maid.getUUID(), maid);
            }
            if (fresh.size() >= MAX_MAIDS) break;
        }
        maidsByOwner.clear();
        maidsByOwner.putAll(counts);
        // 先比对再换名册：清完再问「上一版里有她吗」永远是否，复活会被认成 every chunk reload。
        noteReappear(new HashMap<>(maids), fresh);
        maids.clear();
        maids.putAll(fresh);
    }

    private void updateBeaconMaids() {
        JsonArray array = new JsonArray();
        for (EntityMaid maid : maids.values()) {
            JsonObject item = new JsonObject();
            item.addProperty("id", maid.getUUID().toString());
            item.addProperty("name", cleanName(maid));
            item.addProperty("model", maid.getModelId());
            item.addProperty("health", Math.round(maid.getHealth() * 10) / 10.0);
            item.addProperty("bound", boundCharacters.containsKey(maid.getUUID()));
            array.add(item);
        }
        beaconMaids = array;
    }

    public JsonArray beaconMaids() {
        return beaconMaids;
    }

    /**
     * 发现阶段的 beacon：只带配对必需的字段。世界名、玩家名和女仆名册放到 hello 里——
     * 那时双方已经用 token/proof 对上，不至于每 3 秒朝整个局域网广播这些隐私。
     */
    public JsonObject beacon(String nonce) {
        JsonObject out = new JsonObject();
        out.addProperty("magic", MAGIC);
        out.addProperty("v", Integer.parseInt(PROTOCOL));
        out.addProperty("type", "beacon");
        out.addProperty("nonce", nonce);
        out.addProperty("instance", instanceId);
        out.addProperty("mod", TouhouLittleMaid.MOD_ID);
        out.addProperty("modVersion", modVersion(TouhouLittleMaid.MOD_ID));
        out.addProperty("mc", SharedConstants.getCurrentVersion().getName());
        out.addProperty("loader", loaderName());
        out.addProperty("loaderVersion", loaderVersion());
        out.addProperty("bridge", BRIDGE_BUILD);
        return out;
    }

    public String hello(String nonce, String token) {
        JsonObject out = beacon(nonce);
        out.addProperty("type", "hello");
        out.addProperty("token", token);
        out.addProperty("world", worldName);
        out.addProperty("player", ownerName);
        out.add("maids", beaconMaids);
        // 握手就把这台游戏的配置报上去：宿主拿它跟插件设置对账，两边打架时当场说破。
        // 不带这一份的话，宿主那个「模组侧配置」镜像永远是空的。
        out.add("config", WabConfig.snapshot());
        return out.toString();
    }

    private static String modVersion(String modId) {
        try {
            return ModList.get().getModContainerById(modId)
                    .map(container -> container.getModInfo().getVersion().toString())
                    .orElse("");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 加载器靠类探测报，不硬写 "forge"：这条分支将来要往 NeoForge 上搬（上游 1.21.1 就是 NeoForge），
     * 到时候报错了环境比不报更糟。先认 net.neoforged 的包名——NeoForge 早期版本连
     * net.minecraftforge.* 的类一起带着，只查那边会把它误报成 Forge。
     */
    private static String loaderName() {
        if (hasClass("net.neoforged.fml.ModList") || hasClass("net.neoforged.neoforge.common.NeoForge")) {
            return "neoforge";
        }
        if (hasClass("net.minecraftforge.fml.ModList") || hasClass("net.minecraftforge.common.ForgeMod")) {
            return "forge";
        }
        return "unknown";
    }

    private static String loaderVersion() {
        for (String id : new String[]{"neoforge", "forge"}) {
            String version = modVersion(id);
            if (!version.isEmpty()) return version;
        }
        return "";
    }

    private static boolean hasClass(String name) {
        try {
            Class.forName(name, false, WabBridge.class.getClassLoader());
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static String makeInstanceId(MinecraftServer server) {
        try {
            String raw = server.getWorldData().getLevelName() + "|" + server.overworld().getSeed();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) sb.append(String.format("%02x", hash[i]));
            return sb.toString();
        } catch (Exception e) {
            return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        }
    }

    public void sendState(boolean force) {
        WabSession current = session;
        if (current == null || current.isClosed()) return;
        JsonObject msg = new JsonObject();
        msg.addProperty("v", Integer.parseInt(PROTOCOL));
        msg.addProperty("type", "state");
        msg.add("world", WabSensors.world(this));
        JsonArray list = new JsonArray();
        for (EntityMaid maid : maids.values()) {
            list.add(WabSensors.snapshot(this, maid));
        }
        msg.add("maids", list);
        // 配置也跟着状态一起报：用户在 WAB 里改完插件设置、或在游戏里改了 WAB 配置，
        // 宿主要立刻看到「此刻生效的是什么」，不然两侧对照表会停在握手那一瞬的旧值。
        msg.add("config", WabConfig.snapshot());
        String hash = msg.toString();
        long now = System.currentTimeMillis();
        if (!force && hash.equals(lastStateHash) && now - lastStateAt < 30000) return;
        lastStateHash = hash;
        lastStateAt = now;
        current.send(msg);
    }

    public void sendEvent(String kind, EntityMaid maid, String text, double importance,
                          int affectionDelta, String reason) {
        sendEvent(kind, maid, text, importance, affectionDelta, reason, null);
    }

    /** extra 是这一类事件独有的字段（主人睡在哪、谁跟她搭的话），WAB 那边按 kind 取用。 */
    public void sendEvent(String kind, EntityMaid maid, String text, double importance,
                          int affectionDelta, String reason, JsonObject extra) {
        WabSession current = session;
        if (current == null || current.isClosed()) return;
        JsonObject ev = new JsonObject();
        ev.addProperty("v", Integer.parseInt(PROTOCOL));
        ev.addProperty("type", "event");
        ev.addProperty("kind", kind);
        ev.addProperty("maid", maid.getUUID().toString());
        ev.addProperty("text", text);
        ev.addProperty("importance", importance);
        if (affectionDelta != 0) {
            ev.addProperty("affection_delta", affectionDelta);
            ev.addProperty("reason", reason == null ? text : reason);
        }
        if (extra != null) {
            for (String key : extra.keySet()) ev.add(key, extra.get(key));
        }
        ev.add("snapshot", WabSensors.snapshot(this, maid));
        current.send(ev);
    }

    public void sendChat(EntityMaid maid, String text) {
        WabSession current = session;
        if (current == null || current.isClosed()) return;
        JsonObject chat = new JsonObject();
        chat.addProperty("v", Integer.parseInt(PROTOCOL));
        chat.addProperty("type", "chat");
        chat.addProperty("maid", maid.getUUID().toString());
        chat.addProperty("name", cleanName(maid));
        chat.addProperty("text", text);
        current.send(chat);
    }

    /**
     * 游戏里改了 WAB 插件的设置：把键值发回宿主落盘。
     * 这里只发不判——哪些能改、值合不合规矩由宿主那边说了算，
     * 模组不认识 plugin.json 的字段，也不该存一份会过期的副本。
     */
    public void sendConfigSet(String key, String value) {
        WabSession current = session;
        if (current == null || current.isClosed()) return;
        JsonObject msg = new JsonObject();
        msg.addProperty("v", Integer.parseInt(PROTOCOL));
        msg.addProperty("type", "config_set");
        msg.addProperty("key", key == null ? "" : key);
        msg.addProperty("value", value == null ? "" : value);
        current.send(msg);
    }

    /**
     * 绑了 WAB 角色的女仆：这句话由 WAB 那边的人来答，游戏里不再调自己的 LLM。
     * 返回 false 表示这具身体不归 WAB 管（或桥没连），调用方照旧走游戏自己的站点。
     */
    public boolean sendChatIfBound(EntityMaid maid, String text) {
        WabSession current = session;
        if (current == null || current.isClosed()) return false;
        if (!isOwned(maid) || !isBound(maid)) return false;
        if (text == null || text.isBlank()) return false;
        // 与上游同一条「正在思考」气泡：WAB 回话后换成正文，失败就撤掉。
        long bubble = maid.getChatBubbleManager().addThinkingText("ai.touhou_little_maid.chat.chat_bubble_waiting");
        pendingReplies.put(maid.getUUID(), bubble);
        sendChat(maid, text);
        return true;
    }

    private void clearPendingReplies() {
        for (Map.Entry<UUID, Long> entry : new HashMap<>(pendingReplies).entrySet()) {
            EntityMaid maid = maids.get(entry.getKey());
            if (maid != null && maid.isAlive()) maid.getChatBubbleManager().removeChatBubble(entry.getValue());
        }
        pendingReplies.clear();
    }

    /**
     * 请主人的客户端拍一张当前画面。取到之后走 {@link #onPhoto} 单独回一帧，
     * 所以这里立刻返回一个请求号，让调用方先拿到「她看到了什么」的文字。
     * 画面只能从正在渲染这个世界的那个客户端取，专用服务器上没有帧缓冲，直接说清楚。
     */
    public int requestPhoto(EntityMaid maid, int width) {
        WabSession current = session;
        ServerPlayer player = currentOwner();
        if (current == null || current.isClosed() || player == null) return -1;
        // 一次只挂一张：截屏要走渲染线程，连着要几张会把客户端的帧排成一串。
        if (!pendingPhotos.isEmpty()) return -1;
        int request = photoSequence++;
        pendingPhotos.put(request, new PendingPhoto(maid.getUUID(), System.currentTimeMillis()));
        try {
            NetworkHandler.sendToClientPlayer(new WabPhotoRequest(request, width), player);
        } catch (Exception e) {
            pendingPhotos.remove(request);
            TouhouLittleMaid.LOGGER.warn("[WAB] photo request failed: {}", e.toString());
            return -1;
        }
        return request;
    }

    /** 客户端把画面送回服务器（也可能带着一句「拍不了」）。 */
    public void onPhoto(int request, byte[] png, int width, int height, String error) {
        PendingPhoto pending = pendingPhotos.remove(request);
        if (pending == null) return;
        WabSession current = session;
        if (current == null || current.isClosed()) return;
        JsonObject frame = new JsonObject();
        frame.addProperty("v", Integer.parseInt(PROTOCOL));
        frame.addProperty("type", "photo");
        frame.addProperty("request", request);
        frame.addProperty("maid", pending.maid().toString());
        boolean bad = error != null && !error.isEmpty();
        if (!bad && (png == null || png.length == 0)) {
            error = "客户端说拍到了，可一个字节也没交回来。";
            bad = true;
        }
        if (!bad && png.length > MAX_PHOTO_BYTES) {
            error = "这张画面压完还有 " + (png.length / 1024) + " KB，太大，没往桥上发。";
            bad = true;
        }
        if (bad) {
            frame.addProperty("ok", false);
            frame.addProperty("error", error == null ? "客户端没能拍到画面。" : error);
        } else {
            frame.addProperty("ok", true);
            frame.addProperty("mime", "image/png");
            frame.addProperty("width", width);
            frame.addProperty("height", height);
            frame.addProperty("bytes", png == null ? 0 : png.length);
            frame.addProperty("png", Base64.getEncoder().encodeToString(png == null ? new byte[0] : png));
        }
        current.send(frame);
    }

    /** 请求发出去了但客户端一直没回（离线、卡住、被别的模组拦住）：也要给 WAB 一个了结，别让它干等。 */
    private void tickPhotos() {
        if (pendingPhotos.isEmpty()) return;
        long now = System.currentTimeMillis();
        for (Map.Entry<Integer, PendingPhoto> entry : new HashMap<>(pendingPhotos).entrySet()) {
            if (now - entry.getValue().sentAt() < PHOTO_TIMEOUT_MS) continue;
            pendingPhotos.remove(entry.getKey());
            onPhoto(entry.getKey(), null, 0, 0, "等客户端回画面超时了，可能这位主人根本没在看这个游戏。");
        }
    }

    /**
     * 女仆互相搭话：两个自己人凑得很近、又隔着一点距离时，报一次「她们在一起」。
     * WAB 那边把这件事当成一个思考场景，才有机会让她们真的聊起来。
     * 每一对都有自己的冷却，不然是每 10 秒朝 AI 打一针。
     */
    private void tickPairChat() {
        if (!connected() || maids.size() < 2) return;
        List<EntityMaid> alive = new ArrayList<>(maids.values());
        alive.sort(Comparator.comparing(EntityMaid::getUUID));
        long now = System.currentTimeMillis();
        for (int i = 0; i < alive.size(); i++) {
            for (int j = i + 1; j < alive.size(); j++) {
                EntityMaid a = alive.get(i);
                EntityMaid b = alive.get(j);
                if (a.level() != b.level() || !a.isAlive() || !b.isAlive()) continue;
                double distance = Math.sqrt(a.distanceToSqr(b));
                if (distance > 10) continue;
                String key = pairKey(a, b);
                Long last = pairChats.get(key);
                if (last != null && now - last < WabConfig.maidChatCooldownSeconds() * 1000L) continue;
                pairChats.put(key, now);
                // 新的一轮从这一句起算，之后 noteMaidSpeech 最多再接 MAX_PAIR_HOPS 句
                pairHops.put(key, 1);
                String how = distance <= 3 ? "凑到" + cleanName(b) + "身边，两个人挨着站了一会儿。"
                        : "和 " + cleanName(b) + " 隔了 " + Math.round(distance) + " 格对上了眼。";
                sendEvent("maid_chat", a, cleanName(a) + how, 0.3, 0, "同伴之间的事");
                return;
            }
        }
    }

    /**
     * 她对着同伴说了句话：把这句话原样报给对方，才有「你来我往」可言——
     * 不然 tickPairChat 只会让一个人单方面感慨「我们挨着站了一会儿」。
     * 每一对最多连着聊 MAX_PAIR_HOPS 轮，冷场过冷却后重新计；不然两个 AI 可以从天黑聊到天亮。
     */
    public void noteMaidSpeech(EntityMaid speaker, String text) {
        if (!connected() || text == null || text.isBlank()) return;
        EntityMaid listener = nearestCompanion(speaker, 8.0);
        if (listener == null) return;
        String key = pairKey(speaker, listener);
        long now = System.currentTimeMillis();
        long gap = WabConfig.maidChatCooldownSeconds() * 1000L;
        Long last = pairChats.get(key);
        int hops = pairHops.getOrDefault(key, 0);
        if (last == null || now - last > gap) hops = 0;
        if (hops >= MAX_PAIR_HOPS) return;
        pairChats.put(key, now);
        pairHops.put(key, hops + 1);
        if (pairHops.size() > 64) pairHops.entrySet().removeIf(e -> {
            Long at = pairChats.get(e.getKey());
            return at == null || now - at > 4 * gap;
        });
        sendEvent("maid_chat", listener,
                cleanName(speaker) + " 凑过来对" + cleanName(listener) + "说：「" + trimForEvent(text) + "」",
                0.3, 0, "同伴说的话");
    }

    private static String pairKey(EntityMaid a, EntityMaid b) {
        return a.getUUID().compareTo(b.getUUID()) <= 0
                ? a.getUUID() + "|" + b.getUUID() : b.getUUID() + "|" + a.getUUID();
    }

    private static String trimForEvent(String text) {
        String flat = text.replaceAll("\\s+", " ").trim();
        return flat.length() <= 120 ? flat : flat.substring(0, 120) + "…";
    }

    private EntityMaid nearestCompanion(EntityMaid speaker, double range) {
        EntityMaid best = null;
        double bestDistance = range * range;
        for (EntityMaid maid : maids.values()) {
            if (maid == speaker || !maid.isAlive() || !isOwned(maid)) continue;
            if (maid.level() != speaker.level()) continue;
            double distance = speaker.distanceToSqr(maid);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = maid;
            }
        }
        return best;
    }

    /**
     * 她倒了：记下 UUID 和当时的名字。
     * 复活在原版里根本没有事件，只能靠「她重新出现在世界上」认，见 onMaidTransform 与 noteReappear。
     */
    private void noteDeath(EntityMaid maid) {
        long now = System.currentTimeMillis();
        mourned.put(maid.getUUID(), new Mourned(cleanName(maid), now, maid.getUUID()));
        sweepMourned(now);
    }

    /**
     * 名册里出现新面孔，按准到不准的三条路依次认她是不是回来了：
     * 一是 onMaidTransform 钉下的那个新 UUID（胶片复活，一步不靠猜）；
     * 二是死过的那个 UUID 原样回来（别的模组把她搬回来了）；
     * 三才退到认名字——这条路会把「新收的同名女仆」也报成复活，只当兜底。
     */
    private void noteReappear(Map<UUID, EntityMaid> previous, Map<UUID, EntityMaid> fresh) {
        if (mourned.isEmpty() && reviving.isEmpty()) return;
        long now = System.currentTimeMillis();
        for (Map.Entry<UUID, EntityMaid> entry : fresh.entrySet()) {
            if (previous.containsKey(entry.getKey())) continue;
            Mourned back = reviving.remove(entry.getKey());
            if (back == null) {
                back = mourned.remove(entry.getKey());
            }
            if (back == null) {
                String name = cleanName(entry.getValue());
                for (Map.Entry<UUID, Mourned> dead : mourned.entrySet()) {
                    if (now - dead.getValue().at() > MOURN_WINDOW_MS) continue;
                    if (!dead.getValue().name().equals(name)) continue;
                    back = dead.getValue();
                    mourned.remove(dead.getKey());
                    break;
                }
            }
            if (back == null) continue;
            // 认到了就把这条死亡记录整个销掉：不然 20 分钟内新收一只同名女仆，还会被名字兜底再报一次复活
            mourned.remove(back.deadId());
            sendEvent("respawn", entry.getValue(), back.name() + " 又站起来了。", 0.75, 0, "她复活了");
        }
        sweepMourned(now);
    }

    private void sweepMourned(long now) {
        long cutoff = now - MOURN_WINDOW_MS;
        mourned.entrySet().removeIf(entry -> entry.getValue().at() < cutoff);
        reviving.entrySet().removeIf(entry -> entry.getValue().at() < cutoff);
    }

    private boolean eventCoolDown(EntityMaid maid, long millis) {
        long now = System.currentTimeMillis();
        Long last = lastEventAt.get(maid.getUUID());
        if (last != null && now - last < millis) return false;
        if (lastEventAt.size() >= MAX_EVENTS) {
            // 事件冷却表按协议封顶 128 项；满了先清掉 60 秒前的旧记录，再不行就整表重置。
            lastEventAt.entrySet().removeIf(entry -> now - entry.getValue() > 60_000L);
            if (lastEventAt.size() >= MAX_EVENTS) lastEventAt.clear();
        }
        lastEventAt.put(maid.getUUID(), now);
        return true;
    }

    private EntityMaid nearestMaid(Player player, double range) {
        EntityMaid best = null;
        double bestDistance = range * range;
        for (EntityMaid maid : maids.values()) {
            if (!maid.isAlive() || maid.level() != player.level() || !isOwned(maid)) continue;
            double distance = maid.distanceToSqr(player);
            if (distance <= bestDistance) {
                bestDistance = distance;
                best = maid;
            }
        }
        return best;
    }

    private static UUID uuid(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (Exception e) {
            return null;
        }
    }

    public static void shutdown() {
        WabBridge old = instance;
        instance = null;
        if (old == null) return;
        old.autonomy.flush();
        old.discovery.stop();
        WabSession current = old.session;
        if (current != null) current.close("server stopping");
        old.dispatcher.cancelAll("server stopping");
        // 换存档/关服都会走到这里：不清的话，下一个世界的设置页会先画出上一个存档那套插件开关
        WabRemoteConfig.clear();
        TouhouLittleMaid.LOGGER.info("[WAB] bridge stopped");
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        shutdown();
        if (WabConfig.enabled()) {
            instance = new WabBridge(event.getServer());
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        shutdown();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && instance != null) instance.tick();
    }

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        WabBridge bridge = instance;
        if (bridge == null || !bridge.connected()) return;
        // 只转主人的话：女仆名册是按主人筛的，别人的话既对不上「她的」女仆，
        // 取消事件又把他的聊天凭空吞掉。
        if (bridge.owner == null || !event.getPlayer().getUUID().equals(bridge.owner.getUUID())) return;
        String text = event.getRawText().trim();
        if (text.isEmpty() || text.startsWith("/")) return;
        EntityMaid maid = bridge.nearestMaid(event.getPlayer(), WabConfig.chatRange());
        if (maid == null) return;
        // 不取消事件：这句话照常进聊天框，记录、翻译、反作弊那些监听聊天的模组仍在链上。
        // 原来取消完只在 action bar 把原文重播一遍，等于把聊天挪了个地方，还顺手丢了别人的监听。
        bridge.sendChat(maid, text);
        event.getPlayer().displayClientMessage(
                Component.literal("这句交给 " + cleanName(maid) + " 回答。").withStyle(ChatFormatting.GRAY), true);
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getSide() != LogicalSide.SERVER) return;
        noteInteraction(event.getTarget(), event.getEntity(), event.getItemStack());
    }

    /**
     * 带坐标的那次右键：附属模组常挂在这条上（摸头、抱抱、递东西这类自己加的互动）。
     * 只认 EntityInteract 的话，那些动作根本不会报给 WAB，「玩家对她做的任何事她都会想一次」就漏了。
     * 两条都对同一次点击触发时由 cooldown 收到一起，不会让她想两遍。
     */
    @SubscribeEvent
    public static void onInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getSide() != LogicalSide.SERVER) return;
        noteInteraction(event.getTarget(), event.getEntity(), event.getItemStack());
    }

    private static void noteInteraction(Entity target, Player player, ItemStack stack) {
        if (!(target instanceof EntityMaid maid)) return;
        WabBridge bridge = instance;
        if (bridge == null || !bridge.isOwned(maid)) return;
        if (player == null) return;
        if (!bridge.eventCoolDown(maid, 20000)) return;
        String what = stack.isEmpty() ? "空着手" : "拿着 " + stack.getHoverName().getString();
        bridge.sendEvent("interaction", maid,
                player.getName().getString() + " 走近她，" + what + " 和她互动了一下。",
                0.35, stack.isEdible() ? 1 : 0, "被主人温柔地回应");
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof EntityMaid maid)) return;
        WabBridge bridge = instance;
        if (bridge == null || !bridge.isOwned(maid)) return;
        if (!bridge.eventCoolDown(maid, 5000)) return;
        Entity source = event.getSource().getEntity();
        String by = source == null ? "不知从哪来的伤害" : "被 " + source.getName().getString() + " 打伤";
        boolean byOwner = source instanceof Player player && bridge.owner != null
                && player.getUUID().equals(bridge.owner.getUUID());
        bridge.sendEvent("hurt", maid,
                by + "，掉了 " + Math.round(event.getAmount() * 10) / 10.0 + " 点生命。",
                0.7, byOwner ? -1 : 0, byOwner ? "被主人弄疼了" : "在游戏里受伤");
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityMaid maid)) return;
        WabBridge bridge = instance;
        if (bridge == null || !bridge.isOwned(maid)) return;
        bridge.noteDeath(maid);
        bridge.sendEvent("death", maid,
                cleanName(maid) + " 在 Minecraft 里倒下了。",
                0.95, -2, "她在游戏里死亡");
    }

    /**
     * 女仆从物品里被放出来（胶片复活、相机、魂符）：这是唯一准确识别「她回来了」的时机。
     * 胶片那条路是 new 一个全新实体再把数据读回去的，UUID 整个换掉，只排名册差异就只能认名字，
     * 而事件带的 data 正是死那个的 saveWithoutId 结果，里面还留着她的 "UUID"——
     * 拿它对上死亡记录，「新收一只同名女仆」就再也不会被误报成复活。
     * 此刻实体还没进世界、名字也还没读上，所以只记下「这个新 UUID 是谁回来了」，等名册认到再报。
     */
    @SubscribeEvent
    public static void onMaidTransform(MaidAndItemTransformEvent.ToMaid event) {
        WabBridge bridge = instance;
        if (bridge == null || !bridge.connected()) return;
        EntityMaid fresh = event.getMaid();
        if (fresh.level().isClientSide) return;
        CompoundTag data = event.getData();
        if (data == null || !data.hasUUID("UUID")) return;
        Mourned dead = bridge.mourned.get(data.getUUID("UUID"));
        if (dead == null) return;
        // 死亡记录先留着：这条路认的是「换 UUID」，但有的模组放人出来时走 load()，UUID 原样回来，
        // 那一条要靠 mourned 本身认。谁先认到谁把记录销掉（见 noteReappear）。
        bridge.reviving.put(fresh.getUUID(), new Mourned(dead.name(), System.currentTimeMillis(), dead.deadId()));
    }

    /**
     * 主人躺下这件事得让她知道——陪睡、爱语都从这个场景起，床的位置也要一起报过去，
     * 不然她只听说「他睡了」，却不知道要往哪儿走。
     * 这里不挂 PlayerSleepInBedEvent：那个事件是「睡不成」时用来改判的，睡成了未必会走到它跟前。
     * 盯 isSleeping 的上升沿才是真的睡下了，而且任何模组加的床都算。
     */
    private void tickOwnerSleep() {
        ServerPlayer player = currentOwner();
        if (player == null) {
            ownerWasSleeping = false;
            return;
        }
        boolean now = player.isSleeping();
        if (!now || ownerWasSleeping) {
            ownerWasSleeping = now;
            return;
        }
        ownerWasSleeping = true;
        EntityMaid maid = nearestMaid(player, 64);
        if (maid == null || !eventCoolDown(maid, 30000)) return;
        JsonObject extra = new JsonObject();
        player.getSleepingPos().ifPresent(pos -> {
            JsonArray bed = new JsonArray();
            bed.add(pos.getX());
            bed.add(pos.getY());
            bed.add(pos.getZ());
            extra.add("bed", bed);
            extra.addProperty("bedBlock", WabSensors.blockId((ServerLevel) player.level(), pos));
        });
        sendEvent("owner_sleep", maid,
                player.getName().getString() + " 上床睡了，" + cleanName(maid) + " 就在旁边。",
                0.6, 0, "主人睡了", extra);
    }
}
