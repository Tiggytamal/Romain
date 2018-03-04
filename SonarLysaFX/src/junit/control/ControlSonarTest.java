package junit.control;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import control.ControlSonar;
import control.ControlXML;
import junit.TestUtils;
import sonarapi.SonarAPI;

@RunWith(PowerMockRunner.class)
public class ControlSonarTest
{
    private ControlSonar handler;
    public static boolean deser;
    private SonarAPI mock;
    
    @Before
    public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
    {
        // handler = new ControlSonar();
        handler = new ControlSonar("ETP8137", "28H02m89,;:!");
        deser = false;
        new ControlXML().recuprerParamXML();
        mock = PowerMockito.mock(SonarAPI.class);
    }

    @Test
    public void recupererLotsSonarQube() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        TestUtils.callPrivate("recupererLotsSonarQube", handler, null);
    }

    @Test
    public void creerVueProduction() throws InvalidFormatException, IOException
    {
        handler.creerVueProduction(new File("d:\\Classeur1.xlsx"));
    }

    @Test
    public void majVues() throws InvalidFormatException, IOException
    {
        handler.majVues();
    }

    @Test
    public void testAppli()
    {
    }
    
    @Test
    public void creationHeader() throws InvalidFormatException, IOException
    {
        String codeUser;
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m89,;:!");
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());

        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
        codeUser.length();
    }

    @Test
    public void recupererComposantsSonar() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        TestUtils.callPrivate("recupererComposantsSonar", handler, null);
    }

    @Test
    public void creerVueParApplication()
    {
        handler.creerVueParApplication();
    }

    @Test
    public void majFichierSuiviExcel() throws InvalidFormatException, IOException, JAXBException
    {
        handler.majFichierSuiviExcel();
    }

    @Test
    public void majFichierSuiviExcelDataStage() throws InvalidFormatException, IOException, JAXBException
    {
        handler.majFichierSuiviExcelDataStage();
    }

    @Test
    public void creerVuesDatastage()
    {
        handler.creerVuesDatastage();
    }
}