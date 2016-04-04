package com.adam_keenan.mcmods.mcquota;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = MCQuota.MODID, name = MCQuota.NAME , version = MCQuota.VERSION, acceptableRemoteVersions = "*")
public class MCQuota
{
    public static final String MODID   = "mcquota";
    public static final String NAME    = "MCQuota";
    public static final String VERSION = "1.7.10-1.0.0";

    @EventHandler
    public void preInit(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHooks());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }

    @EventHandler
    public void postInit(FMLInitializationEvent event)
    {
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandQuota());
    }
}
