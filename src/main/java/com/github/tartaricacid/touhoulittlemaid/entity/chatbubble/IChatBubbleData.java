package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble.IChatBubbleRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IChatBubbleData {
    /**
     * 默认提供的两种气泡背景，推荐 type2
     */
    ResourceLocation TYPE_1 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/chat_bubble/type1.png");
    ResourceLocation TYPE_2 = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/chat_bubble/type2.png");

    /**
     * 默认的气泡持续时间，15 秒
     */
    int DEFAULT_EXIST_TICK = 15 * 20;

    /**
     * 默认的气泡优先级
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * 气泡的持续时间
     *
     * @return 单位：tick
     */
    int existTick();

    /**
     * 气泡对应的序列化器的注册 ID
     *
     * @return 气泡对应的序列化器的注册 ID
     */
    ResourceLocation id();

    /**
     * 气泡的优先级，数字越大越高
     *
     * @return 气泡的优先级
     */
    default int priority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * 客户端的渲染类
     *
     * @param position 排列位置
     * @return 渲染类
     */
    @OnlyIn(Dist.CLIENT)
    IChatBubbleRenderer getRenderer(IChatBubbleRenderer.Position position);

    /**
     * 数据的序列化器，用于服务端向客户端同步数据
     */
    interface ChatSerializer {
        /**
         * 读取数据
         *
         * @param buf 数据包
         * @return 数据
         * @apiNote 往客户端同步的数据里，不需要同步 existTick 和 priority，这两个数据仅在服务端有效
         */
        IChatBubbleData readFromBuff(FriendlyByteBuf buf);

        /**
         * 写入数据
         *
         * @param buf  数据包
         * @param data 数据
         */
        void writeToBuff(FriendlyByteBuf buf, IChatBubbleData data);
    }
}
