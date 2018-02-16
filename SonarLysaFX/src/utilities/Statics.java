package utilities;

import java.time.LocalDate;

/**
 * Classe regroupant toutes les constantes statiques
 * @author ETP8137 - Grégoire Mathon
 *
 */
public abstract class Statics
{
	private Statics() {}
	
	//* Nom de l'application */
	public static final String NOMAPPLI = "SonarLyza";
	/** Adresse du serveur SonarQube */
	public static final String URI = "http://ttp10-snar.ca-technologies.fr";
	/** adresse du serveur SonarQube de test */
	public static final String URITEST = "http://ttt10-snar.ca-technologies.fr";
	/** Valeur pour le séparateur de ligne indépendant du système */
	public static final String NL = System.getProperty("line.separator");
	/** Date du jour */
	public static final LocalDate TODAY = LocalDate.now();
	/** Mois de janvier */
	public static final String JANVIER = "Janvier";
	/** Mois de janvier */
	public static final String FEVRIER = "Février";
	/** Mois de janvier */
	public static final String MARS = "Mars";
	/** Mois de janvier */
	public static final String AVRIL = "Avril";
	/** Mois de janvier */
	public static final String MAI = "Mai";
	/** Mois de janvier */
	public static final String JUIN = "Juin";
	/** Mois de janvier */
	public static final String JUILLET = "Juillet";
	/** Mois de janvier */
	public static final String AOUT = "Aout";
	/** Mois de janvier */
	public static final String SEPTEMBRE = "Septembre";
	/** Mois de janvier */
	public static final String OCTOBRE = "Octobre";
	/** Mois de janvier */
	public static final String NOVEMBRE = "Novembre";
	/** Mois de janvier */
	public static final String DECEMBRE = "Decembre";
	/** espace */
	public static final String SPACE = " ";
	/** apllication inconnue dans Sonar */
	public static final String INCONNUE = "INCONNUE";	
}