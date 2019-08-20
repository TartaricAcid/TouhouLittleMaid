package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/18 18:02
 **/
public class ItemAlbum extends Item {
    public static final int ALBUM_INV_SIZE = 32;

    public ItemAlbum() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".album");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    public static ItemStackHandler getAlbumInv(ItemStack album) {
        AlbumItemStackHandler handler = new AlbumItemStackHandler(ALBUM_INV_SIZE);
        if (album.getItem() instanceof ItemAlbum) {
            NBTTagCompound tag = album.getTagCompound();
            if (tag != null && tag.hasKey(NBT.INVENTORY.getName(), Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound tagInv = tag.getCompoundTag(NBT.INVENTORY.getName());
                handler.deserializeNBT(tagInv);
            }
        }
        return handler;
    }

    public static void setAlbumInv(ItemStack album, ItemStackHandler itemStackHandler) {
        if (album.getItem() instanceof ItemAlbum) {
            NBTTagCompound tag = album.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(NBT.INVENTORY.getName(), itemStackHandler.serializeNBT());
            album.setTagCompound(tag);
        }
    }

    public static int getAlbumPhotoNum(ItemStack album) {
        if (album.getItem() instanceof ItemAlbum && album.hasTagCompound() &&
                album.getTagCompound().hasKey(NBT.INVENTORY.getName(), Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound tag = album.getTagCompound().getCompoundTag(NBT.INVENTORY.getName());
            if (tag.hasKey("Size", Constants.NBT.TAG_INT) && tag.hasKey("Items", Constants.NBT.TAG_LIST)) {
                return tag.getTagList("Items", Constants.NBT.TAG_COMPOUND).tagCount();
            }
        }
        return 0;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() instanceof ItemAlbum && player.getHeldItem(hand).hasTagCompound() &&
                player.getHeldItem(hand).getTagCompound().hasKey(NBT.INVENTORY.getName(), Constants.NBT.TAG_COMPOUND)) {
            ItemStack album = player.getHeldItem(hand);
            ItemStackHandler itemStackHandler = getAlbumInv(album);
            ItemStack photo = ItemFindUtil.getStack(itemStackHandler, (stack) -> stack.getItem() instanceof ItemPhoto);
            if (photo != ItemStack.EMPTY && ItemPhoto.onPhotoUse(player, worldIn, pos, facing, hitX, hitY, hitZ, photo) == EnumActionResult.SUCCESS) {
                setAlbumInv(album, itemStackHandler);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND && playerIn.getHeldItem(handIn).getItem() instanceof ItemAlbum) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.ALBUM.getId(), worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int photoNum = getAlbumPhotoNum(stack);
        if (photoNum > 0) {
            tooltip.add(I18n.format("tooltips.touhou_little_maid.album.desc", photoNum, ALBUM_INV_SIZE));
        }
    }

    enum NBT {
        // 内存存储
        INVENTORY("AlbumInventory");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class AlbumItemStackHandler extends ItemStackHandler {
        private AlbumItemStackHandler(int size) {
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof ItemPhoto;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            // 物品合法才允许插入，否则原样返回
            if (isItemValid(slot, stack)) {
                return super.insertItem(slot, stack, simulate);
            } else {
                return stack;
            }
        }
    }
}
