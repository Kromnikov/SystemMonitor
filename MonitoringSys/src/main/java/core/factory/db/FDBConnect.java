package core.factory.db;

import core.interfaces.db.IDBConnect;

import java.sql.Statement;

/**
 * Created by Алексей on 25.11.2015.
 */
public class FDBConnect {
    IDBConnect db;

    public FDBConnect(IDBConnect db) {
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
