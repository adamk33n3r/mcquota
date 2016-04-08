package com.adam_keenan.mcmods.mcquota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventHooks {
    @SubscribeEvent
    public void entityJoined(EntityJoinWorldEvent event) {
        FMLNetworkEvent.ClientConnectedToServerEvent
        // Probably unnecessary but better safe than sorry.
        if (event.entity instanceof EntityPlayerMP) {
            Log.info("Player connected: " + ((EntityPlayerMP) event.entity).getDisplayName());
//            ((EntityPlayerMP) event.entity).playerNetServerHandler.kickPlayerFromServer("Your time is up for the day.");
//            net.minecraft.server.management.UserList;
        }
    }
}
