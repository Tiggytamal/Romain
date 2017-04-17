package control;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import dao.DaoIncident;
import model.xml.Parametre;

public class XMLControl
{
    /* ---------- ATTIBUTES ---------- */

    /** Objet contenant les paramètres du programme venant du xml. */
    private Parametre param;
    
    /** Paramètres du programme. */
    private String user, pass, driver, jarPath, url;

    /** Doa pour les incidents */
    private DaoIncident daoi;
    
    /** COntroleur */
    private ListControl listControl;
     
    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        try
        {
            jarPath = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        listControl = ListControl.getInstance();
        listControl.setPath(jarPath);
        param = new Parametre();
    }
    
    private static class XMLControlHelper
    {
        private static final XMLControl INSTANCE = new XMLControl();
    }
    
    public static XMLControl getInstance()
    {
        return XMLControlHelper.INSTANCE;
    }
    
    /* ---------- METHODS ---------- */
    
    /**
     * Permet de créer le DAO pour les incidents à partir d'un fichier de paramétrage.
     * @return
     *      Une instance de <code>DaoIncident</code>
     */
    public DaoIncident creationDaoI()
    {
        if (!recupParam())
            recupDefault();
        HashMap<String, String> map = new HashMap<>(); // Creation map des propriétés
        DaoIncident daoi = new DaoIncident(map);
        saveParamJPA();
        return daoi;
    }
    
    /**
     * Récupère les paramètre de connexion depuis le fichier XML s'il est présent.
     * @return
     *        retourne vrai si le fichier est présent et que tous les paramètres sont bons.
     */
    private boolean recupParam()
    {
        File file = new File(jarPath + "param.xml"); // Creation objet correstondant au fichier XML
        boolean paramOK = false; // boolean pour vérifier si le fichier XML existe
        boolean allOK = false; // boolean pour vérifier si tous les paramètres sont bons
        
        if (file.exists())
        {
            try
            {
                // Création objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
                listControl.setParam(param);
                paramOK = true;

            } catch (JAXBException e)
            {
                System.out.println("Erreur dans l'extraction du XML");
            }
        }
        else
            System.out.println("Le fichier XML n'existe pas");
        
        // On vérifie que le XML contient bien tous les paramètres
        if (paramOK == true && (param.getDriver() == null || param.getPass() == null || param.getUrl() == null || param.getUser() == null))
            System.out.println("Certaines valeurs du XML sont nulles");
        else if (paramOK == true)
        {
            // Affectation des paramètres du programme
            driver = param.getDriver();
            user = param.getUser();
            pass = param.getPass();
            url = param.getUrl();
            allOK = true;
        }
        return allOK;
    }
    
    /**
     * Initialise les paramètres par default.
     */
    private void recupDefault()
    {
        System.out.println("Utilisation des valeurs par défault.");
        HashMap<String, String> map = new HashMap<>(); // Creation map des propriétés

        // On ajoute le paramètre dans la map s'il a été renseigné
        if (pass != null)
            map.put("javax.persistence.jdbc.password", pass);
        
        if (driver != null)
            map.put("javax.persistence.jdbc.driver", driver);
        
        if (url != null)
            map.put("javax.persistence.jdbc.url", url);
        
        if (user != null)
            map.put("javax.persistence.jdbc.user", user);

        // Creation de la connection et on récupère les paramètres
        daoi = new DaoIncident(map);
        driver = (String) daoi.getEm().getProperties().get("javax.persistence.jdbc.driver");
        url = (String) daoi.getEm().getProperties().get("javax.persistence.jdbc.url");
        user = (String) daoi.getEm().getProperties().get("javax.persistence.jdbc.user");
        pass = (String) daoi.getEm().getProperties().get("javax.persistence.jdbc.password");
    }
    
    /**
     * Sauvegarde les paramètres dans un XML.
     */
    private void saveParamJPA()
    {
        param.setDriver(driver);
        param.setPass(pass);
        param.setUrl(url);
        param.setUser(user);
        saveParametre();
    }
    
    public void saveParametre()
    {
        try
        {
            File file = new File(jarPath + "param.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(param, file);

        } catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

    /* ---------- ACCESS ---------- */
    
    
    /**
     * @return the jarPath
     */
    public String getJarPath()
    {
        return jarPath;
    }
}


