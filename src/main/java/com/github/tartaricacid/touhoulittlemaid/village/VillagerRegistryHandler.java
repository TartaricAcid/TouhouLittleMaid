package com.github.tartaricacid.touhoulittlemaid.village;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.IMerchant;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import static net.minecraft.entity.passive.EntityVillager.ITradeList;
import static net.minecraft.entity.passive.EntityVillager.PriceInfo;
import static net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import static net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/**
 * @author TartaricAcid
 * @date 2019/8/3 16:45
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class VillagerRegistryHandler {
    private static final ResourceLocation PROFESSION = new ResourceLocation(TouhouLittleMaid.MOD_ID, "store_owner_of_kourindou");
    private static final ResourceLocation VILLAGER_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/villager_rinnosuke.png");
    private static final ResourceLocation ZOMBIE_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/zombie_rinnosuke.png");

    public static final VillagerProfession STORE_OWNER_OF_KOURINDOU = new VillagerProfession(PROFESSION.toString(),
            VILLAGER_TEXTURE.toString(), ZOMBIE_TEXTURE.toString());
    public static final VillagerCareer STORE_OWNER_OF_KOURINDOU_CAREER = new VillagerCareer(STORE_OWNER_OF_KOURINDOU,
            String.format("%s.store_owner_of_kourindou.name", TouhouLittleMaid.MOD_ID));

    @SubscribeEvent
    public static void registry(RegistryEvent.Register<VillagerProfession> event) {
        ItemStack emerald = new ItemStack(Items.EMERALD);

        ItemStack hakureiGohei = MaidItems.HAKUREI_GOHEI.getDefaultInstance();
        ItemStack maidSpawnEgg = new ItemStack(Items.SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(maidSpawnEgg, new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"));

        ItemStack kappaCompass = new ItemStack(MaidItems.KAPPA_COMPASS);
        ItemStack ultramarineOrbElixir = new ItemStack(MaidItems.ULTRAMARINE_ORB_ELIXIR);

        ItemStack marisaBroom = new ItemStack(MaidItems.MARISA_BROOM);
        ItemStack grid = new ItemStack(Item.getItemFromBlock(MaidBlocks.GRID));

        STORE_OWNER_OF_KOURINDOU_CAREER.addTrade(1,
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(12, 24), hakureiGohei, new PriceInfo(1, 1)),
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(24, 32), maidSpawnEgg, new PriceInfo(1, 2))
        ).addTrade(2,
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(2, 3), kappaCompass, new PriceInfo(1, 1)),
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(7, 11), ultramarineOrbElixir, new PriceInfo(1, 1))
        ).addTrade(3,
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(12, 15), marisaBroom, new PriceInfo(1, 1)),
                new ItemStackToItemStack(emerald.copy(), new PriceInfo(2, 3), grid, new PriceInfo(2, 4))
        );

        event.getRegistry().register(STORE_OWNER_OF_KOURINDOU);
    }

    /**
     * 一个简单的物品堆交换物品堆的类
     */
    private static class ItemStackToItemStack implements ITradeList {
        private ItemStack sellItem;
        private ItemStack buyItem;
        private PriceInfo sellCount;
        private PriceInfo buyCount;

        public ItemStackToItemStack(ItemStack sellItem, PriceInfo sellCount, ItemStack buyItem, PriceInfo buyCount) {
            this.sellItem = sellItem;
            this.sellCount = sellCount;
            this.buyItem = buyItem;
            this.buyCount = buyCount;
        }

        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            sellItem.setCount(sellCount.getPrice(random));
            buyItem.setCount(buyCount.getPrice(random));
            recipeList.add(new MerchantRecipe(sellItem, buyItem));
        }
    }
}
