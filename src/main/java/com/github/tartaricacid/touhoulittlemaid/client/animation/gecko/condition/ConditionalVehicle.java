package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.List;

public class ConditionalVehicle {
    private static final String EMPTY = "";
    private final List<ResourceLocation> idTest = Lists.newArrayList();
    private final List<TagKey<EntityType<?>>> tagTest = Lists.newArrayList();
    private final String idPre;
    private final String tagPre;

    public ConditionalVehicle() {
        this.idPre = "vehicle$";
        this.tagPre = "vehicle#";
    }

    public void addTest(String name) {
        int preSize = this.idPre.length();
        if (name.length() <= preSize) {
            return;
        }
        String substring = name.substring(preSize);
        if (name.startsWith(idPre) && ResourceLocation.isValidResourceLocation(substring)) {
            idTest.add(new ResourceLocation(substring));
        }
        if (name.startsWith(tagPre) && ResourceLocation.isValidResourceLocation(substring)) {
            ITagManager<EntityType<?>> tags = BuiltInRegistries.ENTITY_TYPE.tags();
            if (tags == null) {
                return;
            }
            TagKey<EntityType<?>> tagKey = tags.createTagKey(new ResourceLocation(substring));
            tagTest.add(tagKey);
        }
    }

    public String doTest(Mob maid) {
        Entity vehicle = maid.getVehicle();
        if (vehicle == null || !vehicle.isAlive()) {
            return EMPTY;
        }
        String result = doIdTest(vehicle);
        if (result.isEmpty()) {
            return doTagTest(vehicle);
        }
        return result;
    }

    private String doIdTest(Entity vehicle) {
        if (idTest.isEmpty()) {
            return EMPTY;
        }
        ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(vehicle.getType());
        if (registryName == null) {
            return EMPTY;
        }
        if (idTest.contains(registryName)) {
            return idPre + registryName;
        }
        return EMPTY;
    }

    private String doTagTest(Entity vehicle) {
        if (tagTest.isEmpty()) {
            return EMPTY;
        }
        ITagManager<EntityType<?>> tags = BuiltInRegistries.ENTITY_TYPE.tags();
        if (tags == null) {
            return EMPTY;
        }
        return tagTest.stream().filter(tag -> vehicle.getType().is(tag)).findFirst().map(itemTagKey -> tagPre + itemTagKey.location()).orElse(EMPTY);
    }
}
