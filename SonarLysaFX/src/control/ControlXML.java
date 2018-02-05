package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.xml.Application;
import model.xml.ParametreXML;


public class ControlXML
{
	/*---------- ATTRIBUTS ----------*/

	private ParametreXML param;
	private Map<String, Application> mapApplis;
	
	
	/*---------- CONSTRUCTEURS ----------*/

	public ControlXML()
	{
		param = new ParametreXML();		
	}
	
	/*---------- METHODES PUBLIQUES ----------*/

	public void recuprerParamXML() throws JAXBException, InvalidFormatException, IOException
	{
		File file = new File("d:\\param.xml");
		
		if(file.exists())
		{
			JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
			param = (ParametreXML) context.createUnmarshaller().unmarshal(file);
			mapApplis = new HashMap<String, Application>();
			for (Application app : param.getListeApplications())
			{
				mapApplis.put(app.getNom(), app);
			}
		}
		else
		{
			mapApplis = calculerMapApplisDepuisExcel();
			param.setListeApplications(new ArrayList<Application>(mapApplis.values()));
		}
	}
	
	public Map<String, Application> calculerMapApplisDepuisExcel() throws InvalidFormatException, IOException
	{
		File file = new File("d:\\liste applis.xlsx");
		Map<String, Application> retour = new HashMap<>();
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
			
			retour.put(app.getNom(), app);
		}
		wb.close();	
		return retour;
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	
	/*---------- ACCESSEURS ----------*/
	
	public ParametreXML getParam()
	{
		return param;
	}
		
	public Map<String, Application> getMapApplis()
	{
		return mapApplis;
	}
}
