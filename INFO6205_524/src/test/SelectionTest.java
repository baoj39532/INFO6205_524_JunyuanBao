package test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import functions.GA;
public class SelectionTest {
@Test
	public void testSelection() throws IOException {
	GA ga = new GA(5000, 16, 2, 50000, 0.9, 0.1, System.getProperty("user.dir") + "\\data.txt");
	ga.init();
	  ga.selection();

	  int flag=0;
	  for(int i=0;i<ga.currSize()-1;i++) {
		  if(ga.getChromosomes()[i].getFitness()<ga.getChromosomes()[i+1].getFitness()) {
			  flag=1;
			  break;
		  }
	  }
	  assertEquals(flag,0);
	}
}
