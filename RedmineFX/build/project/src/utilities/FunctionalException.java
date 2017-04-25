package utilities;

import utilities.enums.Severity;

public class FunctionalException extends Exception
{

    private static final long serialVersionUID = 1L;

    private String message;
    private Severity severity;
    private String detail;
    
    public FunctionalException(Severity severity, String message, String detail)
    {
        this.message = message;
    }
    
    /**
     * @return the severity
     */
    public Severity getSeverity()
    {
        return severity;
    }
    /**
     * @return the detail
     */
    public String getDetail()
    {
        return detail;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    
}
