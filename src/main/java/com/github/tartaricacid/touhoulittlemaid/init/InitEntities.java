package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor.MaidHostilesSensor;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.schedule.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<EntityType<EntityMaid>> MAID = ENTITY_TYPES.register("maid", () -> EntityMaid.TYPE);
    public static RegistryObject<EntityType<EntityChair>> CHAIR = ENTITY_TYPES.register("chair", () -> EntityChair.TYPE);
    public static RegistryObject<EntityType<EntityFairy>> FAIRY = ENTITY_TYPES.register("fairy", () -> EntityFairy.TYPE);
    public static RegistryObject<EntityType<EntityDanmaku>> DANMAKU = ENTITY_TYPES.register("danmaku", () -> EntityDanmaku.TYPE);

    public static RegistryObject<SensorType<MaidHostilesSensor>> MAID_HOSTILES_SENSOR = SENSOR_TYPES.register("maid_hostiles", () -> new SensorType<>(MaidHostilesSensor::new));
    public static RegistryObject<Schedule> MAID_DAY_SHIFT_SCHEDULES = SCHEDULES.register("maid_day_shift_schedules",
            () -> {
                // 06:00 ~ 18:00 工作
                // 18:00 ~ 22:00 娱乐
                // 22:00 ~ 06:00 睡觉
                return new ScheduleBuilder(new Schedule())
                        .changeActivityAt(0, Activity.WORK)
                        .changeActivityAt(12000, Activity.IDLE)
                        .changeActivityAt(16000, Activity.REST)
                        .build();
            });

    @SubscribeEvent
    public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityMaid.TYPE, MonsterEntity.createMonsterAttributes().build());
        event.put(EntityChair.TYPE, LivingEntity.createLivingAttributes().build());
        event.put(EntityFairy.TYPE, EntityFairy.createFairyAttributes().build());
    }
}
