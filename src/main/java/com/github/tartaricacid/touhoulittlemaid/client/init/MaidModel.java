package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.*;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

/**
 * 这个类似乎名称容易产生误解，
 * 它是加载该模组原版模型和材质的地方，因为这个模组主题是女仆，
 * 所以名字才叫 Maid，而不是因为它是加载女仆实体模型的地方，请谨记
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public final class MaidModel {
    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        // Tile Entity Special Renderer
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGarageKit.class, new TileEntityGarageKitRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrid.class, new TileEntityGridRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new TileEntityAltarRenderer());

        // Block Item
        Item.getItemFromBlock(MaidBlocks.GARAGE_KIT).setTileEntityItemStackRenderer(TileEntityItemStackGarageKitRenderer.INSTANCE);
        MaidItems.CHAIR.setTileEntityItemStackRenderer(TileEntityItemStackChairRenderer.INSTANCE);
        registerRender(Item.getItemFromBlock(MaidBlocks.GARAGE_KIT));
        registerRender(Item.getItemFromBlock(MaidBlocks.GRID));

        // Item
        registerRender(MaidItems.ULTRAMARINE_ORB_ELIXIR);
        registerRender(MaidItems.PROJECTILE_PROTECT_BAUBLE);
        registerRender(MaidItems.MAGIC_PROTECT_BAUBLE);
        registerRender(MaidItems.FIRE_PROTECT_BAUBLE);
        registerRender(MaidItems.EXPLOSION_PROTECT_BAUBLE);
        registerRender(MaidItems.FALL_PROTECT_BAUBLE);
        registerRender(MaidItems.DROWN_PROTECT_BAUBLE);
        registerRender(MaidItems.TOMBSTONE_BAUBLE);
        registerRender(MaidItems.MAID_BEACON);

        registerRender(MaidItems.KAPPA_COMPASS);
        registerRender(MaidItems.HAKUREI_GOHEI);
        registerRender(MaidItems.MARISA_BROOM);
        registerRender(MaidItems.CAMERA);
        registerRender(MaidItems.PHOTO);
        registerRender(MaidItems.CHAIR);
        registerRender(MaidItems.HATA_SASIMONO);
        registerRender(MaidItems.ALBUM);
        registerRender(MaidItems.SPELL_CARD);
    }

    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),
                        "inventory"));
    }
}
