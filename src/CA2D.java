import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CA2D {
	public ArrayList<List<Integer>> organism;
	public int [][][][][] transitionMatrix;
	int organismSize;
	
	public CA2D(int organismSize)
	{
		organism = new ArrayList<List<Integer>>();
        organism.add(Arrays.asList(0, 0, 0, 0, 0 ));
        organism.add(Arrays.asList(0, 0, 0, 0, 0 ));
        organism.add(Arrays.asList(0, 0, 0, 0, 0 ));
        organism.add(Arrays.asList(0, 0, 0, 0, 0 ));
        organism.add(Arrays.asList(0, 0, 0, 0, 0 ));
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
        auxiliarOrganism.add(Arrays.asList(organism.get(0).get(0), organism.get(0).get(1), organism.get(0).get(2), organism.get(0).get(3), organism.get(0).get(4) ));
        auxiliarOrganism.add(Arrays.asList(organism.get(1).get(0), organism.get(1).get(1), organism.get(1).get(2), organism.get(1).get(3), organism.get(1).get(4) ));
        auxiliarOrganism.add(Arrays.asList(organism.get(2).get(0), organism.get(2).get(1), organism.get(2).get(2), organism.get(2).get(3), organism.get(2).get(4) ));
        auxiliarOrganism.add(Arrays.asList(organism.get(3).get(0), organism.get(3).get(1), organism.get(3).get(2), organism.get(3).get(3), organism.get(3).get(4) ));
        auxiliarOrganism.add(Arrays.asList(organism.get(4).get(0), organism.get(4).get(1), organism.get(4).get(2), organism.get(4).get(3), organism.get(4).get(4) ));
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
	
	public int[] calculateAttractor()
	{
		int[] Length = {0,0};
		boolean repeatedState = false;
		HashMap <ArrayList<List<Integer>>,Integer> hash = new HashMap<ArrayList<List<Integer>>,Integer>();
		while(!repeatedState)
		{
			ArrayList<List<Integer>> auxiliarOrganism = new ArrayList<List<Integer>>(this.organismSize);
	        auxiliarOrganism.add(Arrays.asList(organism.get(0).get(0), organism.get(0).get(1), organism.get(0).get(2), organism.get(0).get(3), organism.get(0).get(4) ));
	        auxiliarOrganism.add(Arrays.asList(organism.get(1).get(0), organism.get(1).get(1), organism.get(1).get(2), organism.get(1).get(3), organism.get(1).get(4) ));
	        auxiliarOrganism.add(Arrays.asList(organism.get(2).get(0), organism.get(2).get(1), organism.get(2).get(2), organism.get(2).get(3), organism.get(2).get(4) ));
	        auxiliarOrganism.add(Arrays.asList(organism.get(3).get(0), organism.get(3).get(1), organism.get(3).get(2), organism.get(3).get(3), organism.get(3).get(4) ));
	        auxiliarOrganism.add(Arrays.asList(organism.get(4).get(0), organism.get(4).get(1), organism.get(4).get(2), organism.get(4).get(3), organism.get(4).get(4) ));
			if(!hash.containsKey(auxiliarOrganism))
			{
				hash.put(auxiliarOrganism, Length[0]);
				Length[0]++;
				step();	
			}
			else 
			{
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
		return Majority;
	}
	
	public static void main (String[] args)
	{
		long startTime = System.nanoTime();
		int size = 5;
		CA2D generation[] = new CA2D[10];
		for(int i = 0; i <10; i++)
		{
			System.out.println("****************************************");
			System.out.println("CA #" +i);
			generation[i] = new CA2D(size);
			generation[i].randomizeTransitionMatrix();
			int result[] = generation[i].calculateAttractor();
			System.out.println("TRAJECTORY LENGTH= "+ result[0]);
			System.out.println("ATTRACTOR LENGTH = "+ result[1]);
			System.out.println("MAJORITY = " + generation[i].calculateMajority()/243);
			System.out.println("****************************************");
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("FIN");
		duration = TimeUnit.SECONDS.convert(duration,TimeUnit.NANOSECONDS);
		System.out.println("TIME = "+duration+ " seconds");
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
		
		
		//CA.writeTransition(0,0,1,0,0,1); // move one cell to the right
		
		//CA.writeTransition(0,0,0,1,0,1); //move one cell to the left
		
		//CA.writeTransition(0,1,0,0,0,1); //move one cell up
		 
		//CA.writeTransition(1, 0, 0, 0, 0, 1); //move one cell down
		
//		CA.writeTransition(0,0,1,0,0,2);//move right alternating 1s and 2s
//		CA.writeTransition(0,0,2,0,0,1);
		
//		CA.randomizeTransitionMatrix();
//		CA.organism.get(CA.organismSize/2).set(CA.organismSize/2,1);

	}
}