package edu.chapman.cpsc370.asdplaydate.helpers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 * Created by ryanb on 11/11/2015.
 */
public class DateHelpers
{
    public static Date UTCDate(DateTime localDate)
    {
        DateTime dt = localDate.withZone(DateTimeZone.UTC);
        return dt.toDate();
    }

    public static DateTime LocalDateTime(Date UTCdate)
    {
        return new DateTime(UTCdate,DateTimeZone.getDefault());
    }
}
