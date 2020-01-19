package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import net.minecraft.client.renderer.block.model.ModelBakery;
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
        registerRender(Item.getItemFromBlock(MaidBlocks.GASHAPON_MACHINES));

        // Item
        registerRender(MaidItems.ULTRAMARINE_ORB_ELIXIR);
        registerRender(MaidItems.PROJECTILE_PROTECT_BAUBLE);
        registerRender(MaidItems.MAGIC_PROTECT_BAUBLE);
        registerRender(MaidItems.FIRE_PROTECT_BAUBLE);
        registerRender(MaidItems.EXPLOSION_PROTECT_BAUBLE);
        registerRender(MaidItems.FALL_PROTECT_BAUBLE);
        registerRender(MaidItems.DROWN_PROTECT_BAUBLE);
        registerRender(MaidItems.TOMBSTONE_BAUBLE);
        registerRender(MaidItems.SUBSTITUTE_JIZO);
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
        registerRender(MaidItems.DEBUG_DANMAKU);
        registerRender(MaidItems.NPC_MAID_TOOL);
        registerRender(MaidItems.GASHAPON_COIN);

        ModelResourceLocation maidModelCoupon1 = new ModelResourceLocation(MaidItems.MAID_MODEL_COUPON.getRegistryName() + "_1", "inventory");
        ModelResourceLocation maidModelCoupon2 = new ModelResourceLocation(MaidItems.MAID_MODEL_COUPON.getRegistryName() + "_2", "inventory");
        ModelResourceLocation maidModelCoupon3 = new ModelResourceLocation(MaidItems.MAID_MODEL_COUPON.getRegistryName() + "_3", "inventory");
        ModelResourceLocation maidModelCoupon4 = new ModelResourceLocation(MaidItems.MAID_MODEL_COUPON.getRegistryName() + "_4", "inventory");
        ModelResourceLocation maidModelCoupon5 = new ModelResourceLocation(MaidItems.MAID_MODEL_COUPON.getRegistryName() + "_5", "inventory");
        ModelBakery.registerItemVariants(MaidItems.MAID_MODEL_COUPON, maidModelCoupon1, maidModelCoupon2,
                maidModelCoupon3, maidModelCoupon4, maidModelCoupon5);
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_MODEL_COUPON, stack -> {
            switch (stack.getMetadata()) {
                default:
                    return maidModelCoupon1;
                case 1:
                    return maidModelCoupon1;
                case 2:
                    return maidModelCoupon2;
                case 3:
                    return maidModelCoupon3;
                case 4:
                    return maidModelCoupon4;
                case 5:
                    return maidModelCoupon5;
            }
        });

        ModelResourceLocation gashapon1 = new ModelResourceLocation(MaidItems.GASHAPON.getRegistryName() + "_1", "inventory");
        ModelResourceLocation gashapon2 = new ModelResourceLocation(MaidItems.GASHAPON.getRegistryName() + "_2", "inventory");
        ModelResourceLocation gashapon3 = new ModelResourceLocation(MaidItems.GASHAPON.getRegistryName() + "_3", "inventory");
        ModelResourceLocation gashapon4 = new ModelResourceLocation(MaidItems.GASHAPON.getRegistryName() + "_4", "inventory");
        ModelResourceLocation gashapon5 = new ModelResourceLocation(MaidItems.GASHAPON.getRegistryName() + "_5", "inventory");
        ModelBakery.registerItemVariants(MaidItems.GASHAPON, gashapon1, gashapon2,
                gashapon3, gashapon4, gashapon5);
        ModelLoader.setCustomMeshDefinition(MaidItems.GASHAPON, stack -> {
            switch (stack.getMetadata()) {
                default:
                    return gashapon1;
                case 1:
                    return gashapon1;
                case 2:
                    return gashapon2;
                case 3:
                    return gashapon3;
                case 4:
                    return gashapon4;
                case 5:
                    return gashapon5;
            }
        });

        ModelResourceLocation maidBackPack1 = new ModelResourceLocation(MaidItems.MAID_BACKPACK.getRegistryName() + "_1", "inventory");
        ModelResourceLocation maidBackPack2 = new ModelResourceLocation(MaidItems.MAID_BACKPACK.getRegistryName() + "_2", "inventory");
        ModelResourceLocation maidBackPack3 = new ModelResourceLocation(MaidItems.MAID_BACKPACK.getRegistryName() + "_3", "inventory");
        ModelBakery.registerItemVariants(MaidItems.MAID_BACKPACK, maidBackPack1, maidBackPack2, maidBackPack3);
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_BACKPACK, stack -> {
            switch (EntityMaid.EnumBackPackLevel.getEnumLevelByNum(stack.getMetadata())) {
                default:
                    return maidBackPack1;
                case EMPTY:
                    return maidBackPack1;
                case SMALL:
                    return maidBackPack1;
                case MIDDLE:
                    return maidBackPack2;
                case BIG:
                    return maidBackPack3;
            }
        });
    }

    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),
                        "inventory"));
    }
}
