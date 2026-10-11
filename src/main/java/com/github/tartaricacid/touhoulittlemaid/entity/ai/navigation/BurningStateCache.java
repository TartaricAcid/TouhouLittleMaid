package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class BurningStateCache {
    private static final int MAX_ENTRIES = 4096;
    private static final ThreadLocal<BurningStateCache> LOCAL = ThreadLocal.withInitial(BurningStateCache::new);
    private static volatile Object generation = new Object();
    private static volatile boolean enabled = true;

    private final Reference2ByteOpenHashMap<BlockState> values = new Reference2ByteOpenHashMap<>();
    private Object currentGeneration;
    private boolean computing;

    private BurningStateCache() {
    }

    // Integrations with position-, entity- or time-dependent burning checks must opt out.
    public static void setEnabled(boolean value) {
        enabled = value;
        generation = new Object();
    }

    @Nullable
    public static BurningStateCache get() {
        if (!enabled) {
            return null;
        }
        BurningStateCache cache = LOCAL.get();
        return cache.computing ? null : cache;
    }

    public boolean isBurning(BlockState state) {
        Object epoch = generation;
        if (currentGeneration != epoch) {
            values.clear();
            currentGeneration = epoch;
        }
        byte cached = values.getByte(state);
        if (cached != 0) {
            return cached == 2;
        }
        // Re-enter the original method without caching, preserving other mixins on a miss.
        computing = true;
        boolean result;
        try {
            result = WalkNodeEvaluator.isBurningBlock(state);
        } finally {
            computing = false;
        }
        if (generation == epoch && values.size() < MAX_ENTRIES) {
            values.put(state, (byte) (result ? 2 : 1));
        }
        return result;
    }

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        if (event.shouldUpdateStaticData()) {
            generation = new Object();
        }
    }
}
