package main;

import java.io.File;
import java.util.ArrayList;

/**
 * A MusicFile that represents an MP3 file
 * 
 * When an instance is created, it is converted to a SimpleWav object
 * 
 * @author Ariel Winton, Jansen Kantor, Nnamdi Okeke, Rani Aljondi
 *
 */
public class SimpleMP3 extends MusicFile{
	/**
	 * A SimpleWav version of this object
	 */
	private SimpleWav convertedWav;

	/**
	 * Creates an instance of SimpleMP3 with the given filepath
	 * and converts it into a SimpleWav object
	 * 
	 * @param filepath A file path
	 */
	public SimpleMP3(String filepath){
		super(filepath);
		convertedWav = convertToWav(this.file);
	}

	/**
	 * Creates an instance of SimpleMP3 with the given file
	 * and converts it into a SimpleWav object
	 * 
	 * Sets convertedWav to the SimpleWav object
	 * 
	 * @param file A file
	 */
	public SimpleMP3(File file){
		super(file);
		convertedWav = convertToWav(file);
	}

	/**
	 * Converts the given file to a SimpleWav object
	 * 
	 * @param file An MP3 file
	 * @return A SimpleWav object containing the given MP3 file
	 * converted to WAV format
	 */
	private SimpleWav convertToWav(File file){
		File convertedFile = new File("convertedWavs/"+
				this.file.getName()
				.replace(".mp3", ".wav"));

		ArrayList<String> args = new ArrayList<String>();
		args.add("--decode");
		args.add("--silent");

		LAME.convert(this.file.getAbsolutePath(), 
				convertedFile.getAbsolutePath(), args);
		System.out.println("done");
		return new SimpleWav(convertedFile);
	}

	// Inherited from MusicFile
	// Makes samples from convertedWav
	public void makeSamples() {
		convertedWav.makeSamples();
		samples = convertedWav.getSamples();		
	}

	// Inherited from MusicFile
	// Gets the sampling rate of convertedWav
	public float getSamplingRate(){
		return convertedWav.getSamplingRate();
	}
}
