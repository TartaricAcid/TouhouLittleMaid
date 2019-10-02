package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto.NBT.MAID_INFO;

/**
 * @author TartaricAcid
 * @date 2019/8/6 15:41
 **/
public class ItemPhoto extends Item {
    public ItemPhoto() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".photo");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    static EnumActionResult onPhotoUse(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack photo) {
        // 方向不对或者位置不合适
        if (facing != EnumFacing.UP || PlaceHelper.notSuitableForPlaceMaid(worldIn, pos)) {
            if (worldIn.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
            return EnumActionResult.FAIL;
        }

        // 检查照片的 NBT 数据
        if (photo.getTagCompound() == null || photo.getTagCompound().getCompoundTag(MAID_INFO.getNbtName()).isEmpty()) {
            if (worldIn.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.photo.have_no_nbt_data"));
            }
            return EnumActionResult.FAIL;
        }

        // 最后才应用生成实体的逻辑
        EntityMaid maid = new EntityMaid(worldIn);
        maid.readEntityFromNBT(photo.getTagCompound().getCompoundTag(MAID_INFO.getNbtName()));
        maid.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
        // 实体生成必须在服务端应用
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(maid);
        }
        maid.spawnExplosionParticle();
        photo.shrink(1);
        return EnumActionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return onPhotoUse(player, worldIn, pos, facing, hitX, hitY, hitZ, player.getHeldItem(hand));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        // 无数据时显示红色提醒信息
        boolean haveNoData = !stack.hasTagCompound() ||
                Objects.requireNonNull(stack.getTagCompound()).getCompoundTag(MAID_INFO.getNbtName()).isEmpty();
        if (haveNoData) {
            tooltip.add(TextFormatting.DARK_RED + I18n.format("tooltips.touhou_little_maid.photo.no_data.desc"));
        } else {
            String modelId = stack.getTagCompound().getCompoundTag(MAID_INFO.getNbtName()).getString(EntityMaid.NBT.MODEL_ID.getName());
            if (!"".equals(modelId)) {
                ClientProxy.MAID_MODEL.getInfo(modelId).ifPresent(modelItem ->
                        tooltip.add(I18n.format("tooltips.touhou_little_maid.photo.maid.desc",
                                ParseI18n.parse(modelItem.getName()))));
            }
        }
        // 调试模式直接显示整个 NBT
        if (flagIn.isAdvanced() && GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
            tooltip.add(Objects.requireNonNull(stack.getTagCompound()).toString());
        }
    }

    enum NBT {
        // 女仆 NBT 数据
        MAID_INFO("MaidInfo");

        private String nbtName;

        NBT(String nbtName) {
            this.nbtName = nbtName;
        }

        public String getNbtName() {
            return nbtName;
        }
    }
}
