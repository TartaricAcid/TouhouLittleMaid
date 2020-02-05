package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BowlModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/2/4 14:57
 **/
public class ItemBowl extends ItemArmor {
    private static final String TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/sukuna_shinmyoumaru.png").toString();

    public ItemBowl() {
        super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".bowl");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        EntityEquipmentSlot equipmentSlot = EntityLiving.getSlotForItemStack(heldItem);
        ItemStack equipmentItem = playerIn.getItemStackFromSlot(equipmentSlot);

        if (equipmentItem.isEmpty() && target instanceof EntityMaid) {
            playerIn.setItemStackToSlot(equipmentSlot, heldItem.copy());
            EntityMaid maid = (EntityMaid) target;
            heldItem.shrink(1);
            if (maid.getRidingEntity() == null && maid.getControllingPassenger() == null) {
                maid.startRiding(playerIn);
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped modelBiped) {
        return BowlModel.INSTANCE;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return TEXTURE;
    }
}
