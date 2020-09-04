package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.MaidNumSerializer;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemSmartSlab extends Item {
    public static final String MAID_INFO_TAG = "StoreMaidInfo";

    public ItemSmartSlab() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".smart_slab");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setHasSubtypes(true);
    }

    public static boolean hasMaidNbtData(ItemStack stack) {
        return stack.hasTagCompound() && !Objects.requireNonNull(stack.getTagCompound()).getCompoundTag(MAID_INFO_TAG).isEmpty();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // meta 1 时，为空石板
        ItemStack slab = player.getHeldItem(hand);
        if (slab.getMetadata() == 1) {
            return EnumActionResult.PASS;
        }

        // 方向不对或者位置不合适
        if (facing != EnumFacing.UP || PlaceHelper.notSuitableForPlaceMaid(worldIn, pos)) {
            if (worldIn.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.photo.not_suitable_for_place_maid"));
            }
            return EnumActionResult.FAIL;
        }

        MaidNumHandler num = player.getCapability(MaidNumSerializer.MAID_NUM_CAP, null);
        if (slab.getMetadata() == 0 && num != null && num.canAdd()) {
            num.add();
            EntityMaid maid = new EntityMaid(worldIn);
            maid.setTamedBy(player);
            maid.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            maid.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
            // 实体生成必须在服务端应用
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(maid);
            }
            maid.spawnExplosionParticle();
            setDamage(slab, 1);
            maid.playSound(SoundEvents.ENTITY_PLAYER_SPLASH, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            return EnumActionResult.SUCCESS;
        }

        if (slab.getMetadata() == 2) {
            // 检查照片的 NBT 数据
            if (slab.getTagCompound() == null || slab.getTagCompound().getCompoundTag(MAID_INFO_TAG).isEmpty()) {
                if (worldIn.isRemote) {
                    player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.photo.have_no_nbt_data"));
                }
                return EnumActionResult.FAIL;
            }

            // 最后才应用生成实体的逻辑
            EntityMaid maid = new EntityMaid(worldIn);
            maid.readEntityFromNBT(slab.getTagCompound().getCompoundTag(MAID_INFO_TAG));
            maid.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            if (slab.hasDisplayName()) {
                maid.setCustomNameTag(slab.getDisplayName());
            }
            // 实体生成必须在服务端应用
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(maid);
            }
            maid.spawnExplosionParticle();
            setDamage(slab, 1);
            maid.playSound(SoundEvents.ENTITY_PLAYER_SPLASH, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getMetadata() != 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getMetadata() == 0) {
            String text = I18n.format("tooltips.touhou_little_maid.smart_slab.maid_name",
                    I18n.format("tooltips.touhou_little_maid.smart_slab.maid_name.unknow"));
            tooltip.add(text);
        }
        if (stack.getMetadata() == 2) {
            String modelId = stack.getTagCompound().getCompoundTag(MAID_INFO_TAG).getString(EntityMaid.NBT.MODEL_ID.getName());
            if (!"".equals(modelId)) {
                CustomResourcesLoader.MAID_MODEL.getInfo(modelId).ifPresent(modelItem ->
                        tooltip.add(I18n.format("tooltips.touhou_little_maid.smart_slab.maid_name",
                                ParseI18n.parse(modelItem.getName()))));
            }
        }

        tooltip.add(I18n.format("tooltips.touhou_little_maid.smart_slab.desc"));
    }
}
