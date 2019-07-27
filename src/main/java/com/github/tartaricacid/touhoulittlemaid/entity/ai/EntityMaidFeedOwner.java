package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.task.FeedHandler;
import com.github.tartaricacid.touhoulittlemaid.api.task.Trend;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.stream.Collectors;

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
        // 主人是 EntityPlayer 而且在限制距离内
        boolean ownerIsOkay = entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getDistance(entityMaid.getOwner()) < distance;
        return timeCount < 0 && !entityMaid.isSitting() && ownerIsOkay;
    }

    @Override
    public void startExecuting() {
        // 先将其重置为 60
        // 用处在本段末尾有说明
        timeCount = 60;

        if (entityMaid.getOwner() instanceof EntityPlayer && entityMaid.getOwner().isEntityAlive()) {
            EntityPlayer player = (EntityPlayer) entityMaid.getOwner();
            // 获取得到可用的 FeedHandler 列表
            List<FeedHandler> handlers = LittleMaidAPI.getFeedHandlers().stream().filter(h -> h.canExecute(entityMaid)).collect(Collectors.toList());
            // 使用 FastUtils 的 Int2ObjectMap 来获得更好的性能
            // 存储对象为 slot -> FeedHandler 映射集合
            Int2ObjectMap<FeedHandler> badFoods = new Int2ObjectArrayMap<>();
            Int2ObjectMap<FeedHandler> exactFoods = new Int2ObjectArrayMap<>();

            // 先筛选出 EXACT 和 BAD 分类的食物
            IItemHandlerModifiable inv = entityMaid.getAvailableInv();
            for (int i = 0; i < inv.getSlots(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                // 遍历 FeedHandler 进行食物的添加和分类
                for (FeedHandler handler : handlers) {
                    if (handler.isFood(stack, player)) {
                        // 放入 EXACT 和 BAD 分类的食物
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

            // 如果两者皆为空，不执行喂食
            if (exactFoods.isEmpty() && badFoods.isEmpty()) {
                return;
            }

            // EXACT 优先选择，BAD 次之
            // 随机选择某一个食物所在的格子，还有对应的 FeedHandler
            Int2ObjectMap<FeedHandler> map = !exactFoods.isEmpty() ? exactFoods : badFoods;
            int slot = map.keySet().stream().skip(entityMaid.getRNG().nextInt(map.size())).findFirst().get();
            FeedHandler handler = map.get(slot);

            // 朝向，喂食，手臂动画一气呵成
            entityMaid.getLookHelper().setLookPositionWithEntity(player, 10, 40);
            inv.setStackInSlot(slot, handler.feed(inv.getStackInSlot(slot), player));
            entityMaid.swingArm(EnumHand.MAIN_HAND);

            // 上述执行成功，短暂休息后执行下一次逻辑判定
            // 如果还没到达此处就 return 了，那就说明尝试喂食失败，就使用先前的 60 检索时间
            // 通过一前一后配合，巧妙达到了连续喂养和低频率检索两个目的
            timeCount = 5;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
