package com.github.tartaricacid.touhoulittlemaid.client.tooltip;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.YsmMaidInfo;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class ClientMaidTooltip implements ClientTooltipComponent {
    private final @Nullable MaidModelInfo info;
    private final YsmMaidInfo ysmMaidInfo;
    private final MutableComponent name;
    private final String customName;

    public ClientMaidTooltip(ItemMaidTooltip tooltip) {
        this.info = CustomPackLoader.MAID_MODELS.getInfo(tooltip.modelId()).orElse(null);
        this.ysmMaidInfo = tooltip.ysmMaidInfo();
        this.name = getName(this.info, this.ysmMaidInfo);
        this.customName = tooltip.customName();
    }

    public MutableComponent getName(MaidModelInfo info, YsmMaidInfo ysmMaidInfo) {
        // 优先使用 YSM 模型名称
        if (YsmCompat.isInstalled() && ysmMaidInfo.isYsmModel()) {
            MutableComponent name = ysmMaidInfo.name();
            if (name.equals(Component.empty())) {
                return Component.literal(ysmMaidInfo.modelId());
            }
            return name;
        }

        // 然后才是默认模型名
        if (info == null) {
            return Component.empty();
        }
        return Component.translatable(ParseI18n.getI18nKey(info.getName()));
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(font.width(this.name), 50);
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        if (info == null) {
            return;
        }
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }

        MutableComponent customNameComponent = null;
        if (StringUtils.isNotBlank(customName)) {
            customNameComponent = Component.Serializer.fromJson(customName);
            if (customNameComponent != null) {
                guiGraphics.drawString(font, customNameComponent.withStyle(ChatFormatting.GRAY), pX, pY + 2, 0xFFFFFF);
            }
        } else {
            guiGraphics.drawString(font, name.withStyle(ChatFormatting.GRAY), pX, pY + 2, 0xFFFFFF);
        }

        int width = this.getWidth(font);
        int posX = pX + width / 2;
        int posY = pY + 64;
        double rot = ((System.currentTimeMillis() / 25.0) % 360);
        Quaternionf pose = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf rotation = (new Quaternionf()).rotateY((float) Math.toRadians(rot));
        pose.mul(rotation);
        EntityMaid maid;
        try {
            maid = (EntityMaid) EntityCacheUtil.ENTITY_CACHE.get(EntityMaid.TYPE, () -> {
                Entity e = EntityMaid.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            TouhouLittleMaid.LOGGER.error("Failed to render maid tooltip preview", e);
            return;
        }
        clearMaidDataResidue(maid, false);
        if (StringUtils.isNotBlank(customName)) {
            maid.setCustomName(customNameComponent);
        }
        if (info.getEasterEgg() != null) {
            maid.setModelId(EASTER_EGG_MODEL);
        } else {
            maid.setModelId(info.getModelId().toString());
        }

        // YSM 渲染运用
        if (YsmCompat.isInstalled() && ysmMaidInfo.isYsmModel()) {
            maid.setIsYsmModel(true);
            maid.setYsmModel(ysmMaidInfo.modelId(), ysmMaidInfo.textureId(), ysmMaidInfo.name());
        } else {
            maid.setIsYsmModel(false);
        }

        guiGraphics.enableScissor(pX, posY - 50, pX + width, posY);
        InventoryScreen.renderEntityInInventory(guiGraphics, posX, posY, (int) (25 * info.getRenderItemScale()), pose, null, maid);
        guiGraphics.disableScissor();
    }
}
