package model.xml;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.excel.InfoClarity;
import model.excel.LotSuiviPic;

@XmlRootElement
public class ParametreXML
{
	/*---------- ATTRIBUTS ----------*/

	private List<Application> listeApplications;
	private Map<String, String> mapParams;
	private Map<String, InfoClarity> mapClarity; 
	private Map<String, LotSuiviPic> lotsPic;
	private Map<String, Boolean> mapApplis;
	private Map<String, LocalDate> dateMaj;

    public ParametreXML()
	{
	    listeApplications = new ArrayList<>();
	    mapParams = new HashMap<>();
	    mapClarity = new HashMap<>();
	    lotsPic = new HashMap<>();
	    mapApplis = new HashMap<>();
	    dateMaj = new HashMap<>();
	}
	
	/*---------- ACCESSEURS ----------*/

	@XmlElementWrapper
	@XmlElement(name = "listeApps", required = false)
	public List<Application> getListeApplications()
	{
		return listeApplications;
	}

	public void setListeApplications(List<Application> listeApplications)
	{
		this.listeApplications = listeApplications;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "mapParams", required = false)
    public Map<String, String> getMapParams()
    {
        return mapParams;
    }

    public void setMapParams(Map<String, String> mapParams)
    {
        this.mapParams = mapParams;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapClarity", required = false)
    public Map<String, InfoClarity> getMapClarity()
	{
		return mapClarity;
	}

	public void setMapClarity(Map<String, InfoClarity> mapClarity)
	{
		this.mapClarity = mapClarity;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "maplotsPic", required = false)
	public Map<String, LotSuiviPic> getLotsPic()
	{
		return lotsPic;
	}
 
	public void setLotsPic(Map<String, LotSuiviPic> lotsPic)
	{
		this.lotsPic = lotsPic;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "dateMaj", required = false)
    public Map<String, LocalDate> getDateMaj()
    {
        return dateMaj;
    }

    public void setDateMaj(Map<String, LocalDate> dateMaj)
    {
        this.dateMaj = dateMaj;
    }
	
	/**
	 * Permet de remonter la liste des applications sous forme d'une map (clef = nom application / valeur = etat)
	 * @return
	 */
	@Transient
	public Map<String, Boolean> getMapApplis()
	{
	    if (mapApplis.isEmpty())
	    {   
            for (Application app : listeApplications)
            {
                mapApplis.put(app.getNom(), app.isActif());
            }
	    }	    
        return mapApplis;
	}
}