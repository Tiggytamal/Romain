package control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import sonarapi.SonarAPI;
import sonarapi.model.View;
import sonarapi.model.Views;
import utilities.DateConvert;
import utilities.Statics;

public class ControlAPI
{
	
	/*---------- ATTRIBUTS ----------*/
	
	private final SonarAPI api;
	
	/*---------- CONSTRUCTEURS ----------*/

	public ControlAPI(String name, String password)
	{
		api = SonarAPI.getInstance(Statics.URI, name, password);
	}

	/*---------- METHODES PUBLIQUES ----------*/

	/**
	 * 
	 * @return
	 */
	public Map<String, View> recupererLotsSonarQube()
	{
		Map<String, View> map = new HashMap<>();
		Views views = api.getViews();
		for (final View view : views.getListeViews())
		{
			if (view.getName().startsWith("Lot ")) 
			{
				map.put(view.getName().substring(4), view);
			}
		}
		return map;
	}

	public void creerVueMensuelle(File file) throws InvalidFormatException, IOException
	{
	    Map<String, List<View>> mapLot = recupMapLotExcel(file);	
	    
	    
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	private Map<String, List<View>> recupMapLotExcel(File file) throws InvalidFormatException, IOException
	{
	    // Création du workbook depuis le fichier excel
		Workbook wb = WorkbookFactory.create(file);
		
		// Récupération de la première feuille
		Sheet sheet = wb.getSheetAt(0);
		
		// Initialisation de la map de retour		
		Map<String, List<View>> retour = new HashMap<>();
		
		// Récupération depuis SOnar de tous les lots existants
	    Map<String, View> mapQube = recupererLotsSonarQube();
		
		// Index des colonnes des lots et des dates
		int colLot = 0;
		int colDate = 0;
		
		//Récupération des indices de colonnes pour le numérod e lot et la date de mise en production
		for (Cell cell : sheet.getRow(0)) 
		{
			if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Lot"))
			{
				colLot = cell.getColumnIndex();
			}
			else if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Livraison édition"))
			{
			    colDate = cell.getColumnIndex();
			}			
		}
		
		// parcours de la feuille Excel pour récupérer tous les lots et leurs dates de mise en production
		for (int i = 1; i < sheet.getLastRowNum(); i++) 
		{
		    Cell cellLot = sheet.getRow(i).getCell(colLot);
		    Cell cellDates = sheet.getRow(i).getCell(colDate);
		    
		    if (cellLot.getCellTypeEnum() != CellType.NUMERIC && cellDates.getCellTypeEnum() != CellType.NUMERIC)
		        continue;
		    
		    String lot = String.valueOf((int)cellLot.getNumericCellValue());
		    
		    if (mapQube.keySet().contains(lot))
		    {
		        LocalDate date = DateConvert.localDate(cellDates.getDateCellValue());
	            String clef = DateConvert.moisFrancais(date) + " " + date.getYear();
	            
	            if (retour.keySet().contains(clef))
	            {
	                retour.get(clef).add(mapQube.get(lot));
	            }
	            else
	            {
	                List<View> liste = new ArrayList<>();
	                liste.add(mapQube.get(lot));
	                retour.put(clef, liste);
	            }           	            
		    }   
		}
		wb.close();
		return retour;
	}
	
	/*---------- ACCESSEURS ----------*/
}
