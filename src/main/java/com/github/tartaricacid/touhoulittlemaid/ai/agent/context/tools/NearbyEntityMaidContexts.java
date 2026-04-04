package com.github.tartaricacid.touhoulittlemaid.ai.agent.context.tools;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.AbstractMaidContext;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.LIST_SEPARATORS;
import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.NONE;

public final class NearbyEntityMaidContexts {
    public static final String CATEGORY = "nearby_entities";
    private static final String SUMMARY = "Nearby living entities, including type, entity id, distance to self, and distance to user.";
    private static final int MAX_ENTITIES = 20;

    private NearbyEntityMaidContexts() {
    }

    public static void registerAll(GameContextRegister register) {
        register.registerCategory(CATEGORY, SUMMARY, false);
        register.registerContext(CATEGORY, new NearbyEntitiesContext());
    }

    private static final class NearbyEntitiesContext extends AbstractMaidContext {
        private NearbyEntitiesContext() {
            super("nearby_entities", "List of nearby living entities with id and distances");
        }

        @Override
        public String getValue(EntityMaid maid) {
            AABB scanBox = maid.getTask().searchDimension(maid);
            List<LivingEntity> entities = maid.level.getEntitiesOfClass(LivingEntity.class, scanBox, e -> e != maid && e.isAlive());

            if (entities.isEmpty()) {
                return NONE;
            }

            LivingEntity owner = maid.getOwner();
            List<String> entries = Lists.newArrayList();

            entities.stream()
                    .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(maid)))
                    .limit(MAX_ENTITIES)
                    .forEach(entity -> {
                        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                        if (type == null) {
                            return;
                        }

                        int id = entity.getId();
                        float distToMaid = maid.distanceTo(entity);

                        String entry;
                        if (owner != null) {
                            entry = "%s (id=%d, dist_self=%.1f, dist_user=%.1f)".formatted(type, id, distToMaid, owner.distanceTo(entity));
                        } else {
                            entry = "%s (id=%d, dist_self=%.1f)".formatted(type, id, distToMaid);
                        }

                        if (entity instanceof Player player) {
                            entry = "%s[%s]".formatted(player.getScoreboardName(), entry);
                        }

                        entries.add(entry);
                    });

            return StringUtils.join(entries, LIST_SEPARATORS);
        }
    }
}
