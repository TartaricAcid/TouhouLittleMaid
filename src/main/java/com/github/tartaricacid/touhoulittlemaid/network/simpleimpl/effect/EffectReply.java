package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.effect;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Collection;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/10/17 23:27
 **/
public class EffectReply implements IMessage {
    private int entityId;
    private int effectSize;
    private List<EffectData> effectList = Lists.newArrayList();

    public EffectReply() {
    }

    public EffectReply(int entityId, Collection<PotionEffect> potionEffects) {
        this.entityId = entityId;
        this.effectSize = potionEffects.size();
        for (PotionEffect effect : potionEffects) {
            effectList.add(new EffectData(effect));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.effectSize = buf.readInt();
        for (int i = 0; i < effectSize; i++) {
            effectList.add(new EffectData(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(effectSize);
        for (EffectData data : effectList) {
            data.write(buf);
        }
    }

    public int getEntityId() {
        return entityId;
    }

    public List<EffectData> getEffectList() {
        return effectList;
    }
}
