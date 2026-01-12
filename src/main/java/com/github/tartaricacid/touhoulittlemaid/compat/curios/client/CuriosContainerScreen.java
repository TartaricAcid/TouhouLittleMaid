package com.github.tartaricacid.touhoulittlemaid.compat.curios.client;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BaubleButton;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.MaidCurioSlot;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenMaidGuiMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class CuriosContainerScreen extends AbstractMaidContainerGui<CuriosContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation CURIOS_BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_curios.png");

    private static final int PREV = 0;
    private static final int NEXT = 1;

    private final EntityMaid maid;

    private int maxPages;
    private int maxSlots;
    private int page;
    private int slotCount;

    public CuriosContainerScreen(CuriosContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();

        this.maxSlots = CuriosApi.getCuriosInventory(this.maid).map(ICuriosItemHandler::getVisibleSlots).orElse(0);
        this.maxPages = (maxSlots - 1) / 36;
        this.page = Math.min(page, this.maxPages);
        this.slotCount = Math.min(maxSlots - this.page * 36, 36);
    }

    public void updatePage(int page) {
        this.maxSlots = CuriosApi.getCuriosInventory(this.maid).map(ICuriosItemHandler::getVisibleSlots).orElse(0);
        this.maxPages = (maxSlots - 1) / 36;
        this.page = Math.min(page, this.maxPages);
        this.slotCount = Math.min(maxSlots - this.page * 36, 36);
        this.getMenu().updatePage(page, getMinecraft().player);

        // 更新按钮信息
        this.init();
    }

    @Override
    protected void initAdditionWidgets() {
        BaubleButton button = this.getBaubleButton(maid, leftPos, topPos);
        this.addRenderableWidget(button);

        // 添加 curios 兼容按钮
        CuriosButton curiosButton = new CuriosButton(leftPos, topPos, true, btn -> {
            OpenMaidGuiMessage message = new OpenMaidGuiMessage(maid.getId(), TabIndex.MAIN);
            NetworkHandler.CHANNEL.sendToServer(message);
        });
        this.addRenderableWidget(curiosButton);

        // 如果有翻页，添加翻页按钮
        if (this.maxPages > 0) {
            ImageButton prevButton = new ImageButton(leftPos + 168, topPos + 147, 11, 12,
                    166, 0, 12, CURIOS_BG, b -> {
                MultiPlayerGameMode gameMode = this.getMinecraft().gameMode;
                if (gameMode != null && this.page > 0) {
                    gameMode.handleInventoryButtonClick(this.menu.containerId, PREV);
                }
            });
            ImageButton nextButton = new ImageButton(leftPos + 214, topPos + 147, 11, 12,
                    177, 0, 12, CURIOS_BG, b -> {
                MultiPlayerGameMode gameMode = this.getMinecraft().gameMode;
                if (gameMode != null && this.page < this.maxPages) {
                    gameMode.handleInventoryButtonClick(this.menu.containerId, NEXT);
                }
            });
            this.addRenderableWidget(prevButton);
            this.addRenderableWidget(nextButton);
        }
    }

    @Override
    protected void renderAddition(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 如果大于 36 个 Curios 槽位，渲染页数
        if (this.maxPages > 0) {
            MutableComponent page = Component.literal("%d/%d".formatted(this.page + 1, maxPages + 1));
            int width = font.width(page);
            graphics.drawString(font, page, (leftPos + 197) - width / 2, topPos + 150, 0x555555, false);
        } else {
            MutableComponent name = Component.translatable("curios.name");
            int width = font.width(name);
            graphics.drawString(font, name, (leftPos + 197) - width / 2, topPos + 150, 0x555555, false);
        }
    }

    @Override
    protected void renderAdditionTransTooltip(GuiGraphics graphics, int x, int y) {
        LocalPlayer clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer != null && clientPlayer.inventoryMenu.getCarried().isEmpty() && this.getSlotUnderMouse() != null) {
            Slot slot = this.getSlotUnderMouse();
            if (slot instanceof MaidCurioSlot slotCurio && !slot.hasItem()) {
                MutableComponent name = Component.literal(slotCurio.getSlotName());
                graphics.renderTooltip(this.font, name, x, y);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CURIOS_BG);

        // 护甲、主手、副手背景
        graphics.blit(CURIOS_BG, leftPos + 85, topPos + 36, 0, 0, 57, 58);

        // Curios 背景
        if (this.slotCount > 0) {
            int rows = (this.slotCount - 1) / 6;
            int width = (this.slotCount - 1) % 6 * 18 + 18;
            if (rows > 0) {
                // 绘制前 n-1 行完整行
                int height = rows * 18;
                graphics.blit(CURIOS_BG, leftPos + 142, topPos + 36, 57, 0, 108, height);
                // 绘制最后一行
                graphics.blit(CURIOS_BG, leftPos + 142, topPos + 36 + height, 57, height, width, 18);
            } else {
                graphics.blit(CURIOS_BG, leftPos + 142, topPos + 36, 57, 0, width, 18);
            }
        }
    }

    public int getPage() {
        return page;
    }
}
