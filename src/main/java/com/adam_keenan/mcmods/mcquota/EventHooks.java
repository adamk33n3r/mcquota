package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHooks {
    @SubscribeEvent
    public void entityJoined(EntityJoinWorldEvent event) {
        // Probably unnecessary but better safe than sorry.
        if (event.entity instanceof EntityPlayer) {
            Log.info("Player connected: " + ((EntityPlayer) event.entity).getDisplayName());
            event.setCanceled(true);
        }
    }
}
