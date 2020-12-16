package com.newpoint.workstation;

import com.newpoint.marketdata.Duration;
import com.newpoint.marketdata.Period;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.time.*;

public class NPUtilityTest {
// This method is called to start the application
public static void main(String args[]) {

        DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
        DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma 'ET'");

        Enum e = Duration.S;
        ZoneId istZoneId = ZoneId.of("Asia/Tokyo");
        ZoneId etZoneId = ZoneId.of("America/New_York");

        LocalDateTime currentDateTime = LocalDateTime.now();

        ZonedDateTime currentISTime = currentDateTime.atZone(etZoneId);                //India Time
        ZonedDateTime currentETime = currentISTime.withZoneSameInstant(etZoneId);       //ET Time
        ZonedDateTime currentETime1 = currentDateTime.atZone(etZoneId);

        System.out.println(globalFormat.format(currentISTime));
        System.out.println(etFormat.format(currentETime));
        TimeZone newYorkTZ = TimeZone.getTimeZone("America/New_York");
        Calendar aCalendar = Calendar.getInstance(newYorkTZ, Locale.US);
        Date startDate = aCalendar.getTime();
        Date endDate = aCalendar.getTime();
        Period aPeriod = new Period(startDate, endDate);
        }
}