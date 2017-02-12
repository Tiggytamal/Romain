package utilities;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Classe de méthode utilitaires statiques
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class Utilities
{
    /**
     * Récupère une expression du bundle de langue
     * 
     * @param El
     * valeur de l'expression language
     * @return
     * le texte correspondant
     */
    public static String getEl(String El)
    {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getApplication().evaluateExpressionGet(ctx, El, String.class);
    }
    
    /**
     * permet de mettre a jour un message dans une page jsf
     * 
     * @param message
     */
    public static void updateGrowl(String message)
    {
        // Crée le FaceMessage
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);

        // Ajoute le message au contexte
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    /**
     * Permet de mettre à jour un message dans une page JSF en ajoutant la sévérité
     * 
     * @param severity
     * @param message
     */
    public static void updateGrowl(Severity severity, String message)
    {
        // Crée le FaceMessage
        FacesMessage fm = new FacesMessage(severity, message, null);

        // Ajoute le message au contexte
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    /**
     * Permet de mettre à jour un message dans une page JSF en ajoutant la sévérité et le détail de l'erreur
     * 
     * @param severity
     * @param message
     */
    public static void updateGrowl(Severity severity, String message, String detail)
    {
        // Crée le FaceMessage
        FacesMessage fm = new FacesMessage(severity, message, detail);

        // Ajoute le message au contexte
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
}
