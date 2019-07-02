package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class UltramarineOrbElixir extends Item implements IMaidBauble {
    public UltramarineOrbElixir() {
        setRegistryName("ultramarine_orb_elixir");
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".ultramarine_orb_elixir");
        setMaxStackSize(1);
        setMaxDamage(5);
    }

    @Override
    public boolean onMaidDeath(EntityMaid entityMaid, ItemStack baubleItem, DamageSource cause) {
        if (!baubleItem.isEmpty()) {
            entityMaid.setHealth(entityMaid.getMaxHealth());
            entityMaid.spawnExplosionParticle();
            baubleItem.damageItem(1, entityMaid);
            entityMaid.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.ultramarine_orb_elixir.desc",
                (stack.getMaxDamage() + 1 - stack.getItemDamage())));
    }
}
