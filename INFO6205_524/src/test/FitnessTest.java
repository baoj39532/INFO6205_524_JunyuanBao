package test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import items.Chromosome;;
public class FitnessTest {
	@Test
	public void testFitness() throws IOException {
		 int[] tour=new int[3];
		 int[][] distance=new int[3][3];

		 distance[0][0]=0;
		 distance[0][1]=1;
		 distance[0][2]=2;
		 distance[1][0]=1;
		 distance[1][1]=0;
		 distance[1][2]=1;
		 distance[2][0]=2;
		 distance[2][1]=1;
		 distance[2][2]=0;
		 
		Chromosome c=new Chromosome(3,distance);
		c.getTour()[0]=0;
		c.getTour()[1]=1;
		c.getTour()[2]=2;
		c.calculatefitness();
		double d=0.25;

		assertEquals(c.getFitness(),d,0.00001);

	}

}
