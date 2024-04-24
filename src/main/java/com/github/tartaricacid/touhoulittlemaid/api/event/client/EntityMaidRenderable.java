package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

/**
 * 女仆渲染接口
 */
public interface EntityMaidRenderable {

	@Nullable
	static EntityMaidRenderable convert(Mob mob) {
		if (mob instanceof EntityMaidRenderable holder) return holder;
		var event = new ConvertMaidEvent(mob);
		MinecraftForge.EVENT_BUS.post(event);
		return event.maid;
	}

	@Nullable
	static EntityMaid convertToMaid(Mob mob) {
		EntityMaidRenderable renderable = convert(mob);
		if (renderable == null) return null;
		return renderable.asStrictMaid();
	}

	default ItemStack getBackpackShowItem() {
		return ItemStack.EMPTY;
	}

	String getModelId();

	default IMaidBackpack getMaidBackpackType() {
		return BackpackManager.getEmptyBackpack();
	}

	ItemStack getHeadBlock(Mob mob);

	@Nullable
	default EntityMaid asStrictMaid() {
		return null;
	}

	Mob asEntity();

}
