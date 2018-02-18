package junit.control;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import control.ControlClarity;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlClarityTest
{
    private ControlClarity handler;

    @BeforeAll
    public void init() throws InvalidFormatException, IOException
    {
        // handler = new ControlSonar();
        handler = new ControlClarity(new File("d:\\Referentiel_Projets.xlsm"));
    }
    
    @Test
    public void tesTrecupInfosClarityExcel()
    {
        handler.recupInfosClarityExcel();
    }
}
