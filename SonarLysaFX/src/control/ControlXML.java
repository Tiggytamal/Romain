package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import model.xml.Application;
import model.xml.ParametreXML;


public class ControlXML
{
	/*---------- ATTRIBUTS ----------*/

	private ParametreXML param;
	private Map<String, Application> mapApplis;
	private static final String FICHIERPARAM = "d:\\param.xml";
	private static final String FICHIERLISTEAPPLI = "d:\\liste applis.xlsx";	
	
	/*---------- CONSTRUCTEURS ----------*/

	public ControlXML()
	{
		param = new ParametreXML();	
	}
	
	/*---------- METHODES PUBLIQUES ----------*/

	/**
	 * 
	 * @throws JAXBException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void recuprerParamXML() throws JAXBException, InvalidFormatException, IOException
	{	
	    File file = new File(FICHIERPARAM);
	    
		if(file.exists())
		{
			JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
			param = (ParametreXML) context.createUnmarshaller().unmarshal(file);
			mapApplis = new HashMap<>();
			for (Application app : param.getListeApplications())
			{
				mapApplis.put(app.getNom(), app);
			}
		}
		else
		{
			mapApplis = calculerMapApplisDepuisExcel();
			param.setListeApplications(new ArrayList<>(mapApplis.values()));
		}
	}
	
	/**
	 * 
	 * @throws JAXBException
	 */
	public void saveParam() throws JAXBException
	{
       JAXBContext context = JAXBContext.newInstance(ParametreXML.class);
       Marshaller jaxbMarshaller = context.createMarshaller();
       jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       jaxbMarshaller.marshal(param, new File(FICHIERPARAM));
	}
	
	/**
	 * 
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public Map<String, Application> calculerMapApplisDepuisExcel() throws InvalidFormatException, IOException
	{
		File file = new File(FICHIERLISTEAPPLI);
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