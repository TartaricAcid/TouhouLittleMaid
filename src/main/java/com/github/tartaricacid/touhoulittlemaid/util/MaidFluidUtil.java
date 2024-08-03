package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.minecraft.core.registries.Registries;
import org.joml.Matrix4f;

import java.util.Optional;

public class MaidFluidUtil {
    public static FluidActionResult tankToBucket(ItemStack bucket, IFluidHandler tank, IItemHandler maidBackpack) {
        if (bucket.isEmpty()) {
            return FluidActionResult.FAILURE;
        }
        FluidActionResult filledSimulated = FluidUtil.tryFillContainer(bucket, tank, Integer.MAX_VALUE, null, false);
        if (filledSimulated.isSuccess()) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(maidBackpack, filledSimulated.getResult(), true);
            if (remainder.isEmpty()) {
                FluidActionResult filledReal = FluidUtil.tryFillContainer(bucket, tank, Integer.MAX_VALUE, null, true);
                ItemHandlerHelper.insertItemStacked(maidBackpack, filledReal.getResult(), false);
                bucket.shrink(1);
                return new FluidActionResult(bucket);
            }
        }
        return FluidActionResult.FAILURE;
    }

    public static FluidActionResult bucketToTank(ItemStack bucket, IFluidHandler tank, IItemHandler maidBackpack) {
        if (bucket.isEmpty()) {
            return FluidActionResult.FAILURE;
        }
        FluidActionResult emptiedSimulated = FluidUtil.tryEmptyContainer(bucket, tank, Integer.MAX_VALUE, null, false);
        if (emptiedSimulated.isSuccess()) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedSimulated.getResult(), true);
            if (remainder.isEmpty()) {
                FluidActionResult emptiedReal = FluidUtil.tryEmptyContainer(bucket, tank, Integer.MAX_VALUE, null, true);
                ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedReal.getResult(), false);
                bucket.shrink(1);
                return new FluidActionResult(bucket);
            }
        }
        return FluidActionResult.FAILURE;
    }
}
