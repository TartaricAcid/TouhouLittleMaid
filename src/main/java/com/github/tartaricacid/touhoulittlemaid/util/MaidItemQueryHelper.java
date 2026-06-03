package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IExtraStorageBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 女仆物品查询与操作工具类，供 AI {@code manage_item} Tool 调用。
 * <p>
 * 支持以下存储空间：主手/副手/装备槽、女仆背包(backpack_N)、饰品栏(bauble_N)、
 * 以及通过 {@link IExtraStorageBackpack} 暴露的额外存储(extra_N，如末影箱)。
 * </p>
 * <p>
 * 物品匹配采用 token 阈值策略：将查询按空白分词后，若至少 1 个 token 命中，
 * 且命中 token 数 ≥ 总数一半则通过，
 * 以此兼容中英混合查询（如 "盾牌 shield"）。
 * </p>
 *
 * @see com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.ManageItemTool
 * @see com.github.tartaricacid.touhoulittlemaid.api.backpack.IExtraStorageBackpack
 */
public final class MaidItemQueryHelper {
    private static final String MAINHAND = "mainhand";
    private static final String OFFHAND = "offhand";
    private static final String HEAD = "head";
    private static final String CHEST = "chest";
    private static final String LEGS = "legs";
    private static final String FEET = "feet";
    private static final String BACKPACK_PREFIX = "backpack_";
    private static final String BAUBLE_PREFIX = "bauble_";
    private static final String EXTRA_PREFIX = "extra_";
    private static final String COMPONENTS = "components";
    private static final String CUSTOM_DATA = "minecraft:custom_data";
    private static final int SUMMARY_DEPTH = 2;
    private static final int SEARCH_DEPTH = 4;
    private static final int SEARCH_MAX_LIST_ENTRIES = 8;
    private static final int SEARCH_MAX_CHARS = 8192;
    private static final int AUTO_PICKUP_COOLDOWN_AFTER_DROP_TICKS = 100;

    private MaidItemQueryHelper() {
    }

    /**
     * @return 所有可用槽位名称列表，包含主手/副手/装备槽、背包、饰品、额外存储
     */
    public static List<String> getSlotNames(EntityMaid maid) {
        List<String> slots = Lists.newArrayList(MAINHAND, OFFHAND, HEAD, CHEST, LEGS, FEET);
        CombinedInvWrapper backpack = maid.getAvailableBackpackInv();
        for (int i = 0; i < backpack.getSlots(); i++) {
            slots.add(BACKPACK_PREFIX + i);
        }
        for (int i = 0; i < maid.getMaidBauble().getSlots(); i++) {
            slots.add(BAUBLE_PREFIX + i);
        }
        getExtraStorage(maid).ifPresent(extra -> {
            for (int i = 0; i < extra.getSlots(); i++) {
                slots.add(EXTRA_PREFIX + i);
            }
        });
        return slots;
    }

    /**
     * @return swap/equip 操作允许的目标槽位：主手、副手、四个装备槽
     */
    public static List<String> getTargetSlotNames() {
        return List.of(MAINHAND, OFFHAND, HEAD, CHEST, LEGS, FEET);
    }

    /**
     * 根据已知槽位名直接检索物品详情，含 NBT 摘要。
     */
    public static String inspect(EntityMaid maid, String slotName) {
        Optional<SlotRef> slot = parseSlot(maid, slotName);
        if (slot.isEmpty()) {
            return invalidSlot(slotName, maid);
        }

        SlotRef ref = slot.get();
        ItemStack stack = ref.stack();
        if (stack.isEmpty()) {
            return "Slot %s is empty.".formatted(ref.name());
        }

        return formatStack(ref.name(), maid, stack);
    }

    /**
     * 按描述关键词在所有存储空间中搜索匹配物品。
     * 本地未找到时会通过 {@link ItemsUtil#findStackSlot(IItemHandler, Predicate)} 尝试从外部容器拉取。
     */
    public static String inspectMatching(EntityMaid maid, String desc) {
        if (StringUtils.isBlank(desc)) {
            return "Missing required parameter: item_desc";
        }

        List<SlotRef> matches = findMatchingMoveSources(maid, desc, null);
        if (matches.isEmpty()) {
            requestMatchingItem(maid, stack -> !stack.isEmpty() && matches(maid, stack, desc));
            matches = findMatchingMoveSources(maid, desc, null);
        }

        if (matches.isEmpty()) {
            return "No item matching \"%s\" found.".formatted(desc);
        }
        if (matches.size() > 1) {
            String lines = matches.stream()
                    .map(slot -> "- %s: %s".formatted(slot.name(), formatItemBrief(maid, slot.stack())))
                    .collect(Collectors.joining("\n"));
            return "Multiple matches found:\n%s\nPlease refine your search.".formatted(lines);
        }

        SlotRef match = matches.getFirst();
        return formatStack(match.name(), maid, match.stack());
    }

