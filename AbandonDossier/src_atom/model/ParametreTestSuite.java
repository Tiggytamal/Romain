package model;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import model.xml.BanqueXML;
import model.xml.Parametre;

public class ParametreTestSuite extends TestCase
{
    TestSuite suite = new TestSuite();
    Parametre param;
    ArrayList<BanqueXML> liste;
    BanqueXML banque;
    
    public ParametreTestSuite()
    {
        super("Parametre");
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        param = new Parametre("url", "nomDeFichier");
        liste = new ArrayList<>();
        banque = new BanqueXML("nom", "COAD", "coetb", "plaque");
    }
    
    public void allTest()
    {
        suite.addTestSuite(ParametreTestSuite.class);
    }
    
    @Test
    public void testGetListBanqueXMLVide()
    {
        param.setListBanqueXML(null);
        
        assertTrue(param.getListBanqueXML() != null && param.getListBanqueXML().isEmpty());
    }
    
    @Test
    public void testGetListBanqueXMLNonVide()
    {

        param.setListBanqueXML(liste);
        param.getListBanqueXML().add(banque); 
        
        Assert.assertTrue(param.getListBanqueXML() != null );
        Assert.assertTrue(!param.getListBanqueXML().isEmpty());
        Assert.assertTrue(param.getListBanqueXML().get(0).equals(banque));
    }
    
    @Test
    public void testSetListBanqueXML()
    {
        param.setListBanqueXML(liste);
        
        Assert.assertTrue(param.getListBanqueXML() == liste);
    }
    
    @Test
    public void testGetUrl()
    {
        
        Assert.assertTrue(param.getUrl().equals("url"));
    }
    
    @Test
    public void testSetUrl()
    {
        String url = "urlTest";
        param.setUrl(url);
        
        Assert.assertTrue(param.getUrl() == url);
    }
    
    @Test
    public void testGetNomDeFichier()
    {
        
        Assert.assertTrue(param.getNomFichier().equals("nomDeFichier"));
    }
    
    @Test
    public void testSetNomDeFichier()
    {
        String nom = "nomDeFichierTest";
        param.setNomFichier(nom);
        
        Assert.assertTrue(param.getNomFichier() == nom);
    }
}
