package core.interfaces;

import java.sql.Statement;

public interface DBConnectInterface {

    public boolean load();

    public void close();

    public Statement getStatement();

}
