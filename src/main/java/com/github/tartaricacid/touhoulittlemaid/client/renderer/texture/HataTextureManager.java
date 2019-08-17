package com.github.tartaricacid.touhoulittlemaid.client.renderer.texture;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * @author TartaricAcid
 * @date 2019/8/13 20:03
 **/
@SideOnly(Side.CLIENT)
public class HataTextureManager {
    private static final Map<String, ITextureObject> MAP_TEXTURE_OBJECTS = Maps.newHashMap();
    private static final Logger LOGGER = LogManager.getLogger("HataTextureManager");
    private static IResourceManager RESOURCE_MANAGER;

    public HataTextureManager(IResourceManager resourceManager) {
        RESOURCE_MANAGER = resourceManager;
    }

    public void bindTexture(String texturePath) {
        GlStateManager.bindTexture(loadTexture(texturePath));
    }

    public int loadTexture(String texturePath) {
        ITextureObject itextureobject = MAP_TEXTURE_OBJECTS.get(texturePath);
        if (itextureobject == null) {
            itextureobject = new HataTexture(texturePath);
            this.loadTexture(texturePath, itextureobject);
        }
        return itextureobject.getGlTextureId();
    }

    private void loadTexture(String texturePath, ITextureObject textureObj) {
        try {
            textureObj.loadTexture(RESOURCE_MANAGER);
        } catch (IOException ioexception) {
            LOGGER.warn("Failed to load texture: {}", texturePath, ioexception);
            textureObj = TextureUtil.MISSING_TEXTURE;
            MAP_TEXTURE_OBJECTS.put(texturePath, textureObj);
        }
        MAP_TEXTURE_OBJECTS.put(texturePath, textureObj);
    }

    public Map<String, ITextureObject> getMapTexture() {
        return MAP_TEXTURE_OBJECTS;
    }

    public ITextureObject getTexture(String texturePath) {
        return MAP_TEXTURE_OBJECTS.get(texturePath);
    }

    public void deleteTexture(String texturePath) {
        ITextureObject itextureobject = this.getTexture(texturePath);
        if (itextureobject != null) {
            MAP_TEXTURE_OBJECTS.remove(texturePath);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    public void clear() {
        MAP_TEXTURE_OBJECTS.clear();
    }
}
