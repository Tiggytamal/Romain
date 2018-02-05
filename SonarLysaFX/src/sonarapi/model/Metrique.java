package sonarapi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Metrique
{
	private String type;
	private String valeur;
	private List<Periode> listePeriodes;

	@XmlAttribute(name = "metric")
	public String getMetric()
	{
		return type;
	}

	public void setMetric(String type)
	{
		this.type = type;
	}

	@XmlAttribute(name = "value")
	public String getValue()
	{
		return valeur;
	}

	public void setValue(String value)
	{
		this.valeur = value;
	}

	@XmlElementWrapper
	@XmlElement(name = "periods")
	public List<Periode> getListePeriodes()
	{
		if (listePeriodes == null)
			return new ArrayList<>();
		return listePeriodes;
	}

	public void setListePeriodes(List<Periode> periods)
	{
		this.listePeriodes = periods;
	}
}