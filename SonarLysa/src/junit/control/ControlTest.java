package junit.control;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import control.ControlAPI;

class ControlTest
{

    private static final Logger logger = LogManager.getLogger("commons");
    private static final Logger logger2 = LogManager.getLogger("analytics");
    
	@Test
	void test()
	{
	    logger.info("test");
	    logger2.info("test");
	}
	
	@Test
	void creerVueMensuelle() throws InvalidFormatException, IOException
	{
		final ControlAPI handler = new ControlAPI("ETP8137", "28H02m89,;:!");
		handler.creerVueMensuelle(new File("d:\\Classeur1.xlsx"));
	}
}
