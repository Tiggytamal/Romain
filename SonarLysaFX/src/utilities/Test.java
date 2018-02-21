package utilities;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		File file = new File("d:\\hyperlinks.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet spreadsheet = wb.createSheet("Hyperlinks");
		Cell cell;
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle hlinkstyle = wb.createCellStyle();
		Font hlinkfont = wb.createFont();
		hlinkfont.setUnderline(Font.U_SINGLE);
		hlinkfont.setColor(IndexedColors.BLUE.index);
		hlinkstyle.setFont(hlinkfont);

		// URL Link
		cell = spreadsheet.createRow(1).createCell((short) 1);
		cell.setCellValue("URL Link");
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
		link.setAddress("http://www.tutorialspoint.com/");
		cell.setHyperlink(link);
		cell.setCellStyle(hlinkstyle);

		// Hyperlink to a file in the current directory
		cell = spreadsheet.createRow(2).createCell((short) 1);
		cell.setCellValue("File Link");
		link = createHelper.createHyperlink(HyperlinkType.FILE);
		link.setAddress("cellstyle.xlsx");
		cell.setHyperlink(link);
		cell.setCellStyle(hlinkstyle);

		FileOutputStream out = new FileOutputStream(file.getName());
		wb.write(out);
		out.close();
		wb.close();
	}
}
