package model;

import java.time.LocalDate;

import model.enums.Environnement;

/**
 * Classe de modèle uqi correspond aux données du fichier Excel des anomalies
 * 
 * @author ETP8137
 *
 */
public class Anomalie
{
    /*---------- ATTRIBUTS ----------*/

    private String direction;
    private String departement;
    private String service;
    private String responsableService;
    private String projetClarity;
    private String libelleProjet;
    private String cpiProjet;
    private String edition;
    private String lot;
    private String liensLot;
    private Environnement environnement;
    private int numeroAnomalie;
    private String liensAno;
    private String etat;
    private String typeAssemblage;
    private String securite;
    private String remarque;
    private String version;
    private LocalDate dateCreation;
    private LocalDate dateRelance;

    /*---------- CONSTRUCTEURS ----------*/

    public Anomalie()
    {
    }

    public Anomalie(LotSuiviPic lot)
    {
        this();
        majDepuisPic(lot);
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void majDepuisPic(LotSuiviPic lot)
    {
        setCpiProjet(lot.getCpiProjet());
        setEdition(lot.getEdition());
        setLibelleProjet(lot.getLibelle());
        setProjetClarity(lot.getProjetClarity());
        setLot("Lot " + lot.getLot());
        setEnvironnement(calculerEnvironnement(lot));
    }
    /*---------- METHODES PRIVEES ----------*/

    /**
     * Permet de valoriser l'environnement par rapport aux dates de publication
     * 
     * @param lot
     * @return
     */
    private Environnement calculerEnvironnement(LotSuiviPic lot)
    {
        if (lot.getLivraison() != null)
            return Environnement.EDITION;
        if (lot.getVmoa() != null)
            return Environnement.VMOA;
        if (lot.getVmoe() != null)
            return Environnement.VMOE;
        if (lot.getTfon() != null)
            return Environnement.TFON;
        if (lot.getDevtu() != null)
            return Environnement.DEVTU;
        return Environnement.NOUVEAU;
    }

    /*---------- ACCESSEURS ----------*/

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public String getDepartement()
    {
        return departement;
    }

    public void setDepartement(String departement)
    {
        this.departement = departement;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getResponsableService()
    {
        return responsableService;
    }

    public void setResponsableService(String responsableService)
    {
        this.responsableService = responsableService;
    }

    public String getProjetClarity()
    {
        return projetClarity;
    }

    public void setProjetClarity(String projetClarity)
    {
        this.projetClarity = projetClarity;
    }

    public String getLibelleProjet()
    {
        return libelleProjet;
    }

    public void setLibelleProjet(String libelleProjet)
    {
        this.libelleProjet = libelleProjet;
    }

    public String getCpiProjet()
    {
        return cpiProjet;
    }

    public void setCpiProjet(String cpiProjet)
    {
        this.cpiProjet = cpiProjet;
    }

    public String getEdition()
    {
        return edition;
    }

    public void setEdition(String edition)
    {
        this.edition = edition;
    }

    public String getLot()
    {
        return lot;
    }

    public void setLot(String lot)
    {
        this.lot = lot;
    }

    public Environnement getEnvironnement()
    {
        return environnement;
    }

    public void setEnvironnement(Environnement environnement)
    {
        this.environnement = environnement;
    }

    public int getNumeroAnomalie()
    {
        return numeroAnomalie;
    }

    public void setNumeroAnomalie(int numeroAnomalie)
    {
        this.numeroAnomalie = numeroAnomalie;
    }

    public String getEtat()
    {
        return etat;
    }

    public void setEtat(String etat)
    {
        this.etat = etat;
    }

    public String getSecurite()
    {
        return securite;
    }

    public void setSecurite(String securite)
    {
        this.securite = securite;
    }

    public String getRemarque()
    {
        return remarque;
    }

    public void setRemarque(String remarque)
    {
        this.remarque = remarque;
    }

    public String getLiensLot()
    {
        return liensLot;
    }

    public void setLiensLot(String liensLot)
    {
        this.liensLot = liensLot;
    }

    public String getLiensAno()
    {
        return liensAno;
    }

    public void setLiensAno(String liensAno)
    {
        this.liensAno = liensAno;
    }

    public String getTypeAssemblage()
    {
        return typeAssemblage;
    }

    public void setTypeAssemblage(String typeAssemblage)
    {
        this.typeAssemblage = typeAssemblage;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public void setDateCreation(LocalDate dateCreation)
    {
        this.dateCreation = dateCreation;
    }
    
    public LocalDate getDateCreation()
    {
        return dateCreation;
    }
    
    public void setDateRelance(LocalDate dateRelance)
    {
        this.dateRelance = dateRelance;
    }
    
    public LocalDate getDateRelance()
    {
        return dateRelance;
    }
}