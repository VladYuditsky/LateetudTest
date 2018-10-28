package lateetud;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import commonUtils.Utils;	

public class DocumentParserTest {

	String fakeFile = "fakeFile.txt";
	// Create Utils object
	Utils common = new Utils();
	
	@BeforeEach
	public void testInputFileNotNull() {
		Assert.assertNotNull(DocumentParser.FILENAME);
	}
	
	// test for all methods
	@Test
	public void testFakeFileException() {
		
		assertAll("Output of each should return IOException", 
			()-> assertThrows(IOException.class, ()->{
				DocumentParser.extractAllDates(fakeFile);			
			}),
			()-> assertThrows(IOException.class, ()->{
				DocumentParser.extractAllAmounts(fakeFile);			
			})
		
		);
	}
		
	/*
	 * Date Testing
	 */
	@Test
	public void testDateArrayListNotNull() throws IOException {
		Assert.assertNotNull(DocumentParser.extractAllDates(DocumentParser.FILENAME));
	}
	
	@Test
	public void testDateArrayListContainsDates() throws IOException {
	
		// Define Months
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    ArrayList<String> months = new ArrayList<String>(Arrays.asList(monthNames));
	    // initialize ArrayList dates extracted
		ArrayList<String> dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		
		for(int i=0; i<dateList.size(); i++) {
			
			// check if dateList values have months
			String data = dateList.get(i);
			String month = data.split("\\s+")[0];
			
			Assert.assertTrue(months.contains(month));
		}
		
	}
	
