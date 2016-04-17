package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHooks {
    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            Log.info("Player logged in: " + player.getDisplayName(), player.getUniqueID());

            QuotaManager.getInstance().playerJoined(player);
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            Log.info("Player logged out: " + player.getDisplayName(), player.getUniqueID());
            QuotaManager.getInstance().playerLeft(player);
            Log.info(String.format("time_spent is now %s", QuotaManager.getInstance().getTimelog(player).time_spent));
        }
    }
}
