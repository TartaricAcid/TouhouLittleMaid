package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardManger;
import com.github.tartaricacid.touhoulittlemaid.danmaku.script.EntityLivingBaseWrapper;
import com.github.tartaricacid.touhoulittlemaid.danmaku.script.WorldWrapper;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.script.Invocable;
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
    }

    /**
     * 使用符卡
     *
     * @param user  使用者
     * @param entry 具体的符卡条目
     * @return 是否使用成功
     */
    public static boolean useSpellCard(EntityLivingBase user, CustomSpellCardEntry entry) {
        try {
            if (entry == null) {
                return false;
            }
            Invocable invocable = (Invocable) CommonProxy.NASHORN;
            invocable.invokeMethod(entry.getScript(), "spellCard",
                    new WorldWrapper(user.getEntityWorld()), new EntityLivingBaseWrapper(user));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("all")
    public static ItemStack setCustomSpellCardEntry(String id, ItemStack spellCard) {
        if (spellCard.getItem() == MaidItems.SPELL_CARD) {
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
    public static CustomSpellCardEntry getCustomSpellCardEntry(ItemStack spellCard, @Nullable EntityLivingBase user) {
        Map<String, CustomSpellCardEntry> map;
        if (user != null && user.world.isRemote) {
            map = CustomSpellCardManger.CUSTOM_SPELL_CARD_CLIENT;
        } else {
            map = CustomSpellCardManger.CUSTOM_SPELL_CARD_SERVER;
        }
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
            Set<String> ids = CustomSpellCardManger.CUSTOM_SPELL_CARD_CLIENT.keySet();
            for (String id : ids) {
                items.add(setCustomSpellCardEntry(id, new ItemStack(this)));
            }
        }
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        CustomSpellCardEntry entry = getCustomSpellCardEntry(stack, null);
        if (entry == null) {
            return "";
        }
        return TextFormatting.GOLD + net.minecraft.util.text.translation.I18n.translateToLocal(entry.getNameKey());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        CustomSpellCardEntry entry = getCustomSpellCardEntry(stack, Minecraft.getMinecraft().player);
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
