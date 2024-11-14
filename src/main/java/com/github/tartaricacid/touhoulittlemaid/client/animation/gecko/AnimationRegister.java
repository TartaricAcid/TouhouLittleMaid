package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.function.BiPredicate;

public class AnimationRegister {
    private static final double MIN_SPEED = 0.05;

    public static void registerAnimationState() {
        register("death", ILoopType.EDefaultLoopTypes.PLAY_ONCE, Priority.HIGHEST, (maid, event) -> maid.asEntity().isDeadOrDying());
        register("sleep", Priority.HIGHEST, (maid, event) -> maid.asEntity().getPose() == Pose.SLEEPING);

        register("gomoku", Priority.HIGH, (maid, event) -> sitInJoy(maid, Type.GOMOKU));
        register("bookshelf", Priority.HIGH, (maid, event) -> sitInJoy(maid, Type.BOOKSHELF));
        register("computer", Priority.HIGH, (maid, event) -> sitInJoy(maid, Type.COMPUTER));
        register("keyboard", Priority.HIGH, (maid, event) -> sitInJoy(maid, Type.KEYBOARD));
        register("picnic", Priority.HIGH, (maid, event) -> sitInJoy(maid, Type.ON_HOME_MEAL));

        register("boat", Priority.HIGH, (maid, event) -> maid.asEntity().getVehicle() instanceof Boat);
        register("chair", Priority.HIGH, (maid, event) -> maid.asEntity().isPassenger());
        register("sit", Priority.HIGH, (maid, event) -> maid.isMaidInSittingPose());

        register("swim_stand", Priority.NORMAL, (maid, event) -> maid.asEntity().isInWater());
        register("attacked", ILoopType.EDefaultLoopTypes.PLAY_ONCE, Priority.NORMAL, (maid, event) -> maid.asEntity().hurtTime > 0);
        register("jump", Priority.NORMAL, (maid, event) -> !maid.asEntity().onGround() && !maid.asEntity().isInWater());

        register("run", Priority.LOW, (maid, event) -> maid.asEntity().onGround() && maid.asEntity().isSprinting());
        register("walk", Priority.LOW, (maid, event) -> maid.asEntity().onGround() && event.getLimbSwingAmount() > MIN_SPEED);

        register("idle", Priority.LOWEST, (maid, event) -> true);
    }

    private static boolean sitInJoy(IMaid maid, Type type) {
        return maid.asEntity().getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(type.getTypeName());
    }

    private static void register(String animationName, ILoopType loopType, int priority, BiPredicate<IMaid, AnimationEvent<?>> predicate) {
        AnimationManager manager = AnimationManager.getInstance();
        manager.register(new AnimationState(animationName, loopType, priority, predicate));
    }

    private static void register(String animationName, int priority, BiPredicate<IMaid, AnimationEvent<?>> predicate) {
        register(animationName, ILoopType.EDefaultLoopTypes.LOOP, priority, predicate);
    }
}