package core.branches;

import core.SpringService;

import java.sql.SQLException;

/**
 * Created by ������� on 16.11.2015.
 */
public class CoreBranch {

    public static void run() throws SQLException {
        SpringService.run();
//        Timer time = new Timer();
//        ScheduledTask st = new ScheduledTask();
//        time.schedule(st, 0, 10000);
    }
}
