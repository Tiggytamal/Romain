package bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@ManagedBean (name = "excel")
@RequestScoped
public class ExcelBean implements Serializable
{

    private static final long serialVersionUID = 1L;
    
 // Attribut pour l'import
    private UploadedFile file;

    public void charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
    {
        file = event.getFile();
        Workbook wb = new HSSFWorkbook();
        File test = new File("G:/Workspace/workbook.xls");
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet1 = wb.createSheet("new sheet");
        Sheet sheet2 = wb.createSheet("second sheet");
        Row row = sheet1.createRow((short)0);
        Cell cell = row.createCell(0);
        cell.setCellValue(1);
     // Or do it on one line.
        row.createCell(1).setCellValue(1.2);
        row.createCell(2).setCellValue(
                createHelper.createRichTextString("This is a string"));
        row.createCell(3).setCellValue(true);
        FileOutputStream fileOut = new FileOutputStream(test);
        wb.write(fileOut);
        wb.close();
        WeakReference<UploadedFile> test2 = new WeakReference<UploadedFile>(file);
    }

    /**
     * @return the file
     */
    public UploadedFile getFile()
    {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file)
    {
        this.file = file;
    }   
}