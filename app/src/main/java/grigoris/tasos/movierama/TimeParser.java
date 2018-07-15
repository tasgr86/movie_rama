package grigoris.tasos.movierama;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeParser {

    public static String getFormattedTime(String initialDate){

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-mm-dd");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd MMM yyyy");

        LocalDate ld = new LocalDate(dtf.parseLocalDate(initialDate));

        return dtf2.print(ld);

    }
}