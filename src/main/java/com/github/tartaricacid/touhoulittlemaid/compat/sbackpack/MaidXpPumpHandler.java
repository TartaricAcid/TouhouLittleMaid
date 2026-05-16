package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageFluidHandler;
import net.p3pp3rf1y.sophisticatedcore.init.ModFluids;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.tank.TankUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.xppump.AutomationDirection;
import net.p3pp3rf1y.sophisticatedcore.upgrades.xppump.XpPumpUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.p3pp3rf1y.sophisticatedcore.util.XpHelper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class MaidXpPumpHandler {
    private static final int COOLDOWN_TICKS = 5;
    private static final int DEFAULT_MAX_XP_PER_MENDING = 40;
    private int cooldownTicks = 0;

    public void tick(EntityMaid maid) {
        if (!SBackpackCompat.isLoaded()) {
            return;
        }
        if (maid.level().isClientSide) {
            return;
        }
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }
        cooldownTicks = COOLDOWN_TICKS;

        List<ItemStack> backpacks = findSBackpacksInCurios(maid);
        for (ItemStack backpack : backpacks) {
            processXpPumpForBackpack(maid, backpack);
        }
    }

    private List<ItemStack> findSBackpacksInCurios(EntityMaid maid) {
        List<ItemStack> backpacks = new ArrayList<>();
        CuriosApi.getCuriosInventory(maid).ifPresent(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                var stacksHandler = entry.getValue();
                if (stacksHandler != null) {
                    var stacks = stacksHandler.getStacks();
                    if (stacks != null) {
                        for (int i = 0; i < stacks.getSlots(); i++) {
                            ItemStack stack = stacks.getStackInSlot(i);
                            if (!stack.isEmpty() && stack.getItem() instanceof BackpackItem) {
                                backpacks.add(stack);
                            }
                        }
                    }
                }
            }
        });
        return backpacks;
    }

    private void processXpPumpForBackpack(EntityMaid maid, ItemStack backpack) {
        backpack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent(backpackWrapper -> {
            UpgradeHandler upgradeHandler = backpackWrapper.getUpgradeHandler();
            if (!upgradeHandler.hasUpgrade(XpPumpUpgradeItem.TYPE) || !upgradeHandler.hasUpgrade(TankUpgradeItem.TYPE)) {
                return;
            }

            ItemStack xpPumpStack = ItemStack.EMPTY;
            for (int i = 0; i < upgradeHandler.getSlots(); i++) {
                ItemStack stack = upgradeHandler.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof XpPumpUpgradeItem) {
                    xpPumpStack = stack;
                    break;
                }
            }
            if (xpPumpStack.isEmpty()) {
                return;
            }

            AutomationDirection direction = NBTHelper.getEnumConstant(xpPumpStack, "direction", AutomationDirection::fromName)
                    .orElse(AutomationDirection.INPUT);
            if (direction == AutomationDirection.OFF) {
                return;
            }

            int targetLevel = NBTHelper.getInt(xpPumpStack, "level").orElse(10);
            boolean shouldMend = NBTHelper.getBoolean(xpPumpStack, "mendItems").orElse(true);

            backpackWrapper.getFluidHandler().ifPresent(fluidHandler -> {
                int maidTotalXp = maid.getExperience();
                int targetXp = targetLevel * 120;

                if ((direction == AutomationDirection.INPUT || direction == AutomationDirection.KEEP)
                        && maidTotalXp > targetXp) {
                    int xpToStore = maidTotalXp - targetXp;
                    int liquidAmount = XpHelper.experienceToLiquid(xpToStore);
                    int filled = fluidHandler.fill(
                            ModFluids.EXPERIENCE_TAG, liquidAmount, ModFluids.XP_STILL.get(),
                            IFluidHandler.FluidAction.EXECUTE, false);
                    if (filled > 0) {
                        int actualXpStored = (int) XpHelper.liquidToExperience(filled);
                        maid.setExperience(maidTotalXp - actualXpStored);
                    }
                } else if ((direction == AutomationDirection.OUTPUT || direction == AutomationDirection.KEEP)
                        && maidTotalXp < targetXp) {
                    int xpToGive = targetXp - maidTotalXp;
                    int liquidAmount = XpHelper.experienceToLiquid(xpToGive);
                    FluidStack drained = fluidHandler.drain(
                            ModFluids.EXPERIENCE_TAG, liquidAmount,
                            IFluidHandler.FluidAction.EXECUTE, false);
                    if (!drained.isEmpty()) {
                        int actualXpGiven = (int) XpHelper.liquidToExperience(drained.getAmount());
                        maid.setExperience(maidTotalXp + actualXpGiven);
                    }
                }

                if (shouldMend) {
                    mendMaidItems(maid, fluidHandler);
                }
            });
        });
    }

    private void mendMaidItems(EntityMaid maid, IStorageFluidHandler fluidHandler) {
        List<ItemStack> itemsWithMending = new ArrayList<>();
        maid.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(allItems -> {
            for (int i = 0; i < allItems.getSlots(); i++) {
                ItemStack stack = allItems.getStackInSlot(i);
                if (!stack.isEmpty() && stack.isDamaged() && stack.getEnchantmentLevel(Enchantments.MENDING) > 0) {
                    itemsWithMending.add(stack);
                }
            }
        });

        if (itemsWithMending.isEmpty()) {
            return;
        }

        RandomSource randomSource = maid.getRandom();
        ItemStack itemToMend = itemsWithMending.get(randomSource.nextInt(itemsWithMending.size()));
        if (itemToMend.isEmpty() || !itemToMend.isDamaged()) {
            return;
        }

        float xpRepairRatio = itemToMend.getXpRepairRatio();
        if (xpRepairRatio <= 0) {
            return;
        }

        int maxXpPerMending = DEFAULT_MAX_XP_PER_MENDING;
        int durabilityToRepair = itemToMend.getDamageValue();
        float xpToTryDrain = Math.min(maxXpPerMending, durabilityToRepair / xpRepairRatio);

        if (xpToTryDrain > 0) {
            FluidStack drained = fluidHandler.drain(
                    ModFluids.EXPERIENCE_TAG, XpHelper.experienceToLiquid(xpToTryDrain),
                    IFluidHandler.FluidAction.EXECUTE, false);
            if (!drained.isEmpty()) {
                float xpDrained = XpHelper.liquidToExperience(drained.getAmount());
                int actualDurabilityRepair = (int) (xpDrained * xpRepairRatio);
                itemToMend.setDamageValue(Math.max(0, itemToMend.getDamageValue() - actualDurabilityRepair));
            }
        }
    }
}