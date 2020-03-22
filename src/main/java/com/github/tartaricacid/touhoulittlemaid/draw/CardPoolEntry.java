package com.github.tartaricacid.touhoulittlemaid.draw;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.ToIntFunction;

public final class CardPoolEntry {
    private String name;
    private int baseWeight;
    private Map<String, Integer> card = Maps.newHashMap();
    private ToIntFunction<Context> metaphysics;

    private CardPoolEntry() {
    }

    public static CardPoolEntry createEntry(String name, int baseWeight) {
        CardPoolEntry entry = new CardPoolEntry();
        entry.name = name;
        entry.baseWeight = baseWeight;
        return entry;
    }

    public void addCard(String modelId, int weight) {
        this.card.put(modelId, weight);
    }

    public CardPoolEntry addMetaphysicsWeight(ToIntFunction<Context> metaphysics) {
        this.metaphysics = metaphysics;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getLangKey() {
        return String.format("draw.touhou_little_maid.card_pool.%s", name);
    }

    public int getBaseWeight() {
        return baseWeight;
    }

    public int getActuallyWeight(World world, BlockPos pos, EntityPlayer player) {
        return MathHelper.clamp(metaphysics.applyAsInt(new Context(world, pos, player, getBaseWeight())), 0, Integer.MAX_VALUE);
    }

    public void clearCard() {
        this.card.clear();
    }

    private static class Context {
        private World world;
        private BlockPos pos;
        private EntityPlayer player;
        private int baseWeight;

        private Context(World world, BlockPos pos, EntityPlayer player, int baseWeight) {
            this.world = world;
            this.pos = pos;
            this.player = player;
            this.baseWeight = baseWeight;
        }

        public World getWorld() {
            return world;
        }

        public BlockPos getPos() {
            return pos;
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public int getBaseWeight() {
            return baseWeight;
        }
    }
}
