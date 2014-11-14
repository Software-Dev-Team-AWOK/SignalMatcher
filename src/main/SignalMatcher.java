package main;

import java.io.File;
import java.util.ArrayList;
/**
 * Converts MusicFile objects to SpectrogramImage objects,
 *  and performs comparisons.
 * 
 * @author Ariel Winton, Jansen Kantor, Nnamdi Okeke, Rani Aljondi
 *
 */
public class SignalMatcher {
	private static File temp1 = new File("/tmp/SignalMatcher/D1");
	private static File temp2 = new File("/tmp/SignalMatcher/D2");
	private static int fileNumber = 0;

	/**
	 * Converts MusicFile objects to SpectrogramImage objects, 
	 * and performs comparisons.
	 * 
	 * @param args MusicFiles to be compared
	 */
	public static void main(String[] args){
		// Checks that the given command line arguments are valid
		checkArgs(args);

		ArrayList<CanonicalFile> f1 = createCanonicalFiles(args[0], args[1], temp1);
		ArrayList<CanonicalFile> f2 = createCanonicalFiles(args[2], args[3], temp2);
		
		f1.get(0).fingerprintFile();
		
		System.exit(0);
	}
	
	private static ArrayList<CanonicalFile> createCanonicalFiles(String mode, String target, File dir){
		// Creates lists of MusicFiles 
		ArrayList<FileWrapper> list = 
				FilesCreator.makeMusicFileList(mode, target);

		// Debugs
		System.out.print("Files made:");
		System.out.println("-----f1-----");
		for(FileWrapper m: list){
			System.out.println(m.file.getName());
		}
		
		ArrayList<CanonicalFile> canonicalList = new ArrayList<CanonicalFile>();
		
		for(FileWrapper fw: list){
			canonicalList.add(fw.convert(dir));
		}
		
		return canonicalList;
		
	}

//	/**
//	 * Helps compare SpectogramImage objects with different sampling rates
//	 * 
//	 * @param smaller A SpectrogramImage
//	 * @param larger A SpectrogramImage
//	 * @param smallerFirst Is the smaller SpectrogramImage the 
//	 * first argument?
//	 */
//	private static void compareDiffSamplingRates
//	(SimpleSpectrogramImage smaller, SimpleSpectrogramImage larger, 
//			boolean smallerFirst){
//		if(smaller.getSampleLength() >= larger.getSampleLength()){
//			return;
//		} else {
//			boolean pass = larger.downsample(smaller.getSamplingRate(), 
//					smaller.getSampleLength())
//					.avgDifferenceInAmplitudes(smaller);
//			if(pass && smallerFirst){
//				System.out.println("MATCH " + 
//						smaller.name + " " + larger.name);
//			} else if (pass && !smallerFirst){
//				System.out.println("MATCH " + 
//						larger.name + " " + smaller.name);
//			}
//		}
//	}

	/**
	 * Checks that the given command line arguments are valid
	 * 
	 * @param args Command line arguments
	 */
	private static void checkArgs(String[] args){
		if(((args.length == 4) &&
				(args[0].equals("-f") || args[0].equals("-d")) &&
				(args[2].equals("-f") || args[2].equals("-d")))){
			//Everything is fine
			return;
		} else {
			System.out.println("ERROR: Incorrect command line arguments");
			System.exit(1);
		}
	}
}
