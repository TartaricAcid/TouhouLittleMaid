package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TagEntity extends EntityTypeTagsProvider {
    /**
     * 女仆妖精的攻击目标，默认仅攻击铁傀儡和玩家
     */
    public static TagKey<EntityType<?>> MAID_FAIRY_ATTACK_GOAL = createTagKey("maid_fairy_attack_goal");

    /**
     * 女仆在骑乘时，为了朝向一致，会强制同步女仆朝向和当前骑乘实体朝向；
     * <p>
     * 但是部分模组（如机械动力）这么做反而会导致女仆异常旋转，故添加此标签
     */
    public static TagKey<EntityType<?>> MAID_VEHICLE_ROTATE_BLOCKLIST = createTagKey("maid_vehicle_rotate_blocklist");

    public static TagKey<EntityType<?>> CARRYON_ENTITY_BLACKLIST = createTagKey(ResourceLocation.parse("carryon:entity_blacklist"));

    /**
     * 冰与火的石化效果免疫标签
     */
    public static final TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = createTagKey(
            ResourceLocation.parse("iceandfire:immune_to_gorgon_stone")
    );

    public static TagKey<EntityType<?>> SABLE_DESTROY_WHEN_LEAVING_PLOT = createTagKey(ResourceLocation.parse("sable:destroy_when_leaving_plot"));
    public static TagKey<EntityType<?>> SABLE_RETAIN_IN_SUB_LEVEL = createTagKey(ResourceLocation.parse("sable:retain_in_sub_level"));


    public TagEntity(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TouhouLittleMaid.MOD_ID, existingFileHelper);
    }

    private static TagKey<EntityType<?>> createTagKey(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, name));
    }

    private static TagKey<EntityType<?>> createTagKey(ResourceLocation id) {
        return TagKey.create(Registries.ENTITY_TYPE, id);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(InitEntities.DANMAKU.get());
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(InitEntities.FAIRY.get());

        tag(MAID_FAIRY_ATTACK_GOAL).add(EntityType.IRON_GOLEM)
                .addOptional(id("guardvillagers:guard"))
                .addOptional(id("earthtojavamobs:furnace_golem"))
                .addOptional(id("earthmobsmod:furnace_golem"))
                .addOptional(id("mutantmonsters:mutant_snow_golem"))
                .addOptional(id("alexscaves:gingerbread_man"))
                .addOptional(id("alexsmobs:bunfungus"));

        tag(MAID_VEHICLE_ROTATE_BLOCKLIST)
                .addOptional(id("create:carriage_contraption"))
                .addOptional(id("create:seat"));

        tag(CARRYON_ENTITY_BLACKLIST).add(
                InitEntities.TOMBSTONE.get(),
                InitEntities.SIT.get(),
                InitEntities.BROOM.get());

        tag(SABLE_DESTROY_WHEN_LEAVING_PLOT).add(
                InitEntities.SIT.get()
        );
        tag(SABLE_RETAIN_IN_SUB_LEVEL).add(
                InitEntities.CHAIR.get(),
                InitEntities.SIT.get()
        );

        // 让女仆免疫冰与火的石化效果，避免石化带来的各种问题
        tag(IMMUNE_TO_GORGON_STONE).add(InitEntities.MAID.get());
    }

    private ResourceLocation id(String name) {
        return ResourceLocation.parse(name);
    }
}
