package core.factory;

import core.interfaces.DBConnectInterface;

import java.sql.Statement;

/**
 * Created by Алексей on 25.11.2015.
 */
public class DBConnectFactory {
    DBConnectInterface db;

    public DBConnectFactory(DBConnectInterface db) {
        this.db = db;
    }

    public boolean load() {
        return db.load();
    }

    public void close() {
        db.close();
    }

    public Statement getStatement() {
        return db.getStatement();
    }
}
