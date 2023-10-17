package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.TileEntityItemStackChairRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemChair extends Item {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";

    public ItemChair() {
        super((new Properties()).stacksTo(1));
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                Minecraft minecraft = Minecraft.getInstance();
                return new TileEntityItemStackChairRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.DOWN) {
            Level world = context.getLevel();
            BlockPos clickedPos = new BlockPlaceContext(context).getClickedPos();
            AABB boundingBox = EntityChair.TYPE.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(clickedPos));
            if (world.noCollision(boundingBox) && world.getEntities(null, boundingBox).isEmpty()) {
                ItemStack stack = context.getItemInHand();
                if (world instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel) world;
                    EntityChair chair = EntityChair.TYPE.create(serverWorld, stack.getTag(), (e) -> {
                        if (stack.hasCustomHoverName()) {
                            e.setCustomName(stack.getDisplayName());
                        }
                    }, context.getClickedPos(), MobSpawnType.SPAWN_EGG, true, true);
                    if (chair == null) {
                        return InteractionResult.FAIL;
                    }
                    addExtraData(context, stack, chair);
                    world.addFreshEntity(chair);
                    world.playSound(null, chair.getX(), chair.getY(), chair.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                }
                stack.shrink(1);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    private void addExtraData(UseOnContext context, ItemStack stack, EntityChair chair) {
        Data data = Data.deserialization(stack.getOrCreateTag());
        chair.setModelId(data.getModelId());
        chair.setMountedHeight(data.getHeight());
        chair.setTameableCanRide(data.isCanRide());
        chair.setNoGravity(data.isNoGravity());
        chair.setOwner(context.getPlayer());
        float yaw = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180) + 22.5F) / 45.0F) * 45.0F;
        chair.moveTo(chair.getX(), chair.getY(), chair.getZ(), yaw, 0.0F);
        chair.setYBodyRot(yaw);
        chair.setYHeadRot(yaw);
    }


    public static void fillItemCategory(CreativeModeTab.Output items) {
        for (String key : CustomPackLoader.CHAIR_MODELS.getModelIdSet()) {
            float height = CustomPackLoader.CHAIR_MODELS.getModelMountedYOffset(key);
            boolean canRide = CustomPackLoader.CHAIR_MODELS.getModelTameableCanRide(key);
            boolean isNoGravity = CustomPackLoader.CHAIR_MODELS.getModelNoGravity(key);
            items.accept(setData(new ItemStack(InitItems.CHAIR.get()), new Data(key, height, canRide, isNoGravity)));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ItemChair.Data data = getData(stack);
            if (CustomPackLoader.CHAIR_MODELS.getInfo(data.getModelId()).isPresent()) {
                String name = CustomPackLoader.CHAIR_MODELS.getInfo(data.getModelId()).get().getName();
                return ParseI18n.parse(name);
            }
        }
        return super.getName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.place.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.destroy.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.gui.desc").withStyle(ChatFormatting.GRAY));
        // 调试模式，不加国际化
        if (flagIn.isAdvanced() && Screen.hasShiftDown()) {
            Data data = Data.deserialization(stack.getOrCreateTag());
            tooltip.add(Component.literal("Model Id: " + data.getModelId()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Mounted Height: " + data.getHeight()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Tameable Can Ride: " + data.isCanRide()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Is No Gravity: " + data.isNoGravity()).withStyle(ChatFormatting.GRAY));
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

        public static void serialization(@Nonnull CompoundTag nbt, Data data) {
            nbt.putString(MODEL_ID_TAG, data.getModelId());
            nbt.putFloat(MOUNTED_HEIGHT_TAG, data.getHeight());
            nbt.putBoolean(TAMEABLE_CAN_RIDE_TAG, data.isCanRide());
            nbt.putBoolean(IS_NO_GRAVITY_TAG, data.isNoGravity());
        }

        public static Data deserialization(@Nonnull CompoundTag nbt) {
            String modelId = DEFAULT_MODEL_ID;
            float height = 0f;
            boolean canRide = true;
            boolean isNoGravity = false;

            if (nbt.contains(MODEL_ID_TAG, Tag.TAG_STRING)) {
                modelId = nbt.getString(MODEL_ID_TAG);
            }

            if (nbt.contains(MOUNTED_HEIGHT_TAG, Tag.TAG_FLOAT)) {
                height = nbt.getFloat(MOUNTED_HEIGHT_TAG);
            }

            if (nbt.contains(TAMEABLE_CAN_RIDE_TAG, Tag.TAG_BYTE)) {
                canRide = nbt.getBoolean(TAMEABLE_CAN_RIDE_TAG);
            }

            if (nbt.contains(IS_NO_GRAVITY_TAG, Tag.TAG_BYTE)) {
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
