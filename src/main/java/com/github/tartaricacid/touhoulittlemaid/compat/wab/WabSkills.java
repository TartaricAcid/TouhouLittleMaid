package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * WAB 的技能扩展：记路、翻箱子。
 *
 * <p>放在单独一个类里，是因为 {@link WabActions} 已经只管「动作原语」——
 * 这里开始出现「有前后状态」的能力：地点要落盘、容器要按槽位搬运。
 *
 * <p>两条铁律和其它动作一致：
 * <ul>
 *   <li>只碰主人名下、活着、在当前维度里的女仆；</li>
 *   <li>够不着就先说清楚，不隔空取物。</li>
 * </ul>
 */
final class WabSkills {
    /** 地点表存在女仆自己的 persistentData 里：跟着存档走，换机也带得走。 */
    private static final String WAYPOINT_KEY = "WabWaypoints";
    /** 翻箱子只认这个距离内的容器；再远就得先走过去。 */
    private static final double CONTAINER_REACH = 6.0;
    private static final double CONTAINER_SEARCH = 8.0;

    private WabSkills() {
    }

    // ==================== 地点记忆 ====================

    /**
     * waypoint：set / list / go / remove。
     * 名字不做本地化，AI 和玩家用同一个键，省得「仓库」和「warehouse」互相找不到。
     */
    static JsonObject waypoint(WabBridge bridge, EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "list";
        String name = WabJson.str(p, "name").trim();
        JsonObject book = loadWaypoints(maid);

        return switch (action) {
            case "set" -> {
                if (name.isEmpty()) {
                    yield fail("no_name", "得给这个地点起个名字，比如「仓库」「矿洞」。");
                }
                JsonArray arr = WabJson.arr(p, "pos");
                BlockPos pos = arr.size() >= 3 ? blockPosOf(arr) : maid.blockPosition();
                if (pos == null) {
                    yield fail("bad_pos", "坐标不完整。");
                }
                JsonObject point = new JsonObject();
                point.addProperty("x", pos.getX());
                point.addProperty("y", pos.getY());
                point.addProperty("z", pos.getZ());
                point.addProperty("dim", maid.level().dimension().location().toString());
                point.addProperty("at", System.currentTimeMillis());
                boolean replaced = book.has(name);
                book.add(name, point);
                saveWaypoints(maid, book);
                yield ok(maid, (replaced ? "已经把「" : "记住了「") + name + "」："
                        + pos.getX() + " " + pos.getY() + " " + pos.getZ());
            }
            case "list" -> {
                JsonArray items = new JsonArray();
                String here = maid.level().dimension().location().toString();
                List<Map.Entry<String, JsonElement>> sorted = new ArrayList<>(book.entrySet());
                sorted.sort(Map.Entry.comparingByKey());
                for (Map.Entry<String, JsonElement> entry : sorted) {
                    if (!entry.getValue().isJsonObject()) continue;
                    JsonObject point = entry.getValue().getAsJsonObject();
                    JsonObject one = new JsonObject();
                    one.addProperty("name", entry.getKey());
                    one.addProperty("x", WabJson.integer(point, "x", 0));
                    one.addProperty("y", WabJson.integer(point, "y", 0));
                    one.addProperty("z", WabJson.integer(point, "z", 0));
                    String dim = WabJson.str(point, "dim");
                    one.addProperty("dim", dim);
                    one.addProperty("sameDimension", dim.equals(here));
                    if (dim.equals(here)) {
                        one.addProperty("distance", Math.round(maid.distanceToSqr(
                                WabJson.integer(point, "x", 0) + 0.5,
                                WabJson.integer(point, "y", 0) + 0.5,
                                WabJson.integer(point, "z", 0) + 0.5)));
                    }
                    items.add(one);
                }
                JsonObject out = ok(maid, items.size() == 0 ? "她还不认识任何地点。" : "记得 " + items.size() + " 个地点。");
                out.add("waypoints", items);
                yield out;
            }
            case "go" -> {
                if (name.isEmpty()) {
                    yield fail("no_name", "要去哪个地点？");
                }
                JsonObject point = pointOf(book, name);
                if (point == null) {
                    yield fail("unknown_waypoint", "她不知道「" + name + "」在哪。先让她 set 一下。");
                }
                String here = maid.level().dimension().location().toString();
                if (!WabJson.str(point, "dim").equals(here)) {
                    yield fail("other_dimension", "「" + name + "」在另一个维度，她走不过去。");
                }
                JsonObject goParams = new JsonObject();
                goParams.addProperty("mode", "to");
                JsonArray target = new JsonArray();
                target.add(WabJson.integer(point, "x", 0) + 0.5);
                target.add(WabJson.integer(point, "y", 0));
                target.add(WabJson.integer(point, "z", 0) + 0.5);
                goParams.add("pos", target);
                JsonObject result = WabActions.go(bridge, maid, goParams);
                if (result.has("ok") && !WabJson.bool(result, "ok", true)) {
                    yield result;
                }
                yield ok(maid, "往「" + name + "」那边去了。");
            }
            case "remove" -> {
                if (name.isEmpty()) {
                    yield fail("no_name", "要删哪个地点？");
                }
                if (!book.has(name)) {
                    yield fail("unknown_waypoint", "她本来就不认识「" + name + "」。");
                }
                book.remove(name);
                saveWaypoints(maid, book);
                yield ok(maid, "已经忘掉「" + name + "」了。");
            }
            default -> fail("unknown_action", "地点只支持 set / list / go / remove。");
        };
    }

