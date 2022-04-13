To run the program:
	1. Navigate to the "sudoku.jar" file's location.
	2. run the file with "java -jar sudoku.jar" command.
		The command requires following mandatory arguments:

		-p (followed by an integer)
			Population size.

		-c [filename]
			Can be used to specify a file to be used for initial puzzle configuration.

		The command accepts following optional arguments:

		-m (followed by a decimal number between 1.0 and 0.0)
			Can be used to specify a mutant rate.
			Mutant rate represent the fraction of the population obtained by mutation.

			Default value = 0.2

		-n (followed by a string)
			Can be used to specify a run name.
			Will be used to name the output file.

			Default value "Unspecified"
When the program is run,
	it will report current progress each generation.

After the program finds a solution,
	it will report on the solution found into a .txt file named with the -n optional argument.



To run the program in a batch:
	1. Navigate to the "batch_sudoku.jar" file's location.
	2. run the file with "java -jar batch_sudoku.jar" command.
		The command requires following mandatory arguments:

		-c [filename]
			Can be used to specify a file to be used for initial puzzle configuration.

		The command accepts following optional arguments:

		-n (followed by a string)
			Can be used to specify a run name.
			Will be used to name the output file in the following format
				G(string inputed)PS(pop size)R(number of the run).txt

			Default value = "Unspecified"

			Default value "Unspecified"

When the program is run,
	it will start the base sudoku program for population sizes 10, 100, 1000 and 10000,
	it will run each configuration five times.
	it will report on current progress each minute.
After the program find a solution,
	it will report on the solution found into a .txt file named with the -n optional argument.