	@Test
	public void testDateArrayListContainsLineNumbers() throws IOException {
		
		ArrayList<String> dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		for(int i=0; i<dateList.size(); i++) {
			
			String data = dateList.get(i);
			
			// first check if all data have @ symbol
			Assert.assertTrue(data.contains("@"));
			data = data.split("@ ")[1];
			// check if data has LINE:
			Assert.assertTrue(data.contains("LINE:"));
			// check if data has line number
			data = data.split(":")[1];
			Assert.assertTrue(common.isNumeric(data));
		}
	}
	
	
	// test adding more date lines
	@ParameterizedTest
	@ValueSource(strings = {"Adding date: November 26, 1995 is when I was born", "Today's date is: October 25, 2018"})
	public void testAddingDateLineToFile(String key) throws IOException {
		
		ArrayList<String> dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		// First check ArrayList does not contain values before adding them to file
		Assert.assertFalse(dateList.contains(key));
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new date is added to arrayList
		dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		
		// parse date from key and format
		String dateKey = key.split(": ")[1];
		String year = key.split(": ")[1].split(", ")[1].split("\\s+")[0];
		dateKey = dateKey.split(", ")[0] + "," + year;
		
		// always should come out to line 156 in the file
		Assert.assertTrue(dateList.contains(dateKey + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
		
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"This line does not have a date.", "Just testing my method :) "})
	public void testAddingRandomStringsDate(String key) throws IOException {
		
		ArrayList<String> dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new date is not added to arrayList
		dateList = DocumentParser.extractAllDates(DocumentParser.FILENAME);
		Assert.assertFalse(dateList.contains(key + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
	}
	
	
	/*
	 * Amount Testing
	 */
	@Test
	public void testAmountArrayListNotNull() throws IOException {
		Assert.assertNotNull(DocumentParser.extractAllAmounts(DocumentParser.FILENAME));
	}
	
	@Test
	public void testAmountArrayListContainsAmounts() throws IOException {
	
		ArrayList<String> amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		for(int i=0; i<amountList.size(); i++) {
			
			// check if amountList values have $
			String data = amountList.get(i);
			data = data.split("//s+")[0];
			Assert.assertTrue(data.contains("$"));
		}
	}
	
	@Test
	public void testAmountArrayListContainsLineNumbers() throws IOException {
		
		ArrayList<String> amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		for(int i=0; i<amountList.size(); i++) {
			
			String data = amountList.get(i);
			// first check if all data have @ symbol
			Assert.assertTrue(data.contains("@"));
			data = data.split("@ ")[1];
			// check if data has LINE:
			Assert.assertTrue(data.contains("LINE:"));
			// check if data has line number
			data = data.split(":")[1];
			Assert.assertTrue(common.isNumeric(data));
		}
	}
	
	// test adding more amount lines
	@ParameterizedTest
	@ValueSource(strings = {"Adding amount: $100,000.00 is a developer's salary.", "I am selling my car for: $25,000.00."})
	public void testAddingAmountLineToFile(String key) throws IOException {
		
		ArrayList<String> amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		// First check ArrayList does not contain values before adding them to file
		Assert.assertFalse(amountList.contains(key));
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new amount is added to arrayList
		amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		
		// parse amount from key and format
		String amountKey = key.split(": ")[1].split("\\s+")[0];		
		// make sure last char is digit
		while(!Character.isDigit(amountKey.charAt(amountKey.length()-1))) {
			amountKey = amountKey.substring(0, amountKey.length()-1);
		}

		// always should come out to line 156 in the file
		Assert.assertTrue(amountList.contains(amountKey + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
		
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"This line does not have an amount.", "Just testing my method :) "})
	public void testAddingRandomStringsAmount(String key) throws IOException {
		
		ArrayList<String> amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new date is not added to arrayList
		amountList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		Assert.assertFalse(amountList.contains(key + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
	}
	
	/*
	 * Numeric Testing
	 */
	@Test
	public void testNumericArrayListNotNull() throws IOException {
		Assert.assertNotNull(DocumentParser.extractAllNumericValues(DocumentParser.FILENAME));
	}
	
	@Test
	public void testNumericArrayListContainsNumbers() throws IOException {
	
		ArrayList<String> numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
		for(int i=0; i<numericList.size(); i++) {
			
			// check if numericList has numbers
			String data = numericList.get(i);		
			// check if data has multiple numbers
			if(data.contains(",")) {
				data = data.split(",")[0];
				Assert.assertTrue(common.isNumeric(data));
			} else {
				data = data.split(" @")[0];
				Assert.assertTrue(common.isNumeric(data));
			}
		}
			
	}
	
	@Test
	public void testNumericArrayListContainsLineNumbers() throws IOException {
		
		ArrayList<String> numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
		for(int i=0; i<numericList.size(); i++) {
			
			String data = numericList.get(i);
			// first check if all data have @ symbol
			Assert.assertTrue(data.contains("@"));
			data = data.split("@ ")[1];
			// check if data has LINE:
			Assert.assertTrue(data.contains("LINE:"));
			// check if data has line number
			data = data.split(":")[1];
			Assert.assertTrue(common.isNumeric(data));
		}
	}
	
	// test adding more numeric single lines
	@ParameterizedTest
	@ValueSource(strings = {"Adding number 123 to test", "1234567 but this part is a string", "1", "123thisisstring"})
	public void testAddingSingleNumericLineToFile(String key) throws IOException {
			
			ArrayList<String> numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
			// First check ArrayList does not contain values before adding them to file
			Assert.assertFalse(numericList.contains(key));
			// append to last line parameter
			Writer output;
			output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
			output.append(key);
			output.close();
			
			// test new numeric is added to arrayList
			numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
			
			// parse numeric from key and format
			String numericKey = key.replaceAll("[^?0-9]+", "");
			// always should come out to line 156 in the file
			Assert.assertTrue(numericList.contains(numericKey + " @ LINE:156"));
			// remove lastLine to undo
			common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
			
		}
	
	// test adding more number multilines 
	@ParameterizedTest
	@ValueSource(strings = {"Adding number 123 and 12345 to test", "adding 123, 12345, 123456", "test123 and test1234 and test12345"})
	public void testAddingMulitNumericLineToFile(String key) throws IOException {
		
		ArrayList<String> numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
		// First check ArrayList does not contain values before adding them to file
		Assert.assertFalse(numericList.contains(key));
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new amount is added to arrayList
		numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
		
		// parse numeric from key and format
		String numericKey = key.replaceAll("[^?0-9]+", ",");
		
		// format numericKey
		if(numericKey.startsWith(",")) {
			numericKey = numericKey.substring(1);
		}
		if(numericKey.endsWith(",")) {
			numericKey = numericKey.substring(0, numericKey.length()-1);
		}
		// always should come out to line 156 in the file
		Assert.assertTrue(numericList.contains(numericKey + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
	}
		
	@ParameterizedTest
	@ValueSource(strings = {"This line does not have a number.", "Just testing my method :) "})
	public void testAddingRandomStringsNumeric(String key) throws IOException {
		
		ArrayList<String> numericList = DocumentParser.extractAllNumericValues(DocumentParser.FILENAME);
		// append to last line parameter
		Writer output;
		output = new BufferedWriter(new FileWriter(DocumentParser.FILENAME, true));
		output.append(key);
		output.close();
		
		// test new date is not added to arrayList
		numericList = DocumentParser.extractAllAmounts(DocumentParser.FILENAME);
		Assert.assertFalse(numericList.contains(key + " @ LINE:156"));
		// remove lastLine to undo
		common.deleteLastLineInFile(DocumentParser.FILENAME, key.length());
	}
}
