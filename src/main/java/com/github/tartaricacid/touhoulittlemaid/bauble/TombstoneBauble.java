package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.block.BlockTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.util.PlaceHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author TartaricAcid
 * @date 2019/10/2 22:19
 **/
public class TombstoneBauble implements IMaidBauble {
    @Override
    public boolean onDropsPre(AbstractEntityMaid maid, ItemStack baubleItem) {
        World world = maid.world;
        BlockPos pos = maid.getPosition();
        UUID ownerUuid = maid.getOwnerId();
        IItemHandler maidCapability = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // 位置不适合时候不生成墓碑，并且对在线的玩家发出提示
        if (PlaceHelper.notSuitableForPlaceTombstone(world, pos) && ownerUuid != null) {
            applyNotSuitableForPlaceTombstoneLogic(world, ownerUuid);
            return false;
        }

        world.setBlockState(pos, MaidBlocks.TOMBSTONE.getDefaultState().withProperty(BlockTombstone.FACING, maid.getHorizontalFacing()));
        TileEntity te = world.getTileEntity(pos);
        baubleItem.shrink(1);

        if (te instanceof TileEntityTombstone && maidCapability != null) {
            applyTombstoneStorageItemLogic(maidCapability, (TileEntityTombstone) te, maid, ownerUuid);
        }
        return true;
    }

    private void applyNotSuitableForPlaceTombstoneLogic(World world, UUID ownerUuid) {
        Entity owner = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(ownerUuid);
        if (owner instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) owner;
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.tombstone.place_fail"));
            }
        }
    }

    private ItemStack getGarageKitItemStack(AbstractEntityMaid maid) {
        // 先在死亡前获取女仆的 NBT 数据
        maid.hurtResistantTime = 0;
        maid.hurtTime = 0;
        maid.deathTime = 0;
        NBTTagCompound entityTag = new NBTTagCompound();
        maid.writeEntityToNBT(entityTag);
        // 剔除物品部分标签
        entityTag.removeTag("ArmorItems");
        entityTag.removeTag("HandItems");
        entityTag.removeTag(EntityMaid.NBT.MAID_INVENTORY.getName());
        entityTag.removeTag(EntityMaid.NBT.BAUBLE_INVENTORY.getName());
        // 女仆手办
        return BlockGarageKit.getItemStackWithData("touhou_little_maid:entity.passive.maid", maid.getModelId(), entityTag);
    }

    private void applyTombstoneStorageItemLogic(IItemHandler maidCapability, TileEntityTombstone tombstone, AbstractEntityMaid maid, @Nullable UUID ownerUuid) {
        for (int i = 0; i < maidCapability.getSlots(); i++) {
            ItemStack maidItemStack = maidCapability.getStackInSlot(i);
            tombstone.handler.setStackInSlot(i, maidItemStack.copy());
            maidItemStack.setCount(0);
        }
        tombstone.handler.setStackInSlot(maidCapability.getSlots(), getGarageKitItemStack(maid));
        if (ownerUuid != null) {
            GameProfile gameProfile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getProfileByUUID(ownerUuid);
            if (gameProfile != null) {
                tombstone.setOwnerName(gameProfile.getName());
            }
        }
        tombstone.refresh();
        maid.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
    }
}
