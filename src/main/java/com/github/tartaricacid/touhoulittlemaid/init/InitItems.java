package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<Item> MAID_BACKPACK_SMALL = ITEMS.register("maid_backpack_small", () -> new ItemMaidBackpack(BackpackLevel.SMALL));
    public static RegistryObject<Item> MAID_BACKPACK_MIDDLE = ITEMS.register("maid_backpack_middle", () -> new ItemMaidBackpack(BackpackLevel.MIDDLE));
    public static RegistryObject<Item> MAID_BACKPACK_BIG = ITEMS.register("maid_backpack_big", () -> new ItemMaidBackpack(BackpackLevel.BIG));
    public static RegistryObject<Item> CHAIR = ITEMS.register("chair", ItemChair::new);
    public static RegistryObject<Item> HAKUREI_GOHEI = ITEMS.register("hakurei_gohei", ItemHakureiGohei::new);

    public static ItemGroup MAIN_TAB = new MaidGroup("main", HAKUREI_GOHEI);

    public static RegistryObject<Item> MAID_SPAWN_EGG = ITEMS.register("maid_spawn_egg", () -> new SpawnEggItem(EntityMaid.TYPE, 0x4a6195, 0xffffff, (new Item.Properties()).tab(MAIN_TAB)));
    public static RegistryObject<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new SpawnEggItem(EntityFairy.TYPE, 0x171c20, 0xffffff, (new Item.Properties()).tab(MAIN_TAB)));
}