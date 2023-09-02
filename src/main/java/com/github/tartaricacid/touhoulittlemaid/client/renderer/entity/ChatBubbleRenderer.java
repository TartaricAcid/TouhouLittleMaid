package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.SizeTexture;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatText;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.MaidChatBubbles;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.function.Consumer;

public class ChatBubbleRenderer {
    private static final List<Pair<Long, ChatText>> TMP_CHAT_BUBBLES = Lists.newArrayList();
    private static final RenderState.AlphaState DEFAULT_ALPHA = new RenderState.AlphaState(0.003921569F);
    private static final String LEFT_ARROW = "left_arrow";
    private static final String MIDDLE_ARROW = "middle_arrow";
    private static final String RIGHT_ARROW = "right_arrow";

    public static void renderChatBubble(EntityMaidRenderer renderer, EntityMaid maid, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        double distance = renderer.getDispatcher().distanceToSqr(maid);
        if (ForgeHooksClient.isNameplateInRenderDistance(maid, distance)) {
            getTmpChatBubbles(maid);
            FontRenderer font = renderer.getFont();

            if (TMP_CHAT_BUBBLES.size() == 1) {
                Pair<Long, ChatText> chatBubble = TMP_CHAT_BUBBLES.get(0);
                ChatText chatText = chatBubble.getRight();
                RenderData data = new RenderData(renderer, maid, matrixStack, buffer, MIDDLE_ARROW);
                if (chatText.isText()) {
                    ITextComponent parseText = ParseI18n.parse(chatText.getText());
                    int width = font.width(parseText);
                    renderChatBubbleBody(0, -32, width, data, d -> renderText(parseText, 0, -32, width, packedLight, d));
                }
                if (chatText.isIcon()) {
                    renderChatBubbleBody(0, -32, 20, data, d -> renderIcon(0, -32, chatText.getIconPath(), d));
                }
                return;
            }

            if (TMP_CHAT_BUBBLES.size() == 2) {
                Pair<Long, ChatText> chatBubble1 = TMP_CHAT_BUBBLES.get(0);
                Pair<Long, ChatText> chatBubble2 = TMP_CHAT_BUBBLES.get(1);
                ChatText chatText1 = chatBubble1.getRight();
                ChatText chatText2 = chatBubble2.getRight();
                RenderData data1 = new RenderData(renderer, maid, matrixStack, buffer, RIGHT_ARROW);
                RenderData data2 = new RenderData(renderer, maid, matrixStack, buffer, LEFT_ARROW);
                if (chatText1.isText()) {
                    ITextComponent parseText1 = ParseI18n.parse(chatText1.getText());
                    int width = font.width(parseText1);
                    int fullWidth = ((width / 20) + 2) * 20;
                    int startX = -fullWidth / 2 - 5;
                    renderChatBubbleBody(startX, -32, width, data1, d -> renderText(parseText1, startX, -32, width, packedLight, d));
                }
                if (chatText1.isIcon()) {
                    int fullWidth = 60;
                    int startX = -fullWidth / 2 - 5;
                    renderChatBubbleBody(startX, -32, 20, data1, d -> renderIcon(startX, -32, chatText1.getIconPath(), d));
                }

                if (chatText2.isText()) {
                    ITextComponent parseText2 = ParseI18n.parse(chatText2.getText());
                    int width = font.width(parseText2);
                    int fullWidth = ((width / 20) + 2) * 20;
                    int startX = fullWidth / 2 + 5;
                    renderChatBubbleBody(startX, -32, width, data2, d -> renderText(parseText2, startX, -32, width, packedLight, d));
                }
                if (chatText2.isIcon()) {
                    int fullWidth = 60;
                    int startX = fullWidth / 2 + 5;
                    renderChatBubbleBody(startX, -32, 20, data2, d -> renderIcon(startX, -32, chatText2.getIconPath(), d));
                }
                return;
            }

            if (TMP_CHAT_BUBBLES.size() == 3) {
                Pair<Long, ChatText> chatBubble1 = TMP_CHAT_BUBBLES.get(0);
                Pair<Long, ChatText> chatBubble2 = TMP_CHAT_BUBBLES.get(1);
                Pair<Long, ChatText> chatBubble3 = TMP_CHAT_BUBBLES.get(2);
                ChatText chatText1 = chatBubble1.getRight();
                ChatText chatText2 = chatBubble2.getRight();
                ChatText chatText3 = chatBubble3.getRight();
                RenderData data1 = new RenderData(renderer, maid, matrixStack, buffer, RIGHT_ARROW);
                RenderData data2 = new RenderData(renderer, maid, matrixStack, buffer, LEFT_ARROW);
                RenderData data3 = new RenderData(renderer, maid, matrixStack, buffer, MIDDLE_ARROW);
                if (chatText1.isText()) {
                    ITextComponent parseText1 = ParseI18n.parse(chatText1.getText());
                    int width = font.width(parseText1);
                    int fullWidth = ((width / 20) + 2) * 20;
                    int startX = -fullWidth / 2 - 5;
                    renderChatBubbleBody(startX, -32, width, data1, d -> renderText(parseText1, startX, -32, width, packedLight, d));
                }
                if (chatText1.isIcon()) {
                    int fullWidth = 60;
                    int startX = -fullWidth / 2 - 5;
                    renderChatBubbleBody(startX, -32, 20, data1, d -> renderIcon(startX, -32, chatText1.getIconPath(), d));
                }

                if (chatText2.isText()) {
                    ITextComponent parseText2 = ParseI18n.parse(chatText2.getText());
                    int width = font.width(parseText2);
                    int fullWidth = ((width / 20) + 2) * 20;
                    int startX = fullWidth / 2 + 5;
                    renderChatBubbleBody(startX, -32, width, data2, d -> renderText(parseText2, startX, -32, width, packedLight, d));
                }
                if (chatText2.isIcon()) {
                    int fullWidth = 60;
                    int startX = fullWidth / 2 + 5;
                    renderChatBubbleBody(startX, -32, 20, data2, d -> renderIcon(startX, -32, chatText2.getIconPath(), d));
                }

                if (chatText3.isText()) {
                    ITextComponent parseText3 = ParseI18n.parse(chatText3.getText());
                    int width = font.width(parseText3);
                    renderChatBubbleBody(0, -62, width, data3, d -> renderText(parseText3, 0, -62, width, packedLight, d));
                }
                if (chatText3.isIcon()) {
                    renderChatBubbleBody(0, -62, 20, data3, d -> renderIcon(0, -62, chatText3.getIconPath(), d));
                }
            }
        }
    }

