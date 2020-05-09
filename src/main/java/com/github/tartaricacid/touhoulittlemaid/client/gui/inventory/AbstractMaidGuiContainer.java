package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.event.KappaCompassRenderEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.download.ResourcesDownloadGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidHataSelect;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.MaidSkinGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.sound.GuiMaidSound;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ClearMaidPosMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.MaidHomeModeMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.MaidPickupModeMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SwitchMaidGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect.EffectRequest;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.ScaledResolution;
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
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    protected static final ResourceLocation BACKGROUND = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_main.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_side.png");
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_icon.png");
    private static ItemStack BARRIER = new ItemStack(Blocks.BARRIER);
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
     * 绘制自定义的文本提示
     *
     * @param mouseX       鼠标 x 坐标
     * @param mouseY       鼠标 y 坐标
     * @param partialTicks tick 插值
     */
    public abstract void drawCustomTooltips(int mouseX, int mouseY, float partialTicks);

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
        this.buttonList.add(new GuiButtonImage(BUTTON.MAIN.ordinal(), i + 6, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(BUTTON.INVENTORY.ordinal(), i + 34, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(BUTTON.BAUBLE.ordinal(), i + 62, j - 25, 22,
                22, 234, 234, 0, BACKGROUND));

        // 切换是否开启 home 模式的按钮
        toggleHome = new GuiButtonToggle(BUTTON.HOME.ordinal(), i + 116, j + 63, 26, 16, maid.isHomeModeEnable());
        toggleHome.initTextureValues(178, 36, 28, 18, BACKGROUND);
        this.buttonList.add(toggleHome);

        // 切换模型的按钮
        this.buttonList.add(new GuiButtonImage(BUTTON.SKIN.ordinal(), i + 65, j + 9, 9,
                9, 178, 72, 10, BACKGROUND));

        // 切换旗指物的按钮
        if (maid.hasSasimono()) {
            this.buttonList.add(new GuiButtonImage(BUTTON.HATA_SASIMONO.ordinal(), i + 26, j + 9, 9,
                    9, 188, 72, 10, BACKGROUND));
        }

        // 模式翻页
        GuiButtonImage leftSwitch = new GuiButtonImage(BUTTON.TASK_LEFT_SWITCH.ordinal(), i - 70, j + 150, 7,
                11, 177, 0, 16, SIDE);
        GuiButtonImage rightSwitch = new GuiButtonImage(BUTTON.TASK_RIGHT_SWITCH.ordinal(), i - 17, j + 150, 7,
                11, 165, 0, 16, SIDE);
        leftSwitch.visible = taskPageIndex > 0;
        rightSwitch.visible = taskPageIndex < taskPageTotal;
        this.buttonList.add(leftSwitch);
        this.buttonList.add(rightSwitch);

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

        // 显示声音版权的页面
        this.buttonList.add(new GuiButton(BUTTON.SOUND_CREDIT.ordinal(), i + 5, j + 167, 20, 20, ""));
        // 下载资源包按钮
        this.buttonList.add(new GuiButton(BUTTON.DOWNLOAD_RESOURCES.ordinal(), i + 26, j + 167, 20, 20, ""));
        // 显示坐标按钮
        this.buttonList.add(new GuiButton(BUTTON.SHOW_POS.ordinal(), i + 47, j + 167, 20, 20, ""));
        // 清除界面按钮
        this.buttonList.add(new GuiButton(BUTTON.CLEAR_POS.ordinal(), i + 68, j + 167, 20, 20, ""));

        // 占位按钮
        for (int k = 2; k < 6; k++) {
            this.buttonList.add(new GuiButton(405 + k, i + 47 + 21 * k, j + 167, 20, 20, ""));
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
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(maid.getEntityId(), BUTTON.MAIN.getGuiId(), container.taskIndex));
            return;
        }
        if (button.id == BUTTON.INVENTORY.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(maid.getEntityId(), BUTTON.INVENTORY.getGuiId(), container.taskIndex));
            return;
        }
        if (button.id == BUTTON.BAUBLE.ordinal()) {
            CommonProxy.INSTANCE.sendToServer(new SwitchMaidGuiMessage(maid.getEntityId(), BUTTON.BAUBLE.getGuiId(), container.taskIndex));
            return;
        }

        if (button.id == BUTTON.HOME.ordinal()) {
            if (maid.isHomeModeEnable()) {
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
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiMaidSound()));
            return;
        }

        if (button.id == BUTTON.DOWNLOAD_RESOURCES.ordinal()) {
            List<DownloadInfo> downloadInfoList;
            int page = ResourcesDownloadGui.getCurrentPage();
            if (page == 0) {
                downloadInfoList = InfoGetManager.DOWNLOAD_INFO_LIST_ALL;
            } else {
                DownloadInfo.TypeEnum typeEnum = DownloadInfo.TypeEnum.getTypeByIndex(page - 1);
                downloadInfoList = InfoGetManager.getTypedDownloadInfoList(typeEnum);
            }
            mc.addScheduledTask(() -> mc.displayGuiScreen(new ResourcesDownloadGui(downloadInfoList)));
            return;
        }

        if (button.id == BUTTON.SHOW_POS.ordinal()) {
            KappaCompassRenderEvent.setTmpDisplay(maid.getCompassPosList(maid.getCompassMode()), maid.getCompassMode());
            return;
        }

        if (button.id == BUTTON.CLEAR_POS.ordinal()) {
            KappaCompassRenderEvent.setTmpDisplay(Lists.newArrayList(), ItemKappaCompass.Mode.NONE);
            CommonProxy.INSTANCE.sendToServer(new ClearMaidPosMessage(maid.getUniqueID()));
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
            fontRenderer.drawString(name, i - 32 - fontRenderer.getStringWidth(name) / 2.0f, j + 29 + 21 * k, 0xdddddd, false);
        }

        // 绘制下方按钮图标
        mc.renderEngine.bindTexture(ICON);
        drawModalRectWithCustomSizedTexture(i + 8, j + 170, 0, 0, 14, 14, 256, 256);
        drawModalRectWithCustomSizedTexture(i + 29, j + 170, 14, 0, 14, 14, 256, 256);
        drawModalRectWithCustomSizedTexture(i + 50, j + 170, 28, 0, 14, 14, 256, 256);
        drawModalRectWithCustomSizedTexture(i + 71, j + 170, 42, 0, 14, 14, 256, 256);

        // 占位按钮图标
        for (int k = 2; k < 6; k++) {
            drawItemStack(BARRIER, i + 49 + 21 * k, j + 169);
        }

        // 绘制女仆的药水效果
        int spacing = 0;
        Collection<PotionEffect> effects = maid.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getDuration() <= 0) {
                continue;
            }

            Potion potion = effect.getPotion();
            int startX = i + 178 + getRenderPotionStartXOffset();
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
            xInRange = (i + 3 + 28 * (gui.getId() - 1)) < mouseX && mouseX < (i + 3 + 28 * gui.getId());
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
                    I18n.format("gui.touhou_little_maid.button.home." + maid.isHomeModeEnable()),
                    I18n.format("gui.touhou_little_maid.button.home.desc")
            ), mouseX, mouseY);
        }

        // 切换皮肤描述
        xInRange = (i + 65) < mouseX && mouseX < (i + 74);
        yInRange = (j + 9) < mouseY && mouseY < (j + 18);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.skin"), mouseX, mouseY);
        }

        // 切换皮肤描述
        xInRange = (i + 26) < mouseX && mouseX < (i + 35);
        yInRange = (j + 9) < mouseY && mouseY < (j + 18);
        if (xInRange && yInRange && maid.hasSasimono()) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.hata_sasimono"), mouseX, mouseY);
        }

        // 声音素材感谢描述
        xInRange = (i + 3) < mouseX && mouseX < (i + 23);
        yInRange = (j + 167) < mouseY && mouseY < (j + 187);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.sound_credit"), mouseX, mouseY);
        }

        // 自定义资源包下载
        xInRange = (i + 24) < mouseX && mouseX < (i + 44);
        yInRange = (j + 167) < mouseY && mouseY < (j + 187);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.pack_download"), mouseX, mouseY);
        }

        // 显示坐标
        xInRange = (i + 45) < mouseX && mouseX < (i + 65);
        yInRange = (j + 167) < mouseY && mouseY < (j + 187);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.show_pos"), mouseX, mouseY);
        }

        // 清除坐标
        xInRange = (i + 67) < mouseX && mouseX < (i + 86);
        yInRange = (j + 167) < mouseY && mouseY < (j + 187);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.button.clear_pos"), mouseX, mouseY);
        }

        drawCustomTooltips(mouseX, mouseY, partialTicks);

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
        this.drawTexturedModalRect(i + 3, j - 28, 0, 193, 168, 32);

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
        this.drawTexturedModalRect(i + 3 + 28 * (guiId - 1), j - 28, 28 * (guiId - 1), 224, 28, 32);

        // 绘制模式图标
        this.drawItemStack(Items.WRITABLE_BOOK.getDefaultInstance(), i + 9, j - 19);
        this.drawItemStack(Item.getItemFromBlock(Blocks.CHEST).getDefaultInstance(), i + 37, j - 19);
        this.drawItemStack(new ItemStack(Items.DYE, 1, 4), i + 65, j - 19);
        this.drawItemStack(BARRIER, i + 93, j - 19);
        this.drawItemStack(BARRIER, i + 121, j - 19);
        this.drawItemStack(BARRIER, i + 149, j - 19);

        // 绘制女仆
        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        GL11.glScissor((int) ((i + 26) * scaleW), (int) ((j + 87.5) * scaleH), (int) (49 * scaleW), (int) (70 * scaleH));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiInventory.drawEntityOnScreen(i + 51, j + 70, 28,
                (float) (i + 51) - mouseX, (float) (j + 70 - 45) - mouseY, maid);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
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

    /**
     * 渲染药水时距离左侧边距的距离
     *
     * @return 距离，单位为像素
     */
    abstract public int getRenderPotionStartXOffset();

    @Override
    public int getXSize() {
        if (maid.getActivePotionEffects().size() <= 0) {
            return super.getXSize() + getRenderPotionStartXOffset();
        } else {
            int i = 0;
            for (PotionEffect effect : maid.getActivePotionEffects()) {
                i += effect.getDuration();
            }
            if (i == 0) {
                return super.getXSize() + getRenderPotionStartXOffset();
            }
            return super.getXSize() + 128 + getRenderPotionStartXOffset();
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
        timer.shutdownNow();
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
        TASK_RIGHT_SWITCH(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 下载资源包按钮
        DOWNLOAD_RESOURCES(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 显示坐标按钮
        SHOW_POS(MaidGuiHandler.OTHER_GUI.NONE.getId()),
        // 清除坐标按钮
        CLEAR_POS(MaidGuiHandler.OTHER_GUI.NONE.getId());

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
}
