package utilities;

public enum Champs
{
    APPLICATION("Application"),
    DATEPRISENCHARGE("Date de prise en charge"),
    RESOLVED("Resolved"),
    NOUVEAU("Nouveau / Open"),
    WRKINPRG("Work In Progress"),
    CLOSED("Closed"),
    TRANSFERED("Transfered"),
    REFERRED("Referred"),
    NONTMA("Non traité par TMA");

    private final String string;

    private Champs(String string)
    {
        this.string = string;
    }
    
    /**
     * @return the string
     */
    public String getString()
    {
        return string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public boolean equals(String otherString)
    {
        return string.equals(otherString);
    }
}
