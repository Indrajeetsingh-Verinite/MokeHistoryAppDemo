package com.verinite.interestapp.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class DateUtility {


    static final String SIMPLETIMESTAMPFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String SIMPLEDATEFORMAT = "yyyy-MM-dd";
    public Timestamp getLocalTimestamp() {
        DateFormat format = new SimpleDateFormat(SIMPLETIMESTAMPFORMAT);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = format.format(timestamp);

        return Timestamp.valueOf(time);
        
    }


public boolean dateCompare(String startDate, String endDate){

    return Timestamp.valueOf(startDate)
            .compareTo(Timestamp.valueOf(endDate)) > 0;
}


}
