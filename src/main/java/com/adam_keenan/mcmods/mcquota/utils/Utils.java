package com.adam_keenan.mcmods.mcquota.utils;

import net.minecraft.entity.player.EntityPlayer;

public class Utils {
    public static String playerToUUID(EntityPlayer player) {
        return player.getUniqueID().toString();
    }
}
