package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions;


import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics.FirstOrder;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics.IPhysics;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity.EntityFunction;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.StringUtils;

public class FirstOrderFunction extends EntityFunction {
    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        String key = arguments.getAsString(context, 0);
        if (StringUtils.isBlank(key)) {
            return 0;
        }
        float input = arguments.getAsFloat(context, 1);

        int size = arguments.size();
        float response = 1;
        if (size >= 3) {
            response = arguments.getAsFloat(context, 2);
        }

        var manager = context.entity().geoInstance().getPhysicsManager();
        IPhysics physicsValue = manager.get(key);
        if (physicsValue == null) {
            FirstOrder firstOrder = new FirstOrder(input, response);
            manager.put(key, firstOrder);
            return input;
        }
        physicsValue.setArgs(input, response, 0, 0);
        return physicsValue.getValue();
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 2;
    }
}
