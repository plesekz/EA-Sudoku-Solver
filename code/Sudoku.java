/**
 * A class representing a sudoku grid.
 * An instance of this class represent a specific fully filled in grid.
 */
public class Sudoku {
	/**
	 * The Sudoku grid configuration.
	 */
	private int[][] sudoku;
	
	/**
	 * Public constructor.
	 * @param g, Genome of 81 Gene length to be inputted into the Sudoku grid.
	 */
	Sudoku(Genome g){
		sudoku = new int[9][9];
		for(int x = 0; x<9; x++) {
			for(int y = 0; y<9; y++) {
				sudoku[x][y] = g.getGene(x,y).getValue();
			}
		}
	}
	/**
	 * Returns the number of times a number was seen again after being seen once in a
	 *   row
	 *   column
	 *   3x3 square.
	 * @return
	 */
	public int duplicits() {
		int d = 0;
		//rows
		for(int x = 0; x<sudoku.length;x++) {
			boolean[] seen = new boolean[9];
			for(int y = 0; y<sudoku[x].length; y++) {
				if(seen[sudoku[x][y]-1]) d++;
				seen[sudoku[x][y]-1] = true;
			}
		}
		//columns
		for(int x = 0; x<sudoku.length;x++) {
			boolean[] seen = new boolean[9];
			for(int y = 0; y<sudoku[x].length; y++) {
				if(seen[sudoku[y][x]-1]) d++;
				seen[sudoku[y][x]-1] = true;
			}
		}
		//squares
		for(int outerX = 0; outerX < 9; outerX = outerX+3) {
			for(int outerY = 0; outerY < 9; outerY = outerY+3) {
				boolean[] seen = new boolean[9];
				for(int x = 0; x < 3; x++) {
					for(int y = 0; y<3; y++) {
						if(seen[sudoku[x+outerX][y+outerY]-1]) d++;
						seen[sudoku[x+outerX][y+outerY]-1] = true;
					}
				}
			}
		}
		return d;
	}
}
