package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityFromItem;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMonsterList;
import com.google.common.collect.Maps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public final class DefaultMonsterType {
    private final static Map<EntityType<?>, MonsterType> DEFAULT = Maps.newHashMap();

    public static void initDefault(Level level) {
        // 只允许初始化一次
        if (!DEFAULT.isEmpty()) {
            return;
        }

        ForgeRegistries.ENTITY_TYPES.getValues().forEach(type -> {
            Entity entity = type.create(level);
            if (!(entity instanceof LivingEntity livingEntity)) {
                return;
            }

            // 排除一些盔甲架，还有本模组的实体
            if (livingEntity instanceof ArmorStand || livingEntity instanceof AbstractEntityFromItem) {
                return;
            }

            // 如果继承了 Enemy 接口，那就是敌对生物
            if (livingEntity instanceof Enemy) {
                DEFAULT.putIfAbsent(type, MonsterType.HOSTILE);
                return;
            }

            // 如果是玩家、宠物、NPC、归为友好
            if (livingEntity instanceof Player || livingEntity instanceof TamableAnimal || livingEntity instanceof Npc) {
                DEFAULT.putIfAbsent(type, MonsterType.FRIENDLY);
                return;
            }

            // 否则归为中立
            DEFAULT.putIfAbsent(type, MonsterType.NEUTRAL);
        });
    }

    public static Map<EntityType<?>, MonsterType> getMonsterList(ItemStack stack, @Nullable Level level) {
        Map<EntityType<?>, MonsterType> output = Maps.newHashMap();

        if (level == null || stack.getItem() != InitItems.MONSTER_LIST.get()) {
            return output;
        }

        // 先读取配置文件的
        List<String> attackIgnore = MaidConfig.MAID_ATTACK_IGNORE.get();
        attackIgnore.forEach(text -> readConfigData(text, output));

        // 再从物品里读取数据
        for (MonsterType type : MonsterType.values()) {
            ItemMonsterList.getMonsterList(stack, type).forEach(tag -> readTagData(type, tag, output));
        }

        // 最后补齐默认数据
        DEFAULT.forEach(output::putIfAbsent);

        return output;
    }

    private static void readConfigData(String config, Map<EntityType<?>, MonsterType> output) {
        ResourceLocation id = new ResourceLocation(config);
        EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
        if (type != null) {
            output.putIfAbsent(type, MonsterType.FRIENDLY);
        }
    }

    private static void readTagData(MonsterType type, Tag tag, Map<EntityType<?>, MonsterType> output) {
        if (!(tag instanceof StringTag stringTag)) {
            return;
        }
        ResourceLocation id = new ResourceLocation(stringTag.getAsString());
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(id);
        if (entityType != null) {
            output.put(entityType, type);
        }
    }
}
