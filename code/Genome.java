/**
 * Public interface for binding Organic and Corrected Genes
 *
 */
public interface Genome {
	
	// Gene getters
	/**
	 * Get a gene with at a specific grid coordinates.
	 * @param x, int, position along the x axis
	 * @param y, int, position along the y axis
	 * @return Gene at the given position.
	 */
	public Gene getGene(int x, int y);
	/**
	 * Get a gene at a specific index within the Genome.
	 * @param i, int, index of the Gene to be retrieved within the Genome
	 * @return Gene within the Genome at the given index.
	 */
	public Gene getGene(int i);
	
	// Reproduction methods
	/**
	 * Method to produce sexually with another Genome.
	 * @param g, the other parent to reproduce with
	 * @return Genome, a child Genome created by random combination of this Genome and the parent Genome passed as an argument.
	 */
	public Genome cross(Genome g);
	/**
	 * Mutate this Genome. 
	 */
	public void mutate();
	
	// Fitness methods
	/**
	 * Use to record this Genome's fitness value.
	 * @param fitness, int this Genome's Fitness value
	 */
	public void setFitness(int fitness);
	/**
	 * Method to return the Genome's fitness set earlier.
	 * @return
	 */
	public int getFitness();
}
