package junit.control;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import control.ControlHandler;

class ControlTest
{


	@Test
	void test()
	{
		final ControlHandler handler = new ControlHandler("ETP8137", "28H02m89,;:!");
		handler.recupererLotsSonarQube();
	}
	
	@Test
	void creerVueMensuelle() throws InvalidFormatException, IOException
	{
		final ControlHandler handler = new ControlHandler("ETP8137", "28H02m89,;:!");
		handler.creerVueMensuelle(new File("d:\\Classeur1.xlsx"));
	}
}
