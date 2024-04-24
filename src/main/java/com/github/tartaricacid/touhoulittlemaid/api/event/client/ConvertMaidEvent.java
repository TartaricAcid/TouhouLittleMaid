package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
@OnlyIn(Dist.CLIENT)
public class ConvertMaidEvent extends Event {

	private Mob mob;
	EntityMaidRenderable maid;

	public ConvertMaidEvent(Mob mob) {
		this.mob = mob;
	}

	public Mob getEntity() {
		return mob;
	}

	public void setMaid(EntityMaidRenderable maid) {
		this.maid = maid;
	}

}
