package bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import utilities.interfaces.Instance;


@ManagedBean (name = "excel")
@SessionScoped
public class ExcelBean implements Serializable, Instance
{

    /* ---------- ATTIBUTES ---------- */
    
    private static final long serialVersionUID = 1L;
    
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;
    
    // Attribut pour l'import
    private UploadedFile file;
    
    private StreamedContent upload;
    
    /* ---------- CONSTUCTORS ---------- */
    
    public ExcelBean()
    {
        instanciation();
    }
    
    /* ---------- METHODS ---------- */

    public void charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
    {
        file = event.getFile();
        Workbook wb = new HSSFWorkbook();
        File test = new File("G:/Workspace/workbook.xls");
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet1 = wb.createSheet("new sheet");
        Row row = sheet1.createRow((short)0);
        Cell cell = row.createCell(0);
        row.createCell(3).setCellValue(true);
        FileOutputStream fileOut = new FileOutputStream(test);
        wb.write(fileOut);
        wb.close();
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/ressources/workbook.xls");
        upload = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "downloaded_workbook.xls");
    }

    @Override
    public void instanciation()
    {
        // TODO Auto-generated method stub
        
    }

    /* ---------- ACCESS ---------- */
    
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
    
    /**
     * @return the upload
     */
    public StreamedContent getUpload()
    {
        return upload;
    }
    
    /**
     * @return the listBean
     */
    public ListBean getListBean()
    {
        return listBean;
    }

    /**
     * @param listBean the listBean to set
     */
    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }
}