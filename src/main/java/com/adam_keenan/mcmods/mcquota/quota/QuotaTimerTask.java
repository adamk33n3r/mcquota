package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.Utils;
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
            int timeLeft = -1;
            if (Config.globalEnabled.getBoolean()) {
                timeLeft = Config.quotaLength.getInt() - timelog.time_spent;
            } else {
                int playerQuota = QuotaDBManager.getInstance().getPlayerQuota(Utils.playerToUUID(player));
                if (playerQuota >= 0) {
                    timeLeft = playerQuota - timelog.time_spent;
                }
            }
            if (timeLeft >= 0) {
                player.addChatMessage(new ChatComponentText(String.format("You have %d seconds left for today!", timeLeft)));
            }
        }
    }
}
