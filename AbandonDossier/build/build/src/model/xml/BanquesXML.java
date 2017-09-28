package model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BanquesXML
{
    /* ---------- ATTIBUTES ---------- */

    private String nom;
    private String coad;

    /* ---------- CONSTUCTORS ---------- */
    
    public BanquesXML(String nom, String coad)
    {
        super();
        this.nom = nom;
        this.coad = coad;
    }   

    public BanquesXML()
    {
        
    }
    
    /* ---------- ACCESS ---------- */

    /**
     * @return the nom
     */
    @XmlAttribute (required = true)
    public String getNom()
    {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }   
    
    /**
     * @return the coad
     */
    public String getCoad()
    {
        return coad;
    }

    /**
     * @param coad the coad to set
     */
    public void setCoad(String coad)
    {
        this.coad = coad;
    }
}