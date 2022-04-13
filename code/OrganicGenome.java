import java.util.concurrent.ThreadLocalRandom;

/**
 * instances of the OrganicGenome class represent a specific OrganicGenome capable of mutation and sexual reproduction.
 */
public class OrganicGenome implements Genome {
	// Properties
	
	/**
	 * Internal property used to set the probability of Genome mutating at all when the mutate method is called on it.
	 * The probability is equal to 1/genomeMutationRate.
	 */
	private static final int genomeMutationRate = 1; // Ratio = 1:rate
	/**
	 * Internal propert used to set the probability of each single Gene mutating when the mutate method is called on this Genome.
	 * The probability is equal to 1/geneMutationRate.
	 */
	private static final int geneMutationRate = 10; // Ratio = 1:rate
	
	// Dynamic variables
	/**
	 * An array of Gene objects creating the Genome itself.
	 */
	private Gene[] genome;
	/**
	 * A variable storing the Genome's fitness once determined.
	 */
	private int fitness;
	
	//methods
	/**
	 * An internal constructor creating an empty Genome with a set length.
	 * @param i, the Genome lenght
	 */
	private OrganicGenome(int i){
		genome = new Gene[i];
	}
	/**
	 * Constructor creating a Genome consisting of the Gene[] array.
	 * @param genome, a Gene[] array that the Genome will consist off.
	 */
	OrganicGenome(Gene[] genome){
		this.genome = genome;
	}
	/**
	 * An internal method that sets a Gene at a specific index.
	 * @param i, int, Gene's index
	 * @param g, Gene the Gene to be set to the Given index.
	 */
	private void setGene(int i, Gene g) {
		genome[i] = g;
	}
	
	//Defined by interface
	public Gene getGene(int x, int y) {
		return genome[x*9+y];
	}
	public Gene getGene(int i) {
		return genome[i];
	}
	public void mutate() {
		if(ThreadLocalRandom.current().nextInt(genomeMutationRate)==0) {
			for(int i = 0; i<genome.length; i++) {
				if(ThreadLocalRandom.current().nextInt(geneMutationRate)==0) {
					genome[i] = Gene.getGene(ThreadLocalRandom.current().nextInt(Gene.numberOfGenes()));
				}
			}
		}
	}
	public Genome cross(Genome g) {
		OrganicGenome child = new OrganicGenome(genome.length);
		for(int i = 0; i<genome.length; i++) {
			if(ThreadLocalRandom.current().nextBoolean()) {
				child.setGene(i, genome[i]);
			} else {
				child.setGene(i, g.getGene(i));
			}			
		}
		return child;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Fitness:");
		sb.append(this.getFitness());
		sb.append("\n");
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y<9; y++) {
				sb.append(genome[x*9+y]);
				if(y == 2) sb.append('!');
				if(y == 5) sb.append('!');
			}
			sb.append("\n");
			if(x == 2) sb.append("---!---!---\n");
			if(x == 5) sb.append("---!---!---\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	@Override
	public void setFitness(int fitness) {
		this.fitness = fitness;	
	}
	public int getFitness() {
		return fitness;
	}
}
