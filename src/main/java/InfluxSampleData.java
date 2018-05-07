import java.io.*;
import java.text.*;
import java.util.Random; 
import java.util.*;

public class InfluxSampleData {

	
	/**
	 * Generate a line protocol file for import into InfluxDB. 
	 * The file will simulate one day's worth of data for kdb comparison: 
	 *   
	 *   7 symbols, 500k entries each
	 *   
	 * @param args
	 */
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Generating line protocol data:");
        PrintWriter pw = new PrintWriter("InfluxData.lp","UTF-8");
        
        String symbols[] = { "EURUSD", "USDJPY", "GBPUSD", "USDCAD", "AUDUSD", "NZDUSD", "USDCHF" }; 
        
        pw.write("# DDL\n");
        pw.write("CREATE DATABASE dae\n");
        pw.write("CREATE RETENTION POLICY oneweek ON dae DURATION 1w REPLICATION 1\n");
        pw.write(" \n");
        
        pw.write("# DML\n");
        pw.write("# CONTEXT-DATABASE: dae\n");
        pw.write("# CONTEXT-RETENTION-POLICY: oneweek\n");
        pw.write(" \n");	
        
        Random r = new Random(); 
        
        // starting point: 
        DateFormat tsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date startDate = tsdf.parse("2018-05-04 00:00:00 GMT");
        long nextTs = (long) startDate.getTime()*1000000; // nanosecond precision 
        DateFormat daysdf = new SimpleDateFormat("yyyy-MM-dd");
         
        StringBuilder sb;
        for(int i=0;i<24*60*60*100;i++) { // 10mm values
        	nextTs = nextTs + r.nextInt(3)*1000000; // this needs to be in nanoseconds
        	sb = new StringBuilder(); 
        	sb.append("md,"); // measurement
        	sb.append("day=" + daysdf.format(nextTs/1000000) + ",sym=" + symbols[r.nextInt(symbols.length)] + " "); // tag values
        	sb.append("bpx1=1.2345,opx1=1.2346 "); // field values
        	sb.append(nextTs); // timestamp with nanosecond precision 
        	sb.append("\n");
        	pw.write(sb.toString());
        }
        
        pw.close(); 
        System.out.println("Done.");
		
	}

}
