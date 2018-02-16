package model.xml;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParametreXML
{
	private List<Application> listeApplications;
	private Map<String, String> mapParams;

    @XmlElementWrapper
	@XmlElement(name = "listeApps", required = true)
	public List<Application> getListeApplications()
	{
		return listeApplications;
	}

	public void setListeApplications(List<Application> listeApplications)
	{
		this.listeApplications = listeApplications;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "mapParams", required = true)
    public Map<String, String> getMapParams()
    {
        return mapParams;
    }

    public void setMapParams(Map<String, String> mapParams)
    {
        this.mapParams = mapParams;
    }
}
