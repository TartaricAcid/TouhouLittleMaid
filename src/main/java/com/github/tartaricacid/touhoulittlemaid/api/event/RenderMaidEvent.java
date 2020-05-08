package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Cancelable
@SideOnly(Side.CLIENT)
public class RenderMaidEvent extends Event {
    private final EntityMaid maid;
    private ModelData modelData;

    public RenderMaidEvent(EntityMaid maid, ModelData modelData) {
        this.maid = maid;
        this.modelData = modelData;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ModelData getModelData() {
        return modelData;
    }

    public static class ModelData {
        private EntityModelJson model;
        private MaidModelInfo info;
        private List<Object> animations;

        public ModelData(EntityModelJson model, MaidModelInfo info, List<Object> animations) {
            this.model = model;
            this.info = info;
            this.animations = animations;
        }

        public void setModel(EntityModelJson model) {
            this.model = model;
        }

        public void setInfo(MaidModelInfo info) {
            this.info = info;
        }

        public void setAnimations(List<Object> animations) {
            this.animations = animations;
        }

        public EntityModelJson getModel() {
            return model;
        }

        public MaidModelInfo getInfo() {
            return info;
        }

        public List<Object> getAnimations() {
            return animations;
        }
    }
}
