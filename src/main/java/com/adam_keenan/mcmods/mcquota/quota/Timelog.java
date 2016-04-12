package com.adam_keenan.mcmods.mcquota.quota;

import java.sql.Date;

public class Timelog {
    public int id;
    public String players_uuid;
    public Date date;
    public int time_spent;

    public Timelog() {}

    public Timelog(int id, String players_uuid, Date date, int time_spent) {
        this.id = id;
        this.players_uuid = players_uuid;
        this.date = date;
        this.time_spent = time_spent;
    }

    public String toString() {
        return String.format("(Timelog) id: %s, players_uuid: %s, date: %s, time_spent: %s", this.id, this.players_uuid, this.date, this.time_spent);
    }
}
