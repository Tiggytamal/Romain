package bean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import dao.DaoConnexion;
import model.system.Connexion;
import utilities.Utilities;

@ManagedBean (name = "connexion")
@RequestScoped
public class ConnexionBean implements Serializable
{
    /* Attributes */

    private static final long serialVersionUID = 1L;

    private String pseudo;
    private String pass;

    // Dao
    @EJB
    private DaoConnexion daoc;

    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    /* Constructors */

    public ConnexionBean()
    {

    }

    /* Methods */

    /**
     * Vérifie pseudo et mot de passe correspondant à la base de données
     * 
     * @return
     *         la page de menu ou la même page si erreur de connexion
     */
    public String connexion()
    {
        Connexion connexion = daoc.findByNom(pseudo);
        if (connexion == null)
        {
            Utilities.updateGrowl(Utilities.getEl("#{b['message.erreurconnect']}"), FacesMessage.SEVERITY_ERROR, null);
            return "";
        }
        else
        {
            listBean.setConnect(pseudo);
            listBean.setConnectok(true);
            return "index2?faces-redirect=true";
        }
    }

    /**
     * Déconnection du programme
     * 
     * @return
     */
    public String deconnexion()
    {
        listBean.setConnectok(false);
        listBean.setConnect("");
        return "index";
    }

    /* Access */

    public String getPseudo()
    {
        return pseudo;
    }

    public void setPseudo(String pseudo)
    {
        this.pseudo = pseudo;
    }

    public String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public ListBean getListBean()
    {
        return listBean;
    }

    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }
}