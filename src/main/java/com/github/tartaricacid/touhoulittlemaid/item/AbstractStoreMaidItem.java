package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;

public abstract class AbstractStoreMaidItem extends Item {
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
        CustomData maidInfo = stack.get(InitDataComponent.MAID_INFO);
        if (maidInfo == null) {
            return Optional.empty();
        }
        Optional<String> modelId = maidInfo.read(Codec.STRING.fieldOf(MODEL_ID_TAG_NAME)).result();
        if (modelId.isEmpty()) {
            return Optional.empty();
        }
        Optional<String> customName = maidInfo.read(Codec.STRING.fieldOf(CUSTOM_NAME)).result();
        return customName
                .map(s -> (TooltipComponent) new ItemMaidTooltip(modelId.get(), s))
                .or(() -> Optional.of(new ItemMaidTooltip(modelId.get(), "")));
    }
}
