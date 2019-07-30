//package com.github.tartaricacid.touhoulittlemaid.internal.task;
//
//import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
//import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidGridInteract;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidPlaceTorch;
//
//import net.minecraft.entity.ai.EntityAIBase;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundEvent;
//
//public class TaskGrid implements IMaidTask {
//    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "grid");
//
//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return new ItemStack(Item.getItemFromBlock(Blocks.CRAFTING_TABLE));
//    }
//
//    @Override
//    public SoundEvent getAmbientSound(AbstractEntityMaid maid) {
//        return null;
//    }
//
//    @Override
//    public EntityAIBase createAI(AbstractEntityMaid maid) {
//        return new EntityMaidGridInteract(maid, 0.6f);
//    }
//}
