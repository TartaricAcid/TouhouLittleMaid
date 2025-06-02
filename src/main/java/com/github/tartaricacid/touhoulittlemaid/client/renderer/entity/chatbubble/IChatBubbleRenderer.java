package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IChatBubbleRenderer {
    /**
     * 气泡框高度
     *
     * @return 高度
     */
    int getHeight();

    /**
     * 气泡框宽度
     *
     * @return 宽度
     */
    int getWidth();

    /**
     * 渲染气泡框内部
     *
     * @param renderer 女仆实体渲染器
     * @param graphics 渲染工具
     */
    void render(EntityMaidRenderer renderer, EntityGraphics graphics);

    /**
     * 获取气泡框背景纹理
     */
    ResourceLocation getBackgroundTexture();

    /**
     * 渲染位置，是在左侧，右侧还是中间
     */
    enum Position {
        LEFT,
        RIGHT,
        CENTER
    }
}
