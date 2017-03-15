package model.system;

import java.util.Date;

/**
 * Classe représentant une ligne dans la feuille Excel du stock SM9
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class IncidentSM9
{
    /* ---------- ATTIBUTES ---------- */
 
    private String numero;

    private String tracker;
    
    private String application;
    
    private String banque;
    
    private String environnement;
    
    private String priorite;
    
    private String sujet;
    
    private String assigne;
    
    private String statut;
    
    private Date dateOuvertute; 
    
    private Date datePriseEnCharge;
    
    private Date dateResolution;
    
    private String reouverture;
    
    /* ---------- CONSTUCTORS ---------- */
    
    public IncidentSM9()
    {
        
    }
    
    /* ---------- METHODS ---------- */
    
    /* ---------- ACCESS ---------- */
        
    /**
     * @return the numero
     */
    public String getNumero()
    {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    /**
     * @return the tracker
     */
    public String getTracker()
    {
        return tracker;
    }

    /**
     * @param tracker the tracker to set
     */
    public void setTracker(String tracker)
    {
        this.tracker = tracker;
    }

    /**
     * @return the application
     */
    public String getApplication()
    {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(String application)
    {
        this.application = application;
    }

    /**
     * @return the banque
     */
    public String getBanque()
    {
        return banque;
    }

    /**
     * @param banque the banque to set
     */
    public void setBanque(String banque)
    {
        this.banque = banque;
    }

    /**
     * @return the environnement
     */
    public String getEnvironnement()
    {
        return environnement;
    }

    /**
     * @param environnement the environnement to set
     */
    public void setEnvironnement(String environnement)
    {
        this.environnement = environnement;
    }

    /**
     * @return the priorite
     */
    public String getPriorite()
    {
        return priorite;
    }

    /**
     * @param priorite the priorite to set
     */
    public void setPriorite(String priorite)
    {
        this.priorite = priorite;
    }

    /**
     * @return the sujet
     */
    public String getSujet()
    {
        return sujet;
    }

    /**
     * @param sujet the sujet to set
     */
    public void setSujet(String sujet)
    {
        this.sujet = sujet;
    }

    /**
     * @return the assigne
     */
    public String getAssigne()
    {
        return assigne;
    }

    /**
     * @param assigne the assigne to set
     */
    public void setAssigne(String assigne)
    {
        this.assigne = assigne;
    }

    /**
     * @return the statut
     */
    public String getStatut()
    {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut)
    {
        this.statut = statut;
    }

    /**
     * @return the dateOuvertute
     */
    public Date getDateOuvertute()
    {
        return dateOuvertute;
    }

    /**
     * @param dateOuvertute the dateOuvertute to set
     */
    public void setDateOuvertute(Date dateOuvertute)
    {
        this.dateOuvertute = dateOuvertute;
    }

    /**
     * @return the datePriseEnCharge
     */
    public Date getDatePriseEnCharge()
    {
        return datePriseEnCharge;
    }

    /**
     * @param datePriseEnCharge the datePriseEnCharge to set
     */
    public void setDatePriseEnCharge(Date datePriseEnCharge)
    {
        this.datePriseEnCharge = datePriseEnCharge;
    }

    /**
     * @return the dateResolution
     */
    public Date getDateResolution()
    {
        return dateResolution;
    }

    /**
     * @param dateResolution the dateResolution to set
     */
    public void setDateResolution(Date dateResolution)
    {
        this.dateResolution = dateResolution;
    }

    /**
     * @return the reouverture
     */
    public String getReouverture()
    {
        return reouverture;
    }

    /**
     * @param reouverture the reouverture to set
     */
    public void setReouverture(String reouverture)
    {
        this.reouverture = reouverture;
    }
}