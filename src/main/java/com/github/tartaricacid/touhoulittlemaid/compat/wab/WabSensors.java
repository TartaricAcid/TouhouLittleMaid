package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 只读感知：世界、女仆、周围实体、矿石、流体、亮度、容器。
 * 所有结果都是 JSON，方便直接放进 status / scan 的 result。
 */
public final class WabSensors {
    /** 床的两个属性。原版床和女仆床都用这一对，所以能一起读。 */
    private static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    private static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    private WabSensors() {
    }

    public static JsonObject world(WabBridge bridge) {
        ServerLevel level = bridge.server().overworld();
        JsonObject out = new JsonObject();
        out.addProperty("name", bridge.worldName());
        out.addProperty("dimension", level.dimension().location().toString());
        out.addProperty("weather", weather(level));
        long dayTime = level.getDayTime();
        out.addProperty("dayTime", dayTime);
        out.addProperty("time", clock(dayTime));
        out.addProperty("day", dayTime / 24000L);
        out.addProperty("ownerOnline", bridge.currentOwner() != null);
        out.addProperty("players", bridge.server().getPlayerList().getPlayerCount());
        out.addProperty("raining", level.isRaining());
        out.addProperty("thundering", level.isThundering());
        return out;
    }

    public static String weather(ServerLevel level) {
        if (level.isThundering()) return "thunder";
        if (level.isRaining()) return "rain";
        return "clear";
    }

    /** dayTime 0 是 06:00；1000 tick 等于游戏内一小时。 */
    public static String clock(long dayTime) {
        long normalized = ((dayTime + 6000L) % 24000L + 24000L) % 24000L;
        int hour = (int) (normalized / 1000L);
        int minute = (int) ((normalized % 1000L) * 60L / 1000L);
        return String.format(Locale.ROOT, "%02d:%02d", hour, minute);
    }

    public static JsonObject snapshot(WabBridge bridge, EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.addProperty("id", maid.getUUID().toString());
        out.addProperty("name", WabBridge.cleanName(maid));
        out.addProperty("model", maid.getModelId());
        out.addProperty("health", round(maid.getHealth()));
        out.addProperty("maxHealth", round(maid.getMaxHealth()));
        JsonObject love = loveLoathe(maid);
        out.addProperty("food", love != null ? round(love.get("hunger").getAsDouble() / 5.0) : maid.getHunger());
        JsonArray pos = new JsonArray();
        pos.add(round(maid.getX()));
        pos.add(round(maid.getY()));
        pos.add(round(maid.getZ()));
        out.add("pos", pos);
        out.addProperty("dimension", maid.level().dimension().location().toString());
        out.addProperty("task", maid.getTask().getUid().toString());
        out.addProperty("activity", maid.getScheduleDetail().getName());
        out.addProperty("owner", ownerName(maid));
        Player owner = maid.getOwner() instanceof Player player ? player : null;
        out.addProperty("distanceToOwner", owner != null && owner.level() == maid.level()
                ? round(Math.sqrt(maid.distanceToSqr(owner))) : -1);
        if (love != null) out.add("loveLoathe", love);
        out.addProperty("sitting", maid.isInSittingPose());
        out.addProperty("sleeping", maid.isSleeping());
        out.addProperty("onGround", maid.onGround());
        out.addProperty("inWater", maid.isInWater());
        out.addProperty("pickup", maid.isPickup());
        return out;
    }

    public static JsonObject status(WabBridge bridge, EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.add("world", world(bridge));
        out.add("maid", snapshot(bridge, maid));
        out.addProperty("uuid", maid.getUUID().toString());
        out.addProperty("healthDetail", round(maid.getHealth()) + "/" + round(maid.getMaxHealth()));
        out.addProperty("favorability", maid.getFavorability());
        out.addProperty("experience", maid.getExperience());
        out.addProperty("hunger", maid.getHunger());
        out.addProperty("schedule", maid.getSchedule().name());
        out.addProperty("homeMode", maid.isHomeModeEnable());
        out.addProperty("restrictRadius", round(maid.getRestrictRadius()));
        BlockPos center = maid.getRestrictCenter();
        out.add("restrictCenter", blockPos(center));
        out.addProperty("taskName", taskName(maid));
        out.add("mainHand", item(maid.getMainHandItem()));
        out.add("offHand", item(maid.getOffhandItem()));
        out.add("inventory", inventory(maid, 24));
        out.add("armor", armor(maid));
        out.add("nearbyPlayers", players(maid, 16));
        out.add("schedulePos", schedulePos(maid));
        return out;
    }

