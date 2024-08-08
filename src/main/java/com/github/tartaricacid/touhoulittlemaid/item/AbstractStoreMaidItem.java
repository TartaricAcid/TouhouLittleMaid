package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.data.CompoundData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public abstract class AbstractStoreMaidItem extends Item {
    static final String MAID_INFO = "MaidInfo";
    static final String CUSTOM_NAME = "CustomName";

    public AbstractStoreMaidItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.isCurrentlyGlowing()) {
            entity.setGlowingTag(true);
        }
        if (!entity.isInvulnerable()) {
            entity.setInvulnerable(true);
        }
        Vec3 position = entity.position();
        int minY = entity.level.getMinBuildHeight();
        if (position.y < minY) {
            entity.setNoGravity(true);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.setPos(position.x, minY, position.z);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundData compoundDataComponent = stack.get(InitDataComponent.MAID_INFO);
        if (compoundDataComponent != null) {
            CompoundTag maidData = compoundDataComponent.nbt();
            if (maidData.contains(EntityMaid.MODEL_ID_TAG, Tag.TAG_STRING)) {
                String modelId = maidData.getString(EntityMaid.MODEL_ID_TAG);
                String customName = "";
                if (maidData.contains(CUSTOM_NAME, Tag.TAG_STRING)) {
                    customName = maidData.getString(CUSTOM_NAME);
                }
                return Optional.of(new ItemMaidTooltip(modelId, customName));
            }
        }
        return Optional.empty();
    }
}
