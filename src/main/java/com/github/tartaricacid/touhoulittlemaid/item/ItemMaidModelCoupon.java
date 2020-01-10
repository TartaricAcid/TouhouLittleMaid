package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.DrawCalculation;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/12/31 20:37
 **/
public class ItemMaidModelCoupon extends Item {
    private static final String MODEL_DATA_TAG = "ModelData";

    public ItemMaidModelCoupon() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_model_coupon");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MODEL_COUPON_TABS);
        setHasSubtypes(true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab) && GeneralConfig.MAID_CONFIG.maidCannotChangeModel) {
            for (String modelId : DrawCalculation.getModelIdSet()) {
                ItemStack stack = new ItemStack(this, 1, DrawCalculation.getModelLevel(modelId));
                items.add(setModelData(stack, modelId));
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AbstractEntityMaid) {
            AbstractEntityMaid maid = (AbstractEntityMaid) event.getTarget();
            ItemStack stack = event.getItemStack();
            boolean isYourMaid = maid.getOwnerId() != null && maid.getOwnerId().equals(event.getEntityPlayer().getUniqueID());
            boolean stackIsRight = stack.getItem() == MaidItems.MAID_MODEL_COUPON && hasModelData(stack);
            boolean modelIdNotSame = !getModelData(stack).equals(maid.getModelId());
            if (isYourMaid && stackIsRight && modelIdNotSame) {
                maid.setModelId(getModelData(stack));
                stack.shrink(1);
                event.setCanceled(true);
            }
        }
    }

    public static boolean hasModelData(ItemStack coupon) {
        if (coupon.hasTagCompound()) {
            NBTTagCompound tag = coupon.getTagCompound();
            if (tag != null && tag.hasKey(MODEL_DATA_TAG, Constants.NBT.TAG_STRING)) {
                return CommonProxy.VANILLA_ID_NAME_MAP.containsKey(tag.getString(MODEL_DATA_TAG));
            }
        }
        return false;
    }

    public static ItemStack setModelData(ItemStack coupon, String modelId) {
        NBTTagCompound tag;
        if (coupon.hasTagCompound()) {
            tag = coupon.getTagCompound();
        } else {
            tag = new NBTTagCompound();
        }
        if (tag != null && StringUtils.isNotBlank(modelId)) {
            tag.setString(MODEL_DATA_TAG, modelId);
            coupon.setTagCompound(tag);
        }
        return coupon;
    }

    private String getModelData(ItemStack coupon) {
        if (coupon.hasTagCompound()) {
            NBTTagCompound tag = coupon.getTagCompound();
            if (tag != null && tag.hasKey(MODEL_DATA_TAG, Constants.NBT.TAG_STRING)) {
                return tag.getString(MODEL_DATA_TAG);
            }
        }
        return "touhou_little_maid:hakurei_reimu";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getMetadata() > 3) {
            return true;
        }
        return super.hasEffect(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ClientProxy.MAID_MODEL.getInfo(getModelData(stack)).isPresent()) {
            tooltip.add(I18n.format("tooltips.touhou_little_maid.maid_model_coupon.desc",
                    ParseI18n.parse(ClientProxy.MAID_MODEL.getInfo(getModelData(stack)).get().getName())));
        }
        String star;
        switch (stack.getMetadata()) {
            case 1:
                star = "§bN";
                break;
            case 2:
                star = "§aR";
                break;
            case 3:
                star = "§cSR";
                break;
            case 4:
                star = "§5SSR";
                break;
            case 5:
                star = "§6UR";
                break;
            default:
                star = "§bN";
                break;
        }
        tooltip.add(star);
    }
}
