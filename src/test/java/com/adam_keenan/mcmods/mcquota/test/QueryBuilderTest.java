package com.adam_keenan.mcmods.mcquota.test;

import com.adam_keenan.mcmods.mcquota.utils.QueryBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryBuilderTest {
    @Test
    public void table() throws Exception {
        assertEquals(QueryBuilder.table("table").getSql(), "select * from table;");
    }

    @Test
    public void select() throws Exception {
        assertEquals(QueryBuilder.table("table").select().getSql(), "select * from table;");
        assertEquals(QueryBuilder.table("table").select("*").getSql(), "select * from table;");
//        assertEquals(QueryBuilder.table("table").select(" *   ").getSql(), "select * from table;");
        assertEquals(QueryBuilder.table("table").select("username").getSql(), "select username from table;");
        assertEquals(QueryBuilder.table("table").select("username", "name").getSql(), "select username, name from table;");
    }

    @Test
    public void where() throws Exception {
        String query = QueryBuilder
            .table("table")
            .where("username", "ilike", "adamk33n3r")
            .getSql();
        assertEquals(query, "select * from table where \"username\" ilike 'adamk33n3r';");
        query = QueryBuilder
            .table("table")
            .where("age", ">=", 21)
            .where("age", "<=", 25)
            .getSql();
        assertEquals(query, "select * from table where \"age\" >= 21 and \"age\" <= 25;");
    }

    @Test
    public void get() throws Exception {
        assertEquals(QueryBuilder.table("table").getSql(), "select * from table;");
    }

}