package model.xml;

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
}