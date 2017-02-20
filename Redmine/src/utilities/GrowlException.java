package utilities;

import javax.faces.application.FacesMessage.Severity;

public class GrowlException extends Exception
{

    private static final long serialVersionUID = 1L;

    private String message;
    private Severity severity;
    private String detail;
    
    public GrowlException(Severity severity, String message, String detail)
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
