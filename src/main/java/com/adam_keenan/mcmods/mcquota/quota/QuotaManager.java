package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Timer;

public class QuotaManager {
    private static QuotaManager instance = new QuotaManager();

    private Timer timer;

    public static QuotaManager getInstance() {
        return instance;
    }

    private QuotaManager() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new QuotaTimerTask(), Config.interval.getInt()*1000, Config.interval.getInt()*1000);
    }

    @Override
    protected void finalize() {
        this.timer.cancel();
    }

    public void playerJoined(EntityPlayer player) {
        QuotaDBManager.getInstance().updateLoginTime(Utils.playerToUUID(player));
        this.kickIfOverQuota(player);
    }

    public void playerLeft(EntityPlayer player) {
        QuotaDBManager.getInstance().updateTimeSpent(Utils.playerToUUID(player));
    }

    public void save(EntityPlayer player) {
        String uuid = Utils.playerToUUID(player);
        QuotaDBManager.getInstance().updateTimeSpent(uuid);
        QuotaDBManager.getInstance().updateLoginTime(uuid);
    }

    public Timelog getTimelog(EntityPlayer player) {
        return QuotaDBManager.getInstance().getTimelog(Utils.playerToUUID(player));
    }

    public boolean kickIfOverQuota(EntityPlayer player) {
        Timelog timelog = this.getTimelog(player);
        if (timelog != null) {
            int playerQuota = QuotaDBManager.getInstance().getPlayerQuota(Utils.playerToUUID(player));
            int quotaToCheck = -1;
            if (Config.globalEnabled.getBoolean()) {
                quotaToCheck = Config.quotaLength.getInt();
            }
            if (playerQuota >= 0) {
                Log.info("player has quota");
                quotaToCheck = playerQuota;
            }
            Log.info("timelog exists. checking against quota");
            Log.info(String.format("%s >= %s = %s", timelog.time_spent, quotaToCheck, timelog.time_spent >= quotaToCheck));
            if (quotaToCheck > 0 && timelog.time_spent >= quotaToCheck) {
                Log.info("time_spent is >= quotaLength");
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Your time is up for the day.");
                } else {
                    Log.info("player is single player world i dont know what to do yet");
                }
                return true;
            } else {
                Log.info("still have time left");
            }
        } else {
            Log.info("no timelog exists");
        }
        return false;
    }
}
