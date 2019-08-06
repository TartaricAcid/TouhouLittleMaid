package com.github.tartaricacid.touhoulittlemaid.tileentity;

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
import net.minecraft.init.SoundEvents;
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
import net.minecraftforge.items.*;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class TileEntityGrid extends TileEntity {
    public final ItemStackHandler handler = new ItemStackHandler(9) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public boolean input = true;
    public boolean blacklist = false;
    private Mode mode = Mode.UNKNOWN;
    private ItemStack craftingResult = ItemStack.EMPTY;
    private List<ItemStack> remainingItems = Collections.EMPTY_LIST;

    /**
     * 比较；两个 ItemStack 是否相等
     *
     * @param ignoreNBT 比较时是否忽略 NBT
     */
    private static boolean itemMatches(ItemStack stackA, ItemStack stackB, boolean ignoreNBT) {
        return ItemStack.areItemsEqual(stackA, stackB) && (ignoreNBT || ItemStack.areItemStackTagsEqual(stackA, stackB));
    }

    /**
     * 用于刷新方块信息，通知 world 进行数据存储
     */
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

    /**
     * 尝试进行交互
     *
     * @return true -> 交互成功；false -> 交互失败
     */
    public boolean interact(IItemHandlerModifiable items, AbstractEntityMaid maid, boolean simulate) {
        if (mode == Mode.UNKNOWN) {
            updateMode(null);
        }
        switch (mode) {
            case UNKNOWN:
                return false;
            case ITEM_IO:
                return interactItemIO(items, maid, simulate);
            case CRAFTING:
                return interactCrafting(items, maid, simulate);
            default:
                return false;
        }
    }

    /**
     * 比较某物品栏和 grid 的物品，进行匹配
     *
     * @param inv 某物品栏
     * @return 匹配的第一个对象，如果不匹配，返回 ItemStack.EMPTY
     */
    public ItemStack getItem(IItemHandler inv) {
        outer:
        // 首先遍历该物品栏
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            // 空物品跳过
            if (stack.isEmpty()) {
                continue;
            }
            // 而后遍历 grid 上的物品
            for (int j = 0; j < 9; j++) {
                ItemStack filterItem = handler.getStackInSlot(j);
                // 空物品跳过
                if (filterItem.isEmpty()) {
                    continue;
                }
                // 对比两者是否匹配
                if (itemMatches(stack, filterItem, true)) {
                    if (blacklist) {
                        // 如果是黑名单模式，匹配符合说明该物品不能被选择
                        // 跳到最外层进行重新遍历
                        continue outer;
                    } else {
                        // 白名单模型，说明找到了合适的物品
                        return stack;
                    }
                }
            }
            // 如果上述遍历 gird 的操作都没有找到物品，而且现在还是黑名单模式
            // 说明第一个物品就可以选择
            if (blacklist) {
                return stack;
            }
        }
        // 什么都没有找到，返回空
        return ItemStack.EMPTY;
    }

    /**
     * 处理物品的输入输出
     *
     * @param inv      输入输出的物品栏
     * @param maid     女仆
     * @param simulate 是否是模拟操作
     */
    private boolean interactItemIO(IItemHandlerModifiable inv, AbstractEntityMaid maid, boolean simulate) {
        // 基本数据的获取
        IBlockState state = getBlockType().getStateFromMeta(getBlockMetadata());
        EnumFacing facing = state.getValue(BlockGrid.DIRECTION).face;
        BlockPos offsetPos = pos.offset(facing.getOpposite());
        TileEntity tile = world.getTileEntity(offsetPos);
        // 空 tileEntity 或无 Capability 返回
        if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
            return false;
        }
        // 获取指定方向的 ITEM_HANDLER_CAPABILITY
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

        // 依据当前是输入还是输出模型，设定目标物品栏和源物品栏
        IItemHandler src = input ? inv : itemHandler;
        IItemHandler dest = input ? itemHandler : inv;

        // 开始对物品栏中的物品和标记物品进行匹配
        ItemStack matchedStack = getItem(src);
        if (matchedStack.isEmpty()) {
            return false;
        }

        // 开始尝试插入物品，获取插入后剩余的物品堆
        ItemStack remainStack = ItemHandlerHelper.insertItemStacked(dest, matchedStack.copy(), simulate);
        // 如果两者不相等，说明成功插入了
        if (matchedStack.getCount() != remainStack.getCount()) {
            // 非模拟状态下，就需要进行对应物品扣除和音效的播放
            if (!simulate) {
                maid.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.5f, 1);
                if (input) {
                    // 如果是输入模式，将匹配的物品数量进行扣除
                    matchedStack.setCount(remainStack.getCount());
                } else {
                    // 如果是输出模式，则需要对容器里面的物品进行扣除
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack s = itemHandler.getStackInSlot(i);
                        if (matchedStack == s) {
                            int amount = matchedStack.getCount() - remainStack.getCount();
                            // 通过 extractItem 方法进行物品的扣除
                            // 比如某个容器的物品是可以无限取出的，那么如果用前面的 setCount 方法
                            // 就会导致无限取出失效
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
                for (ItemStack ingredient : ingredients) {
                    if (itemMatches(stack, ingredient, false)) {
                        ingredient.grow(1);
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
            for (ItemStack ingredient : ingredients) {
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
            stacks.sort(Comparator.comparingInt(ItemStack::getCount));
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
        result.setCount(result.getCount() * minCount);
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
        maid.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.5f, 1);
        return true;
    }

    private ItemStack getCraftingResult() {
        if (craftingResult.isEmpty()) {
            Container container = new Container() {
                @Override
                public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
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

    public enum Mode {
        // 未知模式
        UNKNOWN,
        // 物品 IO 模式
        ITEM_IO,
        // 物品合成模式
        CRAFTING
    }
}
