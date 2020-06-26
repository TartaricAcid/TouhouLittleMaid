Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        ahoge = modelMap.get("ahoge");

        if (maid.isBegging()) {
            if (head != undefined) {
                head.setRotateAngleZ(0.139);
            }
            if (ahoge != undefined) {
                ahoge.setRotateAngleX(Math.cos(ageInTicks * 1.0) * 0.05);
                ahoge.setRotateAngleZ(Math.sin(ageInTicks * 1.0) * 0.05);
            }
        } else {
            if (head != undefined) {
                head.setRotateAngleZ(0);
            }
            if (ahoge != undefined) {
                ahoge.setRotateAngleZ(0);
            }
        }
    }
})