package net.core.tools;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Averaging {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static public Map<Long, Object> getValues(List<Map<String, Object>> rows,Date nDate,String rout) throws ParseException {
        Map<Long, Object> map = new HashMap<>();
        int minutesCount=0,hourCounts=0,monthCount=0,yearCount=0,counter=1;
        double sumValues=0,sumValues1=0;
        long x=0,prevX=0,Dif=0,defTime= 10*1000
                ,startTime=nDate.getTime(),pStartTime=nDate.getTime();
        Object y=0,prevY=0;
        prevX = (long) (((java.sql.Timestamp) rows.get(0).get("date_time")).getTime());
        prevY = (double) rows.get(0).get("value");

        switch (rout) {
            case "minutes":
            {
                prevX = (long) (((java.sql.Timestamp) rows.get(0).get("date_time")).getTime());
                prevY = (double) rows.get(0).get("value");
                map.put(prevX, (double)prevY);
                for (int i = 1; i < rows.size(); i++) {
                    x = (long) (((java.sql.Timestamp) rows.get(i).get("date_time")).getTime());
                    y = (double) rows.get(i).get("value");
                    Dif = x - prevX;
                    while (Dif>defTime) {
                        prevX += defTime;
                        prevY = null;
                        Dif = x - prevX;
                        map.put(prevX, prevY);
                    }
                    map.put(x, y);
                    prevX=x;
                    prevY=y;
                }
            }









            case "hour": {
                for (int i = 0; i < 60; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (counter < rows.size()) {
                            if (startTime < prevX) {
                                startTime += defTime;
                            } else {
                                sumValues += (double) prevY;
                                minutesCount++;
                                prevY = (double) rows.get(counter).get("value");
                                prevX = (long) (((java.sql.Timestamp) rows.get(counter).get("date_time")).getTime());
                                counter++;
                                startTime += defTime;
                            }
                        }
                    }
                    if (minutesCount > 0) {
//                    System.out.println(new Date(pStartTime)+"-"+(new BigDecimal(sumValues / countPoint).setScale(3, RoundingMode.UP).doubleValue()));
                        map.put(pStartTime, new BigDecimal(sumValues / minutesCount).setScale(3, RoundingMode.UP).doubleValue());
                        minutesCount = 0;
                        sumValues = 0;
                    } else {
//                    System.out.println(new Date(pStartTime)+"-null");
                        map.put(pStartTime, null);
                    }
                    pStartTime = startTime;
                }
            }











            case "day":{
                for (int o = 0; o < 24; o++) {
                    for (int i = 0; i < 60; i++) {
                        for (int j = 0; j < 6; j++) {
                            if (counter < rows.size()) {
                                if (startTime < prevX) {
                                    startTime += defTime;
                                } else {
                                    sumValues += (double) prevY;
                                    minutesCount++;
                                    prevY = (double) rows.get(counter).get("value");
                                    prevX = (long) (((java.sql.Timestamp) rows.get(counter).get("date_time")).getTime());
                                    counter++;
                                    startTime += defTime;
                                }
                            }
                        }
                        if (minutesCount > 0) {
                            sumValues1 += new BigDecimal(sumValues / minutesCount).setScale(3, RoundingMode.UP).doubleValue();
                            minutesCount = 0;
                            sumValues = 0;
                            hourCounts++;
                        }
                    }
                    if (sumValues1 > 0) {
                        map.put(pStartTime, new BigDecimal(sumValues1 / hourCounts).setScale(3, RoundingMode.UP).doubleValue());
                        sumValues1 = 0;
                        hourCounts=0;
                    } else {
                        map.put(pStartTime, null);
                        hourCounts=0;
                    }
                    pStartTime = startTime;
                }
            }










            case "month": {
                for (int m = 0; m < 30; m++) {
                    for (int o = 0; o < 24; o++) {
                        for (int i = 0; i < 60; i++) {
                            for (int j = 0; j < 6; j++) {
                                if (counter < rows.size()) {
                                    if (startTime < prevX) {
                                        startTime += defTime;
                                    } else {
//                                        System.out.println("startTime= " + (new Date(startTime)));
//                                        System.out.println("prevX= " + (new Date(prevX)));
                                        sumValues += (double) prevY;
                                        minutesCount++;
                                        prevY = (double) rows.get(counter).get("value");
                                        prevX = (long) (((java.sql.Timestamp) rows.get(counter).get("date_time")).getTime());
                                        counter++;
                                        startTime += defTime;
                                    }
                                }
                            }
                            if (minutesCount > 0) {
                                sumValues1 += new BigDecimal(sumValues / minutesCount).setScale(3, RoundingMode.UP).doubleValue();
                                minutesCount = 0;
                                sumValues = 0;
                                hourCounts++;
                            }
                        }
                        if (hourCounts > 0) {
                            sumValues += new BigDecimal(sumValues1 / hourCounts).setScale(3, RoundingMode.UP).doubleValue();
                            sumValues1 = 0;
                            hourCounts = 0;
                            monthCount++;
                        }

                    }
                    if (monthCount > 0) {
                        map.put(pStartTime, new BigDecimal(sumValues / monthCount).setScale(3, RoundingMode.UP).doubleValue());
                        sumValues=0;
                        monthCount=0;

                    } else {
                        map.put(pStartTime, null);
                        sumValues=0;
                        monthCount = 0;
                    }
                    pStartTime = startTime;
                }
            }















            case "year": {
                for (int ym = 0; ym < 12; ym++) {
                    for (int m = 0; m < 30; m++) {
                        for (int o = 0; o < 24; o++) {
                            for (int i = 0; i < 60; i++) {
                                for (int j = 0; j < 6; j++) {
                                    if (counter < rows.size()) {
                                        if (startTime < prevX) {
                                            startTime += defTime;
                                        } else {
//                                            System.out.println("startTime= " + (new Date(startTime)));
//                                            System.out.println("prevX= " + (new Date(prevX)));
                                            sumValues += (double) prevY;
                                            minutesCount++;
                                            prevY = (double) rows.get(counter).get("value");
                                            prevX = (long) (((java.sql.Timestamp) rows.get(counter).get("date_time")).getTime());
                                            counter++;
                                            startTime += defTime;
                                        }
                                    }
                                }
                                if (minutesCount > 0) {
                                    sumValues1 += new BigDecimal(sumValues / minutesCount).setScale(3, RoundingMode.UP).doubleValue();
                                    minutesCount = 0;
                                    sumValues = 0;
                                    hourCounts++;
                                }
                            }
                            if (hourCounts > 0) {
                                sumValues += new BigDecimal(sumValues1 / hourCounts).setScale(3, RoundingMode.UP).doubleValue();
                                sumValues1 = 0;
                                hourCounts = 0;
                                monthCount++;
                            }

                        }
                        if (monthCount > 0) {
                            sumValues1+=new BigDecimal(sumValues / monthCount).setScale(3, RoundingMode.UP).doubleValue();;
                            sumValues = 0;
                            monthCount = 0;
                            yearCount++;

                        } else {
                            sumValues = 0;
                            monthCount = 0;
                        }
                    }
                    if (yearCount > 0) {
                        map.put(pStartTime, new BigDecimal(sumValues1 / yearCount).setScale(3, RoundingMode.UP).doubleValue());
                        sumValues1=0;
                        yearCount=0;
                    } else {
                        map.put(pStartTime, null);
                        sumValues1=0;
                        yearCount=0;
                    }
                    pStartTime = startTime;
                }
            }
        }

        return map;
    }


    public static Date parserDate(String startDate) throws ParseException {
        String mTimeZone = startDate.substring(20,23);
        String mActualDate = startDate.replace(mTimeZone + " ", "");
        System.out.println(mActualDate);
        String TWITTER = "EEE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat mSf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        mSf.setTimeZone(TimeZone.getDefault());
        Date mNewDate = mSf.parse(mActualDate);
        return mNewDate;
    }


}