    public static JsonObject scan(WabBridge bridge, EntityMaid maid, JsonObject params) {
        int radius = Math.max(4, Math.min(64, WabJson.integer(params, "radius", 16)));
        // 方块/流体是立方体遍历（(2r+1)^3 次 getBlockState）：r=64 时是 210 万次，
        // 一帧就能把服务器线程卡住。实体查询走 AABB，成本低得多，所以只夹方块那一半。
        int blockRadius = Math.min(radius, 24);
        boolean wantEntities = WabJson.bool(params, "entities", true);
        boolean wantBlocks = WabJson.bool(params, "blocks", true);
        boolean wantFluids = WabJson.bool(params, "fluids", true);
        boolean wantLight = WabJson.bool(params, "light", true);
        ServerLevel level = (ServerLevel) maid.level();

        JsonObject out = new JsonObject();
        out.add("world", world(bridge));
        out.add("from", snapshot(bridge, maid));
        out.addProperty("radius", radius);
        out.addProperty("blockRadius", blockRadius);

        if (wantEntities) out.add("entities", entities(maid, radius));
        if (wantBlocks) {
            JsonObject blocks = blocks(level, maid, blockRadius);
            out.add("ores", blocks.get("ores"));
            out.add("containers", blocks.get("containers"));
            out.add("crops", blocks.get("crops"));
            out.add("beds", blocks.get("beds"));
        }
        if (wantFluids) out.add("fluids", fluids(level, maid, blockRadius));
        if (wantLight) out.add("light", light(level, maid));
        out.add("drops", itemEntities(maid, Math.min(radius, 12)));
        return out;
    }

    /**
     * 她「眼前」看到了什么：只报视线锥里、而且真的没有被方块挡住的東西，再拼成一段人话交给 AI。
     * scan 是把周围一圈全摊开，这里回答的是另一件事——「她盯着的方向上有什么」。
     * 半径和视角都钳过：这条能被远端点名调用，不钳的话一次查询能把整片区块扫穿。
     */
    public static JsonObject see(WabBridge bridge, EntityMaid maid, JsonObject params) {
        int radius = Math.max(4, Math.min(48, WabJson.integer(params, "radius", 20)));
        int cone = Math.max(20, Math.min(170, WabJson.integer(params, "cone", 70)));
        ServerLevel level = (ServerLevel) maid.level();
        Vec3 eye = maid.getEyePosition();
        Vec3 forward = maid.getLookAngle().normalize();
        double cosLimit = Math.cos(Math.toRadians(cone / 2.0));
        String facing = facingName(maid.getDirection());

        JsonArray targets = inFront(level, maid, eye, forward, radius, cosLimit);
        JsonArray blocks = notableInCone(level, maid, eye, forward, cosLimit, Math.min(radius, 14));
        JsonObject looksAt = rayHit(level, maid, eye, forward, radius);

        JsonObject out = new JsonObject();
        out.addProperty("maid", maid.getUUID().toString());
        out.addProperty("facing", maid.getDirection().getName());
        out.addProperty("facingText", facing);
        out.addProperty("radius", radius);
        out.addProperty("cone", cone);
        out.add("eye", vec(eye));
        out.add("world", world(bridge));
        out.add("light", light(level, maid));
        out.add("targets", targets);
        out.add("blocks", blocks);
        out.add("ground", blockAt(level, maid.blockPosition().below()));
        if (looksAt != null) out.add("looksAt", looksAt);
        out.addProperty("text", describe(facing, targets, blocks, looksAt, out));
        return out;
    }

