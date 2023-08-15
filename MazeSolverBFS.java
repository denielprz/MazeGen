import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/**
 * The MazeSolverBFS class implements a breadth first search to solve a maze
 * given an input file from the MazeGenerator Class. It prints out the steps (in
 * terms of index) of the correct solution, the number of steps taken by the
 * solution, the actual number of steps taken by the program (in case there are
 * wrong turns made) and the number of milliseconds it took to solve the maze.
 * The program also outputs a visual representation of the solution.
 * 
 * @version 1.0 23/09/2022
 * @author John Deniel Perez
 */
public class MazeSolverBFS extends Maze {
	// An ArrayList containing the indexes of the Cells that are part of the correct
	// solution.
	private ArrayList<Integer> solution;
	// A counter for the number of steps that the program takes to solve the maze.
	// It starts at -1 since the starting cell is not counted as a step.
	private int steps = -1;
	// A string that contains a list of the openness of each cell in the maze.
	private String openness;

	/**
	 * The constructor of the class.
	 * 
	 * @param rows       the number of rows of the maze.
	 * @param cols       the number of columns of the maze.
	 * @param startIndex the index of the starting cell.
	 * @param lastIndex  the index of the last cell.
	 * @param openness   the openness list of each cell in the maze.
	 */
	public MazeSolverBFS(int rows, int cols, int startIndex, int lastIndex, String openness) {
		super(rows, cols);
		this.startIndex = startIndex;
		this.startRow = getRow(startIndex);
		this.startCol = getCol(startIndex);
		this.lastIndex = lastIndex;
		this.lastRow = getRow(lastIndex);
		this.lastCol = getCol(lastIndex);
		this.openness = openness;
		setCells();
	}

	/**
	 * Overrides the setCells method of the superclass. It sets the index and
	 * openness of each cell in the 2D Array based on the input data. It also sets
	 * all the connected neighbor cells of each cells based on the openness.
	 *
	 */
	@Override
	public void setCells() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = new Cell(i, j);
				cells[i][j].setIndex(cols);
				cells[i][j].openness = Character.getNumericValue(openness.charAt(i * cols + j));
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				setNeighbors(getCell(i, j));
			}
		}
	}

	/**
	 * This method solves the maze by a queue implementation of breadth first
	 * search. Starting from the starting cell, it checks each unvisited neighbor of
	 * the cells in the queue and adds them to the queue if they are not the "final"
	 * cell that is being looked for. It increments the number of steps by each
	 * neighbor visited and sets those neighbors as visited. It also sets the
	 * "previous" cell of each neighbor cell as the current cell being checked in
	 * the loop. If the final cell is reached, it empties the queue and breaks the
	 * loop. It also calls the getSolution method.
	 * 
	 */
	public void solve() {
		Queue<Cell> path = new LinkedList<>();
		Cell start = getByIndex(this.startIndex);
		path.add(start);
		while (!path.isEmpty()) {
			Cell current;
			current = path.peek();
			current.visited = true;
			path.remove();
			steps++;
			for (Cell neighbor : current.neighbors) {
				if (neighbor.index == lastIndex) {
					neighbor.previous = current.index;
					steps++;
					path.removeAll(path);
					break;
				}
				if (neighbor.visited == false) {
					path.add(neighbor);
					neighbor.previous = current.index;
				}
			}
		}
		solution = getSolution();
	}

	/**
	 * This method gets the solution of the maze solved by the solve method.
	 * Starting from the "final" cell, it adds to an ArrayList the index of the cell
	 * previous to the current cell until it reaches the starting cell. It then
	 * takes the contents of the ArrayList and adds it in reverse to another
	 * ArrayList to get the solution in the correct order.
	 * 
	 * @return an ArrayList containing the indexes of the right solution to the
	 *         maze.
	 */
	private ArrayList<Integer> getSolution() {
		ArrayList<Integer> reverse = new ArrayList<Integer>();
		ArrayList<Integer> solution = new ArrayList<Integer>();
		int pointer = lastIndex;
		reverse.add(pointer);
		while (pointer != startIndex) {
			pointer = getByIndex(pointer).previous;
			reverse.add(pointer);
		}
		for (int i = reverse.size() - 1; i >= 0; i--) {
			solution.add(reverse.get(i));
		}
		return solution;
	}

	/**
	 * Prints out the index of each cell in the solution, the number of steps in the
	 * correct solution, and the number of steps actually taken by the program in
	 * looking for the solution.
	 * 
	 */
	private void printSolution() {
		System.out.println();
		System.out.print("( ");
		for (Integer i : solution) {
			System.out.print(i + " ");
		}
		System.out.println(")");
		System.out.println(solution.size() - 1);
		System.out.println(steps);
	}

	/**
	 * Based on the indexes in the ArrayList solution, this method replaces the
	 * Strings in the 2D Array of Strings with a * to point out the Cells that are
	 * part of the correct solution to the maze. It skips the first and last cell so
	 * that the S and F are not replaced.
	 * 
	 */
	private void drawPath() {
		for (Integer i : solution) {
			if (cells[getRow(i)][getCol(i)].index == startIndex || cells[getRow(i)][getCol(i)].index == lastIndex) {
				continue;
			} else if (cells[getRow(i)][getCol(i)].openness == 0 || cells[getRow(i)][getCol(i)].openness == 2) {
				graph[getRow(i) * 2 + 1][getCol(i) + 1] = "* |";
			} else {
				graph[getRow(i) * 2 + 1][getCol(i) + 1] = "*  ";
			}
		}
	}

	/**
	 * The main method of the program that tracks the number of milliseconds it
	 * takes to run the program. It reads a file based on the filename in the
	 * argument input by the user. It then uses the data in the file to reconstruct
	 * the maze into a 2D Array and then solve the maze using BFS. It then prints
	 * out the correct solution of the maze and a visual representation of the
	 * solution. It also prints out the number of steps in the solution, the actual
	 * number of steps taken by the program, and the amount of time it took to
	 * complete the program. The method also throws exceptions in case there are
	 * errors in the file input.
	 * 
	 * @param args the arguments in the input by the user.
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		String fileName = args[0];
		String[] mazeContent;
		String[] dimensions;
		try {
			File mazeFile = new File(fileName);
			Scanner reader = new Scanner(mazeFile);
			mazeContent = reader.nextLine().split(":");
			reader.close();

			dimensions = mazeContent[0].split(",");
			int rows = Integer.parseInt(dimensions[0]);
			int cols = Integer.parseInt(dimensions[1]);
			int initIndex = Integer.parseInt(mazeContent[1]);
			int lastIndex = Integer.parseInt(mazeContent[2]);
			String openness = mazeContent[3];

			MazeSolverBFS maze = new MazeSolverBFS(rows, cols, initIndex, lastIndex, openness);
			maze.solve();
			maze.drawGraph();
			maze.drawPath();
			maze.printGraph();
			maze.printSolution();

			long endTime = System.currentTimeMillis();
			System.out.println(endTime - startTime + "ms");
		} catch (FileNotFoundException e) {
			System.out.println("File is not found, please ensure the filename is correct.");
		} catch (NumberFormatException e) {
			System.out.println("An error was found within the file contents. Please ensure the data is correct.");
		}
	}
}
