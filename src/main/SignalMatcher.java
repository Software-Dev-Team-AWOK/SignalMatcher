package main;

import java.util.ArrayList;
/**
 * Converts MusicFile objects to SpectrogramImage objects,
 *  and performs comparisons.
 * 
 * @author Ariel Winton, Jansen Kantor, Nnamdi Okeke, Rani Aljondi
 *
 */
public class SignalMatcher {
	/**
	 * Converts MusicFile objects to SpectrogramImage objects, 
	 * and performs comparisons.
	 * 
	 * @param args MusicFiles to be compared
	 */
	public static void main(String[] args){
		// Checks that the given command line arguments are valid
		checkArgs(args);

		// Creates lists of MusicFiles 
		ArrayList<MusicFile> l1 = 
				FilesCreator.makeMusicFileList(args[0], args[1]);
		ArrayList<MusicFile> l2 = 
				FilesCreator.makeMusicFileList(args[2], args[3]);

		// Debugs
		System.out.print("Files made:");
		System.out.println("-----f1-----");
		for(MusicFile m:l1){
			System.out.println(m.getName());
		}
		System.out.println("-----f2-----");
		for(MusicFile m:l2){
			System.out.println(m.getName());
		}

		// Converts MusicFile objects to SpectrogramImage objects
		ArrayList<SimpleSpectrogramImage> s1 = 
				new ArrayList<SimpleSpectrogramImage>(l1.size());
		for(MusicFile m: l1){
			s1.add(new SimpleSpectrogramImage(m.getName(), 
					m.getSamples(), 
					m.getSamplingRate(), true));
		}

		System.out.println("Spectrogram1 made");

		ArrayList<SimpleSpectrogramImage> s2 = 
				new ArrayList<SimpleSpectrogramImage>(l2.size());
		for(MusicFile m: l2){
			s2.add(new SimpleSpectrogramImage(m.getName(), 
					m.getSamples(),
					m.getSamplingRate(),true));

		}

		System.out.println("Spectrogram 2 made");

		// Compares SpectrogramImage objects to one another
		for(SimpleSpectrogramImage spectro1: s1){
			for (SimpleSpectrogramImage spectro2: s2){
				if(spectro1.getSamplingRate() > spectro2.getSamplingRate()){
					compareDiffSamplingRates(spectro2, spectro1, false);
				} else if(spectro1.getSamplingRate() < 
						spectro2.getSamplingRate()) {
					compareDiffSamplingRates(spectro1, spectro2, true);
				} else {
					spectro1.compareSpectrogram(spectro2);				
				}
			}
		}

		System.exit(0);
	}

	/**
	 * Helps compare SpectogramImage objects with different sampling rates
	 * 
	 * @param smaller A SpectrogramImage
	 * @param larger A SpectrogramImage
	 * @param smallerFirst Is the smaller SpectrogramImage the 
	 * first argument?
	 */
	private static void compareDiffSamplingRates
	(SimpleSpectrogramImage smaller, SimpleSpectrogramImage larger, 
			boolean smallerFirst){
		if(smaller.getSampleLength() >= larger.getSampleLength()){
			return;
		} else {
			boolean pass = larger.downsample(smaller.getSamplingRate(), 
					smaller.getSampleLength())
					.avgDifferenceInAmplitudes(smaller);
			if(pass && smallerFirst){
				System.out.println("MATCH " + 
						smaller.name + " " + larger.name);
			} else if (pass && !smallerFirst){
				System.out.println("MATCH " + 
						larger.name + " " + smaller.name);
			}
		}
	}

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
