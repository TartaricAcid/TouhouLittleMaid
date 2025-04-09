package com.github.tartaricacid.touhoulittlemaid.datagen.tag;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeGenerator extends EntityTypeTagsProvider {
    public EntityTypeGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TouhouLittleMaid.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        tag(EntityTypeTags.IMPACT_PROJECTILES).add(InitEntities.DANMAKU.get());
        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(InitEntities.FAIRY.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(InitEntities.FAIRY.get());
//        tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(InitEntities.FAIRY.get());
        tag(TagKey.create(ForgeRegistries.ENTITIES.getRegistryKey(), new ResourceLocation("iceandfire", "immune_to_gorgon_stone")))
                .add(InitEntities.MAID.get());
    }
}
