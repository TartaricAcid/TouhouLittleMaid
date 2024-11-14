package com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.ContextBinding;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.query.*;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.MolangUtils;
import com.github.tartaricacid.touhoulittlemaid.util.EquipmentUtil;
import net.minecraft.client.CameraType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;

public class QueryBinding extends ContextBinding {
    public static final QueryBinding INSTANCE = new QueryBinding();

    @SuppressWarnings("resource")
    private QueryBinding() {
        function("biome_has_all_tags", new BiomeHasAllTags());
        function("biome_has_any_tag", new BiomeHasAnyTag());
        function("relative_block_has_all_tags", new RelativeBlockHasAllTags());
        function("relative_block_has_any_tag", new RelativeBlockHasAnyTag());
        function("is_item_name_any", new ItemNameAny());
        function("equipped_item_all_tags", new EquippedItemAllTags());
        function("equipped_item_any_tag", new EquippedItemAnyTags());
        function("position", new Position());
        function("position_delta", new PositionDelta());

        function("max_durability", new ItemMaxDurability());
        function("remaining_durability", new ItemRemainingDurability());

        var("actor_count", ctx -> ctx.level().getEntityCount());
        var("anim_time", ctx -> ctx.animationControllerContext().animTime());
        var("life_time", ctx -> ctx.geoInstance().getSeekTime() / 20.0);
        var("head_x_rotation", ctx -> ctx.data().netHeadYaw);
        var("head_y_rotation", ctx -> ctx.data().headPitch);
        var("moon_phase", ctx -> ctx.level().getMoonPhase());
        var("time_of_day", ctx -> MolangUtils.normalizeTime(ctx.level().getDayTime()));
        var("time_stamp", ctx -> ctx.level().getDayTime());

        entityVar("yaw_speed", ctx -> getYawSpeed(ctx.entity()));
        entityVar("cardinal_facing_2d", ctx -> ctx.entity().getDirection().get3DDataValue());
        entityVar("distance_from_camera", ctx -> ctx.mc().gameRenderer.getMainCamera().getPosition().distanceTo(ctx.entity().position()));
        entityVar("eye_target_x_rotation", ctx -> ctx.entity().getViewXRot(ctx.animationEvent().getPartialTick()));
        entityVar("eye_target_y_rotation", ctx -> ctx.entity().getViewYRot(ctx.animationEvent().getPartialTick()));
        entityVar("ground_speed", ctx -> getGroundSpeed(ctx.entity()));
        entityVar("modified_distance_moved", ctx -> ctx.entity().walkDist);
        entityVar("vertical_speed", ctx -> getVerticalSpeed(ctx.entity()));
        entityVar("walk_distance", ctx -> ctx.entity().moveDist);
        entityVar("has_rider", ctx -> ctx.entity().isVehicle());
        entityVar("is_first_person", ctx -> ctx.mc().options.getCameraType() == CameraType.FIRST_PERSON);
        entityVar("is_in_water", ctx -> ctx.entity().isInWater());
        entityVar("is_in_water_or_rain", ctx -> ctx.entity().isInWaterRainOrBubble());
        entityVar("is_on_fire", ctx -> ctx.entity().isOnFire());
        entityVar("is_on_ground", ctx -> ctx.entity().onGround());
        entityVar("is_riding", ctx -> ctx.entity().isPassenger());
        entityVar("is_sneaking", ctx -> ctx.entity().onGround() && ctx.entity().getPose() == Pose.CROUCHING);
        entityVar("is_spectator", ctx -> ctx.entity().isSpectator());
        entityVar("is_sprinting", ctx -> ctx.entity().isSprinting());
        entityVar("is_swimming", ctx -> ctx.entity().isSwimming());

        livingEntityVar("body_x_rotation", ctx -> Mth.lerp(ctx.animationEvent().getPartialTick(), ctx.entity().xRotO, ctx.entity().getXRot()));
        livingEntityVar("body_y_rotation", ctx -> Mth.wrapDegrees(Mth.lerp(ctx.animationEvent().getPartialTick(), ctx.entity().yBodyRotO, ctx.entity().yBodyRot)));
        livingEntityVar("health", ctx -> ctx.entity().getHealth());
        livingEntityVar("max_health", ctx -> ctx.entity().getMaxHealth());
        livingEntityVar("hurt_time", ctx -> ctx.entity().hurtTime);
        livingEntityVar("is_eating", ctx -> ctx.entity().getUseItem().getUseAnimation() == UseAnim.EAT);
        livingEntityVar("is_playing_dead", ctx -> ctx.entity().isDeadOrDying());
        livingEntityVar("is_sleeping", ctx -> ctx.entity().isSleeping());
        livingEntityVar("is_using_item", ctx -> ctx.entity().isUsingItem());
        livingEntityVar("item_in_use_duration", ctx -> ctx.entity().getTicksUsingItem() / 20.0);
        livingEntityVar("item_max_use_duration", ctx -> getMaxUseDuration(ctx.entity()) / 20.0);
        livingEntityVar("item_remaining_use_duration", ctx -> ctx.entity().getUseItemRemainingTicks() / 20.0);
        livingEntityVar("equipment_count", ctx -> getEquipmentCount(ctx.entity()));

        // 为了兼容 ysm 添加的变量
        function("debug_output", new EmptyFunction());
        var("has_cape", ctx -> false);
        var("cape_flap_amount", ctx -> 0);
        maidEntityVar("player_level", ctx -> ctx.entity().getExperience());
        mobEntityVar("is_jumping", ctx -> !ctx.entity().isPassenger() && !ctx.entity().onGround() && !ctx.entity().isInWater());
    }


    private static int getEquipmentCount(LivingEntity entity) {
        int count = 0;
        for (var slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) {
                continue;
            }
            var stack = EquipmentUtil.getEquippedItem(entity, slot);
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private static double getMaxUseDuration(LivingEntity player) {
        ItemStack useItem = player.getUseItem();
        if (useItem.isEmpty()) {
            return 0.0;
        } else {
            return useItem.getUseDuration();
        }
    }

    private static float getYawSpeed(Entity entity) {
        return 20 * (entity.getYRot() - entity.yRotO);
    }

    private static float getGroundSpeed(Entity player) {
        Vec3 velocity = player.getDeltaMovement();
        return 20 * Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
    }

    private static float getVerticalSpeed(Entity entity) {
        return 20 * (float) (entity.position().y - entity.yo);
    }
}
