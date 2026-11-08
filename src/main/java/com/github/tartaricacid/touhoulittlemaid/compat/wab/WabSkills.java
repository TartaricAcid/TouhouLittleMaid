package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * WAB 的技能扩展：记路、翻箱子、合成冶炼、建造、骑乘、战斗、分工。
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
            } else if (ItemStack.isSameItemSameComponents(existing, stack)) {
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
            ResourceLocation tagId = ResourceLocation.tryParse(f.substring(1));
            if (tagId == null) return false;
            return stack.is(TagKey.create(Registries.ITEM, tagId));
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key != null && key.toString().equalsIgnoreCase(f);
    }

    private static JsonArray summarize(List<ItemStack> contents) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, String> names = new LinkedHashMap<>();
        for (ItemStack stack : contents) {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
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

    // ==================== 多女仆分工 ====================

    /**
     * crew：assign / status / stop。
     * 把「谁干什么」摊到附近所有女仆身上：每只各拿一个 Job，天然并行。
     */
    static JsonObject crew(WabBridge bridge, EntityMaid leader, JsonObject p) {
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "assign";
        double radius = Math.max(4, Math.min(64, WabJson.number(p, "radius", 24)));

        List<EntityMaid> team = new ArrayList<>();
        for (EntityMaid maid : bridge.ownedMaids()) {
            if (maid == null || !maid.isAlive()) continue;
            if (maid.level() != leader.level()) continue;
            if (maid.distanceToSqr(leader) > radius * radius) continue;
            team.add(maid);
        }
        if (team.isEmpty()) {
            return fail("no_maid", "附近 " + Math.round(radius) + " 格里没有你名下的女仆。");
        }
        WabDispatcher dispatcher = bridge.dispatcher();

        if (action.equals("stop")) {
            for (EntityMaid maid : team) dispatcher.cancelJob(maid, "主人让停手");
            return ok(leader, "让 " + team.size() + " 只女仆停手了。");
        }
        if (action.equals("status")) {
            JsonArray arr = new JsonArray();
            for (EntityMaid maid : team) {
                JsonObject one = new JsonObject();
                one.addProperty("maid", maid.getName().getString());
                one.addProperty("busy", dispatcher.hasJob(maid));
                one.addProperty("health", Math.round(maid.getHealth()));
                one.addProperty("x", maid.blockPosition().getX());
                one.addProperty("y", maid.blockPosition().getY());
                one.addProperty("z", maid.blockPosition().getZ());
                arr.add(one);
            }
            JsonObject out = ok(leader, "队伍里 " + team.size() + " 只女仆。");
            out.add("team", arr);
            return out;
        }

        String role = WabJson.str(p, "role").toLowerCase(Locale.ROOT);
        if (!role.equals("miner") && !role.equals("farmer") && !role.equals("hauler") && !role.equals("guard")) {
            return fail("unknown_role", "分工只支持 miner / farmer / hauler / guard。");
        }
        for (EntityMaid maid : team) {
            dispatcher.cancelJob(maid, "分工调整");
            switch (role) {
                case "miner" -> {
                    JsonObject params = new JsonObject();
                    params.addProperty("action", "mine");
                    params.addProperty("filter", "ores");
                    params.addProperty("count", 32);
                    params.addProperty("radius", 16);
                    dispatcher.noteJob(WabActions.startGather(bridge, maid, params, 4000, true, quiet()));
                }
                case "farmer" -> {
                    JsonObject params = new JsonObject();
                    params.addProperty("action", "harvest");
                    params.addProperty("count", 32);
                    params.addProperty("radius", 16);
                    dispatcher.noteJob(WabActions.startGather(bridge, maid, params, 4000, true, quiet()));
                }
                case "hauler" -> {
                    JsonObject collect = new JsonObject();
                    collect.addProperty("action", "collect");
                    collect.addProperty("count", 32);
                    collect.addProperty("radius", 8);
                    WabActions.collect(bridge, maid, collect);
                    JsonObject dump = new JsonObject();
                    dump.addProperty("action", "dump");
                    dump.addProperty("radius", 8);
                    container(maid, dump);
                }
                default -> {
                    JsonObject guard = new JsonObject();
                    guard.addProperty("action", "guard");
                    guard.addProperty("radius", 12);
                    fight(maid, guard);
                }
            }
        }
        return ok(leader, "给 " + team.size() + " 只女仆派了「" + role + "」。");
    }

    private static WabActions.Responder quiet() {
        return new WabActions.Responder() {
            @Override
            public void success(JsonObject data) {
            }

            @Override
            public void failure(String code, String message) {
            }
        };
    }

    // ==================== 合成与冶炼 ====================

    /**
     * craft：craft / smelt / list。
     * 只用原版配方表，材料按「背包里真的有」逐条扣；做不出来就说缺什么，不猜。
     */
    static JsonObject craft(EntityMaid maid, JsonObject p) {
        if (!(maid.level() instanceof ServerLevel level)) {
            return fail("bad_level", "这个维度做不了。");
        }
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "craft";
        String itemId = normalizeId(WabJson.str(p, "item"));
        int count = Math.max(1, Math.min(64, WabJson.integer(p, "count", 1)));
        IItemHandler inv = maid.getAvailableInv(false);

        return switch (action) {
            case "list" -> listCraftable(level, inv);
            case "craft" -> doCraft(level, maid, inv, itemId, count, p);
            case "smelt" -> doSmelt(level, maid, inv, itemId, count);
            default -> fail("unknown_action", "合成只支持 craft / smelt / list。");
        };
    }

    private static JsonObject listCraftable(ServerLevel level, IItemHandler inv) {
        JsonArray items = new JsonArray();
        int n = 0;
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            if (n >= 24) break;
            CraftingRecipe recipe = holder.value();
            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (result.isEmpty()) continue;
            if (!canConsume(inv, recipe.getIngredients())) continue;
            JsonObject one = new JsonObject();
            one.addProperty("item", idOf(result));
            one.addProperty("name", result.getHoverName().getString());
            one.addProperty("count", result.getCount());
            items.add(one);
            n++;
        }
        JsonObject out = ok(null, items.size() == 0 ? "背包里的材料什么都做不了。" : "现在能做 " + items.size() + " 样东西。");
        out.add("craftable", items);
        return out;
    }

    private static JsonObject doCraft(ServerLevel level, EntityMaid maid, IItemHandler inv,
                                      String itemId, int count, JsonObject p) {
        if (itemId.isEmpty()) {
            return fail("no_item", "要做什么？给个产物 id，比如 minecraft:stick。");
        }
        CraftingRecipe recipe = null;
        boolean sawRecipe = false;
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe candidate = holder.value();
            ItemStack result = candidate.getResultItem(level.registryAccess());
            if (result.isEmpty() || !idOf(result).equals(itemId)) continue;
            sawRecipe = true;
            if (!hasWorkstation(level, maid, candidate, p)) continue;
            if (!canConsume(inv, candidate.getIngredients())) continue;
            recipe = candidate;
            break;
        }
        if (recipe == null) {
            return sawRecipe
                    ? fail("missing_materials", "配方有，但材料不够，或者附近没有工作台。")
                    : fail("no_recipe", "没有「" + itemId + "」的合成配方。");
        }

        int crafted = 0;
        for (int i = 0; i < count; i++) {
            if (!canConsume(inv, recipe.getIngredients())) break;
            consume(inv, recipe.getIngredients());
            ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
            giveToMaid(level, maid, inv, result);
            crafted++;
        }
        if (crafted == 0) {
            return fail("missing_materials", "材料不够，一个都没做出来。");
        }
        return ok(maid, "做了 " + crafted + " 个「" + itemId + "」。");
    }

    private static JsonObject doSmelt(ServerLevel level, EntityMaid maid, IItemHandler inv,
                                      String itemId, int count) {
        if (itemId.isEmpty()) {
            return fail("no_item", "要烧什么？给个产物 id，比如 minecraft:iron_ingot。");
        }
        SmeltingRecipe recipe = null;
        for (RecipeHolder<SmeltingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
            SmeltingRecipe candidate = holder.value();
            ItemStack result = candidate.getResultItem(level.registryAccess());
            if (result.isEmpty() || !idOf(result).equals(itemId)) continue;
            recipe = candidate;
            break;
        }
        if (recipe == null) {
            return fail("no_recipe", "没有「" + itemId + "」的冶炼配方。");
        }
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.isEmpty()) {
            return fail("no_recipe", "这条配方没有原料信息。");
        }
        Ingredient input = ingredients.get(0);

        int smelted = 0;
        int burnTicks = 0;
        while (smelted < count) {
            int inputSlot = findSlot(inv, input);
            if (inputSlot < 0) break;
            if (burnTicks < 200) {
                int fuelSlot = findFuel(inv);
                if (fuelSlot < 0) break;
                int burn = inv.getStackInSlot(fuelSlot).getBurnTime(RecipeType.SMELTING);
                if (burn <= 0) break;
                inv.extractItem(fuelSlot, 1, false);
                burnTicks += burn;
            }
            burnTicks -= 200;
            inv.extractItem(inputSlot, 1, false);
            giveToMaid(level, maid, inv, recipe.getResultItem(level.registryAccess()).copy());
            smelted++;
        }
        if (smelted == 0) {
            return fail("nothing_smelted", "原料或燃料不够，一个都没烧出来。");
        }
        JsonObject out = ok(maid, "烧出 " + smelted + " 个「" + itemId + "」。");
        out.addProperty("smelted", smelted);
        out.addProperty("fuelTicksLeft", burnTicks);
        return out;
    }

    /** 3×3 配方需要有工作台在手边；背包 2×2 能做的就不要求。 */
    private static boolean hasWorkstation(ServerLevel level, EntityMaid maid, CraftingRecipe recipe, JsonObject p) {
        if (recipe.canCraftInDimensions(2, 2)) return true;
        BlockPos given = blockPosOf(WabJson.arr(p, "pos"));
        if (given != null && level.getBlockState(given).getBlock() instanceof CraftingTableBlock) {
            return maid.distanceToSqr(given.getX() + 0.5, given.getY() + 0.5, given.getZ() + 0.5) <= 25;
        }
        BlockPos origin = maid.blockPosition();
        for (BlockPos cursor : BlockPos.betweenClosed(origin.offset(-4, -2, -4), origin.offset(4, 2, 4))) {
            if (level.getBlockState(cursor).getBlock() instanceof CraftingTableBlock) return true;
        }
        return false;
    }

    private static boolean canConsume(IItemHandler inv, List<Ingredient> ingredients) {
        int[] left = new int[inv.getSlots()];
        for (int i = 0; i < left.length; i++) left[i] = inv.getStackInSlot(i).getCount();
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty()) continue;
            boolean took = false;
            for (int i = 0; i < left.length; i++) {
                if (left[i] <= 0) continue;
                if (!ingredient.test(inv.getStackInSlot(i))) continue;
                left[i]--;
                took = true;
                break;
            }
            if (!took) return false;
        }
        return true;
    }

    private static void consume(IItemHandler inv, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient == null || ingredient.isEmpty()) continue;
            int slot = findSlot(inv, ingredient);
            if (slot >= 0) inv.extractItem(slot, 1, false);
        }
    }

    private static int findSlot(IItemHandler inv, Ingredient ingredient) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (ingredient.test(stack)) return i;
        }
        return -1;
    }

    private static int findFuel(IItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getBurnTime(RecipeType.SMELTING) > 0) return i;
        }
        return -1;
    }

    private static void giveToMaid(ServerLevel level, EntityMaid maid, IItemHandler inv, ItemStack stack) {
        ItemStack leftover = ItemHandlerHelper.insertItemStacked(inv, stack, false);
        if (!leftover.isEmpty()) {
            net.minecraft.world.entity.item.ItemEntity drop = new net.minecraft.world.entity.item.ItemEntity(
                    level, maid.getX(), maid.getY() + 0.5, maid.getZ(), leftover);
            level.addFreshEntity(drop);
        }
    }

    // ==================== 建造 ====================

    /**
     * build：line / wall / box / fill / pillar。
     * 一次最多 512 格；没有材料就停在原地，不凭空变方块。
     */
    static JsonObject build(EntityMaid maid, JsonObject p) {
        if (!(maid.level() instanceof ServerLevel level)) {
            return fail("bad_level", "这个维度建不了。");
        }
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "fill";
        BlockPos from = blockPosOf(WabJson.arr(p, "from"));
        if (from == null) {
            return fail("bad_pos", "要给起点坐标 from。");
        }
        JsonArray toArr = WabJson.arr(p, "to");
        BlockPos to = toArr.size() >= 3 ? blockPosOf(toArr) : from;
        if (to == null) {
            return fail("bad_pos", "终点坐标不完整。");
        }
        int height = Math.max(1, Math.min(32, WabJson.integer(p, "height", 1)));
        if (action.equals("wall") || action.equals("pillar")) {
            to = new BlockPos(to.getX(), from.getY(), to.getZ());
        }

        BlockItem blockItem = resolveBlockItem(maid, p);
        if (blockItem == null) {
            return fail("no_block", "没指定方块，手上也不是方块。给个 block 物品 id。");
        }
        if (maid.distanceToSqr(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5) > 32 * 32) {
            return fail("too_far", "起点太远了，先走过去。");
        }

        List<BlockPos> positions = planPositions(action, from, to, height);
        if (positions.isEmpty()) {
            return fail("nothing_to_do", "这个形状算不出要放的方块。");
        }
        boolean replace = WabJson.bool(p, "replace", false);
        IItemHandler inv = maid.getAvailableInv(false);

        int placed = 0;
        int skipped = 0;
        int missing = 0;
        for (BlockPos pos : positions) {
            if (placed >= 512) break;
            if (!level.isLoaded(pos)) {
                skipped++;
                continue;
            }
            BlockState current = level.getBlockState(pos);
            if (current.hasBlockEntity()) {
                skipped++;
                continue;
            }
            if (!current.isAir() && !replace) {
                skipped++;
                continue;
            }
            if (!consumeBlockItem(inv, blockItem)) {
                missing++;
                break;
            }
            level.setBlockAndUpdate(pos, blockItem.getBlock().defaultBlockState());
            placed++;
        }

        if (placed == 0) {
            return fail("nothing_placed", missing > 0 ? "背包里没有这种方块了。" : "这些位置都放不下（被占着或者是容器）。");
        }
        JsonObject out = ok(maid, "放了 " + placed + " 格" + (skipped > 0 ? "，跳过 " + skipped + " 格" : "")
                + (missing > 0 ? "，材料用完" : "") + "。");
        out.addProperty("placed", placed);
        out.addProperty("skipped", skipped);
        out.addProperty("planned", positions.size());
        return out;
    }

    private static List<BlockPos> planPositions(String action, BlockPos from, BlockPos to, int height) {
        List<BlockPos> list = new ArrayList<>();
        switch (action) {
            case "line" -> {
                int dx = Integer.signum(to.getX() - from.getX());
                int dy = Integer.signum(to.getY() - from.getY());
                int dz = Integer.signum(to.getZ() - from.getZ());
                BlockPos cursor = from;
                for (int i = 0; i < 512; i++) {
                    list.add(cursor);
                    if (cursor.equals(to)) break;
                    cursor = cursor.offset(dx, dy, dz);
                }
            }
            case "wall" -> {
                int dx = Integer.signum(to.getX() - from.getX());
                int dz = Integer.signum(to.getZ() - from.getZ());
                BlockPos cursor = from;
                for (int i = 0; i < 512 && list.size() < 512; i++) {
                    for (int h = 0; h < height && list.size() < 512; h++) {
                        list.add(cursor.offset(0, h, 0));
                    }
                    if (cursor.getX() == to.getX() && cursor.getZ() == to.getZ()) break;
                    cursor = cursor.offset(dx, 0, dz);
                }
            }
            case "pillar" -> {
                for (int h = 0; h < height && list.size() < 512; h++) {
                    list.add(from.offset(0, h, 0));
                }
            }
            case "box" -> {
                int top = Math.max(from.getY(), to.getY());
                if (height > 1) top = from.getY() + height - 1;
                for (BlockPos cursor : BlockPos.betweenClosed(
                        new BlockPos(Math.min(from.getX(), to.getX()), from.getY(), Math.min(from.getZ(), to.getZ())),
                        new BlockPos(Math.max(from.getX(), to.getX()), top, Math.max(from.getZ(), to.getZ())))) {
                    boolean edge = cursor.getX() == Math.min(from.getX(), to.getX())
                            || cursor.getX() == Math.max(from.getX(), to.getX())
                            || cursor.getZ() == Math.min(from.getZ(), to.getZ())
                            || cursor.getZ() == Math.max(from.getZ(), to.getZ())
                            || cursor.getY() == from.getY()
                            || cursor.getY() == top;
                    if (edge) list.add(cursor.immutable());
                    if (list.size() >= 512) break;
                }
            }
            default -> {
                for (BlockPos cursor : BlockPos.betweenClosed(
                        new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ())),
                        new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ())))) {
                    list.add(cursor.immutable());
                    if (list.size() >= 512) break;
                }
            }
        }
        return list;
    }

    private static BlockItem resolveBlockItem(EntityMaid maid, JsonObject p) {
        String blockId = normalizeId(WabJson.str(p, "block"));
        if (!blockId.isEmpty()) {
            ResourceLocation key = ResourceLocation.tryParse(blockId);
            if (key != null) {
                Item item = BuiltInRegistries.ITEM.get(key);
                if (item instanceof BlockItem blockItem) return blockItem;
            }
            return null;
        }
        ItemStack hand = maid.getMainHandItem();
        return hand.getItem() instanceof BlockItem blockItem ? blockItem : null;
    }

    private static boolean consumeBlockItem(IItemHandler inv, BlockItem blockItem) {
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() != blockItem) continue;
            inv.extractItem(i, 1, false);
            return true;
        }
        return false;
    }

    // ==================== 骑乘 ====================

    /**
     * ride：mount / boat / cart / dismount。
     * 只骑船、矿车、马这类「本来就是坐骑」的东西，不抢玩家和其它实体的座位。
     */
    static JsonObject ride(EntityMaid maid, JsonObject p) {
        if (!(maid.level() instanceof ServerLevel level)) {
            return fail("bad_level", "这个维度骑不了。");
        }
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "mount";
        return switch (action) {
            case "dismount" -> {
                if (!maid.isPassenger()) {
                    yield fail("not_riding", "她本来就没骑东西。");
                }
                maid.stopRiding();
                yield ok(maid, "下来了。");
            }
            case "boat" -> placeVehicle(maid, level, true);
            case "cart" -> placeVehicle(maid, level, false);
            default -> {
                Entity target = resolveRideable(maid, WabJson.str(p, "target"),
                        Math.max(2, Math.min(24, WabJson.number(p, "radius", 10))));
                if (target == null) {
                    yield fail("no_vehicle", "附近没有能骑的东西。");
                }
                if (maid.isPassenger()) maid.stopRiding();
                boolean mounted = maid.startRiding(target, true);
                yield mounted
                        ? ok(maid, "骑上「" + target.getName().getString() + "」了。")
                        : fail("cannot_ride", "那个东西上不去。");
            }
        };
    }

    private static Entity resolveRideable(EntityMaid maid, String name, double radius) {
        List<Entity> candidates = maid.level().getEntities(maid, maid.getBoundingBox().inflate(radius),
                entity -> entity instanceof Boat || entity instanceof AbstractMinecart || entity instanceof AbstractHorse);
        Entity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : candidates) {
            if (!entity.isAlive()) continue;
            if (!name.isEmpty() && !entity.getName().getString().equalsIgnoreCase(name.trim())) continue;
            double d = maid.distanceToSqr(entity);
            if (d < bestDistance) {
                bestDistance = d;
                best = entity;
            }
        }
        return best;
    }

    private static JsonObject placeVehicle(EntityMaid maid, ServerLevel level, boolean boat) {
        IItemHandler inv = maid.getAvailableInv(false);
        int slot = -1;
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (boat ? stack.getItem() instanceof BoatItem : stack.is(Items.MINECART)) {
                slot = i;
                break;
            }
        }
        if (slot < 0) {
            return fail("no_vehicle_item", boat ? "背包里没有船。" : "背包里没有矿车。");
        }
        Entity vehicle = boat ? EntityType.BOAT.create(level) : EntityType.MINECART.create(level);
        if (vehicle == null) {
            return fail("spawn_failed", "载具放不出来。");
        }
        vehicle.moveTo(maid.getX(), maid.getY(), maid.getZ(), maid.getYRot(), 0);
        level.addFreshEntity(vehicle);
        inv.extractItem(slot, 1, false);
        if (maid.isPassenger()) maid.stopRiding();
        maid.startRiding(vehicle, true);
        return ok(maid, boat ? "放下一条船并坐上去了。" : "放下一辆矿车并坐上去了。");
    }

    // ==================== 战斗策略 ====================

    /**
     * fight：guard / defend / attack / retreat。
     * 换武器按 ATTACK_DAMAGE 挑，不认「剑比斧好」这种写死的偏好。
     */
    static JsonObject fight(EntityMaid maid, JsonObject p) {
        if (!(maid.level() instanceof ServerLevel level)) {
            return fail("bad_level", "这个维度打不了。");
        }
        String action = WabJson.str(p, "action").toLowerCase(Locale.ROOT);
        if (action.isEmpty()) action = "guard";
        double radius = Math.max(2, Math.min(32, WabJson.number(p, "radius", 12)));
        String name = WabJson.str(p, "target");

        return switch (action) {
            case "retreat" -> {
                maid.setTarget(null);
                LivingEntity owner = maid.getOwner();
                if (owner != null && owner.isAlive()) {
                    maid.getNavigation().moveTo(owner, 1.1);
                    yield ok(maid, "先撤到主人身边。");
                }
                JsonObject home = pointOf(loadWaypoints(maid), "家");
                if (home != null && WabJson.str(home, "dim").equals(maid.level().dimension().location().toString())) {
                    maid.getNavigation().moveTo(WabJson.integer(home, "x", 0) + 0.5,
                            WabJson.integer(home, "y", 0), WabJson.integer(home, "z", 0) + 0.5, 1.1);
                    yield ok(maid, "往「家」撤。");
                }
                yield fail("no_retreat_target", "没有主人也没有「家」这个地点，不知道撤去哪。");
            }
            case "defend" -> {
                LivingEntity owner = maid.getOwner();
                LivingEntity attacker = owner == null ? null : owner.getLastHurtByMob();
                if (attacker == null || !attacker.isAlive()) {
                    yield fail("no_attacker", "主人这会儿没被谁打。");
                }
                yield engage(maid, attacker);
            }
            case "attack" -> {
                LivingEntity target = pickTarget(maid, name, radius);
                if (target == null) {
                    yield fail("no_target", name.isEmpty() ? "周围没有可打的敌人。" : "找不到「" + name + "」。");
                }
                yield engage(maid, target);
            }
            default -> {
                LivingEntity target = pickTarget(maid, name, radius);
                if (target == null) {
                    yield ok(maid, "守着呢，周围没有敌人。");
                }
                yield engage(maid, target);
            }
        };
    }

    private static LivingEntity pickTarget(EntityMaid maid, String name, double radius) {
        List<Entity> candidates = maid.level().getEntities(maid, maid.getBoundingBox().inflate(radius),
                entity -> entity instanceof LivingEntity && entity.isAlive());
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : candidates) {
            if (entity == maid) continue;
            if (!name.isEmpty()) {
                if (!entity.getName().getString().equalsIgnoreCase(name.trim())) continue;
            } else if (!(entity instanceof Monster)) {
                continue;
            }
            double d = maid.distanceToSqr(entity);
            if (d < bestDistance) {
                bestDistance = d;
                best = (LivingEntity) entity;
            }
        }
        return best;
    }

    private static JsonObject engage(EntityMaid maid, LivingEntity target) {
        equipBestWeapon(maid);
        maid.setTarget(target);
        maid.getLookControl().setLookAt(target, 30F, 30F);
        if (maid.distanceToSqr(target) > 9) {
            maid.getNavigation().moveTo(target, 1.0);
            return ok(maid, "盯上了「" + target.getName().getString() + "」，正在逼近。");
        }
        boolean hurt = maid.doHurtTarget(target);
        return ok(maid, (hurt ? "打了「" : "没打中「") + target.getName().getString() + "」。");
    }

    /** 从背包挑攻击力最高的那把换到主手；原来手上的东西退回背包。 */
    private static void equipBestWeapon(EntityMaid maid) {
        IItemHandler inv = maid.getAvailableInv(false);
        int bestSlot = -1;
        double bestDamage = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            double damage = attackDamage(inv.getStackInSlot(i));
            if (damage > bestDamage) {
                bestDamage = damage;
                bestSlot = i;
            }
        }
        if (bestSlot < 0) return;
        ItemStack weapon = inv.extractItem(bestSlot, 1, false);
        if (weapon.isEmpty()) return;
        ItemStack previous = maid.getMainHandItem().copy();
        maid.setItemInHand(InteractionHand.MAIN_HAND, weapon);
        if (!previous.isEmpty()) {
            ItemStack leftover = ItemHandlerHelper.insertItemStacked(inv, previous, false);
            if (!leftover.isEmpty()) {
                maid.spawnAtLocation(leftover);
            }
        }
    }

    /** 1.21 没有按槽位取属性修饰符的重载：直接从 ATTRIBUTE_MODIFIERS 组件里筛主手那部分。 */
    private static double attackDamage(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null) return 0;
        double total = 0;
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (!entry.attribute().is(Attributes.ATTACK_DAMAGE)) continue;
            if (!entry.slot().test(EquipmentSlot.MAINHAND)) continue;
            total += entry.modifier().amount();
        }
        return total;
    }

    private static String normalizeId(String raw) {
        String id = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
        if (id.isEmpty()) return "";
        if (!id.contains(":")) id = "minecraft:" + id;
        return id;
    }

    private static String idOf(ItemStack stack) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key == null ? "" : key.toString();
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
        if (maid != null) out.addProperty("maid", maid.getUUID().toString());
        out.addProperty("message", message);
        return out;
    }
}
