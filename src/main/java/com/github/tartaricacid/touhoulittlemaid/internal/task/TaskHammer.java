package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidHammer;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskHammer implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "hammer");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.DIAMOND_PICKAXE);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid) {
        return Util.environmentSound(maid, MaidSoundEvent.MAID_BREAK, 0.2f);
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid) {
        return new EntityMaidHammer(maid, 0.6f);
    }
}
