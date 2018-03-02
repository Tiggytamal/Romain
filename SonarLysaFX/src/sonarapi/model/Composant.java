package sonarapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Metrique> metriques;
    private String uuid;
    private boolean enabled;
    private String longName;
    private int projectId;
    private int subProjectId;

    /*---------- ACCESSEURS ----------*/

    @XmlAttribute(name = "id", required = false)
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute(name = "key", required = false)
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @XmlAttribute(name = "name", required = false)
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

    @XmlAttribute(name = "qualifier", required = false)
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
    @XmlElement(name = "measures", required = false)
    public List<Metrique> getMetriques()
    {
        if (metriques == null)
            return new ArrayList<>();
        return metriques;
    }

    /**
     * Permet de retourner une map des m�triques plut�t qu'une liste.<br>
     * clef = identifation du m�trique<br>
     * valeur = valeur du m�trique
     * 
     * @return
     */
    public Map<String, String> getMapMetriques()
    {
        if (metriques == null)
            return new HashMap<>();

        Map<String, String> retour = new HashMap<>();

        for (Metrique metrique : metriques)
        {
            retour.put(metrique.getMetric(), metrique.getValue());
        }
        return retour;
    }

    public void setMetriques(List<Metrique> metriques)
    {
        this.metriques = metriques;
    }

    @XmlAttribute(name = "uuid", required = false)
    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    @XmlAttribute(name = "enabled", required = false)
    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @XmlAttribute(name = "longName", required = false)
    public String getLongName()
    {
        return longName;
    }

    public void setLongName(String longName)
    {
        this.longName = longName;
    }

    @XmlAttribute(name = "projectId", required = false)
    public int getProjectId()
    {
        return projectId;
    }

    public void setProjectId(int projectId)
    {
        this.projectId = projectId;
    }

    @XmlAttribute(name = "subProjectId", required = false)
    public int getSubProjectId()
    {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId)
    {
        this.subProjectId = subProjectId;
    }
}