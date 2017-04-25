package model.enums;

/**
 * Contient la liste de tous les statuts possible d'un incident dans Redmine
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public enum Statut
{
    
    RESOLVED(Valeur.RESOLVED),
    NOUVEAU(Valeur.NOUVEAU),
    WRKINPRG(Valeur.WRKINPRG),
    CLOSED(Valeur.CLOSED),
    TRANSFERED(Valeur.TRANSFERED),
    REFERRED(Valeur.REFERRED),
    PENDING(Valeur.PENDING),
    NONTMA(Valeur.NONTMA),
    REJECTED(Valeur.REJECTED),
    ACHIFFRER(Valeur.ACHIFFRER),
    CHIFFRE(Valeur.CHIFFRE),
    ANALYSED(Valeur.ANALYSED);
	//TODO Vérifier les valeurs des statuts en tables
    


    private final String string;

    private Statut(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    /**
     * Permet de retourner un Status à partir de la valeur en table.
     * 
     * @param string
     * 			La valeur à convertir
     * @return
     */
    public static Statut getStatus(String string)
    {
    	switch (string)
    	{
    		case Valeur.RESOLVED :
    			return RESOLVED;
    		case Valeur.NOUVEAU :
    			return NOUVEAU;
    		case Valeur.WRKINPRG :
    			return WRKINPRG;
    		case Valeur.CLOSED :
    			return CLOSED;
    		case Valeur.TRANSFERED :
    			return TRANSFERED;
    		case Valeur.REFERRED :
    			return REFERRED;
    		case Valeur.PENDING :
    			return PENDING;
    		case Valeur.NONTMA :
    			return NONTMA;
            case Valeur.REJECTED :
                return REJECTED;
            case Valeur.ACHIFFRER :
                return ACHIFFRER;
            case Valeur.CHIFFRE :
                return CHIFFRE;
            case Valeur.ANALYSED :
                return ANALYSED;
    		default :
    			throw new IllegalArgumentException("Statut non connu : " + string);    			
    	}
    }
    
    /**
     * Permet de référencer les valeur des différents statuts possible en table
     * @author Tiggy Tamal
     * @since 1.0
     */
    private static class Valeur
    {
        private static final String RESOLVED = "Resolved";
        private static final String NOUVEAU = "Nouveau / Open";
        private static final String WRKINPRG = "Work In Progress";
        private static final String CLOSED = "Closed";
        private static final String TRANSFERED = "Transfered";
        private static final String REFERRED = "Referred";
        private static final String PENDING = "Pending xx";
        private static final String NONTMA = "Non traité par TMA";
        private static final String REJECTED = "Rejected";
        private static final String ACHIFFRER = "A chiffrer";
        private static final String CHIFFRE = "Chiffré";
        private static final String ANALYSED = "Analysed and transfered";       
    }
}

