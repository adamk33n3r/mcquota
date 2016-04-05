package com.adam_keenan.mcmods.mcquota.utils;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by Adam on 4/5/2016.
 */
public class ConfigurationManager {
    private Configuration config;

    public ConfigurationManager(Configuration config) {
        this.config = config;
        this.config.load();

        this.config.getInt("quotaLength", "global", 24*60, 1*60, 24*60, "Set the default quota length for all users.");

        this.config.save();
    }
}
