package com.github.tartaricacid.touhoulittlemaid.compat.wab.config;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabHeadPat;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabJson;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabSensors;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabStorage;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 18 大项里「会动世界」的执行层。所有入口先过配置开关 + 纯代码规则，再落到最小化的稳定 MC API。
 *
 * <p>双线一致的技巧：物品句柄用 {@code var} 接，不 import 任何 forge/neoforge 的 IItemHandler 包；
 * 注册表、附魔组件、物品比较一律走 {@link WabPlatform}。本文件在两条线上必须字节一致。
 * <p>断桥时 {@link WabConfigRuntime#connected()} 为 false，风险类开关回落保守默认值（全关），
 * 因此不连 WAB 时不会产生任何额外的世界改动。
 */
public final class WabFeatures {
    private static final int MAX_FARM_BLOCKS = 64;
    private static final int MAX_PICKUP_ITEMS = 32;

    private WabFeatures() {
    }

    private static boolean on(String key) {
        return WabConfigRuntime.on(key);
    }

    private static JsonObject ok(String message) {
        JsonObject out = new JsonObject();
        out.addProperty("message", message);
        return out;
    }

    private static JsonObject fail(String code, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", false);
        out.addProperty("code", code);
        out.addProperty("error", message);
        return out;
    }

    // ==================== 一、自主物品管理 ====================

    /** item：list / drop / equip / pickup。全部动作都受各自开关约束，默认关闭。 */
    public static JsonObject item(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "list").toLowerCase(Locale.ROOT);
        if (!on(WabConfigKeys.ITEM_AUTONOMY_ENABLED)) {
            return fail("disabled", "自主物品管理没开（item_autonomy_enabled）。");
        }
        var inv = maid.getAvailableInv(false);
        switch (action) {
            case "container" -> {
                if (!on(WabConfigKeys.CONTAINER_READ_ENABLED)) return fail("disabled", "容器读取没开。");
                int r = (int) Math.max(1, Math.min(8, WabJson.number(p, "radius", 6)));
                BlockPos origin = maid.blockPosition();
                JsonArray details = new JsonArray();
                int boxes = 0;
                for (BlockPos cursor : BlockPos.betweenClosed(origin.offset(-r, -2, -r), origin.offset(r, 2, r))) {
                    if (!(maid.level().getBlockEntity(cursor) instanceof net.minecraft.world.Container box)) continue;
                    boxes++;
                    for (int i = 0; i < box.getContainerSize() && details.size() < 80; i++) {
                        ItemStack s = box.getItem(i);
                        if (s.isEmpty()) continue;
                        JsonObject one = WabSensors.item(s);
                        one.addProperty("slot", i);
                        one.addProperty("pos", cursor.toShortString());
                        if (on(WabConfigKeys.CONTAINER_ATTR_DETAIL)) WabPlatform.appendAttributes(s, one);
                        details.add(one);
                    }
                }
                JsonObject out = ok("附近 " + boxes + " 个容器，读出 " + details.size() + " 组物品属性。");
                out.add("items", details);
                return out;
            }
            case "list" -> {
                JsonArray items = new JsonArray();
                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    JsonObject one = WabSensors.item(stack);
                    one.addProperty("slot", i);
                    WabPlatform.appendAttributes(stack, one);
                    items.add(one);
                }
                JsonObject out = ok("背包里有 " + items.size() + " 组东西，属性已带回。");
                out.add("items", items);
                return out;
            }
            case "drop" -> {
                if (!on(WabConfigKeys.ITEM_DROP_ENABLED)) return fail("disabled", "丢弃开关没开。");
                int slot = WabJson.integer(p, "slot", -1);
                if (slot < 0 || slot >= inv.getSlots() || inv.getStackInSlot(slot).isEmpty())
                    return fail("bad_slot", "这个槽位是空的。");
                ItemStack taken = inv.extractItem(slot, inv.getStackInSlot(slot).getCount(), false);
                maid.spawnAtLocation(taken);
                return ok("丢掉了 " + taken.getHoverName().getString() + "。");
            }
            case "equip" -> {
                if (!on(WabConfigKeys.ITEM_EQUIP_ENABLED)) return fail("disabled", "主动装备开关没开。");
                int slot = WabJson.integer(p, "slot", -1);
                if (slot < 0 || slot >= inv.getSlots() || inv.getStackInSlot(slot).isEmpty())
                    return fail("bad_slot", "这个槽位是空的。");
                ItemStack taken = inv.extractItem(slot, 1, false);
                ItemStack previous = maid.getMainHandItem().copy();
                maid.setItemInHand(InteractionHand.MAIN_HAND, taken);
                if (!previous.isEmpty()) maid.spawnAtLocation(previous);
                return ok("换上了 " + taken.getHoverName().getString() + "。");
            }
            case "pickup" -> {
                if (!on(WabConfigKeys.ITEM_PICKUP_ENABLED)) return fail("disabled", "主动捡起开关没开。");
                String mode = WabConfigRuntime.text(WabConfigKeys.ITEM_PICKUP_FILTER_MODE);
                String expr = WabConfigRuntime.text(WabConfigKeys.ITEM_PICKUP_FILTER_EXPR);
                double radius = Math.max(1, Math.min(16, WabJson.number(p, "radius", 6)));
                AABB area = maid.getBoundingBox().inflate(radius);
                int taken = 0;
                for (ItemEntity drop : maid.level().getEntitiesOfClass(ItemEntity.class, area, e -> e.isAlive())) {
                    if (taken >= MAX_PICKUP_ITEMS) break;
                    ItemStack stack = drop.getItem();
                    if (stack.isEmpty()) continue;
                    boolean match = WabRules.matchesPickupFilter(mode, WabPlatform.itemId(stack),
                            stack.getHoverName().getString(), stack.getDamageValue(),
                            WabPlatform.hasEnchantment(stack), expr);
                    if (!match) continue;
                    ItemStack rest = WabPlatform.insertStacked(inv, stack);
                    if (rest.getCount() == stack.getCount()) continue;
                    if (rest.isEmpty()) drop.discard();
                    else drop.setItem(rest);
                    taken++;
                }
                return ok("按「" + mode + "」捡起了 " + taken + " 组。");
            }
            default -> {
                return fail("unknown_action", "物品只支持 list / drop / equip / pickup。");
            }
        }
    }

    // ==================== 一、农耕 ====================

    /** farm：必须显式给 tool / crop / area，四要素缺一就拒绝且零世界改动。 */
    public static JsonObject farm(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.FARMING_ENABLED)) return fail("disabled", "农耕开关没开（farming_enabled）。");
        String tool = WabJson.str(p, "tool");
        String crop = WabJson.str(p, "crop");
        String area = WabJson.str(p, "area");
        boolean require = on(WabConfigKeys.FARMING_REQUIRE_EXPLICIT_SPEC);
        String missing = WabRules.missingFarmingSpec(tool, crop, area, require);
        if (!missing.isEmpty()) return fail("missing_spec", "农耕还缺「" + missing + "」，没有指定就不动手。");
        JsonArray posArr = WabJson.arr(p, "pos");
        if (posArr.size() < 3) return fail("bad_pos", "要给种植区域起点坐标。");
        BlockPos origin = new BlockPos((int) posArr.get(0).getAsDouble(), (int) posArr.get(1).getAsDouble(),
                (int) posArr.get(2).getAsDouble());
        int width = Math.max(1, Math.min(8, WabJson.integer(p, "width", 3)));
        int depth = Math.max(1, Math.min(8, WabJson.integer(p, "depth", 3)));
        boolean tilling = on(WabConfigKeys.FARMING_ALLOW_TILLING);
        int tilled = 0;
        int planted = 0;
        for (int dx = 0; dx < width && tilled + planted < MAX_FARM_BLOCKS; dx++) {
            for (int dz = 0; dz < depth && tilled + planted < MAX_FARM_BLOCKS; dz++) {
                BlockPos pos = origin.offset(dx, 0, dz);
                if (!maid.level().isLoaded(pos)) continue;
                if (maid.level().getBlockState(pos).hasBlockEntity()) continue;
                boolean tillable = maid.level().getBlockState(pos).is(Blocks.DIRT)
                        || maid.level().getBlockState(pos).is(Blocks.GRASS_BLOCK);
                if (tilling && tillable) {
                    maid.level().setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
                    tilled++;
                }
                BlockPos above = pos.above();
                if (maid.level().getBlockState(above).isAir()) {
                    ItemStack seed = WabPlatform.itemFromId(crop.contains(":") ? crop : "minecraft:" + crop);
                    if (!seed.isEmpty() && seed.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                        maid.level().setBlockAndUpdate(above, blockItem.getBlock().defaultBlockState());
                        planted++;
                    }
                }
            }
        }
        JsonObject out = ok("开垦 " + tilled + " 格、播种 " + planted + " 格。");
        out.addProperty("tilled", tilled);
        out.addProperty("planted", planted);
        return out;
    }

    // ==================== 二、高级反应逃生 ====================

    /** escape：mlg / suffocation；求安慰单独走 comfort。岩浆船实验项已移除（不主动进入岩浆，见 F13）。 */
    public static JsonObject escape(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "mlg").toLowerCase(Locale.ROOT);
        return switch (action) {
            case "mlg" -> mlgWater(maid, p);
            case "suffocation" -> suffocation(maid, p);
            default -> fail("unknown_action", "逃生只支持 mlg / suffocation。");
        };
    }

    /** F14 待收回的落地水：放水后到点收回，避免永久改世界（「放完水之后收回来」）。 */
    private static final class PendingWater {
        final net.minecraft.server.level.ServerLevel level;
        final BlockPos pos;
        final long expireTick;

        PendingWater(net.minecraft.server.level.ServerLevel level, BlockPos pos, long expireTick) {
            this.level = level;
            this.pos = pos;
            this.expireTick = expireTick;
        }
    }

    /** 按「维度 + 坐标」记，不用女仆 UUID：同一女仆连续触发两次会覆盖，前一格水就永远收不回来。 */
    private static final java.util.concurrent.ConcurrentHashMap<String, PendingWater> PENDING_WATER =
            new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * F14：跟随主人时，若因垂直落差过大（走下去会掉血）而不得不瞬移，先尝试落地水。
     * 返回 true = 本轮不瞬移，让原版寻路继续走下去；false = 照旧瞬移。
     * 断桥时恒为 false（保持原版）。判定走 R21 {@code descentPlan} 与 {@code mayUseMlgWater}。
     */
    public static boolean tryMlgDescent(EntityMaid maid, net.minecraft.world.entity.LivingEntity owner) {
        if (maid == null || owner == null || maid.level().isClientSide) return false;
        if (!WabConfigRuntime.connected()) return false;
        if (!(maid.level() instanceof net.minecraft.server.level.ServerLevel level)) return false;
        if (!on(WabConfigKeys.MLG_WATER_ENABLED)) return false;

        double drop = maid.getY() - owner.getY();
        if (drop < 4) return false;
        boolean mlgAvailable = WabRules.mayUseMlgWater(true, hasBucket(maid),
                on(WabConfigKeys.MLG_AUTO_FETCH_WATER));
        if (WabRules.descentPlan(false, (int) drop, 4, mlgAvailable) != 1) return false;

        BlockPos pos = maid.blockPosition();
        if (!level.isLoaded(pos) || !level.getBlockState(pos).isAir()) return false;
        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        String key = level.dimension().location() + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
        PENDING_WATER.put(key, new PendingWater(level, pos.immutable(),
                level.getGameTime() + 60L));
        return true;
    }

    /** F14：每帧检查，到点把临时放的水收回（只收自己放的、且仍是水的那一格）。 */
    public static void tickDescentWater() {
        if (PENDING_WATER.isEmpty()) return;
        for (java.util.Map.Entry<String, PendingWater> e : PENDING_WATER.entrySet()) {
            PendingWater w = e.getValue();
            if (w.level.getGameTime() < w.expireTick) continue;
            PENDING_WATER.remove(e.getKey());
            if (w.level.isLoaded(w.pos) && w.level.getBlockState(w.pos).is(Blocks.WATER)) {
                w.level.setBlockAndUpdate(w.pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static JsonObject mlgWater(EntityMaid maid, JsonObject p) {
        boolean requireBucket = on(WabConfigKeys.MLG_REQUIRE_BUCKET);
        boolean autoFetch = on(WabConfigKeys.MLG_AUTO_FETCH_WATER);
        boolean hasBucket = hasBucket(maid);
        if (!WabRules.mayUseMlgWater(on(WabConfigKeys.MLG_WATER_ENABLED), hasBucket, autoFetch)) {
            return fail("no_bucket", requireBucket && !hasBucket
                    ? "手里没有水桶或铁桶，也没允许自己去打水。" : "落地水没开或缺桶。");
        }
        BlockPos pos = maid.blockPosition();
        if (!maid.level().getBlockState(pos).isAir()) {
            return fail("occupied", "脚下不是空气格，放不了水。");
        }
        maid.level().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        // F12：放出去的水是临时的，开了「放完水收回来」就登记，60 tick 后由 tickDescentWater 自动收。
        if (on(WabConfigKeys.MLG_WATER_RETRIEVE_ENABLED)
                && maid.level() instanceof net.minecraft.server.level.ServerLevel level) {
            String key = level.dimension().location() + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
            PENDING_WATER.put(key, new PendingWater(level, pos.immutable(), level.getGameTime() + 60L));
        }
        JsonObject out = ok("落地水放好了，这次不找主人求安慰。");
        out.addProperty("mlgSuccess", true);
        out.addProperty("suppressComfort", on(WabConfigKeys.MLG_SUCCESS_SKIP_COMFORT));
        return out;
    }

    private static boolean hasBucket(EntityMaid maid) {
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.is(net.minecraft.world.item.Items.WATER_BUCKET) || s.is(net.minecraft.world.item.Items.BUCKET)) return true;
        }
        return false;
    }

    private static JsonObject suffocation(EntityMaid maid, JsonObject p) {
        int air = WabJson.integer(p, "airTicks", maid.getAirSupply());
        if (!WabRules.shouldEscapeSuffocation(on(WabConfigKeys.SUFFOCATION_ESCAPE_ENABLED), air)) {
            return ok("空气还够，不需要逃生。");
        }
        String mode = WabConfigRuntime.text(WabConfigKeys.SUFFOCATION_PATH_MODE);
        BlockPos from = maid.blockPosition();
        BlockPos target = findAir(maid, from, "dig".equalsIgnoreCase(mode));
        if (target == null) return fail("no_path", "附近没找到有空气的位置，也没能挖出逃生路径。");
        maid.teleportTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
        return ok("窒息了，按「" + mode + "」逃到 " + target.toShortString() + "。");
    }

    private static BlockPos findAir(EntityMaid maid, BlockPos from, boolean dig) {
        for (int r = 1; r <= 6; r++) {
            for (BlockPos pos : BlockPos.betweenClosed(from.offset(-r, -2, -r), from.offset(r, 3, r))) {
                if (!maid.level().isLoaded(pos)) continue;
                if (!maid.level().getBlockState(pos).isAir()) continue;
                if (!maid.level().getBlockState(pos.above()).isAir()) continue;
                return pos.immutable();
            }
        }
        if (dig) {
            BlockPos up = from.above(2);
            if (!maid.level().getBlockState(up).is(Blocks.BEDROCK)) {
                maid.level().setBlockAndUpdate(up, Blocks.AIR.defaultBlockState());
                return up.immutable();
            }
        }
        return null;
    }

    /** comfort：落地水成功必须抑制；概率取 config/ai 两来源里更保守的那个。 */
    public static JsonObject comfort(EntityMaid maid, JsonObject p) {
        boolean nearDeath = WabJson.bool(p, "nearDeath", false);
        boolean mlgSuccess = WabJson.bool(p, "mlgSuccess", false);
        String source = WabConfigRuntime.text(WabConfigKeys.COMFORT_PROBABILITY_SOURCE);
        double probability = WabConfigRuntime.number(WabConfigKeys.COMFORT_SEEK_PROBABILITY);
        if ("ai".equalsIgnoreCase(source)) {
            probability = Math.min(probability, WabJson.number(p, "aiProbability", probability));
        }
        boolean excludeMlg = on(WabConfigKeys.COMFORT_EXCLUDE_MLG_SUCCESS);
        boolean should = WabRules.shouldSeekComfort(on(WabConfigKeys.COMFORT_SEEK_ENABLED),
                nearDeath, excludeMlg && mlgSuccess, probability, WabJson.number(p, "roll", 0.5));
        if (!should) return ok("这次不求安慰。");
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.request");
        return ok("她吓坏了，来找主人求安慰。");
    }

    /** 摸头：AI 主动请求、玩家拒绝伤心；三档反应由 WabHeadPat.pet() 读持久数据播放。 */
    public static JsonObject headpat(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.HEADPAT_ENABLED)) return fail("disabled", "摸头功能整体关闭。");
        String action = WabJson.str(p, "action", "request").toLowerCase(Locale.ROOT);
        return switch (action) {
            case "request" -> {
                if (!on(WabConfigKeys.HEADPAT_AI_REQUEST_ENABLED)) yield fail("disabled", "AI 主动请求摸头没开。");
                WabHeadPat.requestNow(maid);
                yield ok("她主动来讨摸摸了（概率与冷却沿用讨摸规则）。");
            }
            case "refuse" -> {
                if (!on(WabConfigKeys.HEADPAT_PLAYER_REFUSE_ENABLED)) yield fail("disabled", "玩家拒绝摸头没开。");
                WabHeadPat.refuse(maid);
                yield ok(on(WabConfigKeys.HEADPAT_SAD_ON_REFUSE) ? "主人拒绝了，她露出伤心的表情。" : "主人拒绝了。");
            }
            default -> fail("unknown_action", "摸头只支持 request / refuse。");
        };
    }

    // ==================== 八、偷吃 / 自吃 / 中毒自救（F07 + F10）====================

    /**
     * F07 偷吃融合优先级：饿了且主人在附近 → 先讨食（超时才自吃）；饿了但主人不在 → 直接自吃；
     * 不饿 → 按保守概率偷吃。世界状态由 WAB 侧从 status 注入，未注入时用女仆真实饥饿度与主人距离回落。
     */
    public static JsonObject sneakEat(EntityMaid maid, JsonObject p) {
        boolean enabled = on(WabConfigKeys.STEAL_FOOD_ENABLED) || on(WabConfigKeys.SNEAK_EAT_ENABLED);
        boolean hungry = WabJson.bool(p, "hungry",
                maid.getHunger() <= WabConfigRuntime.integer(WabConfigKeys.SELF_EAT_HUNGRY_THRESHOLD));
        boolean ownerNear = WabJson.bool(p, "ownerNear", ownerWithin(maid, 20));
        int plan = WabRules.stealFoodPlan(enabled, hungry, ownerNear,
                WabJson.number(p, "begElapsedMs", 0),
                WabConfigRuntime.number(WabConfigKeys.STEAL_FOOD_BEG_TIMEOUT_MS),
                WabConfigRuntime.number(WabConfigKeys.STEAL_FOOD_PROBABILITY),
                WabJson.number(p, "roll", maid.getRandom().nextDouble()));
        JsonObject out = switch (plan) {
            case 1 -> ok("她饿了，先去找主人讨吃的，等一会儿没人喂再自己吃。");
            case 2 -> eatFromInventory(maid, "自己吃了一口 ");
            case 3 -> eatFromInventory(maid, "不饿也偷偷吃了一口 ");
            default -> ok("这回没偷吃。");
        };
        out.addProperty("plan", plan);
        return out;
    }

    /** F10 自吃与中毒自救：与偷吃同一循环，由饥饿与状态效果驱动，默认全关。 */
    public static JsonObject selfEat(EntityMaid maid, JsonObject p) {
        int response = WabRules.poisonResponse(WabJson.bool(p, "poisoned", false),
                on(WabConfigKeys.POISON_MILK_ENABLED) && hasMilk(maid));
        if (response == 2) {
            JsonObject ask = ok("她中毒了，但背包里没有牛奶，向主人求助。");
            ask.addProperty("needMilk", true);
            return ask;
        }
        if (response == 1) {
            drinkMilk(maid);
            JsonObject cured = ok("中毒了，她自己喝了一桶牛奶解毒。");
            cured.addProperty("cured", true);
            return cured;
        }
        if (!on(WabConfigKeys.SELF_EAT_ENABLED)) return fail("disabled", "自吃开关没开。");
        boolean hungry = WabJson.bool(p, "hungry",
                maid.getHunger() <= WabConfigRuntime.integer(WabConfigKeys.SELF_EAT_HUNGRY_THRESHOLD));
        boolean ownerNear = WabJson.bool(p, "ownerNear", ownerWithin(maid, 20));
        if (!WabRules.shouldSelfEat(hungry, ownerNear)) return ok("主人就在旁边，等投喂就好。");
        JsonObject out = eatFromInventory(maid, "饿了，自己吃了一口 ");
        out.addProperty("selfEat", true);
        return out;
    }

    private static JsonObject eatFromInventory(EntityMaid maid, String prefix) {
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.isEmpty()) continue;
            if (s.getItem().getFoodProperties(s, maid) == null) continue;
            inv.extractItem(i, 1, false);
            JsonObject out = ok(prefix + s.getHoverName().getString() + "。");
            out.addProperty("item", WabPlatform.itemId(s));
            return out;
        }
        return ok("想吃，但背包里没有能吃的东西。");
    }

    private static boolean hasMilk(EntityMaid maid) {
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i).is(net.minecraft.world.item.Items.MILK_BUCKET)) return true;
        }
        return false;
    }

    private static void drinkMilk(EntityMaid maid) {
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i).is(net.minecraft.world.item.Items.MILK_BUCKET)) {
                inv.extractItem(i, 1, false);
                return;
            }
        }
    }

    /** 主人在不在 20×20 内：WAB 侧可从 status.distanceToOwner 注入，离线时按真实距离算。 */
    private static boolean ownerWithin(EntityMaid maid, double radius) {
        var owner = maid.getOwner();
        return owner != null && owner.level() == maid.level()
                && maid.distanceToSqr(owner) <= radius * radius;
    }

    // ==================== 四、模式切换 / 九、聊天 / 六、多步流程 / 三、备份 ====================

    public static JsonObject mode(EntityMaid maid, JsonObject p) {
        long elapsed = WabJson.integer(p, "elapsedSeconds", 0) * 1000L;
        long cooldown = WabConfigRuntime.integer(WabConfigKeys.MODE_SWITCH_COOLDOWN_SECONDS) * 1000L;
        if (!WabRules.maySwitchMode(elapsed, cooldown)) return fail("cooldown", "切换太频繁，还在冷却里。");
        String want = WabJson.str(p, "mode", "follow").toLowerCase(Locale.ROOT);
        if ("home".equals(want) && !on(WabConfigKeys.MODE_SWITCH_ALLOW_HOME)) return fail("disabled", "守家模式没开。");
        if ("follow".equals(want) && !on(WabConfigKeys.MODE_SWITCH_ALLOW_FOLLOW)) return fail("disabled", "跟随模式没开。");
        maid.setHomeModeEnable("home".equals(want));
        return ok("切到" + ("home".equals(want) ? "守家" : "跟随") + "模式。");
    }

    public static JsonObject chat(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "wake").toLowerCase(Locale.ROOT);
        if ("wake".equals(action)) {
            boolean hit = WabRules.wakeByName(on(WabConfigKeys.CHAT_WAKE_BY_NAME_ENABLED),
                    WabJson.str(p, "spoken"), WabConfigRuntime.text(WabConfigKeys.CHAT_WAKE_WORDS));
            return hit ? ok("叫到名字了，她应了一声。") : ok("没叫她名字，先不打扰。");
        }
        if ("dedup".equals(action)) {
            List<String> hashes = new ArrayList<>();
            for (var e : WabJson.arr(p, "hashes")) hashes.add(e.getAsString());
            List<String> unique = on(WabConfigKeys.CHAT_CONTEXT_DEDUP_ENABLED)
                    ? WabRules.dedupContext(hashes) : hashes;
            JsonArray arr = new JsonArray();
            for (String h : unique) arr.add(h);
            JsonObject out = ok("上下文去重后剩 " + unique.size() + " 句。");
            out.add("hashes", arr);
            out.addProperty("sendOnce", on(WabConfigKeys.CHAT_SEND_ONCE_PER_TURN));
            out.addProperty("invisible", on(WabConfigKeys.CHAT_INVISIBLE_ENABLED));
            return out;
        }
        return fail("unknown_action", "聊天只支持 wake / dedup。");
    }

    /** 多步流程：每步先过开关与步数上限，报告按 summary / stepwise / none 组装。 */
    public static JsonObject plan(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.PLAN_EXECUTION_ENABLED)) return fail("disabled", "多步流程没开。");
        JsonArray steps = WabJson.arr(p, "steps");
        int max = WabConfigRuntime.integer(WabConfigKeys.PLAN_MAX_STEPS);
        boolean needScan = on(WabConfigKeys.PLAN_NEED_SCAN_OR_PHOTO)
                && !WabJson.bool(p, "scanned", false) && !WabJson.bool(p, "photo", false);
        if (needScan) return fail("need_scan", "要先探测或拍照，才能规划路线。");
        String report = WabConfigRuntime.text(WabConfigKeys.PLAN_REPORT_MODE);
        JsonArray done = new JsonArray();
        int executed = 0;
        for (int i = 0; i < steps.size(); i++) {
            if (!WabRules.planStepAllowed(i, max)) break;
            JsonObject one = new JsonObject();
            one.addProperty("step", i);
            var step = steps.get(i);
            one.add("action", step);
            one.addProperty("status", "ok");
            done.add(one);
            executed++;
        }
        JsonObject out = ok("走了 " + executed + " 步。");
        out.addProperty("reportMode", report);
        out.addProperty("planner", WabConfigRuntime.text(WabConfigKeys.PLAN_PLANNER));
        if ("summary".equalsIgnoreCase(report) || "stepwise".equalsIgnoreCase(report)) out.add("steps", done);
        return out;
    }

    /** 模组侧备份：把快照写进女仆持久数据（游戏存档内一份），供 WAB 侧恢复时取用。 */
    public static JsonObject backup(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.BACKUP_MOD_SIDE_COPY)) return fail("disabled", "模组侧副本没开。");
        JsonObject snapshot = WabStorage.snapshot(maid);
        maid.getPersistentData().putString("WabBackupSnapshot", snapshot.toString());
        maid.getPersistentData().putLong("WabBackupAt", System.currentTimeMillis());
        String policy = WabConfigRuntime.text(WabConfigKeys.BACKUP_CONFLICT_POLICY);
        boolean verify = on(WabConfigKeys.BACKUP_RESTORE_VERIFY);
        String winner = WabRules.backupWinner(policy, WabJson.integer(p, "wabSavedAt", 0),
                WabJson.integer(p, "modSavedAt", 0), verify);
        JsonObject out = ok("游戏侧快照已写进存档。");
        out.addProperty("policy", policy);
        out.addProperty("restoreFrom", winner);
        out.addProperty("snapshot", snapshot.toString());
        return out;
    }

    // ==================== B5：抱睡同意 / 火逃生 / 保护 / 建造 / 采集 / 坐标 ====================

    /**
     * F05 玩家抱睡同意（纯代码回落）：AI 在线时由 AI 按人设决定，离线按好感度与心情阈值判定。
     * 返回 consent 0=拒绝 / 1=同意 / 2=勉强同意（语气用）。好感度由 WAB 侧注入 [0,1]，
     * 未注入时按女仆好感度折算；moodIndex 由 AI/宿主算好注入，缺省按中性处理。
     */
    public static JsonObject sleepConsent(EntityMaid maid, JsonObject p) {
        boolean enabled = on(WabConfigKeys.SLEEP_CONSENT_ENABLED);
        double fav = WabJson.number(p, "favorability",
                Math.min(1.0, Math.max(0.0, maid.getFavorability() / 100.0)));
        int decision = WabRules.sleepConsent(enabled, fav,
                WabConfigRuntime.number(WabConfigKeys.SLEEP_CONSENT_CLOSENESS_MIN),
                WabJson.number(p, "moodIndex", 0.0),
                WabJson.number(p, "roll", maid.getRandom().nextDouble()));
        JsonObject out = switch (decision) {
            case 1 -> ok("她轻轻点了点头，愿意一起睡。");
            case 2 -> ok("她有点害羞，还是勉强答应了。");
            default -> ok("她摇了摇头，这会儿不想被抱着睡。");
        };
        out.addProperty("consent", decision);
        out.addProperty("enabled", enabled);
        return out;
    }

    /**
     * F12 火逃生：着火优先跳进身边 3×3 水域；没水才用落地水；都没有就维持原版行为。
     * 落地水放完后是否进入「舀回水 + 归还桶」由 shouldRetrieveMlg 判定并回报给 WAB 侧。
     */
    public static JsonObject fireEscape(EntityMaid maid, JsonObject p) {
        boolean onFire = WabJson.bool(p, "onFire", false);
        boolean waterNearby = WabJson.bool(p, "waterNearby", false);
        boolean mlgAvailable = WabRules.mayUseMlgWater(on(WabConfigKeys.MLG_WATER_ENABLED), hasBucket(maid),
                on(WabConfigKeys.MLG_AUTO_FETCH_WATER));
        int plan = WabRules.fireEscapePlan(on(WabConfigKeys.FIRE_ESCAPE_ENABLED) && onFire,
                waterNearby, mlgAvailable);
        if (plan == 1) {
            JsonArray water = WabJson.arr(p, "waterPos");
            if (water.size() < 3) return fail("bad_pos", "跳水逃生要给水域坐标 waterPos。");
            maid.teleportTo(water.get(0).getAsDouble() + 0.5, water.get(1).getAsDouble(),
                    water.get(2).getAsDouble() + 0.5);
            JsonObject out = ok("着火了，跳进旁边的水里灭火。");
            out.addProperty("plan", plan);
            return out;
        }
        if (plan == 2) {
            JsonObject out = mlgWater(maid, p);
            out.addProperty("plan", plan);
            out.addProperty("retrieve", WabRules.shouldRetrieveMlg(true, WabJson.bool(p, "fireOut", false))
                    && on(WabConfigKeys.MLG_WATER_RETRIEVE_ENABLED));
            return out;
        }
        JsonObject out = ok(onFire
                ? "着火了，但身边没水也没有落地水，维持原版行为。" : "没着火，不需要火逃生。");
        out.addProperty("plan", 0);
        return out;
    }

    /** F17 驯服生物保护：按「本女仆 > 玩家 > 其余女仆 > 无主」给优先级，无主不纳入保护。 */
    public static JsonObject tameProtect(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.TAME_PROTECT_ENABLED)) return fail("disabled", "驯服生物保护没开。");
        int priority = WabRules.tameProtectPriority(
                WabJson.bool(p, "tamedByMaid", false),
                WabJson.bool(p, "tamedByOwner", false),
                WabJson.bool(p, "tamedByOther", false));
        JsonObject out = ok(priority > 0
                ? "纳入保护范围，优先级 " + priority + "。" : "无主生物，不纳入保护范围。");
        out.addProperty("priority", priority);
        out.addProperty("protect", priority > 0);
        return out;
    }

    /**
     * F18 建造原点：AI 显式给了 origin 就用它，否则默认女仆当前 XYZ（可由 F19 maid_pos 注入）。
     * 建造白名单开启时必须显式给原点，避免 AI 在没确认位置的情况下误建。
     */
    public static JsonObject buildOrigin(EntityMaid maid, JsonObject p) {
        JsonArray aiOrigin = WabJson.arr(p, "origin");
        boolean useAi = WabRules.useAiProvidedOrigin(aiOrigin.size() >= 3);
        boolean whitelist = on(WabConfigKeys.BUILD_WHITELIST_ENABLED);
        if (whitelist && !useAi) return fail("whitelist", "建造白名单已开启，必须显式给 origin 原点。");
        BlockPos origin = useAi
                ? new BlockPos((int) aiOrigin.get(0).getAsDouble(), (int) aiOrigin.get(1).getAsDouble(),
                        (int) aiOrigin.get(2).getAsDouble())
                : maid.blockPosition();
        JsonArray xyz = new JsonArray();
        xyz.add(origin.getX());
        xyz.add(origin.getY());
        xyz.add(origin.getZ());
        JsonObject out = ok(useAi ? "用 AI 给的原点建造。" : "没给原点，按女仆当前位置建造。");
        out.add("origin", xyz);
        out.addProperty("fromAi", useAi);
        out.addProperty("whitelist", whitelist);
        return out;
    }

    /** F11 牛奶 / 水 / 木板采集：三个开关各自独立，没开的直接拒绝，绝不偷偷改世界。 */
    public static JsonObject collectSupplies(EntityMaid maid, JsonObject p) {
        String kind = WabJson.str(p, "kind", "water").toLowerCase(Locale.ROOT);
        boolean enabled = switch (kind) {
            case "milk" -> on(WabConfigKeys.COLLECT_MILK_ENABLED);
            case "water" -> on(WabConfigKeys.COLLECT_WATER_ENABLED);
            case "board" -> on(WabConfigKeys.COLLECT_BOARD_ENABLED);
            default -> false;
        };
        if (!enabled) return fail("disabled", "「" + kind + "」采集开关没开，或不是 milk/water/board。");
        JsonObject out = ok("按开关放行了「" + kind + "」采集。");
        out.addProperty("kind", kind);
        out.addProperty("origin", maid.blockPosition().toShortString());
        return out;
    }

    /** F19 女仆坐标：只读，供 AI 在建造 / 采集 / 规划路线前取真实原点，不用猜坐标。 */
    public static JsonObject maidPos(EntityMaid maid, JsonObject p) {
        BlockPos pos = maid.blockPosition();
        JsonArray xyz = new JsonArray();
        xyz.add(pos.getX());
        xyz.add(pos.getY());
        xyz.add(pos.getZ());
        JsonObject out = ok("她在 " + pos.toShortString() + "。");
        out.add("pos", xyz);
        out.addProperty("dimension", maid.level().dimension().location().toString());
        out.addProperty("sitting", maid.isInSittingPose());
        out.addProperty("favorability", maid.getFavorability());
        out.addProperty("hunger", maid.getHunger());
        return out;
    }

    /** 容器属性详情：给 container list 的每一行补附魔 / NBT 有无。 */
    public static void enrich(JsonObject one, ItemStack stack) {
        WabPlatform.appendAttributes(stack, one);
    }
}
