package com.adam_keenan.mcmods.mcquota.quota;

import com.adam_keenan.mcmods.mcquota.utils.Log;
import com.adam_keenan.mcmods.mcquota.utils.QueryBuilder;
import cpw.mods.fml.common.FMLCommonHandler;

import java.sql.*;

public class QuotaDBManager {
    private Connection connection;
    private static boolean initialized = false;

    private QuotaDBManager(String dbName) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            FMLCommonHandler.instance().exitJava(-1, true);
        }

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:./" + dbName);
            Log.info("Opened database successfully");
            if (!QuotaDBManager.initialized) {
                this.initDB();
                QuotaDBManager.initialized = true;
            }
        } catch (SQLException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(-1, true);
        }
    }

    public static QuotaDBManager getInstance() {
        return new QuotaDBManager("mcquota");
    }

    public static QuotaDBManager getInstance(String dbName) {
        return new QuotaDBManager(dbName);
    }

    public Timelog getTimelog(String uuid) {
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
                statement.close();
                return null;
            }
            timelog.id = result.getInt("id");
            timelog.players_uuid = result.getString("players_uuid");
            timelog.date = result.getDate("date");
            timelog.time_spent = result.getInt("time_spent");
            statement.close();
            return timelog;
        } catch (SQLException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public int getPlayerQuota(String uuid) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeQuery(
                QueryBuilder
                    .table("players")
                    .where("uuid", "=", uuid)
                    .getSql()
            );
            ResultSet result = statement.getResultSet();
            boolean exists = result.next();
            if (!exists) {
                statement.close();
                return -1;
            }
            int quota = result.getInt("quota");
            statement.close();
            return quota;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void setPlayerQuota(String uuid, int quota) {
        try {
            String sql = "update players set quota = ?1 where uuid = ?2";
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, quota);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTimeSpent(String uuid) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeQuery(
                QueryBuilder
                    .table("timelogs")
                    .where("players_uuid", "=", uuid)
                    .where("date", "=", "today()", true)
                    .getSql()
            );
            ResultSet result = statement.getResultSet();
            boolean exists = result.next();
            if (!exists) {
                statement.close();
                return 0;
            }
            int time_spent = result.getInt("time_spent");
            statement.close();
            return time_spent;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @TODO Make sure the player actually exists
     * @param uuid
     * @param timeSpent
     */
    public void setTimeSpent(String uuid, int timeSpent) {
        try {
            String sql = "merge into timelogs (players_uuid, time_spent, date) key (players_uuid, date) values (?1, ?2, today())";
            PreparedStatement pstatement = this.connection.prepareStatement(sql);
            pstatement.setString(1, uuid);
            pstatement.setInt(2, timeSpent);
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Log.info("closing connection");
        try {
            this.connection.close();
        } catch (SQLException e) {
            Log.severe(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void updateLoginTime(String uuid) {
        try {
            String sql = "merge into players (uuid, last_login) key (uuid) values (?1, now())";
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTimeSpent(String uuid) {
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
            Log.info("Time to be added", difference);
            statement.close();

            sql = QueryBuilder
                .table("timelogs")
                .select("count(*)")
                .where("players_uuid", "=", uuid)
                .where("date", "=", "today()", true)
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
                pstatement.close();
            } else {
                sql = "update timelogs set time_spent = time_spent + ?1 where players_uuid = ?2 and date = today();";
                PreparedStatement pstatement = this.connection.prepareStatement(sql);
                pstatement.setInt(1, difference);
                pstatement.setString(2, uuid);
                pstatement.executeUpdate();
                pstatement.close();
            }

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
                "last_login timestamp   default now()," +
                "quota      int         default -1" +
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
