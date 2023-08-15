import java.util.ArrayList;

/**
 * The Cell class which represents each cell in the maze. It contains the cell
 * constructor and a method to set the index of the cell.
 * 
 * @version 1.0 23/09/2022
 * @author John Deniel Perez
 */

public class Cell {
	// The openness of the cell. It is 0 by default, where 0 means there is no edge
	// to the right or bottom of the cell. An openness of 1 means there is an edge
	// to the right of the cell, 2 means an edge to the bottom of the cell, and 3
	// means an edge to the right and bottom of the cell.
	int openness = 0;
	// This integer tracks the index of the cell that comes before it in a path.
	// Used in MazeSolverBFS to determine the path of the solution.
	int previous;
	// This boolean tracks if the cell has been visited. Used in both generation and
	// solving of the maze.
	boolean visited = false;
	// The row number of the cell.
	int row;
	// The column number of the cell/
	int col;
	// The index of the cell from 1 to n*m.
	int index;
	// An array of cells that are adjacent to the cell (not necessarily connected).
	Cell[] adjacent = new Cell[4];
	// An ArrayList of cells that are connected to the cell by an edge.
	ArrayList<Cell> neighbors = new ArrayList<Cell>();

	/**
	 * The constructor for the cell class.
	 * 
	 * @param row the row of the cell.
	 * @param col the column of the cell.
	 */
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Sets the index of the cell based on the number of columns in the maze.
	 * 
	 * @param cols the number of columns in the maze.
	 */
	public void setIndex(int cols) {
		this.index = (row) * cols + (col + 1);
	}
}
