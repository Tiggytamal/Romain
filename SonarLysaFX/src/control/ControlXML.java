package control;

import static control.view.MainScreen.param;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import application.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import model.Application;
import model.InfoClarity;
import model.LotSuiviPic;
import model.ParametreXML;
import model.enums.TypeFichier;
import utilities.Statics;
import utilities.TechnicalException;
import utilities.Utilities;

/**
 * Classe de gestion des paramètres de XML de l'application
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public class ControlXML
{
    /*---------- ATTRIBUTS ----------*/

    private File fichierParam;
    private String jarPath;

    /*---------- CONSTRUCTEURS ----------*/

    public ControlXML()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        fichierParam = new File(jarPath + "\\param.xml");
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * Récupère le paramètre depuis le fichier externe ou celui interne par default s'il n'existe pas.
     * 
     * @throws JAXBException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public ParametreXML recuprerParamXML()
    {
        JAXBContext context;
        ParametreXML param;
        try
        {
            context = JAXBContext.newInstance(ParametreXML.class);
            // Récupération du paramétrage depuis le fichier externe
            if (fichierParam.exists())
            {
                param = (ParametreXML) context.createUnmarshaller().unmarshal(fichierParam);
            }
            // Récupération du paramétrage depuis le fichier interne
            else
            {
                param = (ParametreXML) context.createUnmarshaller().unmarshal(getClass().getResourceAsStream("/resources/param.xml"));
            }
        } catch (JAXBException e)
        {
            throw new TechnicalException("Impossible de récupérer le fichier de paramètre", e);
        }

        // Contrôle des données et affiche le message de chargement
        createAlert(controleDonneesParam(param));
        return param;
    }

    /**
     * Sauvegarde le fichier de paramètres
     * 
     * @throws JAXBException
     */
    public void saveParam() throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(param, fichierParam);
    }

    /**
     * 
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     * @throws JAXBException
     */
    public void recupListeAppsDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
    {
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheetAt(0);

        for (Row row : sheet)
        {
            Application app = new Application();
            Cell cell = row.getCell(0);

            if (cell.getCellTypeEnum() == CellType.STRING)
                app.setNom(row.getCell(0).getStringCellValue());
            else
                app.setNom(String.valueOf((int) row.getCell(0).getNumericCellValue()));

            String actif = row.getCell(1).getStringCellValue();
            if ("Actif".equals(actif))
                app.setActif(true);
            else
                app.setActif(false);

            param.getListeApplications().add(app);
        }
        wb.close();
        param.setDateFichier(TypeFichier.APPS);
        saveParam();
    }

    public void recupInfosClarityDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
    {
        ControlClarity control = new ControlClarity(file);
        Map<String, InfoClarity> clarity = control.recupInfosClarityExcel();
        control.close();
        param.getMapClarity().putAll(clarity);
        param.setDateFichier(TypeFichier.CLARITY);
        saveParam();
    }

    public void recupLotsPicDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
    {
        ControlPic controlPic = new ControlPic(file);
        Map<String, LotSuiviPic> lotsPic = controlPic.recupLotsDepuisPic();
        controlPic.close();
        param.getLotsPic().putAll(lotsPic);
        param.setDateFichier(TypeFichier.LOTSPICS);
        saveParam();
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * Contrôle les informations des différentes maps
     * 
     * @param param
     * @return
     */
    private String controleDonneesParam(ParametreXML param)
    {
        // Variables
        Map<String, LotSuiviPic> lotsPic = param.getLotsPic();
        List<Application> applis = param.getListeApplications();
        Map<String, InfoClarity> clarity = param.getMapClarity();
        StringBuilder builder = new StringBuilder();
        boolean manquant = false;

        // Contrôle lots Pic
        if (lotsPic.isEmpty())
        {
            builder.append("Données des lots Pic manquantes.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Lots Pics chargés. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.LOTSPICS)).append(Statics.NL);
        }

        // Contrôle liste application
        if (applis.isEmpty())
        {
            builder.append("Liste des apllications manquante.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Liste des apllications chargée. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.APPS)).append(Statics.NL);
        }

        // Contrôle Referentiel Clarity
        if (clarity.isEmpty())
        {
            builder.append("Informations referentiel Clarity manquantes.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Referentiel Clarity chargé. Dernière Maj : ").append(param.getDateMaj().get(TypeFichier.CLARITY)).append(Statics.NL);
        }

        if (manquant)
            builder.append("Merci de recharger le(s) fichier(s) de paramétrage");

        return builder.toString();
    }

    private void createAlert(String texte)
    {
         Alert alert = new Alert(AlertType.INFORMATION);
         alert.initStyle(StageStyle.UTILITY);
         alert.initModality(Modality.NONE);
         alert.setContentText(texte);
         alert.setHeaderText(null);
         alert.show();
         alert.setHeaderText(null);
    }

    /*---------- ACCESSEURS ----------*/
}