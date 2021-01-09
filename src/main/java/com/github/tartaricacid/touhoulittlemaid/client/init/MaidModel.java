package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity.*;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.*;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType.JOYS;
import static com.github.tartaricacid.touhoulittlemaid.util.ItemRenderRegisterUtils.*;

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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStatue.class, new TileEntityStatueRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMaidJoy.class, new TileEntityMaidJoyRenderer());

        // Block Item
        Item.getItemFromBlock(MaidBlocks.GARAGE_KIT).setTileEntityItemStackRenderer(TileEntityItemStackGarageKitRenderer.INSTANCE);
        MaidItems.CHAIR.setTileEntityItemStackRenderer(TileEntityItemStackChairRenderer.INSTANCE);
        MaidItems.SPELL_CARD.setTileEntityItemStackRenderer(TileEntityItemStackSpellCardRenderer.INSTANCE);

        registerRender(MaidBlocks.GARAGE_KIT);
        registerRender(MaidBlocks.GRID);
        registerRender(MaidBlocks.GASHAPON_MACHINES);

        // Item
        registerRender(MaidItems.ULTRAMARINE_ORB_ELIXIR);
        registerRender(MaidItems.PROJECTILE_PROTECT_BAUBLE);
        registerRender(MaidItems.MAGIC_PROTECT_BAUBLE);
        registerRender(MaidItems.FIRE_PROTECT_BAUBLE);
        registerRender(MaidItems.EXPLOSION_PROTECT_BAUBLE);
        registerRender(MaidItems.FALL_PROTECT_BAUBLE);
        registerRender(MaidItems.DROWN_PROTECT_BAUBLE);
        registerRender(MaidItems.TOMBSTONE_BAUBLE);
        registerRender(MaidItems.NIMBLE_FABRIC);
        registerRender(MaidItems.SUBSTITUTE_JIZO);
        registerRender(MaidItems.WIRELESS_IO);
        registerRender(MaidItems.ITEM_MAGNET_BAUBLE);
        registerRender(MaidItems.MUTE_BAUBLE);

        registerRender(MaidItems.KAPPA_COMPASS);
        registerRender(MaidItems.MARISA_BROOM);
        registerRender(MaidItems.PHOTO);
        registerRender(MaidItems.CHAIR);
        registerRender(MaidItems.HATA_SASIMONO);
        registerRender(MaidItems.ALBUM);
        registerRender(MaidItems.SPELL_CARD);
        registerRender(MaidItems.DEBUG_DANMAKU);
        registerRender(MaidItems.NPC_MAID_TOOL);
        registerRender(MaidItems.GASHAPON_COIN);
        registerRender(MaidItems.SUITCASE);
        registerRender(MaidItems.TROLLEY_AUDIO);
        registerRender(MaidItems.SCARECROW);
        registerRender(MaidItems.PORTABLE_AUDIO);
        registerRender(MaidItems.CHISEL);
        registerRender(MaidItems.POWER_POINT);
        registerRender(MaidItems.MAID_BED);
        registerRender(MaidItems.HAMMER);

        register2d3dRender(MaidItems.HAKUREI_GOHEI);
        register2d3dRender(MaidItems.MAID_BEACON);
        register2d3dRender(MaidItems.CAMERA);
        register2d3dRender(MaidItems.EXTINGUISHER);
        registerReplaceRender(Items.TOTEM_OF_UNDYING,
                new ModelResourceLocation("totem", "inventory"),
                getModelRl(TouhouLittleMaid.MOD_ID, "life_point"),
                () -> GeneralConfig.VANILLA_CONFIG.changeTotemModel
        );

        ModelResourceLocation maidModelCoupon1 = getModelRl(MaidItems.MAID_MODEL_COUPON, 1);
        ModelResourceLocation maidModelCoupon2 = getModelRl(MaidItems.MAID_MODEL_COUPON, 2);
        ModelResourceLocation maidModelCoupon3 = getModelRl(MaidItems.MAID_MODEL_COUPON, 3);
        ModelResourceLocation maidModelCoupon4 = getModelRl(MaidItems.MAID_MODEL_COUPON, 4);
        ModelResourceLocation maidModelCoupon5 = getModelRl(MaidItems.MAID_MODEL_COUPON, 5);
        ModelBakery.registerItemVariants(MaidItems.MAID_MODEL_COUPON, maidModelCoupon1, maidModelCoupon2,
                maidModelCoupon3, maidModelCoupon4, maidModelCoupon5);
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_MODEL_COUPON, stack -> {
            switch (stack.getMetadata()) {
                default:
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

        ModelResourceLocation gashapon1 = getModelRl(MaidItems.GASHAPON, 1);
        ModelResourceLocation gashapon2 = getModelRl(MaidItems.GASHAPON, 2);
        ModelResourceLocation gashapon3 = getModelRl(MaidItems.GASHAPON, 3);
        ModelResourceLocation gashapon4 = getModelRl(MaidItems.GASHAPON, 4);
        ModelResourceLocation gashapon5 = getModelRl(MaidItems.GASHAPON, 5);
        ModelBakery.registerItemVariants(MaidItems.GASHAPON, gashapon1, gashapon2,
                gashapon3, gashapon4, gashapon5);
        ModelLoader.setCustomMeshDefinition(MaidItems.GASHAPON, stack -> {
            switch (stack.getMetadata()) {
                default:
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

        ModelResourceLocation maidBackPack1 = getModelRl(MaidItems.MAID_BACKPACK, 1);
        ModelResourceLocation maidBackPack2 = getModelRl(MaidItems.MAID_BACKPACK, 2);
        ModelResourceLocation maidBackPack3 = getModelRl(MaidItems.MAID_BACKPACK, 3);
        ModelBakery.registerItemVariants(MaidItems.MAID_BACKPACK, maidBackPack1, maidBackPack2, maidBackPack3);
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_BACKPACK, stack -> {
            switch (EntityMaid.EnumBackPackLevel.getEnumLevelByNum(stack.getMetadata())) {
                default:
                case EMPTY:
                case SMALL:
                    return maidBackPack1;
                case MIDDLE:
                    return maidBackPack2;
                case BIG:
                    return maidBackPack3;
            }
        });

        List<ModelResourceLocation> maidVehicle = new ArrayList<>();
        for (EntityMaidVehicle.Type type : EntityMaidVehicle.Type.values()) {
            maidVehicle.add(getModelRl(TouhouLittleMaid.MOD_ID, type.name().toLowerCase(Locale.US)));
        }
        ModelBakery.registerItemVariants(MaidItems.MAID_VEHICLE, maidVehicle.toArray(new ModelResourceLocation[]{}));
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_VEHICLE,
                stack -> maidVehicle.get(MathHelper.clamp(stack.getMetadata(), 0, EntityMaidVehicle.Type.values().length - 1)));

        ModelResourceLocation[] maidJoy = new ModelResourceLocation[JOYS.size()];
        for (String type : JOYS.keySet()) {
            maidJoy[JOYS.get(type).getIndex()] = getModelRl(TouhouLittleMaid.MOD_ID, type);
        }
        ModelBakery.registerItemVariants(MaidItems.MAID_JOY, maidJoy);
        ModelLoader.setCustomMeshDefinition(MaidItems.MAID_JOY, (stack -> {
            JoyType type = JOYS.get(ItemMaidJoy.getType(stack));
            if (type != null) {
                return maidJoy[type.getIndex()];
            } else {
                return maidJoy[0];
            }
        }));

        ModelResourceLocation smartSlabInit = getModelRl(MaidItems.SMART_SLAB, 0);
        ModelResourceLocation smartSlabEmpty = getModelRl(MaidItems.SMART_SLAB, 1);
        ModelResourceLocation smartSlabFull = getModelRl(MaidItems.SMART_SLAB, 2);
        ModelBakery.registerItemVariants(MaidItems.SMART_SLAB, smartSlabInit, smartSlabEmpty, smartSlabFull);
        ModelLoader.setCustomMeshDefinition(MaidItems.SMART_SLAB, stack -> {
            switch (stack.getMetadata()) {
                case 0:
                    return smartSlabInit;
                case 1:
                    return smartSlabEmpty;
                case 2:
                default:
                    return smartSlabFull;
            }
        });
    }
}