    private static void renderText(ITextComponent chatText, int startX, int startY, int width, int packedLight, RenderData data) {
        FontRenderer font = data.renderer.getFont();
        font.drawInBatch(chatText, -width / 2.0F + startX, startY + 6, 0xff000000,
                false, data.matrixStack.last().pose(), data.buffer, false, 0, packedLight);
    }

    private static void renderIcon(int startX, int startY, ResourceLocation iconPath, RenderData data) {
        IVertexBuilder iconVertexBuilder = data.buffer.getBuffer(chatBubbleRender(iconPath));
        Texture texture = Minecraft.getInstance().textureManager.getTexture(iconPath);
        if (texture instanceof SizeTexture) {
            SizeTexture sizeTexture = (SizeTexture) texture;
            int textureHeight = sizeTexture.getHeight();
            int textureWidth = sizeTexture.getWidth();
            int count = textureHeight / (textureWidth / 3);
            if (count <= 1) {
                drawIcon(data.matrixStack, iconVertexBuilder, startX - 30, startY, 0, 0, 1);
            } else {
                int iconDelay = (data.maid.tickCount / 2) % count;
                float vStart = (1.0f / count) * iconDelay;
                float vEnd = (1.0f / count) * (iconDelay + 1);
                drawIcon(data.matrixStack, iconVertexBuilder, startX - 30, startY, 0, vStart, vEnd);
            }
        }
    }

    private static float getChatBubbleStartHeight(EntityMaidRenderer renderer, EntityMaid maid) {
        float height = maid.getBbHeight() + 0.75F;
        if (renderer.getModel().hasHead()) {
            height = (24 - renderer.getModel().getHead().y) * 0.0625F + 0.75F;
        }
        return height;
    }

    private static void getTmpChatBubbles(EntityMaid maid) {
        MaidChatBubbles chatBubble = maid.getChatBubble();
        Pair<Long, ChatText> bubble1 = chatBubble.getBubble1();
        Pair<Long, ChatText> bubble2 = chatBubble.getBubble2();
        Pair<Long, ChatText> bubble3 = chatBubble.getBubble3();
        TMP_CHAT_BUBBLES.clear();
        if (bubble1 != MaidChatBubbles.EMPTY) {
            TMP_CHAT_BUBBLES.add(bubble1);
        }
        if (bubble2 != MaidChatBubbles.EMPTY) {
            TMP_CHAT_BUBBLES.add(bubble2);
        }
        if (bubble3 != MaidChatBubbles.EMPTY) {
            TMP_CHAT_BUBBLES.add(bubble3);
        }
    }

