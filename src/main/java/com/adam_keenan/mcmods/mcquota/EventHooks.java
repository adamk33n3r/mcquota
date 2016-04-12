package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.quota.Timelog;
import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHooks {
    @SubscribeEvent
    public void entityJoined(EntityJoinWorldEvent event) {
        // Probably unnecessary but better safe than sorry.
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            QuotaManager.getInstance().join(player.getUniqueID().toString());
            Log.info("Player connected: " + player.getDisplayName(), player.getUniqueID());
            Timelog timelog = QuotaManager.getInstance().getLog(player.getUniqueID().toString());
            if (timelog != null) {
                Log.info("timelog exists. checking against quota");
                if (timelog.time_spent >= Config.quotaLength) {
                    Log.info("time_spent is >= quotaLength");
                    player.playerNetServerHandler.kickPlayerFromServer("Your time is up for the day.");
                } else {
                    Log.info("still have time left");
                }
            } else {
                Log.info("no timelog exists");
//                net.minecraftforge.event.entity.
            }
//            net.minecraft.server.management.UserList;
        }
    }

    @SubscribeEvent
    public void entityLeft(PlayerEvent.SaveToFile event) {
        Log.info("!!!save to file!!!");
    }

    @SubscribeEvent
    public void playerLoggedIn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        Log.info("player logged in", event.player.getDisplayName());
    }

    @SubscribeEvent
    public void playerLoggedOut(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        Log.info("player logged out", event.player.getDisplayName());
    }
}
