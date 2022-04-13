import java.util.HashMap;

/**
 * CorrectedGenome class corrects a contained Genome according to the statically defined preset rules.
 */
public class CorrectedGenome implements Genome {
	private static HashMap<Integer, Gene> rules = new HashMap<Integer, Gene>();
	private Genome genome;
	
	/**
	 * This method is used to add a rule to apply to Genomes within all CorrectedGenome objects.
	 * @param i, Integer defining the gene's index.
	 * @param g, Gene the gene to placed at the index.
	 */
	static void addRule(Integer i, Gene g) {
		rules.put(i, g);
	}
	
	/**
	 * adding a rule[x][y]
	 * 
	 *     0 1 2 
	 *   * - - - y
	 * 0 | 0 1 2
	 * 1 | 3 4 5
	 * 2 | 6 7 8
	 *   x
	 * 
	 * @param x: Integer of x axis
	 * @param y: Integer of y axis
	 * @param g, the Gene to be placed at the location.
	 */
	static void addRule(int x, int y, Gene g) {
		rules.put(x*9+y, g);
	}
	
	/**
	 * Constructor
	 * @param genome the Genome to be encapsulated
	 */
	CorrectedGenome(OrganicGenome genome){
		this.genome=genome;
	}
	/**
	 * Constructor
	 * @param genome: the Genome to be encapsulated
	 */
	CorrectedGenome(Genome genome){
		this.genome=genome;
	}
	@Override
	public Gene getGene(int x, int y) {
		if(rules.containsKey(x*9+y)) {
			return rules.get(x*9+y);
		}
		return genome.getGene(x, y);
	}
	@Override
	public Gene getGene(int i) {
		if(rules.containsKey(i)) {
			return rules.get(i);
		}
		return genome.getGene(i);
	}
	@Override
	public Genome cross(Genome g) {
		return new CorrectedGenome(genome.cross(g));
	}
	@Override
	public void mutate() {
		genome.mutate();		
	}
	@Override
	public void setFitness(int fitness) {
		genome.setFitness(fitness);
	}
	@Override
	public int getFitness() {
		return genome.getFitness();
	}
	public String toString() {
		return genome.toString();
	}
	
}
