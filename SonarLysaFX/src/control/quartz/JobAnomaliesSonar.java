package control.quartz;

import java.io.IOException;
import java.time.LocalDate;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import control.ControlSonar;

public class JobAnomaliesSonar implements Job
{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		try
		{
			ControlSonar handler = new ControlSonar("ETP8137", "28H02m89,;:!");
			System.out.println("Lancement job" + LocalDate.now().toString());
			handler.creerVuesQGErreur();
		} catch (InvalidFormatException | JAXBException | IOException e)
		{
			throw new JobExecutionException(e);
		}
		
	}

}
