package model.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe d'import/export des paramètres au format XML
 * 
 * @author Gregoire Mathon
 *
 */
@XmlRootElement
public class Parametre implements Serializable
{
    /* ---------- ATTIBUTES ---------- */
	
	private static final long serialVersionUID = 1L;
	
	/** Paramètres */
	
	private String url;
	
	private String nomFichier;
	
    private List<BanqueXML> listBanqueXML;
	
    /* ---------- CONSTUCTORS ---------- */
	
    /**
	 * Constructeur par default
	 */
	public Parametre()
	{
		listBanqueXML = new ArrayList<>();
	}
	
    /**
     * Constructeur par default
     */
    public Parametre(String url, String nomDeFichier)
    {
        this();
        this.url = url;
        this.nomFichier = nomDeFichier;
    }

    /* ---------- METHODS ---------- */

    /* ---------- ACCESS ---------- */
	
	/**
     * @return the listAppsXML
     */
	@XmlElementWrapper
	@XmlElement (name = "BanqueXML")
    public List<BanqueXML> getListBanqueXML()
    {
	    if (listBanqueXML == null)
	        return new ArrayList<>();
        return listBanqueXML;
    }

    /**
     * @param listAppsXML the listAppsXML to set
     */
    public void setListBanqueXML(List<BanqueXML> listBanqueXML)
    {
        this.listBanqueXML = listBanqueXML;
    }

	
    @XmlAttribute (required = true)
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
    @XmlAttribute (required = true)
    public String getNomFichier()
    {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier)
    {
        this.nomFichier = nomFichier;
    }
}