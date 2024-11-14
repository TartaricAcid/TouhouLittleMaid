package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.functions.*;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.variable.LadderFacingVariable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.binding.ContextBinding;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.builtin.query.EmptyFunction;
import com.github.tartaricacid.touhoulittlemaid.util.EquipmentUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeMod;
import org.apache.commons.lang3.StringUtils;

public class YSMBinding extends ContextBinding {
    public static final YSMBinding INSTANCE = new YSMBinding();

    @SuppressWarnings("resource")
    private YSMBinding() {
        function("mod_version", new ModVersion());
        function("equipped_enchantment_level", new EquippedEnchantmentLevel());
        function("effect_level", new EffectLevel());
        function("relative_block_name", new RelativeBlockName());

        function("bone_rot", new BoneRotation());
        function("bone_pos", new BonePosition());
        function("bone_scale", new BoneScale());

        var("head_yaw", ctx -> ctx.data().netHeadYaw);
        var("head_pitch", ctx -> ctx.data().headPitch);
        var("weather", ctx -> getWeather(ctx.level()));
        var("dimension_name", ctx -> ctx.level().dimension().location().toString());
        var("fps", ctx -> Minecraft.getInstance().getFps());

        entityVar("is_passenger", ctx -> ctx.entity().isPassenger());
        entityVar("is_sleep", ctx -> ctx.entity().getPose() == Pose.SLEEPING);
        entityVar("is_sneak", ctx -> ctx.entity().onGround() && ctx.entity().getPose() == Pose.CROUCHING);
        entityVar("is_open_air", ctx -> isOpenAir(ctx.entity()));
        entityVar("eye_in_water", ctx -> ctx.entity().isUnderWater());
        entityVar("frozen_ticks", ctx -> ctx.entity().getTicksFrozen());
        entityVar("air_supply", ctx -> ctx.entity().getAirSupply());

        livingEntityVar("has_helmet", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.HEAD));
        livingEntityVar("has_chest_plate", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.CHEST));
        livingEntityVar("has_leggings", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.LEGS));
        livingEntityVar("has_boots", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.FEET));
        livingEntityVar("has_mainhand", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.MAINHAND));
        livingEntityVar("has_offhand", ctx -> getSlotValue(ctx.entity(), EquipmentSlot.OFFHAND));
        livingEntityVar("has_elytra", ctx -> !EquipmentUtil.getEquippedElytraItem(ctx.entity()).isEmpty());
        livingEntityVar("is_riptide", ctx -> ctx.entity().isAutoSpinAttack());
        livingEntityVar("armor_value", ctx -> ctx.entity().getArmorValue());
        livingEntityVar("hurt_time", ctx -> ctx.entity().hurtTime);
        livingEntityVar("is_close_eyes", ctx -> getEyeCloseState(ctx.animationEvent(), ctx.entity()));
        livingEntityVar("on_ladder", ctx -> ctx.entity().onClimbable());
        livingEntityVar("ladder_facing", new LadderFacingVariable());
        livingEntityVar("arrow_count", ctx -> ctx.entity().getArrowCount());
        livingEntityVar("stinger_count", ctx -> ctx.entity().getStingerCount());

        livingEntityVar("attack_damage", ctx -> ctx.entity().getAttributeValue(Attributes.ATTACK_DAMAGE));
        livingEntityVar("attack_speed", ctx -> ctx.entity().getAttributeValue(Attributes.ATTACK_SPEED));
        livingEntityVar("attack_knockback", ctx -> ctx.entity().getAttributeValue(Attributes.ATTACK_KNOCKBACK));
        livingEntityVar("movement_speed", ctx -> ctx.entity().getAttributeValue(Attributes.MOVEMENT_SPEED));
        livingEntityVar("knockback_resistance", ctx -> ctx.entity().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        livingEntityVar("luck", ctx -> ctx.entity().getAttributeValue(Attributes.LUCK));

        livingEntityVar("block_reach", ctx -> ctx.entity().getAttributeValue(ForgeMod.BLOCK_REACH.get()));
        livingEntityVar("entity_reach", ctx -> ctx.entity().getAttributeValue(ForgeMod.ENTITY_REACH.get()));
        livingEntityVar("swim_speed", ctx -> ctx.entity().getAttributeValue(ForgeMod.SWIM_SPEED.get()));
        livingEntityVar("entity_gravity", ctx -> ctx.entity().getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()));
        livingEntityVar("step_height_addition", ctx -> ctx.entity().getAttributeValue(ForgeMod.STEP_HEIGHT_ADDITION.get()));
        livingEntityVar("nametag_distance", ctx -> ctx.entity().getAttributeValue(ForgeMod.NAMETAG_DISTANCE.get()));


        // 女仆和 YSM 之间不一致的 molang，仅保留防止报错
        function("dump_equipped_item", new EmptyFunction());
        function("dump_relative_block", new EmptyFunction());
        function("bone_pivot_abs", new EmptyFunction());

        var("dump_mods", ctx -> 0);
        var("texture_name", ctx -> StringUtils.EMPTY);
        var("elytra_rot_x", ctx -> 0);
        var("elytra_rot_y", ctx -> 0);
        var("elytra_rot_z", ctx -> 0);

        entityVar("dump_effects", ctx -> 0);
        entityVar("dump_biome", ctx -> 0);
        entityVar("biome_category", ctx -> 0);

        livingEntityVar("rendering_in_inventory", ctx -> false);
        maidEntityVar("food_level", ctx -> 20);

        var("first_person_mod_hide", ctx -> false);
        var("has_left_shoulder_parrot", ctx -> false);
        var("has_right_shoulder_parrot", ctx -> false);
        var("left_shoulder_parrot_variant", ctx -> 0);
        var("right_shoulder_parrot_variant", ctx -> 0);
    }

    private static boolean getEyeCloseState(AnimationEvent<?> animationEvent, LivingEntity player) {
        double remainder = (animationEvent.getAnimationTick() + Math.abs(player.getUUID().getLeastSignificantBits()) % 10) % 90;
        boolean isBlinkTime = 85 < remainder && remainder < 90;
        return player.isSleeping() || isBlinkTime;
    }

    private static boolean getSlotValue(LivingEntity entity, EquipmentSlot slot) {
        return !EquipmentUtil.getEquippedItem(entity, slot).isEmpty();
    }

    private static int getWeather(ClientLevel world) {
        if (world.isThundering()) {
            return 2;
        } else if (world.isRaining()) {
            return 1;
        }
        return 0;
    }

    private static boolean isOpenAir(Entity entity) {
        BlockPos blockpos = entity.blockPosition();
        if (!entity.level.canSeeSky(blockpos)) {
            return false;
        }
        return entity.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() <= blockpos.getY();
    }
}
