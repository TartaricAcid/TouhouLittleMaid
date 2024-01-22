package com.github.tartaricacid.touhoulittlemaid.client.overlay;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class MaidTipsOverlay {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tips_icon.png");
    private static final Map<Item, ITextComponent> TIPS = Maps.newHashMap();

    public static void init() {
        addTips("overlay.touhou_little_maid.compass.tips", Items.COMPASS);
        addTips("overlay.touhou_little_maid.golden_apple.tips", Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        addTips("overlay.touhou_little_maid.potion.tips", Items.POTION);
        addTips("overlay.touhou_little_maid.milk_bucket.tips", Items.MILK_BUCKET);
        addTips("overlay.touhou_little_maid.script_book.tips", Items.WRITABLE_BOOK, Items.WRITTEN_BOOK);
        addTips("overlay.touhou_little_maid.glass_bottle.tips", Items.GLASS_BOTTLE);
        addTips("overlay.touhou_little_maid.name_tag.tips", Items.NAME_TAG);
        addTips("overlay.touhou_little_maid.lead.tips", Items.LEAD);
    }

    @SubscribeEvent
    public static void render(RenderGameOverlayEvent.Text event) {
        Minecraft minecraft = Minecraft.getInstance();
        GameSettings options = minecraft.options;
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (!(minecraft.gui instanceof ForgeIngameGui)) {
            return;
        }
        ForgeIngameGui gui = (ForgeIngameGui) minecraft.gui;
        if (!options.getCameraType().isFirstPerson()) {
            return;
        }
        if (minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        if (!(minecraft.hitResult instanceof EntityRayTraceResult)) {
            return;
        }
        EntityRayTraceResult result = (EntityRayTraceResult) minecraft.hitResult;
        if (!(result.getEntity() instanceof EntityMaid)) {
            return;
        }
        EntityMaid maid = (EntityMaid) result.getEntity();
        ClientPlayerEntity player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!maid.isAlive() || !maid.isOwnedBy(player)) {
            return;
        }
        ITextComponent tip = TIPS.get(player.getMainHandItem().getItem());
        if (tip != null) {
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();
            MatrixStack poseStack = event.getMatrixStack();
            List<IReorderingProcessor> split = minecraft.font.split(tip, 150);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            Minecraft.getInstance().getItemRenderer().renderGuiItem(player.getMainHandItem(), screenWidth / 2 - 8, offset);
            Minecraft.getInstance().textureManager.bind(ICON);
            AbstractGui.blit(poseStack, screenWidth / 2 + 2, offset - 4, 16, 16, 16, 16, 16, 16);
            offset += 18;
            for (IReorderingProcessor sequence : split) {
                int width = minecraft.font.width(sequence);
                minecraft.font.drawShadow(poseStack, sequence, (screenWidth - width) / 2.0f, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }

    private static void addTips(String key, Item... items) {
        for (Item item : items) {
            TIPS.put(item, new TranslationTextComponent(key));
        }
    }
}
