package model;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParametreXML
{
	/*---------- ATTRIBUTS ----------*/

	private List<Application> listeApplications;
	private Map<TypeParam, String> mapParams;
	private Map<String, InfoClarity> mapClarity; 
	private Map<String, LotSuiviPic> lotsPic;
	private Map<String, Boolean> mapApplis;
	private Map<TypeFichier, String> dateMaj;

	/*---------- CONSTRUCTEURS ----------*/
	
    public ParametreXML()
	{
	    listeApplications = new ArrayList<>();
	    mapParams = new EnumMap<>(TypeParam.class);
	    mapClarity = new HashMap<>();
	    lotsPic = new HashMap<>();
	    mapApplis = new HashMap<>();
	    dateMaj = new EnumMap<>(TypeFichier.class);
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
    public Map<TypeParam, String> getMapParams()
    {
        return mapParams;
    }

    public void setMapParams(Map<TypeParam, String> mapParams)
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
    public Map<TypeFichier, String> getDateMaj()
    {
        return dateMaj;
    }
    
    public void setDateMaj(Map<TypeFichier, String> dateMaj)
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
	
	/**
	 * Change la date de mise à jour d'un type de fichier.
	 * @param clef
	 */
	public void setDateFichier(TypeFichier fichier)
	{
		dateMaj.put(fichier, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy",Locale.FRANCE)));
	}
	
	public enum TypeFichier implements Serializable
	{
		APPS, CLARITY, LOTSPICS;
	}
	
	public enum TypeParam implements Serializable
	{
		VERSIONS;
	}
}