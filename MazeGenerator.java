import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The MazeGenerator class generates a random maze with n rows and m columns
 * based on user input. It extends the base Maze class. It creates and
 * initializes a (n*m) 2D Array of Cells and randomly selects one as the
 * starting node. It then walks randomly from the current cell until all cells
 * in the 2D Array are visited. The final cell visited is set as the final cell
 * of the maze. The class then prints a visual representation of the maze and
 * writes to a file the number of rows, the number of columns, the starting
 * cell, the final cell, and a list of the openness of each cell in the maze
 * arranged by index.
 * 
 * @version 1.0 23/09/2022
 * @author John Deniel Perez
 */
public class MazeGenerator extends Maze {
	private Random random = new Random();

	/**
	 * The constructor of the class. It calls the superclass constructor and then
	 * initializes each cell in the 2D Array.
	 * 
	 * @param rows the number of rows of the maze.
	 * @param cols the number of columns of the maze.
	 */
	public MazeGenerator(int rows, int cols) {
		super(rows, cols);
		setCells();
	}

	/**
	 * The starting method of the class. It randomly selects a row number and a
	 * column number in the maze and uses those as the parameters to call the random
	 * walk method.
	 * 
	 */
	private void generate() {
		int startRow = random.nextInt(rows);
		int startCol = random.nextInt(cols);
		this.startRow = startRow;
		this.startCol = startCol;
		this.startIndex = startRow * cols + startCol + 1;
		walk(startRow, startCol);
	}

	/**
	 * The method that randomly generates the maze. It uses a stack implemented as
	 * an ArrayList to store and retrieve the cells from which the random walk is
	 * done. The walk continues as long as this stack is not empty. The top of the
	 * stack is removed and checked as the current cell. For each cell, it checks
	 * how many adjacent cells to it have not yet been visited. It then randomly
	 * selects one of the unvisited adjacent cells as the next cell to travel and
	 * saves its row, column and index number as a potential "last" cell. It then
	 * updates the openness of either the current cell or the next cell based on the
	 * position of both cells. Afterwards, it adds first the current cell, and then
	 * the next cell to the stack. If a current cell has no more unvisited adjacent
	 * cells, it skips back to the start of the loop until it reaches a Cell in the
	 * stack with unvisited adjacent cells or until the stack is empty.
	 * 
	 * @param startRow the row of the starting cell.
	 * @param startCol the column of the starting cell.
	 */
	private void walk(int startRow, int startCol) {
		// Stack implemented as an ArrayList to store the Cells that are walked through.
		ArrayList<Cell> path = new ArrayList<Cell>();
		// The starting cell is retrieved, then set as "visited" and added to the stack.
		Cell start = getCell(startRow, startCol);
		path.add(start);
		start.visited = true;
		// The method continues as long as the stack of Cells is not empty.
		while (!path.isEmpty()) {
			// Declares the current cell and initializes it by removing the cell at the top
			// of the stack.
			Cell current;
			current = path.remove(path.size() - 1);
			// Creates a new ArrayList for every loop that collects the number of unvisited
			// adjacent cells for the current cell.
			// i refers to the position of the unvisited cell. 0 is the cell above, 1 is the
			// cell to the right, 2 is the cell to the bottom, and 3 is the cell to the
			// left.
			ArrayList<Integer> unvisitedStep = new ArrayList<Integer>();
			for (int i = 0; i < 4; i++) {
				if (current.adjacent[i] != null && current.adjacent[i].visited == false) {
					unvisitedStep.add(i);
				}
			}
			// If the current cell has no unvisited adjacent cells, skip the rest of the
			// loop and go back.
			if (unvisitedStep.isEmpty()) {
				continue;
			}
			// Randomly select a step from all the unvisited steps and set that cell as the
			// next to be walked to.
			int nextStep = unvisitedStep.remove(random.nextInt(unvisitedStep.size()));
			Cell next = current.adjacent[nextStep];
			// Sets that next step as "visited" and stores its row, column and index number
			// in case it is the final cell to be visited.
			next.visited = true;
			lastRow = next.row;
			lastCol = next.col;
			lastIndex = next.index;
			// Sets the openness of either the current or next cell depending on the
			// position of both cells.
			if (nextStep == 1 || nextStep == 2) {
				current.openness += nextStep;
			} else if (nextStep == 0 || nextStep == 3) {
				next.openness += Math.abs(2 - nextStep);
			}
			// Adds the current cell to the stack (in case there are still unvisited steps
			// and the walk reaches a dead end at one point).
			path.add(current);
			// Adds the next cell to the top of the stack.
			path.add(next);
		}
	}

	/**
	 * This method returns a String of the openness of each Cell in the 2D Array
	 * arranged by index.
	 * 
	 * @return returns a String of the openness of each Cell in the 2D Array
	 *         arranged by index.
	 */
	private String printOpenness() {
		String openness = "";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				openness += cells[i][j].openness;
			}
		}
		return openness;
	}

	/**
	 * The main method of the class. Takes an input of 3 arguments (two numbers and
	 * a String). The first two arguments are the number of rows and columns of the
	 * maze to be generated and the third argument is the filename of the file to be
	 * created. The method then generates a maze, prints out the maze and outputs
	 * data about the maze to a file. The method also throws out exceptions in case
	 * there are issues with the arguments.
	 * 
	 * @param args	the arguments in the input by the user.
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			throw new IllegalArgumentException(
					"An exception occurred. Please enter exactly 3 arguments (e.g., 5 5 filename).");
		}

		int rows;
		int cols;
		try {
			rows = Integer.parseInt(args[0]);
			cols = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("An exception occurred. Please ensure your first two arguments are numbers.");
			return;
		}
		String fileName = args[2];

		try {
			MazeGenerator maze = new MazeGenerator(rows, cols);
			maze.generate();
			maze.drawGraph();
			if (rows < 25 && cols < 25) {
				maze.printGraph();
			}
			System.out.println();

			String fileData = rows + "," + cols + ":" + maze.startIndex + ":" + maze.lastIndex + ":"
					+ maze.printOpenness();
			File mazeFile = new File(fileName);
			FileWriter writer = new FileWriter(mazeFile);
			writer.write(fileData);
			writer.close();
		} catch (OutOfMemoryError e) {
			System.out.println(
					"The number of rows and columns in the input is too large. Please try again with smaller numbers.");
		} catch (IOException e) {
			System.out.println("An exception occurred. Please check your arguments.");
			return;
		}

	}
}
