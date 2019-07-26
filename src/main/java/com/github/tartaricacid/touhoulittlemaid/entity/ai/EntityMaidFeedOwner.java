package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import java.util.List;
import java.util.stream.Collectors;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.task.FeedHandler;
import com.github.tartaricacid.touhoulittlemaid.api.task.Trend;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class EntityMaidFeedOwner extends EntityAIBase {
    private AbstractEntityMaid entityMaid;
    private int timeCount;
    private int distance;

    public EntityMaidFeedOwner(AbstractEntityMaid entityMaid, int distance) {
        this.entityMaid = entityMaid;
        timeCount = 60;
        this.distance = distance;
    }

    @Override
    public boolean shouldExecute() {
        timeCount--;
        return timeCount < 0 && !entityMaid.isSitting()
                && entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getDistance(entityMaid.getOwner()) < distance;
    }

    @Override
    public void startExecuting() {
        timeCount = 60;
        if (entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getOwner().isEntityAlive()) {
            EntityPlayer player = (EntityPlayer) entityMaid.getOwner();
            List<FeedHandler> handlers = LittleMaidAPI.getFeedHandlers().stream().filter(h -> h.canExecute(entityMaid)).collect(Collectors.toList());
            Int2ObjectMap<FeedHandler> badFoods = new Int2ObjectArrayMap<>();
            Int2ObjectMap<FeedHandler> exactFoods = new Int2ObjectArrayMap<>();

            IItemHandlerModifiable inv = entityMaid.getAvailableInv();
            for (int i = 0; i < inv.getSlots(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                for (FeedHandler handler : handlers)
                {
                    if (handler.isFood(stack, player)) {
                        Trend trend = handler.getTrend(stack, player);
                        if (trend == Trend.EXACT) {
                            exactFoods.put(i, handler);
                            break;
                        }
                        if (trend == Trend.BAD) {
                            badFoods.put(i, handler);
                            break;
                        }
                    }
                }
            }

            if (exactFoods.isEmpty() && badFoods.isEmpty()) {
                return;
            }
            Int2ObjectMap<FeedHandler> map = !exactFoods.isEmpty() ? exactFoods : badFoods;
            // 获取随机成员
            int slot = map.keySet().stream().skip(entityMaid.getRNG().nextInt(map.size())).findFirst().get();
            FeedHandler handler = map.get(slot);
            ItemStack stack = inv.getStackInSlot(slot);

            entityMaid.getLookHelper().setLookPositionWithEntity(player, 10, 40);
            timeCount = 5;
            entityMaid.swingArm(EnumHand.MAIN_HAND);
            inv.setStackInSlot(slot, handler.feed(stack, player));
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
