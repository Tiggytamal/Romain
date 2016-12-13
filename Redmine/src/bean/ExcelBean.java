package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@ManagedBean (name = "excel")
@SessionScoped
public class ExcelBean implements Serializable
{

    private static final long serialVersionUID = 1L;
    
 // Attribut pour l'import
    private UploadedFile file;

    public void charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
    {
        file = event.getFile();
        InputStream input = file.getInputstream(); // inputstream depuis le fichier
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputstream()));
        Workbook wb = WorkbookFactory.create(file.getInputstream());
        wb.close();
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
