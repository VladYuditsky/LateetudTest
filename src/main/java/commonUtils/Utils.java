package commonUtils;

import java.io.RandomAccessFile;

public class Utils {

	// isNumeric method for checking lineNumbers
	public boolean isNumeric(String str) {    
		try {  		
			Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  	  
		return true;  
	}
	
	public void deleteLastLineInFile(String fileName, int lastLineNumChars) {
		try{
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            long length = raf.length();
            raf.setLength(length - lastLineNumChars);
            raf.close();
         } catch(Exception ex){
            ex.printStackTrace();
        }
	}
	
}
