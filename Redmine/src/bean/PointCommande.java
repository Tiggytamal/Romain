package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;

import model.enums.Statut;
import utilities.CellHelper;
import utilities.Utilities;
import utilities.enums.Side;
import utilities.interfaces.Instance;

@ManagedBean (name = "commande")
@ViewScoped
public class PointCommande implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    /** Workbook du nouveau fichier excel */
    private Workbook wb;
    /** Helper pour la gestion des cellules */
    private CellHelper helper;
    /** Variable privée des indices des colonnes*/
    private final int iApps = 2, iClos = 3, iResolv = 4, iCharge = 5, iBDC = 6, iAvanc = 7, iNoms = 2;
    
    private List<ApplicationBDC> listApplisBDC;
    
    /* ---------- CONSTUCTORS ---------- */
    
    
    /* ---------- METHODS ---------- */

    @Override
    @PostConstruct
    public void instanciation()
    {
        wb = new HSSFWorkbook();
        helper = new CellHelper(wb);
        listApplisBDC = new ArrayList<>();
        for (String application : listBean.getApplicationschoisies())
        {
            listApplisBDC.add(new ApplicationBDC(application));
        }
    }

    /**
     * Permet de fichier le fichier excel avec le calcul de l'avancée du bon de commande.
     * 
     * @return
     */
    public String calcul()
    {
        Sheet sheet = wb.createSheet("Calcul");
        structure(sheet);
        
        miseAJourDesDonnees(sheet);
               
        // Sauvegarde du premier fichier sur C
        File newFile = new File("/ressources/test.xls");
        try
        {
            wb.write(new FileOutputStream(newFile.getName()));
            wb.close();
            listBean.setUpload(new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "BonDeDommande.xls"));
        }
        catch (IOException e)
        {
           Utilities.updateGrowl("Erreur dans la création du fichier Excel " + e.getClass().getSimpleName(), FacesMessage.SEVERITY_ERROR, null);
           return "";
        }

        return "";
    }
    
    private void miseAJourDesDonnees(Sheet sheet)
    {
        Row row;
        Cell cell;
        int i = iNoms;
        for (String appli : listBean.getApplicationschoisies())
        {
            i++;
            row = sheet.createRow(i);
            
            cell = helper.recentrage(row.createCell(iApps));
            cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.GAUCHE));
            cell.setCellValue(appli);
            
            cell = helper.recentrage(row.createCell(iClos));
            
        }        
    }

    /**
     * Structure le tableau avec le nom des colonnes
     * 
     * @param sheet
     */
    private void structure(Sheet sheet)
    {
        Row row = sheet.createRow(iNoms);
        
        Cell cell = helper.recentrage(row.createCell(iApps));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUTGAUCHE));
        cell.setCellValue("Applications");
        
        cell = helper.recentrage(row.createCell(iClos));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUT));
        cell.setCellValue("Clos");
        
        cell = helper.recentrage(row.createCell(iResolv));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUT));
        cell.setCellValue("Résolus");
        
        cell = helper.recentrage(row.createCell(iCharge));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUT));
        cell.setCellValue("Charge/jour");
        
        cell = helper.recentrage(row.createCell(iBDC));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUT));
        cell.setCellValue("BDC");
        
        cell = helper.recentrage(row.createCell(iAvanc));
        cell.setCellStyle(helper.getStyle(Statut.NOUVEAU, Side.HAUTDROITE));
        cell.setCellValue("Avancée");                
    }
    
    /* ---------- ACCESS ---------- */
    
    
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
        
    /**
     * @return the listApplisBDC
     */
    public List<ApplicationBDC> getListApplisBDC()
    {
        return listApplisBDC;
    }
    
    
    
    protected class ApplicationBDC
    {
        private String nom;
        private int bdc;
        
        public ApplicationBDC(String nom)
        {
            this.nom = nom;
            bdc = 0;
        }        

        /**
         * @return the bdc
         */
        public int getBdc()
        {
            return bdc;
        }

        /**
         * @param bdc the bdc to set
         */
        public void setBdc(int bdc)
        {
            this.bdc = bdc;
        }

        /**
         * @return the nom
         */
        public String getNom()
        {
            return nom;
        }
                
    }

}