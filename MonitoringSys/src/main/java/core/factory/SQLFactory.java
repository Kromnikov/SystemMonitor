package core.factory;

import core.interfaces.DBConnectInterface;

import java.sql.Statement;

public class SQLFactory {

    DBConnectInterface sql;

    public SQLFactory(DBConnectInterface sql) {
        this.sql = sql;
    }

    public boolean load(){return sql.load();}

    public void close(){sql.close();}

    public Statement getStatement() {return sql.getStatement();}



}
