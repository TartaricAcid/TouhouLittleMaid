package com.github.tartaricacid.touhoulittlemaid.entity.backpack.data;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class FurnaceBackpackData extends Inventory implements IBackpackData {
    private static final int INPUT_INDEX = 0;
    private static final int FUEL_INDEX = 1;
    private static final int OUTPUT_INDEX = 2;
    private final World level;
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    private final IIntArray dataAccess = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return FurnaceBackpackData.this.litTime;
                case 1:
                    return FurnaceBackpackData.this.litDuration;
                case 2:
                    return FurnaceBackpackData.this.cookingProgress;
                case 3:
                    return FurnaceBackpackData.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    FurnaceBackpackData.this.litTime = value;
                    break;
                case 1:
                    FurnaceBackpackData.this.litDuration = value;
                    break;
                case 2:
                    FurnaceBackpackData.this.cookingProgress = value;
                    break;
                case 3:
                    FurnaceBackpackData.this.cookingTotalTime = value;
                    break;
            }
        }

        public int getCount() {
            return 4;
        }
    };

    public FurnaceBackpackData(EntityMaid maid) {
        super(3);
        this.level = maid.level;
    }

    @Override
    public IIntArray getDataAccess() {
        return dataAccess;
    }

    @Override
    public void load(CompoundNBT tag, EntityMaid maid) {
        this.litTime = tag.getInt("BurnTime");
        this.cookingProgress = tag.getInt("CookTime");
        this.cookingTotalTime = tag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.getItem(FUEL_INDEX));
        this.fromTag(tag.getList("Items", Constants.NBT.TAG_COMPOUND));
    }

    @Override
    public void save(CompoundNBT tag, EntityMaid maid) {
        tag.putInt("BurnTime", this.litTime);
        tag.putInt("CookTime", this.cookingProgress);
        tag.putInt("CookTimeTotal", this.cookingTotalTime);
        tag.put("Items", this.createTag());
    }

    @Override
    public void serverTick(EntityMaid maid) {
        World level = maid.level;
        // 如果是燃烧状态，继续燃烧
        if (this.isLit()) {
            --this.litTime;
        }
        ItemStack fuelItem = this.getItem(FUEL_INDEX);
        boolean inputNotEmpty = !this.getItem(INPUT_INDEX).isEmpty();
        boolean fuelNotEmpty = !fuelItem.isEmpty();
        boolean readyForLit = inputNotEmpty && fuelNotEmpty;
        // 要么正在燃烧，要么具备燃烧条件
        if (this.isLit() || readyForLit) {
            // 获取配方
            FurnaceRecipe recipe = this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).orElse(null);
            int maxStackSize = this.getMaxStackSize();
            // 没有燃烧，但是可以燃！
            if (!this.isLit() && this.canBurn(recipe, this, maxStackSize)) {
                this.litTime = this.getBurnDuration(fuelItem);
                this.litDuration = this.litTime;
                // 如果此时点燃了
                if (this.isLit()) {
                    // 如果燃料有残留物，比如熔岩桶燃烧后残留一个桶
                    if (fuelItem.hasContainerItem()) {
                        this.setItem(FUEL_INDEX, fuelItem.getContainerItem());
                    } else if (fuelNotEmpty) {
                        // 普通燃料减一
                        fuelItem.shrink(1);
                        if (fuelItem.isEmpty()) {
                            this.setItem(FUEL_INDEX, fuelItem.getContainerItem());
                        }
                    }
                }
            }

            // 点燃了，而且也能燃！
            if (this.isLit() && this.canBurn(recipe, this, maxStackSize)) {
                // 各种进度增加
                ++this.cookingProgress;
                // 如果进度满了，重置，并给出产物
                if (this.cookingProgress == this.cookingTotalTime) {
                    this.cookingProgress = 0;
                    this.cookingTotalTime = getTotalCookTime(level);
                    // 如果烧制成功，把经验给女仆
                    if (this.burn(recipe, this, maxStackSize)) {
                        int exp = this.createExperience(recipe.getExperience());
                        maid.setExperience(maid.getExperience() + exp);
                    }
                }
            } else {
                // 否则直接重置烧制进度
                this.cookingProgress = 0;
            }
        } else if (this.cookingProgress > 0) {
            // 什么，燃料不足，那就逐 tick 递减烧制进度
            this.cookingProgress = MathHelper.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack slotItem = this.getItem(index);
        boolean isSameItem = !stack.isEmpty() && ItemStack.tagMatches(slotItem, stack);
        super.setItem(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (index == 0 && !isSameItem) {
            this.cookingTotalTime = getTotalCookTime(this.level);
            this.cookingProgress = 0;
            this.setChanged();
        }
    }

    private int createExperience(float recipeExp) {
        int integer = MathHelper.floor(recipeExp);
        float decimal = MathHelper.frac(recipeExp);
        if (decimal != 0 && Math.random() < (double) decimal) {
            ++integer;
        }
        return integer;
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    private int getBurnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(fuel, IRecipeType.SMELTING);
        }
    }

    private boolean canBurn(@Nullable FurnaceRecipe recipe, Inventory container, int maxStackSize) {
        // 先检查输入物品和配方
        if (!container.getItem(INPUT_INDEX).isEmpty() && recipe != null) {
            // 先检查配方结果
            ItemStack result = recipe.assemble(this);
            // 没结果，不能燃烧
            if (result.isEmpty()) {
                return false;
            } else {
                // 检查输出栏
                ItemStack output = container.getItem(OUTPUT_INDEX);
                if (output.isEmpty()) {
                    // 空的，可以放
                    return true;
                } else if (!output.sameItem(result)) {
                    // 不同物品，不行
                    return false;
                } else if (output.getCount() + result.getCount() <= maxStackSize && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                    // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    // Forge fix: make furnace respect stack sizes in furnace recipes
                    return output.getCount() + result.getCount() <= result.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(@Nullable FurnaceRecipe recipe, Inventory container, int maxStackSize) {
        if (recipe != null && this.canBurn(recipe, container, maxStackSize)) {
            ItemStack input = container.getItem(INPUT_INDEX);
            ItemStack result = recipe.assemble(this);
            ItemStack output = container.getItem(OUTPUT_INDEX);
            // 如果输出栏为空
            if (output.isEmpty()) {
                // 放东西
                container.setItem(OUTPUT_INDEX, result.copy());
            } else if (output.getItem() == result.getItem()) {
                // 相同物品，增长数量即可
                output.grow(result.getCount());
            }
            // 如果是海绵和桶
            if (input.getItem() == Blocks.WET_SPONGE.asItem() && !container.getItem(FUEL_INDEX).isEmpty() && container.getItem(FUEL_INDEX).getItem() == Items.BUCKET) {
                container.setItem(FUEL_INDEX, new ItemStack(Items.WATER_BUCKET));
            }
            input.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    private int getTotalCookTime(World level) {
        return level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }
}
