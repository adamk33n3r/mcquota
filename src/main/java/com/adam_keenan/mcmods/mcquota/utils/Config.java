package com.adam_keenan.mcmods.mcquota.utils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {
    private static Configuration config;

    public static Property globalEnabled;
    public static Property interval;
    public static Property quotaLength;

    private Config() {}
    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile(), "0.1");
        config.load();

        globalEnabled = config.get(
            "global",
            "enabled",
            true,
            "Enable global quota checks."
        );

        interval = config.get(
            "core",
            "interval",
            /* 5 minutes */
            5*60,
            "The interval to check for players quota length in seconds.",
            /* 1 minute */
            60,
            /* 24 hours */
            24*60*60
        );

        quotaLength = config.get(
            "global",
            "quotaLength",
            /* 1 hour */
            60*60,
            "The default quota length for all users in seconds.",
            /* 0 seconds */
            0,
            /* 24 hours */
            24*60*60
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
