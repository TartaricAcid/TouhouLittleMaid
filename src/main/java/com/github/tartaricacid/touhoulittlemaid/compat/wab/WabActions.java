package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 * 桥的「手」：把 WAB 发来的动作请求落成 Minecraft 里女仆真正会做的事。
 * 一次性动作（移动、攻击、投喂、姿态、预设动画）立即执行；
 * 长时间的动作（挖矿、砍树、收割、舀水、捡拾）交给 {@link Job}，
 * 由 <b>派发器</b> 每个 tick 驱动，避免卡死服务器线程。
 */
public final class WabActions {
    /** 远端传来的坐标是外部数据：走过去最远这么远，动手只在这个距离里认。 */
    private static final double NAV_REACH = 128;
    private static final double WORK_REACH = 24;
    private static final double SIGHT_REACH = 64;

    private WabActions() {
    }

    public interface Responder {
        void success(JsonObject data);

        void failure(String code, String message);
    }

    // ==================== 移动 ====================

    public static JsonObject go(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String mode = WabJson.str(p, "mode", "to").toLowerCase(Locale.ROOT);
        String targetName = WabJson.str(p, "target");
        double distance = WabJson.number(p, "distance", 2.0);
        JsonArray posArr = WabJson.arr(p, "pos");

        return switch (mode) {
            case "to", "near" -> {
                BlockPos dest = remotePos(maid, posArr, NAV_REACH);
                // 只给了坐标时不能再去回退到主人：否则「走到 120,64,300」会变成「跟着主人走」，
                // 坐标参数被悄悄吞掉。名字和坐标同时给时以名字为准。
                Entity target = targetName.isEmpty() && dest != null
                        ? null
                        : resolveEntity(maid, targetName, targetName.isEmpty() ? bridge.currentOwner() : null);
                double stopDistance = clampStopDistance(mode.equals("near") ? distance : 1.0);
                if (target == null && dest != null) {
                    if (!navigateTo(maid, dest.getX() + 0.5, dest.getY(), dest.getZ() + 0.5, stopDistance)) {
                        yield fail("cannot_reach", "找不到可以走过去的路。");
                    }
                    yield ok(maid, String.format("已出发送往 %.0f,%.0f,%.0f", dest.getX(), dest.getY(), dest.getZ()));
                }
                if (target != null) {
                    if (mode.equals("near")) {
                        if (!navigateTo(maid, target.getX(), target.getY(), target.getZ(), stopDistance)) {
                            yield fail("cannot_reach", "追不上目标了。");
                        }
                    } else if (target instanceof ServerPlayer sp) {
                        bridge.setFollow(maid, sp);
                    } else {
                        navigateTo(maid, target.getX(), target.getY(), target.getZ(), 2.0);
                    }
                    yield ok(maid, "正在靠近 " + target.getName().getString());
                }
                if (!posArr.isEmpty() && dest == null) {
                    yield fail("bad_pos", "那个坐标不在她能走到的范围里，或者区块还没加载。");
                }
                yield fail("invalid_target", "没有给出要靠近的目标。");
            }
            case "follow" -> {
                Entity target = resolveEntity(maid, targetName.isEmpty() ? null : targetName, bridge.currentOwner());
                if (!(target instanceof ServerPlayer sp)) {
                    yield fail("invalid_target", "只认得同维度的玩家，没法跟着别的生物。");
                }
                bridge.setFollow(maid, sp);
                if (maid.distanceToSqr(sp) > 9) navigateTo(maid, sp.getX(), sp.getY(), sp.getZ(), 3.0);
                yield ok(maid, "现在开始跟着 " + sp.getName().getString());
            }
            case "wait" -> {
                bridge.clearFollow(maid);
                maid.getNavigation().stop();
                yield ok(maid, "停在原地等着。");
            }
            case "home" -> {
                BlockPos home = maid.getSchedulePos().getNearestPos(maid);
                if (home != null) {
                    navigateTo(maid, home.getX() + 0.5, home.getY(), home.getZ() + 0.5, 1.0);
                    yield ok(maid, "回家。");
                }
                ServerPlayer owner = bridge.currentOwner();
                if (owner != null && owner.level() == maid.level()) {
                    navigateTo(maid, owner.getX(), owner.getY(), owner.getZ(), 2.0);
                    yield ok(maid, "回到主人身边。");
                }
                yield fail("no_home", "没有设置过家，也找不到主人。");
            }
            case "wander" -> {
                double radius = Math.max(1, Math.min(WORK_REACH, WabJson.number(p, "radius", 8.0)));
                java.util.Random random = new java.util.Random();
                double angle = random.nextDouble() * Math.PI * 2;
                double x = maid.getX() + Math.cos(angle) * radius;
                double z = maid.getZ() + Math.sin(angle) * radius;
                if (!navigateTo(maid, x, maid.getY(), z, 1.0)) {
                    yield fail("cannot_reach", "那边去不了。");
                }
                yield ok(maid, "随便逛逛。");
            }
            default -> fail("unknown_mode", "不认识的移动方式：" + mode);
        };
    }

