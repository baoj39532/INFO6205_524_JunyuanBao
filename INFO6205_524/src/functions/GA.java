package functions;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import items.Chromosome;

public class GA {

	private Chromosome[] chromosomes;
	private Chromosome[] nextGeneration;

	private int cityNum;
	private int N;
	private double pco;
	private double pm;
	private int MAX_GEN;
	private int MAX_C;
	private int bestLength;
	private int[] bestTour;
	private double bestFitness;
	private double[] best_Fitness_Record;
	private double[] averageFitness;
	private int[][] distance;
	private String filename;

	/**
	 * Constructor of GA class
	 * 
	 * @param n
	 *            initial individual
	 * @param num
	 *            city number
	 * @param g
	 *            number of generations
	 * @param p_c
	 *            crossover possibility
	 * @param p_m
	 *            mutation possibility
	 * @param filename
	 *            data input
	 */
	public GA(int n, int num, int g, int c, double p_c, double p_m, String filename) {
		
		this.N = n;
		this.cityNum = num;		
		this.MAX_GEN = g;
		this.MAX_C = c;
		this.pco = p_c;
		this.pm = p_m;
		bestTour = new int[cityNum];
		averageFitness = new double[MAX_GEN];
		best_Fitness_Record = new double[MAX_GEN];
		bestFitness = 0.0;
		chromosomes = new Chromosome[MAX_C];
		nextGeneration = new Chromosome[MAX_C];
		distance = new int[cityNum][cityNum];
		this.filename = filename;
	}

	


