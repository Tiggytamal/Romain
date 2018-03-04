package utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Essai
{

    public static void main(String[] args)
    {
        LocalDate date = LocalDate.of(2018, 2, 1);
        System.out.println(date.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRANCE)));
    }

}
