package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemFilm extends Item {
    public static final String MAID_INFO = "MaidInfo";
    private static final String CUSTOM_NAME = "CustomName";

    public ItemFilm() {
        super((new Item.Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    public static ItemStack maidToFilm(EntityMaid maid) {
        ItemStack film = InitItems.FILM.get().getDefaultInstance();
        CompoundTag filmTag = new CompoundTag();
        CompoundTag maidTag = new CompoundTag();
        maid.setHomeModeEnable(false);
        maid.saveWithoutId(maidTag);
        removeMaidSomeData(maidTag);
        maidTag.putString("id", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(InitEntities.MAID.get())).toString());
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

    private static CompoundTag getMaidData(ItemStack stack) {
        if (hasMaidData(stack)) {
            return Objects.requireNonNull(stack.getTag()).getCompound(MAID_INFO);
        }
        return new CompoundTag();
    }

    private static void removeMaidSomeData(CompoundTag nbt) {
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
        nbt.remove("Pos");
        nbt.remove("Motion");
        nbt.remove("FallDistance");
        nbt.remove("Fire");
        nbt.remove("Air");
        nbt.remove("TicksFrozen");
        nbt.remove("HasVisualFire");
        nbt.remove("Passengers");
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
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!hasMaidData(stack)) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.film.no_data.desc").withStyle(ChatFormatting.DARK_RED));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundTag maidData = getMaidData(stack);
        if (maidData.contains(EntityMaid.MODEL_ID_TAG, Tag.TAG_STRING)) {
            String modelId = maidData.getString(EntityMaid.MODEL_ID_TAG);
            String customName = "";
            if (maidData.contains(CUSTOM_NAME, Tag.TAG_STRING)) {
                customName = maidData.getString(CUSTOM_NAME);
            }
            return Optional.of(new ItemMaidTooltip(modelId, customName));
        }
        return Optional.empty();
    }
}
