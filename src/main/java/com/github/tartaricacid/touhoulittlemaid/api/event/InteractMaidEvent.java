package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 这个事件会在玩家主手右击女仆时进行触发。<br>
 * 这个事件是 {@link Cancelable}，如果取消，那么后续的所有右击事件判定都会被取消。<br>
 * 该事件会在客户端和服务端同时触发。
 */
@Cancelable
public class InteractMaidEvent extends Event {
    private World world;
    private EntityPlayer player;
    private AbstractEntityMaid maid;
    private ItemStack stack;

    public InteractMaidEvent(EntityPlayer player, AbstractEntityMaid maid, ItemStack stack) {
        this.player = player;
        this.world = player.getEntityWorld();
        this.maid = maid;
        this.stack = stack;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public AbstractEntityMaid getMaid() {
        return maid;
    }

    public ItemStack getStack() {
        return stack;
    }

    public World getWorld() {
        return world;
    }
}
