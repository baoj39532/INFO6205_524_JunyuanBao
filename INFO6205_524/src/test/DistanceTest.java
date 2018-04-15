package test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import functions.GA;

public class DistanceTest {
	@Test
	public void testDistance() throws IOException {
		GA ga = new GA(1000, 6, 2000,50000, 0.99, 0.01, System.getProperty("user.dir")+"\\data.txt");
		ga.init();
		assertEquals(ga.getDistance()[0][1],6189);
		assertEquals(ga.getDistance()[0][2],3578);
		assertEquals(ga.getDistance()[2][5],4160);
	}

}