    /**
     * 搜索匹配物品并交换到主手。
     */
    public static String swap(EntityMaid maid, String desc) {
        return swap(maid, desc, MAINHAND);
    }

    /**
     * 搜索匹配物品并交换到指定目标槽位。
     *
     * @param targetSlotName 目标槽位名，空字符串默认主手
     */
    public static String swap(EntityMaid maid, String desc, String targetSlotName) {
        if (StringUtils.isBlank(desc)) {
            return "Missing required parameter: item_desc";
        }

        Optional<SlotRef> targetSlot = parseTargetSlot(maid, defaultTargetSlot(targetSlotName));
        if (targetSlot.isEmpty()) {
            return invalidTargetSlot(targetSlotName);
        }

        SlotRef target = targetSlot.get();
        boolean armorTarget = target.type() == SlotType.EQUIPMENT;
        return moveMatchingToTarget(maid, desc, target, armorTarget, "Swapped to");
    }

    /**
     * 搜索护甲类物品并自动穿戴到对应装备槽。
     * 当 {@code targetSlotName} 为空时自动检测 {@link Equipable#getEquipmentSlot()}；
     * 不为空时按指定的目标槽位移动。
     */
    public static String equip(EntityMaid maid, String desc, String targetSlotName) {
        if (StringUtils.isBlank(desc)) {
            return "Missing required parameter: item_desc";
        }
        if (StringUtils.isNotBlank(targetSlotName)) {
            Optional<SlotRef> targetSlot = parseTargetSlot(maid, targetSlotName);
            if (targetSlot.isEmpty()) {
                return invalidTargetSlot(targetSlotName);
            }
            SlotRef target = targetSlot.get();
            boolean armorTarget = target.type() == SlotType.EQUIPMENT;
            return moveMatchingToTarget(maid, desc, target, armorTarget, armorTarget ? "Equipped to" : "Swapped to");
        }

        for (SlotRef armorSlot : getArmorSlots(maid)) {
            if (!armorSlot.stack().isEmpty() && matches(maid, armorSlot.stack(), desc)) {
                return "Already equipped in %s: %s".formatted(formatSlotName(armorSlot.name()),
                        formatItemBrief(maid, armorSlot.stack()));
            }
        }

        List<SlotRef> matches = findMatchingMoveSources(maid, desc, null);
        List<EquipCandidate> candidates = findEquipCandidates(maid, matches);
        if (matches.isEmpty() || candidates.isEmpty()) {
            requestMatchingItem(maid, stack -> !stack.isEmpty()
                    && matches(maid, stack, desc)
                    && getArmorEquipmentSlot(stack).isPresent());
            matches = findMatchingMoveSources(maid, desc, null);
            candidates = findEquipCandidates(maid, matches);
        }

        if (matches.isEmpty()) {
            return "No item matching \"%s\" found.".formatted(desc);
        }
        if (candidates.isEmpty()) {
            return "No equipable armor matching \"%s\" found.".formatted(desc);
        }
        if (candidates.size() > 1) {
            String lines = candidates.stream()
                    .map(candidate -> "- %s -> %s: %s".formatted(candidate.source().name(),
                            candidate.target().name(), formatItemBrief(maid, candidate.source().stack())))
                    .collect(Collectors.joining("\n"));
            return "Multiple equipable matches found:\n%s\nPlease refine your search.".formatted(lines);
        }

        EquipCandidate candidate = candidates.getFirst();
        ItemStack moved = moveToSlot(maid, candidate.source(), candidate.target());
        if (moved.isEmpty()) {
            return "No item moved from %s.".formatted(candidate.source().name());
        }
        return "Equipped to %s: %s".formatted(formatSlotName(candidate.target().name()), formatItemBrief(maid, moved));
    }

