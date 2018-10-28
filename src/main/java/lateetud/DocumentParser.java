package lateetud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DocumentParser {

	// define input file
	public final static String FILENAME = "inputFile.txt";
	
	static BufferedReader br = null;
	static FileReader fr = null;
	
	public static void main(String args[]) throws IOException {
		//System.out.println(extractAllDates(FILENAME));
		//System.out.println(extractAllAmounts(FILENAME));
		System.out.println(extractAllNumericValues(FILENAME));
	}
	
	/*
	 * Extract all dates with line numbers
	 */
	public static ArrayList<String> extractAllDates(String file) throws IOException {
		
		// define month list
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    ArrayList<String> months = new ArrayList<String>(Arrays.asList(monthNames));
	    
	    ArrayList<String> dateLines = new ArrayList<String>();
		String sCurrentLine = "";
		// get lineNumber
		int counter = 0;

		// set buffered reader and file reader
		fr = new FileReader(file);
		br = new BufferedReader(fr);
			
		while ((sCurrentLine = br.readLine()) != null) {
			counter++;
			// locate each month
			for(int i=0; i<months.size(); i++) {
				String monthFound = months.get(i);
				if(sCurrentLine.contains(monthFound)) {
				// format lines
				String formatDateLine = sCurrentLine.split(monthFound)[1];
				formatDateLine = formatDateLine.split("\\s+")[1] + formatDateLine.split("\\s+")[2];
						
				// add new formatted date to ArrayList
				dateLines.add(monthFound + " " + formatDateLine + " @ LINE:" + counter);
				}	
			} //for		
		} //while

		return dateLines;
	}
	
	/*
	 * Extract all $ amounts with line numbers
	 */
	public static ArrayList<String> extractAllAmounts(String file) throws IOException {

		ArrayList<String> amountLines = new ArrayList<String>();
		String sCurrentLine = "";
		// get lineNumber
		int counter = 0;

		// set buffered reader and file reader
		fr = new FileReader(file);
		br = new BufferedReader(fr);
			
		// initialize formatLine
		String formatLine = "";
		while ((sCurrentLine = br.readLine()) != null) {
			counter++;
			if(sCurrentLine.contains("$")) {
					
				// formatted to remove unecessary chars
				formatLine = sCurrentLine.split("\\$")[1];
				formatLine = formatLine.split("\\s+")[0];
					
				// remove any additional characters that are not numbers at the end
				while(!Character.isDigit(formatLine.charAt(formatLine.length()-1))) {
					formatLine = formatLine.substring(0, formatLine.length()-1);
				}
					
				amountLines.add("$" + formatLine + " @ LINE:" + counter);
			}
				
		} //while
		return amountLines;
	}
	
	/*
	 * Extract all numeric with line numbers
	 */
	public static ArrayList<String> extractAllNumericValues(String file) throws IOException {
		
		ArrayList<String> numericLines = new ArrayList<String>();
		String sCurrentLine = "";
		// get lineNumber
		int counter = 0;

		// set buffered reader and file reader
		fr = new FileReader(file);
		br = new BufferedReader(fr);
			
		// initialize formatLine
		String formatLine = "";
		while ((sCurrentLine = br.readLine()) != null) {
			counter++;
			// regex to find numeric value and formatting
			if(sCurrentLine.matches(".*\\d+.*")){
				formatLine = sCurrentLine.replaceAll("[^?0-9]+", ",");//.substring(1);
				if(formatLine.startsWith(",")) {
					formatLine = formatLine.substring(1);
				}
				if(formatLine.endsWith(",")) {
					formatLine = formatLine.substring(0, formatLine.length()-1);
				}
				numericLines.add(formatLine + " @ LINE:" + counter);
			}
				
		} //while
		return numericLines;
	}
	
}