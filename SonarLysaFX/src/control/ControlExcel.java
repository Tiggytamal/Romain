package control;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public abstract class ControlExcel
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
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}
