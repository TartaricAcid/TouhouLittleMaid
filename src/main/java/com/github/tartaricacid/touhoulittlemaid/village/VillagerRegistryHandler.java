package com.github.tartaricacid.touhoulittlemaid.village;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.pojo.VillageTradePOJO;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.IMerchant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
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
    private static final VillagerProfession STORE_OWNER_OF_KOURINDOU = new VillagerProfession(PROFESSION.toString(),
            VILLAGER_TEXTURE.toString(), ZOMBIE_TEXTURE.toString());
    private static final VillagerCareer STORE_OWNER_OF_KOURINDOU_CAREER = new VillagerCareer(STORE_OWNER_OF_KOURINDOU,
            String.format("%s.store_owner_of_kourindou.name", TouhouLittleMaid.MOD_ID));

    @SubscribeEvent
    public static void registry(RegistryEvent.Register<VillagerProfession> event) {
        for (VillageTradePOJO trade : CommonProxy.VILLAGE_TRADE) {
            Item in = Item.getByNameOrId(trade.getIn().getId());
            Item out = Item.getByNameOrId(trade.getOut().getId());
            PriceInfo inCount = new PriceInfo(trade.getIn().getCount().getMin(), trade.getIn().getCount().getMax());
            PriceInfo outCount = new PriceInfo(trade.getOut().getCount().getMin(), trade.getOut().getCount().getMax());

            // 空值不进行加载
            if (in == null || out == null) {
                TouhouLittleMaid.LOGGER.warn("Unable to find the corresponding item: {} or {}", trade.getIn().getId(), trade.getOut().getId());
                continue;
            }

            ItemStack inStack = new ItemStack(in, 1, trade.getIn().getMeta());
            ItemStack outStack = new ItemStack(out, 1, trade.getOut().getMeta());

            // NBT 数据加载
            try {
                if (trade.getIn().getNbt() != null && !"".equals(trade.getIn().getNbt())) {
                    inStack.setTagCompound(JsonToNBT.getTagFromJson(trade.getIn().getNbt()));
                }
                if (trade.getOut().getNbt() != null && !"".equals(trade.getOut().getNbt())) {
                    outStack.setTagCompound(JsonToNBT.getTagFromJson(trade.getOut().getNbt()));
                }
            } catch (NBTException e) {
                TouhouLittleMaid.LOGGER.warn("NBT data reading exception: {} or {}", trade.getIn().getId(), trade.getOut().getId());
            }

            STORE_OWNER_OF_KOURINDOU_CAREER.addTrade(trade.getLevel(), new ItemStackToItemStack(inStack, inCount, outStack, outCount));
        }
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
