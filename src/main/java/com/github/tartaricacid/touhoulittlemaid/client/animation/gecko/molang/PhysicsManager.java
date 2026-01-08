package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.physics.IPhysics;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import org.jetbrains.annotations.Nullable;

public class PhysicsManager {
    private final Object2ReferenceOpenHashMap<String, IPhysics> physicsValues = new Object2ReferenceOpenHashMap<>(16);
    private float lastRenderTicks = 0;

    public void update(float renderTicks) {
        if (lastRenderTicks > 0) {
            if (renderTicks > lastRenderTicks) {
                float interval = (renderTicks - lastRenderTicks) / 20f;
                lastRenderTicks = renderTicks;
                physicsValues.forEach((key, value) -> value.update(interval));
            }
        } else {
            lastRenderTicks = renderTicks;
        }
    }

    public void put(String key, IPhysics physics) {
        this.physicsValues.put(key, physics);
    }

    @Nullable
    public IPhysics get(String key) {
        return this.physicsValues.get(key);
    }

    public void reset() {
        lastRenderTicks = 0;
        physicsValues.clear();
    }
}
