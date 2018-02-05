package model.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParametreXML
{
	private List<Application> listeApplications;

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
}
