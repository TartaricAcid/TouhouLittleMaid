package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.isValidResourceLocation;

public class ConditionalPassenger {
    private static final String EMPTY = "";
    private final List<ResourceLocation> idTest = Lists.newArrayList();
    private final List<TagKey<EntityType<?>>> tagTest = Lists.newArrayList();
    private final String idPre;
    private final String tagPre;

    public ConditionalPassenger() {
        this.idPre = "passenger$";
        this.tagPre = "passenger#";
    }

    public void addTest(String name) {
        int preSize = this.idPre.length();
        if (name.length() <= preSize) {
            return;
        }
        String substring = name.substring(preSize);
        if (name.startsWith(idPre) && isValidResourceLocation(substring)) {
            idTest.add(ResourceLocation.parse(substring));
        }
        if (name.startsWith(tagPre) && isValidResourceLocation(substring)) {
            tagTest.add(TagKey.create(
                    Registries.ENTITY_TYPE,
                    ResourceLocation.parse(substring)
            ));
        }
    }

    public String doTest(Mob maid) {
        Entity passenger = maid.getFirstPassenger();
        if (passenger == null || !passenger.isAlive()) {
            return EMPTY;
        }
        String result = doIdTest(passenger);
        if (result.isEmpty()) {
            return doTagTest(passenger);
        }
        return result;
    }

    private String doIdTest(Entity passenger) {
        if (idTest.isEmpty()) {
            return EMPTY;
        }
        ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(passenger.getType());
        if (idTest.contains(registryName)) {
            return idPre + registryName;
        }
        return EMPTY;
    }

    private String doTagTest(Entity passenger) {
        if (tagTest.isEmpty()) {
            return EMPTY;
        }
        // ITagManager<EntityType<?>> tags = BuiltInRegistries.ENTITY_TYPE.getTags();
        // if (tags == null) {
        //     return EMPTY;
        // }
        return tagTest.stream().filter(tag -> passenger.getType().is(tag)).findFirst().map(itemTagKey -> tagPre + itemTagKey.location()).orElse(EMPTY);
    }
}
