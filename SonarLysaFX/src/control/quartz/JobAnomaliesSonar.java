package control.quartz;

import java.io.IOException;

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
			handler.creerVuesQGErreur();
		} catch (InvalidFormatException | IOException e)
		{
			throw new JobExecutionException(e);
		}
		
	}

}
