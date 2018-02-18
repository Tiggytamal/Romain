package utilities;

import utilities.enums.Severity;

/**
 * Erreur fonctionnelle de l'application
 * 
 * @author ETP8137 - Gr�goire Mathon
 *
 */
public class FunctionalException extends RuntimeException
{
    private static final long serialVersionUID = 1L;    
    private final Severity severity;  

    /**
     * Constructeur des erreurs fonctionnelles.<br>
     * Choix de la s�v�rit� (INFO ou ERROR), du message afficher, ainsi que de l'affichage ou non de la fen�tre du stacktrace
     */
    public FunctionalException(Severity severity, String message)
    {
        super(message);
        this.severity = severity;
    }
    
    /**
     * @return the severity
     */
    public Severity getSeverity()
    {
        return severity;
    }
}