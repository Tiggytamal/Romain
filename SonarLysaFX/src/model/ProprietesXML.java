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
        int nbreColKO = 0;
        int nbreParamKO = 0;
        StringBuilder builder = new StringBuilder("Chargement paramètres :").append(Statics.NL);
        for (TypeCol typeCol : TypeCol.values())
        {
            if (mapColonnes.get(typeCol) == null || mapColonnes.get(typeCol).isEmpty())
                nbreColKO++;
        }
        if (nbreColKO == 0)
            builder.append("Nom colonnes OK").append(Statics.NL);
        else
            builder.append("Certaines colonnes sont mal renseignées").append(Statics.NL);
        
        for (TypeParam typeParam : TypeParam.values())
        {
            if (mapParams.get(typeParam) == null || mapParams.get(typeParam).isEmpty())
            {
                nbreParamKO++;
            }
        }
        if (nbreParamKO == 0)
            builder.append("paramètres OK").append(Statics.NL);
        else
            builder.append("Certaines paramètres sont mal renseignés").append(Statics.NL);
        
        if (nbreParamKO != 0 || nbreColKO != 0)
            builder.append("Merci de changer les paramètres en option ou de recharger le fichier par défaut.");
        
        return builder.toString();
    }
}