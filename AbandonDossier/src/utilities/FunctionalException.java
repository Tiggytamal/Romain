package utilities;

import utilities.enums.Severity;

public class FunctionalException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    private final Severity severity;
    
    private final boolean showException;


    public FunctionalException(Severity severity, String message, boolean show)
    {
        super(message);
        this.severity = severity;
        this.showException = show;
    }
    
    /**
     * @return the severity
     */
    public Severity getSeverity()
    {
        return severity;
    }

    /**
     * @return the showException
     */
    public boolean isShowException()
    {
        return showException;
    }   
}