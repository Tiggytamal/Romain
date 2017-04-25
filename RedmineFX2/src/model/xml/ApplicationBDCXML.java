package model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationBDCXML
{
    /* ---------- ATTIBUTES ---------- */

    private String nom;
    private int bdc;
    private float taux;

    /* ---------- CONSTUCTORS ---------- */
    
    public ApplicationBDCXML()
    {
        
    }
    
    public ApplicationBDCXML(String nom, int bdc, float taux)
    {
        super();
        this.nom = nom;
        this.bdc = bdc;
        this.taux = taux;
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
     * @return the bdc
     */
    @XmlAttribute (required = true)
    public int getBdc()
    {
        return bdc;
    }

    /**
     * @param bdc
     *            the bdc to set
     */
    public void setBdc(int bdc)
    {
        this.bdc = bdc;
    }

    /**
     * @return the taux
     */
    @XmlAttribute (required = true)
    public float getTaux()
    {
        return taux;
    }

    /**
     * @param taux
     *            the taux to set
     */
    public void setTaux(float taux)
    {
        this.taux = taux;
    }
}
