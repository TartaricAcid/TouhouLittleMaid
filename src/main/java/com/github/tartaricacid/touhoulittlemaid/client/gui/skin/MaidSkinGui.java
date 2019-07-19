package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ChangeMaidSkinMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
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
public class MaidSkinGui extends GuiScreen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_select.png");
    /**
     * 按钮的 ID -> 模型 映射
     */
    private static final HashMap<Integer, ModelItem> BUTTON_MODEL_MAP = new HashMap<>();
    /**
     * 存储包的总数和当前包的索引
     */
    private static int PACK_COUNT = ClientProxy.MODEL_PACK_LIST.size();
    private static int PACK_INDEX = 0;
    /**
     * 一页最多能容纳 44 个模型
     */
    private static final int PAGE_MAX_NUM = 44;
    /**
     * 存储页面的总数和当前页面索引
     */
    private static int MODEL_PAGE_COUNT = 1;
    private static int MODEL_PAGE = 0;
    private EntityMaid maid;

    public MaidSkinGui(EntityMaid maid) {
        this.maid = maid;
    }

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
        CustomModelPackPOJO pojo = ClientProxy.MODEL_PACK_LIST.get(PACK_INDEX);

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * MODEL_PAGE;
        int toIndex = PAGE_MAX_NUM * (MODEL_PAGE + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (MODEL_PAGE + 1);

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
        PACK_COUNT = ClientProxy.MODEL_PACK_LIST.size();

        // 计算出模型分页总数（用来刷新）
        MODEL_PAGE_COUNT = ClientProxy.MODEL_PACK_LIST.get(PACK_INDEX).getModelList().size() / PAGE_MAX_NUM + 1;

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
        EntityMaid entityMaidNew = new EntityMaid(mc.world);
        NBTTagCompound nbt = new NBTTagCompound();
        maid.writeEntityToNBT(nbt);
        entityMaidNew.readEntityFromNBT(nbt);
        GuiInventory.drawEntityOnScreen(middleX - 190, middleY + 80, 45, middleX - 190 - mouseX, middleY + 80 - 40 - mouseY, entityMaidNew);

        // 绘制实体，绘制完毕后绘制文本提示
        drawEntity(middleX, middleY);
        drawTooltips(mouseX, mouseY, middleX, middleY);
    }

    /**
     * 绘制所有的模型实体图案
     */
    private void drawEntity(int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPackPOJO pojo = ClientProxy.MODEL_PACK_LIST.get(PACK_INDEX);

        // 绘制包信息
        drawPackInfoText(pojo, middleX, middleY);

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * MODEL_PAGE;
        int toIndex = PAGE_MAX_NUM * (MODEL_PAGE + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (MODEL_PAGE + 1);

        // 开始绘制实体图案，并往上添加对应模型和材质
        for (ModelItem modelItem : pojo.getModelList().subList(fromIndex, toIndex)) {
            EntityMaid maid = new EntityMaid(Minecraft.getMinecraft().world);
            maid.setModelLocation(modelItem.getModel());
            maid.setTextureLocation(modelItem.getTexture());
            GuiInventory.drawEntityOnScreen(middleX + x, middleY + y, 12, -25, -20, maid);

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
        if (pojo.getDescription() != null) {
            for (String str : ParseI18n.parse(pojo.getDescription())) {
                offSet += 10;
                drawCenteredString(fontRenderer, TextFormatting.GRAY + str, middleX - 193, middleY + offSet, 0xffffff);
            }
        }

        // 绘制作者列表
        if (pojo.getAuthor() != null) {
            offSet += 10;
            drawCenteredString(fontRenderer, TextFormatting.GOLD + I18n.format("gui.touhou_little_maid.skin.text.author", pojo.getAuthor().toString()),
                    middleX - 193, middleY + offSet, 0xffffff);
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
        drawCenteredString(fontRenderer, String.format("%s/%s", PACK_INDEX + 1, PACK_COUNT), middleX, middleY - 84, 0xffffff);
        drawCenteredString(fontRenderer, String.format("%s/%s", MODEL_PAGE + 1, MODEL_PAGE_COUNT), middleX, middleY + 78, 0xffffff);
    }

    /**
     * 绘制模型对应的文本提示<br>
     * 用遍历方式绘制文本提示，因为绝大多数情况下是空循环体（可能就涉及几个简单的 int 运算）<br>
     * 应该不会存在性能问题<br>
     */
    private void drawTooltips(int mouseX, int mouseY, int middleX, int middleY) {
        // 获取当前包索引得到的模型列表
        CustomModelPackPOJO pojo = ClientProxy.MODEL_PACK_LIST.get(PACK_INDEX);

        // 起始坐标
        int x = -100;
        int y = -35;

        // 切割列表，让其一页最多显示 PAGE_MAX_NUM 个模型，但是又不至于溢出
        int fromIndex = PAGE_MAX_NUM * MODEL_PAGE;
        int toIndex = PAGE_MAX_NUM * (MODEL_PAGE + 1) > pojo.getModelList().size() ? pojo.getModelList().size() : PAGE_MAX_NUM * (MODEL_PAGE + 1);

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

                // 如果描述不为空，塞入描述
                if (modelItem.getDescription() != null) {
                    str.addAll(modelItem.getDescription());
                }

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
                PACK_INDEX = (PACK_INDEX <= 0) ? PACK_COUNT - 1 : PACK_INDEX - 1;
                MODEL_PAGE = 0;
                this.initGui();
                return;

            // 向后翻包
            case 1:
                // 重载包索引、重新计算该包模型的总数、将页面索引重载成 0 ，同时重载所有的按键
                PACK_INDEX = (PACK_INDEX >= PACK_COUNT - 1) ? 0 : PACK_INDEX + 1;
                MODEL_PAGE = 0;
                this.initGui();
                return;

            // 向前翻页
            case 2:
                // 重载页面索引、重载所有的按键
                MODEL_PAGE = (MODEL_PAGE <= 0) ? MODEL_PAGE_COUNT - 1 : MODEL_PAGE - 1;
                this.initGui();
                return;

            // 向后翻页
            case 3:
                // 重载页面索引、重载所有的按键
                MODEL_PAGE = (MODEL_PAGE >= MODEL_PAGE_COUNT - 1) ? 0 : MODEL_PAGE + 1;
                this.initGui();
                return;

            case 4:
                mc.addScheduledTask(() -> mc.displayGuiScreen(null));
                return;

            // 其他按键进行模型更改的发包
            default:
                CommonProxy.INSTANCE.sendToServer(new ChangeMaidSkinMessage(maid.getUniqueID(),
                        BUTTON_MODEL_MAP.get(button.id).getModel().length(),
                        BUTTON_MODEL_MAP.get(button.id).getTexture().length(),
                        BUTTON_MODEL_MAP.get(button.id).getName().length(),
                        BUTTON_MODEL_MAP.get(button.id).getModel(),
                        BUTTON_MODEL_MAP.get(button.id).getTexture(),
                        BUTTON_MODEL_MAP.get(button.id).getName()));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
