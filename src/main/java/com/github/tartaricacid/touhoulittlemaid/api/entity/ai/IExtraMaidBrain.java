package com.github.tartaricacid.touhoulittlemaid.api.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

import java.util.Collections;
import java.util.List;

public interface IExtraMaidBrain {
    /**
     * 为女仆 AI 添加新的 MemoryModuleType
     */
    default List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return Collections.emptyList();
    }

    /**
     * 为女仆 AI 添加新的 SensorType
     */
    default List<SensorType<? extends Sensor<? super EntityMaid>>> getExtraSensorTypes() {
        return Collections.emptyList();
    }
}
