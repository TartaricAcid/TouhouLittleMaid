package com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class EventType {
    public static final EventType WAKE_UP_NATURALLY = new EventType("wake_up_naturally", 3);
    public static final EventType WAKE_UP_NOISE = new EventType("wake_up_noise", -5);
    public static final EventType WORK_THUNDERSTORM = new EventType("work_thunderstorm", -1);
    public static final EventType WORK_NIGHT = new EventType("work_night", -1);
    public static final EventType HURT = new EventType("hurt", -1);
    public static final EventType HURT_BY_PLAYER = new EventType("hurt_by_player", -3);
    public static final EventType DEATH = new EventType("hurt_by_player", -5);
    public static final EventType JOY = new EventType("joy", 2);

    private String name;
    private int point;

    public EventType(String name, int point) {
        this.point = point;
        this.name = name;
    }

    @SubscribeEvent
    public static void calculateFavorability(FavorabilityEvent event) {
        EntityMaid maid = event.getMaid();
        EventType type = event.getType();
        maid.setFavorability(maid.getFavorability() + type.point);
    }
}
