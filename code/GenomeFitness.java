import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for evaluating Genome fitness.
 * Use:
 * 	Create instance of this class and pass it the list of Genomes to be evaluted.
 * 	Run created instance in a thread to evaluate all Genomes passed to it during construction.
 * 	Retrieve the evaluated Genomes with GetList
 */
public class GenomeFitness implements Runnable {
	/**
	 * Internal list to hold the Genomes (to be) evaluated
	 */
	private List<Genome> genomes;
	/**
	 * Evaluated Genomes held in a hash map according to their fitness.
	 */
	private HashMap<Integer, LinkedList<Genome>> result;

	/**
	 * Public constructor
	 * @param list, List of the Genomes to be evaluated.
	 */
	public GenomeFitness(List<Genome> list) {
		this.genomes = list;
		this.result = new HashMap<Integer, LinkedList<Genome>>();
	}

	/**
	 * Deprecated function.
	 * Returns a HashMap containing processed Genomes mapped against their fitness.
	 * @return a HashMap containing processed Genomes mapped against their fitness.
	 */
	@Deprecated
	public HashMap<Integer, LinkedList<Genome>> getResults() {
		return result;
	}
	
	/**
	 * Function to retrieve the list initially passed to the class in the constructor.
	 * @return
	 */
	public List<Genome> getList(){
		return genomes;
	}

	@Override
	public void run() {
		for(Genome g: genomes) {
			Sudoku s = new Sudoku(g);
			int d = s.duplicits();
			if(!result.containsKey(d)) {
				result.put(d, new LinkedList<Genome>());
			}
			result.get(d).add(g);
			g.setFitness(d);
		}
	}
}
