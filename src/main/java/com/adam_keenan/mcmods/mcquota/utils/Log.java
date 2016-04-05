package com.adam_keenan.mcmods.mcquota.utils;

import com.adam_keenan.mcmods.mcquota.Info;
import cpw.mods.fml.common.FMLLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;


public class Log {

    private static final boolean DEBUG = true;

    public static void log(Level level, Object... objects) {
        FMLLog.log(level, "[%s]: %s", Info.NAME, StringUtils.join(objects, ", "));
    }

    public static void info(Object... objects) {
        if (DEBUG) {
            Log.log(Level.INFO, objects);
        }
    }

    public static void warning(Object... objects) {
        Log.log(Level.WARN, objects);
    }

    public static void severe(Object... objects) {
        Log.log(Level.ERROR, objects);
    }
}
