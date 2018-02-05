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



    /*---------- METHODES PRIVEES ----------*/
	

	
	   

	
	/*---------- ACCESSEURS ----------*/
}
