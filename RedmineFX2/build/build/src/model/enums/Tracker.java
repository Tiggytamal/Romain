package model.enums;

public enum Tracker
{
	INCIDENT(Valeur.INCIDENT),
	PROBLEME(Valeur.PROBLEME),
	DEMANDE(Valeur.DEMANDE),
	ANOMALIE(Valeur.ANOMALIE),
	EVOLUTION(Valeur.EVOLUTION),
	ASSISTANCE(Valeur.ASSISTANCE),
	EXPERTISE(Valeur.EXPERTISE),
	EVOLUTIF(Valeur.EVOLUTIF),
	INTERVENTION(Valeur.INTERVENTION);
	//TODO Vérifier les vlauers des trackers en table.
	
	private final String string;

	private Tracker(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString()
	{
		return string;
	}
    
    public static Tracker getTracker(String trackerString)
    {
    	switch(trackerString)
    	{
    		case Valeur.INCIDENT :
    			return INCIDENT;
    		case Valeur.PROBLEME :
    			return PROBLEME;
    		case Valeur.DEMANDE :
    			return DEMANDE;
            case Valeur.ANOMALIE :
                return ANOMALIE;
            case Valeur.EVOLUTION :
                return EVOLUTION;
            case Valeur.ASSISTANCE :
                return ASSISTANCE;
            case Valeur.EXPERTISE :
                return EXPERTISE;
            case Valeur.EVOLUTIF :
                return EVOLUTIF;
            case Valeur.INTERVENTION :
                return INTERVENTION;
    		default :
    			throw new IllegalArgumentException("Tracker inconnu : " + trackerString);
    	}
    }
    /**
     * Classe privée contenant toutes les valeurs possibles des trackers
     * 
     * @author Tiggy Tamal
     * @since 1.0
     */
    private static class Valeur
    {
        private static final String INCIDENT = "Incident";
        private static final String PROBLEME = "Problème";
        private static final String DEMANDE = "Demande";
        private static final String ANOMALIE = "Anomalie";
        private static final String EVOLUTION = "Evolution";
        private static final String ASSISTANCE = "Assistance";
        private static final String EXPERTISE = "Expertise";
        private static final String EVOLUTIF = "Evolutif";
        private static final String INTERVENTION = "Intervention";
    }
}
