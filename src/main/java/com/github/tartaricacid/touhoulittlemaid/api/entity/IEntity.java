package com.github.tartaricacid.touhoulittlemaid.api.entity;

import com.github.tartaricacid.touhoulittlemaid.util.BiomeCacheUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;

public interface IEntity {

	Mob asEntity();

	default boolean hasHelmet() {
		return !asEntity().getItemBySlot(EquipmentSlot.HEAD).isEmpty();
	}

	default boolean hasChestPlate() {
		return !asEntity().getItemBySlot(EquipmentSlot.CHEST).isEmpty();
	}

	default boolean hasLeggings() {
		return !asEntity().getItemBySlot(EquipmentSlot.LEGS).isEmpty();
	}

	default boolean hasBoots() {
		return !asEntity().getItemBySlot(EquipmentSlot.FEET).isEmpty();
	}

	default boolean onHurt() {
		return asEntity().hurtTime > 0;
	}

	@Deprecated
	default String getAtBiomeTemp() {
		float temp = BiomeCacheUtil.getCacheBiome(asEntity()).getBaseTemperature();
		if (temp < 0.15) {
			return "COLD";
		} else if (temp < 0.55) {
			return "OCEAN";
		} else if (temp < 0.95) {
			return "MEDIUM";
		} else {
			return "WARM";
		}
	}

	@Deprecated
	default boolean hasSasimono() {
		return false;
	}

	@Deprecated
	default boolean isSitInJoyBlock() {
		return false;
	}

}
