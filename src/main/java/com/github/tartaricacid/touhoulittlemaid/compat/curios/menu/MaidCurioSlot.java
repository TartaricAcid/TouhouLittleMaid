package com.github.tartaricacid.touhoulittlemaid.compat.curios.menu;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.mixin.core.AccessorEntity;

import javax.annotation.Nonnull;

public class MaidCurioSlot extends SlotItemHandler {
    private final String identifier;
    private final EntityMaid maid;
    private final SlotContext slotContext;

    private NonNullList<Boolean> renderStatuses;
    private boolean canToggleRender;
    private boolean showCosmeticToggle;
    private boolean isCosmetic;

    public MaidCurioSlot(EntityMaid maid, IDynamicStackHandler handler, int index, String identifier,
                         int xPosition, int yPosition, NonNullList<Boolean> renders,
                         boolean canToggleRender, boolean showCosmeticToggle, boolean isCosmetic) {
        this(maid, handler, index, identifier, xPosition, yPosition, renders, canToggleRender);
        this.showCosmeticToggle = showCosmeticToggle;
        this.isCosmetic = isCosmetic;
    }

    public MaidCurioSlot(EntityMaid maid, IDynamicStackHandler handler, int index, String identifier,
                         int xPosition, int yPosition, NonNullList<Boolean> renders,
                         boolean canToggleRender) {
        super(handler, index, xPosition, yPosition);
        this.identifier = identifier;
        this.renderStatuses = renders;
        this.maid = maid;
        this.canToggleRender = canToggleRender;
        this.slotContext = new SlotContext(identifier, maid, index, false, renders.get(index));
        CuriosApi.getSlot(identifier, maid.level()).ifPresent(slotType -> this.setBackground(InventoryMenu.BLOCK_ATLAS, slotType.getIcon()));
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean canToggleRender() {
        return this.canToggleRender;
    }

    public boolean isCosmetic() {
        return this.isCosmetic;
    }

    public boolean showCosmeticToggle() {
        return this.showCosmeticToggle;
    }

    public boolean getRenderStatus() {
        if (!this.canToggleRender) {
            return true;
        }
        return this.renderStatuses.size() > this.getSlotIndex() &&
               this.renderStatuses.get(this.getSlotIndex());
    }

    @OnlyIn(Dist.CLIENT)
    public String getSlotName() {
        StringBuilder builder = new StringBuilder();

        if (this.isCosmetic) {
            builder.append(I18n.get("curios.cosmetic")).append(" ");
        }
        String key = "curios.identifier." + this.identifier;
        if (I18n.exists(key)) {
            builder.append(I18n.get(key));
            return builder.toString();
        }
        builder.append(Character.toUpperCase(this.identifier.charAt(0)))
                .append(this.identifier.substring(1).toLowerCase());
        return builder.toString();
    }

    @Override
    public void set(@Nonnull ItemStack stack) {
        ItemStack current = this.getItem();
        boolean flag = current.isEmpty() && stack.isEmpty();
        super.set(stack);

        if (!flag && !ItemStack.matches(current, stack) &&
            !((AccessorEntity) maid).getFirstTick()) {
            CuriosApi.getCurio(stack).ifPresent(curio -> curio.onEquipFromUse(this.slotContext));
        }
    }

    @Override
    public boolean allowModification(@Nonnull Player pPlayer) {
        return true;
    }
}
