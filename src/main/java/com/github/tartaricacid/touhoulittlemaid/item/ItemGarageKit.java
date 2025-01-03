package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackGarageKitRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.MODEL_ID_TAG_NAME;

public class ItemGarageKit extends BlockItem {
    public static final IClientItemExtensions ITEM_EXTENSIONS = FMLEnvironment.dist == Dist.CLIENT ? new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new TileEntityItemStackGarageKitRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    } : null;
    private static final CustomData DEFAULT_DATA = getDefaultData();

    public ItemGarageKit() {
        super(InitBlocks.GARAGE_KIT.get(), (new Item.Properties()).stacksTo(1));
    }

    public static CustomData getMaidData(ItemStack stack) {
        return Objects.requireNonNullElse(stack.get(InitDataComponent.MAID_INFO), DEFAULT_DATA);
    }

    private static CustomData getDefaultData() {
        CompoundTag data = new CompoundTag();
        data.putString("id", "touhou_little_maid:maid");
        data.putString(MODEL_ID_TAG_NAME, "touhou_little_maid:hakurei_reimu");
        return CustomData.of(data);
    }

    public Component getName(ItemStack stack) {
        CustomData data = ItemGarageKit.getMaidData(stack);
        Level world = Minecraft.getInstance().level;
        if (data.isEmpty() || world == null) {
            return Component.literal("No Info");
        }

        String id = data.read(Codec.STRING.fieldOf("model_id")).getOrThrow();
        MaidModelInfo info = CustomPackLoader.MAID_MODELS.getInfo(id).orElse(null);
        if (info != null) {
            return Component.translatable(ParseI18n.getI18nKey(info.getName()));
        }
        return Component.literal(id);
    }
}
