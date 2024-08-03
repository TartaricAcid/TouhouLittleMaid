package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

/**
 * 其他模组作者可以捕获此事件 <br>
 * 调用 setMaid 方法，传入 IMaid 实例 <br>
 * 即可调用女仆渲染
 */
public class ConvertMaidEvent extends Event {
    private final Mob mob;
    private @Nullable IMaid maid;

    public ConvertMaidEvent(Mob mob) {
        this.mob = mob;
    }

    public Mob getEntity() {
        return mob;
    }

    public void setMaid(IMaid maid) {
        this.maid = maid;
    }

    @Nullable
    public IMaid getMaid() {
        return maid;
    }
}
