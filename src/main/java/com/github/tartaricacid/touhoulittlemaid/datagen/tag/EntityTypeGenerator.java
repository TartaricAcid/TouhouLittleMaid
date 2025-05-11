package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;

import java.util.concurrent.CompletableFuture;

public class EntityTypeGenerator extends ForgeEntityTypeTagsProvider {
    public static TagKey<EntityType<?>> MAID_FAIRY_ATTACK_GOAL = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid_fairy_attack_goal"));

    public EntityTypeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(InitEntities.DANMAKU.get());
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(InitEntities.FAIRY.get());
        tag(TagKey.create(Registries.ENTITY_TYPE, id("iceandfire:immune_to_gorgon_stone"))).add(InitEntities.MAID.get());

        tag(MAID_FAIRY_ATTACK_GOAL).add(EntityType.IRON_GOLEM)
                .addOptional(id("guardvillagers:guard"))
                .addOptional(id("earthtojavamobs:furnace_golem"))
                .addOptional(id("earthmobsmod:furnace_golem"))
                .addOptional(id("mutantmonsters:mutant_snow_golem"))
                .addOptional(id("alexscaves:gingerbread_man"))
                .addOptional(id("alexsmobs:bunfungus"));
    }

    private ResourceLocation id(String name) {
        return new ResourceLocation(name);
    }
}
