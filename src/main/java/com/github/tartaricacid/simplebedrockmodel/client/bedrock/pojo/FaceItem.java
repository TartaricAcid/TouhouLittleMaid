package com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo;

import com.google.gson.annotations.SerializedName;

public class FaceItem {
    @SerializedName("uv")
    private float[] uv;

    @SerializedName("uv_size")
    private float[] uvSize;

    @SerializedName("uv_rotation")
    private int uvRotation = 0;

    public FaceItem(float[] uv, float[] uvSize) {
        this.uv = uv;
        this.uvSize = uvSize;
    }

    public float[] getUv() {
        return uv;
    }

    public float[] getUvSize() {
        return uvSize;
    }

    /**
     * 获取旋转后的四个顶点的 UV 坐标
     * 渲染顶点顺序: 右上(0) -> 左上(1) -> 左下(2) -> 右下(3)
     * 返回: [右上U, 右上V, 左上U, 左上V, 左下U, 左下V, 右下U, 右下V]
     */
    public float[] getRotatedUVs(float texWidth, float texHeight) {
        float u1 = uv[0] / texWidth;
        float v1 = uv[1] / texHeight;
        float u2 = (uv[0] + uvSize[0]) / texWidth;
        float v2 = (uv[1] + uvSize[1]) / texHeight;

        // 原始 UV 矩形的四个角 (按顺时针: 左上, 右上, 右下, 左下)
        // 左上(u1,v1), 右上(u2,v1), 右下(u2,v2), 左下(u1,v2)

        // 基岩版的 uv_rotation 是顺时针旋转 UV 贴图
        // 旋转后需要重新映射哪个 UV 角对应哪个顶点
        return switch (uvRotation) {
            case 90 ->
                // 顺时针 90°: 原左上->右上, 原右上->右下, 原右下->左下, 原左下->左上
                // 顶点: 右上(原左上), 左上(原左下), 左下(原右下), 右下(原右上)
                    new float[]{u1, v1, u1, v2, u2, v2, u2, v1};
            case 180 ->
                // 顺时针 180°: 原左上->右下, 原右上->左下, 原右下->左上, 原左下->右上
                // 顶点: 右上(原左下), 左上(原右下), 左下(原右上), 右下(原左上)
                    new float[]{u1, v2, u2, v2, u2, v1, u1, v1};
            case 270 ->
                // 顺时针 270°: 原左上->左下, 原右上->左上, 原右下->右上, 原左下->右下
                // 顶点: 右上(原右下), 左上(原右上), 左下(原左上), 右下(原左下)
                    new float[]{u2, v2, u2, v1, u1, v1, u1, v2};
            default ->
                // 0° 无旋转
                // 顶点: 右上(u2,v1), 左上(u1,v1), 左下(u1,v2), 右下(u2,v2)
                    new float[]{u2, v1, u1, v1, u1, v2, u2, v2};
        };
    }
}