	public void init() throws IOException {
		// read data file
		int[] x;
		int[] y;
		String strbuff;
		@SuppressWarnings("resource")
		BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

		distance = new int[cityNum][cityNum];
		x = new int[cityNum];
		y = new int[cityNum];
		for (int i = 0; i < cityNum; i++) {
			strbuff = data.readLine();
			String[] strcol = strbuff.split(",");
			x[i] = Double.valueOf(strcol[0]).intValue();
			y[i] = Double.valueOf(strcol[1]).intValue();
		}
		// calculate distance for every city
//		for (int i = 0; i < cityNum - 1; i++) {
//			distance[i][i] = 0; // 对角线为0
//			for (int j = i + 1; j < cityNum; j++) {
//				double rij = Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
//				int tij = (int) Math.round(rij);
//				// if (tij < rij) {
//				distance[i][j] = tij;
//				distance[j][i] = distance[i][j];
//			}
//		}
//		distance[cityNum - 1][cityNum - 1] = 0;
		
		 for (int i = 0; i < cityNum - 1; i++) {  
	            distance[i][i] = 0; // 对角线为0  
	            for (int j = i + 1; j < cityNum; j++) {  
	                double rij = Math  
	                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])  
	                                * (y[i] - y[j])) / 1.0);  
	                // 四舍五入，取整  
	                int tij = (int) Math.round(rij);  
	                if (tij < rij) {  
	                    distance[i][j] = tij + 1;  
	                    distance[j][i] = distance[i][j];  
	                } else {  
	                    distance[i][j] = tij;  
	                    distance[j][i] = distance[i][j];  
	                }  
	            }  
	        }  
	        distance[cityNum - 1][cityNum - 1] = 0;  

		for (int i = 0; i < N; i++) {
			Chromosome chromosome = new Chromosome(cityNum, distance);
			chromosome.randomGeneration();
			chromosomes[i] = chromosome;
			 chromosome.print();
		}
	}

	private void evolve(int g) {
		
		double[] selectionP = new double[currSize()];
		double sum = 0.0;
		double tmp = 0.0;
		nextGeneration = new Chromosome[MAX_C];
		System.out.println("chromosome num: "+currSize());
		for (int i = 0; i < currSize(); i++) {
			sum += chromosomes[i].getFitness();
			if (chromosomes[i].getFitness() > bestFitness) {
				bestFitness = chromosomes[i].getFitness();
				bestLength = (int) (1.0 / bestFitness);
				for (int j = 0; j < cityNum; j++) {
					bestTour[j] = chromosomes[i].getTour()[j];
				}

			}
		}
		System.out.println("BEST: "+bestFitness);
		averageFitness[g] = sum / currSize();
		best_Fitness_Record[g]=bestFitness;

		 System.out.print("The average fitness in "+g+ " generation is"+averageFitness[g]+ ", and the best fitness is "+bestFitness);

	    //sorting individuals by fitness using MaxPQ
		selection();

		
		
		int curr=currSize();

		//select two alive individuals to do crossover and mutation
		
		for (int i = 0; i < curr; i++) {
			tmp += chromosomes[i].getFitness() / sum;
			selectionP[i] = tmp;
		}
		Random random = new Random();
		for (int i = 0; i < curr; i = i + 2) {

			Chromosome[] children = new Chromosome[2];
			// select two chromosome from alive individuals
			// System.out.println("---------start selection-----------");
			// System.out.println();
//			int last=0;
			for (int j = 0; j < 2; j++) {
				int selectedCity = 0;
//				while(true) {
				for (int k = 0; k < curr - 1; k++) {
					double p = random.nextDouble();
					
					if (p > selectionP[k] && p <= selectionP[k + 1]) {
						selectedCity = k;
					}
					if (k == 0 && random.nextDouble() <= selectionP[k]) {
						selectedCity = 0;
					}
					
				}
				
//					selectedCity=random.nextInt(currSize());
//				if(j==0)
//				{last=selectedCity;break;}
//				if(last!=selectedCity)break;
//				}
				try {
					children[j] = (Chromosome) chromosomes[selectedCity].clone();

					// children[j].print();
					// System.out.println();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

// crossover function
			if (random.nextDouble() < pco) 
			children=crossover(children);

// mutation function
			if (random.nextDouble() < pm) 
			children=mutation(children);

			nextGeneration[i] = children[0];
			nextGeneration[i + 1] = children[1];

		}
		//System.out.println("next size: "+nextSize());
		//System.out.println("curr size: "+currSize());
		if(nextSize()+curr>MAX_C) {
			Chromosome temp[]=new Chromosome[4*MAX_C];
			for(int i=0;i<curr;i++)
				temp[i]=chromosomes[i];
			chromosomes=temp;
		}
		for (int k = 0; k < nextSize(); k++) {
			try {
				chromosomes[k + curr] = (Chromosome) nextGeneration[k].clone();
				

			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//current chromosomes
//		for(int i=0;i<currSize();i++) {
//			System.out.println("current chromosomes:");
//			for(int j=0;j<cityNum;j++)
//		    System.out.print(chromosomes[i].getTour()[j]);
//			System.out.println();
//		}
		/*
		 * System.out.println("Next generation is"); for (int k = 0; k < N; k++) {
		 * chromosomes[k].print(); }
		 */
	}
	
	
	public void selection() {
		MaxPQ<Chromosome> pq = new MaxPQ<Chromosome>(new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return 1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return -1;
				} else {
					return 0;
				}
			}

		});
//		System.out.println("insert time"+currSize());
		for (int k = 0; k < currSize(); k++) {
			pq.insert(chromosomes[k]);
		}
		// System.out.println("The best fitness in PQ is" + pq.max().getFitness());
		chromosomes = new Chromosome[MAX_C];
//		System.out.println("del time"+pq.size() *3/ 5);
//		System.out.println("delete pq result");
		int sz=pq.size() /2;
		for (int i = 0; i < sz; i++) {
			chromosomes[i] = pq.delMax();
//			for(int j=0;j<cityNum;j++)
//			    System.out.print(chromosomes[i].getTour()[j]);
//			System.out.println();
			
		}
	}
	
	public Chromosome[] crossover(Chromosome[] children) {
		Random random = new Random();
//		 System.out.println("----------Start crossover----------");
		// System.out.println();
		// Random random = new Random(System.currentTimeMillis());
		
			// System.out.println("crossover");
			// random = new Random(System.currentTimeMillis());
			// define two cut point
			int cutPoint1 = -1;
			int cutPoint2 = -1;
			int r1 = random.nextInt(cityNum);
			if (r1 > 0 && r1 < cityNum - 1) {
				cutPoint1 = r1;
				// random = new Random(System.currentTimeMillis());
				int r2 = random.nextInt(cityNum - r1);
				if (r2 == 0) {
					cutPoint2 = r1 + 1;
				} else if (r2 > 0) {
					cutPoint2 = r1 + r2;
				}

			}
			if (cutPoint1 > 0 && cutPoint2 > 0) {
//				 System.out.println("Cut point1 is "+cutPoint1 +", and cut point2 is"+cutPoint2);
				int[] tour1 = new int[cityNum];
				int[] tour2 = new int[cityNum];
				if (cutPoint2 == cityNum - 1) {
					for (int j = 0; j < cityNum; j++) {
						tour1[j] = children[0].getTour()[j];
						tour2[j] = children[1].getTour()[j];
					}
					//
//					 System.out.println("The two tours if cutPoint2 == cityNum - 1 are "); 
//					 for (int j = 0; j < cityNum; j++) {
//					 System.out.print(tour1[j] +"\t"); } System.out.println(); 
//					 for (int j = 0; j <cityNum; j++) { 
//						 System.out.print(tour2[j] +"\t"); } System.out.println();
					 //
				} else {

					// int n = 1;
					for (int j = 0; j < cityNum; j++) {
						if (j < cutPoint1) {
							tour1[j] = children[0].getTour()[j];
							tour2[j] = children[1].getTour()[j];
						} else if (j >= cutPoint1 && j < cutPoint1 + cityNum - cutPoint2 - 1) {
							tour1[j] = children[0].getTour()[j + cutPoint2 - cutPoint1 + 1];
							tour2[j] = children[1].getTour()[j + cutPoint2 - cutPoint1 + 1];
						} else {
							tour1[j] = children[0].getTour()[j - cityNum + cutPoint2 + 1];
							tour2[j] = children[1].getTour()[j - cityNum + cutPoint2 + 1];
						}

					}
					//
//					 System.out.println("The two tours if cutPoint2 != cityNum - 1 are "); 
//					 for (int j = 0; j < cityNum; j++) {
//					 System.out.print(tour1[j] +"\t"); } System.out.println(); 
//					 for (int j = 0; j <cityNum; j++) { 
//						 System.out.print(tour2[j] +"\t"); } System.out.println();
					 //
				}
				//
//				 System.out.println("The two tours final are "); 
//				 for (int j = 0; j < cityNum; j++) {
//				 System.out.print(tour1[j] +"\t"); } System.out.println(); 
//				 for (int j = 0; j <cityNum; j++) { 
//					 System.out.print(tour2[j] +"\t"); } System.out.println();
				 //

				for (int j = 0; j < cityNum; j++) {
					if (j < cutPoint1 || j > cutPoint2) {

						children[0].getTour()[j] = -1;
						children[1].getTour()[j] = -1;
					} else {
						int tmp1 = children[0].getTour()[j];
						children[0].getTour()[j] = children[1].getTour()[j];
						children[1].getTour()[j] = tmp1;
					}
				}
				//
//				System.out.println("final children are:");
//				 for (int j = 0; j < cityNum; j++) {
//				  System.out.print(children[0].getTour()[j]+"\t"); } 
//				 System.out.println(); 
//				  for(int j = 0; j < cityNum; j++) {
//				  System.out.print(children[1].getTour()[j]+"\t"); } 
//				  System.out.println();
				 //
				if (cutPoint2 == cityNum - 1) {
					int position = 0;
					for (int j = 0; j < cutPoint1; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour1[m] == children[0].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {

								children[0].getTour()[j] = tour1[m];
								position = m + 1;
								break;
							}
						}
					}
					position = 0;
					for (int j = 0; j < cutPoint1; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour2[m] == children[1].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {
								children[1].getTour()[j] = tour2[m];
								position = m + 1;
								break;
							}
						}
					}

				} else {

					int position = 0;
					for (int j = cutPoint2 + 1; j < cityNum; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour1[m] == children[0].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {
								children[0].getTour()[j] = tour1[m];
								position = m + 1;
								break;
							}
						}
					}
					for (int j = 0; j < cutPoint1; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour1[m] == children[0].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {
								children[0].getTour()[j] = tour1[m];
								position = m + 1;
								break;
							}
						}
					}

					position = 0;
					for (int j = cutPoint2 + 1; j < cityNum; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour2[m] == children[1].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {
								children[1].getTour()[j] = tour2[m];
								position = m + 1;
								break;
							}
						}
					}
					for (int j = 0; j < cutPoint1; j++) {
						for (int m = position; m < cityNum; m++) {
							boolean flag = true;
							for (int n = 0; n < cityNum; n++) {
								if (tour2[m] == children[1].getTour()[n]) {
									flag = false;
									break;
								}
							}
							if (flag) {
								children[1].getTour()[j] = tour2[m];
								position = m + 1;
								break;
							}
						}
					}
				}

			}
		
