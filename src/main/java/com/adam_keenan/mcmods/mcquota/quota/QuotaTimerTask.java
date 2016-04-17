package com.adam_keenan.mcmods.mcquota.quota;

import net.minecraft.entity.player.EntityPlayer;

import java.util.TimerTask;

public class QuotaTimerTask extends TimerTask {
    EntityPlayer player;

    public QuotaTimerTask(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void run() {
        QuotaManager.getInstance().save(this.player);
        QuotaManager.getInstance().kickIfOverQuota(this.player);
    }
}
