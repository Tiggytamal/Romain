package model.enums;

public enum Champ
{
    APPLICATION("Application"),
    DATEPRISENCHARGE("Date de prise en charge"),
    DA("n° de DA"),
    DATETRANSFERT("Date de transfert");
    
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
