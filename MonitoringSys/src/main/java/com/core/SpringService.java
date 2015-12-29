package com.core;

import com.core.interfaces.db.IMetricStorage;
import com.core.models.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Kromnikov on 30.11.2015.
 */
public class SpringService {

    private static ApplicationContext context;

    public static void run() {
        context = new ClassPathXmlApplicationContext("META-INF/beans.xml");


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        IMetricStorage metricStorage = context.getBean(IMetricStorage.class);
        final String date = dateFormat.format(new Date());
        Date dateTime = new Date();
        try {
            dateTime = (dateFormat.parse("2015-12-17 21:44:56"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Value> values = metricStorage.getValuesLastMinets(1, 11, dateTime);
        for (Value val : values) {
            System.out.println(val.getValue());
        }
    }


}
