package lateetud;

import java.util.ArrayList;
import java.util.List;

public class MatrixController {

	static int matrix[][] = { 
			{ 4, 5, 8 }, 
			{ 3, 9, 11 }, 
			{ 7, 3, 6 } 
			};

	public static void main(String args[]) throws Exception {
		ArrayList<Integer> list = matrixToLinearArrayList(matrix);
		System.out.println(list);
		//System.out.println(list.subList(0, matrix[0].length));

		System.out.println(findLocationFromArrayList(list, 2, 3));
	}

	public static ArrayList<Integer> matrixToLinearArrayList(int[][] matrix) {

		ArrayList<Integer> intList = new ArrayList<Integer>();
		// Loop through all rows
		for (int i = 0; i < matrix.length; i++) {
			// Loop through all elements in row
			for (int j = 0; j < matrix[i].length; j++) {
				// add to intList
				intList.add(matrix[i][j]);
			}
		}
		return intList;
	}

	/*
	 * This takes an already linearized ArrayList from a Matrix and allows you to pick out a coordinate
	 * a matrix would have and map it to the corresponding value in the linearized ArrayList.
	 */
	public static int findLocationFromArrayList(ArrayList<Integer> list, int rowPosition, int colPosition) throws Exception {
		
		// detect row,column count in matrix
		int numRows = matrix.length;
		int numCols = matrix[0].length;
		
		//rowPosition and colPosition cannot exceed matrix boundaries
		if(rowPosition > numRows) {
			throw new Exception("rowPosition cannot exceed matrix row length.");
		} else if(colPosition > numCols) {
			throw new Exception("colPosition cannot exceed matrix column count.");
		} else {
			
			// row, col boundaries are good
			// find the sublist index based on matrix rowPosition
			
			// indexing formula
			int index = (rowPosition-1)*numCols;
			List<Integer> subList = list.subList(index, index+numCols);
			// pick out the column position in the sublist
			return subList.get(numCols-1);
		
		}
		

		
	}

}
