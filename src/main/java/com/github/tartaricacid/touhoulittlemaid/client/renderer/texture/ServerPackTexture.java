package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ServerPackTexture extends AbstractTexture {
    private final ResourceLocation texturePath;
    private final ZipFile zipFile;

    public ServerPackTexture(ZipFile zipFile, ResourceLocation texturePath) {
        this.zipFile = zipFile;
        this.texturePath = texturePath;
    }

    @Override
    public void loadTexture(@Nonnull IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        try {
            ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", texturePath.getNamespace(), texturePath.getPath()));
            if (entry == null) {
                throw new IOException();
            }
            try (InputStream stream = zipFile.getInputStream(entry)) {
                BufferedImage bufferedimage = TextureUtil.readBufferedImage(stream);
                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, false, false);
            }
        } catch (IllegalStateException exception) {
            throw new IOException();
        }
    }
}
