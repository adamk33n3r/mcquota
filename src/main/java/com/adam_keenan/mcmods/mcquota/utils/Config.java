package com.adam_keenan.mcmods.mcquota.utils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.Configuration;

public class Config {
    private static Configuration config;

    public static int interval;
    public static int quotaLength;

    private Config() {}
    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile(), "0.1");
        config.load();

        interval = config.getInt(
            "interval",
            "global",
            /* 5 minutes */
            5*60,
            /* 1 minute */
            60,
            /* 24 hours */
            24*60*60,
            "The interval to check for players quota length in seconds."
        );

        quotaLength = config.getInt(
            "quotaLength",
            "global",
            /* 1 hour */
            60*60,
            /* 0 seconds */
            0,
            /* 24 hours */
            24*60*60,
            "The default quota length for all users in seconds."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void save() {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