    /** 视线锥里的活物。先按点积筛方向，再做遮挡判定，最后按远近排——顺序不能反，点积最便宜。 */
    private static JsonArray inFront(ServerLevel level, EntityMaid maid, Vec3 eye, Vec3 forward,
                                     int radius, double cosLimit) {
        List<Entity> candidates = new ArrayList<>(level.getEntities(maid, maid.getBoundingBox().inflate(radius),
                entity -> entity != maid && entity.isAlive()));
        candidates.sort(Comparator.comparingDouble(maid::distanceToSqr));
        JsonArray out = new JsonArray();
        for (Entity entity : candidates) {
            if (out.size() >= 16) break;
            Vec3 to = entity.getEyePosition().subtract(eye);
            double distance = to.length();
            if (distance < 0.4 || distance > radius) continue;
            Vec3 dir = to.normalize();
            if (dir.dot(forward) < cosLimit) continue;
            if (!visible(level, eye, entity.getEyePosition())) continue;
            JsonObject item = new JsonObject();
            item.addProperty("name", entity.getName().getString());
            item.addProperty("type", entityType(entity));
            item.addProperty("kind", kind(entity));
            item.addProperty("distance", round(distance));
            item.addProperty("side", sideLabel(forward, dir));
            item.addProperty("height", dir.y > 0.28 ? "在上方" : dir.y < -0.28 ? "在下方" : "平视");
            item.add("pos", vecPos(entity));
            if (entity instanceof LivingEntity living) {
                item.addProperty("health", round(living.getHealth()));
            }
            if (entity instanceof Player player) {
                item.addProperty("player", player.getGameProfile().getName());
            }
            out.add(item);
        }
        return out;
    }

