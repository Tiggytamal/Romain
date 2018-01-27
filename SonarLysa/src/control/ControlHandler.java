package control;

import java.io.File;
import java.io.IOException;
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
import utilities.Statics;

public class ControlHandler
{
	
	/*---------- ATTRIBUTS ----------*/
	
	private final SonarAPI api;
	
	/*---------- CONSTRUCTEURS ----------*/

	public ControlHandler(String name, String password)
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
		List<Integer> listeLot = recuperationListeLotExcel(file);
		Map<String, View> mapQube = recupererLotsSonarQube();
		List<View> listeViewAjour = new ArrayList<>();
		for (Map.Entry<String, View> entry : mapQube.entrySet()) 
		{
			if (listeLot.contains(Integer.valueOf(entry.getKey())))
					listeViewAjour.add(entry.getValue());
		}
		listeViewAjour.size();		
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	private List<Integer> recuperationListeLotExcel(File file) throws InvalidFormatException, IOException
	{
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheetAt(0);
		List<Integer> listeLot = new ArrayList<>();
		int colLot = 0;
		
		//Récupération de la colonne ou les numéro de lot sont affichés.
		for (Cell cell : sheet.getRow(0)) 
		{
			if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Lot"))
			{
				colLot = cell.getColumnIndex();
			}
				
		}
		
		// parcours de la feuille Excel pour récupérer tous les lots sauf la première ligne
		for (int i = 1; i < sheet.getLastRowNum(); i++) 
		{
			listeLot.add((int)sheet.getRow(i).getCell(colLot).getNumericCellValue());
		}
		wb.close();
		return listeLot;
	}
	
	/*---------- ACCESSEURS ----------*/
}
