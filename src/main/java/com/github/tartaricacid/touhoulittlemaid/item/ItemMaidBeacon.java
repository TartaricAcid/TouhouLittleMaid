package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public class ItemMaidBeacon extends DoubleHighBlockItem {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String STORAGE_DATA_TAG = "StorageData";
    private static final String FORGE_DATA_TAG = "ForgeData";
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemMaidBeacon() {
        super(InitBlocks.MAID_BEACON.get(), (new Item.Properties()).stacksTo(1));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 3, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.2F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
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

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
