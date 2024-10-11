package com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaLootTables;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class AquacultureFishingHook extends MaidFishingHook implements IEntityAdditionalSpawnData {
    public static final EntityType<AquacultureFishingHook> TYPE = EntityType.Builder.<AquacultureFishingHook>of(AquacultureFishingHook::new, MobCategory.MISC)
            .noSave().noSummon().sized(0.25F, 0.25F)
            .clientTrackingRange(4).updateInterval(5)
            .build("aquaculture_fishing_hook");

    private Hook hook = Hooks.EMPTY;
    private ItemStack fishingLine = ItemStack.EMPTY;
    private ItemStack bobber = ItemStack.EMPTY;
    private ItemStack fishingRod = ItemStack.EMPTY;

    public AquacultureFishingHook(EntityType<AquacultureFishingHook> entityType, Level level) {
        super(entityType, level, 0, 0);
    }

    public AquacultureFishingHook(EntityMaid maid, Level world, int luck, int lureSpeed, Vec3 pos,
                                  @Nonnull Hook hook, @Nonnull ItemStack fishingLine, @Nonnull ItemStack bobber, @Nonnull ItemStack rod) {
        super(TYPE, world, luck, lureSpeed);
        this.setOwner(maid);
        this.moveTo(pos);
        this.hook = hook;
        this.fishingLine = fishingLine;
        this.bobber = bobber;
        this.fishingRod = rod;
        if (this.hasHook() && hook.getWeight() != null) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(hook.getWeight()));
        }
    }

    @Override
    protected float getFluidHeight(FluidState fluidState, BlockPos blockPos) {
        if (this.isLavaHook() && fluidState.is(FluidTags.LAVA)) {
            return fluidState.getHeight(this.level(), blockPos);
        }
        return super.getFluidHeight(fluidState, blockPos);
    }

    @Override
    protected void fallTick(FluidState fluidState) {
        if (this.isLavaHook() && !fluidState.is(FluidTags.LAVA)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        } else {
            super.fallTick(fluidState);
        }
    }

    @Override
    protected void spawnFishingParticle(ServerLevel level, BlockState blockState, double x, double y, double z, float sin, float cos) {
        if (this.isLavaHook() && blockState.getFluidState().is(FluidTags.LAVA)) {
            float sinOffset = sin * 0.04F;
            float cosOffset = cos * 0.04F;
            if (this.random.nextFloat() < 0.15F) {
                level.sendParticles(ParticleTypes.LAVA, x, y - 0.1, z, 1, sin, 0.1D, cos, 0.0D);
            }
            level.sendParticles(ParticleTypes.SMOKE, x, y, z, 0, cosOffset, 0.01D, -sinOffset, 1.0D);
            level.sendParticles(ParticleTypes.SMOKE, x, y, z, 0, -cosOffset, 0.01D, sinOffset, 1.0D);
        } else {
            super.spawnFishingParticle(level, blockState, x, y, z, sin, cos);
        }
        EntityMaid maidOwner = this.getMaidOwner();
        if (this.hasHook() && this.hook.getCatchSound() != null && maidOwner != null) {
            this.level.playSound(null, maidOwner.blockPosition(), this.hook.getCatchSound(), this.getSoundSource(), 0.1F, 0.1F);
        }
    }

    @Override
    protected void spawnNibbleParticle(ServerLevel level) {
        if (level.getFluidState(this.blockPosition()).is(FluidTags.WATER)) {
            super.spawnNibbleParticle(level);
        } else if (level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            Vec3 motion = this.getDeltaMovement();
            double boundingBox = this.getBoundingBox().minY + 0.5D;
            float bbWidth = this.getBbWidth();

            this.setDeltaMovement(motion.x, (-0.4F * Mth.nextFloat(this.random, 0.6F, 1.0F)), motion.z);
            this.playSound(AquaSounds.BOBBER_LAND_IN_LAVA.get(), 1.00F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            level.sendParticles(ParticleTypes.LAVA, this.getX(), boundingBox, this.getZ(), (int) (1.0F + bbWidth * 20.0F), bbWidth, 0.0D, bbWidth, 0.2D);
        }
    }

    @Override
    protected void addExtraLoot(List<ItemStack> randomItems) {
        // 如果空军，那么添加一些东西
        if (randomItems.isEmpty()) {
            if (level.dimension() == Level.END) {
                randomItems.add(new ItemStack(AquaItems.FISH_BONES.get()));
            } else {
                if (!level.isEmptyBlock(this.blockPosition()) && (level.getFluidState(this.blockPosition()).isSource())) {
                    randomItems.add(new ItemStack(Items.COD));
                }
            }
        }
    }

    @Override
    protected @NotNull List<ItemStack> getLoot(MinecraftServer server, LootParams lootParams) {
        List<ItemStack> loot = this.getAquaLoot(server, lootParams);

        // 如果双倍钓钩
        if (this.hasHook() && this.hook.getDoubleCatchChance() > 0) {
            if (this.random.nextDouble() <= this.hook.getDoubleCatchChance()) {
                List<ItemStack> doubleLoot = this.getAquaLoot(server, lootParams);
                loot.addAll(doubleLoot);
            }
        }

        return loot;
    }

    private List<ItemStack> getAquaLoot(MinecraftServer server, LootParams lootParams) {
        ResourceLocation lootTableLocation;
        if (this.isLavaHook() && this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            if (this.level.dimensionType().hasCeiling()) {
                lootTableLocation = AquaLootTables.NETHER_FISHING;
            } else {
                lootTableLocation = AquaLootTables.LAVA_FISHING;
            }
        } else {
            lootTableLocation = BuiltInLootTables.FISHING;
        }
        LootTable lootTable = server.getLootData().getLootTable(lootTableLocation);
        return lootTable.getRandomItems(lootParams);
    }

    @Override
    protected void afterFishing() {
        ItemStackHandler rodHandler = AquaFishingRodItem.getHandler(this.fishingRod);
        ItemStack bait = rodHandler.getStackInSlot(1);
        if (!bait.isEmpty()) {
            if (bait.hurt(1, level.random, null)) {
                bait.shrink(1);
                this.playSound(AquaSounds.BOBBER_BAIT_BREAK.get(), 0.7F, 0.2F);
            }
            rodHandler.setStackInSlot(1, bait);
        }
    }

    @Override
    protected void hurtRod(EntityMaid maid, ItemStack rodItem, int rodDamage) {
        int currentDamage = rodItem.getMaxDamage() - rodItem.getDamageValue();
        if (rodDamage >= currentDamage) {
            rodDamage = currentDamage;
        }
        if (hook != Hooks.EMPTY && hook.getDurabilityChance() > 0) {
            if (level.random.nextDouble() >= hook.getDurabilityChance()) {
                rodItem.hurt(rodDamage, level.random, null);
            }
        } else {
            rodItem.hurt(rodDamage, level.random, null);
        }
    }

    protected boolean isLavaHook() {
        return this.hasHook() && this.hook.getFluids().contains(FluidTags.LAVA);
    }

    public boolean hasHook() {
        return this.hook != Hooks.EMPTY;
    }

    public Hook getHook() {
        return hook;
    }

    @Nonnull
    public ItemStack getBobber() {
        return this.bobber;
    }

    public boolean hasBobber() {
        return !this.getBobber().isEmpty();
    }

    @Nonnull
    public ItemStack getFishingLine() {
        return this.fishingLine;
    }

    @Override
    public void lavaHurt() {
        if (!this.isLavaHook()) {
            super.lavaHurt();
        }
    }

    @Override
    public boolean displayFireAnimation() {
        return (this.hasHook() && !this.hook.getFluids().contains(FluidTags.LAVA)) && super.displayFireAnimation();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        Entity owner = this.getOwner();
        buffer.writeVarInt(owner == null ? this.getId() : owner.getId());
        buffer.writeUtf(this.hook.getName() == null ? "" : this.hook.getName());
        buffer.writeItem(this.fishingLine);
        buffer.writeItem(this.bobber);
        buffer.writeItem(this.fishingRod);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        Entity owner = this.level.getEntity(buffer.readVarInt());
        this.setOwner(owner);
        String hookName = buffer.readUtf();
        if (StringUtils.isNoneBlank(hookName)) {
            Item hookItem = Hook.HOOKS.get(hookName).get();
            this.hook = ((HookItem) hookItem).getHookType();
        }
        this.fishingLine = buffer.readItem();
        this.bobber = buffer.readItem();
        this.fishingRod = buffer.readItem();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
