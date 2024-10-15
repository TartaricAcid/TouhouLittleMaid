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
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.*;

public class ItemChair extends Item {
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";
    public static final IClientItemExtensions ITEM_EXTENSIONS = FMLEnvironment.dist == Dist.CLIENT ? new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new TileEntityItemStackChairRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    } : null;

    public ItemChair() {
        super((new Properties()).stacksTo(1));
    }

    public static Data getData(ItemStack stack) {
        if (stack.getItem() == InitItems.CHAIR.get()) {
            return Data.deserialization(stack);
        }
        return new Data(DEFAULT_MODEL_ID, 0f, true, false);
    }

    public static ItemStack setData(ItemStack stack, Data data) {
        if (stack.getItem() == InitItems.CHAIR.get()) {
            Data.serialization(stack, data);
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public static void fillItemCategory(CreativeModeTab.Output items) {
        for (String key : CustomPackLoader.CHAIR_MODELS.getModelIdSet()) {
            float height = CustomPackLoader.CHAIR_MODELS.getModelMountedYOffset(key);
            boolean canRide = CustomPackLoader.CHAIR_MODELS.getModelTameableCanRide(key);
            boolean isNoGravity = CustomPackLoader.CHAIR_MODELS.getModelNoGravity(key);
            items.accept(setData(new ItemStack(InitItems.CHAIR.get()), new Data(key, height, canRide, isNoGravity)));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.DOWN) {
            Level world = context.getLevel();
            BlockPos clickedPos = new BlockPlaceContext(context).getClickedPos();
            AABB boundingBox = EntityChair.TYPE.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(clickedPos));
            if (world.noCollision(boundingBox) && world.getEntities(null, boundingBox).isEmpty()) {
                ItemStack stack = context.getItemInHand();
                if (world instanceof ServerLevel serverWorld) {
                    EntityChair chair = getSpawnChair(serverWorld, context.getPlayer(), stack, context.getClickedPos(), context.getRotation());
                    if (chair == null) {
                        return InteractionResult.FAIL;
                    }
                    world.addFreshEntity(chair);
                    world.playSound(null, chair.getX(), chair.getY(), chair.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                }
                stack.shrink(1);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    public static EntityChair getSpawnChair(ServerLevel serverWorld, @Nullable Player player, ItemStack stack, BlockPos pos, float rotation) {
        EntityChair chair = EntityChair.TYPE.create(serverWorld, (e) -> {
            if (stack.get(DataComponents.CUSTOM_NAME) != null) {
                e.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
            }
        }, pos, MobSpawnType.SPAWN_EGG, true, true);
        if (chair != null) {
            addExtraData(player, stack, chair, rotation);
        }
        return chair;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    private static void addExtraData(@Nullable Player player, ItemStack stack, EntityChair chair, float rotation) {
        Data data = Data.deserialization(stack);
        chair.setModelId(data.modelId());
        chair.setMountedHeight(data.height());
        chair.setTameableCanRide(data.canRide());
        chair.setNoGravity(data.isNoGravity());
        chair.setOwner(player);
        float yaw = (float) Mth.floor((Mth.wrapDegrees(rotation - 180) + 22.5F) / 45.0F) * 45.0F;
        chair.moveTo(chair.getX(), chair.getY(), chair.getZ(), yaw, 0.0F);
        chair.setYBodyRot(yaw);
        chair.setYHeadRot(yaw);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ItemChair.Data data = getData(stack);
            if (CustomPackLoader.CHAIR_MODELS.getInfo(data.modelId()).isPresent()) {
                String name = CustomPackLoader.CHAIR_MODELS.getInfo(data.modelId()).get().getName();
                return ParseI18n.parse(name);
            }
        }
        return super.getName(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.place.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.destroy.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.chair.gui.desc").withStyle(ChatFormatting.GRAY));
        // 调试模式，不加国际化
        if (flagIn.isAdvanced() && Screen.hasShiftDown()) {
            Data data = Data.deserialization(stack);
            tooltip.add(Component.literal("Model Id: " + data.modelId()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Mounted Height: " + data.height()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Tameable Can Ride: " + data.canRide()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Is No Gravity: " + data.isNoGravity()).withStyle(ChatFormatting.GRAY));
        }
    }

    public record Data(String modelId, float height, boolean canRide, boolean isNoGravity) {
        public static void serialization(ItemStack stack, Data data) {
            stack.set(MODEL_ID_TAG, data.modelId());
            stack.set(MOUNTED_HEIGHT_TAG, data.height());
            stack.set(TAMEABLE_CAN_RIDE_TAG, data.canRide());
            stack.set(IS_NO_GRAVITY_TAG, data.isNoGravity());
        }

        public static Data deserialization(ItemStack stack) {
            String modelId = Objects.requireNonNullElse(stack.get(MODEL_ID_TAG), DEFAULT_MODEL_ID);
            float height = Objects.requireNonNullElse(stack.get(MOUNTED_HEIGHT_TAG), 0f);
            boolean canRide = Objects.requireNonNullElse(stack.get(TAMEABLE_CAN_RIDE_TAG), true);
            boolean isNoGravity = Objects.requireNonNullElse(stack.get(IS_NO_GRAVITY_TAG), false);
            return new Data(modelId, height, canRide, isNoGravity);
        }
    }
}
