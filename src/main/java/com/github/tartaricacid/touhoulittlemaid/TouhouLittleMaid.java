package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.internal.LittleMaidAPIImpl;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TouhouLittleMaid.MOD_ID, name = TouhouLittleMaid.MOD_NAME,
        acceptedMinecraftVersions = "[1.12]", version = TouhouLittleMaid.VERSION,
        dependencies = "required-after:forge@[14.23.5.2768,);")
public class TouhouLittleMaid {
    public static final String MOD_ID = "touhou_little_maid";
    public static final String MOD_NAME = "Touhou Little Maid";
    public static final String VERSION = "@VERSION@";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.Instance
    public static TouhouLittleMaid INSTANCE;

    @SidedProxy(serverSide = "com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy",
            clientSide = "com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static boolean MCMPCompat = false;

    /**
     * 设置 API 对象
     */
    public TouhouLittleMaid() {
        LittleMaidAPI.setInstance(new LittleMaidAPIImpl());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
