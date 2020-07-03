var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        legLeft = modelMap.get("legLeft");
        legRight = modelMap.get("legRight");

        isFarm = maid.getTask() === "farm" && maid.getSwingProgress() > 0.0

        if (isFarm) {
            GlWrapper.translate(0, 0.0713625, -0.35876875);
            GlWrapper.rotate(22.5, 1, 0, 0);
        }

        if (legLeft != undefined) {
            leftRad = Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount;
            if (isFarm) {
                leftRad -= 0.3927;
            }
            legLeft.setRotateAngleX(leftRad);
            legLeft.setRotateAngleY(0);
            legLeft.setRotateAngleZ(0);
        }
        if (legRight != undefined) {
            rightRad = -Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount;
            if (isFarm) {
                rightRad -= 0.3927;
            }
            legRight.setRotateAngleX(rightRad);
            legRight.setRotateAngleY(0);
            legRight.setRotateAngleZ(0);
        }
    }
})