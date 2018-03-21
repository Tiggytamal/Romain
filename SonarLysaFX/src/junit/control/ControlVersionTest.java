package junit.control;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import control.ControlVersion;

public class ControlVersionTest
{
 private ControlVersion handler;
    
    @Before
    public void init() throws InvalidFormatException, IOException
    {
        handler = new ControlVersion(new File("d:\\Codification des Editions.xls"));
    }
    
    @Test
    public void recupVersionDepuisExcel()
    {
        Map<String, String> map =  handler.recupVersionDepuisExcel();
        assertTrue(!map.isEmpty());
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            assertTrue(entry.getKey().matches("^([0-9]{2}\\.){3}[0-9]{2}$"));
            assertTrue(entry.getValue().matches("^CHC20[12][0-9]\\-S[0-5][0-9]$"));            
        }

    }

}