    private static void renderChatBubbleBody(int startX, int startY, int stringWidth, RenderData data, Consumer<RenderData> consumer) {
        ResourceLocation bg = data.renderer.getMainInfo().getChatBubble().getBg();
        float height = getChatBubbleStartHeight(data.renderer, data.maid);

        int count = stringWidth / 20;
        int fullWidth = (count + 2) * 20;
        int leftStartX = -fullWidth / 2 + startX;
        int rightStartX = fullWidth / 2 - 20 + startX;
        int middleStartX = -fullWidth / 2 + 20 + startX;

        data.matrixStack.pushPose();
        data.matrixStack.translate(0, height, 0);
        data.matrixStack.mulPose(data.renderer.getDispatcher().cameraOrientation());
        data.matrixStack.scale(-0.025F, -0.025F, 0.025F);

        IVertexBuilder vertexBuilder = data.buffer.getBuffer(chatBubbleRender(bg));
        drawBg(data.matrixStack, vertexBuilder, leftStartX, startY, 0.2f, 0, 0);
        for (int i = 0; i < count; i++) {
            drawBg(data.matrixStack, vertexBuilder, middleStartX, startY, 0.2f, 1, 0);
            middleStartX = middleStartX + 20;
        }
        drawBg(data.matrixStack, vertexBuilder, rightStartX, startY, 0.2f, 2, 0);
        if (LEFT_ARROW.equals(data.arrow)) {
            drawBg(data.matrixStack, vertexBuilder, leftStartX - 10, startY + 10, 0.19f, 0, 1);
        }
        if (MIDDLE_ARROW.equals(data.arrow)) {
            drawBg(data.matrixStack, vertexBuilder, startX - 10, startY + 10, 0.19f, 1, 1);
        }
        if (RIGHT_ARROW.equals(data.arrow)) {
            drawBg(data.matrixStack, vertexBuilder, rightStartX + 10, startY + 10, 0.19f, 2, 1);
        }

        consumer.accept(data);
        data.matrixStack.popPose();
    }

    public static void drawBg(MatrixStack matrixStack, IVertexBuilder builder, int x, int y, float z, int uIndex, int vIndex) {
        float height = 20;
        float width = 20;
        Matrix4f matrix4f = matrixStack.last().pose();
        float u0 = uIndex / 3.0f;
        float u1 = (uIndex + 1) / 3.0f;
        float v0 = vIndex / 2.0f;
        float v1 = (vIndex + 1) / 2.0f;

        vertex(matrix4f, builder, x, y + height, z, u0, v1);
        vertex(matrix4f, builder, x + width, y + height, z, u1, v1);
        vertex(matrix4f, builder, x + width, y, z, u1, v0);
        vertex(matrix4f, builder, x, y, z, u0, v0);
    }

    public static void drawIcon(MatrixStack matrixStack, IVertexBuilder builder, int x, int y, float z, float vStart, float vEnd) {
        float height = 20;
        float width = 60;
        Matrix4f matrix4f = matrixStack.last().pose();

        vertex(matrix4f, builder, x, y + height, z, 0, vEnd);
        vertex(matrix4f, builder, x + width, y + height, z, 1, vEnd);
        vertex(matrix4f, builder, x + width, y, z, 1, vStart);
        vertex(matrix4f, builder, x, y, z, 0, vStart);
    }

    private static void vertex(Matrix4f matrix4f, IVertexBuilder builder, float x, float y, float z, float u, float v) {
        builder.vertex(matrix4f, x, y, z).uv(u, v).endVertex();
    }

    private static RenderType chatBubbleRender(ResourceLocation locationIn) {
        RenderType.State compositeState = RenderType.State.builder()
                .setTextureState(new RenderState.TextureState(locationIn, false, false))
                .setAlphaState(DEFAULT_ALPHA)
                .createCompositeState(true);
        return RenderType.create("chat_bubble", DefaultVertexFormats.POSITION_TEX,
                GL11.GL_QUADS, 0xFF, true, false, compositeState);
    }

    private static class RenderData {
        private final EntityMaidRenderer renderer;
        private final EntityMaid maid;
        private final MatrixStack matrixStack;
        private final IRenderTypeBuffer buffer;
        private final String arrow;

        public RenderData(EntityMaidRenderer renderer, EntityMaid maid, MatrixStack matrixStack, IRenderTypeBuffer buffer, String arrow) {
            this.renderer = renderer;
            this.maid = maid;
            this.matrixStack = matrixStack;
            this.buffer = buffer;
            this.arrow = arrow;
        }
    }
}
