package com.adam_keenan.mcmods.mcquota.utils;

import com.adam_keenan.mcmods.mcquota.Info;
import cpw.mods.fml.common.FMLLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;


public class Log {

    private static final boolean DEBUG = true;

    public static void log(Level level, String format, Object... objects) {
        if (FMLLog.getLogger() != null) {
            FMLLog.log(level, "<%s>: %s", Info.NAME, String.format(format, objects));
        } else {
            String msg = String.format("<%s>: %s", Info.NAME, String.format(format, objects));
            if (level.equals(Level.INFO)) {
                System.out.println(msg);
            } else {
                System.err.println(msg);
            }
        }
    }

    public static void info(Object... objects) {
        if (DEBUG) {
            Log.log(Level.INFO, Log.commaify(objects));
        }
    }

    public static void warning(Object... objects) {
        Log.log(Level.WARN, Log.commaify(objects));
    }

    public static void severe(Object... objects) {
        Log.log(Level.ERROR, Log.commaify(objects));
    }

    public static String commaify(Object... objects) {
        return StringUtils.join(objects, ", ");
    }
}
