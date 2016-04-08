package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.QueryBuilder;
import org.sqlite.SQLiteConfig;

import java.sql.*;

public class QuotaManager {
    private static QuotaManager instance = new QuotaManager();
    private Connection connection;

    private QuotaManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            System.exit(-1);
        }
    }

    public static QuotaManager getInstance() {
        return instance;
    }

    public void init(String databasePath) {
        Log.info("init func");
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
                Log.info("Opened database successfully");
                if (!this.tableExists()) {
                    this.initDB();
                }
            } catch (SQLException e) {
//                Log.severe(e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public ResultSet getLogs() {
        Log.info("getting logs");
        try {
            Statement statement = this.connection.createStatement();
            statement.executeQuery(
                QueryBuilder.table("timelogs")
                    .getSql()
            );
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                Log.info(result.getString("username"));
            }
            statement.close();
        } catch (SQLException e) {
            Log.severe("adsf");
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public void close() {
        Log.info("closing connection");
        try {
            this.connection.close();
        } catch (SQLException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private boolean tableExists() throws SQLException {
        String query = QueryBuilder.table("sqlite_master")
            .where("type", "=", "table")
            .where("name", "=", "timelogs")
            .getSql();
        Statement statement = this.connection.createStatement();
        Log.info(statement.execute(query));
        ResultSet result = statement.getResultSet();
        boolean exists = result.next();
        statement.close();
        return exists;
    }

    private void initDB() throws SQLException {
        Log.info("init db");
        String sql = "create table timelogs " +
                     "(username  text not null," +
                     " timestamp text not null)";
        Statement statement = this.connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }
}
