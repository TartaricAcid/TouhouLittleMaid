package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author TartaricAcid
 * @date 2019/8/13 20:10
 **/
@SideOnly(Side.CLIENT)
public class HataTexture extends AbstractTexture {
    private final String texturePath;

    public HataTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    @Override
    public void loadTexture(@Nonnull IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = FileUtils.openInputStream(new File(texturePath));
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(fileInputStream);
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, false);
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }
}
