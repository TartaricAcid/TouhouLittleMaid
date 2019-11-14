package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/11/14 20:27
 **/
public class SpawnMaidRecipe extends AltarRecipe {
    public SpawnMaidRecipe(float powerCost, ProcessingInput... inputs) {
        super(new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"), powerCost, ItemStack.EMPTY, inputs);
    }

    @Override
    public Entity getOutputEntity(World world, BlockPos pos, List<ItemStack> inputItems) {
        EntityBox box = new EntityBox(world);
        box.setPosition(pos.getX() + 1, pos.getY() + 2, pos.getZ());
        box.setRandomTexture();
        world.spawnEntity(box);

        EntityMaid maid = new EntityMaid(world);
        maid.setPosition(pos.getX() + 1, pos.getY() + 2, pos.getZ());
        maid.onInitialSpawn(world.getDifficultyForLocation(maid.getPosition()), null);
        return maid;
    }
}
