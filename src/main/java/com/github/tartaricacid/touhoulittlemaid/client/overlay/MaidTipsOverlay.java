package com.github.tartaricacid.touhoulittlemaid.client.overlay;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.Map;

public class MaidTipsOverlay implements IGuiOverlay {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tips_icon.png");

    private static Map<Item, MutableComponent> TIPS = Maps.newHashMap();
    private static Map<CheckCondition, MutableComponent> SPECIAL_TIPS = Maps.newHashMap();

    public static void init() {
        MaidTipsOverlay overlay = new MaidTipsOverlay();

        overlay.addTips("overlay.touhou_little_maid.compass.tips", Items.COMPASS);
        overlay.addTips("overlay.touhou_little_maid.golden_apple.tips", Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        overlay.addTips("overlay.touhou_little_maid.potion.tips", Items.POTION);
        overlay.addTips("overlay.touhou_little_maid.milk_bucket.tips", Items.MILK_BUCKET);
        overlay.addTips("overlay.touhou_little_maid.script_book.tips", Items.WRITABLE_BOOK, Items.WRITTEN_BOOK);
        overlay.addTips("overlay.touhou_little_maid.glass_bottle.tips", Items.GLASS_BOTTLE);
        overlay.addTips("overlay.touhou_little_maid.name_tag.tips", Items.NAME_TAG);
        overlay.addTips("overlay.touhou_little_maid.lead.tips", Items.LEAD);
        overlay.addTips("overlay.touhou_little_maid.debug_stick.tips", Items.DEBUG_STICK);
        overlay.addTips("overlay.touhou_little_maid.saddle.tips", Items.SADDLE);

        overlay.addSpecialTips("overlay.touhou_little_maid.ntr_item.tips", (item, maid, player) -> !maid.isOwnedBy(player) && EntityMaid.getNtrItem().test(item));
        overlay.addSpecialTips("overlay.touhou_little_maid.remove_backpack.tips", (item, maid, player) -> maid.isOwnedBy(player) && maid.hasBackpack() && item.is(Tags.Items.SHEARS));

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMaidTips(overlay);
        }

        TIPS = ImmutableMap.copyOf(TIPS);
        SPECIAL_TIPS = ImmutableMap.copyOf(SPECIAL_TIPS);
    }

    private static MutableComponent checkSpecialTips(ItemStack mainhandItem, EntityMaid maid, LocalPlayer player) {
        for (Map.Entry<CheckCondition, MutableComponent> entry : SPECIAL_TIPS.entrySet()) {
            CheckCondition condition = entry.getKey();
            MutableComponent text = entry.getValue();
            if (condition.test(mainhandItem, maid, player)) {
                return text;
            }
        }
        return null;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        Options options = minecraft.options;
        if (!options.getCameraType().isFirstPerson()) {
            return;
        }
        if (minecraft.gameMode == null || minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }
        if (!(minecraft.hitResult instanceof EntityHitResult result)) {
            return;
        }
        if (!(result.getEntity() instanceof EntityMaid maid)) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        if (!maid.isAlive()) {
            return;
        }
        MutableComponent tip = null;
        if (maid.isOwnedBy(player)) {
            tip = TIPS.get(player.getMainHandItem().getItem());
        }
        if (tip == null) {
            tip = checkSpecialTips(player.getMainHandItem(), maid, player);
        }
        if (tip != null) {
            gui.setupOverlayRenderState(true, false);
            List<FormattedCharSequence> split = minecraft.font.split(tip, 150);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            guiGraphics.renderItem(player.getMainHandItem(), screenWidth / 2 - 8, offset);
            guiGraphics.blit(ICON, screenWidth / 2 + 2, offset - 4, 16, 16, 16, 16, 16, 16);
            offset += 18;
            for (FormattedCharSequence sequence : split) {
                int width = minecraft.font.width(sequence);
                guiGraphics.drawString(minecraft.font, sequence, (screenWidth - width) / 2, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }

    public void addTips(String key, Item... items) {
        for (Item item : items) {
            TIPS.put(item, Component.translatable(key));
        }
    }

    public void addSpecialTips(String key, CheckCondition condition) {
        SPECIAL_TIPS.put(condition, Component.translatable(key));
    }

    public interface CheckCondition {
        boolean test(ItemStack mainhandItem, EntityMaid maid, LocalPlayer player);
    }
}
