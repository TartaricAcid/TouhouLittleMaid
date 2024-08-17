package com.github.tartaricacid.touhoulittlemaid.client.gui.sound;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.SoundElementButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.SoundPackButton;
import com.github.tartaricacid.touhoulittlemaid.client.sound.CustomSoundLoader;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.network.pack.SetMaidSoundIdPackage;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.audio.SoundBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MaidSoundPackGui extends Screen {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_custom_sound.png");
    private final int packPerSize = 4;
    private final int soundPerSize = 13;
    private final EntityMaid maid;
    private String selectSoundId = null;
    private int startX;
    private int startY;
    private int packPage = 0;
    private int soundPage = 0;
    private int packMaxPage = 0;
    private int soundMaxPage = 0;

    public MaidSoundPackGui(EntityMaid maid) {
        super(Component.literal("Maid Custom Sound Pack GUI"));
        this.maid = maid;
    }

    @Override
    protected void init() {
        this.clearWidgets();

        this.startX = (width - 400) / 2;
        this.startY = (height - 220) / 2;
        this.packMaxPage = (CustomSoundLoader.CACHE.size() - 1) / packPerSize;
        this.soundMaxPage = 0;

        this.addSoundPackButtons();
        this.addPackPageButtons();
        if (StringUtils.isNotBlank(selectSoundId) && CustomSoundLoader.CACHE.containsKey(selectSoundId)) {
            this.addSoundElementButtons();
            this.addSoundOtherButtons();
            this.addSoundElementPageButtons();
        }
    }

    private void addPackPageButtons() {
        this.addRenderableWidget(new FlatColorButton(startX + 5, startY + 201, 32, 16, Component.literal("<"), (b) -> {
            if (this.packPage > 0) {
                packPage--;
                this.init();
            }
        }));

        this.addRenderableWidget(new FlatColorButton(startX + 203, startY + 201, 32, 16, Component.literal(">"), (b) -> {
            if ((packPage + 1) * packPerSize < CustomSoundLoader.CACHE.size()) {
                packPage++;
                this.init();
            }
        }));
    }

    private void addSoundElementButtons() {
        int yOffset = 41;
        boolean otherColor = false;
        Map<ResourceLocation, List<SoundBuffer>> buffers = CustomSoundLoader.getSoundCache(selectSoundId).getBuffers();
        List<ResourceLocation> soundIds = List.copyOf(buffers.keySet());
        this.soundMaxPage = (buffers.size() - 1) / soundPerSize;
        int startSoundIndex = soundPage * soundPerSize;
        if (startSoundIndex >= soundIds.size()) {
            this.soundPage = 0;
            startSoundIndex = 0;
        }
        int endSoundIndex = Math.min(soundIds.size(), startSoundIndex + soundPerSize);
        for (int i = startSoundIndex; i < endSoundIndex; i++) {
            ResourceLocation soundEvent = soundIds.get(i);
            this.addRenderableWidget(new SoundElementButton(startX + 245, startY + yOffset, 152, 12, soundEvent, buffers.get(soundEvent), otherColor, (b) -> {
                SoundElementButton soundButton = (SoundElementButton) b;
                SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(soundButton.getSoundEvent());
                if (minecraft != null && event != null) {
                    minecraft.getSoundManager().play(new MaidSoundInstance(event, this.selectSoundId, this.maid, true));
                }
            }).setTooltips("tooltips.touhou_little_maid.custom_sound.play_sound"));
            otherColor = !otherColor;
            yOffset += 12;
        }
    }

    private void addSoundOtherButtons() {
        this.addRenderableWidget(new FlatColorButton(startX + 245, startY + 19, 110, 18, Component.translatable("gui.touhou_little_maid.custom_sound.pack.apply"), (b) -> {
            if (StringUtils.isNotBlank(selectSoundId) && CustomSoundLoader.CACHE.containsKey(selectSoundId)) {
                this.maid.setSoundPackId(this.selectSoundId);
                PacketDistributor.sendToServer(new SetMaidSoundIdPackage(this.maid.getId(), this.selectSoundId));
                this.init();
            }
        }));

        this.addRenderableWidget(new FlatColorButton(startX + 358, startY + 19, 18, 18, Component.empty(), (b) -> {
            if (StringUtils.isNotBlank(selectSoundId) && CustomSoundLoader.CACHE.containsKey(selectSoundId)) {
                String url = CustomSoundLoader.getSoundCache(selectSoundId).getInfo().getUrl();
                if (StringUtils.isNotBlank(url) && minecraft != null) {
                    minecraft.setScreen(new ConfirmLinkScreen(yes -> {
                        if (yes) {
                            Util.getPlatform().openUri(url);
                        }
                        minecraft.setScreen(this);
                    }, url, false));
                }
            }
        }).setTooltips("tooltips.touhou_little_maid.custom_sound.open_url"));

        this.addRenderableWidget(new FlatColorButton(startX + 379, startY + 19, 18, 18, Component.empty(), (b) -> {
            if (minecraft != null) {
                minecraft.getSoundManager().play(new MaidSoundInstance(InitSounds.MAID_CREDIT.get(), this.selectSoundId, this.maid, true));
            }
        }).setTooltips("tooltips.touhou_little_maid.custom_sound.credit"));
    }

    private void addSoundElementPageButtons() {
        this.addRenderableWidget(new FlatColorButton(startX + 245, startY + 201, 16, 16, Component.literal("<"), (b) -> {
            if (this.soundPage > 0) {
                soundPage--;
                this.init();
            }
        }));

        this.addRenderableWidget(new FlatColorButton(startX + 381, startY + 201, 16, 16, Component.literal(">"), (b) -> {
            if (StringUtils.isNotBlank(selectSoundId) && CustomSoundLoader.CACHE.containsKey(selectSoundId)) {
                Map<ResourceLocation, List<SoundBuffer>> buffersIn = CustomSoundLoader.getSoundCache(selectSoundId).getBuffers();
                if ((soundPage + 1) * soundPerSize < buffersIn.size()) {
                    soundPage++;
                    this.init();
                }
            }
        }));
    }

    private void addSoundPackButtons() {
        int yOffset = 19;
        List<String> soundPackIds = new ArrayList<>(CustomSoundLoader.CACHE.keySet());
        if (soundPackIds.contains(TouhouLittleMaid.MOD_ID)) {
            Collections.swap(soundPackIds, soundPackIds.indexOf(TouhouLittleMaid.MOD_ID), 0);
        }
        int startPackIndex = packPage * packPerSize;
        if (startPackIndex >= soundPackIds.size()) {
            this.packPage = 0;
            startPackIndex = 0;
        }
        int endPackIndex = Math.min(soundPackIds.size(), startPackIndex + packPerSize);
        for (int i = startPackIndex; i < endPackIndex; i++) {
            String soundId = soundPackIds.get(i);
            SoundPackInfo info = CustomSoundLoader.getSoundCache(soundId).getInfo();
            SoundPackButton button = new SoundPackButton(startX + 5, startY + yOffset, info, b -> {
                this.selectSoundId = soundId;
                this.soundPage = 0;
                this.init();
            });
            if (StringUtils.isNotBlank(info.getDescription())) {
                MutableComponent description = ParseI18n.parse(info.getDescription());
                button.setTooltips(Collections.singletonList(description));
            }
            if (soundId.equals(selectSoundId)) {
                button.setSelect(true);
            }
            if (soundId.equals(this.maid.getSoundPackId())) {
                button.setUse(true);
            }
            this.addRenderableWidget(button);
            yOffset += 45;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
        graphics.fill(startX, startY, startX + 240, startY + 220, 0xFF2A2A2A);
        graphics.fill(startX + 242, startY, startX + 400, startY + 220, 0xFF2A2A2A);
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.custom_sound.pack.title"), startX + 120, startY + 6, 0xFFFFFF);
        graphics.drawCenteredString(font, Component.translatable("gui.touhou_little_maid.custom_sound.sounds.preview"), startX + 321, startY + 6, 0xFFFFFF);
        graphics.drawCenteredString(font, String.format("%d/%d", packPage + 1, packMaxPage + 1), startX + 120, startY + 206, 0xBBBBBB);
        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, pMouseX, pMouseY, pPartialTick);
        }
        if (StringUtils.isNotBlank(selectSoundId) && CustomSoundLoader.CACHE.containsKey(selectSoundId)) {
            graphics.drawCenteredString(font, String.format("%d/%d", soundPage + 1, soundMaxPage + 1), startX + 321, startY + 206, 0xBBBBBB);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ICON);
            graphics.blit(ICON, startX + 359, startY + 20, 0, 0, 16, 16, 256, 256);
            graphics.blit(ICON, startX + 380, startY + 20, 16, 0, 16, 16, 256, 256);
        }
        this.renderables.stream().filter(b -> b instanceof FlatColorButton).forEach(b -> ((FlatColorButton) b).renderToolTip(graphics, this, pMouseX, pMouseY));
    }
}
