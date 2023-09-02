package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import net.minecraft.client.renderer.texture.Texture;

public abstract class SizeTexture extends Texture {
    /**
     * 获取材质宽度
     *
     * @return 宽度（像素）
     */
    abstract public int getWidth();

    /**
     * 获取材质高度
     *
     * @return 高度（像素）
     */
    abstract public int getHeight();

    /**
     * 给定路径的文件是否存在
     *
     * @return 存在与否
     */
    abstract public boolean isExist();
}
