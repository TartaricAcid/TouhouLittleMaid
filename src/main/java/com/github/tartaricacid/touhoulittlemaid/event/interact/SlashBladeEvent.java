package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class SlashBladeEvent {
    private static final String SLASH_BLADE_MOD_ID = "flammpfeil.slashblade";

    @SubscribeEvent
    @Optional.Method(modid = SLASH_BLADE_MOD_ID)
    public static void onInteract(InteractMaidEvent event) {
        ItemStack stick = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (stick.getItem() == Items.STICK && player.isSneaking()) {
            boolean mainHandSuccess = setSlashBladePosition(maid.getHeldItemMainhand());
            boolean offHandSuccess = setSlashBladePosition(maid.getHeldItemOffhand());
            if (mainHandSuccess || offHandSuccess) {
                stick.shrink(1);
                if (!world.isRemote) {
                    player.sendMessage(new TextComponentTranslation("message.touhou_little_maid.slashblade.position"));
                }
                event.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("all")
    private static boolean setSlashBladePosition(ItemStack stack) {
        ResourceLocation res = stack.getItem().getRegistryName();
        boolean isSlashBlade = !stack.isEmpty() && res != null
                && SLASH_BLADE_MOD_ID.equals(res.getNamespace())
                && res.getPath().startsWith("slashblade");
        if (isSlashBlade) {
            NBTTagCompound compound;
            if (stack.hasTagCompound()) {
                compound = stack.getTagCompound();
            } else {
                compound = new NBTTagCompound();
            }
            compound.setFloat("adjustX", -1.5f);
            compound.setFloat("adjustY", -5.5f);
            compound.setFloat("adjustZ", 2.5f);
            stack.setTagCompound(compound);
            return true;
        }
        return false;
    }
}
