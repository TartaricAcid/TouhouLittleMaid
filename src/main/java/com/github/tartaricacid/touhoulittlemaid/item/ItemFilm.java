package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemFilm extends Item {
    public static final String MAID_INFO = "MaidInfo";

    public ItemFilm() {
        super((new Item.Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    public static ItemStack maidToFilm(EntityMaid maid) {
        ItemStack film = InitItems.FILM.get().getDefaultInstance();
        CompoundNBT filmTag = new CompoundNBT();
        CompoundNBT maidTag = new CompoundNBT();
        maid.setHomeModeEnable(false);
        maid.saveWithoutId(maidTag);
        removeMaidSomeData(maidTag);
        maidTag.putString("id", Objects.requireNonNull(InitEntities.MAID.get().getRegistryName()).toString());
        filmTag.put(MAID_INFO, maidTag);
        film.setTag(filmTag);
        return film;
    }

    public static void filmToMaid(ItemStack film, Level worldIn, BlockPos pos, Player player) {
        Optional<Entity> entityOptional = EntityType.create(getMaidData(film), worldIn);
        if (entityOptional.isPresent() && entityOptional.get() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entityOptional.get();
            maid.setPos(pos.getX(), pos.getY(), pos.getZ());
            // 实体生成必须在服务端应用
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(maid);
                NetworkHandler.sendToNearby(maid, new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.EXPLOSION));
                worldIn.playSound(null, pos, InitSounds.ALTAR_CRAFT.get(), SoundSource.VOICE, 1.0f, 1.0f);
            }
            film.shrink(1);
        }
        if (!worldIn.isClientSide) {
            player.sendSystemMessage(Component.translatable("tooltips.touhou_little_maid.film.no_data.desc"));
        }
    }

    private static boolean hasMaidData(ItemStack stack) {
        return stack.hasTag() && !Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO).isEmpty();
    }

    private static CompoundNBT getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO);
        }
        return new CompoundNBT();
    }

    private static void removeMaidSomeData(CompoundNBT nbt) {
        nbt.remove(EntityMaid.MAID_BACKPACK_TYPE);
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
        Vec3 position = entity.position();
        int minY = entity.level.getMinBuildHeight();
        if (position.y < minY) {
            entity.setNoGravity(true);
            entity.setDeltaMovement(Vec3.ZERO);
            entity.setPos(position.x, minY, position.z);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!hasMaidData(stack)) {
            tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.film.no_data.desc").withStyle(TextFormatting.DARK_RED));
        } else {
            CompoundNBT maidData = getMaidData(stack);
            if (maidData.contains(EntityMaid.MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
                String modelId = maidData.getString(EntityMaid.MODEL_ID_TAG);
                if (StringUtils.isNotBlank(modelId)) {
                    CustomPackLoader.MAID_MODELS.getInfo(modelId).ifPresent(modelItem ->
                            tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.photo.maid.desc",
                                    I18n.get(ParseI18n.getI18nKey(modelItem.getName()))).withStyle(TextFormatting.GRAY)
                            ));
                }
            }
        }
    }
}
