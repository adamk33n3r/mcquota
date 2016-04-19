package com.adam_keenan.mcmods.mcquota.test;

import com.adam_keenan.mcmods.mcquota.quota.QuotaDBManager;
import com.adam_keenan.mcmods.mcquota.quota.Timelog;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class QuotaDBManagerTest {
    QuotaDBManager manager;

    @Before
    public void setUp() throws Exception {
        File dbFile = new File("test.mv.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
        dbFile = new File("test.trace.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testManager() throws Exception {
        this.manager = QuotaDBManager.getInstance("test");

        String uuid = "b63fa9df-dbd7-4f8b-9bdb-eaa486781115";
        this.manager.updateLoginTime(uuid);
        this.manager.close();

        Thread.sleep(3200);
        this.manager = QuotaDBManager.getInstance("test");
        this.manager.updateTimeSpent(uuid);
        Timelog timelog = this.manager.getTimelog(uuid);
        Log.info(timelog);
        assertEquals(timelog.time_spent, 3);
    }

    @After
    public void tearDown() throws Exception {
        this.manager.close();
    }
}