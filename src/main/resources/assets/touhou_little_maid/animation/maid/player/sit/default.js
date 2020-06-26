var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        legLeft = modelMap.get("legLeft");
        legRight = modelMap.get("legRight");
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");

        // 头部复位
        if (head != undefined) {
            head.setOffsetY(0);
        }

        if (maid.isRidingMarisaBroom()) {
            // 坐在扫帚上时，应用待命的动作
            ridingBroomPosture(head, armLeft, armRight, legLeft, legRight);
        } else if (maid.isRiding()) {
            ridingPosture(legLeft, legRight);
        } else if (maid.isSitting()) {
            sittingPosture(armLeft, armRight, legLeft, legRight);
        }
    }
})

function ridingPosture(legLeft, legRight) {
    if (legLeft != undefined) {
        legLeft.setRotateAngleX(-1.4);
        legLeft.setRotateAngleY(-0.4);
    }

    if (legRight != undefined) {
        legRight.setRotateAngleX(-1.4);
        legRight.setRotateAngleY(0.4);
    }

    GlWrapper.translate(0, 0.3, 0);
}

function sittingPosture(armLeft, armRight, legLeft, legRight) {
    if (armLeft != undefined) {
        armLeft.setRotateAngleX(-0.798);
        armLeft.setRotateAngleZ(0.274);
    }

    if (armRight != undefined) {
        armRight.setRotateAngleX(-0.798);
        armRight.setRotateAngleZ(-0.274);
    }

    ridingPosture(legLeft, legRight);
}


function ridingBroomPosture(head, armLeft, armRight, legLeft, legRight) {
    sittingPosture(armLeft, armRight, legLeft, legRight);
    if (head != undefined) {
        head.setRotateAngleX(head.getRotateAngleX() - 30 * Math.PI / 180);
        head.setOffsetY(0.0625);
    }

    GlWrapper.rotate(30, 1, 0, 0);
    GlWrapper.translate(0, -0.45, -0.3);
}