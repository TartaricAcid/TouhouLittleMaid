package com.github.tartaricacid.touhoulittlemaid.event.api;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * 这个事件会在玩家主手右击女仆时进行触发。<br>
 * 这个事件是 {@link Cancelable}，如果取消，那么后续的所有右击事件判定都会被取消。<br>
 * 该事件会在客户端和服务端同时触发。
 */
@Cancelable
public class InteractMaidEvent extends Event {
    private final World world;
    private final PlayerEntity player;
    private final EntityMaid maid;
    private final ItemStack stack;

    public InteractMaidEvent(PlayerEntity player, EntityMaid maid, ItemStack stack) {
        this.player = player;
        this.world = player.level;
        this.maid = maid;
        this.stack = stack;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getStack() {
        return stack;
    }

    public World getWorld() {
        return world;
    }
}
