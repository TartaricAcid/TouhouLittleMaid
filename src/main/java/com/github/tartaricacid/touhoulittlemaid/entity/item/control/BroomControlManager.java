package com.github.tartaricacid.touhoulittlemaid.entity.item.control;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IBroomControl;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public final class BroomControlManager {
    private static List<IBroomControl.Factory> FACTORIES = Lists.newArrayList();

    private BroomControlManager() {
    }

    public static void init() {
        BroomControlManager manager = new BroomControlManager();
        manager.register(PlayerBroomControl::new);
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerBroomControl(manager);
        }
        FACTORIES = ImmutableList.copyOf(FACTORIES);
    }

    public static List<IBroomControl> onBroomInit(EntityBroom broom) {
        List<IBroomControl> controls = Lists.newArrayList();
        for (IBroomControl.Factory factory : FACTORIES) {
            IBroomControl control = factory.create(broom);
            controls.add(control);
        }
        controls.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
        return controls;
    }

    public void register(IBroomControl.Factory factory) {
        FACTORIES.add(factory);
    }
}
