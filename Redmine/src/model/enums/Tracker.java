package model.enums;

public enum Tracker
{
	INCIDENT("incident"),
	PROBLEME("probleme"),
	DEMANDE("demande");
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
    		case "incident" :
    			return INCIDENT;
    		case "probleme" :
    			return PROBLEME;
    		case "demande" :
    			return DEMANDE;
    		default :
    			return null;
    	}
    }
}
