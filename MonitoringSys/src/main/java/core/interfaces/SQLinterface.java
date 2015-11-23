package core.interfaces;

import java.sql.Statement;

public interface SQLinterface {

    public boolean load();

    public void close();

    public Statement getStatement();

}
