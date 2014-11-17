package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
	private static int EUCLIDIAN_DISTANCE_MAX = 1000000;
	private static final int ACCEPTABLE_HASH_RANGE = 10000000;

	/**
	 * Converts MusicFile objects to SpectrogramImage objects, 
	 * and performs comparisons.
	 * 
	 * @param args MusicFiles to be compared
	 */
	public static void main(String[] args){
		// Checks that the given command line arguments are valid
		checkArgs(args);

		ArrayList<CanonicalFile> f1 = 
				createCanonicalFiles(args[0], args[1], temp1);
		ArrayList<CanonicalFile> f2 = createCanonicalFiles(args[2], 
				args[3], temp2);

		//Location of all fingerprints for
		HashMap<Integer,Fingerprint> h1 = 
				new HashMap<Integer,Fingerprint>();
		for(CanonicalFile cf : f1){
			Fingerprint[] fingerprints = cf.fingerprintFile();
			for(int i = 0; i < fingerprints.length; i++){
				h1.put(fingerprints[i].hashCode(), fingerprints[i]);
			}
		}

		System.out.println("Created Hashmap");

		for(CanonicalFile cf: f2){
			findMatches(h1, cf.fingerprintFile());
		}

		System.out.println("Found matches");

		System.exit(0);
	}

	private static void 
	findMatches(HashMap<Integer, Fingerprint> map,
			Fingerprint[] fingerprints){

		System.out.println("Number of fingerprints :" + fingerprints.length);
		System.out.println("Finding matches for " + fingerprints[0].getName());

		for(int i = 0; i < fingerprints.length; i++){
			Set<String> matches = new HashSet<String>();
			ArrayList<Fingerprint> validMatches = 
					scanMap(map, fingerprints[i].hashCode(), matches);
			if(validMatches == null)
				continue;
			for(Fingerprint match : validMatches){
				if(compareFingerprints(fingerprints[i], match)){
					if(chainCompare(fingerprints[i], match)){
						System.out.println("MATCH " +
								match.getName() + " " +  
								fingerprints[i].getName() 
								+ " " +
								Fingerprint.findTimeInFile(match) 
								+ " " +
								Fingerprint.findTimeInFile(
										fingerprints[i]));
						matches.add(match.getName());
					}
				}
			}
		}
	}

	private static ArrayList<Fingerprint> scanMap(HashMap<Integer, 
			Fingerprint> map, int hash, Set<String> prevMatches){
		ArrayList<Fingerprint> matches = new ArrayList<Fingerprint>();
		Fingerprint match;
		for(int i = Math.max(0, hash - ACCEPTABLE_HASH_RANGE); 
				i <= hash + ACCEPTABLE_HASH_RANGE; i++){
			if(map.containsKey(i)){
				match = map.get(i);
				if(!prevMatches.contains(match))
					matches.add(match);
			}
		}

		if(matches.size() == 0){
			return null;
		} else {
			return matches;
		}
	}

	private static boolean chainCompare(Fingerprint f1, 
			Fingerprint f2){
		Fingerprint currentF1 = f1;
		Fingerprint currentF2 = f2;

		for(int i = 1; i < 55; i++){
			currentF1 = currentF1.getNext();
			currentF2 = currentF2.getNext();

			if(currentF1 == null || currentF2 == null){
				return false;
			} else if(hashAcceptable(currentF1.hashCode(), 
					currentF2.hashCode())){
				if(compareFingerprints(currentF1, currentF2)){
					continue;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		return true;
	}

	private static boolean hashAcceptable(int h1, int h2){
		return h1 - h2 <= ACCEPTABLE_HASH_RANGE;
	}

	private static boolean compareFingerprints(Fingerprint f1,
			Fingerprint f2){
		return 
				EuclidianValue.getEuclidianValue(f1.getBands(), f2.getBands())
				<= EUCLIDIAN_DISTANCE_MAX;
	}


	private static ArrayList<CanonicalFile> createCanonicalFiles
	(String mode, String target, File dir){
		// Creates lists of MusicFiles 
		ArrayList<FileWrapper> list = 
				FilesCreator.makeMusicFileList(mode, target);

		// Debugs
		System.out.print("Files made:");
		System.out.println("-----f1-----");
		for(FileWrapper m: list){
			System.out.println(m.file.getName());
		}

		ArrayList<CanonicalFile> canonicalList = 
				new ArrayList<CanonicalFile>();

		for(FileWrapper fw: list){
			canonicalList.add(fw.convert(dir));
		}

		return canonicalList;

	}

	//	/**
	//	 * Helps compare SpectogramImage objects with different 
	//   * sampling rates
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
