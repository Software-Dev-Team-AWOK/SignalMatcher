package main;

import java.util.ArrayList;
import java.util.List;

public class LAME {
	private static final int CANONICAL_SAMPLING_RATE = 10240;
	private static final String CANONICAL_SR_STRING = Integer.toString(CANONICAL_SAMPLING_RATE);
	
	public static void convert(String infile, String outfile, List<String> opts){
		ProcessBuilder lamePB = new ProcessBuilder();
        ArrayList<String> lameargs = new ArrayList<String>();
				
		String lamePath = "/course/cs4500f14/bin/lame";
		lameargs.add(lamePath);
		lameargs.addAll(opts);
		lameargs.add(infile);
		lameargs.add(outfile);
		
		lamePB.command(lameargs);
		
		Process lameP = null;
		try{
			lameP = lamePB.start();
			lameP.waitFor();
		}catch (Exception e){
			System.out.println("ERROR: Error converting MP3 to WAV: "
					+ e.getMessage());
			System.exit(1);
		}
	}
	
	public static void convertMP3toCanonical(String targetFile, String newFile){
		ArrayList<String> args = new ArrayList<String>();
		args.add("-m");
		args.add("m");
		args.add("--silent");
		args.add("--decode");
		args.add("--resample");
		args.add(CANONICAL_SR_STRING);
		
		convert(targetFile, newFile, args);
	}
	
	public static void convertWAVtoCanonical(String targetFile, String newFile){
		ArrayList<String> args = new ArrayList<String>();
		args.add("-m");
		args.add("m");
		args.add("--silent");
		args.add("--resample");
		
		convert(targetFile, newFile, args);		
	}

}
