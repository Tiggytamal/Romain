package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import model.Incident;
import utilities.Champ;
import utilities.Statics;
import utilities.Status;
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
    /** Récupération de la date du jour */
    private final LocalDate dateDuJour = LocalDate.now();
    
    /** index pour les incidents ENCOURS. valeur 0 */
    private final int ENCOURS = 0;
    /** index pour les incidents RESOLVED. valeur1 */
    private final int RESOLVED = 1;
    /** index pour les incidents CLOSED. valeur 2 */
    private final int CLOSED = 2;
    
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

        workbook(wbIn, wbOut);
        File newFile = new File("/ressources/test.xls");
        //Sauvegarde du premier fichier sur C
        wbIn.write(new FileOutputStream(newFile.getName()));
        wbIn.close();
        upload = new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "test_workbook.xls");
        

    }
    
    private void workbook(Workbook wbIn, Workbook wbOut)
    {
        
        /* ------ Intialisation des variables ----- */
        
        // Index des cellules
        int moisEnCours = 0, indexEntrants = 0, indexClos = 0, indexTransferes = 0, indexenCours = 0;
        // Valorisation des dates
        LocalDate _1900 = LocalDate.of(1900, 1, 1);
        LocalDate _1901 = LocalDate.of(2015, 1, 1);
        long nbreJours = _1901.toEpochDay() - _1900.toEpochDay();
        // Création des feuilles de classeur
        Sheet sheetAvancement = wbIn.getSheet(Statics.sheetAvancement);
        Sheet sheetSM9 = wbIn.getSheet(Statics.sheetStockSM9);
        
        // Calcul du nombre d'incidents
        int nbreDuMois = calculNbreIncidents()[ENCOURS];
        int nbreResolved = calculNbreIncidents()[RESOLVED];
        int closDuMois = calculNbreIncidents()[CLOSED];
        
        // Printing
        System.out.println(nbreDuMois + " - " + nbreResolved + " - " + closDuMois);
        System.out.println(nbreJours);
        
        Row :
        for (Row row : sheetAvancement)
        {
            for (Cell cell : row)
            {
                if (CellType.NUMERIC.equals(cell.getCellTypeEnum()))
                {
                    if (cell.getNumericCellValue() > 40000)
                    {
                        LocalDate date = LocalDate.ofEpochDay((long) cell.getNumericCellValue()).minusYears(70);
                        if (Statics.TODAY.getMonth().equals(date.getMonth()) && Statics.TODAY.getYear() == date.getYear())
                        {
                            moisEnCours = cell.getRow().getRowNum();
                            break Row;
                        }                           
                    }
                }                  
            }
        }
        sheetAvancement.getActiveCell();

    }
    
    /**
     * Méthode de calcul du nombre d'incidents arrivés le mois en cours
     * @return
     *          le nombre d'incidents
     */
    private int[] calculNbreIncidents()
    {
        // formatteur de date
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // variables locales
        LocalDate dateIncident = null;
        LocalDate dateCloture = null;
        String dateString = null;
        String da = null;
        int totalEnCours = 0;
        int totalResolved = 0;
        int totalClosed = 0;
        int[] retour = new int[3];
        
        // Itération sur les incidents
        for (Incident incident : listIncidents)
        {
            dateString = incident.getMapValeurs().get(Champ.DATEPRISENCHARGE.toString());
            da = incident.getMapValeurs().get(Champ.DA.toString());
            // Elimination des incidents qui ont une date mal formatée ou un n° de DA à abandon.
            if (dateString != null && dateString.length() > 9 && !Statics.ABANDON.equalsIgnoreCase(da))
            {
                dateIncident = LocalDate.parse(dateString.substring(0, 10), f);
                // On ne garde que les incidents arrivés le mois en cours
                if (dateIncident != null && dateIncident.getYear() == dateDuJour.getYear() && dateIncident.getMonth().equals(dateDuJour.getMonth()))
                    totalEnCours++;
                
                // Incrémentation des incidents resolved
                if (Status.RESOLVED.equals(incident.getStatut().getNom()))
                    totalResolved++;
                
                //incrémentation des incidents clos
                if(incident.getDateCloture() != null)
                {
                    dateCloture = ((java.sql.Date) incident.getDateCloture()).toLocalDate();
                    if (Status.CLOSED.equals(incident.getStatut().getNom()) && dateCloture.getYear() == dateDuJour.getYear() && dateCloture.getMonth().equals(dateDuJour.getMonth()))
                        totalClosed++; 
                }
            }           
        }
        // 
        retour[ENCOURS] = totalEnCours;
        retour[RESOLVED] = totalResolved;
        retour[CLOSED] = totalClosed;
        return retour;
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