import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main program class.
 * Either run as a Jar file.
 * Instance can be ran in a thread.
 * If the instance is ran in a batch mode it reports on its finding only once a minute.
 */
public class Program implements Runnable {
	// static section of the class, present to allow it to run
	
	/**
	 * Internal comparator for comparing Genomes.
	 */
	private static final Comparator<Genome> c = (Genome g, Genome h)->{
		return g.getFitness()-h.getFitness();
	};
	
	/**
	 * main method of the program
	 * @param args String[] passed from the console
	 */
	public static void main(String[] args) {
		
		// First Populate Genes, put in an array for easier set up of CorrectedGenome class
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
		for (int i = 0; i<args.length; i++) {
			if(args[i].equals("-c")) arg = i;
		}
		if(arg == -1) {
			System.out.println("Please specify a field configuration by typing -c followed by a file name.");
			System.exit(0);
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(args[arg+1]));
			
			try {
				int xOffset = 0;
				line:
				for(int line = 0; line<11; line++) {
					int yOffset = 0;
					String s = in.readLine();
					cha:
					for(int cha = 0; cha <11; cha++) {
						char c = s.charAt(cha);
						if (c == '.') {
							continue cha;
						}
						if (c == '!') {
							yOffset--;
							continue cha;
						}
						if(c=='-') {
							xOffset--;
							continue line;
						}
						CorrectedGenome.addRule(line+xOffset, cha+yOffset, genes[Integer.parseInt(String.valueOf(c))]);
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
		
		// Getting mutation rate, if specified
		arg = -1;
		double muRate;
		for (int i = 0; i<args.length; i++) {
			if(args[i].equals("-m")) arg = i;
		}
		if(arg == -1) {
			System.out.println("You can specify a mutation rate with the -m tag followed by a double.\nMutation rate represents the proportion of the population obtained by mutating previous generation.\nMutation rate for values above 1.0 is undefined.");
			muRate = 0.2;
		} else {
			muRate = Double.parseDouble(args[arg+1]);
		}
		
		// Getting run name, if specified
		arg = -1;
		String name;
		for (int i = 0; i<args.length; i++) {
			if(args[i].equals("-n")) arg = i;
		}
		if(arg == -1) {
			System.out.println("You can specify a name with the -n tag followed by a name in quatation marks.\n");
			name = "Unspecified";
		} else {
			name = args[arg+1];
		}
		
		
		// Then create the first generation
		arg = -1;
		for (int i = 0; i<args.length; i++) {
			if(args[i].equals("-p")) arg = i;
		}
		if(arg == -1) {
			System.out.println("Please specify a population size with -p followed by a number.");
			System.exit(0);
		}
		int popSize = Integer.parseInt(args[arg+1]);
		
		LinkedList<Genome> firstGeneration = new LinkedList<Genome>();
		for(int i = 0; i<popSize; i++) {
			Gene[] genome = new Gene[9*9];
			for(int j = 0; j < genome.length; j++) {
				genome[j] = Gene.getGene(ThreadLocalRandom.current().nextInt(Gene.numberOfGenes()));
			}
			firstGeneration.add(new CorrectedGenome(new OrganicGenome(genome)));
		}
		
		Program p = new Program(name, firstGeneration, popSize, muRate);
		p.run();
	}
	
	// Dynamic section of the class
	
	//private HashMap<Integer, LinkedList<Genome>> geneticLibrary;
	/**
	 * Linked List containing the new, yet to be evaluated Generation.
	 */
	private LinkedList<Genome> newGeneration;
	/**
	 * This instance's name.
	 */
	private String name;
	/**
	 * Population size of this instance.
	 */
	private int popSize;
	/**
	 * Mutation rate of this instance.
	 */
	private double mutationRatio;
	/**
	 * Current Generation the instance is working on.
	 */
	private int generation;
	/**
	 * Controls whether the instance runs in the batch mode.
	 */
	private boolean batch;
	/**
	 * Control whether the instance found a solution.
	 */
	private boolean finished;
	
	/**
	 * Private constructor initialising all fields.
	 */
	private Program(){
		this.name = "Unknown";
		this.generation = 0;
		this.batch = false;
		this.finished = false;
		//this.geneticLibrary = new HashMap<Integer, LinkedList<Genome>>();
	}
	/**
	 * Public constructor for initialising an instance.
	 * @param name, name for this instance, controls the name of the output file.
	 * @param initialGenomes, LinkedList<Genome> of the initial population.
	 * @param activePopulationSize, the population size this instance should be working with.
	 * @param mutationRatio, double the fraction of the mutant population in each generation.
	 */
	Program(String name, LinkedList<Genome> initialGenomes, int activePopulationSize, double mutationRatio){
		this();
		this.name = name;
		this.newGeneration = initialGenomes;
		this.popSize = activePopulationSize;
		this.mutationRatio = mutationRatio;
	}
	/**
	 * Public constructor for initialising an instance.
	 * @param name, name for this instance, controls the name of the output file.
	 * @param initialGenomes, LinkedList\<Genome\> of the initial population.
	 * @param activePopulationSize, the population size this instance should be working with.
	 * @param mutationRatio, double the fraction of the mutant population in each generation.
	 * @param batch, boolean that controls whether instance is ran in the batch mode. true for batch mode.
	 */
	Program(String name, LinkedList<Genome> initialGenomes, int activePopulationSize, double mutationRatio, boolean batch){
		this(name, initialGenomes, activePopulationSize, mutationRatio);
		this.batch = batch;
	}
	
	public void run(){
		double averageFitness = 0.0;
		int minimumFound = Integer.MAX_VALUE;
		Genome minimumGenome = null;
		Instant startTime = Instant.now();
		Reporter r = null;
		// if ran in the batch mode create and start an instance of the internal Reporter class
		if(batch) {
			r = new Reporter();
			new Thread(r).start();
		}
		
		// the main loop evaluating generation after generation
		while(true) {
			evalCurrentGenFitness();
						
			// evaluating stats for current generation
			
			averageFitness = 0.0;
			for(Genome g:newGeneration) {
				averageFitness+=g.getFitness();
				if(g.getFitness()<minimumFound) {
					minimumGenome = g.cross(g);
					minimumFound = g.getFitness();
					minimumGenome.setFitness(minimumFound);
				}
			}
			averageFitness = averageFitness/popSize;
			
			// reporting on current gen
			
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			sb.append(name);
			sb.append(":");
			sb.append("\n\tGeneration number: ");
			sb.append(generation);
			sb.append("\n\tAverage fitness: ");
			sb.append(averageFitness);
			sb.append("\n\tMinimum fitness: ");
			sb.append(minimumFound);
			sb.append("\n\tMimum genome:\n");
			sb.append(minimumGenome.toString());
			sb.append("\n");
			
			if(!batch) {
				System.out.println(sb.toString());
			} else {
				r.setOutput(sb.toString());
			}			
			
			// ending the program if solution found
			if(minimumFound==0) {
				break;
			}
			
			startNewGeneration(averageFitness);
			
			
		}
		Instant now = Instant.now();
		Duration d = Duration.between(startTime, now);
		StringBuilder sb = new StringBuilder();
		sb.append(now.toString());
		sb.append("\n");
		sb.append(name);
		sb.append(" finished after ");
		sb.append(d.toString());
		System.out.println(sb.toString());
		
		finished = true;
		
		makeReport(d, minimumGenome);
	}
	
	/**
	 * An internal method used for better readability.
	 * This method evaluates the current generation and gives each Genome a fitness value.
	 */
	private void evalCurrentGenFitness() {
		if(!batch)
			System.out.println(Instant.now().toString()+"\n\t"+ name +" started evaluation of generation number "+generation);
		
		// figure out how many threads are needed and how many genomes will be evaluated per thread
		int nOfThreads = (int) Math.ceil(Math.sqrt(newGeneration.size()));
		int numberOfGenomesPerThread = (int) Math.round(newGeneration.size()/(double)nOfThreads);
		
		// assign genomes to threads, barring the last one
		int lastIndexAssigned = 0;
		GenomeFitness[] evaluations = new GenomeFitness[nOfThreads];
		for(int i = 0; i<nOfThreads-1; i++) {
			evaluations[i] = new GenomeFitness(newGeneration.subList(lastIndexAssigned, lastIndexAssigned+numberOfGenomesPerThread));
			lastIndexAssigned = lastIndexAssigned + numberOfGenomesPerThread;
		}
			// assigning leftover genomes to the last thread
		evaluations[evaluations.length-1] = new GenomeFitness(newGeneration.subList(lastIndexAssigned, newGeneration.size()));
		
		// starting and executing the threads
		Thread[] threads = new Thread[nOfThreads];
		for(int i = 0; i<nOfThreads; i++) {
			threads[i] = new Thread(evaluations[i]);
		}
		for(int i = 0; i<nOfThreads; i++) {
			threads[i].start();
		}
		for(int i = 0; i<nOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		return;
	}
	
	/**
	 * An internal method for better readability.
	 * Creates a newGeneration from the current generation.
	 * @param averageFitness, cutoff value, Genomes with fitness above this value will not be allowed to reproduce.
	 */
	private void startNewGeneration(double averageFitness) {
		//starting new generation
		generation++;
		LinkedList<Genome> oldGeneration = newGeneration;
		newGeneration = new LinkedList<Genome>();

		if(!batch)
			System.out.println(Instant.now().toString()+"\n\t"+ name +" started making generation number "+generation);
		
		
		// make new children
		int mutants = (int) Math.ceil(mutationRatio*popSize);
		
		// make new children through biological means
		int biologicalChildrenToBeMade = popSize-mutants;
		
		
		oldGeneration.sort(c);
		int cutout = 0;
		for(Genome g:oldGeneration) {
			if(g.getFitness()>averageFitness)
				break;
			cutout++;
		}
		
		while(biologicalChildrenToBeMade>0) {
			Genome p1 = oldGeneration.get(ThreadLocalRandom.current().nextInt(cutout));
			Genome p2 = oldGeneration.get(ThreadLocalRandom.current().nextInt(cutout));
			Genome g = p1.cross(p2);
			newGeneration.add(g);
			biologicalChildrenToBeMade--;
		}
		
		// make new children through mutation
		
		while(mutants>0) {
			Genome g = oldGeneration.get(ThreadLocalRandom.current().nextInt(cutout));
			g.mutate();
			newGeneration.add(g);
			mutants--;
		}
		
	}
	/**
	 * An internal helper method for creating a report after the instance finishes.
	 * @param d, Duration instance. The duration of the run.
	 * @param minimumGenome, the solution found
	 */
	private void makeReport(Duration d, Genome minimumGenome) {
		StringBuilder sb = new StringBuilder();
		sb.append("=== ");
		sb.append(name);
		sb.append(" ===\n");
		sb.append("\tfinished after: ");
		sb.append(d.toString());
		sb.append("\n");
		sb.append("\tIn ");
		sb.append(generation);
		sb.append(" generations.");
		sb.append("\n");
		sb.append("\tPopulation size: ");
		sb.append(popSize);
		sb.append("\n\t");
		sb.append("Mutated population proportion: ");
		sb.append(mutationRatio);
		sb.append("\n");
		
		sb.append("Solution:\n");
				
		sb.append(minimumGenome.toString());
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(name+".txt"));
			out.append(sb.toString());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Internal private class for handling the output in a batch mode.
	 * Reports on the current progress once a minute.
	 */
	private class Reporter implements Runnable {
		private volatile String output;
		
		public void setOutput(String output) {
			this.output = output;
		}

		@Override
		public void run() {
			while(!finished){
				try {
					Thread.sleep(60*1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(output);
			}
		}
		
	}
}
