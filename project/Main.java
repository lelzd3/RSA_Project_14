ìpackage project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;

public class Main {

	public static int precision = 1000;
	public static int numberOfThreads = 4;
	public static BigDecimal e = BigDecimal.ZERO;
	public static boolean isQuiet = false;
	public static String fileName = "result.txt";

	public static PrintWriter fileWriter;
	
	public static void main(String[] args) throws FileNotFoundException {		
		//start of program
		long t1 = Calendar.getInstance().getTimeInMillis();
		
		//change values based on what is entered on the argument line
		for( int i = 0; i < args.length; i++ ) {
			
			switch(args[i]) {
			
			case "-p":
				Main.precision = Integer.parseInt(args[i+1]);
				break;
			case "-t":
				Main.numberOfThreads = Integer.parseInt(args[i+1]);
				break;
			case "-q":
				Main.isQuiet = true;
				break;
			case "-o":
				Main.fileName = args[i+1];
				break;
			}	
		}
		
		//create file
		File file = new File(Main.fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error occured while creating a file");
			}
		}
		
		// creating writer with autoflush on
		Main.fileWriter = new PrintWriter(new FileOutputStream(file), true);
		
		CalculationThread[] threadPool = new CalculationThread[Main.numberOfThreads];
		
		for( int i = 0; i < threadPool.length; i++ ) {
			threadPool[i] = new CalculationThread("Thread " + (i+1), i);
			threadPool[i].start();
		}
		
		
		for( int i = 0; i < threadPool.length; i++ ) {
			try {
				threadPool[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		long t2 = Calendar.getInstance().getTimeInMillis();
		Main.fileWriter.println("Threads used in current run: " + Main.numberOfThreads);
		Main.fileWriter.println("Total execution time for current run (millis): " + (t2 - t1));
		Main.fileWriter.println("e = " + Main.e.toString());
	}
	
}
