package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Config;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;
import java.util.HashMap;
import java.util.Timer;

public class QuotaManager {
    private static QuotaManager instance = new QuotaManager();

    public static QuotaManager getInstance() {
        return instance;
    }

    private QuotaManager() {
        this.timers = new HashMap<String, Timer>();
    }

    private Map<String, Timer> timers;

    private boolean createTimer(EntityPlayer player) {
        String uuid = player.getUniqueID().toString();
        if (this.timers.containsKey(uuid)) {
            return false;
        }
        Timer t = new Timer();
        t.scheduleAtFixedRate(new QuotaTimerTask(player), Config.interval*1000, Config.interval*1000);
        this.timers.put(uuid, t);
        return true;
    }

    private boolean cancelTimer(EntityPlayer player) {
        String uuid = Utils.playerToUUID(player);
        if (this.timers.containsKey(uuid)) {
            Timer t = this.timers.get(uuid);
            t.cancel();
            this.timers.remove(uuid);
            return true;
        }
        return false;
    }

    public void playerJoined(EntityPlayer player) {
        QuotaDBManager.getInstance().updateLoginTime(Utils.playerToUUID(player));
        if (!this.kickIfOverQuota(player)) {
            this.createTimer(player);
        }
    }

    public void playerLeft(EntityPlayer player) {
        QuotaDBManager.getInstance().updateTimeSpent(Utils.playerToUUID(player));
        this.cancelTimer(player);
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
            int quotaToCheck = Config.quotaLength;
            if (playerQuota >= 0) {
                Log.info("player has quota");
                quotaToCheck = playerQuota;
            }
            Log.info("timelog exists. checking against quota");
            Log.info(String.format("%s >= %s = %s", timelog.time_spent, quotaToCheck, timelog.time_spent >= quotaToCheck));
            if (timelog.time_spent >= quotaToCheck) {
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
