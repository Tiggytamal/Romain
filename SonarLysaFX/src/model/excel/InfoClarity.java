package model.excel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InfoClarity
{
    /*---------- ATTRIBUTS ----------*/

    private boolean actif;
    private String codeClarity;
    private String libelleProjet;
    private String chefProjet;
    private String edition;
    private String direction;
    private String departement;
    private String service;
    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    /**
     * @return the actif
     */
    @XmlAttribute (name = "actif")
    public boolean isActif()
    {
        return actif;
    }
    /**
     * @param actif the actif to set
     */
    public void setActif(boolean actif)
    {
        this.actif = actif;
    }
    /**
     * @return the codeProjet
     */
    @XmlAttribute (name = "codeClarity")
    public String getCodeClarity()
    {
        return codeClarity;
    }
    /**
     * @param codeProjet the codeProjet to set
     */
    public void setCodeClarity(String codeClarity)
    {
        this.codeClarity = codeClarity;
    }
    /**
     * @return the libelleProjet
     */
    @XmlAttribute (name = "libelleProjet")
    public String getLibelleProjet()
    {
        return libelleProjet;
    }
    /**
     * @param libelleProjet the libelleProjet to set
     */
    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }
    /**
     * @return the chefProjet
     */
    @XmlAttribute (name = "chefProjet")
    public String getChefProjet()
    {
        return chefProjet;
    }
    /**
     * @param chefProjet the chefProjet to set
     */
    public void setChefProjet(String chefProjet)
    {
        this.chefProjet = chefProjet;
    }
    /**
     * @return the edition
     */
    @XmlAttribute (name = "edition")
    public String getEdition()
    {
        return edition;
    }
    /**
     * @param edition the edition to set
     */
    public void setEdition(String edition)
    {
        this.edition = edition;
    }
    /**
     * @return the direction
     */
    @XmlAttribute (name = "direction")
    public String getDirection()
    {
        return direction;
    }
    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    /**
     * @return the departement
     */
    @XmlAttribute (name = "departement")
    public String getDepartement()
    {
        return departement;
    }
    /**
     * @param departement the departement to set
     */
    public void setDepartement(String departement)
    {
        this.departement = departement;
    }
    /**
     * @return the service
     */
    @XmlAttribute (name = "service")
    public String getService()
    {
        return service;
    }
    /**
     * @param service the service to set
     */
    public void setService(String service)
    {
        this.service = service;
    }
    
}
