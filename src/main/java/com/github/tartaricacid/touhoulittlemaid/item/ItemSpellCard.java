package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author TartaricAcid
 * @date 2019/11/15 13:06
 **/
public class ItemSpellCard extends Item {
    private static final String SPELL_CARD_ENTRY_TAG = "SpellCardEntry";

    public ItemSpellCard() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".spell_card");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.SPELL_CARD_TABS);
    }

    @SuppressWarnings("all")
    public static ItemStack setCustomSpellCardEntry(String id, ItemStack spellCard) {
        if (spellCard.getItem() == MaidItems.SPELL_CARD && ClientProxy.CUSTOM_SPELL_CARD_MAP_CLIENT.containsKey(id)) {
            NBTTagCompound tag;
            if (spellCard.hasTagCompound()) {
                tag = spellCard.getTagCompound();
            } else {
                tag = new NBTTagCompound();
            }
            tag.setString(SPELL_CARD_ENTRY_TAG, id);
            spellCard.setTagCompound(tag);
        }
        return spellCard;
    }

    @Nullable
    @SuppressWarnings("all")
    public static CustomSpellCardEntry getCustomSpellCardEntry(ItemStack spellCard, Map<String, CustomSpellCardEntry> map) {
        if (map.isEmpty()) {
            return null;
        }
        CustomSpellCardEntry defaultEntry = map.values().stream().findFirst().get();
        if (spellCard.getItem() == MaidItems.SPELL_CARD && spellCard.hasTagCompound()) {
            String id = spellCard.getTagCompound().getString(SPELL_CARD_ENTRY_TAG);
            if (!id.isEmpty()) {
                return map.getOrDefault(id, defaultEntry);
            }
        }
        return defaultEntry;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            Set<String> ids = FMLCommonHandler.instance().getSide().isClient() ? ClientProxy.CUSTOM_SPELL_CARD_MAP_CLIENT.keySet() : CommonProxy.CUSTOM_SPELL_CARD_MAP_SERVER.keySet();
            for (String id : ids) {
                items.add(setCustomSpellCardEntry(id, new ItemStack(this)));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Map<String, CustomSpellCardEntry> map = FMLCommonHandler.instance().getSide().isClient() ? ClientProxy.CUSTOM_SPELL_CARD_MAP_CLIENT : CommonProxy.CUSTOM_SPELL_CARD_MAP_SERVER;
        CustomSpellCardEntry entry = getCustomSpellCardEntry(stack, map);
        if (entry == null) {
            return "";
        }
        return TextFormatting.GOLD + net.minecraft.util.text.translation.I18n.translateToLocal(entry.getNameKey());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        CustomSpellCardEntry entry = getCustomSpellCardEntry(stack, ClientProxy.CUSTOM_SPELL_CARD_MAP_CLIENT);
        if (entry == null) {
            return;
        }
        if (!entry.getDescriptionKey().isEmpty()) {
            tooltip.add(I18n.format(entry.getDescriptionKey()));
        }
        if (!entry.getAuthor().isEmpty()) {
            tooltip.add("\u00a7\u0061\u258d\u00a0\u00a7\u0037" + I18n.format("tooltips.touhou_little_maid.spell_card.author", entry.getAuthor()));
        }
        if (!entry.getVersion().isEmpty()) {
            tooltip.add("\u00a7\u0061\u258d\u00a0\u00a7\u0037" + I18n.format("tooltips.touhou_little_maid.spell_card.version", entry.getVersion()));
        }
        tooltip.add("\u00a7\u0061\u258d\u00a0\u00a7\u0037" + I18n.format("tooltips.touhou_little_maid.spell_card.cooldown", entry.getCooldown()));
    }
}
