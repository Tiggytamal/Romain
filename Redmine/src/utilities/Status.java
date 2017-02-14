package utilities;

public enum Status
{
    RESOLVED("Resolved"),
    NOUVEAU("Nouveau / Open"),
    WRKINPRG("Work In Progress"),
    CLOSED("Closed"),
    TRANSFERED("Transfered"),
    REFERRED("Referred");

    private final String string;

    private Status(String string)
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
