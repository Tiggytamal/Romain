package model.enums;

public enum Champ
{
    APPLICATION(Valeur.APPLICATION),
    DATEOUVERTURE(Valeur.DATEOUVERTURE),
    DATERESOLUTION(Valeur.DATERESOLUTION),
    DATEPERMIEREN3(Valeur.DATEPERMIEREN3),
    DATEPRISENCHARGE(Valeur.DATEPRISENCHARGE),
    DA(Valeur.DA),
    DATETRANSFERT(Valeur.DATETRANSFERT),
    NUMERO(Valeur.NUMERO),
    GRTRANSFERT(Valeur.GRTRANSFERT),
    ENVIRONNEMENT(Valeur.ENVIRONNEMENT),
    BANQUE(Valeur.BANQUE),
    TYPEDEMANDE(Valeur.TYPEDEMANDE),
    TECHNOLOGIE(Valeur.TECHNOLOGIE),
    DATECLOTURE(Valeur.DATECLOTURE),
    CODECLOTURE(Valeur.CODECLOTURE),
    REOUVERTURE(Valeur.REOUVERTURE),
    DATEREOUV(Valeur.DATEREOUV),
    DATERECLO(Valeur.DATERECLO),
    STATUTSM9(Valeur.STATUTSM9),
    FACTURE(Valeur.FACTURE),
    COMMENTAIREREOUV(Valeur.COMMENTAIREREOUV),
    DATECORRECTIONPREVUE(Valeur.DATECORRECTIONPREVUE);
    
    //TODO rajouter des champs si nécessaire. Vérifier les valeurs en table.
	
    private final String string;

    private Champ(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Champ getChamp(String champString)
    {
    	switch(champString)
    	{
    		case Valeur.APPLICATION :
    			return APPLICATION;
    			
    		case Valeur.DATEOUVERTURE :
    			return DATEOUVERTURE;
    			
    		case Valeur.DATERESOLUTION :
    			return DATERESOLUTION;
    			
    		case Valeur.DATEPERMIEREN3 :
    			return DATEPERMIEREN3;
    			
            case Valeur.DATEPRISENCHARGE :
                return DATEPRISENCHARGE;  
                
            case Valeur.DA :
                return DA;
                
            case Valeur.DATETRANSFERT :
                return DATETRANSFERT;
                
            case Valeur.NUMERO :
                return NUMERO;
                
            case Valeur.GRTRANSFERT :
                return GRTRANSFERT;
                
            case Valeur.ENVIRONNEMENT :
                return ENVIRONNEMENT;
                
            case Valeur.BANQUE :
                return BANQUE;
                
            case Valeur.TYPEDEMANDE :
                return TYPEDEMANDE;
                
            case Valeur.TECHNOLOGIE :
                return TECHNOLOGIE;
                
            case Valeur.DATECLOTURE :
                return DATECLOTURE;
                
            case Valeur.CODECLOTURE :
                return CODECLOTURE;
                
            case Valeur.REOUVERTURE :
                return REOUVERTURE; 
                
            case Valeur.DATEREOUV :
                return DATEREOUV;
                
            case Valeur.DATERECLO :
                return DATERECLO;
                
            case Valeur.STATUTSM9 :
                return STATUTSM9;
                
            case Valeur.FACTURE :
                return FACTURE;
                
            case Valeur.COMMENTAIREREOUV :
                return COMMENTAIREREOUV;
                
            case Valeur.DATECORRECTIONPREVUE :
                return DATECORRECTIONPREVUE;
         
    		default :
    			return null;
    	}
    }
    
    private static class Valeur
    {
        private static final String APPLICATION = "Application";
        private static final String DATEOUVERTURE = "Date d'ouverture";
        private static final String DATERESOLUTION = "Date de résolution";
        private static final String DATEPERMIEREN3 = "Date de 1ère affectation au N3";
        private static final String DATEPRISENCHARGE = "Date de prise en charge";
        private static final String DA = "n° de DA";
        private static final String DATETRANSFERT = "Date de transfert";
        private static final String NUMERO = "N° d'incident";
        private static final String GRTRANSFERT = "Groupe de transfert SM9";
        private static final String ENVIRONNEMENT = "Environnement";
        private static final String BANQUE = "Banque";
        private static final String TYPEDEMANDE = "Type demande";
        private static final String TECHNOLOGIE = "Technologie";
        private static final String DATECLOTURE = "Date de clôture";
        private static final String CODECLOTURE = "Code clôture";
        private static final String REOUVERTURE = "Ré-ouverture";
        private static final String DATEREOUV = "Date de ré-ouverture";
        private static final String DATERECLO = "Date de re-clôture";
        private static final String STATUTSM9 = "Statut de l'incident";
        private static final String FACTURE = "Facturé";
        private static final String COMMENTAIREREOUV = "Commentaire de ré-ouverture";
        private static final String DATECORRECTIONPREVUE = "Date de correction prévue";       
    }
}
