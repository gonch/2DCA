import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CA2D {
	public ArrayList<List<Integer>> organism;
	public int [][][][][] transitionMatrix;
	int organismSize;
	double fitness;
	
	public CA2D(int organismSize)
	{
		organism = new ArrayList<List<Integer>>();
        organism.add(Arrays.asList(0, 0, 0, 0));
        organism.add(Arrays.asList(0, 0, 0, 0));
        organism.add(Arrays.asList(0, 0, 0, 0));
        organism.add(Arrays.asList(0, 0, 0, 0));
		organism.get(organismSize/2).set(organismSize/2,1);
		this.organismSize = organismSize;
		transitionMatrix = new int [3][3][3][3][3];
	}
	
	public double getFitness()
	{
		return fitness;
	}
	
	public void randomizeTransitionMatrix()
	{
		for(int i = 0;i < 3; i++)
		{
			for(int j = 0;j < 3; j++)
			{
				for(int k = 0;k < 3; k++)
				{
					for(int l = 0;l < 3; l++)
					{
						for(int m = 0;m < 3; m++)
						{
							transitionMatrix[i][j][k][l][m] = (int)(Math.random() * 3);
						}
					}
				}
			}
		}
	}
	
	public void printOrganism()
	{
		for (int i = 0; i < organismSize; i++)
		{
			for (int j = 0; j < organismSize;j++)
			{
				 System.out.print(organism.get(i).get(j) +"  ");  
			}
			System.out.println("");
		}
	}
	
	public CA2D mutateOrganism()
	{
		CA2D mutatedCA2D = new CA2D(4);
		int random = 0;
		int oldvalue = 0;
		for(int i = 0;i < 3; i++)
		{
			for(int j = 0;j < 3; j++)
			{
				for(int k = 0;k < 3; k++)
				{
					for(int l = 0;l < 3; l++)
					{
						for(int m = 0;m < 3; m++)
						{
							random = (int)(Math.random() * 50);
							if(random == 0)
							{
								
								oldvalue = transitionMatrix[i][j][k][l][m];
								if(oldvalue == 0)
								{
									double randomDouble = (Math.random());
									if(randomDouble < 0.5) mutatedCA2D.transitionMatrix[i][j][k][l][m] = 1;
									else mutatedCA2D.transitionMatrix[i][j][k][l][m] = 2;
								}
								else if (oldvalue == 1)
								{
									double randomDouble = (Math.random());
									if(randomDouble < 0.5) mutatedCA2D.transitionMatrix[i][j][k][l][m] = 0;
									else mutatedCA2D.transitionMatrix[i][j][k][l][m] = 2;
								}
								else
								{
									double randomDouble = (Math.random());
									if(randomDouble < 0.5) mutatedCA2D.transitionMatrix[i][j][k][l][m] = 0;
									else mutatedCA2D.transitionMatrix[i][j][k][l][m] = 1;
								}
							}
							else mutatedCA2D.transitionMatrix[i][j][k][l][m] = transitionMatrix[i][j][k][l][m];
						}
					}
				}
			}
		}
		return mutatedCA2D;
	}
	
	public void writeTransition (int top, int bottom, int left, int right, int centre, int value)
	{
		transitionMatrix[top][bottom][left][right][centre] = value;
	}
	
	public void step()
	{
		ArrayList<List<Integer>> auxiliarOrganism = new ArrayList<List<Integer>>(this.organismSize);
        auxiliarOrganism.add(Arrays.asList(organism.get(0).get(0), organism.get(0).get(1), organism.get(0).get(2), organism.get(0).get(3)));
        auxiliarOrganism.add(Arrays.asList(organism.get(1).get(0), organism.get(1).get(1), organism.get(1).get(2), organism.get(1).get(3)));
        auxiliarOrganism.add(Arrays.asList(organism.get(2).get(0), organism.get(2).get(1), organism.get(2).get(2), organism.get(2).get(3)));
        auxiliarOrganism.add(Arrays.asList(organism.get(3).get(0), organism.get(3).get(1), organism.get(3).get(2), organism.get(3).get(3)));
		for (int i = 0; i < organismSize; i++)
		{
			for (int j = 0; j < organismSize;j++)
			{
				int valueTop, valueBottom, valueLeft, valueRight, valueCentre;
				
				if (i == 0) valueTop = organism.get(organismSize-1).get(j);
				else valueTop = organism.get(i-1).get(j);
				
				if (i == organismSize-1) valueBottom = organism.get(0).get(j);
				else valueBottom = organism.get(i+1).get(j);
				
				if (j == 0) valueLeft = organism.get(i).get(organismSize-1);
				else valueLeft = organism.get(i).get(j-1);
				
				if (j == organismSize-1) valueRight = organism.get(i).get(0);
				else valueRight = organism.get(i).get(j+1);
				
				valueCentre = organism.get(i).get(j);
				auxiliarOrganism.get(i).set(j,transitionMatrix[valueTop][valueBottom][valueLeft][valueRight][valueCentre]);
			}
		}
		for (int i = 0; i < organismSize; i++)
		{
			for (int j = 0; j < organismSize;j++)
			{
				organism.get(i).set(j,auxiliarOrganism.get(i).get(j));
			}
		}
	}
	
	public void writeRule(int[] rule)
	{
		
	}
	
	public double fitness1()//compare to total attractor length of the population also majority
	{
		double Majority = this.calculateMajority();
		int result[] = this.calculateAttractor();
		double attractor = result[1];
		if (attractor > 100000) attractor = 100000;
		attractor = attractor/100000;	
		return (Majority+attractor)/2;	
	}
	
	public int fitness2()
	{
		int result[] = this.calculateAttractor();
		return result[1];
	}
	
	public int[] calculateAttractor()
	{
		int[] Length = {0,0};
		boolean repeatedState = false;
		HashMap <ArrayList<List<Integer>>,Integer> hash = new HashMap<ArrayList<List<Integer>>,Integer>();
		while(!repeatedState)
		{
			ArrayList<List<Integer>> auxiliarOrganism = new ArrayList<List<Integer>>(this.organismSize);
	        auxiliarOrganism.add(Arrays.asList(organism.get(0).get(0), organism.get(0).get(1), organism.get(0).get(2), organism.get(0).get(3)));
	        auxiliarOrganism.add(Arrays.asList(organism.get(1).get(0), organism.get(1).get(1), organism.get(1).get(2), organism.get(1).get(3)));
	        auxiliarOrganism.add(Arrays.asList(organism.get(2).get(0), organism.get(2).get(1), organism.get(2).get(2), organism.get(2).get(3)));
	        auxiliarOrganism.add(Arrays.asList(organism.get(3).get(0), organism.get(3).get(1), organism.get(3).get(2), organism.get(3).get(3)));
			if(!hash.containsKey(auxiliarOrganism))
			{
//				System.out.println(auxiliarOrganism.toString());
				hash.put(auxiliarOrganism, Length[0]);
				Length[0]++;
				step();	
			}
			else 
			{
//				System.out.println("REPEATED ORGANISM");
//				System.out.println(auxiliarOrganism.toString());
				Length[1] = Length[0] - hash.get(auxiliarOrganism);
				repeatedState = true;
			}
		}		
		return Length;
	}
	
	public double calculateMajority()
	{
		double Majority = 0;
		int numberOfZeros = 0;
		int numberOfOnes = 0;
		int numberOfTwos = 0;
		int max = 0;
		for(int i = 0;i < 3; i++)
		{
			for(int j = 0;j < 3; j++)
			{
				for(int k = 0;k < 3; k++)
				{
					for(int l = 0;l < 3; l++)
					{
						for(int m = 0;m < 3; m++)
						{
							if(i == 0)numberOfZeros++;
							else if(i == 1) numberOfOnes++;
							else numberOfTwos++;
							if(j == 0)numberOfZeros++;
							else if(j == 1) numberOfOnes++;
							else numberOfTwos++;
							if(k == 0)numberOfZeros++;
							else if(k == 1) numberOfOnes++;
							else numberOfTwos++;
							if(l == 0)numberOfZeros++;
							else if(l == 1) numberOfOnes++;
							else numberOfTwos++;
							if(m == 0)numberOfZeros++;
							else if(m == 1) numberOfOnes++;
							else numberOfTwos++;
							if(numberOfZeros > numberOfOnes) max = numberOfZeros;
							else max = numberOfOnes;
							if(max < numberOfTwos) max = numberOfTwos;
							if(max == numberOfZeros && transitionMatrix[i][j][k][l][m] == 0 ) Majority++;
							else if (max == numberOfOnes && transitionMatrix[i][j][k][l][m] == 1 ) Majority++;
							else if (max == numberOfTwos && transitionMatrix[i][j][k][l][m] == 2 ) Majority ++;
							max = 0;
							numberOfOnes = 0;
							numberOfTwos= 0;
							numberOfZeros = 0;
						}
					}
				}
			}
		}
		return Majority/243;
	}
	
	public static void main (String[] args) throws IOException
	{
		long startTime = System.nanoTime();
		int size = 4;
		int numberOfGenerations = 1000;
		CA2D generation[] = new CA2D[10];
		FileWriter fstream = new FileWriter("GA_10runs_avg_std.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("Generation Best");
		out.write("\n");
		//for(int k = 0;k<10;k++)
		{
		for(int i = 0; i <10; i++) //creates random population of intial organism and fills the first map
		{
//			System.out.println("****************************************");
//			System.out.println("CA #" +i);
			generation[i] = new CA2D(size);
			//generation[i].randomizeTransitionMatrix();
			int result[] = generation[i].calculateAttractor();
			int attractor = result[1];
			generation[i].fitness = attractor;
			//fitness[i] = -1*attractor;
			//fitness[i] = -1*generation[i].fitness1();
//			System.out.println("FITNESS = "+ -1*fitness[i]);
//			System.out.println("FITNESS = "+ generation[i].fitness);
//			System.out.println("****************************************");
//			totalFitness += -1*fitness[i];
//			map.put(-1*fitness[i], i);
		}

		for(int i = 0;i < numberOfGenerations;i++)
		{
//			System.out.println("****************************************");
//			if(generation[i])
//			if(i%100==0) System.out.println("GENERATION "+i);
			generation = evoStep(generation);
			CA2DComparator comparator = new CA2DComparator();
			Arrays.sort(generation, comparator);
			//double totalFitness = 0;
			//for (int j = 0;j < generation.length;j++)
			{
				//totalFitness += generation[j].fitness;
			}
			try
			{
				  int max = (int)generation[0].fitness;
				//  int min = (int)generation[9].fitness;
				  out.write(i+" ");
				  out.write(max+" ");
				 // out.write(totalFitness/10+" ");
				 // out.write(min+" ");
				  out.write("\n");
				  //Close the output stream
				  //out.close();
			}
			catch (Exception e)
			{//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
			}
//			System.out.println("****************************************");
		}
			}
		  out.close();
		CA2DComparator comparator = new CA2DComparator();
		Arrays.sort(generation, comparator);
		for (int i = 0;i< generation.length;i++)
		{
			System.out.println("FINAL "+ i + " = " + generation[i].fitness);
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("FIN");
		System.out.println("TIME = "+duration+ " nanoseconds");
//		int generations = 20;
		
		/*
		 * public void writeTtransition (int top, int bottom, int left, int right, int centre, int value)
		 */
		
		/*
		 * GAME OF LIFE
		*/
		
		/*
		CA.organism[CA.organism.length/2][CA.organism.length/2] = 1;
		CA.organism[CA.organism.length/2-1][CA.organism.length/2-1] = 1;
		CA.organism[CA.organism.length/2-1][CA.organism.length/2+1] = 1;
		CA.writeTransition(0,0,1,1,1,1);
		CA.writeTransition(0,1,0,1,1,1);
		CA.writeTransition(1,0,0,1,1,1);
		CA.writeTransition(1,0,1,0,1,1);
		CA.writeTransition(0,1,1,0,1,1);
		CA.writeTransition(1,1,0,0,1,1);
	
		CA.writeTransition(1,1,1,0,1,1);
		CA.writeTransition(1,1,0,1,1,1);
		CA.writeTransition(1,0,1,1,1,1);
		CA.writeTransition(0,1,1,1,1,1);
		
		CA.writeTransition(1,1,1,0,0,1);
		CA.writeTransition(1,1,0,1,0,1);
		CA.writeTransition(1,0,1,1,0,1);
		CA.writeTransition(0,1,1,1,0,1);
		*/
		
		
//		CA.writeTransition(0,0,1,0,0,1); // move one cell to the right
		
		//CA.writeTransition(0,0,0,1,0,1); //move one cell to the left
		
//		CA.writeTransition(0,1,0,0,0,1); //move one cell up
		 
		//CA.writeTransition(1, 0, 0, 0, 0, 1); //move one cell down
		
//		CA.writeTransition(0,0,1,0,0,2);//move right alternating 1s and 2s
//		CA.writeTransition(0,0,2,0,0,1);
		
	}

	private static CA2D[] evoStep(CA2D[] generation) {		
		CA2DComparator comparator = new CA2DComparator();
		
		Arrays.sort(generation, comparator);
		double normalizedFitness[] = new double[10];
		double leap = 0.5; // (1/number of selected organisms)
		double totalFitness = 0;
		for (int i = 0;i < generation.length;i++)
		{
			totalFitness += generation[i].fitness;
		}
		for (int i = 0;i < generation.length;i++)
		{
			if(i!=0) normalizedFitness[i]= normalizedFitness[i-1] + (generation[i].fitness/totalFitness);
			else normalizedFitness[i] = generation[i].fitness/totalFitness;
//			System.out.println("NORMALIZED FITNESS " + i + " = " + normalizedFitness[i]);
		}
		Random r = new Random();
		double randomStart = 0 + (leap - 0) * r.nextDouble();
		int chosen = 0;
		int chosen1 = -1;
		int chosen2 = -1;
//		System.out.println("randomStart " + randomStart);
//		System.out.println("leap = " + leap);
		for(int i = 0; i <2; i++) //select the TWO strongest with Stochastic universal sampling
		{
//			System.out.println("ITERATION " + i);
			double pointer = randomStart+(i*leap);
//			System.out.println("POINTER = " + pointer);
			while(pointer > normalizedFitness[chosen])
			{
//				System.out.println("<------ Ignore organism  " + chosen);
//				System.out.println("KEY TO DELETE " + fitness[chosen]);
//				map.remove(fitness[chosen]);
//				System.out.println("ELEMENT DELETED, NEW SIZE = " + map.size());
				chosen++;
				if(chosen>=10)break;
			}
			if(chosen>=10)break;
//			System.out.println("----->Take organism " + chosen);
			if(chosen1==-1) chosen1 = chosen;
			else chosen2 = chosen;
//			TNG[i] = generation[map.get(fitness[chosen])];//Mirar esta linea... chosen o i? ojo!;
			chosen++;
		}
		if(chosen1 == -1 || chosen2 ==-1) throw new IllegalArgumentException("chosen 1 or chosen 2 not valid");
//		System.out.println("CHOSEN 1 = "+chosen1+" - CHOSEN 2 = "+chosen2);
//		System.out.println("MAP SIZE = "+map.size());
		int worst = 9;
		while (worst == chosen2 || worst == chosen1)
		{
			worst--;
			if(worst==0) throw new IllegalArgumentException("worst out of range");
		}
//		System.out.println("WORST =" + worst);
		for(int i = 0; i < 2; i++) { // delete the two worst and replace for two mutations of chosen1
			generation[worst] = generation[chosen1].mutateOrganism();
			generation[worst].fitness = generation[worst].fitness2(); //attractor as fitness
//			System.out.println(worst+" organism deleted by mutation of chosen 1, new organism fitness = "+generation[worst].fitness);
			worst--;
			while (worst == chosen2 || worst == chosen1)
			{
				worst--;
				if(worst==0) throw new IllegalArgumentException("worst out of range");
			}
		}
		for(int i = 0; i < 2; i++) { // delete the two worst and replace for two mutations of chosen2
			generation[worst] = generation[chosen2].mutateOrganism();
			generation[worst].fitness = generation[worst].fitness2(); //attractor as fitness
//			System.out.println(worst+" organism deleted by mutation of chosen 2, new organism fitness = "+generation[worst].fitness);
			worst--;
			while (worst == chosen2 || worst == chosen1)
			{
				worst--;
				if(worst==0) throw new IllegalArgumentException("worst out of range");
			}
		}
		generation[worst]=offspring(generation[chosen1],generation[chosen2]);
		generation[worst].fitness = generation[worst].fitness2(); //attractor as fitness

//		System.out.println(worst+" organism deleted by crossover, new organism fitness = "+generation[worst].fitness);
//		System.out.println(worst+" organism deleted by offspring");
//		System.out.println("NEXT GENERATION FITNESS");
//		for(int i = 0;i< generation.length;i++)
		{
//			System.out.println(generation[i].fitness);
		}
		return generation;
	}

	private static CA2D offspring(CA2D father, CA2D mother) {//split into two parts
		CA2D child = new CA2D(4);
		double randomDouble = (Math.random());
		boolean fatherFirst  = false;
		if(randomDouble < 0.5) fatherFirst = true;
		int counter = 0;
		for(int i = 0;i < 3; i++)
		{
			for(int j = 0;j < 3; j++)
			{
				for(int k = 0;k < 3; k++)
				{
					for(int l = 0;l < 3; l++)
					{
						for(int m = 0;m < 3; m++)
						{
							counter++;
							if(fatherFirst)
							{
								if(counter<121) child.transitionMatrix[i][j][k][l][m] = father.transitionMatrix[i][j][k][l][m];
								else child.transitionMatrix[i][j][k][l][m] = mother.transitionMatrix[i][j][k][l][m];
							}
							else
							{
								if(counter<121)	child.transitionMatrix[i][j][k][l][m] = mother.transitionMatrix[i][j][k][l][m];
								else child.transitionMatrix[i][j][k][l][m] = father.transitionMatrix[i][j][k][l][m];
							}
						}
					}
				}
			}
		}
		return child;
	}
	
	public static class CA2DComparator implements Comparator<CA2D>{
		 
	    @Override
	    public int compare(CA2D CA2D1, CA2D CA2D2) {
	 
	        double fitness1 = CA2D1.fitness;
	        double fitness2 = CA2D2.fitness;
	 
	        if (fitness1 < fitness2)return +1;
	        else if (fitness1 > fitness2)return -1;
	        else return 0;
	    }
	}
	// mutation=1/N chance [DONE] - changed to 2%
	// offspring done by taking half from father and half from mother [DONE]
	// select 2 organisms, mutate them twice and replace the 4 worse, crossover and generate another one replacing the worst left;
	// for each generation, best distance vs generation , best parameter vs generation (average and deviation)
	// allways keep the best organism
	// --- 10 tests with GA to plot generations vs fitness (average and deviation). each generation, save best, worst and average 
	// add target and see how many generations it takes to get there (500,1000,15000,20000,100000(impossible)) 50k generation cap
	// discard with majority -> thershold of difference: 
	// calcaulate majority comparing best to mutated mutated and crossover (+-0.02) if it does not get in the interval, mutate again
	// mutate crossover too!;
	// fitness 2 = normalize attractor with sum of all, and same for majority then 50% each
	// in target graph, fitness is target-best
}