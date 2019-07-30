package com.github.tartaricacid.touhoulittlemaid.tileentity;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityGrid extends TileEntity {

    public final ItemStackHandler handler = new ItemStackHandler(9) {
        public int getSlotLimit(int slot) {
            return 1;
        };
    };

    public boolean input = true;
    public boolean blacklist = false;
    private Mode mode = Mode.UNKNOWN;
    private ItemStack craftingResult = ItemStack.EMPTY;
    private List<ItemStack> remainingItems = Collections.EMPTY_LIST;

    public void refresh() {
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, -1, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public final void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Nonnull
    @Override
    public final NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        handler.deserializeNBT(compound);
        blacklist = compound.getBoolean("Blacklist");
        input = compound.getBoolean("Input");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.merge(handler.serializeNBT());
        compound.setBoolean("Blacklist", blacklist);
        compound.setBoolean("Input", input);
        return super.writeToNBT(compound);
    }

    public boolean interact(IItemHandlerModifiable items, AbstractEntityMaid maid, boolean simulate) {
        if (mode == Mode.UNKNOWN) {
            updateMode(null);
        }
        switch (mode) {
        default:
        case UNKNOWN:
            return false;
        case ITEM_IO:
            return interactItemIO(items, maid, simulate);
        case CRAFTING:
            return interactCrafting(items, maid, simulate);
        }
    }

    public ItemStack getItem(IItemHandler inv) {
        outer:
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            for (int j = 0; j < 9; j++) {
                ItemStack filterItem = handler.getStackInSlot(j);
                if (filterItem.isEmpty()) {
                    continue;
                }
                if (itemMatches(stack, filterItem, true)) {
                    if (blacklist) {
                        continue outer;
                    }
                    else {
                        return stack;
                    }
                }
            }
            if (blacklist) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean interactItemIO(IItemHandlerModifiable inv, AbstractEntityMaid maid, boolean simulate) {
        IBlockState state = getBlockType().getStateFromMeta(getBlockMetadata());
        EnumFacing facing = state.getValue(BlockGrid.DIRECTION).face;
        BlockPos offsetPos = pos.offset(facing.getOpposite());
        TileEntity tile = world.getTileEntity(offsetPos);
        if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            return false;
        }
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
        IItemHandler src = input ? inv : itemHandler;
        IItemHandler dest = input ? itemHandler : inv;
        ItemStack stack = getItem(src);
        if (stack.isEmpty()) {
            return false;
        }
        ItemStack remain = ItemHandlerHelper.insertItemStacked(dest, stack.copy(), simulate);
        if (stack.getCount() != remain.getCount()) {
            if (!simulate) {
                if (input) {
                    stack.setCount(remain.getCount());
                }
                else {
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack s = itemHandler.getStackInSlot(i);
                        if (stack == s) {
                            int amount = stack.getCount() - remain.getCount();
                            itemHandler.extractItem(i, amount, false);
                            break;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean interactCrafting(IItemHandlerModifiable inv, AbstractEntityMaid maid, boolean simulate) {
        ItemStack result = getCraftingResult();
        if (result.isEmpty()) {
            return false;
        }

        // 计算单次所需材料
        List<ItemStack> ingredients = Lists.newArrayList();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                boolean flag = false;
                for (int j = 0; j < ingredients.size(); j++) {
                    if (itemMatches(stack, ingredients.get(j), false)) {
                        ingredients.get(j).grow(1);
                        flag = true;
                    }
                }
                if (!flag) {
                    ingredients.add(stack.copy());
                }
            }
        }
        if (ingredients.isEmpty()) {
            return false;
        }

        // 计算背包中匹配材料总数
        boolean hasEmptySlot = false;
        int sameCount = 0;
        Object2IntMap<ItemStack> ingredientsCount = new Object2IntArrayMap();
        Multimap<ItemStack, ItemStack> matchedItems = HashMultimap.create();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                hasEmptySlot = true;
                continue;
            }
            if (itemMatches(stack, result, false)) {
                sameCount += result.getMaxStackSize() - stack.getCount();
            }
            for (int j = 0; j < ingredients.size(); j++) {
                ItemStack ingredient = ingredients.get(j);
                if (itemMatches(stack, ingredient, false)) {
                    matchedItems.put(ingredient, stack);
                    int c0 = ingredientsCount.getOrDefault(ingredient, 0);
                    ingredientsCount.put(ingredient, c0 + stack.getCount());
                    break;
                }
            }
        }

        if (ingredients.size() != matchedItems.size()) {
            return false;
        }

        // 计算合成最大完成次数
        int expectedCount = result.getMaxStackSize();
        for (Entry<ItemStack, Integer> entry : ingredientsCount.entrySet()) {
            int count = entry.getValue() / entry.getKey().getCount();
            expectedCount = Math.min(expectedCount, count);
        }
        if (expectedCount == 0) {
            return false;
        }
        if (!hasEmptySlot) {
            for (ItemStack stack : ingredientsCount.keySet()) {
                int newCount = expectedCount * stack.getCount();
                for (ItemStack s : matchedItems.get(stack)) {
                    if (newCount >= s.getCount()) {
                        hasEmptySlot = true;
                        break;
                    }
                }
            }
        }
        int minCount = expectedCount;
        if (!hasEmptySlot) {
            minCount = Math.min(sameCount, minCount);
        }
        if (minCount == 0) {
            return false;
        }

        if (simulate) {
            return true;
        }

        // 先清空合成材料，以免塞不进去
        for (ItemStack stack : ingredientsCount.keySet()) {
            int newCount = minCount * stack.getCount();
            ingredientsCount.put(stack, newCount);
        }

        // 优先清除数量少的stack
        for (ItemStack ingredient : matchedItems.keySet()) {
            int count = ingredientsCount.getInt(ingredient);
            List<ItemStack> stacks = Lists.newArrayList(matchedItems.get(ingredient));
            Collections.sort(stacks, (a, b) -> a.getCount() - b.getCount());
            for (ItemStack stack : stacks) {
                int shrink = Math.min(stack.getCount(), count);
                stack.shrink(shrink);
                count -= shrink;
                if (count <= 0) {
                    break;
                }
            }
        }

        result = result.copy();
        result.setCount(minCount);
        ItemHandlerHelper.insertItemStacked(inv, result, false);
        for (ItemStack stack : remainingItems) {
            if (!stack.isEmpty()) {
                stack = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() * minCount);
                while (!stack.isEmpty()) {
                    ItemStack split = stack.splitStack(stack.getMaxStackSize());
                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, split, false);
                    if (!remainder.isEmpty() && !world.isRemote) {
                        EntityItem entityitem = new EntityItem(world, maid.posX, maid.posY + 0.5, maid.posZ, remainder);
                        entityitem.setPickupDelay(40);
                        entityitem.motionX = 0;
                        entityitem.motionZ = 0;
                        world.spawnEntity(entityitem);
                    }
                }
            }
        }
        return true;
    }

    private ItemStack getCraftingResult() {
        if (craftingResult.isEmpty()) {
            Container container = new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) {
                    return false;
                }
            };
            InventoryCrafting inventoryCrafting = new InventoryCrafting(container, 3, 3);
            boolean empty = true;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    empty = false;
                }
                inventoryCrafting.setInventorySlotContents(i, stack);
            }
            if (!empty) {
                IRecipe recipe = CraftingManager.findMatchingRecipe(inventoryCrafting, world);
                if (recipe != null) {
                    craftingResult = recipe.getCraftingResult(inventoryCrafting);
                    remainingItems = recipe.getRemainingItems(inventoryCrafting);
                }
            }
        }
        return craftingResult;
    }

    public void clearCraftingResult() {
        craftingResult = ItemStack.EMPTY;
        remainingItems = Collections.EMPTY_LIST;
    }

    private static boolean itemMatches(ItemStack stackA, ItemStack stackB, boolean ignoreNBT) {
        return ItemStack.areItemsEqual(stackA, stackB) && (ignoreNBT || ItemStack.areItemStackTagsEqual(stackA, stackB));
    }

    public Mode updateMode(@Nullable IBlockState state) {
        if (state == null) {
            state = getBlockType().getStateFromMeta(getBlockMetadata());
        }
        EnumFacing facing = state.getValue(BlockGrid.DIRECTION).face;
        BlockPos offsetPos = pos.offset(facing.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);
        if (offsetState.getBlock() == Blocks.CRAFTING_TABLE) {
            return mode = Mode.CRAFTING;
        }
        if (OreDictionary.doesOreNameExist("workbench")) {
            ItemStack stack = offsetState.getBlock().getItem(world, offsetPos, offsetState);
            int id = OreDictionary.getOreID("workbench");
            if (!stack.isEmpty() && ArrayUtils.contains(OreDictionary.getOreIDs(stack), id)) {
                return mode = Mode.CRAFTING;
            }
        }
        TileEntity tile = world.getTileEntity(offsetPos);
        if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            return mode = Mode.ITEM_IO;
        }
        return mode = Mode.UNKNOWN;
    }

    public static enum Mode {
        UNKNOWN, ITEM_IO, CRAFTING
    }
}
