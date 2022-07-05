package me.gameisntover.knockbackffa.database;

import me.gameisntover.knockbackffa.util.Knocker;

public class MySQL implements Database{
    private static MySQL mySQL;
    @Override
    public void insertData(Knocker knocker) {

    }

    @Override
    public void updateData(Knocker knocker) {

    }

    @Override
    public void loadData(Knocker knocker) {

    }
    public static MySQL get(){
    if (mySQL == null) mySQL = new MySQL();
    return mySQL;
    }
}
