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

        quotaLength = config.getInt(
            "quotaLength",
            "global",
            /* 24 hours */
            24*60*60,
            /* 1 minute */
            60,
            /* 24 hours */
            24*60*60,
            "The default quota length for all users in seconds.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}
