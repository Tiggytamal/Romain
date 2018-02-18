package junit.control.quartz;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.quartz.SchedulerException;

import control.quartz.ControlJob;

/**
 * JUnit pour control.quartz.ControlJob
 * @author ETP137 - Grégoire Mathon
 *
 */
@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class ControlJobTest
{
    private ControlJob handler;

    @BeforeAll
    public void init() throws InvalidFormatException, JAXBException, IOException, SchedulerException
    {
        handler = new ControlJob();
    }

    @Test
    public void testCreationJobAnomaliesSonar() throws SchedulerException, InterruptedException
    {
        handler.creationJobAnomaliesSonar();
    }

    @Test
    public void testFermeturePlanificateur() throws SchedulerException, InterruptedException
    {
        handler.fermeturePlanificateur();
    }
}
