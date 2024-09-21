package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.PatchouliWarningScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidSideTabButton;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SideTab;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ToggleSideTabMessage;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class MaidSideTabs<T extends AbstractMaidContainer> {
    private final int entityId;
    private final int rightPos;
    private final int topPos;
    private final int spacing = 25;

    public MaidSideTabs(int entityId, int rightPos, int topPos) {
        this.entityId = entityId;
        this.rightPos = rightPos;
        this.topPos = topPos;
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        // 任务配置界面按钮
        MaidSideTabButton taskConfig = genSideTabButton(screen, SideTab.TASK_CONFIG, (b) -> {
            EntityMaid maid = screen.getMaid();
            if (maid != null) {
                NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(screen.getMenu().containerId, entityId, SideTab.TASK_CONFIG.getIndex(), maid.getTask().getUid(), screen.isTaskListOpen(), screen.getTaskPage()));
            }
        });
        if (screen instanceof MaidTaskConfigGui<?>) {
            taskConfig.active = false;
        }

        // 跳转帕秋莉手册按钮
        MaidSideTabButton taskBook = genSideTabButton(screen, SideTab.TASK_BOOK, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.PATCHOULI)) {
                EntityMaid maid = screen.getMaid();
                if (maid != null) {
                    maid.getTask().openPatchouliBook();
                }
            } else {
                PatchouliWarningScreen.open();
            }
        });

        // 任务信息界面按钮
        MaidSideTabButton taskInfo = genSideTabButton(screen, SideTab.TASK_INFO, (b) -> {
        });
//        if (screen instanceof ??Gui) {
//            taskInfo.active = false;
//        }

        // 跳转全局配置按钮
        MaidSideTabButton globalConfig = genSideTabButton(screen, SideTab.GLOBAL_CONFIG, (b) -> {
            if (ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
                ConfigBuilder configBuilder = MenuIntegration.getConfigBuilder();
                configBuilder.setGlobalizedExpanded(true);
                Minecraft.getInstance().setScreen(configBuilder.build());
            } else {
                ClothConfigScreen.open();
            }
        });

        return new MaidSideTabButton[]{taskConfig, taskBook, taskInfo, globalConfig};
    }

    private MaidSideTabButton genSideTabButton(AbstractMaidContainerGui<T> screen, SideTab sideTab, Button.OnPress onPressIn) {
        String titleLangKey = String.format("gui.touhou_little_maid.button.%s", sideTab.name().toLowerCase());
        String descLangKey = String.format("gui.touhou_little_maid.button.%s.desc", sideTab.name().toLowerCase());

        return new MaidSideTabButton(sideTab, rightPos, topPos + sideTab.getIndex() * spacing, sideTab.getIndex() * spacing, onPressIn,
                List.of(Component.translatable(titleLangKey),
                        Component.translatable(descLangKey),
                        Component.translatable("gui.touhou_little_maid.button.warn_text")));
    }
}
