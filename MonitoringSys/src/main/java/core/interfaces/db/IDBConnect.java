package core.interfaces.db;

import java.sql.Statement;

public interface IDBConnect {

    public boolean load();

    public void close();

    public Statement getStatement();

}
