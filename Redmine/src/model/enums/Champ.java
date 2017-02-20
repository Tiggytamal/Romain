package model.enums;

public enum Champ
{
    APPLICATION("Application"),
    DATEOUVERTURE("Date d'ouverture"),
    DATERESOLUTION("Date de résolution"),
    DATEPERMIEREN3("Date de 1ère affectation au N3"),
    DATEPRISENCHARGE("Date de prise en charge"),
    DA("n° de DA"),
    DATETRANSFERT("Date de transfert"),
    NUMERO("N° d'incident"),
    GRTRANSFERT("Groupe de transfert"),
    ENVIRONNEMENT("Environnement"),
    BANQUE("Banque"),
    TYPEDEMANDE("Type demande"),
    TECHNOLOGIE("Technologie"),
    DATECLOTURE("Date de clôture"),
    CODECLOTURE("Code clôture"),
    REOUVERTURE("Ré-ouverture"),
    DATEREOUV("Date de ré-ouverture"),
    DATERECLO("Date de re-clôture"),
    STATUTSM9("Statut de l'incident"),
    FACTURE("Facturé"),
    COMMENTAIREREOUV("Commentaire de ré-ouverture"),
    DATECORRECTIONPREVUE("Date de correction prévue");
    
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
    		case "Application" :
    			return APPLICATION;
    		case "Date de prise en charge" :
    			return DATEPRISENCHARGE;
    		case "n° de DA" :
    			return DA;
    		case "Date de transfert" :
    			return DATETRANSFERT;
    		default :
    			return null;
    	}
    }
}
