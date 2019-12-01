package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author TartaricAcid
 * @date 2019/12/1 14:08
 **/
public class ItemDebugDanmaku extends Item {
    private static final String TYPE_TAG_NAME = "DanmakuType";
    private static final Random RANDOM = new Random();

    public ItemDebugDanmaku() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".debug_danmaku");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        ItemStack stack = new ItemStack(this);
        addType(stack);
        if (this.isInCreativeTab(tab)) {
            items.add(stack);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND) {
            ItemStack stack = playerIn.getHeldItemMainhand();
            if (playerIn.isSneaking()) {
                addType(stack);
                if (!worldIn.isRemote) {
                    String typeKey = String.format("danmaku_type.touhou_little_maid.%s", getType(stack).name().toLowerCase(Locale.US));
                    playerIn.sendStatusMessage(new TextComponentTranslation("message.touhou_little_maid.debug_danmaku.type",
                            new TextComponentTranslation(typeKey)), true);
                }
            } else {
                DanmakuType type = getType(stack);
                DanmakuColor color = DanmakuColor.getColor(RANDOM.nextInt(DanmakuColor.getLength() + 1));
                if (!worldIn.isRemote) {
                    shootDanmaku(worldIn, playerIn, type, color);
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void shootDanmaku(World world, EntityPlayer player, DanmakuType type, DanmakuColor color) {
        Vec3d v = player.getLookVec();
        EntityDanmaku danmaku = new EntityDanmaku(world, player, 2, 0, type, color);
        danmaku.setPosition(danmaku.posX + v.x, danmaku.posY + v.y, danmaku.posZ + v.z);
        danmaku.shoot(v.x, v.y, v.z, 0.1f, 0f);
        world.spawnEntity(danmaku);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, player.getSoundCategory(), 1.0f, 0.8f);
    }

    @SuppressWarnings("all")
    private static void addType(ItemStack stack) {
        NBTTagCompound tag;
        if (stack.hasTagCompound()) {
            tag = stack.getTagCompound();
            if (tag.hasKey(TYPE_TAG_NAME)) {
                tag.setInteger(TYPE_TAG_NAME, (tag.getInteger(TYPE_TAG_NAME) + 1) % DanmakuType.values().length);
            } else {
                tag.setInteger(TYPE_TAG_NAME, 0);
            }
        } else {
            tag = new NBTTagCompound();
            tag.setInteger(TYPE_TAG_NAME, 0);
        }
        stack.setTagCompound(tag);
    }

    @SuppressWarnings("all")
    private static DanmakuType getType(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TYPE_TAG_NAME)) {
            int index = stack.getTagCompound().getInteger(TYPE_TAG_NAME);
            return DanmakuType.getType(index);
        } else {
            return DanmakuType.PELLET;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.debug_danmaku.desc"));
        tooltip.add(I18n.format("message.touhou_little_maid.debug_danmaku.type",
                I18n.format(String.format("danmaku_type.touhou_little_maid.%s", getType(stack).name().toLowerCase(Locale.US)))));
    }
}
