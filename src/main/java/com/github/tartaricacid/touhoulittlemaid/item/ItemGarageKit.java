package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackGarageKitRenderer;
import com.github.tartaricacid.touhoulittlemaid.data.CompoundData;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;

public class ItemGarageKit extends BlockItem {
    private static final String ENTITY_INFO = "EntityInfo";
    private static final CompoundTag DEFAULT_DATA = getDefaultData();
    public static final IClientItemExtensions itemExtensions = new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new TileEntityItemStackGarageKitRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    };

    public ItemGarageKit() {
        super(InitBlocks.GARAGE_KIT.get(), (new Item.Properties()).stacksTo(1));
    }

    public static CompoundTag getMaidData(ItemStack stack) {
        CompoundData compoundData = stack.get(InitDataComponent.MAID_INFO);
        if (compoundData != null) {
            return compoundData.nbt();
        }
        return DEFAULT_DATA;
    }

    private static CompoundTag getDefaultData() {
        CompoundTag data = new CompoundTag();
        data.putString("id", "touhou_little_maid:maid");
        data.putString(MODEL_ID_TAG_NAME, "touhou_little_maid:hakurei_reimu");
        return data;
    }
}
