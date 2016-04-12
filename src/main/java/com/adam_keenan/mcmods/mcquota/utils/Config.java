package com.adam_keenan.mcmods.mcquota.utils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.Configuration;

public class Config {
    private static Configuration config;

    public static int quotaLength;

    private Config() {}
    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile(), "0.1");
        config.load();
        Log.info(ForgeModContainer.getConfig().getConfigFile().getName());

        quotaLength = config.getInt(
            "quotaLength",
            "global",
            /* 24 hours */
            24*60,
            /* 1 hour */
            1*60,
            /* 24 hours */
            24*60,
            "Set the default quota length for all users.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}
