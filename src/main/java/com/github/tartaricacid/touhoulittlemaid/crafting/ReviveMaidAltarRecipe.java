package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ReviveMaidAltarRecipe extends AltarRecipe {

    public ReviveMaidAltarRecipe(float powerCost, ProcessingInput... inputs) {
        super(new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"), powerCost, ItemStack.EMPTY, inputs);
    }

    @Override
    public Entity getOutputEntity(World world, BlockPos pos, List<ItemStack> inputItems) {
        ItemStack garageKit = inputItems.stream().filter(stack -> stack.getItem() == Item.getItemFromBlock(MaidBlocks.GARAGE_KIT)).findFirst().orElseThrow(NullPointerException::new);
        EntityMaid maid = new EntityMaid(world);
        maid.setPosition(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
        maid.readEntityFromNBT(BlockGarageKit.getEntityData(garageKit));
        maid.setHealth(maid.getMaxHealth());
        maid.setModelId(BlockGarageKit.getModelId(garageKit));
        return maid;
    }

}