    /**
     * 丢弃指定槽位的物品。丢弃后将自动设置拾取冷却防止自丢自捡。
     *
     * @param amount 丢弃数量，-1 表示丢弃整组
     */
    public static String drop(EntityMaid maid, String slotName, int amount) {
        Optional<SlotRef> slot = parseSlot(maid, slotName);
        if (slot.isEmpty()) {
            return invalidSlot(slotName, maid);
        }
        if (amount == 0 || amount < -1) {
            return "Invalid parameter: amount must be positive, or omitted to drop the whole stack.";
        }

        SlotRef ref = slot.get();
        ItemStack stack = ref.stack();
        if (stack.isEmpty()) {
            return "Slot %s is empty.".formatted(ref.name());
        }

        int dropCount = amount < 0 ? stack.getCount() : Math.min(amount, stack.getCount());
        ItemStack dropped;
        if (ref.type() == SlotType.BACKPACK || ref.type() == SlotType.BAUBLE || ref.type() == SlotType.EXTRA) {
            dropped = extractFromHandlerSlot(maid, ref, dropCount);
        } else {
            dropped = stack.split(dropCount);
            if (stack.isEmpty()) {
                setSlotStack(maid, ref, ItemStack.EMPTY);
            }
        }

        if (dropped.isEmpty()) {
            return "No item dropped from %s.".formatted(ref.name());
        }
        maid.spawnAtLocation(dropped);
        maid.setAutoPickupCooldown(AUTO_PICKUP_COOLDOWN_AFTER_DROP_TICKS);
        return "Dropped %s x%d".formatted(dropped.getHoverName().getString(), dropped.getCount());
    }

    /**
     * 从 {@code minecraft:custom_data} 组件提取 NBT 摘要。
     * 深度限制 {@value #SUMMARY_DEPTH} 层，避免全量 NBT 噪音，
     * 可兼容识别 TACZ 枪械的 GunId、弹药数、配件等信息。
     */
    public static String extractNbtSummary(EntityMaid maid, ItemStack stack) {
        if (stack.isEmpty()) {
            return StringUtils.EMPTY;
        }

        Optional<CompoundTag> rootTag = saveStack(maid, stack);
        return rootTag.map(MaidItemQueryHelper::extractNbtSummary).orElse(StringUtils.EMPTY);
    }

    private static String extractNbtSummary(CompoundTag rootTag) {
        CompoundTag components = rootTag.getCompound(COMPONENTS);
        List<String> lines = Lists.newArrayList();
        CompoundTag customData = components.getCompound(CUSTOM_DATA);
        if (!customData.isEmpty()) {
            appendTagLines(customData, lines);
        }
        components.getAllKeys().stream()
                .filter(key -> !Objects.equals(key, CUSTOM_DATA))
                .sorted()
                .forEach(key -> lines.add("%s: %s".formatted(key, summarizeTag(components.get(key)))));
        return String.join("\n", lines);
    }

    private static String moveMatchingToTarget(EntityMaid maid, String desc, SlotRef target,
                                              boolean requireEquipable, String successPrefix) {
        if (!target.stack().isEmpty() && matches(maid, target.stack(), desc)) {
            return "Already in %s: %s".formatted(formatSlotName(target.name()), formatItemBrief(maid, target.stack()));
        }

        List<SlotRef> matches = findMatchingMoveSources(maid, desc, target);
        List<SlotRef> movableMatches = matches;
        if (requireEquipable) {
            movableMatches = matches.stream()
                    .filter(slot -> canEquipTo(slot.stack(), target.equipmentSlot()))
                    .toList();
        }

        if (matches.isEmpty() || movableMatches.isEmpty()) {
            Predicate<ItemStack> requestFilter = stack -> !stack.isEmpty()
                    && matches(maid, stack, desc)
                    && (!requireEquipable || canEquipTo(stack, target.equipmentSlot()));
            requestMatchingItem(maid, requestFilter);
            matches = findMatchingMoveSources(maid, desc, target);
            movableMatches = requireEquipable ? matches.stream()
                    .filter(slot -> canEquipTo(slot.stack(), target.equipmentSlot()))
                    .toList() : matches;
        }

        if (matches.isEmpty()) {
            return "No item matching \"%s\" found.".formatted(desc);
        }
        if (movableMatches.isEmpty()) {
            return "No item matching \"%s\" can be equipped to %s.".formatted(desc, formatSlotName(target.name()));
        }
        if (movableMatches.size() > 1) {
            String lines = movableMatches.stream()
                    .map(slot -> "- %s: %s".formatted(slot.name(), formatItemBrief(maid, slot.stack())))
                    .collect(Collectors.joining("\n"));
            return "Multiple matches found:\n%s\nPlease refine your search.".formatted(lines);
        }

        SlotRef source = movableMatches.getFirst();
        ItemStack moved = moveToSlot(maid, source, target);
        if (moved.isEmpty()) {
            return "No item moved from %s.".formatted(source.name());
        }
        return "%s %s: %s".formatted(successPrefix, formatSlotName(target.name()), formatItemBrief(maid, moved));
    }

