package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics.IPhysics;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics.SecondOrder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.context.IContext;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.function.entity.EntityFunction;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.ExecutionContext;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.StringUtils;

public class SecondOrderFunction extends EntityFunction {
    @Override
    protected Object eval(ExecutionContext<IContext<Entity>> context, ArgumentCollection arguments) {
        String key = arguments.getAsString(context, 0);
        if (StringUtils.isBlank(key)) {
            return 0;
        }
        float input = arguments.getAsFloat(context, 1);

        int size = arguments.size();
        float frequency = 1, coefficient = 1, response = 1;
        if (size >= 3) {
            frequency = arguments.getAsFloat(context, 2);
        }
        if (size >= 4) {
            coefficient = arguments.getAsFloat(context, 3);
        }
        if (size >= 5) {
            response = arguments.getAsFloat(context, 4);
        }

        var manager = context.entity().geoInstance().getPhysicsManager();
        IPhysics physicsValue = manager.get(key);
        if (physicsValue == null) {
            SecondOrder secondOrder = new SecondOrder(input, frequency, coefficient, response);
            manager.put(key, secondOrder);
            return input;
        }
        physicsValue.setArgs(input, frequency, coefficient, response);
        return physicsValue.getValue();
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 2;
    }
}
