package com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public final class JoyType {
    public static final Map<String, JoyType> JOYS = Maps.newHashMap();
    private static final int FAVORABILITY_INCREASES_FREQUENCY = 100;
    private static int COUNTER = 0;
    public static final JoyType READING = addJoyType("reading", 20 * 60, 20 * 60 * 10);
    public static final JoyType GAME = addJoyType("game", 20 * 30, 20 * 60 * 20);
    public static final JoyType PIANO = addJoyType("piano", 20 * 60, 20 * 60 * 15);
    public static final JoyType DRUM = addJoyType("drum", 20 * 60, 20 * 60 * 15);
    public static final JoyType LIVE = addJoyType("live", 20 * 60, 20 * 60 * 10);
    public static final JoyType MAKING_MANUAL = addJoyType("making_manual", 20 * 60, 20 * 60 * 10);
    public static final JoyType TEA = addJoyType("tea", 20 * 60, 20 * 60 * 10);
    private final String name;
    private final int joyingTick;
    private final int coolingTick;
    private final int index;

    public JoyType(String name, int joyingTick, int coolingTick) {
        this.name = name;
        this.joyingTick = joyingTick;
        this.coolingTick = coolingTick;
        index = COUNTER;
    }

    public static JoyType addJoyType(String name, int joyingTick, int coolingTick) {
        JoyType type = new JoyType(name, joyingTick, coolingTick);
        JOYS.put(name, type);
        COUNTER++;
        return type;
    }

    public static void updateJoyTick(EntityMaid maid, Map<String, Integer> joyTickData) {
        for (String joyTypeName : joyTickData.keySet()) {
            int tick = joyTickData.get(joyTypeName);
            if (tick > 0) {
                joyTickData.put(joyTypeName, --tick);
            }
        }
        if (maid.getRidingEntity() instanceof EntityMaidJoy) {
            if (maid.ticksExisted % FAVORABILITY_INCREASES_FREQUENCY == 0) {
                MinecraftForge.EVENT_BUS.post(new FavorabilityEvent(EventType.JOY, maid));
            }
            EntityMaidJoy entityMaidJoy = (EntityMaidJoy) maid.getRidingEntity();
            String joyTypeName = entityMaidJoy.getType();
            JoyType joyType = JOYS.get(joyTypeName);
            int tick = 0;
            if (joyTickData.containsKey(joyTypeName)) {
                tick = joyTickData.get(joyTypeName);
            }
            if (tick > joyType.coolingTick) {
                maid.dismountRidingEntity();
            } else {
                joyTickData.put(joyTypeName, tick + 1 + joyType.coolingTick / joyType.joyingTick);
            }
        }
    }

    public static NBTTagCompound joyTickDataToCompound(Map<String, Integer> joyTickData) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        for (String joyTypeName : joyTickData.keySet()) {
            tagCompound.setInteger(joyTypeName, joyTickData.get(joyTypeName));
        }
        return tagCompound;
    }

    public static Map<String, Integer> compoundToJoyTickData(NBTTagCompound compound) {
        Map<String, Integer> joyTickData = Maps.newHashMap();
        for (String name : compound.getKeySet()) {
            joyTickData.put(name, compound.getInteger(name));
        }
        return joyTickData;
    }

    public static boolean canUseJoyType(EntityMaid maid, String type) {
        if (maid.getJoyTickData().containsKey(type)) {
            return maid.getJoyTickData().get(type) <= 0;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
