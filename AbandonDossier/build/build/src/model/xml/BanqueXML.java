package model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BanqueXML
{
    /* ---------- ATTIBUTES ---------- */

    private String nom;
    private String coad;
    private String coetb;
    private String codePlaque;

    /* ---------- CONSTUCTORS ---------- */
    
    /**
     * @param nom the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @param coad the coad to set
     */
    public void setCoad(String coad)
    {
        this.coad = coad;
    }

    /**
     * @param coetb the coetb to set
     */
    public void setCoetb(String coetb)
    {
        this.coetb = coetb;
    }

    /**
     * @param codePlaque the codePlaque to set
     */
    public void setCodePlaque(String codePlaque)
    {
        this.codePlaque = codePlaque;
    }

    public BanqueXML(String nom, String coad, String coetb, String codePlaque)
    {
        super();
        this.nom = nom;
        this.coad = coad;
        this.coetb = coetb;
        this.codePlaque = codePlaque;
    }   

    public BanqueXML()
    {
        
    }
    
    /* ---------- ACCESS ---------- */

    /**
     * @return the coetb
     */
    @XmlAttribute (required = true)
    public String getCoetb()
    {
        return coetb;
    }

    /**
     * @return the codePlaque
     */
    @XmlAttribute (required = true)
    public String getCodePlaque()
    {
        return codePlaque;
    }
    
    /**
     * @return the nom
     */
    @XmlAttribute (required = true)
    public String getNom()
    {
        return nom;
    }
    
    /**
     * @return the coad
     */
    @XmlAttribute (required = true)
    public String getCoad()
    {
        return coad;
    }
}