    /**
     * 将物品从 source 移动到 target，同时处理 target 原有物品的回迁。
     * 当 source 为 bauble/extra 且无法写入 target 旧物品时，
     * 旧物品会降级存入背包；若背包也满则操作失败。
     */
    private static ItemStack moveToSlot(EntityMaid maid, SlotRef source, SlotRef target) {
        if (Objects.equals(source.name(), target.name())) {
            return ItemStack.EMPTY;
        }

        ItemStack oldTarget = target.stack().copy();
        ItemStack moved = extractSlotStack(maid, source);
        if (moved.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!canMoveOldTargetToSource(maid, source, oldTarget)) {
            setSlotStack(maid, source, moved);
            return ItemStack.EMPTY;
        }

        moveOldTargetToSource(maid, source, oldTarget);
        setSlotStack(maid, target, moved);
        return moved;
    }

    private static ItemStack extractSlotStack(EntityMaid maid, SlotRef slot) {
        if (slot.type() == SlotType.BACKPACK || slot.type() == SlotType.BAUBLE || slot.type() == SlotType.EXTRA) {
            return extractFromHandlerSlot(maid, slot, slot.stack().getCount());
        }
        ItemStack stack = slot.stack();
        setSlotStack(maid, slot, ItemStack.EMPTY);
        return stack;
    }

    private static ItemStack extractFromHandlerSlot(EntityMaid maid, SlotRef slot, int amount) {
        return switch (slot.type()) {
            case BACKPACK -> maid.getAvailableBackpackInv().extractItem(slot.index(), amount, false);
            case BAUBLE -> maid.getMaidBauble().extractItem(slot.index(), amount, false);
            case EXTRA -> getExtraStorage(maid)
                    .map(extra -> extra.extractItem(slot.index(), amount, false))
                    .orElse(ItemStack.EMPTY);
            default -> ItemStack.EMPTY;
        };
    }

    private static void setSlotStack(EntityMaid maid, SlotRef slot, ItemStack stack) {
        switch (slot.type()) {
            case MAINHAND -> maid.setItemInHand(InteractionHand.MAIN_HAND, stack);
            case OFFHAND -> maid.setItemInHand(InteractionHand.OFF_HAND, stack);
            case EQUIPMENT -> maid.setItemSlot(slot.equipmentSlot(), stack);
            case BACKPACK -> maid.getAvailableBackpackInv().setStackInSlot(slot.index(), stack);
            case BAUBLE -> maid.getMaidBauble().setStackInSlot(slot.index(), stack);
            case EXTRA -> storeRemainderInBackpackOrDrop(maid, insertIntoHandlerSlot(maid, slot, stack, false));
        }
    }

    /**
     * 检查 source 槽位是否允许存放 target 旧物品。
     * bauble/extra 槽位有物品类型限制，不允许时将旧物品回退到背包。
     * 此方法必须在 source 已被提空后调用，才能准确模拟 handler 槽位插入结果。
     */
    private static boolean canMoveOldTargetToSource(EntityMaid maid, SlotRef source, ItemStack oldTarget) {
        if (oldTarget.isEmpty() || source.type() != SlotType.BAUBLE && source.type() != SlotType.EXTRA) {
            return true;
        }
        ItemStack remainder = insertIntoHandlerSlot(maid, source, oldTarget.copy(), true);
        return remainder.isEmpty() || canInsertIntoBackpack(maid, remainder);
    }

