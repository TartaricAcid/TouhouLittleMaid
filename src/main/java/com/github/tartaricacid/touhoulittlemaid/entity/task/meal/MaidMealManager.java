package com.github.tartaricacid.touhoulittlemaid.entity.task.meal;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MaidMealManager {
    private static Map<MaidMealType, List<IMaidMeal>> ALL_MEAL_TYPES;

    public MaidMealManager() {
        ALL_MEAL_TYPES = Maps.newEnumMap(MaidMealType.class);
    }

    public static void init() {
        MaidMealManager manager = new MaidMealManager();
        manager.addMaidMeal(MaidMealType.WORK_MEAL, new DefaultMaidWorkMeal());
        manager.addMaidMeal(MaidMealType.HEAL_MEAL, new DefaultMaidHealSelfMeal());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMaidMeal(manager);
        }
        ALL_MEAL_TYPES = ImmutableMap.copyOf(ALL_MEAL_TYPES);
    }

    public void addMaidMeal(MaidMealType type, IMaidMeal maidMeal) {
        ALL_MEAL_TYPES.putIfAbsent(type, Lists.newArrayList());
        ALL_MEAL_TYPES.get(type).add(maidMeal);
    }

    public static List<IMaidMeal> getMaidMeals(MaidMealType type) {
        List<IMaidMeal> maidMeals = ALL_MEAL_TYPES.get(type);
        if (maidMeals == null) {
            return Collections.emptyList();
        }
        return maidMeals;
    }
}
