package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LAME {
	private static final int CANONICAL_SAMPLING_RATE = 11025;
	private static final String CANONICAL_SR_STRING =
			Integer.toString(CANONICAL_SAMPLING_RATE);

	//Runs lame on infile and outfile with opts as arguments
	public static void convert(String infile,
			String outfile, List<String> opts){
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
	//Converts an MP3 to a wav at the canonical sample rate
	public static void convertMP3toCanonical(String targetFile,
			String newFile){
		ArrayList<String> args = new ArrayList<String>();
		args.add("--silent");
		args.add("--resample");
		args.add(CANONICAL_SR_STRING);

		File dir = new File(newFile);
		File intermed = new File(dir.getParentFile(), 
				"intermediate-file-transform.mp3");

		convert(targetFile, intermed.getAbsolutePath(), args);

		ArrayList<String> args2 = new ArrayList<String>();
		args2.add("--silent");
		args2.add("--decode");
		convert(intermed.getAbsolutePath(), newFile, args2);

	}
	//Converts a wav to an MP3 and then back to a WAV at the 
	//canonical sample rate
	public static void convertWAVtoCanonical(String targetFile, 
			String newFile){
		ArrayList<String> args = new ArrayList<String>();
		args.add("--silent");

		File inputFile = new File(targetFile);
		File createdFile = new File(newFile);
		File mp3MediaryDir = createdFile.getParentFile();
		File mp3Mediary = new File(mp3MediaryDir,
				inputFile.getName() +"-mp31.mp3");
		convert(targetFile, mp3Mediary.getAbsolutePath(), args);

		convertMP3toCanonical(mp3Mediary.getAbsolutePath(),
				newFile);
	}

}
