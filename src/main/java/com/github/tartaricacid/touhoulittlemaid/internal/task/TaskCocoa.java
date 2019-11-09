package com.github.tartaricacid.touhoulittlemaid.internal.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidFarm;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * @author TartaricAcid
 * @date 2019/11/9 14:26
 **/
public class TaskCocoa implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "cocoa");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage());
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid) {
        return Util.environmentSound(maid, MaidSoundEvent.MAID_FARM, 0.2f);
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid) {
        return new EntityMaidFarm(maid, 0.6f, FarmHandler.Mode.COCOA);
    }
}
