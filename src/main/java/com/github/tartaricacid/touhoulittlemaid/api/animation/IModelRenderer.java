package com.github.tartaricacid.touhoulittlemaid.api.animation;

public interface IModelRenderer {
    /**
     * Get ModelRenderer's x rotate angle
     *
     * @return float
     */
    float getRotateAngleX();

    /**
     * Set ModelRenderer's x rotate angle
     *
     * @param xRot x rotate angle
     */
    void setRotateAngleX(float xRot);

    /**
     * Get ModelRenderer's initialization x rotate angle
     *
     * @return float
     */
    float getInitRotateAngleX();

    /**
     * Get ModelRenderer's y rotate angle
     *
     * @return float
     */
    float getRotateAngleY();

    /**
     * Set ModelRenderer's y rotate angle
     *
     * @param yRot y rotate angle
     */
    void setRotateAngleY(float yRot);

    /**
     * Get ModelRenderer's initialization y rotate angle
     *
     * @return float
     */
    float getInitRotateAngleY();

    /**
     * Get ModelRenderer's z rotate angle
     *
     * @return float
     */
    float getRotateAngleZ();

    /**
     * Set ModelRenderer's z rotate angle
     *
     * @param zRot z rotate angle
     */
    void setRotateAngleZ(float zRot);

    /**
     * Get ModelRenderer's initialization z rotate angle
     *
     * @return float
     */
    float getInitRotateAngleZ();

    /**
     * Get ModelRenderer's x offset
     *
     * @return float
     */
    float getOffsetX();

    /**
     * Set ModelRenderer's x offset
     *
     * @param offsetX x offset
     */
    void setOffsetX(float offsetX);

    /**
     * Get ModelRenderer's y offset
     *
     * @return float
     */
    float getOffsetY();

    /**
     * Set ModelRenderer's y offset
     *
     * @param offsetY y offset
     */
    void setOffsetY(float offsetY);

    /**
     * Get ModelRenderer's z offset
     *
     * @return float
     */
    float getOffsetZ();

    /**
     * Set ModelRenderer's z offset
     *
     * @param offsetZ z offset
     */
    void setOffsetZ(float offsetZ);

    /**
     * Get ModelRenderer's x rotation point
     *
     * @return float
     */
    float getRotationPointX();

    /**
     * Get ModelRenderer's y rotation point
     *
     * @return float
     */
    float getRotationPointY();

    /**
     * Get ModelRenderer's z rotation point
     *
     * @return float
     */
    float getRotationPointZ();

    /**
     * Is ModelRenderer' hidden
     *
     * @return boolean
     */
    boolean isHidden();

    /**
     * Set ModelRenderer hidden
     *
     * @param hidden boolean
     */
    void setHidden(boolean hidden);
}
