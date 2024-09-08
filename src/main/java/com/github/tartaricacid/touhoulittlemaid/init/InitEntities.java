package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor.MaidHostilesSensor;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor.MaidNearestLivingEntitySensor;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor.MaidPickupEntitiesSensor;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.MaidChatBubbles;
import com.github.tartaricacid.touhoulittlemaid.entity.item.*;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityThrowPowerPoint;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<EntityType<EntityMaid>> MAID = ENTITY_TYPES.register("maid", () -> EntityMaid.TYPE);
    public static RegistryObject<EntityType<EntityChair>> CHAIR = ENTITY_TYPES.register("chair", () -> EntityChair.TYPE);
    public static RegistryObject<EntityType<EntityFairy>> FAIRY = ENTITY_TYPES.register("fairy", () -> EntityFairy.TYPE);
    public static RegistryObject<EntityType<EntityDanmaku>> DANMAKU = ENTITY_TYPES.register("danmaku", () -> EntityDanmaku.TYPE);
    public static RegistryObject<EntityType<EntityPowerPoint>> POWER_POINT = ENTITY_TYPES.register("power_point", () -> EntityPowerPoint.TYPE);
    public static RegistryObject<EntityType<EntityExtinguishingAgent>> EXTINGUISHING_AGENT = ENTITY_TYPES.register("extinguishing_agent", () -> EntityExtinguishingAgent.TYPE);
    public static RegistryObject<EntityType<EntityBox>> BOX = ENTITY_TYPES.register("box", () -> EntityBox.TYPE);
    public static RegistryObject<EntityType<EntityThrowPowerPoint>> THROW_POWER_POINT = ENTITY_TYPES.register("throw_power_point", () -> EntityThrowPowerPoint.TYPE);
    public static RegistryObject<EntityType<EntityTombstone>> TOMBSTONE = ENTITY_TYPES.register("tombstone", () -> EntityTombstone.TYPE);
    public static RegistryObject<EntityType<EntitySit>> SIT = ENTITY_TYPES.register("sit", () -> EntitySit.TYPE);
    public static RegistryObject<EntityType<EntityBroom>> BROOM = ENTITY_TYPES.register("broom", () -> EntityBroom.TYPE);

    public static RegistryObject<MemoryModuleType<List<Entity>>> VISIBLE_PICKUP_ENTITIES = MEMORY_MODULE_TYPES.register("visible_pickup_entities", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<PositionTracker>> TARGET_POS = MEMORY_MODULE_TYPES.register("target_pos", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<SensorType<MaidNearestLivingEntitySensor>> MAID_NEAREST_LIVING_ENTITY_SENSOR = SENSOR_TYPES.register("maid_nearest_living_entity", () -> new SensorType<>(MaidNearestLivingEntitySensor::new));
    public static RegistryObject<SensorType<MaidHostilesSensor>> MAID_HOSTILES_SENSOR = SENSOR_TYPES.register("maid_hostiles", () -> new SensorType<>(MaidHostilesSensor::new));
    public static RegistryObject<SensorType<MaidPickupEntitiesSensor>> MAID_PICKUP_ENTITIES_SENSOR = SENSOR_TYPES.register("maid_pickup_entities", () -> new SensorType<>(MaidPickupEntitiesSensor::new));
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
    public static RegistryObject<Schedule> MAID_NIGHT_SHIFT_SCHEDULES = SCHEDULES.register("maid_night_shift_schedules",
            () -> {
                // 18:00 ~ 06:00 工作
                // 06:00 ~ 14:00 睡觉
                // 14:00 ~ 18:00 娱乐
                return new ScheduleBuilder(new Schedule())
                        .changeActivityAt(0, Activity.REST)
                        .changeActivityAt(8000, Activity.IDLE)
                        .changeActivityAt(12000, Activity.WORK)
                        .build();
            });
    public static RegistryObject<Schedule> MAID_ALL_DAY_SCHEDULES = SCHEDULES.register("maid_all_day_schedules",
            () -> new ScheduleBuilder(new Schedule()).changeActivityAt(0, Activity.WORK).build());
    public static RegistryObject<EntityDataSerializer<?>> MAID_SCHEDULE_DATA_SERIALIZERS = DATA_SERIALIZERS.register("maid_schedule", () -> MaidSchedule.DATA);
    public static RegistryObject<EntityDataSerializer<?>> MAID_CHAT_BUBBLE_DATA_SERIALIZERS = DATA_SERIALIZERS.register("maid_chat_bubble", () -> MaidChatBubbles.DATA);

    @SubscribeEvent
    public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityMaid.TYPE, EntityMaid.createAttributes().build());
        event.put(EntityChair.TYPE, LivingEntity.createLivingAttributes().build());
        event.put(EntityBroom.TYPE, LivingEntity.createLivingAttributes().build());
        event.put(EntityFairy.TYPE, EntityFairy.createFairyAttributes().build());
    }

    @SubscribeEvent
    public static void addEntitySpawnPlacement(FMLCommonSetupEvent event) {
        SpawnPlacements.register(InitEntities.FAIRY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }
}
