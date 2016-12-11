package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean (name = "excel")
@RequestScoped
public class ExcelBean implements Serializable
{

    private static final long serialVersionUID = 1L;
    
 // Attribut pour l'import
    private UploadedFile file;

    public void test()
    {
        try
        {
            Workbook wb2 = WorkbookFactory.create(new File("MyExcel.xls"));
            Workbook wb = WorkbookFactory.create(new FileInputStream("MyExcel.xlsx"));
        }
        catch(InvalidFormatException | EncryptedDocumentException | IOException e)
        {
            
        }
    }
    
    public void charger(FileUploadEvent event) throws EncryptedDocumentException, InvalidFormatException, IOException
    {
        file = event.getFile();
        Workbook wb = WorkbookFactory.create(new File(file.getFileName()));
    }
   
}
