package main;

import java.util.ArrayList;
import java.util.List;
/**
 * Used to convert MP3 files to WAV
 * @author Ariel Winton, Jansen Kantor, Nnamdi Okeke, Rani Aljondi
 *
 */
public class LAME {
	/**
	 * 
	 * Uses a ProcessBuilder to launch a LAME conversion
	 * from the given infile to a WAV file with the given
	 * outfile name 
	 * 
	 * @param infile A path to a file to be converted
	 * @param outfile A name for the converted output file
	 * @param opts Format options for the output WAV file
	 */
	public static void convert(String infile, 
			String outfile, 
			List<String> opts){
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

}
