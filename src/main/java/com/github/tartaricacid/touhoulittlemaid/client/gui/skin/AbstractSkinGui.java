package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/12 12:27
 **/
@SideOnly(Side.CLIENT)
public abstract class AbstractSkinGui<T extends EntityLivingBase> extends GuiScreen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select.png");
    /**
     * 按钮的 ID -> 模型 映射
     */
    private static final HashMap<Integer, ModelItem> BUTTON_MODEL_MAP = Maps.newHashMap();
    /**
     * 一页最多能容纳 44 个模型
     */
    private static final int PAGE_MAX_NUM = 44;
    /**
     * 用来在子类中通过实体 id 获取缓存中的实体对象
     */
    protected static String ENTITY_ID;
    /**
     * 存储包的总数
     */
    private static int PACK_COUNT;
    /**
     * 存储页面的总数
     */
    private static int PAGE_COUNT = 1;
    /**
     * 使用的模型包列表
     */
    private static List<CustomModelPackPOJO> MODEL_PACK_LIST = Lists.newArrayList();
    protected T entity;

    public AbstractSkinGui(T entity, List<CustomModelPackPOJO> listPack, String entityId) {
        this.entity = entity;
        MODEL_PACK_LIST = listPack;
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
    abstract void drawRightEntity(int posX, int posY, ModelItem modelItem);

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

    abstract int getPageIndex();

    abstract void setPageIndex(int pageIndex);

    @Override
    public void initGui() {
        // 清除按钮列表、标签列表，用来给后面重载按键用的
        this.buttonList.clear();
        this.labelList.clear();

        int i = this.width / 2 + 50;
        int j = this.height / 2 + 5;
        int id = 0;

        // 添加切换模型包的按钮
        this.buttonList.add(new GuiButtonImage(id++, i - 118, j - 89, 11, 17, 15, 201, 18, BG));
        this.buttonList.add(new GuiButtonImage(id++, i + 107, j - 89, 11, 17, 1, 201, 18, BG));

        // 添加切换页面的按钮
        this.buttonList.add(new GuiButtonImage(id++, i - 118, j + 73, 11, 17, 15, 201, 18, BG));
        this.buttonList.add(new GuiButtonImage(id++, i + 107, j + 73, 11, 17, 1, 201, 18, BG));

        // 关闭当前界面的按键
        this.buttonList.add(new GuiButtonImage(id++, i + 104, j - 116, 21, 17, 58, 201, 18, BG));

        // 添加按键，顺便装填按键对应模型的索引
        CustomModelPackPOJO pojo = MODEL_PACK_LIST.get(getPackIndex());

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * getPageIndex();
        int toIndex = PAGE_MAX_NUM * (getPageIndex() + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (getPageIndex() + 1);

        // 开始添加按键，顺便装填按键对应模型的索引
        for (ModelItem modelItem : pojo.getModelList().subList(fromIndex, toIndex)) {
            this.buttonList.add(new GuiButtonImage(id, i + x - 8, j + y - 23, 15, 24, 41, 201, 24, BG));
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
        // 计算出包总数（用来刷新）
        PACK_COUNT = MODEL_PACK_LIST.size();

        // 计算出模型分页总数（用来刷新）
        PAGE_COUNT = (MODEL_PACK_LIST.get(getPackIndex()).getModelList().size() - 1) / PAGE_MAX_NUM + 1;

        // 中心点
        int middleX = this.width / 2 + 50;
        int middleY = this.height / 2 + 5;

        // 绘制灰色默认背景
        drawDefaultBackground();

        // 绘制 GUI 背景
        mc.renderEngine.bindTexture(BG);
        drawTexturedModalRect(middleX - 256 / 2, middleY - 100, 0, 0, 256, 200);

        // 调用父类方法绘制按钮列表、标签列表
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 绘制左边示例实体
        drawLeftEntity(middleX, middleY, mouseX, mouseY);

        // 绘制实体，绘制完毕后绘制文本提示
        drawEntity(middleX, middleY);
        drawTooltips(mouseX, mouseY, middleX, middleY);
    }

    /**
     * 绘制所有的模型实体图案
     */
    private void drawEntity(int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPackPOJO pojo = MODEL_PACK_LIST.get(getPackIndex());

        // 绘制包信息
        drawPackInfoText(pojo, middleX, middleY);

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * getPageIndex();
        int toIndex = PAGE_MAX_NUM * (getPageIndex() + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (getPageIndex() + 1);

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (ModelItem modelItem : pojo.getModelList().subList(fromIndex, toIndex)) {
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
    private void drawPackInfoText(CustomModelPackPOJO pojo, int middleX, int middleY) {
        int offSet = -80;

        // 绘制包名
        drawCenteredString(fontRenderer, ParseI18n.parse(pojo.getPackName()), middleX - 193, middleY + offSet, 0xffffff);

        // 如果描述不为空，逐行绘制描述
        for (String str : ParseI18n.parse(pojo.getDescription())) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.GRAY + str, middleX - 193, middleY + offSet, 0xffffff);
        }

        // 绘制作者列表
        if (!pojo.getAuthor().isEmpty()) {
            for (List<String> textList : Lists.partition(pojo.getAuthor(), 2)) {
                offSet += 10;
                drawCenteredString(fontRenderer, TextFormatting.GOLD + textList.toString(),
                        middleX - 193, middleY + offSet, 0xffffff);
            }
        }

        // 绘制版本信息
        if (pojo.getVersion() != null) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.DARK_AQUA + I18n.format("gui.touhou_little_maid.skin.text.version", pojo.getVersion()),
                    middleX - 193, middleY + offSet, 0xffffff);
        }


        // 绘制日期信息
        if (pojo.getDate() != null) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.GREEN + I18n.format("gui.touhou_little_maid.skin.text.date", pojo.getDate()),
                    middleX - 193, middleY + offSet, 0xffffff);
        }


        // 绘制最后的翻页数
        drawCenteredString(fontRenderer, String.format("%s/%s", getPackIndex() + 1, PACK_COUNT), middleX, middleY - 84, 0xffffff);
        drawCenteredString(fontRenderer, String.format("%s/%s", getPageIndex() + 1, PAGE_COUNT), middleX, middleY + 78, 0xffffff);
    }

    /**
     * 绘制模型对应的文本提示<br>
     * 用遍历方式绘制文本提示，因为绝大多数情况下是空循环体（可能就涉及几个简单的 int 运算）<br>
     * 应该不会存在性能问题<br>
     */
    private void drawTooltips(int mouseX, int mouseY, int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPackPOJO pojo = MODEL_PACK_LIST.get(getPackIndex());

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * getPageIndex();
        int toIndex = PAGE_MAX_NUM * (getPageIndex() + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (getPageIndex() + 1);

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (ModelItem modelItem : pojo.getModelList().subList(fromIndex, toIndex)) {
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

        // 绘制关闭按钮的文本提示
        boolean xInRange = (middleX + 104) < mouseX && mouseX < (middleX + 125);
        boolean yInRange = (middleY - 116) < mouseY && mouseY < (middleY - 99);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.skin.button.close"), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            // 向前翻包按钮
            case 0:
                // 重载包索引、重新计算该包分页总数、将页面索引重载成 0 ，同时重载所有的按键
                setPackIndex((getPackIndex() <= 0) ? PACK_COUNT - 1 : getPackIndex() - 1);
                setPageIndex(0);
                this.initGui();
                return;

            // 向后翻包
            case 1:
                // 重载包索引、重新计算该包模型的总数、将页面索引重载成 0 ，同时重载所有的按键
                setPackIndex((getPackIndex() >= PACK_COUNT - 1) ? 0 : getPackIndex() + 1);
                setPageIndex(0);
                this.initGui();
                return;

            // 向前翻页
            case 2:
                // 重载页面索引、重载所有的按键
                setPageIndex((getPageIndex() <= 0) ? PAGE_COUNT - 1 : getPageIndex() - 1);
                this.initGui();
                return;

            // 向后翻页
            case 3:
                // 重载页面索引、重载所有的按键
                setPageIndex((getPageIndex() >= PAGE_COUNT - 1) ? 0 : getPageIndex() + 1);
                this.initGui();
                return;

            case 4:
                mc.addScheduledTask(() -> mc.displayGuiScreen(null));
                return;

            // 其他按键
            default:
                if (isShiftKeyDown()) {
                    // shift 状态下打开详情页
                    openDetailsGui(entity, BUTTON_MODEL_MAP.get(button.id).getModelId());
                } else {
                    // 进行模型更改的发包
                    notifyModelChange(entity, BUTTON_MODEL_MAP.get(button.id).getModelId());
                }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
