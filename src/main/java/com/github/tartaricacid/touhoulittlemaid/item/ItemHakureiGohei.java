package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ItemHakureiGohei extends Item {
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
        List<IMultiBlock> multiBlockList = LittleMaidAPI.getMultiBlockList();
        IBlockState blockState = worldIn.getBlockState(pos);
        for (IMultiBlock multiBlock : multiBlockList) {
            boolean baseConditionIsOkay = hand == EnumHand.MAIN_HAND;
            boolean multiBlockIsOkay = multiBlock.blockIsSuitable(blockState) && multiBlock.facingIsSuitable(facing);
            if (baseConditionIsOkay && multiBlockIsOkay) {
                if (!worldIn.isRemote) {
                    BlockPos posStart = pos.add(multiBlock.getCenterPos(facing));
                    Template altarTemplate = multiBlock.getTemplate(worldIn, facing);
                    if (multiBlock.isMatch(worldIn, posStart, facing, altarTemplate)) {
                        multiBlock.build(worldIn, posStart, facing, altarTemplate);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer && !entityLiving.isSneaking()) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (player.getHeldItemOffhand().getItem() == MaidItems.SPELL_CARD) {
                spellCardShoot(worldIn, player);
            } else {
                normalShoot(stack, worldIn, player, timeLeft);
            }
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(), 1.0f, 0.8f);
            stack.damageItem(1, player);
        }
    }

    private void spellCardShoot(World worldIn, EntityPlayer player) {
        try {
            CustomSpellCardEntry entry = ItemSpellCard.getCustomSpellCardEntry(player.getHeldItemOffhand());
            CommonProxy.NASHORN.eval(entry.getScript());
            Invocable invocable = (Invocable) CommonProxy.NASHORN;
            invocable.invokeFunction("spell_card", worldIn, player);
            player.getCooldownTracker().setCooldown(this, entry.getCooldown());
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
    }

    private void normalShoot(ItemStack stack, World worldIn, EntityPlayer player, int timeLeft) {
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
        danmaku.setPosition(danmaku.posX + player.getLookVec().x,
                danmaku.posY + player.getLookVec().y,
                danmaku.posZ + player.getLookVec().z);
        danmaku.shoot(v.x, v.y, v.z, velocity, 5f);
        worldIn.spawnEntity(danmaku);
        player.getCooldownTracker().setCooldown(this, 11 - a);
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
        tooltip.add(I18n.format("tooltips.touhou_little_maid.hakurei_gohei.desc",
                I18n.format("danmaku_type.touhou_little_maid."
                        + getGoheiMode(stack).name().toLowerCase(Locale.US))));
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    public DanmakuType getGoheiMode(ItemStack stack) {
        return DanmakuType.getType(getTagCompoundSafe(stack).getInteger(NBT.MODE.getName()));
    }

    public void setGoheiMode(ItemStack stack, DanmakuType mode) {
        getTagCompoundSafe(stack).setInteger(NBT.MODE.getName(), mode.ordinal());
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
