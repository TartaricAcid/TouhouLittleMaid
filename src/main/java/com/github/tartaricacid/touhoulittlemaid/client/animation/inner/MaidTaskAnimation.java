package com.github.tartaricacid.touhoulittlemaid.client.animation.inner;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

import static com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation.INNER_ANIMATION;

public final class MaidTaskAnimation {
    public static void init() {
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/attack.js"), getTaskAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/danmaku_attack.js"), getTaskDanmakuAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/farm.js"), getTaskFarm());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/feed_animal.js"), getTaskFeedAnimal());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/idle.js"), getTaskIdle());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/milk.js"), getTaskMilk());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/shears.js"), getTaskShears());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/sugar_cane.js"), getTaskSugarCane());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/cocoa.js"), getTaskCocoa());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/extinguishing.js"), getTaskExtinguishing());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/feed.js"), getTaskFeed());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/grass.js"), getTaskGrass());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/melon.js"), getTaskMelon());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/ranged_attack.js"), getTaskRangedAttack());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/snow.js"), getTaskSnow());
        INNER_ANIMATION.put(new ResourceLocation("touhou_little_maid:animation/maid/default/task/torch.js"), getTaskTorch());
    }

    public static IAnimation<EntityMaid> getTaskAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper attackHidden = modelMap.get("attackHidden");

                if (attackHidden != null) {
                    attackHidden.setHidden("attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper attackShow = modelMap.get("attackShow");
                if (attackShow != null) {
                    attackShow.setHidden(!"attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskDanmakuAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper danmakuAttackHidden = modelMap.get("danmakuAttackHidden");

                if (danmakuAttackHidden != null) {
                    danmakuAttackHidden.setHidden("danmaku_attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper danmakuAttackShow = modelMap.get("danmakuAttackShow");
                if (danmakuAttackShow != null) {
                    danmakuAttackShow.setHidden(!"danmaku_attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFarm() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper farmHidden = modelMap.get("farmHidden");

                if (farmHidden != null) {
                    farmHidden.setHidden("farm".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper farmShow = modelMap.get("farmShow");
                if (farmShow != null) {
                    farmShow.setHidden(!"farm".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFeedAnimal() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper feedAnimalHidden = modelMap.get("feedAnimalHidden");

                if (feedAnimalHidden != null) {
                    feedAnimalHidden.setHidden("feed_animal".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper feedAnimalShow = modelMap.get("feedAnimalShow");
                if (feedAnimalShow != null) {
                    feedAnimalShow.setHidden(!"feed_animal".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskIdle() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper idleHidden = modelMap.get("idleHidden");

                if (idleHidden != null) {
                    idleHidden.setHidden("idle".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper idleShow = modelMap.get("idleShow");
                if (idleShow != null) {
                    idleShow.setHidden(!"idle".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskMilk() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper milkHidden = modelMap.get("milkHidden");

                if (milkHidden != null) {
                    milkHidden.setHidden("milk".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper milkShow = modelMap.get("milkShow");
                if (milkShow != null) {
                    milkShow.setHidden(!"milk".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskShears() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper shearsHidden = modelMap.get("shearsHidden");

                if (shearsHidden != null) {
                    shearsHidden.setHidden("shears".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper shearsShow = modelMap.get("shearsShow");
                if (shearsShow != null) {
                    shearsShow.setHidden(!"shears".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskSugarCane() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper sugarCaneHidden = modelMap.get("sugarCaneHidden");

                if (sugarCaneHidden != null) {
                    sugarCaneHidden.setHidden("sugar_cane".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper sugarCaneShow = modelMap.get("sugarCaneShow");
                if (sugarCaneShow != null) {
                    sugarCaneShow.setHidden(!"sugar_cane".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskCocoa() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper cocoaHidden = modelMap.get("cocoaHidden");

                if (cocoaHidden != null) {
                    cocoaHidden.setHidden("cocoa".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper cocoaShow = modelMap.get("cocoaShow");
                if (cocoaShow != null) {
                    cocoaShow.setHidden(!"cocoa".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskExtinguishing() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper extinguishingHidden = modelMap.get("extinguishingHidden");

                if (extinguishingHidden != null) {
                    extinguishingHidden.setHidden("extinguishing".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper extinguishingShow = modelMap.get("extinguishingShow");
                if (extinguishingShow != null) {
                    extinguishingShow.setHidden(!"extinguishing".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskFeed() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper feedHidden = modelMap.get("feedHidden");

                if (feedHidden != null) {
                    feedHidden.setHidden("feed".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper feedShow = modelMap.get("feedShow");
                if (feedShow != null) {
                    feedShow.setHidden(!"feed".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskGrass() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper grassHidden = modelMap.get("grassHidden");

                if (grassHidden != null) {
                    grassHidden.setHidden("grass".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper grassShow = modelMap.get("grassShow");
                if (grassShow != null) {
                    grassShow.setHidden(!"grass".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskMelon() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper melonHidden = modelMap.get("melonHidden");

                if (melonHidden != null) {
                    melonHidden.setHidden("melon".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper melonShow = modelMap.get("melonShow");
                if (melonShow != null) {
                    melonShow.setHidden(!"melon".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskRangedAttack() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper rangedAttackHidden = modelMap.get("rangedAttackHidden");

                if (rangedAttackHidden != null) {
                    rangedAttackHidden.setHidden("ranged_attack".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper rangedAttackShow = modelMap.get("rangedAttackShow");
                if (rangedAttackShow != null) {
                    rangedAttackShow.setHidden(!"ranged_attack".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskSnow() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper snowHidden = modelMap.get("snowHidden");

                if (snowHidden != null) {
                    snowHidden.setHidden("snow".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper snowShow = modelMap.get("snowShow");
                if (snowShow != null) {
                    snowShow.setHidden(!"snow".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }

    public static IAnimation<EntityMaid> getTaskTorch() {
        return new IAnimation<EntityMaid>() {
            @Override
            public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, EntityMaid maid, HashMap<String, ModelRendererWrapper> modelMap) {
                ModelRendererWrapper torchHidden = modelMap.get("torchHidden");

                if (torchHidden != null) {
                    torchHidden.setHidden("torch".equals(maid.getTask().getUid().getPath()));
                }

                ModelRendererWrapper torchShow = modelMap.get("torchShow");
                if (torchShow != null) {
                    torchShow.setHidden(!"torch".equals(maid.getTask().getUid().getPath()));
                }
            }
        };
    }
}
