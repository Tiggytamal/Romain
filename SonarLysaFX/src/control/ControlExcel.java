package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	protected File file;
	protected Workbook wb;
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlExcel(File file) throws InvalidFormatException, IOException
	{
		this.file = file;
		createWb();
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
	protected void createWb() throws InvalidFormatException, IOException
	{
	    // Création du workbook depuis le fichier excel
		wb = WorkbookFactory.create(file);
	}
	
	protected void close() throws IOException
	{
		wb.close();
	}
		
	protected List<String> listAnomaliesSurLotsCrees()
	{
		// Récupération de la première feuille
		Sheet sheet = wb.getSheetAt(0);
		
		List<String> retour = new ArrayList<>();
		int colLot = 0;
		
		for (Cell cell : sheet.getRow(0)) 
		{
			if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Lot projet RTC"))
				colLot = cell.getColumnIndex();				
		}
		
		for (int i = 1; i < sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
			String string = row.getCell(colLot).getStringCellValue();
			if (string.startsWith("Lot "))
				string = string.substring(4);
		    retour.add(string);
		}
		return retour;
	}
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}
