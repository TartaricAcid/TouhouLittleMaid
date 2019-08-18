package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/18 18:02
 **/
public class ItemAlbum extends Item {
    private static final int ALBUM_INV_SIZE = 32;

    public ItemAlbum() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".album");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND && playerIn.getHeldItem(handIn).getItem() instanceof ItemAlbum) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.ALBUM.getId(), worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static ItemStackHandler getAlbumInv(ItemStack album) {
        AlbumItemStackHandler handler = new AlbumItemStackHandler(ALBUM_INV_SIZE);
        if (album.getItem() instanceof ItemAlbum) {
            NBTTagCompound tag = album.getTagCompound();
            if (tag != null && tag.hasKey(NBT.INVENTORY.getName())) {
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
}
