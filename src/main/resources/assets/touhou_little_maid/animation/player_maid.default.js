var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        legLeft = modelMap.get("legLeft");
        legRight = modelMap.get("legRight");
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");

        headAnimation(head, netHeadYaw, headPitch);
        legAnimation(legLeft, legRight, limbSwing, limbSwingAmount);
        armAnimation(maid, armLeft, armRight, limbSwing, limbSwingAmount, ageInTicks);
        beggingPosture(maid, head, ageInTicks);
        swingingArmsPosture(maid, armLeft, armRight);

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

function headAnimation(head, netHeadYaw, headPitch) {
    if (head != undefined) {
        head.setRotateAngleX(headPitch * 0.017453292);
        head.setRotateAngleY(netHeadYaw * 0.017453292);
    }
}

function legAnimation(legLeft, legRight, limbSwing, limbSwingAmount) {
    if (legLeft != undefined) {
        legLeft.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
        legLeft.setRotateAngleZ(0);
        legLeft.setRotateAngleY(0);
    }
    if (legRight != undefined) {
        legRight.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
        legRight.setRotateAngleZ(0);
        legRight.setRotateAngleY(0);
    }
}

function armAnimation(maid, armLeft, armRight, limbSwing, limbSwingAmount, ageInTicks) {
    f1 = 1.0 - Math.pow(1.0 - maid.getSwingProgress(), 4);
    f2 = Math.sin(f1 * Math.PI);
    f3 = Math.sin(maid.getSwingProgress() * Math.PI) * -0.7 * 0.75;

    if (armLeft != undefined) {
        if (maid.isHoldTrolley()) {
            armLeft.setRotateAngleX(0.5);
            armLeft.setRotateAngleY(0);
            armLeft.setRotateAngleZ(-0.395);
        } else if (maid.isHoldVehicle()) {
            rotation = maid.getLeftHandRotation();
            armLeft.setRotateAngleX(rotation[0]);
            armLeft.setRotateAngleY(rotation[1]);
            armLeft.setRotateAngleZ(rotation[2]);
        } else {
            armLeft.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
            armLeft.setRotateAngleY(0);
            armLeft.setRotateAngleZ(Math.cos(ageInTicks * 0.05) * 0.025 - 0.05);
            // 手部使用动画
            if (maid.getSwingProgress() > 0.0 && maid.isSwingLeftHand()) {
                armLeft.setRotateAngleX(armLeft.getRotateAngleX() - (f2 * 1.2 + f3));
                armLeft.setRotateAngleZ(armLeft.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
            }
        }
    }

    if (armRight != undefined) {
        if (maid.isHoldVehicle()) {
            rotation = maid.getRightHandRotation();
            armRight.setRotateAngleX(rotation[0]);
            armRight.setRotateAngleY(rotation[1]);
            armRight.setRotateAngleZ(rotation[2]);
        } else {
            armRight.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
            armRight.setRotateAngleY(0);
            armRight.setRotateAngleZ(-Math.cos(ageInTicks * 0.05) * 0.025 + 0.05);
            // 手部使用动画
            if (maid.getSwingProgress() > 0.0 && !maid.isSwingLeftHand()) {
                armRight.setRotateAngleX(armRight.getRotateAngleX() - (f2 * 1.2 + f3));
                armRight.setRotateAngleZ(armRight.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
            }
        }
    }
}

function beggingPosture(maid, head, ageInTicks) {
    if (maid.isBegging()) {
        if (head != undefined) {
            head.setRotateAngleZ(0.139);
        }
    } else {
        if (head != undefined) {
            head.setRotateAngleZ(0);
        }
    }
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

function swingingArmsPosture(maid, armLeft, armRight) {
    if (maid.isSwingingArms()) {
        if (armLeft != undefined) {
            armLeft.setRotateAngleX(-1.396);
            armLeft.setRotateAngleY(0.785);
        }

        if (armRight != undefined) {
            armRight.setRotateAngleX(-1.396);
            armRight.setRotateAngleY(-0.174);
        }
    }
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
