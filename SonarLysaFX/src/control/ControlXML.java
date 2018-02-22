package control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import control.view.MainScreen;
import model.excel.InfoClarity;
import model.excel.LotSuiviPic;
import model.xml.Application;
import model.xml.ParametreXML;
import model.xml.ParametreXML.TypeFichier;
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
	public ParametreXML recuprerParamXML() throws JAXBException
	{	
	    JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
	    ParametreXML retour;
	    
	    // Récupération du paramétrage depuis le fichier externe
		if(fichierParam.exists())
		{
		    retour = (ParametreXML) context.createUnmarshaller().unmarshal(fichierParam);			
		}
		// Récupération du paramétrage depuis le fichier interne
		else
		{
		    retour = (ParametreXML) context.createUnmarshaller().unmarshal(getClass().getResourceAsStream("/resources/param.xml"));
		}
        return retour;
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
       jaxbMarshaller.marshal(MainScreen.getParam(), fichierParam);
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
				app.setNom(String.valueOf((int)row.getCell(0).getNumericCellValue()));
			
			String actif = row.getCell(1).getStringCellValue();
			if ("Actif".equals(actif))
				app.setActif(true);
			else
				app.setActif(false);
			
			apps.add(app);
		}
		wb.close();	
		MainScreen.getParam().setListeApplications(apps);
	    MainScreen.getParam().setDateMaj(TypeFichier.APPS);
        saveParam();		
	}
	
	public void recupInfosClarityDepuisExcel(File file) throws InvalidFormatException, IOException, JAXBException
	{
	      ControlClarity control = new ControlClarity(file);
	      Map<String, InfoClarity> clarity = control.recupInfosClarityExcel();
	      control.close();
	      MainScreen.getParam().setMapClarity(clarity);
	      MainScreen.getParam().setDateMaj(TypeFichier.CLARITY);
	      saveParam();              
	}
	
	public void recupLotsPicDepuisExcel(File file) throws IOException, InvalidFormatException, JAXBException
	{
		ControlPic controlPic = new ControlPic(file);
		Map<String, LotSuiviPic> lotsPic = controlPic.recupLotsDepuisPic();
		controlPic.close();
		MainScreen.getParam().setLotsPic(lotsPic);
	    MainScreen.getParam().setDateMaj(TypeFichier.LOTSPICS);
		saveParam();
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	/*---------- ACCESSEURS ----------*/
}