package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidHataSelect;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidSkinGui;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.MaidHomeModeMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.MaidPickupModeMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SwitchMaidGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect.EffectRequest;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 女仆主 GUI 界面的集合，其他界面在此基础上拓展得到
 *
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public abstract class AbstractMaidGuiContainer extends GuiContainer {
    protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_main.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_side.png");
    private static ScheduledExecutorService timer;
    private static int taskPageIndex;
    protected MaidMainContainer container;
    EntityMaid maid;
    private int guiId;
    private int taskPageTotal;
    private GuiButtonToggle togglePickup;
    private GuiButtonToggle toggleHome;

    public AbstractMaidGuiContainer(MaidMainContainer inventorySlotsIn, int guiId) {
        super(inventorySlotsIn);
        this.guiId = guiId;
        this.container = inventorySlotsIn;
        this.maid = container.maid;
        this.taskPageTotal = (LittleMaidAPI.getTasks().size() - 1) / 6;
        syncEffectThread();
    }

    /**
     * 绘制自定义的背景，会在基础背景调用后，但在控件，前景图标，文本调用前渲染
     *
     * @param mouseX       鼠标 x 坐标
     * @param mouseY       鼠标 y 坐标
     * @param partialTicks tick 插值
     */
    public abstract void drawCustomBackground(int mouseX, int mouseY, float partialTicks);

    /**
     * 绘制自定义 GUI，会在主背景绘制后调用
     *
     * @param mouseX       鼠标 x 坐标
     * @param mouseY       鼠标 y 坐标
     * @param partialTicks tick 插值
     */
    public abstract void drawCustomScreen(int mouseX, int mouseY, float partialTicks);

    /**
     * 该 GUI 的名称
     */
    public abstract String getGuiName();

    @Override
    public void initGui() {
        // 清除按钮列表、标签列表，用来给后面重载按键用的
        this.buttonList.clear();
        this.labelList.clear();

        super.initGui();
        int i = this.guiLeft;
        int j = this.guiTop;

        // 切换是否拾起物品的按钮
        togglePickup = new GuiButtonToggle(BUTTON.PICKUP.ordinal(), i + 143, j + 63, 26, 16, maid.isPickup());
        togglePickup.initTextureValues(178, 0, 28, 18, BACKGROUND);
        this.buttonList.add(togglePickup);

        // 不同标签页切换按钮
        this.buttonList.add(new GuiButtonImage(BUTTON.MAIN.ordinal(), i + 3, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(BUTTON.INVENTORY.ordinal(), i + 31, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(BUTTON.BAUBLE.ordinal(), i + 59, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));

        // 切换是否开启 home 模式的按钮
        toggleHome = new GuiButtonToggle(BUTTON.HOME.ordinal(), i + 116, j + 63, 26, 16, maid.isHome());
        toggleHome.initTextureValues(178, 36, 28, 18, BACKGROUND);
        this.buttonList.add(toggleHome);

        // 切换模型的按钮
        // FIXME: 2019/11/13 其实这个逻辑实现还是有点问题的
        if (mc.player.isCreative() || !GeneralConfig.MAID_CONFIG.maidCannotChangeModel) {
            this.buttonList.add(new GuiButtonImage(BUTTON.SKIN.ordinal(), i + 65, j + 9, 9,
                    9, 178, 72, 10, BACKGROUND));
        }

        // 切换旗指物的按钮
        if (maid.hasSasimono()) {
            this.buttonList.add(new GuiButtonImage(BUTTON.HATA_SASIMONO.ordinal(), i + 26, j + 9, 9,
                    9, 188, 72, 10, BACKGROUND));
        }

        // 显示声音版权的页面
        this.buttonList.add(new GuiButtonImage(BUTTON.SOUND_CREDIT.ordinal(), i + 3, j + 166, 21,
                21, 233, 0, 24, BACKGROUND));

        // 模式翻页
        this.buttonList.add(new GuiButtonImage(BUTTON.TASK_LEFT_SWITCH.ordinal(), i - 70, j + 150, 7,
                11, 177, 0, 16, SIDE));
        this.buttonList.add(new GuiButtonImage(BUTTON.TASK_RIGHT_SWITCH.ordinal(), i - 17, j + 150, 7,
                11, 165, 0, 16, SIDE));


        // 模式
        for (int k = 0; k < 6; k++) {
            if (k + taskPageIndex * 6 >= LittleMaidAPI.getTasks().size()) {
                break;
            }
            if (LittleMaidAPI.getTasks().get(k + taskPageIndex * 6) == container.task) {
                this.buttonList.add(new GuiButtonImage(k + BUTTON.values().length, i - 70, j + 23 + 21 * k, 60,
                        20, 98, 0, 0, SIDE));
            } else {
                this.buttonList.add(new GuiButtonImage(k + BUTTON.values().length, i - 70, j + 23 + 21 * k, 60,
                        20, 98, 20, 20, SIDE));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == BUTTON.PICKUP.ordinal()) {
            if (maid.isPickup()) {
                togglePickup.setStateTriggered(false);
                CommonProxy.INSTANCE.sendToServer(new MaidPickupModeMessage(maid.getUniqueID(), false));
                return;
            } else {
                togglePickup.setStateTriggered(true);
                CommonProxy.INSTANCE.sendToServer(new MaidPickupModeMessage(maid.getUniqueID(), true));
                return;
            }
        }

        // 切换标签页
        if (button.id == BUTTON.MAIN.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(mc.player.getUniqueID(), maid.getEntityId(), BUTTON.MAIN.getGuiId(), container.taskIndex));
            return;
        }
        if (button.id == BUTTON.INVENTORY.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(mc.player.getUniqueID(), maid.getEntityId(), BUTTON.INVENTORY.getGuiId(), container.taskIndex));
            return;
        }
        if (button.id == BUTTON.BAUBLE.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(mc.player.getUniqueID(), maid.getEntityId(), BUTTON.BAUBLE.getGuiId(), container.taskIndex));
            return;
        }

        if (button.id == BUTTON.HOME.ordinal()) {
            if (maid.isHome()) {
                toggleHome.setStateTriggered(false);
                CommonProxy.INSTANCE.sendToServer(new MaidHomeModeMessage(maid.getUniqueID(), false));
                return;
            } else {
                toggleHome.setStateTriggered(true);
                CommonProxy.INSTANCE.sendToServer(new MaidHomeModeMessage(maid.getUniqueID(), true));
                return;
            }
        }

        if (button.id == BUTTON.SKIN.ordinal()) {
            // 避免多线程的 Bug
            mc.addScheduledTask(() -> mc.displayGuiScreen(new MaidSkinGui(maid)));
            return;
        }

        if (button.id == BUTTON.HATA_SASIMONO.ordinal()) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new MaidHataSelect(maid)));
            return;
        }

        if (button.id == BUTTON.SOUND_CREDIT.ordinal()) {
            mc.player.playSound(MaidSoundEvent.OTHER_CREDIT, 1, 1);
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiSoundCredit(this)));
            return;
        }

        if (button.id == BUTTON.TASK_LEFT_SWITCH.ordinal()) {
            if (taskPageIndex > 0) {
                taskPageIndex--;
                this.initGui();
            }
            return;
        }

        if (button.id == BUTTON.TASK_RIGHT_SWITCH.ordinal()) {
            if (taskPageIndex < taskPageTotal) {
                taskPageIndex++;
                this.initGui();
            }
            return;
        }

        if ((button.id >= BUTTON.values().length) && (button.id < BUTTON.values().length + 6)) {
            int listIndex = button.id - BUTTON.values().length + taskPageIndex * 6;
            if (listIndex < LittleMaidAPI.getTasks().size()) {
                container.taskIndex = listIndex;
                container.task = LittleMaidAPI.getTasks().get(listIndex);
                this.initGui();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 绘制自定义前景
        this.drawCustomScreen(mouseX, mouseY, partialTicks);

        int i = this.guiLeft;
        int j = this.guiTop;
        boolean xInRange;
        boolean yInRange;

        // 绘制侧边栏信息
        String currentModeName = TextFormatting.DARK_GRAY + I18n.format(container.task.getTranslationKey());
        fontRenderer.drawString(currentModeName, (float) (i - 39 - fontRenderer.getStringWidth(currentModeName) / 2), j + 9, 0xffffff, false);
        String pageText = String.format("%s%d/%d", TextFormatting.DARK_GRAY, taskPageIndex + 1, this.taskPageTotal + 1);
        fontRenderer.drawString(pageText, (float) (i - 39 - fontRenderer.getStringWidth(pageText) / 2), j + 151, 0xffffff, false);

        // 绘制侧边栏模式列表图标、名称
        for (int k = 0; k < 6; k++) {
            if (k + taskPageIndex * 6 >= LittleMaidAPI.getTasks().size()) {
                break;
            }
            IMaidTask task = LittleMaidAPI.getTasks().get(k + taskPageIndex * 6);
            drawItemStack(task.getIcon(), i - 68, j + 25 + 21 * k);
            String name = I18n.format(task.getTranslationKey());
            fontRenderer.drawString(name, i - 32 - fontRenderer.getStringWidth(name) / 2, j + 29 + 21 * k, 0xdddddd, false);
        }

        // 绘制女仆的药水效果
        int spacing = 0;
        Collection<PotionEffect> effects = maid.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getDuration() <= 0) {
                continue;
            }

            Potion potion = effect.getPotion();
            int startX = i + 178;
            int startY = j + 5 + spacing;
            spacing += 12;

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(startX, startY - 1.5, 0);
            GlStateManager.scale(0.6, 0.6, 0.6);
            this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
            if (potion.hasStatusIcon()) {
                int index = potion.getStatusIconIndex();
                this.drawTexturedModalRect(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
            }
            GlStateManager.popMatrix();
            potion.renderInventoryEffect(i, j, effect, mc);
            if (!potion.shouldRenderInvText(effect)) {
                continue;
            }
            if (effect.getDuration() > 0) {
                fontRenderer.drawString(String.format("%s %s %s %s%s",
                        effect.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.RESET,
                        I18n.format(effect.getEffectName()),
                        I18n.format("enchantment.level." + (effect.getAmplifier() + 1)),
                        TextFormatting.GRAY,
                        Potion.getPotionDurationString(effect, 1.0f)
                ), startX + 9, startY, 0xffffffff, true);
            }
        }

        // 绘制不同标签页的提示文字
        for (MaidGuiHandler.MAIN_GUI gui : MaidGuiHandler.MAIN_GUI.values()) {
            xInRange = (i + 28 * (gui.getId() - 1)) < mouseX && mouseX < (i + 28 * gui.getId());
            yInRange = (j - 28) < mouseY && mouseY < j;
            if (xInRange && yInRange) {
                this.drawHoveringText(I18n.format("gui.touhou_little_maid.tab." + gui.name().toLowerCase(Locale.US)), mouseX, mouseY);
            }
        }

        // Home 模式的描述
        xInRange = (i + 143) < mouseX && mouseX < (i + 169);
        yInRange = (j + 63) < mouseY && mouseY < (j + 79);
        if (xInRange && yInRange) {
            this.drawHoveringText(Arrays.asList(
                    I18n.format("gui.touhou_little_maid.button.pickup." + maid.isPickup()),
                    I18n.format("gui.touhou_little_maid.button.pickup.desc")
            ), mouseX, mouseY);
        }

        // 拾物模式描述
        xInRange = (i + 116) < mouseX && mouseX < (i + 142);
        yInRange = (j + 63) < mouseY && mouseY < (j + 79);
        if (xInRange && yInRange) {
            this.drawHoveringText(Arrays.asList(
                    I18n.format("gui.touhou_little_maid.button.home." + maid.isHome()),
                    I18n.format("gui.touhou_little_maid.button.home.desc")
            ), mouseX, mouseY);
        }

        // 切换皮肤描述
        xInRange = (i + 65) < mouseX && mouseX < (i + 74);
        yInRange = (j + 9) < mouseY && mouseY < (j + 18);
        // FIXME: 2019/11/13 其实这个逻辑实现还是有点问题的
        boolean canChangeMaidModel = mc.player.isCreative() || !GeneralConfig.MAID_CONFIG.maidCannotChangeModel;
        if (xInRange && yInRange && canChangeMaidModel) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.skin"), mouseX, mouseY);
        }

        // 切换皮肤描述
        xInRange = (i + 26) < mouseX && mouseX < (i + 35);
        yInRange = (j + 9) < mouseY && mouseY < (j + 18);
        if (xInRange && yInRange && maid.hasSasimono()) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.hata_sasimono"), mouseX, mouseY);
        }

        // 绘制物品的文本提示
        this.renderHoveredToolTip(mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int i = this.guiLeft;
        int j = this.guiTop;

        this.drawDefaultBackground();

        // 绘制选择图标背景
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j - 28, 0, 193, 112, 32);

        // 绘制侧边栏
        mc.getTextureManager().bindTexture(SIDE);
        this.drawTexturedModalRect(i - 76, j, 0, 0, 89, this.ySize);

        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        // 绘制自定义背景
        drawCustomBackground(mouseX, mouseY, partialTicks);

        // 绘制选择图标前景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i + 28 * (guiId - 1), j - 28, 28 * (guiId - 1), 224, 28, 32);

        // 绘制模式图标
        this.drawItemStack(Items.WRITABLE_BOOK.getDefaultInstance(), i + 6, j - 19);
        this.drawItemStack(Item.getItemFromBlock(Blocks.CHEST).getDefaultInstance(), i + 34, j - 19);
        this.drawItemStack(new ItemStack(Items.DYE, 1, 4), i + 62, j - 19);
        this.drawItemStack(Items.DIAMOND_SWORD.getDefaultInstance(), i + 90, j - 19);

        // 绘制女仆
        GuiInventory.drawEntityOnScreen(i + 51, j + 70, 28,
                (float) (i + 51) - mouseX, (float) (j + 70 - 45) - mouseY, maid);
    }

    /**
     * 通过 ItemStack 绘制对应图标
     */
    private void drawItemStack(ItemStack stack, int x, int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int getXSize() {
        if (maid.getActivePotionEffects().size() <= 0) {
            return super.getXSize();
        } else {
            int i = 0;
            for (PotionEffect effect : maid.getActivePotionEffects()) {
                i += effect.getDuration();
            }
            if (i == 0) {
                return super.getXSize();
            }
            return super.getXSize() + 128;
        }
    }

    private void syncEffectThread() {
        timer = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder()
                .namingPattern("sync-maid-effect-schedule").daemon(true).build());
        timer.scheduleAtFixedRate(() -> CommonProxy.INSTANCE.sendToServer(new EffectRequest(maid.getUniqueID())),
                0, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onGuiClosed() {
        // 关闭发包线程
        timer.shutdown();
        super.onGuiClosed();
    }

    /**
     * 该界面的按钮枚举
     */
    public enum BUTTON {
        // 拾物模式按钮
        PICKUP(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 主界面按钮
        MAIN(MaidGuiHandler.MAIN_GUI.MAIN.getId()),
        // 主物品栏按钮
        INVENTORY(MaidGuiHandler.MAIN_GUI.INVENTORY.getId()),
        // 饰品栏按钮
        BAUBLE(MaidGuiHandler.MAIN_GUI.BAUBLE.getId()),
        // HOME 模式切换按钮
        HOME(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 女仆模型皮肤按钮
        SKIN(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 声音素材致谢
        SOUND_CREDIT(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 旗指物按钮
        HATA_SASIMONO(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 模式左翻页
        TASK_LEFT_SWITCH(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 模式右翻页
        TASK_RIGHT_SWITCH(MaidGuiHandler.OTHER_GUI.NONE.getId());

        private int guiId;

        /**
         * @param guiId 摁下按钮后触发的 GUI 的 ID，如果不触发 GUI，可以将其设置为 MaidGuiHandler.NONE_GUI.NONE
         */
        BUTTON(int guiId) {
            this.guiId = guiId;
        }

        public int getGuiId() {
            return guiId;
        }
    }

    /**
     * 一个简单的打开连接的提示界面
     */
    private class GuiSoundCredit extends GuiConfirmOpenLink {
        GuiSoundCredit(GuiYesNoCallback parentScreenIn) {
            super(parentScreenIn, "https://www14.big.or.jp/~amiami/happy/index.html", BUTTON.SOUND_CREDIT.ordinal(), true);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 0) {
                try {
                    super.openWebLink(new URI("https://www14.big.or.jp/~amiami/happy/index.html"));
                } catch (URISyntaxException urisyntaxexception) {
                    TouhouLittleMaid.LOGGER.error("Can't open url for {}", urisyntaxexception);
                }
                return;
            }
            if (button.id == 1) {
                mc.addScheduledTask(() -> mc.displayGuiScreen(null));
                return;
            }
            if (button.id == 2) {
                this.copyLinkToClipboard();
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.disableSecurityWarning();
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.drawCenteredString(this.fontRenderer, TextFormatting.GOLD.toString() + TextFormatting.BOLD.toString() +
                    I18n.format("gui.touhou_little_maid.credit.url.close"), this.width / 2, 110, 0xffcccc);
        }
    }
}
