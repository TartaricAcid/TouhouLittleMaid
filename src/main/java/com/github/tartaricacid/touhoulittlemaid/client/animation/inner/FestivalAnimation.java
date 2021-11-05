package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.ULocale;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Calendar;
import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class FestivalAnimation {
    private static final ULocale CHINESE = new ULocale("@calendar=chinese");

    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/festival/new_year.js"), getNewYear());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/festival/christmas.js"), getChristmas());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/festival/spring_festival.js"), getSpringFestival());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/festival/duanwu.js"), getDuanwu());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/base/festival/mid_autumn.js"), getMidAutumn());
    }

    public static IAnimation<LivingEntity> getNewYear() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper newYearShow = modelMap.get("newYearShow");
                ModelRendererWrapper newYearHidden = modelMap.get("newYearHidden");

                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
                boolean isNewYear = month == Calendar.JANUARY && dayInMonth == 1;
                if (newYearShow != null) {
                    newYearShow.setHidden(!isNewYear);
                }
                if (newYearHidden != null) {
                    newYearHidden.setHidden(isNewYear);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getChristmas() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper christmasShow = modelMap.get("christmasShow");
                ModelRendererWrapper christmasHidden = modelMap.get("christmasHidden");

                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
                boolean isChristmas = month == Calendar.DECEMBER && (dayInMonth == 24 || dayInMonth == 25);
                if (christmasShow != null) {
                    christmasShow.setHidden(!isChristmas);
                }
                if (christmasHidden != null) {
                    christmasHidden.setHidden(isChristmas);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getSpringFestival() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper springFestivalShow = modelMap.get("springFestivalShow");
                ModelRendererWrapper springFestivalHidden = modelMap.get("springFestivalHidden");

                com.ibm.icu.util.Calendar calendar = ChineseCalendar.getInstance(CHINESE);
                int month = calendar.get(ChineseCalendar.MONTH);
                int dayInMonth = calendar.get(ChineseCalendar.DAY_OF_MONTH);
                // 官方的春节假期是农历年三十到初六
                // 但是有的腊月没有三十……所以我们从廿九算起
                boolean isSpringFestival = (month == ChineseCalendar.DECEMBER && dayInMonth >= 29) || (month == ChineseCalendar.JANUARY && dayInMonth <= 6);
                if (springFestivalShow != null) {
                    springFestivalShow.setHidden(!isSpringFestival);
                }
                if (springFestivalHidden != null) {
                    springFestivalHidden.setHidden(isSpringFestival);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getDuanwu() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper duanwuShow = modelMap.get("duanwuShow");
                ModelRendererWrapper duanwuHidden = modelMap.get("duanwuHidden");

                com.ibm.icu.util.Calendar calendar = ChineseCalendar.getInstance(CHINESE);
                int month = calendar.get(ChineseCalendar.MONTH);
                int dayInMonth = calendar.get(ChineseCalendar.DAY_OF_MONTH);
                boolean isDuanwu = month == ChineseCalendar.MAY && dayInMonth == 5;
                if (duanwuShow != null) {
                    duanwuShow.setHidden(!isDuanwu);
                }
                if (duanwuHidden != null) {
                    duanwuHidden.setHidden(isDuanwu);
                }
            }
        };
    }

    public static IAnimation<LivingEntity> getMidAutumn() {
        return new IAnimation<LivingEntity>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netLeadYaw, float headPitch, float scaleFactor, LivingEntity entity, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper midAutumnShow = modelMap.get("midAutumnShow");
                ModelRendererWrapper midAutumnHidden = modelMap.get("midAutumnHidden");

                com.ibm.icu.util.Calendar calendar = ChineseCalendar.getInstance(CHINESE);
                int month = calendar.get(ChineseCalendar.MONTH);
                int dayInMonth = calendar.get(ChineseCalendar.DAY_OF_MONTH);
                boolean isMidAutumn = month == ChineseCalendar.AUGUST && dayInMonth == 15;
                if (midAutumnShow != null) {
                    midAutumnShow.setHidden(!isMidAutumn);
                }
                if (midAutumnHidden != null) {
                    midAutumnHidden.setHidden(isMidAutumn);
                }
            }
        };
    }
}