    /**
     * 将 target 旧物品移回 source。若 source 是 bauble/extra 且无法接受该物品，
     * 则降级存入女仆背包；若背包也满，最后掉落在女仆位置，避免静默删除。
     */
    private static void moveOldTargetToSource(EntityMaid maid, SlotRef source, ItemStack oldTarget) {
        if (oldTarget.isEmpty()) {
            return;
        }
        if (source.type() == SlotType.BAUBLE || source.type() == SlotType.EXTRA) {
            ItemStack remainder = insertIntoHandlerSlot(maid, source, oldTarget.copy(), false);
            storeRemainderInBackpackOrDrop(maid, remainder);
            return;
        }
        setSlotStack(maid, source, oldTarget.copy());
    }

    private static ItemStack insertIntoHandlerSlot(EntityMaid maid, SlotRef source, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return switch (source.type()) {
            case BAUBLE -> maid.getMaidBauble().insertItem(source.index(), stack, simulate);
            case EXTRA -> getExtraStorage(maid)
                    .map(extra -> extra.insertItem(source.index(), stack, simulate))
                    .orElse(stack);
            default -> ItemStack.EMPTY;
        };
    }

    private static void storeRemainderInBackpackOrDrop(EntityMaid maid, ItemStack remainder) {
        if (remainder.isEmpty()) {
            return;
        }
        ItemStack backpackRemainder = ItemHandlerHelper.insertItemStacked(maid.getAvailableBackpackInv(), remainder, false);
        if (!backpackRemainder.isEmpty()) {
            maid.spawnAtLocation(backpackRemainder);
        }
    }

    private static boolean canInsertIntoBackpack(EntityMaid maid, ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(maid.getAvailableBackpackInv(), stack.copy(), true).isEmpty();
    }

    private static List<SlotRef> findMatchingMoveSources(EntityMaid maid, String desc, SlotRef target) {
        return getMoveSlots(maid).stream()
                .filter(slot -> target == null || !Objects.equals(slot.name(), target.name()))
                .filter(slot -> !slot.stack().isEmpty())
                .filter(slot -> matches(maid, slot.stack(), desc))
                .toList();
    }

    private static List<EquipCandidate> findEquipCandidates(EntityMaid maid, List<SlotRef> matches) {
        List<EquipCandidate> candidates = Lists.newArrayList();
        for (SlotRef source : matches) {
            getArmorEquipmentSlot(source.stack()).ifPresent(slot -> {
                SlotRef target = getEquipmentSlotRef(maid, slot);
                candidates.add(new EquipCandidate(source, target));
            });
        }
        return candidates;
    }

    private static Optional<SlotRef> parseTargetSlot(EntityMaid maid, String slotName) {
        Optional<SlotRef> slot = parseSlot(maid, slotName);
        if (slot.isEmpty() || slot.get().type() == SlotType.BACKPACK
                || slot.get().type() == SlotType.BAUBLE || slot.get().type() == SlotType.EXTRA) {
            return Optional.empty();
        }
        return slot;
    }

