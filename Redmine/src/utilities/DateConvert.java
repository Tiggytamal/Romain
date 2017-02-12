package utilities;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Date;


/**
 * Utilitaire de conversion entre les deux formats de Date
 *  {@code java.util.Date} et {@code java.time.*}).
 * <p>
 * Toutes les méthodes sont null-safe.
 */
public class DateConvert
{
    /**
     * Permet de convertir un objet temporel dans les nouveau formats du JDK 1.8
     * @param la classe dans laquelle on veut convertir l'objet
     * @param date
     * @return
     */
    public static Temporal convert(Class<? extends Temporal> classe , Object date)
    {
        if (classe == null || date == null)
            return null;
        
        LocalDate date2 = LocalDate.now();
        date2.getClass().getSimpleName();
        
        switch (classe.getSimpleName())
        {
            case "LocalDate" :
                
                break;
            case "LocalDateTime" :
                break;
            case "Instant" :
                break;
            case "ZonedDateTime" :
                break;
            case "Default" :
                               
        }
        
        return null;
    }
    
    /**
     * Appel de {@link #asLocalDate(Date, ZoneId)} avec la time-zone par default du système.
     */
    public static LocalDate asLocalDate(Date date)
    {
        return asLocalDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDate} depuis {@code java.util.Date} ou ses sous-classes. Null-safe.
     */
    public static LocalDate asLocalDate(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }

    /**
     * Appel de {@link #asLocalDateTime(Date, ZoneId)} avec la time-zone par default du système.
     */
    public static LocalDateTime asLocalDateTime(Date date)
    {
        return asLocalDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Crée un {@link LocalDateTime} depuis {@code java.util.Date} ou ses sous-classes. Null-safe.
     */
    public static LocalDateTime asLocalDateTime(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) date).toLocalDateTime();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    /**
     * Appel {@link #createDate(Object, ZoneId)} avec la time-zone par default du système.
     */
    public static Date createDate(Object date)
    {
        return createDate(date, ZoneId.systemDefault());
    }

    /**
     * Crée une {@link java.util.Date} à partir de différents format de Date. Null-safe. Supporte les formats suivants:
     * <ul>
     * <li>{@link java.util.Date}
     * <li>{@link java.sql.Date}
     * <li>{@link java.sql.Timestamp}
     * <li>{@link java.time.LocalDate}
     * <li>{@link java.time.LocalDateTime}
     * <li>{@link java.time.ZonedDateTime}
     * <li>{@link java.time.Instant}
     * </ul>
     * 
     * @param zone
     *            Time zone, utilisée seulement pour LocalDate ou LocalDateTime.
     * @return {@link java.util.Date}
     */
    public static Date createDate(Object date, ZoneId zone)
    {
        if (date == null)
            return null;

        if (date instanceof Timestamp || date instanceof Date)
            return (Date) date;
        if (date instanceof LocalDate)
            return Date.from(((LocalDate) date).atStartOfDay(zone).toInstant());
        if (date instanceof LocalDateTime)
            return Date.from(((LocalDateTime) date).atZone(zone).toInstant());
        if (date instanceof ZonedDateTime)
            return Date.from(((ZonedDateTime) date).toInstant());
        if (date instanceof Instant)
            return Date.from((Instant) date);

        throw new UnsupportedOperationException("Don't know how to convert " + date.getClass().getName() + " to java.util.Date");
    }

    /**
     * Creates an {@link Instant} from {@code java.util.Date} or it's subclasses. Null-safe.
     */
    public static Instant asInstant(Date date)
    {
        if (date == null)
            return null;

        return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * Calls {@link #asZonedDateTime(Date, ZoneId)} with the system default time zone.
     */
    public static ZonedDateTime asZonedDateTime(Date date)
    {
        return asZonedDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Creates {@link ZonedDateTime} from {@code java.util.Date} or it's subclasses. Null-safe.
     */
    public static ZonedDateTime asZonedDateTime(Date date, ZoneId zone)
    {
        if (date == null)
            return null;

        return asInstant(date).atZone(zone);
    }

}