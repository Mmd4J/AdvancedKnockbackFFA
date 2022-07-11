package me.gameisntover.knockbackffa.database;

import me.gameisntover.knockbackffa.util.Config;
import me.gameisntover.knockbackffa.util.Knocker;

public interface Database {
    Config config = new Config("database");

    void insertData(Knocker knocker);

    void updateData(Knocker knocker);

    void loadData(Knocker knocker);

     static Database getDatabase() {
        Database db = null;
        switch (DatabaseType.valueOf(config.getString("type"))) {
            case MySQL:
                db = MySQL.get();
                break;
            case SQLite:
                db = SQLite.get();
                break;
        }
        return db;
    }

    enum DatabaseType {
        SQLite,
        MySQL
    }
}
