package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.google.common.collect.Multimap;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
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

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (!worldIn.isRemote && entityLiving instanceof EntityPlayer && !entityLiving.isSneaking()) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            Vec3d v = player.getLookVec();
            // 右键有效时长为 0-5 s
            // 右键持续 1 秒 -> a 为 1
            // 右键持续 3 秒 -> a 为 3
            int a = ((500 - timeLeft) > 100 ? 100 : (500 - timeLeft)) / 20;
            int damage = a + 4;
            float velocity = 0.2f * (a + 1);
            DanmakuColor color = DanmakuColor.getColor(random.nextInt(DanmakuColor.getLength() + 1));
            DanmakuType type = getGoheiMode(stack);

            EntityDanmaku danmaku = new EntityDanmaku(worldIn, player, damage, 0, type, color);
            danmaku.shoot(v.x, v.y, v.z, velocity, 5f);
            worldIn.spawnEntity(danmaku);
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(), 1.0f, 0.8f);
            player.getCooldownTracker().setCooldown(this, 10);
            stack.damageItem(1, player);
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = new ItemStack(this);
        setGoheiMode(itemStack, DanmakuType.PELLET);
        return itemStack;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(getDefaultInstance());
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            playerIn.setActiveHand(handIn);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
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
