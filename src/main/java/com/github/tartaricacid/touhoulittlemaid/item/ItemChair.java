package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackChairRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemChair extends Item {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";

    public ItemChair() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1)
                .setISTER(() -> TileEntityItemStackChairRenderer::new));
    }

    public static Data getData(ItemStack stack) {
        if (stack.getItem() == InitItems.CHAIR.get()) {
            return Data.deserialization(stack.getOrCreateTag());
        }
        return new Data(DEFAULT_MODEL_ID, 0f, true, false);
    }

    public static ItemStack setData(ItemStack stack, Data data) {
        if (stack.getItem() == InitItems.CHAIR.get()) {
            Data.serialization(stack.getOrCreateTag(), data);
        }
        return stack;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getClickedFace() != Direction.DOWN) {
            World world = context.getLevel();
            BlockPos clickedPos = new BlockItemUseContext(context).getClickedPos();
            AxisAlignedBB boundingBox = EntityChair.TYPE.getDimensions().makeBoundingBox(Vector3d.atBottomCenterOf(clickedPos));
            if (world.noCollision(null, boundingBox, Predicates.alwaysTrue()) && world.getEntities(null, boundingBox).isEmpty()) {
                ItemStack stack = context.getItemInHand();
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) world;
                    ITextComponent customName = null;
                    if (stack.hasCustomHoverName()) {
                        customName = stack.getDisplayName();
                    }
                    EntityChair chair = EntityChair.TYPE.create(serverWorld, stack.getTag(), customName, context.getPlayer(), clickedPos, SpawnReason.SPAWN_EGG, true, true);
                    if (chair == null) {
                        return ActionResultType.FAIL;
                    }
                    addExtraData(context, stack, chair);
                    world.addFreshEntity(chair);
                    world.playSound(null, chair.getX(), chair.getY(), chair.getZ(), SoundEvents.WOOL_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }
                stack.shrink(1);
                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }
        return ActionResultType.FAIL;
    }

    private void addExtraData(ItemUseContext context, ItemStack stack, EntityChair chair) {
        Data data = Data.deserialization(stack.getOrCreateTag());
        chair.setModelId(data.getModelId());
        chair.setMountedHeight(data.getHeight());
        chair.setTameableCanRide(data.isCanRide());
        chair.setNoGravity(data.isNoGravity());
        float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getRotation() - 180) + 22.5F) / 45.0F) * 45.0F;
        chair.moveTo(chair.getX(), chair.getY(), chair.getZ(), yaw, 0.0F);
        chair.setYBodyRot(yaw);
        chair.setYHeadRot(yaw);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(setData(getDefaultInstance(), new Data(DEFAULT_MODEL_ID, 0f, true, false)));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ITextComponent getDescription() {
        return super.getDescription();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.place.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.destroy.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.gui.desc").withStyle(TextFormatting.GRAY));
        // 调试模式，不加国际化
        if (flagIn.isAdvanced() && Screen.hasShiftDown()) {
            Data data = Data.deserialization(stack.getOrCreateTag());
            tooltip.add(new StringTextComponent("Model Id: " + data.getModelId()).withStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent("Mounted Height: " + data.getHeight()).withStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent("Tameable Can Ride: " + data.isCanRide()).withStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent("Is No Gravity: " + data.isNoGravity()).withStyle(TextFormatting.GRAY));
        }
    }

    public static class Data {
        private static final String MODEL_ID_TAG = "ModelId";
        private static final String MOUNTED_HEIGHT_TAG = "MountedHeight";
        private static final String TAMEABLE_CAN_RIDE_TAG = "TameableCanRide";
        private static final String IS_NO_GRAVITY_TAG = "IsNoGravity";

        private final String modelId;
        private final float height;
        private final boolean canRide;
        private final boolean isNoGravity;

        public Data(String modelId, float height, boolean canRide, boolean isNoGravity) {
            this.modelId = modelId;
            this.height = height;
            this.canRide = canRide;
            this.isNoGravity = isNoGravity;
        }

        public static void serialization(@Nonnull CompoundNBT nbt, Data data) {
            nbt.putString(MODEL_ID_TAG, data.getModelId());
            nbt.putFloat(MOUNTED_HEIGHT_TAG, data.getHeight());
            nbt.putBoolean(TAMEABLE_CAN_RIDE_TAG, data.isCanRide());
            nbt.putBoolean(IS_NO_GRAVITY_TAG, data.isNoGravity());
        }

        public static Data deserialization(@Nonnull CompoundNBT nbt) {
            String modelId = DEFAULT_MODEL_ID;
            float height = 0f;
            boolean canRide = true;
            boolean isNoGravity = false;

            if (nbt.contains(MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
                modelId = nbt.getString(MODEL_ID_TAG);
            }

            if (nbt.contains(MOUNTED_HEIGHT_TAG, Constants.NBT.TAG_FLOAT)) {
                height = nbt.getFloat(MOUNTED_HEIGHT_TAG);
            }

            if (nbt.contains(TAMEABLE_CAN_RIDE_TAG, Constants.NBT.TAG_BYTE)) {
                canRide = nbt.getBoolean(TAMEABLE_CAN_RIDE_TAG);
            }

            if (nbt.contains(IS_NO_GRAVITY_TAG, Constants.NBT.TAG_BYTE)) {
                isNoGravity = nbt.getBoolean(IS_NO_GRAVITY_TAG);
            }

            return new Data(modelId, height, canRide, isNoGravity);
        }

        public String getModelId() {
            return modelId;
        }

        public float getHeight() {
            return height;
        }

        public boolean isCanRide() {
            return canRide;
        }

        public boolean isNoGravity() {
            return isNoGravity;
        }
    }
}
