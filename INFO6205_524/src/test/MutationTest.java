package test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import functions.GA;
import items.Chromosome;
public class MutationTest {
	@Test
	public void testMutation() throws IOException {
		GA ga = new GA(5000, 16, 2, 50000, 0.9, 0.1, System.getProperty("user.dir") + "\\data.txt");
		ga.init();
		Chromosome[] children = new Chromosome[2];
		children[0]=ga.getChromosomes()[0];
		children[1]=ga.getChromosomes()[1];
		children=ga.mutation(children);

		
		int flag=0;
		  for(int i=0;i<2;i++) {
			  for(int j=0;j<ga.getCityNum();j++) {
				  for(int k=0;k<ga.getCityNum();k++) {
				  if((j!=k)&&(children[i].getTour()[j]==children[i].getTour()[k])){
					  flag=1;
					  break;
				  }
				  if(flag==1)break;
				  }
				  if(flag==1)break;
			  }
			  if(flag==1)break;
			  
		  }
		  assertEquals(flag,0);

	}
}
