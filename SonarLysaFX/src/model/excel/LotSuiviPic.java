package model.excel;

import java.time.LocalDate;

public class LotSuiviPic
{
	/*---------- ATTRIBUTS ----------*/

	private String lot;
	private String libelle;
	private String projetClarity;
	private String cpiProjet;
	private String edition;
	private int nbreComposants;
	private int nbrePaquets;
	private LocalDate build;
	private LocalDate devtu;
	private LocalDate tfon;
	private LocalDate vmoe;
	private LocalDate vmoa;
	private LocalDate livraison;
	
	/*---------- CONSTRUCTEURS ----------*/
	/*---------- METHODES PUBLIQUES ----------*/
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
	
	public String getLot()
	{
		return lot;
	}
	public void setLot(String lot)
	{
		this.lot = lot;
	}
	public String getLibelle()
	{
		return libelle;
	}
	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}
	public String getProjetClarity()
	{
		return projetClarity;
	}
	public void setProjetClarity(String projetClarity)
	{
		this.projetClarity = projetClarity;
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
	public int getNbreComposants()
	{
		return nbreComposants;
	}
	public void setNbreComposants(int nbreComposants)
	{
		this.nbreComposants = nbreComposants;
	}
	public int getNbrePaquets()
	{
		return nbrePaquets;
	}
	public void setNbrePaquets(int nbrePaquets)
	{
		this.nbrePaquets = nbrePaquets;
	}
	public LocalDate getBuild()
	{
		return build;
	}
	public void setBuild(LocalDate build)
	{
		this.build = build;
	}
	public LocalDate getDevtu()
	{
		return devtu;
	}
	public void setDevtu(LocalDate devtu)
	{
		this.devtu = devtu;
	}
	public LocalDate getTfon()
	{
		return tfon;
	}
	public void setTfon(LocalDate tfon)
	{
		this.tfon = tfon;
	}
	public LocalDate getVmoe()
	{
		return vmoe;
	}
	public void setVmoe(LocalDate vmoe)
	{
		this.vmoe = vmoe;
	}
	public LocalDate getVmoa()
	{
		return vmoa;
	}
	public void setVmoa(LocalDate vmoa)
	{
		this.vmoa = vmoa;
	}
	public LocalDate getLivraison()
	{
		return livraison;
	}
	public void setLivraison(LocalDate livraison)
	{
		this.livraison = livraison;
	}	
}