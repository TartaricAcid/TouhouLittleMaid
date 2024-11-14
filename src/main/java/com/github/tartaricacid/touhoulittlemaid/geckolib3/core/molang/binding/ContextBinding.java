package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.IValueEvaluator;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.LambdaVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.block.AbstractBlockVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.block.BlockStateVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.block.BlockVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.entity.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.item.ItemStackVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.variable.item.ItemVariable;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.Function;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ContextBinding implements ObjectBinding {
    private final Object2ReferenceOpenHashMap<String, Object> bindings = new Object2ReferenceOpenHashMap<>();

    @Override
    public Object getProperty(String name) {
        return bindings.get(name);
    }

    public void function(String name, Function function) {
        bindings.put(name, function);
    }

    public void constValue(String name, Object value) {
        bindings.put(name, value);
    }

    public void var(String name, IValueEvaluator<?, IContext<Object>> evaluator) {
        bindings.put(name, new LambdaVariable<>(evaluator));
    }

    public void entityVar(String name, IValueEvaluator<?, IContext<Entity>> evaluator) {
        bindings.put(name, new EntityVariable(evaluator));
    }

    public void livingEntityVar(String name, IValueEvaluator<?, IContext<LivingEntity>> evaluator) {
        bindings.put(name, new LivingEntityVariable(evaluator));
    }

    public void mobEntityVar(String name, IValueEvaluator<?, IContext<Mob>> evaluator) {
        bindings.put(name, new MobEntityVariable(evaluator));
    }

    public void tamableEntityVar(String name, IValueEvaluator<?, IContext<TamableAnimal>> evaluator) {
        bindings.put(name, new TamableEntityVariable(evaluator));
    }

    public void maidEntityVar(String name, IValueEvaluator<?, IContext<EntityMaid>> evaluator) {
        bindings.put(name, new MaidEntityVariable(evaluator));
    }

    public void itemVar(String name, IValueEvaluator<?, IContext<Item>> evaluator) {
        bindings.put(name, new ItemVariable(evaluator));
    }

    public void itemStackVar(String name, IValueEvaluator<?, IContext<ItemStack>> evaluator) {
        bindings.put(name, new ItemStackVariable(evaluator));
    }

    public void blockStateVar(String name, IValueEvaluator<?, IContext<BlockState>> evaluator) {
        bindings.put(name, new BlockStateVariable(evaluator));
    }

    public void blockVar(String name, IValueEvaluator<?, IContext<Block>> evaluator) {
        bindings.put(name, new BlockVariable(evaluator));
    }

    public void abstractBlockVar(String name, IValueEvaluator<?, IContext<BlockBehaviour>> evaluator) {
        bindings.put(name, new AbstractBlockVariable(evaluator));
    }
}
