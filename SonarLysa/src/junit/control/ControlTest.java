package junit.control;

import org.junit.jupiter.api.Test;

import control.RestHandler;

class ControlTest
{
	
	
	@Test
	void test()
	{
		RestHandler handler = new RestHandler();
		handler.recupererLots();		
	}

}
