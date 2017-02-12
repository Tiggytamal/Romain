package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import jersey.repackaged.com.google.common.collect.Lists;
import model.Incident;
import utilities.Champs;
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
    
    /** Excel envoyé à l'application*/
    private UploadedFile file;    
    /** Nouveau fichier à télécharger */
    private StreamedContent upload;    
    /** workbook correspondant au fichier envoyé*/
    private Workbook wbIn;    
    /** Workbook du nouveau fichier excel */
    private Workbook wbOut;
    /** liste des incidents triés */
    private List<Incident> listIncidents;
    
    /* ---------- CONSTUCTORS ---------- */
    
    public ExcelBean()
    {
        instanciation();
    }
    
    /* ---------- METHODS ---------- */
    
    @PostConstruct
    public void postConstruct()
    {
        listIncidents = listBean.getListIncidents();
    }
    
    @Override
    public void instanciation()
    {
        wbIn = new HSSFWorkbook();
        wbOut = new HSSFWorkbook();
        
    }

    public void charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
    {
        //Récupération du fichier envoyé
        file = event.getFile();
        
        //Création des deux workooks
        wbIn = WorkbookFactory.create(file.getInputstream());
        wbOut = WorkbookFactory.create(file.getInputstream());

        File newFile = new File("/ressources/test.xls");
        //Sauvegarde du premier fichier sur C
        wbIn.write(new FileOutputStream(newFile.getName()));
        wbIn.close();
        upload = new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "test_workbook.xls");
        

    }
    
    public void workbook()
    {
        int nbreDuMois = incidentsDuMois();
        int nbreResolved = incidentsResolved();
        
        
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet1 = wb.createSheet("new sheet");
        Row row = sheet1.createRow((short)0);
        Cell cell = row.createCell(0);
        row.createCell(3).setCellValue(true);

    }
    
    private int incidentsDuMois()
    {
        // Récupération de la date du jour
        LocalDate date = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Copie de la liste d'incidents
        List<Incident> list = new ArrayList<>();
        list.addAll(listIncidents);
        
        for (Iterator<Incident> iter = list.iterator(); iter.hasNext();)
        {
            Incident incident = iter.next();
            String dateString = incident.getMapValeurs().get(Champs.DATEPRISENCHARGE.toString());
            if (dateString == null)
            {
                iter.remove();
                continue;
            }
            LocalDate dateIncident = null;
            try
            {
                dateIncident = LocalDate.parse(dateString.substring(0, 10), f);
            }
            catch (DateTimeParseException e)
            {
                iter.remove();
                continue;
            }
            catch (StringIndexOutOfBoundsException e)
            {
                System.out.println(dateString);
                iter.remove();
                continue;
            }
            if (dateIncident == null || dateIncident.getYear() != date.getYear() || dateIncident.getMonth() != date.getMonth())
                iter.remove();
        }
        return list.size();
    }
    
    private int incidentsResolved()
    {
        //Copie de la liste d'incidents
        List<Incident> list = new ArrayList<>();
        list.addAll(listIncidents);
        
        for (Iterator<Incident> iter = list.iterator(); iter.hasNext();)
        {
            if (!Champs.RESOLVED.equals(iter.next().getStatut().getNom()))
            {
                iter.remove();
            }
        }
        return list.size();
    }

    private int incidentClosDumois()
    {
        //Copie de la liste d'incidents
        List<Incident> list = new ArrayList<>();
        list.addAll(listIncidents);
        
        for (Iterator<Incident> iter = list.iterator(); iter.hasNext();)
        {
            Incident incident = iter.next();
            if (!Champs.CLOSED.equals(incident.getStatut().getNom()))
            {
                iter.remove();
                continue;
            }
            
            
        }
        
        return list.size();
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