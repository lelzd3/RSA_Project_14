package project;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

public class CalculationThread extends Thread {
	private static int scale = 50;
	
	private String threadName;
	private int threadId;
	
	public CalculationThread(String threadName, int threadId) {
		this.threadName = threadName;
		this.threadId = threadId;
	}
	
	private static BigInteger factorial(int index) {
		BigInteger factorial = BigInteger.ONE;
		for( int i = 1; i <= index; i++  ) {
			factorial = factorial.multiply(BigInteger.valueOf(i));
		}
		return factorial;
	}
	
	
	
	@Override
	public void run() {
		long t1 = Calendar.getInstance().getTimeInMillis();
		
		if(!Main.isQuiet) {
			Main.fileWriter.println(this.threadName + " started");
		}
		
		for( int i = this.threadId; i <= Main.precision; i += Main.numberOfThreads) {
			BigDecimal numerator = new BigDecimal(3*i*3*i + 1);
			BigDecimal denominator = new BigDecimal(factorial(3*i));
			BigDecimal sum = (numerator).divide(denominator, CalculationThread.scale, BigDecimal.ROUND_HALF_UP);
			
			synchronized(Main.e) {
				Main.e = Main.e.add(sum);
			}
			
			
		}
		
		long t2 = Calendar.getInstance().getTimeInMillis();
		if(!Main.isQuiet) {
			Main.fileWriter.println(this.threadName + " stopped");
			Main.fileWriter.println(this.threadName + " execution time was (millis):" + (t2 - t1));
		}
	}
}
