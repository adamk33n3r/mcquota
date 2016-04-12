package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.quota.Timelog;
import com.adam_keenan.mcmods.mcquota.utils.Config;
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
            QuotaManager.getInstance().join(player.getUniqueID().toString());
            Timelog timelog = QuotaManager.getInstance().getLog(player.getUniqueID().toString());
            if (timelog != null) {
                Log.info("timelog exists. checking against quota");
                Log.info(String.format("%s >= %s = %s", timelog.time_spent, Config.quotaLength, timelog.time_spent >= Config.quotaLength));
                if (timelog.time_spent >= Config.quotaLength) {
                    Log.info("time_spent is >= quotaLength");
                    player.playerNetServerHandler.kickPlayerFromServer("Your time is up for the day.");
                } else {
                    Log.info("still have time left");
                }
            } else {
                Log.info("no timelog exists");
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            Log.info("Player logged out: " + player.getDisplayName(), player.getUniqueID());
            QuotaManager.getInstance().leave(player.getUniqueID().toString());
            Log.info(String.format("time_spent is now %s", QuotaManager.getInstance().getLog(player.getUniqueID().toString()).time_spent));
        }
    }
}
