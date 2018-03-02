package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Application
{
    private String nom;
    private boolean actif;

    @XmlAttribute(name = "nom", required = true)
    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @XmlAttribute(name = "actif", required = true)
    public boolean isActif()
    {
        return actif;
    }

    public void setActif(boolean actif)
    {
        this.actif = actif;
    }

}
