package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.dataGen.EnchantmentKeys;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static com.github.tartaricacid.touhoulittlemaid.init.InitItems.*;

public class InitCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TouhouLittleMaid.MOD_ID);

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.touhou_little_maid.main"))
            .icon(() -> InitItems.HAKUREI_GOHEI.get().getDefaultInstance())
            .displayItems((par, output) -> {
//                if (ModList.get().isLoaded("patchouli")) {
//                    output.accept(ItemModBook.forBook(new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo")));
//                }
// TODO 帕秋莉手册
                output.accept(MAID_SPAWN_EGG.get());
                output.accept(FAIRY_SPAWN_EGG.get());
                output.accept(HAKUREI_GOHEI.get());
                output.accept(SANAE_GOHEI.get());
                output.accept(POWER_POINT.get());
                output.accept(SMART_SLAB_EMPTY.get());
                output.accept(SMART_SLAB_INIT.get());
                output.accept(MAID_BACKPACK_SMALL.get());
                output.accept(MAID_BACKPACK_MIDDLE.get());
                output.accept(MAID_BACKPACK_BIG.get());
                output.accept(CRAFTING_TABLE_BACKPACK.get());
                output.accept(ENDER_CHEST_BACKPACK.get());
                output.accept(FURNACE_BACKPACK.get());
                output.accept(TANK_BACKPACK.get());
                output.accept(SUBSTITUTE_JIZO.get());
                output.accept(ULTRAMARINE_ORB_ELIXIR.get());
                output.accept(EXPLOSION_PROTECT_BAUBLE.get());
                output.accept(FIRE_PROTECT_BAUBLE.get());
                output.accept(PROJECTILE_PROTECT_BAUBLE.get());
                output.accept(MAGIC_PROTECT_BAUBLE.get());
                output.accept(FALL_PROTECT_BAUBLE.get());
                output.accept(DROWN_PROTECT_BAUBLE.get());
                output.accept(NIMBLE_FABRIC.get());
                output.accept(ITEM_MAGNET_BAUBLE.get());
                output.accept(MUTE_BAUBLE.get());
                output.accept(WIRELESS_IO.get());
                output.accept(TRUMPET.get());
                output.accept(RED_FOX_SCROLL.get());
                output.accept(WHITE_FOX_SCROLL.get());
                output.accept(KAPPA_COMPASS.get());
                output.accept(EXTINGUISHER.get());
                output.accept(GOMOKU.get());
                output.accept(KEYBOARD.get());
                output.accept(BOOKSHELF.get());
                output.accept(COMPUTER.get());
                output.accept(FAVORABILITY_TOOL_ADD.get());
                output.accept(FAVORABILITY_TOOL_REDUCE.get());
                output.accept(FAVORABILITY_TOOL_FULL.get());
                output.accept(CAMERA.get());
                output.accept(PHOTO.get());
                output.accept(FILM.get());
                output.accept(CHISEL.get());
                output.accept(MAID_BED.get());
                output.accept(PICNIC_BASKET.get());
                output.accept(MAID_BEACON.get());
                output.accept(SHRINE.get());
                output.accept(MODEL_SWITCHER.get());
                output.accept(CHAIR_SHOW.get());
                output.accept(BROOM.get());
//                if (FMLEnvironment.dist == Dist.CLIENT) {
//                    ItemEntityPlaceholder.fillItemCategory(output);
//                }
//                output.accept(getEnchantmentBook(EnchantmentKeys.IMPEDING));
//                output.accept(getEnchantmentBook(EnchantmentKeys.SPEEDY));
//                output.accept(getEnchantmentBook(EnchantmentKeys.ENDERS_ENDER));
            }).build());

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> GARAGE_KIT_TAB = TABS.register("chair", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.touhou_little_maid.chair"))
            .icon(() -> InitItems.CHAIR.get().getDefaultInstance())
            .displayItems((par, output) -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ItemChair.fillItemCategory(output);
                }
            }).build());

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> CHAIR_TAB = TABS.register("garage_kit", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.touhou_little_maid.garage_kit"))
            .icon(() -> InitItems.GARAGE_KIT.get().getDefaultInstance())
            .displayItems((par, output) -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    BlockGarageKit.fillItemCategory(output);
                }
            }).build());

//    private static ItemStack getEnchantmentBook(DeferredHolder<Enchantment,Enchantment> registryObject) {
//        Enchantment enchantment = registryObject.get();
//        EnchantmentInstance instance = new EnchantmentInstance(enchantment, enchantment.getMaxLevel());
//        return EnchantedBookItem.createForEnchantment(instance);
//    }
}
