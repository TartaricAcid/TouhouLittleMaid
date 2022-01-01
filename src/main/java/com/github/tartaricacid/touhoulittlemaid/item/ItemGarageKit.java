package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackGarageKitRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.GARAGE_KIT_TAB;

public class ItemGarageKit extends BlockItem {
    private static final String ENTITY_INFO = "EntityInfo";
    private static final CompoundNBT DEFAULT_DATA = getDefaultData();

    public ItemGarageKit() {
        super(InitBlocks.GARAGE_KIT.get(), (new Item.Properties()).tab(GARAGE_KIT_TAB).stacksTo(1)
                .setISTER(() -> TileEntityItemStackGarageKitRenderer::new));
    }

    private static boolean hasMaidData(ItemStack stack) {
        return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(ENTITY_INFO).isEmpty();
    }

    public static CompoundNBT getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(ENTITY_INFO);
        }
        return DEFAULT_DATA;
    }

    private static CompoundNBT getDefaultData() {
        CompoundNBT data = new CompoundNBT();
        data.putString("id", "touhou_little_maid:maid");
        data.putString(EntityMaid.MODEL_ID_TAG, "touhou_little_maid:hakurei_reimu");
        return data;
    }
}
