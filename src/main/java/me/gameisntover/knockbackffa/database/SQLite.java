package me.gameisntover.knockbackffa.database;

import lombok.Getter;
import me.gameisntover.knockbackffa.cosmetics.Cosmetic;
import me.gameisntover.knockbackffa.cosmetics.TrailCosmetic;
import me.gameisntover.knockbackffa.kit.KitManager;
import me.gameisntover.knockbackffa.kit.KnockKit;
import me.gameisntover.knockbackffa.player.Knocker;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLite implements Database {
    private static SQLite sqLite;
    @Getter
    private Connection connection;

    public SQLite() {
        String dbname = config.getString("SQLite.dbname");

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbname + ".db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS kbffa(" +
                    "uuid CHAR(36) PRIMARY KEY," +
                    "kills INT(20)," +
                    "deaths INT(20)," +
                    "elo INT," +
                    "maxkillstreak INT," +
                    "balance INT," +
                    "selectedCosmetic TEXT," +
                    "selectedTrail TEXT," +
                    "selectedKit TEXT," +
                    "ownedKits TEXT," +
                    "ownedCosmetics MEDIUMTEXT" +
                    ");");
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void insertData(Knocker knocker) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT OR IGNORE INTO kbffa(uuid,kills,deaths,elo,maxkillstreak,balance,selectedCosmetic,selectedTrail,selectedKit,ownedKits,ownedCosmetics) VALUES(?,?,?,?,?,?,?,?,?,?,?);");
            stmt.setString(1,knocker.getUniqueID().toString());
            stmt.setInt(2,knocker.getKills());
            stmt.setInt(3,knocker.getDeaths());
            stmt.setInt(4,knocker.getElo());
            stmt.setInt(5,knocker.getMaxks());
            stmt.setDouble(6,knocker.getBalance());
            String selC = "none";
            String selT = "none";
            String selK = "default";
            if (knocker.getSelectedCosmetic() != null) selC = knocker.getSelectedCosmetic().getName();
            if (knocker.getSelectedTrail() != null) selT = knocker.getSelectedTrail().getName();
            if (knocker.getSelectedKit() != null) selK = knocker.getSelectedKit().getName();
            stmt.setString(7,selC);
            stmt.setString(8,selT);
            stmt.setString(9,selK);
            StringBuilder ownedKits = new StringBuilder();
            StringBuilder ownedCosmetics = new StringBuilder();
            ownedKits.append("default");
            ownedCosmetics.append("none");
            for (KnockKit knockKit : knocker.getOwnedKits()) ownedKits.append(":").append(knockKit.getName());
            for (Cosmetic cosmetic : knocker.getOwnedCosmetics()) ownedCosmetics.append(":").append(cosmetic.getName());
            stmt.setString(10, ownedKits.toString());
            stmt.setString(11, ownedCosmetics.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateData(Knocker knocker) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE OR IGNORE kbffa SET " +
                    "kills = ?," +
                    "deaths = ?," +
                    "elo = ?," +
                    "maxkillstreak = ?," +
                    "balance = ?," +
                    "selectedCosmetic = ?," +
                    "selectedTrail = ?," +
                    "selectedKit = ?," +
                    "ownedKits = ?," +
                    "ownedCosmetics = ? WHERE uuid = ?;");
            stmt.setInt(1,knocker.getKills());
            stmt.setInt(2,knocker.getDeaths());
            stmt.setInt(3,knocker.getElo());
            stmt.setInt(4,knocker.getMaxks());
            stmt.setDouble(5,knocker.getBalance());
            String selC = "none";
            String selT = "none";
            String selK = "default";
            if (knocker.getSelectedCosmetic() != null) selC = knocker.getSelectedCosmetic().getName();
            if (knocker.getSelectedTrail() != null) selT = knocker.getSelectedTrail().getName();
            if (knocker.getSelectedKit() != null) selK = knocker.getSelectedKit().getName();
            stmt.setString(6,selC);
            stmt.setString(7,selT);
            stmt.setString(8,selK);
            StringBuilder ownedKits = new StringBuilder();
            StringBuilder ownedCosmetics = new StringBuilder();
            for (KnockKit knockKit : knocker.getOwnedKits()) ownedKits.append(":").append(knockKit.getName());
            for (Cosmetic cosmetic : knocker.getOwnedCosmetics()) ownedCosmetics.append(":").append(cosmetic.getName());
            stmt.setString(9, ownedKits.toString());
            stmt.setString(10, ownedCosmetics.toString());
            stmt.setString(11,knocker.getUniqueID().toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void loadData(Knocker knocker) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM kbffa WHERE uuid=?;");
            stmt.setString(1,knocker.getUniqueID().toString());
            ResultSet rs = stmt.executeQuery();
            knocker.setKills(rs.getInt("kills"));
            knocker.setDeaths(rs.getInt("deaths"));
            knocker.setElo(rs.getInt("elo"));
            knocker.setMaxks(rs.getInt("maxkillstreak"));
            knocker.setBalance(rs.getDouble("balance"));
            knocker.setSelectedCosmetic(Cosmetic.fromString(rs.getString("selectedCosmetic"),knocker));
            knocker.setSelectedKit(KnockKit.getFromString(rs.getString("selectedKit")));
            knocker.setSelectedTrail((TrailCosmetic) Cosmetic.fromString(rs.getString("selectedTrail"),knocker));
            if (rs.getString("ownedCosmetics").contains(":")) {
                String[] cosmeticsstr = rs.getString("ownedCosmetics").split(":");
                List<Cosmetic> cosmetics = new ArrayList<>();
                for (String co : cosmeticsstr) cosmetics.add(Cosmetic.fromString(co, knocker));
                knocker.setOwnedCosmetics(cosmetics);
            }else knocker.setOwnedCosmetics(Collections.singletonList(Cosmetic.fromString(rs.getString("ownedCosmetics"),knocker)));
            if (rs.getString("ownedKits").contains(":")) {
                String[] kitsstr = rs.getString("ownedKits").split(":");
                List<KnockKit> kits = new ArrayList<>();
                for (String ki : kitsstr)kits.add(KnockKit.getFromString(ki));
                knocker.setOwnedKits(kits);
            }else knocker.setOwnedKits(Collections.singletonList(KitManager.load(rs.getString("ownedKits"))));
            stmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static SQLite get() {
        if (sqLite == null) sqLite = new SQLite();
        return sqLite;
    }
}
