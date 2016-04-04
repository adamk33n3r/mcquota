package com.adam_keenan.mcmods.mcquota;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHooks {
    @cpw.mods.fml.common.Mod.EventHandler
    public void entityJoined(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayer) {
            FMLLog.info(event.getClass().getName());
            FMLLog.info(((EntityPlayer) event.entity).getDisplayName());
        }
    }
}
