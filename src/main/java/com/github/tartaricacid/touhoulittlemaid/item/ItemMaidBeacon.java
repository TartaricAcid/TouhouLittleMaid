package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemMaidBeacon extends DoubleHighBlockItem {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String STORAGE_DATA_TAG = "StorageData";
    private static final String FORGE_DATA_TAG = "ForgeData";

    public ItemMaidBeacon() {
        super(InitBlocks.MAID_BEACON.get(), (new Item.Properties())
                .stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.2F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()));
    }

    public static ItemStack tileEntityToItemStack(TileEntityMaidBeacon beacon) {
        ItemStack stack = InitItems.MAID_BEACON.get().getDefaultInstance();
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.put(STORAGE_DATA_TAG, beacon.saveWithoutMetadata());
        return stack;
    }

    public static void itemStackToTileEntity(ItemStack stack, TileEntityMaidBeacon beacon) {
        CompoundTag tag = stack.getOrCreateTagElement(STORAGE_DATA_TAG);
        if (tag.contains(FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
            beacon.loadData(tag.getCompound(FORGE_DATA_TAG));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        float numPower = 0f;
        CompoundTag tag = stack.getOrCreateTagElement(STORAGE_DATA_TAG);
        if (tag.contains(FORGE_DATA_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag forgeTag = tag.getCompound(FORGE_DATA_TAG);
            if (forgeTag.contains(TileEntityMaidBeacon.STORAGE_POWER_TAG, Tag.TAG_FLOAT)) {
                numPower = forgeTag.getFloat(TileEntityMaidBeacon.STORAGE_POWER_TAG);
            }
        }
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.maid_beacon.desc", DECIMAL_FORMAT.format(numPower)).withStyle(ChatFormatting.GRAY));
    }
}