    /**
     * 寻路到 (x,y,z) 附近，并在离目标 stopDistance 处停下。
     * 原版寻路没有「停在几格之外」的概念，这里改成朝目标前方一点走：
     * 停在距目标 stopDistance 的位置，而不是挤到目标方块上。距离已经够近时直接停。
     */
    private static boolean navigateTo(EntityMaid maid, double x, double y, double z, double stopDistance) {
        if (!(maid.level() instanceof ServerLevel)) return false;
        double stop = clampStopDistance(stopDistance);
        double dx = x - maid.getX();
        double dz = z - maid.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal <= stop) {
            maid.getNavigation().stop();
            return true;
        }
        double scale = (horizontal - stop) / horizontal;
        return maid.getNavigation().moveTo(maid.getX() + dx * scale, y, maid.getZ() + dz * scale, 1.0F);
    }

    /** 停止距离来自远端数字，夹到 0.5—16 格之间，避免「停 0 格」或「停 100 格」这种怪值。 */
    private static double clampStopDistance(double raw) {
        if (!Double.isFinite(raw)) return 1.0;
        return Math.max(0.5, Math.min(16.0, raw));
    }

    private static Entity resolveEntity(EntityMaid maid, String name, Entity fallback) {
        if (name == null || name.isEmpty()) return fallback;
        if (fallback != null && fallback.getName().getString().equalsIgnoreCase(name)) return fallback;
        Level level = maid.level();
        List<Entity> candidates = level.getEntities(maid, maid.getBoundingBox().inflate(32),
                entity -> entity != maid && entity.isAlive());
        for (Entity entity : candidates) {
            if (entity.getName().getString().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 「递给谁」的解析：不写就是主人；写了名字或 UUID 就在同维度里找另一只女仆或玩家。
     * 女仆只在自己的名册里找——不然一张地图上别人家的女仆也能被她塞东西。
     */
    private static Entity resolveRecipient(WabBridge bridge, EntityMaid maid, String raw) {
        String text = raw == null ? "" : raw.trim();
        if (text.isEmpty()) {
            ServerPlayer owner = bridge.currentOwner();
            return owner != null && owner.isAlive() ? owner : null;
        }
        for (EntityMaid other : bridge.ownedMaids()) {
            if (other == maid || !other.isAlive() || other.level() != maid.level()) continue;
            if (WabBridge.cleanName(other).equalsIgnoreCase(text)
                    || other.getUUID().toString().equalsIgnoreCase(text)) {
                return other;
            }
        }
        for (Player player : maid.level().getEntitiesOfClass(Player.class,
                maid.getBoundingBox().inflate(64), Player::isAlive)) {
            if (player.getName().getString().equalsIgnoreCase(text)) return player;
        }
        return null;
    }

    // ==================== 攻击 ====================

    public static JsonObject attack(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String targetName = WabJson.str(p, "target");
        Entity target = resolveEntity(maid, targetName, null);
        if (target == null && targetName.isEmpty()) {
            // 没点名时只找有敌意的目标，不再「谁最近打谁」——主人站在旁边时那等于让她打主人。
            target = nearestHostile(maid, 8.0);
        }
        if (target == null) {
            return fail("no_target", "附近没有可以攻击的目标。");
        }
        ServerPlayer owner = bridge.currentOwner();
        if (owner != null && target.getUUID().equals(owner.getUUID())) {
            return fail("refused", "她不会对主人动手。");
        }
        double distance = maid.distanceToSqr(target);
        if (distance > 3.5 * 3.5) {
            navigateTo(maid, target.getX(), target.getY(), target.getZ(), 2.0);
            return fail("too_far", "目标还在 " + Math.round(Math.sqrt(distance) * 10) / 10.0 + " 格外，先让她靠近。");
        }
        maid.setTarget(target instanceof LivingEntity le ? le : null);
        maid.getLookControl().setLookAt(target, 30F, 30F);
        maid.swing(InteractionHand.MAIN_HAND);
        boolean hit = maid.doHurtTarget(target);
        if (!hit) {
            return fail("attack_failed", "这一下没打中 " + target.getName().getString() + "。");
        }
        return ok(maid, "对 " + target.getName().getString() + " 出手了。");
    }

    private static Entity nearestHostile(EntityMaid maid, double radius) {
        LivingEntity best = null;
        double bestDistance = radius * radius;
        List<Entity> candidates = maid.level().getEntities(maid, maid.getBoundingBox().inflate(radius),
                entity -> entity instanceof Monster && entity.isAlive());
        for (Entity entity : candidates) {
            double distance = maid.distanceToSqr(entity);
            if (distance <= bestDistance) {
                bestDistance = distance;
                best = (LivingEntity) entity;
            }
        }
        return best;
    }

    // ==================== 手势 / 说 / 姿态 ====================

    public static JsonObject act(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        double duration = WabJson.number(p, "duration", 2.0);
        int ticks = (int) Math.max(1, Math.min(60, duration) * 20);
        return switch (action) {
            case "attack" -> attack(bridge, maid, p);
            case "use" -> {
                JsonArray posArr = WabJson.arr(p, "pos");
                BlockPos pos = remotePos(maid, posArr, WORK_REACH);
                if (pos != null) {
                    yield ok(maid, useBlock(maid, pos, p));
                }
                if (!posArr.isEmpty()) {
                    yield fail("bad_pos", "那个坐标太远、还没加载，或者不在她能碰到的范围里。");
                }
                Entity target = resolveEntity(maid, WabJson.str(p, "target"), null);
                if (target == null) {
                    yield fail("no_target", "没有可用的使用目标。");
                }
                maid.getLookControl().setLookAt(target, 30F, 30F);
                maid.swing(InteractionHand.MAIN_HAND);
                yield ok(maid, "朝着 " + target.getName().getString() + " 比划了一下。");
            }
            case "gesture" -> {
                String arg = WabJson.str(p, "arg");
                String animation = WabCompat.animationForGesture(arg.isEmpty() ? "wave" : arg);
                com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler.sendToNearby(maid,
                        new WabAnimationMessage(maid.getId(), animation, ticks));
                if (arg.equals("sit")) maid.setInSittingPose(true);
                if (arg.equals("stand")) maid.setInSittingPose(false);
                yield ok(maid, "做了一个动作：" + animation);
            }
            case "say" -> {
                String text = WabJson.str(p, "text");
                if (!text.isEmpty() && maid.level() instanceof ServerLevel) {
                    maid.getChatBubbleManager().addLLMChatText(text, -1);
                    // 旁边站着别的女仆就把这句话递过去，她们才聊得起来；一个人自言自语时什么都不额外发。
                    bridge.noteMaidSpeech(maid, text);
                }
                yield ok(maid, "说：「" + text + "」");
            }
            case "sit" -> {
                maid.setInSittingPose(true);
                maid.getNavigation().stop();
                yield ok(maid, "坐下了。");
            }
            case "stand" -> {
                maid.setInSittingPose(false);
                yield ok(maid, "站了起来。");
            }
            case "sleep" -> {
                BlockPos sleepPos = remotePos(maid, WabJson.arr(p, "pos"), SIGHT_REACH);
                if (sleepPos == null) sleepPos = maid.getSchedulePos().getSleepPos();
                // 原来只认 BlockTags.BEDS，于是她自己那张女仆床反而睡不上——床一律按「是不是一张床」判。
                if (sleepPos != null && maid.level().isLoaded(sleepPos)
                        && WabSensors.isBedLike(maid.level().getBlockState(sleepPos))) {
                    maid.startSleeping(WabSensors.bedHead(maid.level(), sleepPos));
                    yield ok(maid, "上床睡觉了。");
                }
                com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler.sendToNearby(maid,
                        new WabAnimationMessage(maid.getId(), "sleep", ticks));
                yield ok(maid, "没有床，她只是闭眼休息了一会儿。");
            }
            case "look" -> {
                JsonArray posArr = WabJson.arr(p, "pos");
                BlockPos pos = remotePos(maid, posArr, SIGHT_REACH);
                Entity target = resolveEntity(maid, WabJson.str(p, "target"), null);
                if (target != null) {
                    maid.getLookControl().setLookAt(target, 30F, 30F);
                } else if (pos != null) {
                    maid.getLookControl().setLookAt(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 30F, 30F);
                } else if (!posArr.isEmpty()) {
                    yield fail("bad_pos", "那个坐标太远或者区块还没加载。");
                } else {
                    yield fail("no_target", "没有可以看的对象。");
                }
                yield ok(maid, "看向那边。");
            }
            case "interact_addon" -> ok(maid, WabCompat.invokeAddonAction(bridge, maid, WabJson.str(p, "arg")));
            default -> fail("unknown_action", "不认识的动作：" + action);
        };
    }

    private static String useBlock(EntityMaid maid, BlockPos pos, JsonObject p) {
        String itemHint = WabJson.str(p, "item");
        ItemStack mainHand = maid.getMainHandItem();
        if (mainHand.getItem() instanceof BlockItem && (itemHint.isEmpty() || itemHint.equals("block"))) {
            return maid.placeItemBlock(pos, mainHand) ? "把方块放了上去。" : "那个位置放不进去。";
        }
        if (mainHand.is(Items.BUCKET) && isFluid(maid.level(), pos)) {
            return scoopFluid(maid, pos, mainHand)
                    ? "舀起了一桶。"
                    : "那儿的水她舀不走——流动的水面不是源方块。";
        }
        maid.getLookControl().setLookAt(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 30F, 30F);
        maid.swing(InteractionHand.MAIN_HAND);
        return "朝着那个方块比划了一下。";
    }

    private static boolean isFluid(Level level, BlockPos pos) {
        return !level.getFluidState(pos).isEmpty();
    }

    /**
     * 舀一桶水/岩浆。只认源方块，且走女仆原生采集任务的同一道 canDestroyBlock 判定，
     * 这样保护类模组看到的行为和模组自己派活时一致。
     */
    private static boolean scoopFluid(EntityMaid maid, BlockPos pos, ItemStack bucket) {
        if (!(maid.level() instanceof ServerLevel level)) return false;
        BlockState state = level.getBlockState(pos);
        if (!(state.is(Blocks.WATER) || state.is(Blocks.LAVA)) || !state.getFluidState().isSource()) return false;
        if (!maid.canDestroyBlock(pos)) return false;
        // 一桶换一桶：以前按 count 生成，16 只空桶能从一个源方块灌出 16 桶水。
        bucket.shrink(1);
        ItemStack filled = new ItemStack(state.is(Blocks.LAVA) ? Items.LAVA_BUCKET : Items.WATER_BUCKET);
        ItemStack leftover = ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(false), filled, false);
        if (!leftover.isEmpty()) {
            maid.level().addFreshEntity(new ItemEntity(maid.level(), maid.getX(), maid.getY(), maid.getZ(), leftover));
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        return true;
    }

    // ==================== 物品 ====================

    public static JsonObject hold(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        int count = WabJson.integer(p, "count", 1);
        String item = WabJson.str(p, "item");
        String target = WabJson.str(p, "target");
        return switch (action) {
            case "give" -> {
                // 递给谁：不写就是主人。以前只认主人，AI 想让两只女仆换东西、或者把石头塞给路过的同伴，全都得吃一次「no_owner」。
                String to = WabJson.str(p, "to");
                Entity recipient = resolveRecipient(bridge, maid, to);
                if (recipient == null) {
                    yield fail("no_recipient", to.isEmpty() ? "主人不在线。" : "找不到要递给她的人：" + to);
                }
                // 协议里递东西是 3 格内的手边动作；8 格等于允许隔空投喂。
                if (recipient.level() != maid.level() || maid.distanceToSqr(recipient) > 3 * 3) {
                    if (recipient.level() == maid.level()) {
                        navigateTo(maid, recipient.getX(), recipient.getY(), recipient.getZ(), 2.0);
                    }
                    yield fail("too_far", "离 " + recipient.getName().getString() + " 有点远，没法递过去。");
                }
                ItemStack stack = takeFromMaid(maid, item, count);
                if (stack.isEmpty()) yield fail("not_held", "手上没有要找的东西。");
                int wanted = stack.getCount();
                IItemHandler into = recipient instanceof ServerPlayer player
                        ? new InvWrapper(player.getInventory())
                        : ((EntityMaid) recipient).getAvailableInv(false);
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(into, stack, false);
                // 对方包塞不下时剩下的必须落地，不能留在手里这个临时变量里蒸发掉。
                if (!remainder.isEmpty()) {
                    recipient.level().addFreshEntity(new ItemEntity(recipient.level(),
                            recipient.getX(), recipient.getY(), recipient.getZ(), remainder));
                }
                int handed = Math.max(0, wanted - remainder.getCount());
                maid.swing(InteractionHand.MAIN_HAND);
                yield ok(maid, handed > 0
                        ? "递给了 " + recipient.getName().getString() + "（" + handed + " 个）。"
                        : recipient.getName().getString() + " 包满了，只能丢在他脚边。");
            }
            case "drop" -> {
                ItemStack stack = takeFromMaid(maid, item, count);
                if (stack.isEmpty()) yield fail("not_held", "手上没有要找的东西。");
                ItemEntity dropped = new ItemEntity(maid.level(), maid.getX(), maid.getY(), maid.getZ(), stack);
                maid.level().addFreshEntity(dropped);
                yield ok(maid, "丢在了地上。");
            }
            case "take" -> collectNearby(maid, WabJson.str(p, "item"), count, WabJson.number(p, "radius", 8.0));
            case "feed_player" -> {
                ServerPlayer owner = bridge.currentOwner();
                if (owner == null) yield fail("no_owner", "主人不在线。");
                if (owner.level() != maid.level() || owner.distanceToSqr(maid) > 3 * 3) {
                    if (owner.level() == maid.level()) navigateTo(maid, owner.getX(), owner.getY(), owner.getZ(), 2.0);
                    yield fail("too_far", "隔得太远，喂不到。");
                }
                ItemStack food = takeFromMaid(maid, item.isEmpty() ? "food" : item, 1);
                if (food.isEmpty() || food.getItem().getFoodProperties(food, owner) == null) {
                    yield fail("no_food", "手上没有能喂的食物。");
                }
                if (food.getUseAnimation() == net.minecraft.world.item.UseAnim.DRINK) {
                    owner.level().playSound(null, owner, food.getDrinkingSound(),
                            net.minecraft.sounds.SoundSource.NEUTRAL, 0.5F, 0.9F);
                }
                food.getItem().finishUsingItem(food, owner.level(), owner);
                com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler.sendToNearby(maid,
                        new WabAnimationMessage(maid.getId(), "use_mainhand", 40));
                yield ok(maid, "直接把食物喂给了 " + owner.getName().getString() + "。");
            }
            case "place" -> {
                JsonArray posArr = WabJson.arr(p, "pos");
                BlockPos pos = remotePos(maid, posArr, WORK_REACH);
                if (pos == null) {
                    yield posArr.isEmpty() ? fail("no_pos", "没有给放置的位置。")
                            : fail("bad_pos", "那个位置她够不着，或者区块还没加载。");
                }
                ItemStack stack = takeFromMaid(maid, item.isEmpty() ? "block" : item, 1);
                if (stack.isEmpty()) yield fail("not_held", "手上没有可放置的东西。");
                yield ok(maid, stack.getItem() instanceof BlockItem && maid.placeItemBlock(pos, stack)
                        ? "放了上去。" : "不确定能不能放这儿。");
            }
            case "equip" -> {
                ItemStack stack = takeFromMaid(maid, item, 1);
                if (stack.isEmpty()) yield fail("not_held", "手上没有要找的东西。");
                String slot = target.isEmpty() ? "mainhand" : target.toLowerCase(Locale.ROOT);
                net.minecraft.world.entity.EquipmentSlot equipmentSlot = switch (slot) {
                    case "offhand" -> net.minecraft.world.entity.EquipmentSlot.OFFHAND;
                    case "head" -> net.minecraft.world.entity.EquipmentSlot.HEAD;
                    case "chest" -> net.minecraft.world.entity.EquipmentSlot.CHEST;
                    case "legs" -> net.minecraft.world.entity.EquipmentSlot.LEGS;
                    case "feet" -> net.minecraft.world.entity.EquipmentSlot.FEET;
                    default -> net.minecraft.world.entity.EquipmentSlot.MAINHAND;
                };
                ItemStack previous = maid.getItemBySlot(equipmentSlot);
                maid.setItemSlot(equipmentSlot, stack);
                // 原来那件装备不能凭空消失：先退回背包，塞不下就丢在脚边。
                if (!previous.isEmpty()) {
                    ItemStack orphan = ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(false), previous, false);
                    if (!orphan.isEmpty()) {
                        maid.level().addFreshEntity(new ItemEntity(maid.level(), maid.getX(), maid.getY(), maid.getZ(), orphan));
                    }
                }
                yield ok(maid, "装备好了，原来那件收回包里了。");
            }
            default -> fail("unknown_action", "不认识的动作：" + action);
        };
    }

    public static JsonObject collectNearby(EntityMaid maid, String item, int count, double radius) {
        // 半径和数量都要夹：这条路径能被远端直接指定，不夹的话一次扫描能把整片区域的东西吸走。
        radius = Math.max(1, Math.min(32, radius));
        count = Math.max(1, Math.min(64, count));
        List<ItemEntity> picked = new ArrayList<>();
        double maxDistance = radius * radius;
        List<Entity> candidates = maid.level().getEntities(maid, maid.getBoundingBox().inflate(radius),
                entity -> entity instanceof ItemEntity);
        for (Entity entity : candidates) {
            if (!(entity instanceof ItemEntity itemEntity) || !itemEntity.isAlive()) continue;
            if (maid.distanceToSqr(itemEntity) > maxDistance) continue;
            if (!itemEntity.getItem().isEmpty() && (item.isEmpty() || item.equals(WabSensors.itemId(itemEntity.getItem())))) {
                picked.add(itemEntity);
            }
            if (picked.size() >= count) break;
        }
        if (picked.isEmpty()) {
            return fail("nothing_nearby", "附近没有要找的东西。");
        }
        int got = 0;
        for (ItemEntity entity : picked) {
            if (maid.pickupItem(entity, false)) got++;
        }
        return ok(maid, "捡起了 " + got + " 件东西。");
    }

    // ==================== 长时间任务（Job）====================

    public static final class Job {
        private final WabBridge bridge;
        final EntityMaid maid;
        private final String action;
        private final List<BlockPos> targets = new ArrayList<>();
        private int index;
        private final int want;
        private int got;
        private final int budgetTicks;
        private int tickCount;
        private final boolean autonomous;
        private final Responder responder;
        private int stuckTicks;
        private double lastDistance = -1;
        private boolean guiWasOpening;
        private boolean done;
        private String cancelReason = "";
        private Map<String, Integer> beforeInventory = new HashMap<>();

        private Job(WabBridge bridge, EntityMaid maid, String action, int want, int budgetTicks,
                    boolean autonomous, Responder responder) {
            this.bridge = bridge;
            this.maid = maid;
            this.action = action;
            this.want = want;
            this.budgetTicks = budgetTicks;
            this.autonomous = autonomous;
            this.responder = responder;
        }

        void begin() {
            guiWasOpening = maid.guiOpening;
            maid.guiOpening = true;
            maid.getNavigation().stop();
            beforeInventory = snapshotInventory(maid);
        }

        private static Map<String, Integer> snapshotInventory(EntityMaid maid) {
            Map<String, Integer> result = new HashMap<>();
            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    result.merge(WabSensors.itemId(stack), stack.getCount(), Integer::sum);
                }
            }
            return result;
        }

        public void tick() {
            if (done) return;
            tickCount++;
            if (!maid.isAlive() || !maid.level().isLoaded(maid.blockPosition())) {
                finish(false, "她倒下了，任务中止。");
                return;
            }
            if (tickCount >= budgetTicks) {
                finish(false, "时间到了，先停下来。");
                return;
            }
            if (index >= targets.size()) {
                finish(true, "干完了手头的事。");
                return;
            }
            BlockPos current = targets.get(index);
            if (!blockStillValid(current)) {
                index++;
                stuckTicks = 0;
                lastDistance = -1;
                if (index >= targets.size()) finish(true, "剩下的都去不了。");
                return;
            }
            double distance = maid.distanceToSqr(current.getX() + 0.5, current.getY(), current.getZ() + 0.5);
            if (distance > 20) { // 大约 4.5 格射程
                if (distance - lastDistance > 0.05) stuckTicks++;
                else stuckTicks = 0;
                lastDistance = distance;
                if (stuckTicks > 60 || !maid.getNavigation().moveTo(current.getX() + 0.5, current.getY(),
                        current.getZ() + 0.5, 1.0F)) {
                    if (stuckTicks > 60 || !maid.canPathReach(current)) {
                        index++;
                        stuckTicks = 0;
                        lastDistance = -1;
                        if (index >= targets.size()) finish(true, "剩下的都去不了。");
                    }
                    return;
                }
                return;
            }
            // 到射程内，执行动作
            executeAction(current);
            index++;
            stuckTicks = 0;
            lastDistance = -1;
            if (index >= targets.size() || got >= want) {
                finish(true, "完成了能做的部分。");
            }
        }

        private void executeAction(BlockPos pos) {
            switch (action) {
                case "mine", "chop", "harvest" -> {
                    // 保护区/工具不对时 destroyBlock 会返回 false：不能照样记一次「挖到」，
                    // 否则 AI 以为拿到了资源，背包里其实什么都没有。
                    if (maid.destroyBlock(pos, true)) got++;
                }
                case "collect" -> {
                    List<ItemEntity> nearby = new ArrayList<>();
                    for (Entity entity : maid.level().getEntities(maid, maid.getBoundingBox().inflate(3.0),
                            entity -> entity instanceof ItemEntity)) {
                        if (entity instanceof ItemEntity itemEntity && maid.distanceToSqr(itemEntity) < 3.0 * 3.0) {
                            nearby.add(itemEntity);
                        }
                    }
                    for (ItemEntity entity : nearby) {
                        if (maid.pickupItem(entity, false)) got++;
                    }
                }
                case "scoop" -> {
                    ItemStack bucket = takeFromMaid(maid, "bucket", 1);
                    if (!bucket.isEmpty() && scoopFluid(maid, pos, bucket)) {
                        got++;
                    }
                }
                case "fill" -> {
                    ItemStack block = takeFromMaid(maid, "block", 1);
                    if (!block.isEmpty() && block.getItem() instanceof BlockItem && maid.placeItemBlock(pos, block)) {
                        got++;
                    }
                }
                default -> {
                }
            }
        }

        private boolean blockStillValid(BlockPos pos) {
            if (!maid.level().isLoaded(pos)) return false;
            BlockState state = maid.level().getBlockState(pos);
            switch (action) {
                case "mine":
                    return WabSensors.isOre(state);
                case "chop":
                    return state.is(BlockTags.LOGS);
                case "harvest":
                    return WabSensors.isMatureCrop(state);
                case "collect", "scoop", "fill":
                    return true;
                default:
                    return true;
            }
        }

        public void cancel(String reason) {
            if (done) return;
            cancelReason = reason;
            done = true;
            maid.guiOpening = guiWasOpening;
        }

        public void finish(boolean complete, String message) {
            if (done) return;
            done = true;
            maid.guiOpening = guiWasOpening;
            JsonObject data = new JsonObject();
            data.addProperty("action", action);
            data.addProperty("complete", complete);
            data.addProperty("message", message);
            data.addProperty("got", got);
            data.addProperty("wanted", want);
            JsonArray loot = new JsonArray();
            Map<String, Integer> after = snapshotInventory(maid);
            for (Map.Entry<String, Integer> entry : after.entrySet()) {
                int before = beforeInventory.getOrDefault(entry.getKey(), 0);
                int delta = entry.getValue() - before;
                if (delta > 0) {
                    JsonObject item = new JsonObject();
                    item.addProperty("id", entry.getKey());
                    item.addProperty("count", delta);
                    loot.add(item);
                }
            }
            data.add("loot", loot);

            if (autonomous) {
                bridge.sendEvent("job_" + action, maid, message, complete ? 0.4 : 0.25, 0, "自主任务结束");
            } else if (responder != null) {
                if (complete || got > 0) {
                    JsonObject result = new JsonObject();
                    result.addProperty("ok", true);
                    result.add("data", data);
                    responder.success(data);
                } else {
                    responder.failure("nothing_done", message);
                }
            }
        }

        public boolean isActive() {
            return !done;
        }
    }

    public static Job startGather(WabBridge bridge, EntityMaid maid, JsonObject params, int budgetTicks,
                                  boolean autonomous, Responder responder) {
        String action = WabJson.str(params, "action", "mine").toLowerCase(Locale.ROOT);
        int want = WabJson.integer(params, "count", 16);
        double radius = WabJson.number(params, "radius", 16.0);
        radius = Math.max(4, Math.min(24, radius));
        String filter = WabJson.str(params, "filter");

        Job job = new Job(bridge, maid, action, want, budgetTicks, autonomous, responder);
        List<BlockPos> found = new ArrayList<>();
        BlockPos center = maid.blockPosition();
        int r = (int) radius;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, 0, -r), center.offset(r, 2, r))) {
            if (!maid.level().isLoaded(pos)) continue;
            if (pos.distSqr(center) > radius * radius) continue;
            BlockState state = maid.level().getBlockState(pos);
            boolean matches = switch (action) {
                case "mine" -> filter.isEmpty() || filter.equals("ores")
                        ? WabSensors.isOre(state)
                        : WabSensors.blockId((ServerLevel) maid.level(), pos).equals(filter);
                case "chop" -> state.is(BlockTags.LOGS);
                case "harvest" -> WabSensors.isMatureCrop(state);
                case "scoop" -> state.getFluidState().isSource()
                        && (state.is(Blocks.WATER) || state.is(Blocks.LAVA));
                default -> false;
            };
            if (matches) found.add(pos.immutable());
        }
        found.sort(Comparator.comparingDouble((BlockPos a) -> a.distSqr(center)));
        if (found.size() > want) found.subList(want, found.size()).clear();
        job.targets.addAll(found);
        job.begin();
        return job;
    }

    public static JsonObject collect(WabBridge bridge, EntityMaid maid, JsonObject params) {
        String item = WabJson.str(params, "filter");
        int count = WabJson.integer(params, "count", 16);
        double radius = WabJson.number(params, "radius", 8.0);
        return collectNearby(maid, item.isEmpty() ? WabJson.str(params, "item") : item, count, radius);
    }

    public static JsonObject fill(WabBridge bridge, EntityMaid maid, JsonObject params) {
        JsonArray posArr = WabJson.arr(params, "pos");
        BlockPos pos = remotePos(maid, posArr, WORK_REACH);
        if (pos == null && !posArr.isEmpty()) {
            return fail("bad_pos", "那个位置她够不着，或者区块还没加载。");
        }
        if (pos == null) {
            pos = maid.blockPosition().above();
        }
        if (!maid.level().getBlockState(pos).canBeReplaced()) {
            return fail("occupied", "那个位置已经是固体方块了，塞不进去。");
        }
        ItemStack block = takeFromMaid(maid, WabJson.str(params, "item"), 1);
        if (block.isEmpty() || !(block.getItem() instanceof BlockItem)) {
            return fail("not_held", "手上没有可以填进去的方块。");
        }
        if (maid.placeItemBlock(pos, block)) {
            return ok(maid, "填上了。");
        }
        return fail("cannot_place", "放不上去，可能位置不对。");
    }

    // ==================== 日程 / 计划 ====================

    public static JsonObject plan(WabBridge bridge, EntityMaid maid, JsonObject params) {
        String action = WabJson.str(params, "action", "get").toLowerCase(Locale.ROOT);
        return switch (action) {
            case "get" -> {
                JsonObject data = new JsonObject();
                data.addProperty("plan", String.valueOf(maid.getScheduleDetail()));
                data.addProperty("homeMode", String.valueOf(maid.isHomeModeEnable()));
                data.addProperty("work", String.valueOf(maid.getSchedulePos().getWorkPos()));
                data.addProperty("idle", String.valueOf(maid.getSchedulePos().getIdlePos()));
                data.addProperty("sleep", String.valueOf(maid.getSchedulePos().getSleepPos()));
                data.addProperty("steps", maid.getPersistentData().getString("WabPlan"));
                data.addProperty("autonomy", maid.getPersistentData().getString("WabAutonomy"));
                yield data;
            }
            case "clear" -> {
                CompoundTag data = maid.getPersistentData();
                // 以前这里只回了一句「已清空」，两行数据原封不动地留在存档里，
                // 于是日程会一天一天继续跑，换回上游模组也带着它。
                data.remove("WabPlan");
                data.remove("WabAutonomy");
                bridge.autonomy().notePlanChanged(maid);
                yield ok(maid, "日程已经清掉了，她不会再自己按点行动。");
            }
            case "set" -> {
                CompoundTag data = maid.getPersistentData();
                data.putString("WabPlan", WabJson.arr(params, "steps").toString());
                JsonObject autonomy = WabJson.obj(params, "autonomy");
                if (autonomy.size() > 0) {
                    data.putString("WabAutonomy", autonomy.toString());
                } else {
                    data.remove("WabAutonomy");
                }
                bridge.autonomy().notePlanChanged(maid);
                yield ok(maid, "日程写好了，她知道了今天要做什么。");
            }
            default -> fail("unknown_action", "不认识的日程操作。");
        };
    }

    // ==================== 工具 ====================

    private static ItemStack takeFromMaid(EntityMaid maid, String hint, int count) {
        if (hint.isEmpty() || hint.equals("food") || hint.equals("block") || hint.equals("bucket")) {
            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                boolean match = switch (hint) {
                    case "food" -> stack.getItem().getFoodProperties(stack, maid) != null;
                    case "block" -> stack.getItem() instanceof BlockItem;
                    case "bucket" -> stack.is(Items.BUCKET);
                    default -> true;
                };
                if (match) {
                    ItemStack take = inv.extractItem(i, count, false);
                    if (!take.isEmpty()) return take;
                }
            }
            return ItemStack.EMPTY;
        }
        String wanted = hint.toLowerCase(Locale.ROOT);
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            String id = WabSensors.itemId(stack);
            if (id.equals(wanted) || id.toLowerCase(Locale.ROOT).contains(wanted)) {
                return inv.extractItem(i, count, false);
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * WAB 传来的坐标属于远端数据：必须能安全取值、区块已加载、在世界边界内、离她够近，
     * 任一条件不满足就当没给，由调用方决定是回错误还是用她自己的位置。
     */
    private static BlockPos remotePos(EntityMaid maid, JsonArray arr, double maxDistance) {
        if (arr.size() < 3) return null;
        double x = coord(arr, 0);
        double y = coord(arr, 1);
        double z = coord(arr, 2);
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) return null;
        BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
        return reachable(maid, pos, maxDistance) ? pos : null;
    }

    private static double coord(JsonArray arr, int index) {
        JsonElement element = arr.get(index);
        if (element == null || !element.isJsonPrimitive()) return Double.NaN;
        try {
            return element.getAsDouble();
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private static boolean reachable(EntityMaid maid, BlockPos pos, double maxDistance) {
        if (!(maid.level() instanceof ServerLevel level)) return false;
        if (!level.isLoaded(pos)) return false;
        if (pos.getY() < level.getMinBuildHeight() || pos.getY() > level.getMaxBuildHeight()) return false;
        if (!level.getWorldBorder().isWithinBounds(pos)) return false;
        return maid.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= maxDistance * maxDistance;
    }

    private static JsonObject fail(String code, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", false);
        out.addProperty("code", code);
        out.addProperty("error", message);
        return out;
    }

    /** 一次性动作的回执。以前这个对象造出来就丢了，AI 那边只收到空的 data、看不到她做了什么。 */
    private static JsonObject ok(EntityMaid maid, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("maid", maid.getUUID().toString());
        out.addProperty("message", message);
        return out;
    }
}
