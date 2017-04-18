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
	private String user, pass, url, driver;
	
	private List<ApplicationBDCXML> listAppsXML;
	
    /* ---------- CONSTUCTORS ---------- */
	
    /**
	 * Constructeur par default
	 */
	public Parametre()
	{
		listAppsXML = new ArrayList<>();
	}

    /* ---------- METHODS ---------- */

    /* ---------- ACCESS ---------- */
	
	/**
     * @return the listAppsXML
     */
	@XmlElementWrapper
	@XmlElement (name = "AppsXML")
    public List<ApplicationBDCXML> getListAppsXML()
    {
        return listAppsXML;
    }

    /**
     * @param listAppsXML the listAppsXML to set
     */
    public void setListAppsXML(List<ApplicationBDCXML> listAppsXML)
    {
        this.listAppsXML = listAppsXML;
    }

	
	@XmlAttribute (required = true)
	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	@XmlAttribute (required = true)
	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
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
	public String getDriver()
	{
		return driver;
	}

	public void setDriver(String driver)
	{
		this.driver = driver;
	}
}