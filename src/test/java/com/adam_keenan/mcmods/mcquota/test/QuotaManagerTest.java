package com.adam_keenan.mcmods.mcquota.test;

import com.adam_keenan.mcmods.mcquota.quota.QuotaManager;
import com.adam_keenan.mcmods.mcquota.utils.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class QuotaManagerTest {
    QuotaManager manager;

    @Before
    public void setUp() throws Exception {
//        File dbFile = new File("test.sqlite");
//        if (dbFile.exists()) {
//            dbFile.delete();
//        }
        this.manager = QuotaManager.getInstance();
        this.manager.init("test.sqlite");
        this.manager.getLogs();
    }

    @Test
    public void testManager() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        this.manager.close();
    }
}