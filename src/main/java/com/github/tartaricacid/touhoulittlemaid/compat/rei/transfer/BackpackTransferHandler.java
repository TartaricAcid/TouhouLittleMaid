package com.github.tartaricacid.touhoulittlemaid.compat.rei.transfer;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class BackpackTransferHandler implements SimpleTransferHandler {
    private final Class<? extends MaidMainContainer> containerClass;
    private final CategoryIdentifier<?> categoryIdentifier;
    private final int recipeSlotStart;
    private final int recipeSlotCount;
    private final int inventorySlotStart;
    private final int inventorySlotCount;


    public BackpackTransferHandler(Class<? extends MaidMainContainer> containerClass, CategoryIdentifier<?> categoryIdentifier, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
        this.containerClass = containerClass;
        this.categoryIdentifier = categoryIdentifier;
        this.recipeSlotStart = recipeSlotStart;
        this.recipeSlotCount = recipeSlotCount;
        this.inventorySlotStart = inventorySlotStart;
        this.inventorySlotCount = inventorySlotCount;
    }


    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        if (!containerClass.isInstance(context.getMenu())
                || !categoryIdentifier.equals(context.getDisplay().getCategoryIdentifier())
                || context.getContainerScreen() == null) {
            return ApplicabilityResult.createNotApplicable();
        } else {
            return ApplicabilityResult.createApplicable();
        }
    }

    @Override
    public Iterable<SlotAccessor> getInputSlots(Context context) {
        return IntStream.range(recipeSlotStart, recipeSlotStart + recipeSlotCount)
                .mapToObj(id -> SlotAccessor.fromSlot(context.getMenu().getSlot(id)))
                .toList();
    }

    @Override
    public Iterable<SlotAccessor> getInventorySlots(Context context) {
        AbstractContainerMenu menu = context.getMenu();
        return IntStream.range(inventorySlotStart, inventorySlotCount)
                .mapToObj(index -> SlotAccessor.fromSlot(menu.getSlot(index)))
                .collect(Collectors.toList());
    }
}