package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.LazyVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.data.EntityModelData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.MolangUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiPredicate;

public class AnimationRegister {
    private static final double MIN_SPEED = 0.05;

    public static void registerAnimationState() {
        register("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE, Priority.HIGHEST, (maid, event) -> maid.asEntity().isDeadOrDying());
        register("sleep", Priority.HIGHEST, (maid, event) -> maid.asEntity().getPose() == Pose.SLEEPING);
        register("swim", Priority.HIGHEST, (maid, event) -> maid.asEntity().isSwimming());

        register("boat", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof Boat);
        register("gomoku", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.GOMOKU.getTypeName()));
        register("bookshelf", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.BOOKSHELF.getTypeName()));
        register("computer", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.COMPUTER.getTypeName()));
        register("keyboard", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.KEYBOARD.getTypeName()));
        register("picnic", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.ON_HOME_MEAL.getTypeName()));
        register("sit", Priority.HIGH, (maid, event) -> maid.isMaidInSittingPose());
        register("chair", Priority.HIGH, (maid, event) -> maid.asEntity().isPassenger());

        register("swim_stand", Priority.NORMAL, (maid, event) -> maid.asEntity().isInWater());
        register("attacked", ILoopType.EDefaultLoopTypes.PLAY_ONCE, Priority.NORMAL, (maid, event) -> maid.asEntity().hurtTime > 0);
        register("jump", Priority.NORMAL, (maid, event) -> !maid.asEntity().onGround() && !maid.asEntity().isInWater());

        register("run", Priority.LOW, (maid, event) -> maid.asEntity().onGround() && maid.asEntity().isSprinting());
        register("walk", Priority.LOW, (maid, event) -> maid.asEntity().onGround() && event.getLimbSwingAmount() > MIN_SPEED);

        register("idle", Priority.LOWEST, (maid, event) -> true);
    }

    @SuppressWarnings("all")
    public static void registerVariables() {
        MolangParser parser = GeckoLibCache.getInstance().parser;

        parser.register(new LazyVariable("query.actor_count", 0));
        parser.register(new LazyVariable("query.anim_time", 0));

        parser.register(new LazyVariable("query.body_x_rotation", 0));
        parser.register(new LazyVariable("query.body_y_rotation", 0));
        parser.register(new LazyVariable("query.cardinal_facing_2d", 0));
        parser.register(new LazyVariable("query.distance_from_camera", 0));
        parser.register(new LazyVariable("query.equipment_count", 0));
        parser.register(new LazyVariable("query.eye_target_x_rotation", 0));
        parser.register(new LazyVariable("query.eye_target_y_rotation", 0));
        parser.register(new LazyVariable("query.ground_speed", 0));

        parser.register(new LazyVariable("query.has_cape", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.has_rider", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.head_x_rotation", 0));
        parser.register(new LazyVariable("query.head_y_rotation", 0));
        parser.register(new LazyVariable("query.health", 0));
        parser.register(new LazyVariable("query.hurt_time", 0));

        parser.register(new LazyVariable("query.is_eating", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_first_person", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_in_water", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_in_water_or_rain", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_jumping", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_on_fire", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_on_ground", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_playing_dead", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_riding", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_sleeping", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_sneaking", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_spectator", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_sprinting", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_swimming", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.is_using_item", MolangUtils.FALSE));
        parser.register(new LazyVariable("query.item_in_use_duration", 0));
        parser.register(new LazyVariable("query.item_max_use_duration", 0));
        parser.register(new LazyVariable("query.item_remaining_use_duration", 0));

        parser.register(new LazyVariable("query.life_time", 0));
        parser.register(new LazyVariable("query.max_health", 0));
        parser.register(new LazyVariable("query.modified_distance_moved", 0));
        parser.register(new LazyVariable("query.moon_phase", 0));

        parser.register(new LazyVariable("query.player_level", 0));
        parser.register(new LazyVariable("query.time_of_day", 0));
        parser.register(new LazyVariable("query.time_stamp", 0));
        parser.register(new LazyVariable("query.vertical_speed", 0));
        parser.register(new LazyVariable("query.walk_distance", 0));
        parser.register(new LazyVariable("query.yaw_speed", 0));

        parser.register(new LazyVariable("ysm.head_yaw", 0));
        parser.register(new LazyVariable("ysm.head_pitch", 0));
        parser.register(new LazyVariable("ysm.has_helmet", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.has_chest_plate", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.has_leggings", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.has_boots", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.has_mainhand", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.has_offhand", MolangUtils.FALSE));

        parser.register(new LazyVariable("ysm.has_elytra", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.elytra_rot_x", 0));
        parser.register(new LazyVariable("ysm.elytra_rot_y", 0));
        parser.register(new LazyVariable("ysm.elytra_rot_z", 0));

        parser.register(new LazyVariable("ysm.is_close_eyes", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.is_passenger", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.is_sleep", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.is_sneak", MolangUtils.FALSE));
        parser.register(new LazyVariable("ysm.is_riptide", MolangUtils.FALSE));

        parser.register(new LazyVariable("ysm.armor_value", 0));
        parser.register(new LazyVariable("ysm.hurt_time", 0));
        parser.register(new LazyVariable("ysm.food_level", 20));

        parser.register(new LazyVariable("ysm.first_person_mod_hide", MolangUtils.FALSE));

        parser.register(new LazyVariable("tlm.is_begging", MolangUtils.FALSE));
        parser.register(new LazyVariable("tlm.is_sitting", MolangUtils.FALSE));
        parser.register(new LazyVariable("tlm.has_backpack", MolangUtils.FALSE));
    }

    public static void setParserValue(AnimationEvent<?> animationEvent, MolangParser parser, EntityModelData data, IMaid maid) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }

        Mob mob = maid.asEntity();

        parser.setValue("query.actor_count", () -> mc.level.getEntityCount());
        parser.setValue("query.body_x_rotation", mob::getXRot);
        parser.setValue("query.body_y_rotation", () -> Mth.wrapDegrees(mob.getYRot()));
        parser.setValue("query.cardinal_facing_2d", () -> mob.getDirection().get3DDataValue());
        parser.setValue("query.distance_from_camera", () -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(mob.position()));
        parser.setValue("query.equipment_count", () -> getEquipmentCount(mob));
        parser.setValue("query.eye_target_x_rotation", () -> mob.getViewXRot(0));
        parser.setValue("query.eye_target_y_rotation", () -> mob.getViewYRot(0));
        parser.setValue("query.ground_speed", () -> getGroundSpeed(mob));

        parser.setValue("query.has_rider", () -> MolangUtils.booleanToFloat(mob.isVehicle()));
        parser.setValue("query.head_x_rotation", () -> data.netHeadYaw);
        parser.setValue("query.head_y_rotation", () -> data.headPitch);
        parser.setValue("query.health", mob::getHealth);
        parser.setValue("query.hurt_time", () -> mob.hurtTime);

        parser.setValue("query.is_eating", () -> MolangUtils.booleanToFloat(mob.getUseItem().getUseAnimation() == UseAnim.EAT));
        parser.setValue("query.is_first_person", () -> MolangUtils.booleanToFloat(mc.options.getCameraType() == CameraType.FIRST_PERSON));
        parser.setValue("query.is_in_water", () -> MolangUtils.booleanToFloat(mob.isInWater()));
        parser.setValue("query.is_in_water_or_rain", () -> MolangUtils.booleanToFloat(mob.isInWaterRainOrBubble()));
        parser.setValue("query.is_jumping", () -> MolangUtils.booleanToFloat(!mob.isPassenger() && !mob.onGround() && !mob.isInWater()));
        parser.setValue("query.is_on_fire", () -> MolangUtils.booleanToFloat(mob.isOnFire()));
        parser.setValue("query.is_on_ground", () -> MolangUtils.booleanToFloat(mob.onGround()));
        parser.setValue("query.is_playing_dead", () -> MolangUtils.booleanToFloat(mob.isDeadOrDying()));
        parser.setValue("query.is_riding", () -> MolangUtils.booleanToFloat(mob.isPassenger()));
        parser.setValue("query.is_sleeping", () -> MolangUtils.booleanToFloat(mob.isSleeping()));
        parser.setValue("query.is_sneaking", () -> MolangUtils.booleanToFloat(mob.onGround() && mob.getPose() == Pose.CROUCHING));
        parser.setValue("query.is_spectator", () -> MolangUtils.booleanToFloat(mob.isSpectator()));
        parser.setValue("query.is_sprinting", () -> MolangUtils.booleanToFloat(mob.isSprinting()));
        parser.setValue("query.is_swimming", () -> MolangUtils.booleanToFloat(mob.isSwimming()));
        parser.setValue("query.is_using_item", () -> MolangUtils.booleanToFloat(mob.isUsingItem()));
        parser.setValue("query.item_in_use_duration", () -> mob.getTicksUsingItem() / 20.0);
        parser.setValue("query.item_max_use_duration", () -> getMaxUseDuration(mob) / 20.0);
        parser.setValue("query.item_remaining_use_duration", () -> mob.getUseItemRemainingTicks() / 20.0);

        parser.setValue("query.max_health", mob::getMaxHealth);
        parser.setValue("query.modified_distance_moved", () -> mob.walkDist);
        parser.setValue("query.moon_phase", () -> mc.level.getMoonPhase());

        parser.setValue("query.player_level", () -> maid.getExperience() / 120d);
        parser.setValue("query.time_of_day", () -> MolangUtils.normalizeTime(mc.level.getDayTime()));
        parser.setValue("query.time_stamp", () -> mc.level.getDayTime());
        parser.setValue("query.vertical_speed", () -> getVerticalSpeed(mob));
        parser.setValue("query.walk_distance", () -> mob.moveDist);
        parser.setValue("query.yaw_speed", () -> getYawSpeed(animationEvent, mob));

        parser.setValue("ysm.head_yaw", () -> data.netHeadYaw);
        parser.setValue("ysm.head_pitch", () -> data.headPitch);

        parser.setValue("ysm.has_helmet", () -> getSlotValue(mob, EquipmentSlot.HEAD));
        parser.setValue("ysm.has_chest_plate", () -> getSlotValue(mob, EquipmentSlot.CHEST));
        parser.setValue("ysm.has_leggings", () -> getSlotValue(mob, EquipmentSlot.LEGS));
        parser.setValue("ysm.has_boots", () -> getSlotValue(mob, EquipmentSlot.FEET));
        parser.setValue("ysm.has_mainhand", () -> getSlotValue(mob, EquipmentSlot.MAINHAND));
        parser.setValue("ysm.has_offhand", () -> getSlotValue(mob, EquipmentSlot.OFFHAND));

        parser.setValue("ysm.has_elytra", () -> MolangUtils.booleanToFloat(mob.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.ELYTRA));

        parser.setValue("ysm.is_close_eyes", () -> getEyeCloseState(animationEvent, mob));
        parser.setValue("ysm.is_passenger", () -> MolangUtils.booleanToFloat(mob.isPassenger()));
        parser.setValue("ysm.is_sleep", () -> MolangUtils.booleanToFloat(mob.getPose() == Pose.SLEEPING));
        parser.setValue("ysm.is_sneak", () -> MolangUtils.booleanToFloat(mob.onGround() && mob.getPose() == Pose.CROUCHING));
        parser.setValue("ysm.is_riptide", () -> MolangUtils.booleanToFloat(mob.isAutoSpinAttack()));

        parser.setValue("ysm.armor_value", mob::getArmorValue);
        parser.setValue("ysm.hurt_time", () -> mob.hurtTime);

        parser.setValue("tlm.is_begging", () -> MolangUtils.booleanToFloat(maid.isBegging()));
        parser.setValue("tlm.is_sitting", () -> MolangUtils.booleanToFloat(maid.isMaidInSittingPose()));
        parser.setValue("tlm.has_backpack", () -> MolangUtils.booleanToFloat(maid.hasBackpack()));
    }

    private static void register(String animationName, ILoopType loopType, int priority, BiPredicate<IMaid, AnimationEvent<?>> predicate) {
        AnimationManager manager = AnimationManager.getInstance();
        manager.register(new AnimationState(animationName, loopType, priority, predicate));
    }

    private static void register(String animationName, int priority, BiPredicate<IMaid, AnimationEvent<?>> predicate) {
        register(animationName, ILoopType.EDefaultLoopTypes.LOOP, priority, predicate);
    }

    private static int getEquipmentCount(Mob maid) {
        int count = 0;
        for (ItemStack s : maid.getArmorSlots()) {
            if (!s.isEmpty()) {
                count += 1;
            }
        }
        return count;
    }

    private static double getMaxUseDuration(Mob maid) {
        ItemStack useItem = maid.getUseItem();
        if (useItem.isEmpty()) {
            return 0.0;
        } else {
            return useItem.getUseDuration();
        }
    }

    private static float getYawSpeed(AnimationEvent<?> animationEvent, Mob maid) {
        double seekTime = animationEvent.getAnimationTick();
        return maid.getViewYRot((float) seekTime - maid.getViewYRot((float) seekTime - 0.1f));
    }

    private static float getGroundSpeed(Mob maid) {
        Vec3 velocity = maid.getDeltaMovement();
        return 20 * Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
    }

    private static float getVerticalSpeed(Mob maid) {
        return 20 * (float) (maid.position().y - maid.yo);
    }

    private static double getEyeCloseState(AnimationEvent<?> animationEvent, Mob maid) {
        double remainder = (animationEvent.getAnimationTick() + Math.abs(maid.getUUID().getLeastSignificantBits()) % 10) % 90;
        boolean isBlinkTime = 85 < remainder && remainder < 90;
        return MolangUtils.booleanToFloat(maid.isSleeping() || isBlinkTime);
    }

    private static double getSlotValue(Mob maid, EquipmentSlot slot) {
        return MolangUtils.booleanToFloat(!maid.getItemBySlot(slot).isEmpty());
    }
}