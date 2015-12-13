package core.branches;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Kromnikov on 13.12.2015.
 */
public class test {

    @Scheduled(fixedDelay = 10000)
    public void  run() {
        System.out.println("run");
        System.out.println("runrun");
        System.out.println("runrunrun");
        System.out.println("runrunrunrun");
        System.out.println("runrunrunrunrun");
    }
}
