package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.variable;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.AssignableVariable;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;

public class TempVariableBinding implements ObjectBinding {
    private final Object2ReferenceMap<String, TempVariable> variableMap = new Object2ReferenceOpenHashMap<>();
    private int topPointer = 0;

    @Override
    public Object getProperty(String name) {
        return variableMap.computeIfAbsent(name, k -> new TempVariable(topPointer++));
    }

    public void reset() {
        variableMap.clear();
        topPointer = 0;
    }

    private record TempVariable(int address) implements AssignableVariable {
        @Override
        @SuppressWarnings("unchecked")
        public Object evaluate(final @NotNull ExecutionContext<?> context) {
            return ((IContext<Object>) context.entity()).tempStorage().getTemp(address);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void assign(@NotNull ExecutionContext<?> context, Object value) {
            ((IContext<Object>) context.entity()).tempStorage().setTemp(address, value);
        }
    }
}
