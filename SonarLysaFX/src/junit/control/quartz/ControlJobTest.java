package junit.control.quartz;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.quartz.SchedulerException;

import control.quartz.ControlJob;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlJobTest
{
	private ControlJob handler;

	@BeforeAll
	public void init() throws InvalidFormatException, JAXBException, IOException, SchedulerException
	{
		handler = new ControlJob();
	}

	@Test
	public void testPremierTest() throws SchedulerException, InterruptedException
	{
		handler.creationJobAnomaliesSonar();
	}
}