//		 children[0].print();
//		 children[1].print();
		return children;
	}
	
	public Chromosome[] mutation(Chromosome[] children) {
		Random random = new Random();
		// System.out.println("---------Start mutation------");
					// System.out.println();
					// random = new Random(System.currentTimeMillis());
					
						// System.out.println("mutation");
						for (int j = 0; j < 2; j++) {
							// random = new Random(System.currentTimeMillis());
							// 定义两个cut点
							int cutPoint1 = -1;
							int cutPoint2 = -1;
							int r1 = random.nextInt(cityNum);
							if (r1 > 0 && r1 < cityNum - 1) {
								cutPoint1 = r1;
								// random = new Random(System.currentTimeMillis());
								int r2 = random.nextInt(cityNum - r1);
								if (r2 == 0) {
									cutPoint2 = r1 + 1;
								} else if (r2 > 0) {
									cutPoint2 = r1 + r2;
								}

							}

							if (cutPoint1 > 0 && cutPoint2 > 0) {
								List<Integer> tour = new ArrayList<Integer>();
								// System.out.println("Cut point1 is "+cutPoint1+", and cut point2 is
								// "+cutPoint2);
								if (cutPoint2 == cityNum - 1) {
									for (int k = 0; k < cutPoint1; k++) {
										tour.add(Integer.valueOf(children[j].getTour()[k]));
									}
								} else {
									for (int k = 0; k < cityNum; k++) {
										if (k < cutPoint1 || k > cutPoint2) {
											tour.add(Integer.valueOf(children[j].getTour()[k]));
										}
									}
								}
								// random = new Random(System.currentTimeMillis());
								int position = random.nextInt(tour.size());

								if (position == 0) {

									for (int k = cutPoint2; k >= cutPoint1; k--) {
										tour.add(0, Integer.valueOf(children[j].getTour()[k]));
									}

								} else if (position == tour.size() - 1) {

									for (int k = cutPoint1; k <= cutPoint2; k++) {
										tour.add(Integer.valueOf(children[j].getTour()[k]));
									}

								} else {

									for (int k = cutPoint1; k <= cutPoint2; k++) {
										tour.add(position, Integer.valueOf(children[j].getTour()[k]));
									}

								}

								for (int k = 0; k < cityNum; k++) {
									children[j].getTour()[k] = tour.get(k).intValue();

								}
								// System.out.println();
							}

						}
					

					// children[0].print();
					// children[1].print();
		return children;
	}

	public int currSize() {
		int i = 0;
		while (chromosomes[i] != null) {
			i++;
		}
		return i;
	}

	private int nextSize() {
		int i = 0;
		while (nextGeneration[i] != null) {
			i++;
		}
		return i;
	}

	private void printOptimal() {

		System.out.println("The best fitness is " + bestFitness);
		System.out.println("The best tour length is " + bestLength);
		System.out.println("The best tour is ");
		for (int i = 0; i < cityNum; i++) {
			System.out.print(bestTour[i] + ",");
		}
		System.out.println();
	}

	private void outputResults() {
		String filename = System.getProperty("user.dir") + "\\result.txt";
		/*
		 * File file = new File(filename); if (!file.exists()) { try {
		 * file.createNewFile(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } }
		 */
		try {
			@SuppressWarnings("resource")
			FileOutputStream outputStream = new FileOutputStream(filename);
			for (int i = 0; i < averageFitness.length; i++) {
				String line = "The average fitness in "+i+ " generation is"+String.valueOf(averageFitness[i])+ ", and the best fitness is "+best_Fitness_Record[i]+ "\r\n";

				outputStream.write(line.getBytes());

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Chromosome[] getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(Chromosome[] chromosomes) {
		this.chromosomes = chromosomes;
	}

	public int getCityNum() {
		return cityNum;
	}

	public void setCityNum(int cityNum) {
		this.cityNum = cityNum;
	}

	public double getPco() {
		return pco;
	}

	public void setPco(double pco) {
		this.pco = pco;
	}

	public double getPm() {
		return pm;
	}

	public void setPm(double pm) {
		this.pm = pm;
	}

	public int getMAX_GEN() {
		return MAX_GEN;
	}

	public void setMAX_GEN(int mAX_GEN) {
		MAX_GEN = mAX_GEN;
	}

	public int getBestLength() {
		return bestLength;
	}

	public void setBestLength(int bestLength) {
		this.bestLength = bestLength;
	}

	public int[] getBestTour() {
		return bestTour;
	}

	public void setBestTour(int[] bestTour) {
		this.bestTour = bestTour;
	}

	public double[] getBest_Fitness_Record() {
		return best_Fitness_Record;
	}

	public void setBest_Fitness_Record(double[] best_Fitness_Record) {
		this.best_Fitness_Record = best_Fitness_Record;
	}




	public double[] getAverageFitness() {
		return averageFitness;
	}

	public void setAverageFitness(double[] averageFitness) {
		this.averageFitness = averageFitness;
	}

	public int getN() {
		return N;
	}

	public int getMAX_C() {
		return MAX_C;
	}

	public void setMAX_C(int mAX_C) {
		MAX_C = mAX_C;
	}

	public void setN(int n) {
		N = n;
	}

	public int[][] getDistance() {
		return distance;
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}


	
	public void findBestTour() throws IOException {

		init();
		System.out.println("---------------------Start evolution---------------------");
		for (int i = 0; i < MAX_GEN; i++) {
			 System.out.println("-----------Start generation "+ i+"----------");
				evolve(i);
			 System.out.println("-----------End generation "+ i+"----------");
		}
		System.out.println("---------------------End evolution---------------------");
		printOptimal();
		outputResults();

	}
	
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 1; i++) {
			GA ga = new GA(5000, 16, 100, 50000, 0.9, 0.1, System.getProperty("user.dir") + "\\data.txt");
			ga.findBestTour();
		}
	}

}
