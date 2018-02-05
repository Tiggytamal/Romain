package sonarapi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "component")
public class Composant implements ModeleSonar
{
	/*---------- ATTRIBUTS ----------*/

	private String id;
	private String key;
	private String nom;
	private String descritpion;
	private String qualifier;
	private String langage;
	private String path;
	private List<Metrique> listeMeriques;

	/*---------- ACCESSEURS ----------*/

	@XmlAttribute(name = "id")
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@XmlAttribute(name = "key")
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@XmlAttribute(name = "name")
	public String getNom()
	{
		return nom;
	}

	public void setNom(String name)
	{
		this.nom = name;
	}

	@XmlAttribute(name = "description", required = false)
	public String getDescritpion()
	{
		return descritpion;
	}

	public void setDescritpion(String descritpion)
	{
		this.descritpion = descritpion;
	}

	@XmlAttribute(name = "qualifier")
	public String getQualifier()
	{
		return qualifier;
	}

	public void setQualifier(String qualifier)
	{
		this.qualifier = qualifier;
	}

	@XmlAttribute(name = "language", required = false)
	public String getLangage()
	{
		return langage;
	}

	public void setLangage(String langage)
	{
		this.langage = langage;
	}

	@XmlAttribute(name = "path", required = false)
	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	@XmlElementWrapper
	@XmlElement(name = "measures")
	public List<Metrique> getListeMeriques()
	{
		if (listeMeriques == null)
			return new ArrayList<>();
		return listeMeriques;
	}

	public void setListeMeriques(List<Metrique> listeMeriques)
	{
		this.listeMeriques = listeMeriques;
	}
}