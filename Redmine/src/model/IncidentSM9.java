package model;

public class IncidentSM9
{
    /* ---------- Attributes ---------- */
	
	private String numero;
	
	private String tracker;
	
	private String application;
	
	private String banque;
	
	private String environnement;
	
	private String priorite;
	
	private String sujet;
	
	private String assigne;
	
	private String commentaire;
	
	private long dateOuverture;
	
	private long datePriseEnCharge;
	
	private long dateResolution;
	
	private String reOuverture;
	
    /* --------- Constructors ---------- */
	
	public IncidentSM9()
	{
		
	}

	public IncidentSM9(String numero, String tracker, String application, String banque, String environnement, String priorite, String sujet, 
			String assigne, String commentaire, long dateOuverture, long datePriseEnCharge, long dateResolution, String reOuverture)
	{
		this.numero = numero;
		this.tracker = tracker;
		this.application = application;
		this.banque = banque;
		this.environnement = environnement;
		this.priorite = priorite;
		this.sujet = sujet;
		this.assigne = assigne;
		this.commentaire = commentaire;
		this.dateOuverture = dateOuverture;
		this.datePriseEnCharge = datePriseEnCharge;
		this.dateResolution = dateResolution;
		this.reOuverture = reOuverture;
	}
	
    /* ---------- Methods ---------- */
	
	

    /* ---------- Access ---------- */
	
	public String getNumero()
	{
		return numero;
	}

	public void setNumero(String numero)
	{
		this.numero = numero;
	}

	public String getTracker()
	{
		return tracker;
	}

	public void setTracker(String tracker)
	{
		this.tracker = tracker;
	}

	public String getApplication()
	{
		return application;
	}

	public void setApplication(String application)
	{
		this.application = application;
	}

	public String getBanque()
	{
		return banque;
	}

	public void setBanque(String banque)
	{
		this.banque = banque;
	}

	public String getEnvironnement()
	{
		return environnement;
	}

	public void setEnvironnement(String environnement)
	{
		this.environnement = environnement;
	}

	public String getPriorite()
	{
		return priorite;
	}

	public void setPriorite(String priorite)
	{
		this.priorite = priorite;
	}

	public String getSujet()
	{
		return sujet;
	}

	public void setSujet(String sujet)
	{
		this.sujet = sujet;
	}

	public String getAssigne()
	{
		return assigne;
	}

	public void setAssigne(String assigne)
	{
		this.assigne = assigne;
	}

	public String getCommentaire()
	{
		return commentaire;
	}

	public void setCommentaire(String commentaire)
	{
		this.commentaire = commentaire;
	}

	public long getDateOuverture()
	{
		return dateOuverture;
	}

	public void setDateOuverture(long dateOuverture)
	{
		this.dateOuverture = dateOuverture;
	}

	public long getDatePriseEnCharge()
	{
		return datePriseEnCharge;
	}

	public void setDatePriseEnCharge(long datePriseEnCharge)
	{
		this.datePriseEnCharge = datePriseEnCharge;
	}

	public long getDateResolution()
	{
		return dateResolution;
	}

	public void setDateResolution(long dateResolution)
	{
		this.dateResolution = dateResolution;
	}

	public String getReOuverture()
	{
		return reOuverture;
	}

	public void setReOuverture(String reOuverture)
	{
		this.reOuverture = reOuverture;
	}
}