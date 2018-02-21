package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;

import utilities.CellHelper;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Classe mère des contrôleurs pour les fichiers Excel
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public abstract class ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	protected File file;
	protected Workbook wb;
	protected CellHelper helper;
	protected int maxIndice;
	protected Row titres;
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlExcel(File file) throws InvalidFormatException, IOException
	{
		this.file = file;
		createWb();
		calculIndiceColonnes();
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
	protected void createWb() throws InvalidFormatException, IOException
	{
	    // Création du workbook depuis le fichier excel
		wb = WorkbookFactory.create(file);
		helper = new CellHelper(wb);
	}
	
	protected void close() throws IOException
	{
		wb.close();
	}
	
	protected void write() throws IOException
	{
		wb.write(new FileOutputStream(file.getName()));
	}
	
	/**
	 * Permet de changer la couleur de fond d'une ligne du fichier
	 * @param row
	 * @param couleur
	 */
	protected void majCouleurLigne(Row row, IndexedColors couleur)
	{
        for (int j = 0; j < row.getLastCellNum(); j++)
		{               
        	Cell cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			CellStyle style = wb.createCellStyle();
			style.cloneStyleFrom(cell.getCellStyle());					
			style.setFillForegroundColor(couleur.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell.setCellStyle(style);
		}
	}
	
	protected void autosizeColumns(Sheet sheet)
	{
		for (int i = 0; i <= maxIndice; i++)
		{
			sheet.autoSizeColumn(i);
		}
	}
	
	/**
	 * Initialise les numéro des colonnes du fichier Excel venant de la PIC. Calcul l'indice de la dernière colonne. et Copie la lignde  titre pour de futures feuilles
	 */
	protected abstract void calculIndiceColonnes();
	
	protected void testMax(int i)
	{
		if (maxIndice < i)
			maxIndice = i;
	}

	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}
