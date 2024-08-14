package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.PicnicBasketRender;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.PicnicBasketContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemContainerTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemPicnicBasket extends BlockItem implements MenuProvider {
    public static final IClientItemExtensions itemExtensions = FMLEnvironment.dist == Dist.CLIENT? new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new PicnicBasketRender(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    }: null;
    private static final int PICNIC_BASKET_SIZE = 9;

    public ItemPicnicBasket(Block block) {
        super(block, (new Properties()).stacksTo(1));
    }

    public static ItemStackHandler getContainer(ItemStack stack) {
        ItemStackHandler handler = new ItemStackHandler(PICNIC_BASKET_SIZE);
        if (stack.getItem() == InitItems.PICNIC_BASKET.get()) {
            ItemContainerContents container = stack.get(DataComponents.CONTAINER);
            if (container != null) {
                assert container.getSlots() <= PICNIC_BASKET_SIZE;
                for (int i = 0; i < container.getSlots(); i++) {
                    handler.setStackInSlot(i, container.getStackInSlot(i));
                }
            }
        }
        return handler;
    }

    public static void setContainer(ItemStack stack, ItemStackHandler itemStackHandler) {
        if (stack.getItem() == InitItems.PICNIC_BASKET.get()) {
            NonNullList<ItemStack> items = NonNullList.withSize(PICNIC_BASKET_SIZE, ItemStack.EMPTY);
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                items.set(i, itemStackHandler.getStackInSlot(i));
            }
            ItemContainerContents container = ItemContainerContents.fromItems(items);
            stack.set(DataComponents.CONTAINER, container);
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
