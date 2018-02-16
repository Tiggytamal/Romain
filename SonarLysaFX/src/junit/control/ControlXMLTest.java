package junit.control;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import control.ControlXML;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlXMLTest
{
	private ControlXML handler;

	@BeforeAll
	public void init()
	{
		handler = new ControlXML();
	}

	@Test
	public void testCalculerListeApplisDepuisExcel() throws InvalidFormatException, IOException, JAXBException
	{
		handler.calculerMapApplisDepuisExcel();
	}
}
