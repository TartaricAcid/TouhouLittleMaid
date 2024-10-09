package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.data.inner.TestData;
import net.minecraft.resources.ResourceLocation;

public final class InitTaskData {
    public static TaskDataKey<TestData> TEST;

    public static void registerAll(TaskDataRegister register) {
        TEST = register.register(new ResourceLocation(TouhouLittleMaid.MOD_ID, "test"), TestData.CODEC);
    }
}