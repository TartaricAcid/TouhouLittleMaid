package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * 这个事件会在玩家主手右击女仆时进行触发。<br>
 * 这个事件是 {@link #isCanceled() canceled}，如果取消，那么后续的所有右击事件判定都会被取消。<br>
 * 该事件会在客户端和服务端同时触发。
 */

public class InteractMaidEvent extends Event implements ICancellableEvent {
    private final Level world;
    private final Player player;
    private final EntityMaid maid;
    private final ItemStack stack;

    public InteractMaidEvent(Player player, EntityMaid maid, ItemStack stack) {
        this.player = player;
        this.world = player.level();
        this.maid = maid;
        this.stack = stack;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getStack() {
        return stack;
    }

    public Level getWorld() {
        return world;
    }
}
