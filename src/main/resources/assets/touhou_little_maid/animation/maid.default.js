// 用来和旧版本资源包保持兼容的残留文件
// 其他情况下不建议使用

var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        legLeft = modelMap.get("legLeft");
        legRight = modelMap.get("legRight");
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");
        wingLeft = modelMap.get("wingLeft");
        wingRight = modelMap.get("wingRight");
        blink = modelMap.get("blink");
        tail = modelMap.get("tail");
        sinFloat = modelMap.get("sinFloat");
        cosFloat = modelMap.get("cosFloat");
        negativeSinFloat = modelMap.get("-sinFloat");
        negativeCosFloat = modelMap.get("-cosFloat");
        negativeSinFloat2 = modelMap.get("_sinFloat");
        negativeCosFloat2 = modelMap.get("_cosFloat");
        helmet = modelMap.get("helmet");
        chestPlate = modelMap.get("chestPlate");
        chestPlateLeft = modelMap.get("chestPlateLeft");
        chestPlateMiddle = modelMap.get("chestPlateMiddle");
        chestPlateRight = modelMap.get("chestPlateRight");
        leggings = modelMap.get("leggings");
        leggingsLeft = modelMap.get("leggingsLeft");
        leggingsMiddle = modelMap.get("leggingsMiddle");
        leggingsRight = modelMap.get("leggingsRight");
        bootsLeft = modelMap.get("bootsLeft");
        bootsRight = modelMap.get("bootsRight");
        // 反向隐藏护甲
        reverseHelmet = modelMap.get("-helmet");
        reverseChestPlate = modelMap.get("-chestPlate");
        reverseChestPlateLeft = modelMap.get("-chestPlateLeft");
        reverseChestPlateMiddle = modelMap.get("-chestPlateMiddle");
        reverseChestPlateRight = modelMap.get("-chestPlateRight");
        reverseLeggings = modelMap.get("-leggings");
        reverseLeggingsLeft = modelMap.get("-leggingsLeft");
        reverseLeggingsMiddle = modelMap.get("-leggingsMiddle");
        reverseLeggingsRight = modelMap.get("-leggingsRight");
        reverseBootsLeft = modelMap.get("-bootsLeft");
        reverseBootsRight = modelMap.get("-bootsRight");
        reverseHelmet2 = modelMap.get("_helmet");
        reverseChestPlate2 = modelMap.get("_chestPlate");
        reverseChestPlateLeft2 = modelMap.get("_chestPlateLeft");
        reverseChestPlateMiddle2 = modelMap.get("_chestPlateMiddle");
        reverseChestPlateRight2 = modelMap.get("_chestPlateRight");
        reverseLeggings2 = modelMap.get("_leggings");
        reverseLeggingsLeft2 = modelMap.get("_leggingsLeft");
        reverseLeggingsMiddle2 = modelMap.get("_leggingsMiddle");
        reverseLeggingsRight2 = modelMap.get("_leggingsRight");
        reverseBootsLeft2 = modelMap.get("_bootsLeft");
        reverseBootsRight2 = modelMap.get("_bootsRight");
        ahoge = modelMap.get("ahoge");

        headAnimation(head, netHeadYaw, headPitch);
        legAnimation(legLeft, legRight, limbSwing, limbSwingAmount);
        armAnimation(maid, armLeft, armRight, limbSwing, limbSwingAmount, ageInTicks);
        wingAnimation(wingLeft, wingRight, ageInTicks);
        blinkAnimation(blink, ageInTicks);
        tailAnimation(tail, ageInTicks);
        floatAnimation(sinFloat, cosFloat, negativeSinFloat, negativeCosFloat, negativeSinFloat2, negativeCosFloat2, ageInTicks);
        renderArmor(maid, helmet, chestPlate, chestPlateLeft, chestPlateMiddle, chestPlateRight,
            leggings, leggingsLeft, leggingsMiddle, leggingsRight, bootsLeft, bootsRight);
        renderReverseArmor(maid, reverseHelmet, reverseChestPlate, reverseChestPlateLeft, reverseChestPlateMiddle, reverseChestPlateRight,
            reverseLeggings, reverseLeggingsLeft, reverseLeggingsMiddle, reverseLeggingsRight,
            reverseBootsLeft, reverseBootsRight);
        renderReverseArmor(maid, reverseHelmet2, reverseChestPlate2, reverseChestPlateLeft2, reverseChestPlateMiddle2, reverseChestPlateRight2,
            reverseLeggings2, reverseLeggingsLeft2, reverseLeggingsMiddle2, reverseLeggingsRight2,
            reverseBootsLeft2, reverseBootsRight2);
        beggingPosture(maid, head, ahoge, ageInTicks);
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
    }
    if (legRight != undefined) {
        legRight.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
        legRight.setRotateAngleZ(0);
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
            armLeft.setRotateAngleZ(Math.cos(ageInTicks * 0.05) * 0.05 - 0.4);
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
            armRight.setRotateAngleZ(-Math.cos(ageInTicks * 0.05) * 0.05 + 0.4);
            // 手部使用动画
            if (maid.getSwingProgress() > 0.0 && !maid.isSwingLeftHand()) {
                armRight.setRotateAngleX(armRight.getRotateAngleX() - (f2 * 1.2 + f3));
                armRight.setRotateAngleZ(armRight.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
            }
        }
    }
}