    private static Optional<SlotRef> parseSlot(EntityMaid maid, String slotName) {
        if (StringUtils.isBlank(slotName)) {
            return Optional.empty();
        }

        String normalized = slotName.toLowerCase(Locale.ROOT);
        if (Objects.equals(normalized, MAINHAND)) {
            return Optional.of(new SlotRef(MAINHAND, SlotType.MAINHAND, -1, EquipmentSlot.MAINHAND,
                    maid.getMainHandItem()));
        }
        if (Objects.equals(normalized, OFFHAND)) {
            return Optional.of(new SlotRef(OFFHAND, SlotType.OFFHAND, -1, EquipmentSlot.OFFHAND,
                    maid.getOffhandItem()));
        }
        Optional<EquipmentSlot> equipmentSlot = getEquipmentSlot(normalized);
        if (equipmentSlot.isPresent()) {
            EquipmentSlot slot = equipmentSlot.get();
            return Optional.of(getEquipmentSlotRef(maid, slot));
        }
        if (normalized.startsWith(EXTRA_PREFIX)) {
            return parseExtraSlot(maid, normalized);
        }
        if (!normalized.startsWith(BACKPACK_PREFIX)) {
            return parseBaubleSlot(maid, normalized);
        }

        try {
            int index = Integer.parseInt(normalized.substring(BACKPACK_PREFIX.length()));
            CombinedInvWrapper backpack = maid.getAvailableBackpackInv();
            if (index < 0 || index >= backpack.getSlots()) {
                return Optional.empty();
            }
            return Optional.of(new SlotRef(BACKPACK_PREFIX + index, SlotType.BACKPACK, index, null,
                    backpack.getStackInSlot(index)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static Optional<SlotRef> parseBaubleSlot(EntityMaid maid, String slotName) {
        if (!slotName.startsWith(BAUBLE_PREFIX)) {
            return Optional.empty();
        }
        try {
            int index = Integer.parseInt(slotName.substring(BAUBLE_PREFIX.length()));
            if (index < 0 || index >= maid.getMaidBauble().getSlots()) {
                return Optional.empty();
            }
            return Optional.of(new SlotRef(BAUBLE_PREFIX + index, SlotType.BAUBLE, index, null,
                    maid.getMaidBauble().getStackInSlot(index)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static Optional<SlotRef> parseExtraSlot(EntityMaid maid, String slotName) {
        try {
            int index = Integer.parseInt(slotName.substring(EXTRA_PREFIX.length()));
            Optional<IItemHandler> extraStorage = getExtraStorage(maid);
            if (extraStorage.isEmpty()) {
                return Optional.empty();
            }
            IItemHandler extra = extraStorage.get();
            if (index < 0 || index >= extra.getSlots()) {
                return Optional.empty();
            }
            return Optional.of(new SlotRef(EXTRA_PREFIX + index, SlotType.EXTRA, index, null,
                    extra.getStackInSlot(index)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static List<SlotRef> getMoveSlots(EntityMaid maid) {
        List<SlotRef> slots = Lists.newArrayList();
        slots.addAll(getBackpackSlots(maid));
        slots.addAll(getBaubleSlots(maid));
        slots.addAll(getExtraSlots(maid));
        slots.add(new SlotRef(MAINHAND, SlotType.MAINHAND, -1, EquipmentSlot.MAINHAND, maid.getMainHandItem()));
        slots.add(new SlotRef(OFFHAND, SlotType.OFFHAND, -1, EquipmentSlot.OFFHAND, maid.getOffhandItem()));
        slots.addAll(getArmorSlots(maid));
        return slots;
    }

    private static List<SlotRef> getBaubleSlots(EntityMaid maid) {
        List<SlotRef> slots = Lists.newArrayList();
        for (int i = 0; i < maid.getMaidBauble().getSlots(); i++) {
            slots.add(new SlotRef(BAUBLE_PREFIX + i, SlotType.BAUBLE, i, null,
                    maid.getMaidBauble().getStackInSlot(i)));
        }
        return slots;
    }

    private static List<SlotRef> getExtraSlots(EntityMaid maid) {
        List<SlotRef> slots = Lists.newArrayList();
        getExtraStorage(maid).ifPresent(extra -> {
            for (int i = 0; i < extra.getSlots(); i++) {
                slots.add(new SlotRef(EXTRA_PREFIX + i, SlotType.EXTRA, i, null, extra.getStackInSlot(i)));
            }
        });
        return slots;
    }

    private static List<SlotRef> getBackpackSlots(EntityMaid maid) {
        List<SlotRef> slots = Lists.newArrayList();
        CombinedInvWrapper backpack = maid.getAvailableBackpackInv();
        for (int i = 0; i < backpack.getSlots(); i++) {
            slots.add(new SlotRef(BACKPACK_PREFIX + i, SlotType.BACKPACK, i, null, backpack.getStackInSlot(i)));
        }
        return slots;
    }

    private static List<SlotRef> getArmorSlots(EntityMaid maid) {
        return List.of(
                getEquipmentSlotRef(maid, EquipmentSlot.HEAD),
                getEquipmentSlotRef(maid, EquipmentSlot.CHEST),
                getEquipmentSlotRef(maid, EquipmentSlot.LEGS),
                getEquipmentSlotRef(maid, EquipmentSlot.FEET)
        );
    }

    private static SlotRef getEquipmentSlotRef(EntityMaid maid, EquipmentSlot slot) {
        return new SlotRef(slot.getName(), SlotType.EQUIPMENT, -1, slot, maid.getItemBySlot(slot));
    }

    private static Optional<EquipmentSlot> getEquipmentSlot(String name) {
        return switch (name) {
            case HEAD -> Optional.of(EquipmentSlot.HEAD);
            case CHEST -> Optional.of(EquipmentSlot.CHEST);
            case LEGS -> Optional.of(EquipmentSlot.LEGS);
            case FEET -> Optional.of(EquipmentSlot.FEET);
            default -> Optional.empty();
        };
    }

    private static Optional<EquipmentSlot> getArmorEquipmentSlot(ItemStack stack) {
        Equipable equipable = Equipable.get(stack);
        if (equipable == null) {
            return Optional.empty();
        }
        EquipmentSlot slot = equipable.getEquipmentSlot();
        return isArmorSlot(slot) ? Optional.of(slot) : Optional.empty();
    }

    private static boolean canEquipTo(ItemStack stack, EquipmentSlot targetSlot) {
        return targetSlot != null && getArmorEquipmentSlot(stack).filter(slot -> slot == targetSlot).isPresent();
    }

    private static boolean isArmorSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST
                || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
    }

    private static boolean matches(EntityMaid maid, ItemStack stack, String desc) {
        String query = desc.toLowerCase(Locale.ROOT).trim();
        if (query.isEmpty()) {
            return false;
        }

        Optional<CompoundTag> saved = saveStack(maid, stack);
        String searchText = "%s\n%s\n%s\n%s".formatted(
                stack.getHoverName().getString(),
                getItemId(stack),
                saved.map(MaidItemQueryHelper::extractNbtSummary).orElse(StringUtils.EMPTY),
                saved.map(MaidItemQueryHelper::extractSearchNbtText).orElse(StringUtils.EMPTY)
        ).toLowerCase(Locale.ROOT);
        return matchesAtLeastHalfTokens(searchText, query);
    }

    /**
     * Token 阈值匹配：查询按空白分词后，命中 token 数须 ≥ 总数的一半且至少命中 1 个。
     * 例如 "盾牌 shield" (2 tokens) 命中 "shield" 即可通过，兼容中英混合查询。
     */
    private static boolean matchesAtLeastHalfTokens(String searchText, String query) {
        String[] tokens = query.split("\\s+");
        int matched = 0;
        for (String token : tokens) {
            if (searchText.contains(token)) {
                matched++;
            }
        }
        return matched > 0 && matched * 2 >= tokens.length;
    }

    private static void requestMatchingItem(EntityMaid maid, Predicate<ItemStack> filter) {
        ItemsUtil.findStackSlot(maid.getAvailableBackpackInv(), filter);
    }

    private static String extractSearchNbtText(CompoundTag rootTag) {
        StringBuilder builder = new StringBuilder();
        appendSearchTag(rootTag, builder, SEARCH_DEPTH);
        return builder.toString();
    }

    private static void appendSearchTag(Tag tag, StringBuilder builder, int depth) {
        if (tag == null || builder.length() >= SEARCH_MAX_CHARS) {
            return;
        }
        if (tag instanceof CompoundTag compoundTag) {
            compoundTag.getAllKeys().stream()
                    .sorted()
                    .forEach(key -> {
                        appendSearchText(builder, key);
                        if (depth > 0) {
                            appendSearchTag(compoundTag.get(key), builder, depth - 1);
                        }
                    });
            return;
        }
        if (tag instanceof ListTag listTag) {
            appendSearchText(builder, "list_size_%d".formatted(listTag.size()));
            if (depth > 0) {
                int maxIndex = Math.min(listTag.size(), SEARCH_MAX_LIST_ENTRIES);
                for (int i = 0; i < maxIndex; i++) {
                    appendSearchTag(listTag.get(i), builder, depth - 1);
                }
            }
            return;
        }
        appendSearchText(builder, tag.getAsString());
    }

    private static void appendSearchText(StringBuilder builder, String text) {
        if (StringUtils.isBlank(text) || builder.length() >= SEARCH_MAX_CHARS) {
            return;
        }
        int remaining = SEARCH_MAX_CHARS - builder.length();
        if (builder.length() > 0) {
            builder.append('\n');
            remaining--;
        }
        if (remaining > 0) {
            builder.append(StringUtils.left(text, remaining));
        }
    }

    private static Optional<IItemHandler> getExtraStorage(EntityMaid maid) {
        if (maid.getMaidBackpackType() instanceof IExtraStorageBackpack backpack) {
            return Optional.ofNullable(backpack.getExtraStorage(maid));
        }
        return Optional.empty();
    }

    private static String formatStack(String slotName, EntityMaid maid, ItemStack stack) {
        StringBuilder builder = new StringBuilder();
        builder.append("Slot: ").append(slotName).append('\n');
        builder.append("Name: ").append(stack.getHoverName().getString()).append('\n');
        builder.append("Item: ").append(getItemId(stack)).append('\n');
        builder.append("Count: ").append(stack.getCount());

        String summary = extractNbtSummary(maid, stack);
        if (StringUtils.isNotBlank(summary)) {
            builder.append('\n').append(summary);
        }
        return builder.toString();
    }

    private static String formatItemBrief(EntityMaid maid, ItemStack stack) {
        String summary = extractNbtSummary(maid, stack).lines()
                .limit(4)
                .collect(Collectors.joining(", "));
        if (StringUtils.isBlank(summary)) {
            return "%s [%s]".formatted(stack.getHoverName().getString(), getItemId(stack));
        }
        return "%s [%s]".formatted(stack.getHoverName().getString(), summary);
    }

    private static Optional<CompoundTag> saveStack(EntityMaid maid, ItemStack stack) {
        try {
            Tag tag = stack.saveOptional(maid.level().registryAccess());
            if (tag instanceof CompoundTag compoundTag) {
                return Optional.of(compoundTag);
            }
        } catch (RuntimeException ignored) {
        }
        return Optional.empty();
    }

    private static void appendTagLines(CompoundTag tag, List<String> lines) {
        tag.getAllKeys().stream()
                .sorted()
                .forEach(key -> lines.add("%s: %s".formatted(key, summarizeTag(tag.get(key)))));
    }

    private static String summarizeTag(Tag tag) {
        return summarizeTag(tag, SUMMARY_DEPTH);
    }

    private static String summarizeTag(Tag tag, int depth) {
        if (tag instanceof CompoundTag compoundTag) {
            if (depth <= 0) {
                return "{%d keys}".formatted(compoundTag.size());
            }
            String values = compoundTag.getAllKeys().stream()
                    .sorted()
                    .map(key -> "%s: %s".formatted(key, summarizeTag(compoundTag.get(key), depth - 1)))
                    .collect(Collectors.joining(", "));
            return values.isEmpty() ? "{}" : "{ %s }".formatted(values);
        }
        if (tag instanceof ListTag listTag) {
            return "[%d entries]".formatted(listTag.size());
        }
        if (tag instanceof StringTag) {
            return "\"%s\"".formatted(tag.getAsString());
        }
        return tag == null ? "null" : tag.getAsString();
    }

    private static String getItemId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    private static String defaultTargetSlot(String targetSlotName) {
        return StringUtils.isBlank(targetSlotName) ? MAINHAND : targetSlotName;
    }

    private static String formatSlotName(String slotName) {
        return Objects.equals(slotName, MAINHAND) ? "main hand" : slotName;
    }

    private static String invalidSlot(String slotName, EntityMaid maid) {
        String validSlots = getSlotNames(maid).stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(", "));
        return "Invalid slot: %s. Correct usage: slot must be one of [%s]".formatted(slotName, validSlots);
    }

    private static String invalidTargetSlot(String slotName) {
        String validSlots = String.join(", ", getTargetSlotNames());
        return "Invalid target_slot: %s. Correct usage: target_slot must be one of [%s]".formatted(slotName, validSlots);
    }

    /**
     * 槽位类型：区分原版装备槽、女仆背包、饰品栏、额外存储等不同存储介质，
     * 不同介质使用不同的 get/set/extract 策略。
     */
    private enum SlotType {
        MAINHAND,  // 主手
        OFFHAND,   // 副手
        EQUIPMENT, // 装备槽 (head/chest/legs/feet)
        BACKPACK,  // 女仆背包 (CombinedInvWrapper)
        BAUBLE,    // 饰品栏槽位本身，槽内外部容器内容通过 ItemsUtil 请求流拉取
        EXTRA      // 额外存储 (IExtraStorageBackpack，如末影箱)
    }

    /**
     * 槽位引用：绑定具体存储空间的某个物品位置。
     * 对 MAINHAND/OFFHAND/EQUIPMENT 类型，index 不使用 (-1)。
     */
    private record SlotRef(String name, SlotType type, int index, EquipmentSlot equipmentSlot, ItemStack stack) {
    }

    private record EquipCandidate(SlotRef source, SlotRef target) {
    }
}
