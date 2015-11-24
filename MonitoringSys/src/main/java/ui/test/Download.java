package ui.test;

import core.Models.Metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by ANTON on 15.11.2015.
 */
public class Download {
    double CPU;
    public void saveMetrics(Metric ssh) {
        try {
            FileWriter out = new FileWriter("D:\\Net_Ctacker\\GitHub\\MonitoringSystem\\metric.txt", true);
            while (true) {
//                CPU = ssh.getCPU();
                out.write(Double.toString(CPU) + "\n");
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
        public String readMetics() throws FileNotFoundException {
        String s = "";
        Scanner in = new Scanner(new File("D:\\Net_Ctacker\\GitHub\\MonitoringSystem\\metric.txt"));
        s += in.nextLine();
        in.close();
        return s;
    }
    }

