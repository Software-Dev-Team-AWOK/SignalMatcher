package main;

import java.util.ArrayList;

//This is our base class that is launched by the script. 
//It reads in the files to MusicFile objects,
//Converts them to SpectrogramImage objects,
// and performs comparisons.
public class SignalMatcher {
	//Main method. Does all the things in the class description.
	public static void main(String[] args){
		//Check arguments are correct
		checkArgs(args);
		
		//Create lists of MusicFiles 
		ArrayList<MusicFile> l1 = 
				FilesCreator.makeMusicFileList(args[0], args[1]);
		ArrayList<MusicFile> l2 = 
				FilesCreator.makeMusicFileList(args[2], args[3]);

		//debugging
		System.out.print("Files made:");
		System.out.println("-----f1-----");
		for(MusicFile m:l1){
			System.out.println(m.getName());
		}
		System.out.println("-----f2-----");
		for(MusicFile m:l2){
			System.out.println(m.getName());
		}

		//converts music files to spectrograms
		ArrayList<SimpleSpectrogramImage> s1 = 
				new ArrayList<SimpleSpectrogramImage>(l1.size());
		for(MusicFile m: l1){
			s1.add(new SimpleSpectrogramImage(m.getName(), 
												m.getSamples(), 
												m.getSamplingRate(), true));
			 
			//s1.add(new SpectrogramImage(m.getName(), m.getSamples()));
		}

		System.out.println("Spectrogram1 made");

		ArrayList<SimpleSpectrogramImage> s2 = 
				new ArrayList<SimpleSpectrogramImage>(l2.size());
		for(MusicFile m: l2){
			//s2.add(new SpectrogramImage(m.getName(), m.getSamples()));
			s2.add(new SimpleSpectrogramImage(m.getName(), 
												m.getSamples(),
												 m.getSamplingRate(),true));
			
		}

		System.out.println("Spectrogram 2 made");

		//Compare all spectrograms
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

	//Helper method for comparing files of different sampling rates.
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

	//Checks that the command line arguments are correct
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
