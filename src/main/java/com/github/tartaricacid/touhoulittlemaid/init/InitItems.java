package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TouhouLittleMaid.MOD_ID);
    public static ItemGroup MAIN_TAB = new ItemGroup(TouhouLittleMaid.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return Items.WHEAT.getDefaultInstance();
        }
    };

    public static RegistryObject<Item> MAID_SPAWN_EGG = ITEMS.register("maid_spawn_egg", () -> new SpawnEggItem(EntityMaid.TYPE, 0x4a6195, 0xffffff, (new Item.Properties()).tab(MAIN_TAB)));
    public static RegistryObject<Item> CHAIR = ITEMS.register("chair", ItemChair::new);
}
