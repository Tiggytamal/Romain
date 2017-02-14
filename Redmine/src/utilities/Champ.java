package utilities;

public enum Champ
{
    NONTMA("Non traité par TMA"),
    APPLICATION("Application"),
    DATEPRISENCHARGE("Date de prise en charge"),
    DA("n° de DA");
    
    private final String string;

    private Champ(String string)
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