    /** 锥里有用的方块（矿石/容器/成熟作物）。这一层不做遮挡——「那块铁矿在墙后面」对她规划路线同样是信息。 */
    private static JsonArray notableInCone(ServerLevel level, EntityMaid maid, Vec3 eye, Vec3 forward,
                                           double cosLimit, int radius) {
        JsonArray out = new JsonArray();
        BlockPos origin = maid.blockPosition();
        List<FoundBlock> found = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -radius, -radius),
                origin.offset(radius, radius, radius))) {
            if (pos.distSqr(origin) > (double) radius * radius) continue;
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            boolean ore = isOre(state);
            boolean container = state.hasBlockEntity() && level.getBlockEntity(pos) instanceof Container;
            boolean crop = isMatureCrop(state);
            boolean bed = isBedLike(state) && state.hasProperty(PART) && state.getValue(PART) == BedPart.HEAD;
            if (!ore && !container && !crop && !bed) continue;
            Vec3 to = new Vec3(pos.getX() + 0.5 - eye.x, pos.getY() + 0.5 - eye.y, pos.getZ() + 0.5 - eye.z);
            double distance = to.length();
            if (distance < 0.4 || distance > radius) continue;
            Vec3 dir = to.normalize();
            if (dir.dot(forward) < cosLimit) continue;
            found.add(new FoundBlock(pos.immutable(), distance, ore, container, crop, bed));
        }
        found.sort(Comparator.comparingDouble(FoundBlock::distance));
        for (FoundBlock block : found) {
            if (out.size() >= 24) break;
            JsonObject item = blockJson(block.pos, maid);
            item.addProperty("block", blockId(level, block.pos));
            item.addProperty("what", block.ore ? "矿" : block.container ? "容器" : block.bed ? "床" : "作物");
            if (block.bed) {
                BlockState state = level.getBlockState(block.pos);
                item.addProperty("for", isMaidBed(state) ? "maid" : "vanilla");
                item.addProperty("occupied", state.hasProperty(OCCUPIED) && state.getValue(OCCUPIED));
            }
            out.add(item);
        }
        return out;
    }

    /** 她正看着哪儿：沿视线步进，撞上第一个实心方块就停。 */
    private static JsonObject rayHit(ServerLevel level, EntityMaid maid, Vec3 eye, Vec3 forward, int radius) {
        for (double d = 0.5; d <= radius; d += 0.4) {
            Vec3 at = eye.add(forward.scale(d));
            BlockPos pos = new BlockPos(Mth.floor(at.x), Mth.floor(at.y), Mth.floor(at.z));
            if (!level.isLoaded(pos)) return null;
            if (!level.getFluidState(pos).isEmpty()) {
                JsonObject hit = blockJson(pos, maid);
                hit.addProperty("fluid", level.getFluidState(pos).is(FluidTags.LAVA) ? "lava" : "water");
                hit.addProperty("at", round(d));
                return hit;
            }
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            // 玻璃、栅栏、台阶这类不遮视线的方块不算「撞上」——她透过它们看见的东西才是她想说的。
            if (!state.canOcclude() && !state.hasBlockEntity()) continue;
            JsonObject hit = blockJson(pos, maid);
            hit.addProperty("block", blockId(level, pos));
            hit.addProperty("at", round(d));
            return hit;
        }
        return null;
    }

    /**
     * 遮挡判定用采样，不用 level.clip：Ray / ClipResult 这几个版本挪过家，
     * 采样只依赖 isAir 和 canOcclude 两个一直没变的口子。
     */
    private static boolean visible(ServerLevel level, Vec3 from, Vec3 to) {
        double distance = from.distanceTo(to);
        int steps = (int) Math.max(2, Math.min(160, distance * 3));
        Vec3 delta = to.subtract(from).scale(1.0 / steps);
        double x = from.x + delta.x * 0.6;
        double y = from.y + delta.y * 0.6;
        double z = from.z + delta.z * 0.6;
        for (int i = 0; i < steps; i++) {
            BlockPos pos = new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
            if (!level.isLoaded(pos)) return false;
            if (level.getBlockState(pos).canOcclude()) return false;
            x += delta.x;
            y += delta.y;
            z += delta.z;
        }
        return true;
    }

    private static String describe(String facing, JsonArray targets, JsonArray blocks,
                                   JsonObject looksAt, JsonObject out) {
        StringBuilder sb = new StringBuilder();
        JsonObject world = WabJson.obj(out, "world");
        sb.append("她面朝").append(facing).append("，此刻 ").append(WabJson.str(world, "time", "?"))
                .append("，").append(weatherName(WabJson.str(world, "weather", "clear"))).append("。");
        if (targets.size() == 0) {
            sb.append("眼前没有活物。");
        } else {
            sb.append("眼前有 ").append(targets.size()).append(" 个：");
            for (int i = 0; i < targets.size(); i++) {
                JsonObject item = targets.get(i).getAsJsonObject();
                if (i > 0) sb.append("，");
                sb.append(nameOf(item)).append("在").append(WabJson.str(item, "side", "正前方"))
                        .append(roundText(WabJson.number(item, "distance", 0))).append(" 格");
            }
            sb.append("。");
        }
        if (looksAt != null) {
            if (looksAt.has("fluid")) {
                sb.append("她正对着一片").append(WabJson.str(looksAt, "fluid").equals("lava") ? "岩浆" : "水")
                        .append("，离她 ").append(roundText(WabJson.number(looksAt, "at", 0))).append(" 格。");
            } else {
                sb.append("视线尽头是一格 ").append(shortId(WabJson.str(looksAt, "block", "方块")))
                        .append("，离她 ").append(roundText(WabJson.number(looksAt, "at", 0))).append(" 格。");
            }
        } else {
            sb.append("视线尽头没有挡住她的东西。");
        }
        if (blocks.size() > 0) {
            sb.append("视野里还能看出：");
            int shown = Math.min(blocks.size(), 8);
            for (int i = 0; i < shown; i++) {
                JsonObject item = blocks.get(i).getAsJsonObject();
                if (i > 0) sb.append("、");
                sb.append(WabJson.str(item, "what", "方块")).append(" ").append(shortId(WabJson.str(item, "block", "?")));
                if (item.has("for")) {
                    sb.append(WabJson.str(item, "for").equals("maid") ? "（她的床）" : "（普通床）");
                }
                sb.append("，").append(roundText(WabJson.number(item, "distance", 0))).append(" 格");
            }
            if (blocks.size() > shown) sb.append("，一共 ").append(blocks.size()).append(" 处");
            sb.append("。");
        }
        return sb.length() > 700 ? sb.substring(0, 700) : sb.toString();
    }

    private static String nameOf(JsonObject item) {
        String kind = WabJson.str(item, "kind", "");
        String name = WabJson.str(item, "name", "东西");
        if (kind.equals("player")) return "玩家 " + name;
        if (kind.equals("maid")) return "女仆 " + name;
        if (kind.equals("hostile")) return name;
        return name;
    }

    private static String weatherName(String weather) {
        return switch (weather) {
            case "rain" -> "下着雨";
            case "thunder" -> "雷雨交加";
            default -> "天气晴";
        };
    }

    private static String roundText(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    /** minecraft:iron_ore → 铁矿石之外的裸 id 太长了，AI 读起来费眼；只留路径段。 */
    private static String shortId(String id) {
        int i = id.lastIndexOf(':');
        String bare = i >= 0 ? id.substring(i + 1) : id;
        return bare.isEmpty() ? "方块" : bare;
    }

    private static String facingName(Direction direction) {
        return switch (direction) {
            case NORTH -> "北";
            case SOUTH -> "南";
            case EAST -> "东";
            case WEST -> "西";
            case UP -> "上";
            case DOWN -> "下";
        };
    }

    /** 绕 Y 的叉积分量：正=在她右手边，负=左手边。面朝南时 +X（东）在左手，验算过。 */
    private static String sideLabel(Vec3 forward, Vec3 dir) {
        double cross = forward.x * dir.z - forward.z * dir.x;
        if (cross < -0.08) return "偏左";
        if (cross > 0.08) return "偏右";
        return "正前方";
    }

    private static JsonObject blockAt(ServerLevel level, BlockPos pos) {
        JsonObject out = new JsonObject();
        if (!level.isLoaded(pos)) {
            out.addProperty("block", "");
            return out;
        }
        out.add("pos", blockPos(pos));
        out.addProperty("block", blockId(level, pos));
        return out;
    }

    private static JsonArray vec(Vec3 value) {
        JsonArray out = new JsonArray();
        out.add(round(value.x));
        out.add(round(value.y));
        out.add(round(value.z));
        return out;
    }

    public static JsonArray entities(EntityMaid maid, int radius) {
        ServerLevel level = (ServerLevel) maid.level();
        AABB box = maid.getBoundingBox().inflate(radius);
        List<Entity> found = new ArrayList<>(level.getEntities(maid, box,
                entity -> entity != maid && entity.isAlive() && entity instanceof LivingEntity));
        found.sort(Comparator.comparingDouble(maid::distanceToSqr));
        JsonArray out = new JsonArray();
        for (Entity entity : found) {
            if (out.size() >= 48) break;
            LivingEntity living = (LivingEntity) entity;
            JsonObject item = new JsonObject();
            item.addProperty("id", entity.getUUID().toString());
            item.addProperty("type", entityType(entity));
            item.addProperty("kind", kind(entity));
            item.addProperty("name", entity.getName().getString());
            item.addProperty("distance", round(Math.sqrt(maid.distanceToSqr(entity))));
            item.add("pos", vecPos(entity));
            item.addProperty("health", round(living.getHealth()));
            item.addProperty("maxHealth", round(living.getMaxHealth()));
            if (entity instanceof ServerPlayer player) {
                item.addProperty("player", player.getGameProfile().getName());
                item.addProperty("creative", player.isCreative());
            }
            out.add(item);
        }
        return out;
    }

    private static JsonObject blocks(ServerLevel level, EntityMaid maid, int radius) {
        JsonObject out = new JsonObject();
        JsonArray ores = new JsonArray();
        JsonArray containers = new JsonArray();
        JsonArray crops = new JsonArray();
        JsonArray beds = new JsonArray();
        BlockPos origin = maid.blockPosition();
        List<FoundBlock> found = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -radius, -radius),
                origin.offset(radius, radius, radius))) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) continue;
            boolean ore = isOre(state);
            boolean container = state.hasBlockEntity() && level.getBlockEntity(pos) instanceof Container;
            boolean crop = isMatureCrop(state);
            boolean bed = isBedLike(state) && state.hasProperty(PART) && state.getValue(PART) == BedPart.HEAD;
            if (!ore && !container && !crop && !bed) continue;
            found.add(new FoundBlock(pos.immutable(), maid.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
                    ore, container, crop, bed));
        }
        found.sort(Comparator.comparingDouble(FoundBlock::distance));
        for (FoundBlock block : found) {
            if (block.ore && ores.size() < 48) {
                JsonObject item = blockJson(block.pos, maid);
                item.addProperty("block", blockId(level, block.pos));
                ores.add(item);
            }
            if (block.container && containers.size() < 24) {
                JsonObject item = blockJson(block.pos, maid);
                item.addProperty("block", blockId(level, block.pos));
                containers.add(item);
            }
            if (block.crop && crops.size() < 32) {
                JsonObject item = blockJson(block.pos, maid);
                item.addProperty("block", blockId(level, block.pos));
                crops.add(item);
            }
            // 床单独一档：她要在「睡自己的床 / 睡主人的床 / 直接躺地上」之间做决定，得先看得见选项。
            if (block.bed && beds.size() < 12) {
                JsonObject item = blockJson(block.pos, maid);
                BlockState state = level.getBlockState(block.pos);
                item.addProperty("block", blockId(level, block.pos));
                item.addProperty("for", isMaidBed(state) ? "maid" : "vanilla");
                item.addProperty("occupied", state.getValue(OCCUPIED));
                beds.add(item);
            }
        }
        out.add("ores", ores);
        out.add("containers", containers);
        out.add("crops", crops);
        out.add("beds", beds);
        return out;
    }

    private static JsonArray fluids(ServerLevel level, EntityMaid maid, int radius) {
        JsonArray out = new JsonArray();
        BlockPos origin = maid.blockPosition();
        List<FoundBlock> found = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -radius, -radius),
                origin.offset(radius, radius, radius))) {
            BlockState state = level.getBlockState(pos);
            if (!state.getFluidState().isSource()) continue;
            if (!state.getFluidState().is(FluidTags.WATER) && !state.getFluidState().is(FluidTags.LAVA)) continue;
            found.add(new FoundBlock(pos.immutable(),
                    maid.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), false, false, false, false));
        }
        found.sort(Comparator.comparingDouble(FoundBlock::distance));
        for (FoundBlock block : found) {
            if (out.size() >= 32) break;
            BlockState state = level.getBlockState(block.pos);
            JsonObject item = blockJson(block.pos, maid);
            item.addProperty("fluid", state.getFluidState().is(FluidTags.LAVA) ? "lava" : "water");
            out.add(item);
        }
        return out;
    }

    private static JsonObject light(ServerLevel level, EntityMaid maid) {
        BlockPos pos = maid.blockPosition();
        JsonObject out = new JsonObject();
        out.addProperty("block", level.getBrightness(LightLayer.BLOCK, pos));
        out.addProperty("sky", level.getBrightness(LightLayer.SKY, pos));
        out.addProperty("maxLocal", level.getMaxLocalRawBrightness(pos));
        out.addProperty("canSeeSky", level.canSeeSky(pos));
        return out;
    }

    private static JsonArray itemEntities(EntityMaid maid, int radius) {
        JsonArray out = new JsonArray();
        List<ItemEntity> items = new ArrayList<>(maid.level().getEntitiesOfClass(ItemEntity.class,
                maid.getBoundingBox().inflate(radius), ItemEntity::isAlive));
        items.sort(Comparator.comparingDouble(maid::distanceToSqr));
        for (ItemEntity entity : items) {
            if (out.size() >= 24) break;
            JsonObject item = new JsonObject();
            item.addProperty("id", entity.getUUID().toString());
            item.addProperty("item", itemId(entity.getItem()));
            item.addProperty("name", entity.getItem().getHoverName().getString());
            item.addProperty("count", entity.getItem().getCount());
            item.addProperty("distance", round(Math.sqrt(maid.distanceToSqr(entity))));
            item.add("pos", vecPos(entity));
            out.add(item);
        }
        return out;
    }

    public static JsonArray inventory(EntityMaid maid, int limit) {
        JsonArray out = new JsonArray();
        var inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots() && out.size() < limit; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            JsonObject item = item(stack);
            item.addProperty("slot", i);
            out.add(item);
        }
        return out;
    }

    private static JsonArray armor(EntityMaid maid) {
        JsonArray out = new JsonArray();
        // 直接点这四个：1.21.1 把 EquipmentSlot.Type 的 ARMOR 拆成了人形/动物两种，
        // 名字跟 1.20 不同；写死槽位既避开改名，也和「她身上穿的」这个语义一致。
        for (net.minecraft.world.entity.EquipmentSlot slot : new net.minecraft.world.entity.EquipmentSlot[]{
                net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.CHEST,
                net.minecraft.world.entity.EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.FEET}) {
            ItemStack stack = maid.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            JsonObject item = item(stack);
            item.addProperty("slot", slot.getName());
            out.add(item);
        }
        return out;
    }

    private static JsonArray players(EntityMaid maid, int radius) {
        JsonArray out = new JsonArray();
        for (Player player : maid.level().getEntitiesOfClass(Player.class, maid.getBoundingBox().inflate(radius))) {
            JsonObject item = new JsonObject();
            item.addProperty("name", player.getName().getString());
            item.addProperty("distance", round(Math.sqrt(maid.distanceToSqr(player))));
            item.addProperty("health", round(player.getHealth()));
            item.addProperty("food", player.getFoodData().getFoodLevel());
            item.addProperty("creative", player.isCreative());
            out.add(item);
        }
        return out;
    }

    private static JsonObject schedulePos(EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.add("work", blockPos(maid.getSchedulePos().getWorkPos()));
        out.add("idle", blockPos(maid.getSchedulePos().getIdlePos()));
        out.add("sleep", blockPos(maid.getSchedulePos().getSleepPos()));
        out.addProperty("configured", maid.getSchedulePos().isConfigured());
        out.addProperty("dimension", maid.getSchedulePos().getDimension().toString());
        return out;
    }

    public static JsonObject loveLoathe(EntityMaid maid) {
        CompoundTag root = maid.getPersistentData();
        if (!root.contains("MaidEmotions") && !root.contains("MaidHunger")) return null;
        CompoundTag emotions = root.getCompound("MaidEmotions");
        UUID ownerId = maid.getOwnerUUID();
        CompoundTag mine = ownerId == null ? new CompoundTag() : emotions.getCompound(ownerId.toString());
        int trust = mine.contains("trust") ? mine.getInt("trust") : 40;
        int fear = mine.contains("fear") ? mine.getInt("fear") : 10;
        float hunger = root.contains("MaidHunger") ? root.getFloat("MaidHunger") : 50.0f;
        JsonObject out = new JsonObject();
        out.addProperty("trust", trust);
        out.addProperty("fear", fear);
        out.addProperty("hunger", round(hunger));
        out.addProperty("tendency", tendency(trust, fear));
        return out;
    }

    public static String tendency(int trust, int fear) {
        if (trust >= 70 && fear <= 20) return "BOND";
        if (trust >= 40 && fear <= 30) return "FRIENDLY";
        if (trust <= 20 && fear >= 70) return "TERRIFIED";
        if (trust <= 30 && fear <= 20) return "STRANGER";
        if (trust <= 40 && fear >= 50) return "FEARFUL";
        if (trust >= 60 && fear >= 60) return "CONFLICTED";
        return "NEUTRAL";
    }

    public static JsonObject item(ItemStack stack) {
        JsonObject out = new JsonObject();
        if (stack.isEmpty()) return out;
        out.addProperty("item", itemId(stack));
        out.addProperty("name", stack.getHoverName().getString());
        out.addProperty("count", stack.getCount());
        if (stack.isDamageableItem()) {
            out.addProperty("damage", stack.getDamageValue());
            out.addProperty("maxDamage", stack.getMaxDamage());
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            out.addProperty("block", BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).toString());
        }
        return out;
    }

    public static String itemId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    public static String blockId(ServerLevel level, BlockPos pos) {
        return BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock()).toString();
    }

    public static String entityType(Entity entity) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
    }

    public static String kind(Entity entity) {
        // 另一只女仆既不是 player 也不是 animal：不单独分出来，AI 眼里她就是一团「别的活物」。
        if (entity instanceof EntityMaid) return "maid";
        if (entity instanceof Player) return "player";
        if (entity instanceof Monster) return "hostile";
        if (entity instanceof Animal) return "animal";
        return "other";
    }

    /** 床：原版床、别的模组装进 minecraft:beds 标签的床，和女仆自己的床，都算。 */
    public static boolean isBedLike(BlockState state) {
        return state.is(BlockTags.BEDS) || isMaidBed(state);
    }

    public static boolean isMaidBed(BlockState state) {
        return state.is(InitBlocks.MAID_BED.get());
    }

    /**
     * 躺下统一躺床头那一格：方块实体、颜色和 MaidBedTask 查的 OCCUPIED 都挂在头上，
     * 睡到脚那格会让她渲染在床尾，也让「这张床有没有人」的判断对不上。
     * AI 给的坐标、日程里的睡位都可能是任一半，所以过一遍再躺。
     */
    public static BlockPos bedHead(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(PART)) return pos;
        return state.getValue(PART) == BedPart.HEAD ? pos
                : pos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public static boolean isOre(BlockState state) {
        return state.is(BlockTags.COAL_ORES) || state.is(BlockTags.IRON_ORES) || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.REDSTONE_ORES) || state.is(BlockTags.LAPIS_ORES)
                || state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.EMERALD_ORES)
                || state.is(Blocks.ANCIENT_DEBRIS) || state.is(Blocks.NETHER_QUARTZ_ORE);
    }

    public static boolean isMatureCrop(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) return crop.isMaxAge(state);
        if (state.is(Blocks.NETHER_WART)) return age(state) >= 3;
        if (state.is(Blocks.SWEET_BERRY_BUSH)) return age(state) >= 3;
        return false;
    }

    private static int age(BlockState state) {
        for (var property : state.getProperties()) {
            if (property instanceof IntegerProperty integer && property.getName().equals("age")) {
                return state.getValue(integer);
            }
        }
        return -1;
    }

    public static JsonArray blockPos(BlockPos pos) {
        JsonArray out = new JsonArray();
        out.add(pos.getX());
        out.add(pos.getY());
        out.add(pos.getZ());
        return out;
    }

    public static JsonArray vecPos(Entity entity) {
        JsonArray out = new JsonArray();
        out.add(round(entity.getX()));
        out.add(round(entity.getY()));
        out.add(round(entity.getZ()));
        return out;
    }

    public static JsonObject blockJson(BlockPos pos, EntityMaid maid) {
        JsonObject out = new JsonObject();
        out.add("pos", blockPos(pos));
        out.addProperty("distance", round(Math.sqrt(maid.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5))));
        return out;
    }

    public static String ownerName(EntityMaid maid) {
        if (maid.getOwner() != null) return maid.getOwner().getName().getString();
        return "";
    }

    private static String taskName(EntityMaid maid) {
        String id = maid.getTask().getUid().toString();
        Component name = Component.translatable("task." + maid.getTask().getUid().getNamespace() + "." + maid.getTask().getUid().getPath());
        return id + "|" + name.getString();
    }

    public static double round(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return 0;
        return Math.round(value * 10.0) / 10.0;
    }

    private record FoundBlock(BlockPos pos, double distance, boolean ore, boolean container, boolean crop, boolean bed) {
    }
}
