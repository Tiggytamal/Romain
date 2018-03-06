package model;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.enums.TypeCol;
import model.enums.TypeParam;
import utilities.Statics;

@XmlRootElement
public class ProprietesXML implements XML
{
    /*---------- ATTRIBUTS ----------*/

    private Map<TypeParam, String> mapParams;
    
    private Map<TypeCol, String> mapColonnes;
    
    private static final String NOMFICHIER = "\\proprietes.xml";

    /*---------- CONSTRUCTEURS ----------*/
    
    public ProprietesXML()
    {
        mapParams = new EnumMap<>(TypeParam.class); 
        mapColonnes = new EnumMap<>(TypeCol.class);
    }
    
    /*---------- ACCESSEURS ----------*/
   
    @XmlElementWrapper
    @XmlElement(name = "mapParams", required = false)
    public Map<TypeParam, String> getMapParams()
    {
        return mapParams;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "mapColonnes", required = false)
    public Map<TypeCol, String> getMapColonnes()
    {
        return mapColonnes;
    }

    @Override
    public File getFile()
    {
        return new File (Statics.jarPath + NOMFICHIER);
    }

    @Override
    public String controleDonnees()
    {
        // TODO Auto-generated method stub
        return null;
    }
}