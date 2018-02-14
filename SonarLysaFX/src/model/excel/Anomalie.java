package model.excel;

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
	private Environnement environnement;
	private String numeroAnomalie;
	private String etat;
	private String remarque;
	
	/*---------- CONSTRUCTEURS ----------*/
	
	public Anomalie() {}
	
	public Anomalie(LotSuiviPic lot)
	{
		this();
		setCpiProjet(lot.getCpiProjet());
		setEdition(lot.getEdition());
		setLibelleProjet(lot.getLibelle());
		setProjetClarity(lot.getProjetClarity());
		setLot("Lot " + lot.getLot());
		setEnvironnement(calculerEnvironnement(lot));		
	}
	/*---------- METHODES PUBLIQUES ----------*/
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
	public String getnumeroAnomalie()
	{
		return numeroAnomalie;
	}
	public void setnumeroAnomalie(String numeroAnomalie)
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
	public String getRemarque()
	{
		return remarque;
	}
	public void setRemarque(String remarque)
	{
		this.remarque = remarque;
	}	
}