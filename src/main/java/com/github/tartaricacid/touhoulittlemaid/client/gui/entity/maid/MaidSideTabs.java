package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.OpenPatchouliBookEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.PatchouliWarningScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidSideTabButton;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.ClothConfigCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SideTab;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ToggleSideTabMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Locale;

public class MaidSideTabs<T extends AbstractMaidContainer> {
    private static final int SPACING = 25;

    private final int entityId;
    private final int rightPos;
    private final int topPos;

    public MaidSideTabs(int entityId, int rightPos, int topPos) {
        this.entityId = entityId;
        this.rightPos = rightPos;
        this.topPos = topPos;
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        // 任务配置界面按钮
        MaidSideTabButton taskConfig = genSideTabButton(SideTab.TASK_CONFIG, b -> {
            EntityMaid maid = screen.getMaid();
            if (maid != null) {
                NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(entityId, SideTab.TASK_CONFIG.getIndex(), maid.getTask().getUid()));
            }
        });
        if (screen instanceof MaidTaskConfigGui<?>) {
            taskConfig.active = false;
        }

        // 跳转帕秋莉手册按钮
        MaidSideTabButton taskBook = genSideTabButton(SideTab.TASK_BOOK, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.PATCHOULI)) {
                EntityMaid maid = screen.getMaid();
                if (maid != null) {
                    MinecraftForge.EVENT_BUS.post(new OpenPatchouliBookEvent(maid, maid.getTask()));
                }
            } else {
                PatchouliWarningScreen.open();
            }
        });

        // TODO: 未完成信息界面内容
        // 任务信息界面按钮
        MaidSideTabButton taskInfo = genSideTabButton(SideTab.TASK_INFO, (b) -> {
        });

        // 跳转全局配置按钮
        MaidSideTabButton globalConfig = genSideTabButton(SideTab.GLOBAL_CONFIG, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
                ClothConfigCompat.openConfigScreen();
            } else {
                ClothConfigScreen.open();
            }
        });

        return new MaidSideTabButton[]{taskConfig, taskBook, taskInfo, globalConfig};
    }

    private MaidSideTabButton genSideTabButton(SideTab sideTab, Button.OnPress onPressIn) {
        String name = sideTab.name().toLowerCase(Locale.ENGLISH);
        String titleLangKey = String.format("gui.touhou_little_maid.button.%s", name);
        String descLangKey = String.format("gui.touhou_little_maid.button.%s.desc", name);

        return new MaidSideTabButton(rightPos, topPos + sideTab.getIndex() * SPACING, sideTab.getIndex() * SPACING, onPressIn,
                List.of(Component.translatable(titleLangKey), Component.translatable(descLangKey)));
    }
}