    private static JsonObject loadWaypoints(EntityMaid maid) {
        CompoundTag tag = maid.getPersistentData();
        String raw = tag.getString(WAYPOINT_KEY);
        if (raw == null || raw.isEmpty()) return new JsonObject();
        try {
            JsonElement parsed = JsonParser.parseString(raw);
            return parsed.isJsonObject() ? parsed.getAsJsonObject() : new JsonObject();
        } catch (Exception e) {
            // 坏掉的表就当没有：宁可丢地点，也不能让她因为一行脏数据再也记不住路。
            return new JsonObject();
        }
    }

    private static void saveWaypoints(EntityMaid maid, JsonObject book) {
        maid.getPersistentData().putString(WAYPOINT_KEY, book.toString());
    }

    private static JsonObject pointOf(JsonObject book, String name) {
        JsonElement el = book.get(name);
        return el != null && el.isJsonObject() ? el.getAsJsonObject() : null;
    }

    // ==================== 容器存取 ====================

    /**
     * container：list / store / take / dump。
     * 只认 {@link Container}（箱子、木桶、漏斗、熔炉都算），够不着就让她先走过去。
     */
    static JsonObject container(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "list";
        BlockPos pos = resolveContainerPos(maid, p);
        if (pos == null) {
            return fail("no_container", "附近 " + (int) CONTAINER_SEARCH + " 格里没有能打开的容器。");
        }
        if (!(maid.level() instanceof ServerLevel level) || !level.isLoaded(pos)) {
            return fail("not_loaded", "那个位置的区块还没加载。");
        }
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof Container box)) {
            return fail("not_container", "那个方块不是容器。");
        }
        double distance = maid.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (distance > CONTAINER_REACH * CONTAINER_REACH) {
            maid.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.6);
            return fail("too_far", "离容器还有 " + Math.round(Math.sqrt(distance)) + " 格，她正在走过去；到了再试一次。");
        }

        IItemHandler inv = maid.getAvailableInv(false);
        String filter = WabJson.str(p, "filter");
        if (filter.isEmpty()) filter = WabJson.str(p, "item");
        int limit = Math.max(1, Math.min(512, WabJson.integer(p, "count", 64)));

        return switch (action) {
            case "list" -> {
                List<ItemStack> contents = new ArrayList<>();
                for (int i = 0; i < box.getContainerSize(); i++) {
                    ItemStack stack = box.getItem(i);
                    if (!stack.isEmpty()) contents.add(stack);
                }
                JsonArray items = summarize(contents);
                JsonObject out = ok(maid, contents.isEmpty() ? "这个容器是空的。"
                        : "里面装着 " + contents.size() + " 组东西。");
                out.add("items", items);
                out.addProperty("slots", box.getContainerSize());
                yield out;
            }
            case "store" -> {
                int moved = storeInto(box, inv, filter, limit);
                yield moved == 0
                        ? fail("nothing_stored", filter.isEmpty() ? "背包里没有能放进容器的东西。"
                        : "背包里没有符合「" + filter + "」的东西。")
                        : ok(maid, "往容器里放了 " + moved + " 件。");
            }
            case "dump" -> {
                int moved = storeInto(box, inv, "", 512);
                yield moved == 0
                        ? fail("nothing_stored", "背包里没有可收起来的东西（工具和食物她会留着）。")
                        : ok(maid, "把 " + moved + " 件东西收进容器了。");
            }
            case "take" -> {
                int moved = takeFrom(box, inv, filter, limit);
                yield moved == 0
                        ? fail("nothing_taken", filter.isEmpty() ? "容器里没有能拿的东西。"
                        : "容器里没有「" + filter + "」。")
                        : ok(maid, "从容器里拿了 " + moved + " 件。");
            }
            default -> fail("unknown_action", "容器只支持 list / store / take / dump。");
        };
    }

    private static BlockPos resolveContainerPos(EntityMaid maid, JsonObject p) {
        JsonArray arr = WabJson.arr(p, "pos");
        if (arr.size() >= 3) {
            BlockPos pos = blockPosOf(arr);
            if (pos == null) return null;
            // 显式给了坐标就按坐标来：她可以走过去，不能被搜索半径挡住。
            return pos;
        }
        int r = (int) Math.min(16, Math.max(1, WabJson.number(p, "radius", CONTAINER_SEARCH)));
        BlockPos origin = maid.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (BlockPos cursor : BlockPos.betweenClosed(origin.offset(-r, -4, -r), origin.offset(r, 4, r))) {
            if (!(maid.level().getBlockEntity(cursor) instanceof Container)) continue;
            double d = maid.distanceToSqr(cursor.getX() + 0.5, cursor.getY() + 0.5, cursor.getZ() + 0.5);
            if (d < bestDistance) {
                bestDistance = d;
                best = cursor.immutable();
            }
        }
        return best;
    }

    /** 从她的背包往容器里搬；dump 时跳过工具、装备和食物（那些是她的家当）。 */
    private static int storeInto(Container box, IItemHandler inv, String filter, int limit) {
        int moved = 0;
        boolean dump = filter == null || filter.isEmpty();
        for (int slot = 0; slot < inv.getSlots() && moved < limit; slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.isEmpty()) continue;
            if (dump && keepInBag(stack)) continue;
            if (!dump && !matches(stack, filter)) continue;
            int want = Math.min(stack.getCount(), limit - moved);
            ItemStack probe = stack.copy();
            probe.setCount(want);
            int left = insertIntoContainer(box, probe);
            int done = want - left;
            if (done <= 0) continue;
            inv.extractItem(slot, done, false);
            moved += done;
        }
        if (moved > 0) box.setChanged();
        return moved;
    }

    private static int takeFrom(Container box, IItemHandler inv, String filter, int limit) {
        int moved = 0;
        for (int slot = 0; slot < box.getContainerSize() && moved < limit; slot++) {
            ItemStack stack = box.getItem(slot);
            if (stack.isEmpty()) continue;
            if (!filter.isEmpty() && !matches(stack, filter)) continue;
            int want = Math.min(stack.getCount(), limit - moved);
            ItemStack probe = stack.copy();
            probe.setCount(want);
            ItemStack left = ItemHandlerHelper.insertItemStacked(inv, probe, false);
            int done = want - left.getCount();
            if (done <= 0) continue;
            stack.shrink(done);
            box.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
            moved += done;
        }
        if (moved > 0) box.setChanged();
        return moved;
    }

    private static int insertIntoContainer(Container box, ItemStack stack) {
        int remaining = stack.getCount();
        int max = Math.min(box.getMaxStackSize(), stack.getMaxStackSize());
        for (int slot = 0; slot < box.getContainerSize() && remaining > 0; slot++) {
            ItemStack existing = box.getItem(slot);
            if (existing.isEmpty()) {
                int put = Math.min(remaining, max);
                ItemStack copy = stack.copy();
                copy.setCount(put);
                box.setItem(slot, copy);
                remaining -= put;
            } else if (ItemStack.isSameItemSameTags(existing, stack)) {
                int space = max - existing.getCount();
                if (space <= 0) continue;
                int put = Math.min(space, remaining);
                existing.grow(put);
                box.setItem(slot, existing);
                remaining -= put;
            }
        }
        return remaining;
    }

    /** 工具/武器/护甲（有耐久）和食物留在身上，别一 dump 连镐子都收进箱子。 */
    private static boolean keepInBag(ItemStack stack) {
        if (stack.isDamageableItem()) return true;
        return stack.getItem().getFoodProperties(stack, null) != null;
    }

    private static boolean matches(ItemStack stack, String filter) {
        if (filter == null || filter.isEmpty()) return true;
        String f = filter.trim().toLowerCase(Locale.ROOT);
        if (f.equals("food")) return stack.getItem().getFoodProperties(stack, null) != null;
        if (f.equals("block")) return stack.getItem() instanceof BlockItem;
        if (f.startsWith("#")) {
            var tag = stack.getTags().filter(t -> ("#" + t.location()).equalsIgnoreCase(f)).findFirst();
            return tag.isPresent();
        }
        var key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key != null && key.toString().equalsIgnoreCase(f);
    }

    private static JsonArray summarize(List<ItemStack> contents) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, String> names = new LinkedHashMap<>();
        for (ItemStack stack : contents) {
            var key = ForgeRegistries.ITEMS.getKey(stack.getItem());
            String id = key == null ? "unknown" : key.toString();
            counts.merge(id, stack.getCount(), Integer::sum);
            names.putIfAbsent(id, stack.getHoverName().getString());
        }
        JsonArray items = new JsonArray();
        int n = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (n++ >= 40) break;
            JsonObject one = new JsonObject();
            one.addProperty("item", entry.getKey());
            one.addProperty("name", names.getOrDefault(entry.getKey(), entry.getKey()));
            one.addProperty("count", entry.getValue());
            items.add(one);
        }
        return items;
    }

    private static BlockPos blockPosOf(JsonArray arr) {
        if (arr.size() < 3) return null;
        try {
            return new BlockPos(
                    (int) Math.floor(arr.get(0).getAsDouble()),
                    (int) Math.floor(arr.get(1).getAsDouble()),
                    (int) Math.floor(arr.get(2).getAsDouble()));
        } catch (Exception e) {
            return null;
        }
    }

    private static JsonObject fail(String code, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", false);
        out.addProperty("code", code);
        out.addProperty("error", message);
        return out;
    }

    private static JsonObject ok(EntityMaid maid, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("maid", maid.getUUID().toString());
        out.addProperty("message", message);
        return out;
    }
}
