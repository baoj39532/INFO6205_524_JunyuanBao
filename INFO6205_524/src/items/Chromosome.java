package items;

import java.util.Random;
import java.util.Vector;

public class Chromosome implements Cloneable {

	private int cityNum;
	private int[] tour;
	private int[][] distance;
	private double fitness;


	public Chromosome(int num, int[][] distance) {
		this.cityNum = num;
		tour = new int[cityNum];
		this.distance = distance;

	}

	public void randomGeneration() {
		Vector<Integer> cityList = new Vector<Integer>();
		for (int i = 0; i < cityNum; i++) {
			cityList.add(Integer.valueOf(i));
		}

		Random r = new Random(System.nanoTime());
		for (int i = 0; i < cityNum; i++) {

			int index = r.nextInt(cityList.size());
			int selected = cityList.get(index).intValue();
			tour[i] = selected;
			cityList.remove(index);
		}

	}

	public void print() {
		for (int i = 0; i < cityNum; i++) {
			System.out.print(tour[i] + ",");
		}
		System.out.println();
		System.out.println("Its fitness measure is: " + getFitness());
	}

	public double calculatefitness() {

		double fitness = 0.0;
		int len = 0;
		for (int i = 0; i < cityNum - 1; i++) {
			len += distance[this.tour[i]][this.tour[i + 1]];
		}
		
		len += distance[0][tour[cityNum - 1]];
//		System.out.print("calculate the length:" + len+" ");
//		for (int i = 0; i < cityNum; i++) {
//			System.out.print(tour[i] + ",");
//		}
//		System.out.println();
		fitness = 1.0 / len;
		return fitness;
	}

	public int[] getTour() {
		return tour;
	}

	public void setTour(int[] tour) {
		this.tour = tour;
	}

	public int[][] getDistance() {
		return distance;
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}

	public int getCityNum() {
		return cityNum;
	}

	public void setCityNum(int cityNum) {
		this.cityNum = cityNum;
	}

	public double getFitness() {
		this.fitness = calculatefitness();
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Chromosome chromosome = (Chromosome) super.clone();
		chromosome.cityNum = this.cityNum;
		chromosome.distance = this.distance.clone();
		chromosome.tour = this.tour.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}

}