function wingAnimation(wingLeft, wingRight, ageInTicks) {
    if (wingLeft != undefined) {
        wingLeft.setRotateAngleY(-Math.cos(ageInTicks * 0.3) * 0.2 + 1.0);
    }
    if (wingRight != undefined) {
        wingRight.setRotateAngleY(Math.cos(ageInTicks * 0.3) * 0.2 - 1.0);
    }
}

function blinkAnimation(blink, ageInTicks) {
    if (blink != undefined) {
        remainder = ageInTicks % 60;
        blink.setHidden(!(55 < remainder && remainder < 60));
    }
}

function tailAnimation(tail, ageInTicks) {
    if (tail != undefined) {
        tail.setRotateAngleX(Math.sin(ageInTicks * 0.2) * 0.05);
        tail.setRotateAngleZ(Math.cos(ageInTicks * 0.2) * 0.1);
    }
}

function floatAnimation(sinFloat, cosFloat, negativeSinFloat, negativeCosFloat, negativeSinFloat2, negativeCosFloat2, ageInTicks) {
    if (sinFloat != undefined) {
        sinFloat.setOffsetY(Math.sin(ageInTicks * 0.1) * 0.05);
    }
    if (cosFloat != undefined) {
        cosFloat.setOffsetY(Math.cos(ageInTicks * 0.1) * 0.05);
    }
    if (negativeSinFloat != undefined) {
        negativeSinFloat.setOffsetY(-Math.sin(ageInTicks * 0.1) * 0.05);
    }
    if (negativeSinFloat2 != undefined) {
        negativeSinFloat2.setOffsetY(-Math.sin(ageInTicks * 0.1) * 0.05);
    }
    if (negativeCosFloat != undefined) {
        negativeCosFloat.setOffsetY(-Math.cos(ageInTicks * 0.1) * 0.05);
    }
    if (negativeCosFloat2 != undefined) {
        negativeCosFloat2.setOffsetY(-Math.cos(ageInTicks * 0.1) * 0.05);
    }
}

function renderArmor(maid, helmet, chestPlate, chestPlateLeft, chestPlateMiddle, chestPlateRight, leggings, leggingsLeft, leggingsMiddle, leggingsRight, bootsLeft, bootsRight) {
    if (helmet != undefined) {
        helmet.setHidden(!maid.hasHelmet());
    }
    if (chestPlate != undefined) {
        chestPlate.setHidden(!maid.hasChestPlate());
    }
    if (chestPlateLeft != undefined) {
        chestPlateLeft.setHidden(!maid.hasChestPlate());
    }
    if (chestPlateMiddle != undefined) {
        chestPlateMiddle.setHidden(!maid.hasChestPlate());
    }
    if (chestPlateRight != undefined) {
        chestPlateRight.setHidden(!maid.hasChestPlate());
    }
    if (leggings != undefined) {
        leggings.setHidden(!maid.hasLeggings());
    }
    if (leggingsLeft != undefined) {
        leggingsLeft.setHidden(!maid.hasLeggings());
    }
    if (leggingsMiddle != undefined) {
        leggingsMiddle.setHidden(!maid.hasLeggings());
    }
    if (leggingsRight != undefined) {
        leggingsRight.setHidden(!maid.hasLeggings());
    }
    if (bootsLeft != undefined) {
        bootsLeft.setHidden(!maid.hasBoots());
    }
    if (bootsRight != undefined) {
        bootsRight.setHidden(!maid.hasBoots());
    }
}

function renderReverseArmor(maid, reverseHelmet, reverseChestPlate, reverseChestPlateLeft, reverseChestPlateMiddle, reverseChestPlateRight,
                            reverseLeggings, reverseLeggingsLeft, reverseLeggingsMiddle, reverseLeggingsRight,
                            reverseBootsLeft, reverseBootsRight) {

    if (reverseHelmet != undefined) {
        reverseHelmet.setHidden(maid.hasHelmet());
    }
    if (reverseChestPlate != undefined) {
        reverseChestPlate.setHidden(maid.hasChestPlate());
    }
    if (reverseChestPlateLeft != undefined) {
        reverseChestPlateLeft.setHidden(maid.hasChestPlate());
    }
    if (reverseChestPlateMiddle != undefined) {
        reverseChestPlateMiddle.setHidden(maid.hasChestPlate());
    }
    if (reverseChestPlateRight != undefined) {
        reverseChestPlateRight.setHidden(maid.hasChestPlate());
    }
    if (reverseLeggings != undefined) {
        reverseLeggings.setHidden(maid.hasLeggings());
    }
    if (reverseLeggingsLeft != undefined) {
        reverseLeggingsLeft.setHidden(maid.hasLeggings());
    }
    if (reverseLeggingsMiddle != undefined) {
        reverseLeggingsMiddle.setHidden(maid.hasLeggings());
    }
    if (reverseLeggingsRight != undefined) {
        reverseLeggingsRight.setHidden(maid.hasLeggings());
    }
    if (reverseBootsLeft != undefined) {
        reverseBootsLeft.setHidden(maid.hasBoots());
    }
    if (reverseBootsRight != undefined) {
        reverseBootsRight.setHidden(maid.hasBoots());
    }
}

function beggingPosture(maid, head, ahoge, ageInTicks) {
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

function ridingPosture(legLeft, legRight) {
    if (legLeft != undefined) {
        legLeft.setRotateAngleX(-0.960);
        legLeft.setRotateAngleZ(-0.523);
    }
    if (legRight != undefined) {
        legRight.setRotateAngleX(-0.960);
        legRight.setRotateAngleZ(0.523);
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
    GlWrapper.translate(0, -0.4, -0.3);
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