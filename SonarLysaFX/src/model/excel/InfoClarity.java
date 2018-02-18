package model.excel;

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
