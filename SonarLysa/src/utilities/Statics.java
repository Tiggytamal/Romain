package utilities;

import java.time.LocalDate;

public class Statics
{
	private Statics() {}
	/** Adresse du serveur SonarQube */
	public static final String URI = "http://ttp10-snar.ca-technologies.fr";
    /** Valeur pour le séparateur de ligne indépendant du système */
    public static final String NL = System.getProperty("line.separator");   
    /** Date du jour */
    public static final LocalDate TODAY = LocalDate.now();
}
