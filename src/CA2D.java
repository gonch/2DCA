import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CA2D {
	public ArrayList<List<Integer>> organism;
	public int [][][][][] transitionMatrix;
	int organismSize;
	
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
	
	public double fitness1()
	{
		double Majority = this.calculateMajority();
		int result[] = this.calculateAttractor();
		double attractor = result[1];
		if (attractor > 100000) attractor = 100000;
		attractor = attractor/100000;	
		return (Majority+attractor)/2;	
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
	
	public static void main (String[] args)
	{
		long startTime = System.nanoTime();
		int size = 4;
		int numberOfGenerations = 100;
		CA2D generation[] = new CA2D[10];
		double totalFitness = 0;
		double fitness[] = new double[10];
		HashMap<Double,Integer> map = new HashMap<Double,Integer>();
		for(int i = 0; i <10; i++) //creates random population of intial organism and fills the first map
		{
			System.out.println("****************************************");
			System.out.println("CA #" +i);
			generation[i] = new CA2D(size);
			generation[i].randomizeTransitionMatrix();
			fitness[i] = -1*generation[i].fitness1();
			System.out.println("FITNESS = "+ -1*fitness[i]);
			System.out.println("****************************************");
			totalFitness += -1*fitness[i];
			map.put(-1*fitness[i], i);
		}
		CA2D generation2[] = new CA2D[10];
		long time1,time2,time3;
		time1=0;
		time2=0;
		time3=0;
		for(int i = 0;i < numberOfGenerations;i++)
		{
//			System.out.println("****************************************");
			if(i%100==0)System.out.println("GENERATION "+i);
			generation2 = evoStep(generation,map,fitness,totalFitness,time1,time2,time3);
//			System.out.println("****************************************");
		}
		double sortedFitness[]  = new double[10];;
		for (int i = 0;i< generation2.length;i++)
		{
			sortedFitness[i] = -1*generation2[i].fitness1();
		}
		Arrays.sort(sortedFitness);
		for (int i = 0;i< generation2.length;i++)
		{
			System.out.println("FINAL "+ i + " = " + sortedFitness[i]*-1);
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

	private static CA2D[] evoStep(CA2D[] generation,
			HashMap<Double, Integer> map, double[] fitness, double totalFitness,long time1, long time2, long time3) {
		CA2D TNG[]= new CA2D[10];
		Arrays.sort(fitness);
		for(int i = 0;i<fitness.length;i++) 
		{
			fitness[i]*=-1;
		}
		double normalizedFitness[] = new double[10];
		double leap = 0.2;
		for (int i = 0;i < fitness.length;i++)
		{
			if(i!=0) 
			{
				normalizedFitness[i]= normalizedFitness[i-1] + (fitness[i]/totalFitness);
			}
			else normalizedFitness[i] = fitness[i]/totalFitness;
//			System.out.println("NORMALIZED FITNESS " + i + " = " + normalizedFitness[i]);
		}
		Random r = new Random();
		double randomStart = 0 + (leap - 0) * r.nextDouble();
		int chosen = 0;
//		System.out.println("HASHMAP AT START" + map.toString());
//		System.out.println("HASHMAP SIZE AT START" + map.size());
//		System.out.println("randomStart " + randomStart);
//		System.out.println("leap = " + leap);
		long start = System.nanoTime();
		for(int i = 0; i <5; i++) //select the five strongest with Stochastic universal sampling
		{
//			double auxiliarFitness;
//			System.out.println("ITERATION " + i);
			double pointer = randomStart+(i*leap);
//			System.out.println("POINTER = " + pointer);
			while(pointer > normalizedFitness[chosen])
			{
//				System.out.println("<------ Ignore organism  " + chosen);
//				System.out.println("KEY TO DELETE " + fitness[chosen]);
				map.remove(fitness[chosen]);
//				System.out.println("ELEMENT DELETED, NEW SIZE = " + map.size());
				chosen++;
				if(chosen>=10)break;
			}
			if(chosen>=10)break;
//			System.out.println("----->Take organism " + chosen);
//			System.out.println("ORIGINAL organism = " +  map.get(fitness[chosen]));
			TNG[i] = generation[map.get(fitness[chosen])];//Mirar esta linea... chosen o i? ojo!
//			map.remove(fitness[chosen]);
			map.put(fitness[chosen], i);
//			auxiliarFitness = Math.abs(fitness[chosen]);
//			System.out.println("NEW HASHMAP" + map.toString());
//			System.out.println("ELEMENT REASSIGNED - SIZE OF THE MAP = " + map.size());
			chosen++;
		}
		while(chosen < 10) {
			map.remove(fitness[chosen]);
			chosen++;
		}
//		System.out.println("MAP SIZE = "+map.size());
		long finish = System.nanoTime();
		time1 += (finish-start);
		start = System.nanoTime();
		for(int i = 5; i < TNG.length;i++) //fill the other five with combinations of the first five
		{
			int random1 = (int)(Math.random() * 5);
			int random2 = (int)(Math.random() * 5);
			while(random1==random2) random2 = (int)(Math.random() * 5);		
			TNG[i] = offspring(TNG[random1],TNG[random2]);
		}
		finish = System.nanoTime();
		time2 += (finish-start);
		start = System.nanoTime();
		map.clear();
		for(int i = 0; i <10; i++) //update fitness values and map
		{
			fitness[i] = -1*generation[i].fitness1();
			totalFitness += -1*fitness[i];
			map.put(-1*fitness[i], i);
		}
		finish = System.nanoTime();
		time3 += (finish-start);
//		System.out.println("TIME 1 = "+time1);
//		System.out.println("TIME 2 = "+time2);
//		System.out.println("TIME 3 = "+time3);
		return TNG;
	}

	private static CA2D offspring(CA2D father, CA2D mother) {
		CA2D child = new CA2D(4);
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
							double random = (Math.random());
							if(random < 0.5) child.transitionMatrix[i][j][k][l][m] = father.transitionMatrix[i][j][k][l][m];
							else child.transitionMatrix[i][j][k][l][m] = mother.transitionMatrix[i][j][k][l][m];
						}
					}
				}
			}
		}
		return child;
	}
}