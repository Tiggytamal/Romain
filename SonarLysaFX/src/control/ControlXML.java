package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import model.excel.InfoClarity;
import model.xml.Application;
import model.xml.ParametreXML;
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

	private ParametreXML param;
	private File fichierParam;
	private Map<String, Boolean> mapApplis;
	private String jarPath;
	
	/*---------- CONSTRUCTEURS ----------*/

	public ControlXML()
	{
		param = new ParametreXML();	
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        mapApplis = new HashMap<>();
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
	    
	    // Récupération du paramétrage depuis le fichier externe
		if(fichierParam.exists())
		{
	        param = (ParametreXML) context.createUnmarshaller().unmarshal(fichierParam);			
		}
		// Récupération du paramétrage depuis le fichier interne
		else
		{
	        param = (ParametreXML) context.createUnmarshaller().unmarshal(getClass().getResourceAsStream("/resources/param.xml"));
		}
		
        for (Application app : param.getListeApplications())
        {
            mapApplis.put(app.getNom(), app.isActif());
        }
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
	 */
	public void calculerListeAppsDepuisExcel(File file) throws InvalidFormatException, IOException
	{
		List<Application> retour = new ArrayList<>();
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
			
			retour.add(app);
		}
		wb.close();	
		param.setListeApplications(retour);
	}
	
	public Map<String, InfoClarity> recupInfosClarityDepuisExcel(File file) throws InvalidFormatException, IOException
	{
	      ControlClarity control = new ControlClarity(file);
	      Map<String, InfoClarity> retour = control.recupInfosClarityExcel();
	      param.setMapClarity(retour);
	      return retour;	              
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	/*---------- ACCESSEURS ----------*/
	
	public ParametreXML getParam()
	{
		return param;
	}
		
	public Map<String, Boolean> getMapApplis()
	{
		return mapApplis;
	}
}