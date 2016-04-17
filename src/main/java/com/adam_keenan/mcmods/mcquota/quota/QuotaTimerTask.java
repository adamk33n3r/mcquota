package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import cpw.mods.fml.common.ModClassLoader;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.TimerTask;

public class QuotaTimerTask extends TimerTask {
    public QuotaTimerTask() {}

    @Override
    public void run() {
        for (Object obj : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP)obj;
            Log.info(String.format("Checking logs for %s", player.getDisplayName()));
            QuotaManager.getInstance().save(player);
            QuotaManager.getInstance().kickIfOverQuota(player);
            Timelog timelog = QuotaManager.getInstance().getTimelog(player);
            player.addChatMessage(new ChatComponentText(String.format("You have %d seconds left for today!", timelog.time_spent)));
        }
    }
}
