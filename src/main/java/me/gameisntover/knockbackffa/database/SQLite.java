package me.gameisntover.knockbackffa.database;

import lombok.Getter;
import me.gameisntover.knockbackffa.KnockbackFFA;
import me.gameisntover.knockbackffa.util.Knocker;

import java.io.File;
import java.sql.*;

public class SQLite implements Database {
    private static SQLite sqLite;
    @Getter
    private static File file;
    private Connection connection;

    public SQLite() {
        String dbname = config.getString("SQLite.dbname");
        file = new File(KnockbackFFA.getInstance().getDataFolder(), dbname + ".db");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXIST kbffa (" +
                    "uuid CHAR(36) PRIMARY KEY NOT NULL," +
                    "name VARCHAR(16) NOT NULL," +
                    "kills INT(20) NOT NULL DEFAULT 0," +
                    "deaths INT(20) NOT NULL DEFAULT 0," +
                    "elo INT NOT NULL DEFAULT 0," +
                    "maxkillstreak INT(20) NOT NULL DEFAULT 0," +
                    "balance INT(20) NOT NULL DEFAULT 0," +
                    "selectedCosmetic VARCHAR(30) NOT NULL," +
                    "selectedTrail VARCHAR(30) NOT NULL," +
                    "selectedKit VARCHAR(30) NOT NULL," +
                    "ownedKits MEDIUMTEXT NOT NULL," +
                    "ownedCosmetics MEDIUMTEXT NOT NULL," +
                    ");");
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void insertData(Knocker knocker) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT OR IGNORE INTO kbffa(uuid,name,kills,deaths,elo,maxkillstreak,balance,selectedCosmetic,selectedTrail,selectedKit,ownedKits,ownedCosmetics) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
            stmt.setString(1,knocker.getUniqueID().toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateData(Knocker knocker) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE kbffa SET uuid = ?," +
                    "name = ?," +
                    "kills = ?," +
                    "deaths = ?," +
                    "elo = ?," +
                    "maxkillstreak = ?," +
                    "balance = ?," +
                    "selectedCosmetic = ?," +
                    "selectedTrail = ?," +
                    "selectedKit = ?," +
                    "ownedKits = ?" +
                    "ownedCosmetics = ?;");

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void loadData(Knocker knocker) {

    }

    public static SQLite get() {
        if (sqLite == null) sqLite = new SQLite();
        return sqLite;
    }
}
