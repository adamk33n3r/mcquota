package com.adam_keenan.mcmods.mcquota.test;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.quota.Timelog;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;

public class QuotaManagerTest {
    QuotaManager manager;

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
        this.manager = QuotaManager.getInstance();

        String uuid = "b63fa9df-dbd7-4f8b-9bdb-eaa486781115";
        this.manager.join(uuid);
        this.manager.close();

        Thread.sleep(3200);
        this.manager = QuotaManager.getInstance();
        this.manager.leave(uuid);
        Timelog timelog = this.manager.getLog(uuid);
        Log.info(timelog);
    }

    @After
    public void tearDown() throws Exception {
        this.manager.close();
    }
}