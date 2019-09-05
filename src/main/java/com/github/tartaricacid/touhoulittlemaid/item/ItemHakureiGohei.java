package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemHakureiGohei extends Item {
    private static final ResourceLocation ALTAR_SOUTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_south");
    private static final ResourceLocation ALTAR_NORTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_north");
    private static final ResourceLocation ALTAR_EAST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_east");
    private static final ResourceLocation ALTAR_WEST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_west");
    private static final BlockPos SOUTH_POS = new BlockPos(-4, -3, 0);
    private static final BlockPos NORTH_POS = new BlockPos(-3, -3, -7);
    private static final BlockPos EAST_POS = new BlockPos(0, -3, -3);
    private static final BlockPos WEST_POS = new BlockPos(-7, -3, -4);
    private static Random random = new Random();
    private double attackDamage;
    private double attackSpeed;

    public ItemHakureiGohei() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".hakurei_gohei");
        setMaxStackSize(1);
        setMaxDamage(300);
        setCreativeTab(MaidItems.TABS);
        this.attackDamage = 4;
        this.attackSpeed = -2;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState blockState = worldIn.getBlockState(pos);
        if (blockState.getBlock() == Blocks.WOOL && Blocks.WOOL.getMetaFromState(blockState) == EnumDyeColor.RED.getMetadata() && hand == EnumHand.MAIN_HAND) {
            boolean facingIsSuitable = facing != EnumFacing.DOWN && facing != EnumFacing.UP;
            if (!worldIn.isRemote && facingIsSuitable) {
                applyBuildAltarLogic(worldIn, pos.add(getAltarCenterPos(facing)), facing);
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    private void applyBuildAltarLogic(World worldIn, BlockPos posStart, EnumFacing facing) {
        Template altarTemplate = getAltarTemplate(worldIn, facing);
        if (areTemplateEqualWorldBlock(worldIn, posStart, altarTemplate)) {
            List<BlockPos> posList = Lists.newArrayList();
            List<BlockPos> canPlaceItemPosList = Lists.newArrayList();
            for (Template.BlockInfo blockInfo : altarTemplate.blocks) {
                posList.add(posStart.add(blockInfo.pos));
                if (blockInfo.pos.getY() == 2 && blockInfo.blockState.equals(Blocks.LOG.getDefaultState())) {
                    canPlaceItemPosList.add(blockInfo.pos);
                }
            }
            for (Template.BlockInfo blockInfo : altarTemplate.blocks) {
                BlockPos currentPos = posStart.add(blockInfo.pos);
                worldIn.setBlockState(currentPos, MaidBlocks.ALTAR.getDefaultState());
                TileEntity altarTileEntity = worldIn.getTileEntity(currentPos);
                if (altarTileEntity instanceof TileEntityAltar) {
                    if (currentPos.equals(posStart.subtract(getAltarCenterPos(facing)))) {
                        ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, true,
                                false, facing, posList, canPlaceItemPosList);
                    } else if (blockInfo.pos.getY() == 2 && blockInfo.blockState.equals(Blocks.LOG.getDefaultState())) {
                        ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, false,
                                true, facing, posList, canPlaceItemPosList);
                    } else {
                        ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, false,
                                false, facing, posList, canPlaceItemPosList);
                    }
                }
            }
        }
    }

    private BlockPos getAltarCenterPos(EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                return NORTH_POS;
            case NORTH:
                return SOUTH_POS;
            case EAST:
                return WEST_POS;
            case WEST:
                return EAST_POS;
            default:
                return NORTH_POS;
        }
    }

    private Template getAltarTemplate(World worldIn, EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                return getAltarTemplate(worldIn, ALTAR_NORTH);
            case NORTH:
                return getAltarTemplate(worldIn, ALTAR_SOUTH);
            case EAST:
                return getAltarTemplate(worldIn, ALTAR_WEST);
            case WEST:
                return getAltarTemplate(worldIn, ALTAR_EAST);
            default:
                return getAltarTemplate(worldIn, ALTAR_NORTH);
        }
    }

    private Template getAltarTemplate(World worldIn, ResourceLocation resourceLocation) {
        return worldIn.getSaveHandler().getStructureTemplateManager().getTemplate(worldIn.getMinecraftServer(), resourceLocation);
    }

    private boolean areTemplateEqualWorldBlock(World worldIn, BlockPos posStart, Template template) {
        for (Template.BlockInfo blockInfo : template.blocks) {
            if (worldIn.getBlockState(posStart.add(blockInfo.pos)).equals(blockInfo.blockState)) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer && !entityLiving.isSneaking()) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            Vec3d v = player.getLookVec();
            PowerHandler powerHandler = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
            int power = powerHandler == null ? 0 : MathHelper.floor(powerHandler.get());

            // 依据右键时长，Power 数来决定伤害和发射速度
            // 右键有效时长为 0-5 s
            // Power 有效范围 0-5
            // a = 右键时长增益（0-5）+ Power 数（0-5）
            int a = (((500 - timeLeft) > 100 ? 100 : (500 - timeLeft)) / 20) + power;
            int damage = a + 4;
            float velocity = 0.2f * (a + 1);
            DanmakuColor color = DanmakuColor.getColor(random.nextInt(DanmakuColor.getLength() + 1));
            DanmakuType type = getGoheiMode(stack);

            EntityDanmaku danmaku = new EntityDanmaku(worldIn, player, damage, 0, type, color);
            danmaku.shoot(v.x, v.y, v.z, velocity, 5f);
            worldIn.spawnEntity(danmaku);
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(), 1.0f, 0.8f);
            player.getCooldownTracker().setCooldown(this, 11 - a);
            stack.damageItem(1, player);
        }
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    public ItemStack getDefaultItemStack() {
        ItemStack itemStack = new ItemStack(this);
        setGoheiMode(itemStack, DanmakuType.PELLET);
        return itemStack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(getDefaultItemStack());
        }
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
        }

        return multimap;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 500;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (!worldIn.isRemote) {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.hakurei_gohei.switch_mode.desc"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.hakurei_gohei.desc", I18n.format("danmaku_type.touhou_little_maid." + getGoheiMode(stack).getName())));
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    public DanmakuType getGoheiMode(ItemStack stack) {
        return DanmakuType.getType(getTagCompoundSafe(stack).getInteger(NBT.MODE.getName()));
    }

    public void setGoheiMode(ItemStack stack, DanmakuType mode) {
        getTagCompoundSafe(stack).setInteger(NBT.MODE.getName(), mode.getIndex());
    }

    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }

    private enum NBT {
        // 御币的模式
        MODE("GoheiMode");
        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
