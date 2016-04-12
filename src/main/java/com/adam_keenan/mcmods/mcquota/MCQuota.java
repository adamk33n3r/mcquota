package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Info.MODID, name = Info.NAME , version = Info.VERSION, acceptableRemoteVersions = "*")
public class MCQuota
{
    private Config config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Log.info("preInit");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            FMLCommonHandler.instance().exitJava(-1, true);
        }
        MinecraftForge.EVENT_BUS.register(new EventHooks());
        FMLCommonHandler.instance().bus().register(new EventHooks());
        Config.load(event);
        Log.info(String.format("Global quota length is %s", Config.quotaLength));
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Log.info("init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Log.info("postInit");
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        Log.info("serverLoad");
        event.registerServerCommand(new CommandQuota());
    }
}
