package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.utils.ConfigurationManager;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Info.MODID, name = Info.NAME , version = Info.VERSION, acceptableRemoteVersions = "*")
public class MCQuota
{
    private ConfigurationManager configManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Log.info("preInit");
        MinecraftForge.EVENT_BUS.register(new EventHooks());
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        this.configManager = new ConfigurationManager(config);
        //QuotaManager qm = new QuotaManager();
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
