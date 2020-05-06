package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * 这个事件会在玩家使用祭坛，匹配到合成列表时进行触发。<br>
 * 这个事件是 {@link Cancelable}，如果取消，那么该匹配的合成会被取消。<br>
 * 该事件会在客户端和服务端同时触发。
 */
@Cancelable
public class AltarCraftEvent extends Event {
    private final World world;
    private final EntityPlayer player;
    private final PowerHandler power;
    private final AltarRecipe altarRecipe;
    private final List<ItemStack> inputStackList;
    private final TileEntityAltar altar;

    public AltarCraftEvent(World world, EntityPlayer playerIn, PowerHandler power, AltarRecipe altarRecipe, List<ItemStack> inputStackList, TileEntityAltar altar) {
        this.world = world;
        this.player = playerIn;
        this.power = power;
        this.altarRecipe = altarRecipe;
        this.inputStackList = inputStackList;
        this.altar = altar;
    }

    public World getWorld() {
        return world;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public PowerHandler getPower() {
        return power;
    }

    public AltarRecipe getAltarRecipe() {
        return altarRecipe;
    }

    public List<ItemStack> getInputStackList() {
        return inputStackList;
    }

    public TileEntityAltar getAltar() {
        return altar;
    }
}
