package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemFilm extends Item {
    public static final String MAID_INFO = "MaidInfo";

    public ItemFilm() {
        super((new Item.Properties()).stacksTo(1));
    }

    public static ItemStack maidToFilm(EntityMaid maid) {
        ItemStack film = InitItems.FILM.get().getDefaultInstance();
        CompoundTag filmTag = new CompoundTag();
        CompoundTag maidTag = new CompoundTag();
        maid.addAdditionalSaveData(maidTag);
        removeMaidSomeData(maidTag);
        maidTag.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(InitEntities.MAID.get())).toString());
        filmTag.put(MAID_INFO, maidTag);
        film.setTag(filmTag);
        if (maid.hasCustomName()) {
            film.setHoverName(maid.getCustomName());
        }
        return film;
    }

    private static boolean hasMaidData(ItemStack stack) {
        return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO).isEmpty();
    }

    private static CompoundTag getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO);
        }
        return new CompoundTag();
    }

    private static void removeMaidSomeData(CompoundTag nbt) {
        nbt.remove(EntityMaid.BACKPACK_LEVEL_TAG);
        nbt.remove(EntityMaid.MAID_INVENTORY_TAG);
        nbt.remove(EntityMaid.MAID_BAUBLE_INVENTORY_TAG);
        nbt.remove(EntityMaid.EXPERIENCE_TAG);
        nbt.remove("ArmorItems");
        nbt.remove("HandItems");
        nbt.remove("Leash");
        nbt.remove("Health");
        nbt.remove("HurtTime");
        nbt.remove("DeathTime");
        nbt.remove("HurtByTimestamp");
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (!entity.isInvulnerable()) {
            entity.setInvulnerable(true);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!hasMaidData(stack)) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.film.no_data.desc").withStyle(ChatFormatting.DARK_RED));
        } else {
            CompoundTag maidData = getMaidData(stack);
            if (maidData.contains(EntityMaid.MODEL_ID_TAG, Tag.TAG_STRING)) {
                String modelId = maidData.getString(EntityMaid.MODEL_ID_TAG);
                if (StringUtils.isNotBlank(modelId)) {
                    CustomPackLoader.MAID_MODELS.getInfo(modelId).ifPresent(modelItem ->
                            tooltip.add(Component.translatable("tooltips.touhou_little_maid.photo.maid.desc",
                                    I18n.get(ParseI18n.getI18nKey(modelItem.getName()))).withStyle(ChatFormatting.GRAY)
                            ));
                }
            }
        }
    }
}
