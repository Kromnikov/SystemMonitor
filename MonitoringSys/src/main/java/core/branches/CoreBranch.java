package core.branches;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.util.Timer;

/**
 * Created by ������� on 16.11.2015.
 */
public class CoreBranch {

//    public static void run() throws InterruptedException {
//        Thread myThready = new Thread(new Runnable() {
//            public void run() //���� ����� ����� ����������� � �������� ������
//            {
//                Timer time = new Timer();
//                ScheduledTask st = new ScheduledTask();
//                time.schedule(st, 0, 10000); // ������� ������ � ����������� ����� 10 ���.
//            }
//        });
//        myThready.start();
//    }

    public static void run() throws SQLException {
        SQLBranch.run();
        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask();
        time.schedule(st, 0, 10000); // ������� ������ � ����������� ����� 10 ���.
    }
}
