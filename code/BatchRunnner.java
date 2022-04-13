import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * BatchRunner class is a class specifically designed for batch running on the Program class.
 *
 */
public class BatchRunnner {

	/**
	 * Main method
	 * @param String[]: args, arguments passed through command line
	 */
	public static void main(String[] args) {
		// First Populate Genes, put in an array for easier set up of CorrectedGenome
		// class
		Gene[] genes = new Gene[10];
		genes[1] = new Gene(1);
		genes[2] = new Gene(2);
		genes[3] = new Gene(3);
		genes[4] = new Gene(4);
		genes[5] = new Gene(5);
		genes[6] = new Gene(6);
		genes[7] = new Gene(7);
		genes[8] = new Gene(8);
		genes[9] = new Gene(9);

		// Then create corrected Genome
		int arg = -1;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c"))
				arg = i;
		}
		if (arg == -1) {
			System.out.println("Please specify a field configuration by typing -c followed by a file name.");
			System.exit(0);
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(args[arg + 1]));

			try {
				int xOffset = 0;
				line: for (int line = 0; line < 11; line++) {
					int yOffset = 0;
					String s = in.readLine();
					cha: for (int cha = 0; cha < 11; cha++) {
						char c = s.charAt(cha);
						if (c == '.') {
							continue cha;
						}
						if (c == '!') {
							yOffset--;
							continue cha;
						}
						if (c == '-') {
							xOffset--;
							continue line;
						}
						CorrectedGenome.addRule(line + xOffset, cha + yOffset,
								genes[Integer.parseInt(String.valueOf(c))]);
					}
				}
			} catch (Exception e) {
				System.err.println("File in incompatible format.");
				in.close();
				System.exit(0);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File specified with the -c argument not found.");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Unknown IO error.");
		}
		
		// Gets the runName string from the arguments
		arg = -1;
		String runName;
		for (int i = 0; i<args.length; i++) {
			if(args[i].equals("-n")) arg = i;
		}
		if(arg == -1) {
			System.out.println("You can specify a name with the -n tag followed by a name in quatation marks.\n");
			runName = "Unspecified";
		} else {
			runName = args[arg+1];
		}

		// Setting mutation rate
		double muRate = 0.2;

		// Then create the threads
		int timesToRun = 5;
		int[] popSizes = {10,100,1000,10000};
		for(int popSize:popSizes) {
			for(int ran = 0; ran < timesToRun; ran++) {
				String name = "G"+runName+"PS"+popSize+"R"+ran;
				
				LinkedList<Genome> firstGeneration = new LinkedList<Genome>();
				for (int i = 0; i < popSize; i++) {
					Gene[] genome = new Gene[9 * 9];
					for (int j = 0; j < genome.length; j++) {
						genome[j] = Gene.getGene(ThreadLocalRandom.current().nextInt(Gene.numberOfGenes()));
					}
					firstGeneration.add(new CorrectedGenome(new OrganicGenome(genome)));
				}
				System.out.println("Starting thread "+name);

				Program p = new Program(name, firstGeneration, popSize, muRate, true);
				new Thread(p).start();
			}
		}
		
		

		

	}

}
