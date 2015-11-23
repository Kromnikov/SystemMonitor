package core.factory;

import core.interfaces.SQLinterface;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLFactory {

    SQLinterface sql;

    public SQLFactory(SQLinterface sql) {
        this.sql = sql;
    }

    public boolean load(){return sql.load();}

    public void close(){sql.close();}

    public Statement getStatement() {return sql.getStatement();}



}
