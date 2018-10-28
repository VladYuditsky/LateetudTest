package lateetud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import lateetud.MatrixController;

public class MatrixControllerTest {

	int[][] matrix = MatrixController.matrix;
	ArrayList<Integer> matrixList = MatrixController.matrixToLinearArrayList(matrix);
	
	@BeforeEach
	public void testMatrixNotNull() {
		Assert.assertNotNull(matrix);
	}
	

	// matrixToLinearArrayList tests
	@Test
	public void testArrayListNotNull() {
		Assert.assertNotNull(matrixList);
	}
	
	@Test
	public void testMatrixHasSameAmountValues() {
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		int matrixSize = rows*cols;
		Assert.assertEquals(matrixSize, matrixList.size());
	}
	
	@Test
	public void testMatrixHasCorrectValues() {
		
		// loop through rows
		for(int i=0; i<matrix.length; i++) {			
			// loop through elements in row
			for(int j = 0; j<matrix[i].length; j++) {
				Assert.assertTrue(matrixList.contains(matrix[i][j]));
			}
		}
	}
	
	@Test
	public void testMatrixDoesNotHaveUnknownValue() {
		
		// generate random number 1-100
		Random rand = new Random();
		int num = rand.nextInt(50 - 0 + 1) + 1;
		// keep trying until it is not in List
		while(matrixList.contains(num)) {
			num = rand.nextInt(50 - 0 + 1) + 1;
		}
		Assert.assertFalse(matrixList.contains(num));
	}
	
	// findLocationFromArrayList tests
	@ParameterizedTest
	@ValueSource(ints = {0, 4, 99})
	public void testExceptionForOutOfBoundsRow(final int key) {
		
		assertThrows(Exception.class, ()->{
			MatrixController.findLocationFromArrayList(matrixList, key, 1);			
		});
	}
	
	@ParameterizedTest
	@ValueSource(ints = {4, 99})
	public void testExceptionForOutOfBoundsCol(final int key) {
		
		assertThrows(Exception.class, ()->{
			MatrixController.findLocationFromArrayList(matrixList, 1, key);			
		});
	}
	
	/*
	@Test
	public void testLocationValues() throws Exception {
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		int counter=0;
		int currentRow = 0;
		
		// loop through as many times as there are matrix values
		while(counter<rows*cols) {
			
			if(counter%cols==0) {
				currentRow++;
				for(int i=1; i<=cols; i++) {
					//System.out.println(currentRow + ", " + i);
					System.out.println(MatrixController.findLocationFromArrayList(matrixList, currentRow, i));
					Assert.assertEquals(matrix[currentRow][i], MatrixController.findLocationFromArrayList(matrixList, currentRow, i));
				}
			}
			counter++;
		}

		
	}
	*/
	
	
}
