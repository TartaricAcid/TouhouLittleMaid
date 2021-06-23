package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackChairRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemChair extends Item {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";

    public ItemChair() {
        super((new Properties()).tab(InitItems.MAIN_TAB).stacksTo(1)
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
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (context.getClickedFace() == Direction.UP) {
            ItemStack itemstack = context.getItemInHand();
            float yaw = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
            BlockPos pos = context.getClickedPos();
            EntityChair chair = new EntityChair(context.getLevel(), pos.getX() + 0.5, pos.above().getY(), pos.getZ() + 0.5, yaw);
            Data data = Data.deserialization(stack.getOrCreateTag());
            chair.setModelId(data.getModelId());
            chair.setMountedHeight(data.getHeight());
            chair.setTameableCanRide(data.isCanRide());
            chair.setNoGravity(data.isNoGravity());
            // 应用命名
            if (itemstack.hasCustomHoverName()) {
                chair.setCustomName(itemstack.getDisplayName());
            }
            // 物品消耗，实体生成
            if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
            if (!context.getLevel().isClientSide) {
                context.getLevel().addFreshEntity(chair);
            }
            chair.yHeadRot = yaw;
            chair.playSound(SoundEvents.WOOL_PLACE, 1.0f, 1.0f);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
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
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.place.desc"));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.destroy.desc"));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.chair.gui.desc"));
        // 调试模式，不加国际化
        if (flagIn.isAdvanced() && Screen.hasShiftDown()) {
            Data data = Data.deserialization(stack.getOrCreateTag());
            tooltip.add(new StringTextComponent("Model Id: " + data.getModelId()));
            tooltip.add(new StringTextComponent("Mounted Height: " + data.getHeight()));
            tooltip.add(new StringTextComponent("Tameable Can Ride: " + data.isCanRide()));
            tooltip.add(new StringTextComponent("Is No Gravity: " + data.isNoGravity()));
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
