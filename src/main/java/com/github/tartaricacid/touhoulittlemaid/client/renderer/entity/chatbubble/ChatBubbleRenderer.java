package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleDataCollection;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.GuiTools;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.ClientHooks;

public class ChatBubbleRenderer {
    private final EntityMaidRenderer renderer;

    public ChatBubbleRenderer(EntityMaidRenderer renderer) {
        this.renderer = renderer;
    }

    @SuppressWarnings("all")
    public void render(EntityGraphics graphics) {
        EntityMaid maid = graphics.getMaid();
        double distance = renderer.getDispatcher().distanceToSqr(maid);
        if (!ClientHooks.isNameplateInRenderDistance(maid, distance)) {
            return;
        }

        ChatBubbleDataCollection chatBubble = maid.getChatBubbleManager().getChatBubbleDataCollection();
        if (chatBubble == null || chatBubble.isEmpty()) {
            return;
        }

        int size = chatBubble.size();
        boolean isOdd = isOdd(size);
        int sideNum = isOdd ? size - 1 : size;
        int marginY = 16;
        int leftY = 0;
        int rightY = 0;

        // 其他的在两边
        ObjectIterator<IChatBubbleData> iterator = chatBubble.iterator();
        for (int i = 0; i < sideNum; i++) {
            if (!iterator.hasNext()) {
                break;
            }
            if (isOdd(i)) {
                IChatBubbleRenderer bubble = iterator.next().getRenderer(IChatBubbleRenderer.Position.LEFT);
                renderChatBubble(graphics, bubble, IChatBubbleRenderer.Position.LEFT, leftY);
                leftY += bubble.getHeight() + marginY;
            } else {
                IChatBubbleRenderer bubble = iterator.next().getRenderer(IChatBubbleRenderer.Position.RIGHT);
                renderChatBubble(graphics, bubble, IChatBubbleRenderer.Position.RIGHT, rightY);
                rightY += bubble.getHeight() + marginY;
            }
        }

        // 奇数位的在中间
        if (isOdd) {
            int middleY = Math.max(leftY, rightY);
            IChatBubbleData last = chatBubble.getLast();
            IChatBubbleRenderer bubble = last.getRenderer(IChatBubbleRenderer.Position.CENTER);
            renderChatBubble(graphics, bubble, IChatBubbleRenderer.Position.CENTER, middleY);
        }
    }

    private void renderChatBubble(EntityGraphics graphics, IChatBubbleRenderer chatBubble, IChatBubbleRenderer.Position position, int y) {
        int offset = 5;
        int marginX = 1;

        int width = chatBubble.getWidth();
        int height = chatBubble.getHeight();
        ResourceLocation texture = chatBubble.getBackgroundTexture();
        int bgWidth = width + 2 * offset;
        int bgHeight = height + 2 * offset;

        graphics.pose().pushPose();
        graphics.pose().translate(0, -y, 0);
        RenderSystem.enableDepthTest();

        if (position == IChatBubbleRenderer.Position.LEFT) {
            GuiTools.blitNineSliced(graphics, texture, -marginX - bgWidth, -bgHeight, bgWidth, bgHeight, 8, 8, 48, 24, 0, 0);
            graphics.pose().translate(0, 0, -0.01);
            graphics.blit(texture, -marginX - 8, -8, 32, 24, 16, 16);
            graphics.pose().translate(-marginX - bgWidth + offset, -bgHeight + offset, -0.01);
            chatBubble.render(renderer, graphics);
        } else if (position == IChatBubbleRenderer.Position.RIGHT) {
            GuiTools.blitNineSliced(graphics, texture, marginX, -bgHeight, bgWidth, bgHeight, 8, 8, 48, 24, 0, 0);
            graphics.pose().translate(0, 0, -0.01);
            graphics.blit(texture, marginX - 8, -8, 0, 24, 16, 16);
            graphics.pose().translate(offset + marginX, -bgHeight + offset, -0.01);
            chatBubble.render(renderer, graphics);
        } else if (position == IChatBubbleRenderer.Position.CENTER) {
            GuiTools.blitNineSliced(graphics, texture, -bgWidth / 2, -bgHeight, bgWidth, bgHeight, 8, 8, 48, 24, 0, 0);
            graphics.pose().translate(0, 0, -0.01);
            graphics.blit(texture, -8, -8, 16, 24, 16, 16);
            graphics.pose().translate(-bgWidth / 2d + offset, -bgHeight + offset, -0.01);
            chatBubble.render(renderer, graphics);
        }

        graphics.pose().popPose();
    }

    private boolean isOdd(int number) {
        return number % 2 != 0;
    }
}
