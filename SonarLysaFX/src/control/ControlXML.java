package control;

import static control.view.MainScreen.param;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import model.ParametreXML.TypeFichier;
import utilities.Statics;
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
	public void recuprerParamXML() throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
		ParametreXML temp;

		// Récupération du paramétrage depuis le fichier externe
		if (fichierParam.exists())
		{
			temp = (ParametreXML) context.createUnmarshaller().unmarshal(fichierParam);
		}
		// Récupération du paramétrage depuis le fichier interne
		else
		{
			temp = (ParametreXML) context.createUnmarshaller().unmarshal(getClass().getResourceAsStream("/resources/param.xml"));
		}

		// Contrôle des données
		createAlert(controleDonneesParam(temp));
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
		List<Application> apps = new ArrayList<>();
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

			apps.add(app);
		}
		wb.close();
		param.setListeApplications(apps);
		param.setDateMaj(TypeFichier.APPS);
		saveParam();
	}

	public void recupInfosClarityDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
	{
		ControlClarity control = new ControlClarity(file);
		Map<String, InfoClarity> clarity = control.recupInfosClarityExcel();
		control.close();
		param.setMapClarity(clarity);
		param.setDateMaj(TypeFichier.CLARITY);
		saveParam();
	}

	public void recupLotsPicDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
	{
		ControlPic controlPic = new ControlPic(file);
		Map<String, LotSuiviPic> lotsPic = controlPic.recupLotsDepuisPic();
		controlPic.close();
		param.setLotsPic(lotsPic);
		param.setDateMaj(TypeFichier.LOTSPICS);
		saveParam();
	}

	/*---------- METHODES PRIVEES ----------*/

	private String controleDonneesParam(ParametreXML temp)
	{
		// Variables
		Map<String, LotSuiviPic> lotsPic = temp.getLotsPic();
		List<Application> applis = temp.getListeApplications();
		Map<String, InfoClarity> clarity = temp.getMapClarity();
		StringBuilder builder = new StringBuilder();
		boolean manquant = false;

		// Contrôle lots Pic
		if (lotsPic == null || lotsPic.isEmpty())
		{
			builder.append("Données des lots Pic manquantes.").append(Statics.NL);
			manquant = true;
		}
		else
		{
			builder.append("Lots Pics chargés. Dernière Maj : ").append(temp.getDateMaj().get(TypeFichier.LOTSPICS)).append(Statics.NL);
			param.setLotsPic(lotsPic);
		}

		// Contrôle liste application
		if (applis == null || applis.isEmpty())
		{
			builder.append("Liste des apllications manquante.").append(Statics.NL);
			manquant = true;
		}
		else
		{
			builder.append("Liste des apllications chargée. Dernière Maj : ").append(temp.getDateMaj().get(TypeFichier.APPS)).append(Statics.NL);
			param.setListeApplications(applis);
		}

		// Contrôle Referentiel Clarity
		if (clarity == null || clarity.isEmpty())
		{
			builder.append("Informations referentiel Clarity manquantes.").append(Statics.NL);
			manquant = true;
		}
		else
		{
			builder.append("Referentiel Clarity chargé. Dernière Maj : ").append(temp.getDateMaj().get(TypeFichier.CLARITY)).append(Statics.NL);
			param.setMapClarity(clarity);
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