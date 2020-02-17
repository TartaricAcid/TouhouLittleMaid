package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.IModelItem;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/12 12:27
 **/
@SideOnly(Side.CLIENT)
public abstract class AbstractSkinGui<T extends EntityLivingBase, U extends IModelItem> extends GuiScreen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select_side.png");
    /**
     * 按钮的 ID -> 模型 映射
     */
    private static final HashMap<Integer, IModelItem> BUTTON_MODEL_MAP = Maps.newHashMap();
    private SkinGuiNumber<U> guiNumber;
    /**
     * 用来在子类中通过实体 id 获取缓存中的实体对象
     */
    protected static String ENTITY_ID;
    /**
     * 使用的模型包列表
     */
    private List<CustomModelPack<U>> modelPackList;
    protected T entity;

    public AbstractSkinGui(T entity, List<CustomModelPack<U>> listPack, String entityId) {
        this.entity = entity;
        modelPackList = listPack;
        guiNumber = new SkinGuiNumber<>(modelPackList);
        setPageIndex(MathHelper.clamp(getPageIndex(), 0, guiNumber.getPageSize() - 1));
        setPackIndex(MathHelper.clamp(getPackIndex(), 0, guiNumber.getPackSize() - 1));
        setRowIndex(MathHelper.clamp(getRowIndex(), 0, guiNumber.getRowSize(getPackIndex())));
        ENTITY_ID = entityId;
    }

    /**
     * 绘制左侧示例实体
     *
     * @param middleX 屏幕参考中点
     * @param middleY 屏幕参考中点
     * @param mouseX  鼠标 x 坐标
     * @param mouseY  鼠标 Y 坐标
     */
    abstract void drawLeftEntity(int middleX, int middleY, float mouseX, float mouseY);

    /**
     * 绘制右侧示例实体
     *
     * @param posX      实体所在的 x 坐标
     * @param posY      实体所在的 y 坐标
     * @param modelItem 该实体应该对应的模型数据
     */
    abstract void drawRightEntity(int posX, int posY, U modelItem);

    /**
     * 打开详情界面
     *
     * @param entity  实体
     * @param modelId 该实体应该对应的模型数据
     */
    abstract void openDetailsGui(T entity, ResourceLocation modelId);

    /**
     * 发包通知模型更改
     *
     * @param entity  实体
     * @param modelId 该实体应该对应的模型数据
     */
    abstract void notifyModelChange(T entity, ResourceLocation modelId);

    abstract int getPackIndex();

    abstract void setPackIndex(int packIndex);

    abstract int getRowIndex();

    abstract void setRowIndex(int rowIndex);

    abstract int getPageIndex();

    abstract void setPageIndex(int packIndex);

    @Override
    public void initGui() {
        // 清除按钮列表、标签列表，用来给后面重载按键用的
        this.buttonList.clear();
        this.labelList.clear();

        int i = this.width / 2 + 50;
        int j = this.height / 2 + 5;
        int id = 0;

        // 模型包的分栏按钮
        for (int index = 0; index < 7; index++) {
            if (index == guiNumber.getTabIndex(getPackIndex())) {
                this.buttonList.add(new GuiButtonImage(id++, i - 98 + 28 * index, j - 108, 28, 31, 116, 224, 0, BG));
            } else if (index < guiNumber.getTabSize(getPackIndex())) {
                this.buttonList.add(new GuiButtonImage(id++, i - 98 + 28 * index, j - 105, 28, 25, 116, 194, 0, BG));
            } else {
                GuiButtonImage buttonImage = new GuiButtonImage(id++, i - 98 + 28 * index, j - 105, 28, 25, 116, 194, 0, BG);
                buttonImage.visible = false;
                this.buttonList.add(buttonImage);
            }
        }
        // 关闭当前界面的按键
        this.buttonList.add(new GuiButtonImage(id++, i + 122, j - 97, 21, 17, 58, 201, 18, BG));

        // 添加切换页面的按钮
        id = 8;
        GuiButton prePage = new GuiButton(id++, i - 119, j - 101, 20, 20, "<");
        GuiButton nextPage = new GuiButton(id++, i + 99, j - 101, 20, 20, ">");
        if (getPageIndex() == 0) {
            prePage.enabled = false;
        }
        if (getPageIndex() == guiNumber.getPageSize() - 1) {
            nextPage.enabled = false;
        }
        this.buttonList.add(prePage);
        this.buttonList.add(nextPage);

        // 添加按键，顺便装填按键对应模型的索引
        CustomModelPack<U> pack = modelPackList.get(getPackIndex());

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 44 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始添加按键，顺便装填按键对应模型的索引
        for (U modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            this.buttonList.add(new GuiButtonImage(id, i + x - 8, j + y - 26, 15, 24, 41, 201, 24, BG));
            BUTTON_MODEL_MAP.put(id, modelItem);

            // 往右绘制
            x = x + 20;
            id++;

            // 如果超出一定限制，换行
            if (x > 105) {
                x = -100;
                y = y + 30;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 中心点
        int middleX = this.width / 2 + 50;
        int middleY = this.height / 2 + 5;

        // 绘制灰色默认背景
        drawDefaultBackground();

        // 绘制 GUI 背景
        mc.renderEngine.bindTexture(BG);
        drawTexturedModalRect(middleX - 256 / 2, middleY - 80, 0, 0, 256, 180);
        mc.renderEngine.bindTexture(SIDE);
        drawTexturedModalRect(middleX - 256 / 2 + 250, middleY - 80, 0, 0, 24, 180);

        // 绘制侧边的滚动条
        drawScrollSide(middleX, middleY);

        // 调用父类方法绘制按钮列表、标签列表
        drawButton(mouseX, mouseY, partialTicks);

        // 绘制标签栏图标
        drawTabIcon(middleX, middleY);

        // 绘制左边示例实体
        drawLeftEntity(middleX, middleY, mouseX, mouseY);

        // 绘制实体，绘制完毕后绘制文本提示
        drawEntity(middleX, middleY);
        drawTooltips(mouseX, mouseY, middleX, middleY);
    }

    private void drawButton(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }

    private void drawScrollSide(int middleX, int middleY) {
        if (guiNumber.canScroll(getPackIndex(), getRowIndex())) {
            drawTexturedModalRect(middleX - 256 / 2 + 254,
                    middleY - 72 + (int) (149 * guiNumber.getCurrentScroll(getPackIndex(), getRowIndex())),
                    24, 0, 12, 15);
        } else {
            drawTexturedModalRect(middleX - 256 / 2 + 254,
                    middleY - 72 + (int) (149 * guiNumber.getCurrentScroll(getPackIndex(), getRowIndex())),
                    36, 0, 12, 15);
        }
    }

    private void drawTabIcon(int middleX, int middleY) {
        // 模型包的分栏按钮图标
        int size = guiNumber.getTabSize(getPackIndex());
        for (int index = 0; index < size; index++) {
            CustomModelPack<U> pack = modelPackList.get(guiNumber.tabToPackIndex(index, getPageIndex()));
            ResourceLocation icon = pack.getIcon();
            if (icon != null) {
                if (pack.getIconAnimation() == CustomModelPack.AnimationState.UNCHECK) {
                    checkIconAnimation(pack, icon);
                }
                if (pack.getIconAnimation() == CustomModelPack.AnimationState.FALSE) {
                    mc.renderEngine.bindTexture(icon);
                    drawModalRectWithCustomSizedTexture(middleX - 92 + 28 * index, middleY - 98,
                            0, 0, 16, 16, 16, 16);
                } else {
                    mc.renderEngine.bindTexture(icon);
                    int time = (int) mc.world.getWorldTime() / pack.getIconDelay();
                    int iconIndex = time % pack.getIconAspectRatio();
                    drawModalRectWithCustomSizedTexture(middleX - 92 + 28 * index, middleY - 98,
                            0, iconIndex * 16, 16,
                            16, 16, 16 * pack.getIconAspectRatio());
                }
            }
        }
    }

    private void checkIconAnimation(CustomModelPack<U> pack, ResourceLocation icon) {
        InputStream stream = null;
        try {
            stream = mc.getResourceManager().getResource(icon).getInputStream();
            BufferedImage img = ImageIO.read(stream);
            if (img.getWidth() == img.getHeight()) {
                pack.setIconAnimation(CustomModelPack.AnimationState.FALSE);
            } else {
                pack.setIconAnimation(CustomModelPack.AnimationState.TRUE);
                pack.setIconAspectRatio(img.getHeight() / img.getWidth());
            }
        } catch (IOException ignore) {
            pack.setIconAnimation(CustomModelPack.AnimationState.FALSE);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    /**
     * 绘制所有的模型实体图案
     */
    private void drawEntity(int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPack<U> pack = modelPackList.get(getPackIndex());

        // 绘制包信息
        drawPackInfoText(pack, middleX, middleY);

        // 起始坐标
        int x = -100;
        int y = -38;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (U modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            drawRightEntity(middleX + x, middleY + y, modelItem);
            // 往右绘制
            x = x + 20;
            // 如果超出一定限制，换行
            if (x > 105) {
                x = -100;
                y = y + 30;
            }
        }
    }

    /**
     * 绘制包的文本信息
     */
    private void drawPackInfoText(CustomModelPack<U> pack, int middleX, int middleY) {
        int offSet = -80;

        // 绘制包名
        drawCenteredString(fontRenderer, ParseI18n.parse(pack.getPackName()), middleX - 193, middleY + offSet, 0xffffff);

        // 如果描述不为空，逐行绘制描述
        for (String str : ParseI18n.parse(pack.getDescription())) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.GRAY + str, middleX - 193, middleY + offSet, 0xffffff);
        }

        // 绘制作者列表
        if (!pack.getAuthor().isEmpty()) {
            for (List<String> textList : Lists.partition(pack.getAuthor(), 2)) {
                offSet += 10;
                drawCenteredString(fontRenderer, TextFormatting.GOLD + textList.toString(),
                        middleX - 193, middleY + offSet, 0xffffff);
            }
        }

        // 绘制版本信息
        if (pack.getVersion() != null) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.DARK_AQUA + I18n.format("gui.touhou_little_maid.skin.text.version", pack.getVersion()),
                    middleX - 193, middleY + offSet, 0xffffff);
        }


        // 绘制日期信息
        if (pack.getDate() != null) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.GREEN + I18n.format("gui.touhou_little_maid.skin.text.date", pack.getDate()),
                    middleX - 193, middleY + offSet, 0xffffff);
        }


        // 绘制最后的翻页数
        drawCenteredString(fontRenderer, String.format("%s/%s", getPageIndex() + 1, guiNumber.getPageSize()), middleX, middleY - 120, 0xffffff);
    }

    /**
     * 绘制模型对应的文本提示<br>
     * 用遍历方式绘制文本提示，因为绝大多数情况下是空循环体（可能就涉及几个简单的 int 运算）<br>
     * 应该不会存在性能问题<br>
     */
    private void drawTooltips(int mouseX, int mouseY, int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPack<U> pack = modelPackList.get(getPackIndex());

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 44 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (U modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            // 判定鼠标所在的位置
            boolean isxInRange = middleX + x - 8 < mouseX && mouseX < middleX + x + 7;
            boolean isyInRange = middleY + y - 23 < mouseY && mouseY < middleY + y + 1;

            // 在位置内，就绘制文本提示
            if (isxInRange && isyInRange) {
                // 绘制的文本
                List<String> str = new ArrayList<>();
                // 塞入模型名称
                str.add(modelItem.getName());
                // 塞入描述
                str.addAll(modelItem.getDescription());
                // 塞入提示语
                str.add(TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC.toString() +
                        I18n.format("gui.touhou_little_maid.skin.tooltips.show_details"));
                // 绘制解析过的文本提示
                drawHoveringText(ParseI18n.parse(str), mouseX, mouseY);
            }

            // 往右绘制
            x = x + 20;

            // 如果超出一定限制，换行
            if (x > 105) {
                x = -100;
                y = y + 30;
            }
        }

        // 绘制便签页的文本提示
        int size = guiNumber.getTabSize(getPackIndex());
        for (int index = 0; index < size; index++) {
            boolean isxInRange = middleX - 98 + 28 * index < mouseX && mouseX < middleX - 98 + 28 * index + 28;
            boolean isyInRange = middleY - 108 < mouseY && mouseY < middleY - 108 + 31;
            if (isxInRange && isyInRange) {
                CustomModelPack<U> hoverPack = modelPackList.get(guiNumber.tabToPackIndex(index, getPageIndex()));
                drawHoveringText(ParseI18n.parse(hoverPack.getPackName()), mouseX, mouseY);
            }
        }

        // 绘制关闭按钮的文本提示
        boolean xInRange = (middleX + 122) < mouseX && mouseX < (middleX + 143);
        boolean yInRange = (middleY - 97) < mouseY && mouseY < (middleY - 80);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.skin.button.close"), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (0 <= button.id && button.id < guiNumber.getTabSize(getPackIndex())) {
            setRowIndex(0);
            setPackIndex(guiNumber.tabToPackIndex(button.id, getPageIndex()));
            this.initGui();
            return;
        }
        if (button.id == 7) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(null));
            return;
        }
        if (button.id == 8) {
            setRowIndex(0);
            setPageIndex(MathHelper.clamp(getPageIndex() - 1, 0, guiNumber.getPageSize() - 1));
            setPackIndex(guiNumber.tabToPackIndex(0, getPageIndex()));
            this.initGui();
            return;
        }
        if (button.id == 9) {
            setRowIndex(0);
            setPageIndex(MathHelper.clamp(getPageIndex() + 1, 0, guiNumber.getPageSize() - 1));
            setPackIndex(guiNumber.tabToPackIndex(0, getPageIndex()));
            this.initGui();
            return;
        }
        if (isShiftKeyDown()) {
            // shift 状态下打开详情页
            openDetailsGui(entity, BUTTON_MODEL_MAP.get(button.id).getModelId());
        } else {
            // 进行模型更改的发包
            notifyModelChange(entity, BUTTON_MODEL_MAP.get(button.id).getModelId());
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            // 向上滚
            if (dWheel > 0) {
                dWheel = 1;
            }
            // 向下滚
            if (dWheel < 0) {
                dWheel = -1;
            }
            int row = MathHelper.clamp(getRowIndex() - dWheel, 0, guiNumber.getRowSize(getPackIndex()));
            if (row != getRowIndex()) {
                setRowIndex(row);
                this.initGui();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
