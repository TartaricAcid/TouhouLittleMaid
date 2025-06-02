package com.github.tartaricacid.touhoulittlemaid.client.overlay;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
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
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.config.subconfig.RenderConfig.*;

public class MaidTipsOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tips_icon.png");

    private static Map<Item, MutableComponent> TIPS = Maps.newHashMap();
    private static Map<Item, ModConfigSpec.BooleanValue> TIPS_CONFIG = Maps.newHashMap();
    private static Map<CheckCondition, MutableComponent> SPECIAL_TIPS = Maps.newHashMap();

    public static void init() {
        MaidTipsOverlay overlay = new MaidTipsOverlay();

        overlay.addTips("overlay.touhou_little_maid.compass.tips", ENABLE_COMPASS_TIP, Items.COMPASS);
        overlay.addTips("overlay.touhou_little_maid.golden_apple.tips", ENABLE_GOLDEN_APPLE_TIP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        overlay.addTips("overlay.touhou_little_maid.potion.tips", ENABLE_POTION_TIP, Items.POTION);
        overlay.addTips("overlay.touhou_little_maid.milk_bucket.tips", ENABLE_MILK_BUCKET_TIP, Items.MILK_BUCKET);
        overlay.addTips("overlay.touhou_little_maid.glass_bottle.tips", ENABLE_GLASS_BOTTLE_TIP, Items.GLASS_BOTTLE);
        overlay.addTips("overlay.touhou_little_maid.name_tag.tips", ENABLE_NAME_TAG_TIP, Items.NAME_TAG);
        overlay.addTips("overlay.touhou_little_maid.lead.tips", ENABLE_LEAD_TIP, Items.LEAD);
        overlay.addTips("overlay.touhou_little_maid.debug_stick.tips", Items.DEBUG_STICK);
        overlay.addTips("overlay.touhou_little_maid.saddle.tips", ENABLE_SADDLE_TIP, Items.SADDLE);

        overlay.addSpecialTips("overlay.touhou_little_maid.ntr_item.tips", (item, maid, player) -> !maid.isOwnedBy(player) && EntityMaid.getNtrItem().test(item));
        overlay.addSpecialTips("overlay.touhou_little_maid.remove_backpack.tips", MaidTipsOverlay::checkShears);

        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMaidTips(overlay);
        }

        TIPS = ImmutableMap.copyOf(TIPS);
        TIPS_CONFIG = ImmutableMap.copyOf(TIPS_CONFIG);
        SPECIAL_TIPS = ImmutableMap.copyOf(SPECIAL_TIPS);
    }

    private static boolean checkShears(ItemStack item, EntityMaid maid, LocalPlayer player) {
        if (!ENABLE_SHEARS_TIP.get()) {
            return false;
        }
        return maid.isOwnedBy(player) && maid.hasBackpack() && item.is(Tags.Items.TOOLS_SHEAR);
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
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
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
        // 如果女仆和玩家同骑乘一个实体，很容易出现提示闪烁问题，故禁用
        if (player.getVehicle() != null && player.getVehicle().equals(maid.getVehicle())) {
            return;
        }

        MutableComponent tip;
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        if (maid.isOwnedBy(player) && TIPS.containsKey(item)) {
            boolean configIsNull = !TIPS_CONFIG.containsKey(item);
            boolean configIsEnable = TIPS_CONFIG.containsKey(item) && TIPS_CONFIG.get(item).get();
            if (configIsNull || configIsEnable) {
                tip = TIPS.get(item);
            } else {
                tip = checkSpecialTips(itemStack, maid, player);
            }
        } else {
            tip = checkSpecialTips(itemStack, maid, player);
        }
        if (tip != null) {
            int screenHeight = guiGraphics.guiHeight();
            int screenWidth = guiGraphics.guiWidth();
            List<FormattedCharSequence> split = minecraft.font.split(tip, 120);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            guiGraphics.renderItem(itemStack, screenWidth / 2 + 32, offset);
            guiGraphics.blit(ICON, screenWidth / 2 + 42, offset - 4, 16, 16, 16, 16, 16, 16);
            offset += 18;
            for (FormattedCharSequence sequence : split) {
                guiGraphics.drawString(minecraft.font, sequence, screenWidth / 2 + 32, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }

    public void addTips(String key, Item... items) {
        addTips(key, null, items);
    }

    public void addTips(String key, @Nullable ModConfigSpec.BooleanValue config, Item... items) {
        for (Item item : items) {
            TIPS.put(item, Component.translatable(key));
            if (config != null) {
                TIPS_CONFIG.put(item, config);
            }
        }
    }

    public void addSpecialTips(String key, CheckCondition condition) {
        SPECIAL_TIPS.put(condition, Component.translatable(key));
    }

    public interface CheckCondition {
        boolean test(ItemStack mainhandItem, EntityMaid maid, LocalPlayer player);
    }
}
