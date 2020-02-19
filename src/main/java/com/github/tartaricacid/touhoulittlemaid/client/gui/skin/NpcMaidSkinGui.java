package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SendNpcMaidModelMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.entity.EntityCustomNpc;

import java.util.Optional;

public class NpcMaidSkinGui extends MaidSkinGui {
    private EntityCustomNpc npc;

    public NpcMaidSkinGui(EntityCustomNpc npc, EntityMaid maid) {
        super(maid);
        this.npc = npc;
    }

    @Override
    void notifyModelChange(EntityMaid maid, ResourceLocation modelId) {
        Optional<MaidModelItem> info = CustomModelLoader.MAID_MODEL.getInfo(modelId.toString());
        if (info.isPresent()) {
            ResourceLocation texture = info.get().getTexture();
            // 客户端数据更改
            // 虽然不更改也会在半秒后自动同步，但是强迫症使然
            maid.setModelId(modelId.toString());
            npc.textureLocation = texture;
            // 服务端数据更改
            CommonProxy.INSTANCE.sendToServer(new SendNpcMaidModelMessage(npc.getUniqueID(), modelId.toString(), texture.toString()));
        }
    }
}
