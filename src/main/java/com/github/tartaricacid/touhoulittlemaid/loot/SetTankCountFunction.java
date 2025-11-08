package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.TANK_BACKPACK_TAG;

public class SetTankCountFunction extends LootItemConditionalFunction {
    public static MapCodec<SetTankCountFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance)
            .and(instance.group(
                    ResourceLocation.CODEC.fieldOf("fluid_id").forGetter(f -> f.fluidId),
                    Codec.INT.fieldOf("count").forGetter(f -> f.count)
            )).apply(instance, SetTankCountFunction::new));

    private final ResourceLocation fluidId;
    private final int count;

    public SetTankCountFunction(List<LootItemCondition> predicates, ResourceLocation fluidId, int count) {
        super(predicates);
        this.fluidId = fluidId;
        this.count = count;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return InitLootModifier.SET_TANK_COUNT_FUNCTION.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        CompoundTag tags = stack.get(TANK_BACKPACK_TAG);
        if (tags == null) {
            tags = new CompoundTag();
        }
        FluidTank tank = new FluidTank(TankBackpackData.CAPACITY);
        FluidStack fluidStack = new FluidStack(BuiltInRegistries.FLUID.get(this.fluidId), count);
        tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        tank.writeToNBT(context.getLevel().registryAccess(), tags);
        stack.set(InitDataComponent.TANK_BACKPACK_TAG, tags);
        return stack;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetTankCountFunction.Builder> {
        private final Fluid fluid;
        private final int bucketCount;

        public Builder(Fluid fluid, int bucketCount) {
            this.fluid = fluid;
            this.bucketCount = bucketCount;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            ResourceLocation key = BuiltInRegistries.FLUID.getKey(fluid);
            return new SetTankCountFunction(this.getConditions(), key, bucketCount * FluidType.BUCKET_VOLUME);
        }
    }
}
