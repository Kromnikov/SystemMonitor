package com.ui.tools;

/**
 * Created by ANTON on 09.12.2015.
 */
public class TypeOfMetric {
    public static int getTypeOfMetric(String title) {
        int k = 0;
        switch (title) {
            case "getCPU":
                k = 1;
                break;
            case "getFreeDiskMb":
                k = 2;
                break;
            case "getUsedDiskMb":
                k = 3;
                break;
            case "getTotalDiskMb":
                k = 4;
                break;
            case "getFreeRAM":
                k = 5;
                break;
            case "getUsedRAM":
                k = 6;
                break;
            case "getTotalRAM":
                k = 7;
                break;

        }
        return k;
    }
}
