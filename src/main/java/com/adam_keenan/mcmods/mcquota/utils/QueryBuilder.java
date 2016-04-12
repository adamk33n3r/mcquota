package com.adam_keenan.mcmods.mcquota.utils;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {
    private String table;
    private List<String> selects;
    private List<String> wheres;
    private List<String> joins;


    public static QueryBuilder table(String tableName) {
        return new QueryBuilder(tableName);
    }

    private QueryBuilder(String table) {
        this.table = table;
    }

    public QueryBuilder select(String... selects) {
        if (selects.length > 0) {
            this.selects = Arrays.asList(selects);
        } else {
            this.selects = Arrays.asList("*");
        }
        return this;
    }

    public QueryBuilder where(String column, String cmp, Object value) {
        return this.where(column, cmp, value, false);
    }

    public QueryBuilder where(String column, String cmp, Object value, Boolean raw) {
        if (this.wheres == null) {
            this.wheres = new ArrayList<String>();
        }
        if (!raw && !(value instanceof Number || value instanceof Boolean)) {
            value = String.format("'%s'", value);
        }
        this.wheres.add(String.format("%s %s %s", column, cmp, value));
        return this;
    }

    public QueryBuilder join(String table, String column1, String cmp, String column2) {
        if (this.joins == null) {
            this.joins = new ArrayList<String>();
        }
        this.joins.add(String.format("inner join %s on %s %s %s", table, column1, cmp, column2));
        return this;
    }

    private String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append("select ");
        if (this.selects != null) {
            query.append(StringUtils.join(this.selects, ", "));
        } else {
            query.append("*");
        }
        query.append(" from ");
        query.append(this.table);
        if (this.joins != null) {
            query.append(" " + StringUtils.join(this.joins, " "));
        }
        if (this.wheres != null) {
            query.append(" where ");
            query.append(StringUtils.join(this.wheres, " and "));
        }
        query.append(";");
        return query.toString();
    }

    public QueryResult get() {
        throw new NotImplementedException("Not yet implemented");
    }

    public String getSql() {
        return this.buildQuery();
    }
}
