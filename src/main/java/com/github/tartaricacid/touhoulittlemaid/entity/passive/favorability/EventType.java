package com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class EventType {
    public static final EventType WAKE_UP_NATURALLY = new EventType("wake_up_naturally", 50);
    public static final EventType WAKE_UP_NOISE = new EventType("wake_up_noise", -50);
    public static final EventType WORK_NIGHT_OR_THUNDERSTORM = new EventType("work_night_or_thunderstorm", -1);
    public static final EventType HURT_BY_PLAYER = new EventType("hurt_by_player", -20);
    public static final EventType DEATH = new EventType("death", -100);
    public static final EventType JOY = new EventType("joy", 2);

    private final String name;
    private final int point;

    public EventType(String name, int point) {
        this.point = point;
        this.name = name;
    }

    @SubscribeEvent
    public static void calculateFavorability(FavorabilityEvent event) {
        EntityMaid maid = event.getMaid();
        EventType type = event.getType();
        maid.setFavorability(maid.getFavorability() + type.point);

        Level level = Level.getLevelByCount(maid.getFavorability());
        maid.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(level.getAttackValue());
        if (maid.isStruckByLightning()) {
            maid.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(level.getHealthyValue() + 20);
        } else {
            maid.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(level.getHealthyValue());
        }
        if (maid.getHealth() > maid.getMaxHealth()) {
            maid.setHealth(maid.getMaxHealth());
        }
    }

    public String getName() {
        return name;
    }
}
