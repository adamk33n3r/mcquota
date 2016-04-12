package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.QueryBuilder;

import java.sql.*;

public class QuotaManager {
    private Connection connection;
    private static boolean initialized = false;

    private QuotaManager() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            System.exit(-1);
        }

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:./test");
            Log.info("Opened database successfully");
            if (!QuotaManager.initialized) {
                this.initDB();
                QuotaManager.initialized = true;
            }
        } catch (SQLException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static QuotaManager getInstance() {
        return new QuotaManager();
    }

    public Timelog getLog(String uuid) {
        Log.info("getting logs");
        try {
            Statement statement = this.connection.createStatement();
            statement.executeQuery(
                QueryBuilder
                    .table("timelogs")
                    .where("players_uuid", "=", uuid)
                    .where("date", "=", "today()", true)
                    .getSql()
            );
            Timelog timelog = new Timelog();
            ResultSet result = statement.getResultSet();
            int colCount = result.getMetaData().getColumnCount();
            boolean exists = result.next();
            if (!exists) {
                return null;
            }
            timelog.id = result.getInt("id");
            timelog.players_uuid = result.getString("players_uuid");
            timelog.date = result.getDate("date");
            timelog.time_spent = result.getInt("time_spent");
            for (int i = 1; i <= colCount; i++) {
                Log.info(result.getMetaData().getColumnName(i), result.getObject(i));
            }
            statement.close();
            return timelog;
        } catch (SQLException e) {
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

    public void join(String uuid) {
        try {
            String sql = "merge into players (uuid, last_login) key (uuid) values (?1, now())";
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void leave(String uuid) {
        try {
            String sql = QueryBuilder
                .table("players")
                .select("datediff('second', last_login, now())")
                .where("uuid", "=", uuid)
                .getSql()
                ;
            Statement statement = this.connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            result.next();
            int difference = result.getInt(1);
            Log.info(difference);
            statement.close();

            sql = QueryBuilder
                .table("timelogs")
                .select("count(*)")
                .where("players_uuid", "=", uuid)
                .where("date", "=", "today()")
                .getSql()
            ;
            statement = this.connection.createStatement();
            result = statement.executeQuery(sql);
            result.next();
            int count = result.getInt(1);
            statement.close();
            if (count == 0) {
                sql = "insert into timelogs (players_uuid, time_spent) values (?1, ?2);";
                PreparedStatement pstatement = this.connection.prepareStatement(sql);
                pstatement.setString(1, uuid);
                pstatement.setInt(2, difference);
                pstatement.executeUpdate();
            } else {
                sql = "update timelogs set time_spent = time_spent + ?1 where uuid = ?2 and date = today();";
                PreparedStatement pstatement = this.connection.prepareStatement(sql);
                pstatement.setInt(1, difference);
                pstatement.setString(2, uuid);
                pstatement.executeUpdate();
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDB() {
        Log.info("init db");
        Statement statement;
        try {
            statement = this.connection.createStatement();
            String sql = "create table if not exists players " +
                "(" +
                "uuid       varchar(36) primary key," +
                "last_login timestamp default now()" +
                ")";
            statement.executeUpdate(sql);
            sql = "create table if not exists timelogs " +
                "(" +
                "id           identity primary key," +
                "players_uuid varchar(36)," +
                "date         date  default today()," +
                "time_spent   int   default 0," +

                "foreign key (players_uuid)" +
                "references players(uuid)" +
                ")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
