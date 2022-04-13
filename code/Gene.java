import java.util.HashMap;

public class Gene {
	// static values
	/**
	 * Internal Hash map storing all Genes in the system by index.
	 */
	private static HashMap<Integer, Gene> allGenes = new HashMap<Integer,Gene>();
	
	//dynamic values
	/**
	 * The value the Gene takes on.
	 */
	private int value;
	
	/**
	 * Constructor
	 * @param value, the Integer value the Gene carries.
	 * Gene gets automatically added to the library of all Genes in the system after it is created.
	 */
	Gene(int value){
		this.value = value;
		allGenes.put(allGenes.size(), this);
	}
	/**
	 * Get the value the Gene carries.
	 * @return
	 */
	public int getValue(){
		return value;
	}
	/**
	 * Get the number of Genes in the system.
	 * @return Integer the number of Genes in the system.
	 */
	public static int numberOfGenes() {
		return allGenes.size();
	}
	/**
	 * Get a Gene with a specific index.
	 * @param i, int index
	 * @return Gene at the given index.
	 */
	public static Gene getGene(int i) {
		return allGenes.get(i);
	}
	public String toString() {
		return String.valueOf(value);
	}
}
