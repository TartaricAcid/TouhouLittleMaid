package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.PicnicBasketRender;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.PicnicBasketContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemContainerTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemPicnicBasket extends BlockItem implements MenuProvider {
    public static final IClientItemExtensions itemExtensions = new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new PicnicBasketRender(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    };
    private static final int PICNIC_BASKET_SIZE = 9;
    private static final String PICNIC_BASKET_TAG = "PicnicBasketContainer";

    public ItemPicnicBasket(Block block) {
        super(block, (new Properties()).stacksTo(1));
    }

    public static ItemStackHandler getContainer(ItemStack stack) {
        ItemStackHandler handler = new ItemStackHandler(PICNIC_BASKET_SIZE);
        if (stack.getItem() == InitItems.PICNIC_BASKET.get()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(PICNIC_BASKET_TAG, Tag.TAG_COMPOUND)) {
                handler.deserializeNBT(tag.getCompound(PICNIC_BASKET_TAG));
            }
        }
        return handler;
    }

    public static void setContainer(ItemStack stack, ItemStackHandler itemStackHandler) {
        if (stack.getItem() == InitItems.PICNIC_BASKET.get()) {
            stack.getOrCreateTag().put(PICNIC_BASKET_TAG, itemStackHandler.serializeNBT());
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND && playerIn instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(this);
            return InteractionResultHolder.success(playerIn.getMainHandItem());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        ItemStackHandler container = getContainer(stack);
        return Optional.of(new ItemContainerTooltip(container));
    }

    @Override
    public String getDescriptionId() {
        return "item.touhou_little_maid.picnic_basket";
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PicnicBasketContainer(containerId, playerInventory, player.getMainHandItem());
    }
}
