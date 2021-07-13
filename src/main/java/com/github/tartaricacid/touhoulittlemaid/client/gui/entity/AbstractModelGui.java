package com.github.tartaricacid.touhoulittlemaid.client.gui.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.ImageButtonWithId;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.SizeTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.IModelInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModelGui<T extends LivingEntity, E extends IModelInfo> extends Screen {
    public static final Button.IPressable NO_PRESS = (b) -> {
    };
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select.png");
    private static final ResourceLocation SIDE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select_side.png");
    protected final T entity;
    private final SkinGuiNumber<E> guiNumber;
    private final List<CustomModelPack<E>> modelPackList;

    public AbstractModelGui(T entity, List<CustomModelPack<E>> listPack) {
        super(new StringTextComponent("Custom Model GUI"));
        this.entity = entity;
        this.modelPackList = listPack;
        this.guiNumber = new SkinGuiNumber<>(modelPackList);
        setPageIndex(MathHelper.clamp(getPageIndex(), 0, guiNumber.getPageSize() - 1));
        setPackIndex(MathHelper.clamp(getPackIndex(), 0, guiNumber.getPackSize() - 1));
        setRowIndex(MathHelper.clamp(getRowIndex(), 0, guiNumber.getRowSize(getPackIndex())));
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
    abstract void drawRightEntity(int posX, int posY, E modelItem);

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
     * @param entity    实体
     * @param modelInfo 该实体应该对应的模型数据
     */
    abstract void notifyModelChange(T entity, E modelInfo);

    abstract int getPackIndex();

    abstract void setPackIndex(int packIndex);

    abstract int getRowIndex();

    abstract void setRowIndex(int rowIndex);

    abstract int getPageIndex();

    abstract void setPageIndex(int packIndex);

    @Override
    public void init() {
        // 清除按钮列表、标签列表，用来给后面重载按键用的
        this.buttons.clear();
        this.children.clear();

        int startX = this.width / 2 + 50;
        int startY = this.height / 2 + 5;

        // 模型包的分栏按钮
        for (int index = 0; index < 7; index++) {
            addTabButton(startX, startY, index);
        }

        // 关闭当前界面的按键
        this.addButton(new ImageButton(startX + 122, startY - 97, 21, 17, 58, 201, 18, BG,
                (b) -> getMinecraft().submit(() -> getMinecraft().setScreen(null))));

        // 添加切换页面的按钮
        addPageButton(startX, startY);

        // 添加切换模型的按钮
        addModelButton(startX, startY);
    }

    private void addModelButton(int startX, int startY) {
        // 添加按键，顺便装填按键对应模型的索引
        CustomModelPack<E> pack = modelPackList.get(getPackIndex());

        // 起始坐标
        int offsetX = -100;
        int offsetY = -35;

        // 切割列表，让其一页最多显示 44 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始添加按键，顺便装填按键对应模型的索引
        for (E modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            this.addButton(new ImageButton(startX + offsetX - 8, startY + offsetY - 26, 15, 24, 41, 201, 24, BG,
                    (b) -> notifyModelChange(entity, modelItem)));

            // 往右绘制
            offsetX = offsetX + 20;

            // 如果超出一定限制，换行
            if (offsetX > 105) {
                offsetX = -100;
                offsetY = offsetY + 30;
            }
        }
    }

    private void addPageButton(int startX, int startY) {
        Button prePage = new Button(startX - 119, startY - 101, 20, 20, new StringTextComponent("<"), (b) -> {
            setRowIndex(0);
            setPageIndex(MathHelper.clamp(getPageIndex() - 1, 0, guiNumber.getPageSize() - 1));
            setPackIndex(guiNumber.tabToPackIndex(0, getPageIndex()));
            this.init();
        });
        Button nextPage = new Button(startX + 99, startY - 101, 20, 20, new StringTextComponent(">"), (b) -> {
            setRowIndex(0);
            setPageIndex(MathHelper.clamp(getPageIndex() + 1, 0, guiNumber.getPageSize() - 1));
            setPackIndex(guiNumber.tabToPackIndex(0, getPageIndex()));
            this.init();
        });
        if (getPageIndex() == 0) {
            prePage.active = false;
        }
        if (getPageIndex() == guiNumber.getPageSize() - 1) {
            nextPage.active = false;
        }
        this.addButton(prePage);
        this.addButton(nextPage);
    }

    private void addTabButton(int startX, int startY, int index) {
        if (index == guiNumber.getTabIndex(getPackIndex())) {
            this.addButton(new ImageButton(startX - 98 + 28 * index, startY - 108, 28, 31, 116, 224, 0, BG, NO_PRESS));
        } else if (index < guiNumber.getTabSize(getPackIndex())) {
            this.addButton(new ImageButtonWithId(index, startX - 98 + 28 * index, startY - 105, 28, 25, 116, 194, 0, BG,
                    (b) -> {
                        setRowIndex(0);
                        setPackIndex(guiNumber.tabToPackIndex(((ImageButtonWithId) b).getIndex(), getPageIndex()));
                        this.init();
                    }));
        } else {
            ImageButton buttonImage = new ImageButton(startX - 98 + 28 * index, startY - 105, 28, 25, 116, 194, 0, BG, NO_PRESS);
            buttonImage.visible = false;
            this.addButton(buttonImage);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        matrixStack.translate(0, 0, -100);

        // 中心点
        int middleX = this.width / 2 + 50;
        int middleY = this.height / 2 + 5;

        // 绘制灰色默认背景
        renderBackground(matrixStack);

        // 绘制 GUI 背景
        getMinecraft().textureManager.bind(BG);
        blit(matrixStack, middleX - 256 / 2, middleY - 80, 0, 0, 256, 180);
        getMinecraft().textureManager.bind(SIDE);
        blit(matrixStack, middleX - 256 / 2 + 250, middleY - 80, 0, 0, 24, 180);

        // 绘制侧边的滚动条
        drawScrollSide(matrixStack, middleX, middleY);

        // 调用父类方法绘制按钮列表、标签列表
        drawButton(matrixStack, mouseX, mouseY, partialTicks);

        // 绘制标签栏图标
        drawTabIcon(matrixStack, middleX, middleY);

        // 绘制左边示例实体
        double scale = getMinecraft().getWindow().getGuiScale();
        RenderSystem.enableScissor(0, 0,
                (int) ((middleX - 256 / 2) * scale), (int) (height * scale));
        drawLeftEntity(middleX, middleY, mouseX, mouseY);
        RenderSystem.disableScissor();

        // 绘制实体，绘制完毕后绘制文本提示
        drawEntity(matrixStack, middleX, middleY);

        // 绘制其他文本提示
        drawTooltips(matrixStack, mouseX, mouseY, middleX, middleY);
    }

    private void drawButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for (Widget button : this.buttons) {
            button.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    private void drawScrollSide(MatrixStack matrixStack, int middleX, int middleY) {
        if (guiNumber.canScroll(getPackIndex(), getRowIndex())) {
            blit(matrixStack, middleX - 256 / 2 + 254,
                    middleY - 72 + (int) (149 * guiNumber.getCurrentScroll(getPackIndex(), getRowIndex())),
                    24, 0, 12, 15);
        } else {
            blit(matrixStack, middleX - 256 / 2 + 254,
                    middleY - 72 + (int) (149 * guiNumber.getCurrentScroll(getPackIndex(), getRowIndex())),
                    36, 0, 12, 15);
        }
    }

    private void drawTabIcon(MatrixStack matrixStack, int middleX, int middleY) {
        // 模型包的分栏按钮图标
        int size = guiNumber.getTabSize(getPackIndex());
        for (int index = 0; index < size; index++) {
            CustomModelPack<E> pack = modelPackList.get(guiNumber.tabToPackIndex(index, getPageIndex()));
            ResourceLocation icon = pack.getIcon();
            if (icon != null) {
                if (pack.getIconAnimation() == CustomModelPack.AnimationState.UNCHECK) {
                    checkIconAnimation(pack, icon);
                }
                if (pack.getIconAnimation() == CustomModelPack.AnimationState.FALSE) {
                    getMinecraft().textureManager.bind(icon);
                    blit(matrixStack, middleX - 92 + 28 * index, middleY - 98,
                            0, 0, 16, 16, 16, 16);
                } else {
                    getMinecraft().textureManager.bind(icon);
                    int time = getTickTime() / pack.getIconDelay();
                    int iconIndex = time % pack.getIconAspectRatio();
                    blit(matrixStack, middleX - 92 + 28 * index, middleY - 98,
                            0, iconIndex * 16, 16,
                            16, 16, 16 * pack.getIconAspectRatio());
                }
            }
        }
    }

    private int getTickTime() {
        return (int) System.currentTimeMillis() / 50;
    }

    private void checkIconAnimation(CustomModelPack<E> pack, ResourceLocation icon) {
        Texture iconText = getMinecraft().textureManager.getTexture(icon);
        if (iconText instanceof SizeTexture) {
            int width = ((SizeTexture) iconText).getWidth();
            int height = ((SizeTexture) iconText).getHeight();
            if (width >= height) {
                pack.setIconAnimation(CustomModelPack.AnimationState.FALSE);
            } else {
                pack.setIconAnimation(CustomModelPack.AnimationState.TRUE);
                pack.setIconAspectRatio(height / width);
            }
        } else {
            pack.setIconAnimation(CustomModelPack.AnimationState.FALSE);
        }
    }

    /**
     * 绘制所有的模型实体图案
     */
    private void drawEntity(MatrixStack matrixStack, int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPack<E> pack = modelPackList.get(getPackIndex());

        // 绘制包信息
        drawPackInfoText(matrixStack, pack, middleX, middleY);

        // 起始坐标
        int offsetX = -100;
        int offsetY = -38;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (E modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            drawRightEntity(middleX + offsetX, middleY + offsetY, modelItem);
            // 往右绘制
            offsetX = offsetX + 20;
            // 如果超出一定限制，换行
            if (offsetX > 105) {
                offsetX = -100;
                offsetY = offsetY + 30;
            }
        }
    }

    /**
     * 绘制包的文本信息
     */
    private void drawPackInfoText(MatrixStack matrixStack, CustomModelPack<E> pack, int middleX, int middleY) {
        int offsetY = -80;

        // 绘制包名
        drawCenteredString(matrixStack, font, ParseI18n.parse(pack.getPackName()), middleX - 193, middleY + offsetY, 0xffffff);

        // 如果描述不为空，逐行绘制描述
        for (ITextComponent str : ParseI18n.parse(pack.getDescription())) {
            List<ITextProperties> split = font.getSplitter().splitLines(str, (middleX - 256 / 2) - 20, Style.EMPTY);
            for (ITextProperties properties : split) {
                offsetY += 10;
                drawCenteredString(matrixStack, font, properties.getString(), middleX - 193, middleY + offsetY, TextFormatting.DARK_GRAY.getColor());
            }
        }

        // 绘制作者列表
        if (!pack.getAuthor().isEmpty()) {
            for (List<String> textList : Lists.partition(pack.getAuthor(), 2)) {
                offsetY += 10;
                drawCenteredString(matrixStack, font, new StringTextComponent(textList.toString()).withStyle(TextFormatting.GOLD),
                        middleX - 193, middleY + offsetY, 0xffffff);
            }
        }

        // 绘制版本信息
        if (pack.getVersion() != null) {
            offsetY += 10;
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.skin.text.version", pack.getVersion())
                            .withStyle(TextFormatting.DARK_AQUA),
                    middleX - 193, middleY + offsetY, 0xffffff);
        }

        // 绘制日期信息
        if (pack.getDate() != null) {
            offsetY += 10;
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.skin.text.date", pack.getDate())
                            .withStyle(TextFormatting.GREEN),
                    middleX - 193, middleY + offsetY, 0xffffff);
        }

        // 绘制最后的翻页数
        drawCenteredString(matrixStack, font, String.format("%s/%s", getPageIndex() + 1, guiNumber.getPageSize()), middleX, middleY - 120, 0xffffff);
    }

    /**
     * 绘制模型对应的文本提示<br>
     * 用遍历方式绘制文本提示，因为绝大多数情况下是空循环体（可能就涉及几个简单的 int 运算）<br>
     * 应该不会存在性能问题<br>
     */
    private void drawTooltips(MatrixStack matrixStack, int mouseX, int mouseY, int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPack<E> pack = modelPackList.get(getPackIndex());

        // 起始坐标
        int offsetX = -100;
        int offsetY = -35;

        // 切割列表，让其一页最多显示 44 个模型，但是又不至于溢出
        int fromIndex = guiNumber.modelFromIndex(getRowIndex());
        int toIndex = guiNumber.modelToIndex(getPackIndex(), getRowIndex());

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (E modelItem : pack.getModelList().subList(fromIndex, toIndex)) {
            // 判定鼠标所在的位置
            boolean isxInRange = middleX + offsetX - 8 < mouseX && mouseX < middleX + offsetX + 7;
            boolean isyInRange = middleY + offsetY - 23 < mouseY && mouseY < middleY + offsetY + 1;

            // 在位置内，就绘制文本提示
            if (isxInRange && isyInRange) {
                // 绘制的文本
                List<String> str = new ArrayList<>();
                // 塞入模型名称
                str.add(modelItem.getName());
                // 塞入描述
                str.addAll(modelItem.getDescription());
                // 转换为 ITextComponent
                List<ITextComponent> tooltips = ParseI18n.parse(str);
                // 塞入提示语
                tooltips.add(new TranslationTextComponent("gui.touhou_little_maid.skin.tooltips.show_details")
                        .withStyle(TextFormatting.DARK_GRAY, TextFormatting.ITALIC));
                // 绘制解析过的文本提示
                renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
            }

            // 往右绘制
            offsetX = offsetX + 20;

            // 如果超出一定限制，换行
            if (offsetX > 105) {
                offsetX = -100;
                offsetY = offsetY + 30;
            }
        }

        // 绘制标签页的文本提示
        int size = guiNumber.getTabSize(getPackIndex());
        for (int index = 0; index < size; index++) {
            boolean isxInRange = middleX - 98 + 28 * index < mouseX && mouseX < middleX - 98 + 28 * index + 28;
            boolean isyInRange = middleY - 108 < mouseY && mouseY < middleY - 108 + 31;
            if (isxInRange && isyInRange) {
                CustomModelPack<E> hoverPack = modelPackList.get(guiNumber.tabToPackIndex(index, getPageIndex()));
                renderTooltip(matrixStack, ParseI18n.parse(hoverPack.getPackName()), mouseX, mouseY);
            }
        }

        // 绘制关闭按钮的文本提示
        boolean xInRange = (middleX + 122) < mouseX && mouseX < (middleX + 143);
        boolean yInRange = (middleY - 97) < mouseY && mouseY < (middleY - 80);
        if (xInRange && yInRange) {
            renderTooltip(matrixStack, new TranslationTextComponent("gui.touhou_little_maid.skin.button.close"), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta != 0) {
            int index = 0;
            // 向上滚
            if (delta > 0) {
                index = 1;
            }
            // 向下滚
            if (delta < 0) {
                index = -1;
            }
            int row = MathHelper.clamp(getRowIndex() - index, 0, guiNumber.getRowSize(getPackIndex()));
            if (row != getRowIndex()) {
                setRowIndex(row);
                this.init();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
