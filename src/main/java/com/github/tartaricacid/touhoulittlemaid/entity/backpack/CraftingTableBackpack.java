package com.github.tartaricacid.touhoulittlemaid.entity.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.CraftingTableBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.CraftingTableBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class CraftingTableBackpack extends IMaidBackpack {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "crafting_table");

    @Override
    public void onPutOn(ItemStack stack, Player player, EntityMaid maid) {
    }

    @Override
    public void onTakeOff(ItemStack stack, @Nullable Player player, EntityMaid maid) {
        Item item = stack.getItem();
        if (item instanceof ItemMaidBackpack) {
            if (item == InitItems.MAID_BACKPACK_SMALL.get()) {
                ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), BackpackLevel.SMALL_CAPACITY);
            }
        } else {
            this.dropAllItems(maid);
        }
    }

    @Override
    public MenuProvider getGuiProvider(int entityId) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Crafting Table Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new CraftingTableBackpackContainer(index, playerInventory, entityId);
            }
        };
    }

    @Override
    public int getAvailableMaxContainerIndex() {
        return BackpackLevel.CRAFTING_TABLE_CAPACITY;
    }

    @Override
    public void offsetBackpackItem(PoseStack poseStack) {
    }

    @Nullable
    @Override
    public EntityModel<EntityMaid> getBackpackModel(EntityModelSet modelSet) {
        return new CraftingTableBackpackModel(modelSet.bakeLayer(CraftingTableBackpackModel.LAYER));
    }

    @Nullable
    @Override
    public ResourceLocation getBackpackTexture() {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/crafting_table_backpack.png");
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Item getItem() {
        return Items.CRAFTING_TABLE;
    }
}
