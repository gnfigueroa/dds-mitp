package dds.servicios.helpers;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {

    private static DateHelper dateHelper = new DateHelper();

    public static DateHelper getHelper() { return dateHelper; }

    public Date LocalDateToDate (LocalDate time){
        return Date.from(time.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public Date LocalDateTimeToDate(LocalDateTime time){
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }




}
