
public class Pair
{
    private final int organismNumber;
    private final double fitness;

    public Pair(int aorganismNumber, double afitness)
    {
        organismNumber   = aorganismNumber;
        fitness = afitness;
    }

    public int organismNumber()   { return organismNumber; }
    public double fitness() { return fitness; }
}