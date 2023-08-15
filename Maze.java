/**
 * The base Maze class from where MazeGenerator and MazeSolverBFS extend from.
 * The class contains the superclass constructor, as well as methods for
 * creating the 2D Array of cells, and the 2D Array of characters for the visual
 * representation of the maze.
 *
 * @version 1.0 23/09/2022
 * @author John Deniel Perez
 */

public class Maze {
	// 2D Array of Cells that represents the undirected grid graph for the maze.
	public Cell[][] cells;
	// 2D Array of Strings that represents the visual representation of the maze.
	public String[][] graph;
	// The number of rows and columns of the maze.
	public final int rows, cols;
	// The index (from 1 to n*m), row number and column number of the starting cell
	// and the final cell of the maze.
	public int startRow, startCol, startIndex, lastRow, lastCol, lastIndex;

	/**
	 * Constructor for the maze superclass, where the 2D Arrays are initialized.
	 * 
	 * @param rows the number of rows of the maze.
	 * @param cols the number of columns of the maze
	 */
	public Maze(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		cells = new Cell[rows][cols];
		graph = new String[rows * 2 + 1][cols + 1];
	}

	/**
	 * Initializes each cell in the 2D Array of Cells and sets their index. It then
	 * sets the cells adjacent to each cell.
	 * 
	 */
	public void setCells() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = new Cell(i, j);
				cells[i][j].setIndex(cols);
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j].adjacent[0] = getCell(i - 1, j);
				cells[i][j].adjacent[1] = getCell(i, j + 1);
				cells[i][j].adjacent[2] = getCell(i + 1, j);
				cells[i][j].adjacent[3] = getCell(i, j - 1);
			}
		}
	}

	/**
	 * Returns the cell found in the specified row and column number. If the
	 * specified cell is not within the 2D Array, returns null.
	 * 
	 * @param row the row where the cell is located.
	 * @param col the column where the cell is located.
	 * @return returns the cell found in the specified row and column. returns null
	 *         if the cell is not within the 2D Array.
	 */
	public Cell getCell(int row, int col) {
		try {
			return cells[row][col];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Returns the cell that has the specified index. Calculates the row and column
	 * number of the cell using the index and then uses the getCell method to return
	 * the requested cell.
	 * 
	 * @param index the index of the requested cell.
	 * @return returns the cell in the specified index. returns null if the cell is
	 *         not within the 2D Array.
	 */
	public Cell getByIndex(int index) {
		int row = getRow(index);
		int col = getCol(index);
		return getCell(row, col);
	}

	/**
	 * Calculates the row number of the cell given the index of the cell.
	 * 
	 * @param index the index of the requested cell.
	 * @return returns the row number of the cell.
	 */
	public int getRow(int index) {
		int row = index / cols;
		if (index % cols == 0) {
			row -= 1;
		}
		return row;
	}

	/**
	 * Calculates the column number of the cell given the index of the cell.
	 * 
	 * @param index the index of the requested cell.
	 * @return returns the column number of the cell.
	 */
	public int getCol(int index) {
		int col = (index % cols) - 1;
		if (col == -1) {
			col = cols - 1;
		}
		return col;
	}

	/**
	 * Sets the neighbors (the cells connected to the cell by an edge) of the
	 * specific cell based on the cell's openness. The method also sets the given
	 * cell as a neighbor to its neighbor cells.
	 * 
	 * @param cell the cell to be assigned neighbors.
	 */
	public void setNeighbors(Cell cell) {
		if (cell.openness == 1 || cell.openness == 3) {
			Cell neighbor = getCell(cell.row, cell.col + 1);
			cell.neighbors.add(neighbor);
			neighbor.neighbors.add(cell);
		}
		if (cell.openness == 2 || cell.openness == 3) {
			Cell neighbor = getCell(cell.row + 1, cell.col);
			cell.neighbors.add(neighbor);
			neighbor.neighbors.add(cell);
		}
	}

	/**
	 * Fills the 2D Array of Strings with characters to serve as a visual
	 * representation of the maze. The method uses the openness of every Cell in the
	 * 2D Array of Cells to determine what character to place in the maze. The
	 * method also sets the position of the starting Cell as "S " and the position
	 * of the final Cell as "F ".
	 * 
	 */
	public void drawGraph() {
		for (int i = 1; i < rows * 2; i++) {
			graph[i][0] = "|";
		}
		graph[0][0] = "-";
		graph[rows * 2][0] = "-";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				switch (cells[i][j].openness) {
				case 0:
					graph[(i * 2) + 1][j + 1] = "  |";
					graph[(i * 2) + 2][j + 1] = "--|";
					break;
				case 1:
					graph[(i * 2) + 1][j + 1] = "   ";
					graph[(i * 2) + 2][j + 1] = "--|";
					break;
				case 2:
					graph[(i * 2) + 1][j + 1] = "  |";
					graph[(i * 2) + 2][j + 1] = "  |";
					break;
				case 3:
					graph[(i * 2) + 1][j + 1] = "   ";
					graph[(i * 2) + 2][j + 1] = "  |";
					break;
				}
			}
		}
		if (cells[startRow][startCol].openness == 0 || cells[startRow][startCol].openness == 2) {
			graph[startRow * 2 + 1][startCol + 1] = "S |";
		} else {
			graph[startRow * 2 + 1][startCol + 1] = "S  ";
		}
		if (cells[lastRow][lastCol].openness == 0 || cells[lastRow][lastCol].openness == 2) {
			graph[lastRow * 2 + 1][lastCol + 1] = "F |";
		} else {
			graph[lastRow * 2 + 1][lastCol + 1] = "F  ";
		}
		for (int j = 1; j <= cols; j++) {
			graph[0][j] = "---";
			graph[rows * 2][j] = "---";
		}
	}

	/**
	 * This method prints out all of the Strings in the 2D Array of Strings.
	 * 
	 */
	public void printGraph() {
		for (int i = 0; i <= rows * 2; i++) {
			System.out.println();
			for (int j = 0; j <= cols; j++) {
				System.out.print(graph[i][j]);
			}
		}
	}
}
