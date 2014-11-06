package main;

import java.io.File;
/**
 * Abstract class to represent all music files
 * @author Ariel Winton, Jansen Kantor, Nnamdi Okeke, Rani Aljondi
 *
 */
public abstract class MusicFile {
	/**
	 * A file
	 */
	public File file;
	/**
	 * A sample array made from the raw data of the file
	 */
	protected double[] samples;
	/**
	 * Creates an instance of a MusicFile
	 */
	public MusicFile(){}
	/**
	 * Creates an instance of a MusicFile with a file
	 * created using the given path
	 * 
	 * Sets samples to null
	 * 
	 * @param path Path to a file
	 */
	public MusicFile(String path){
		file = new File(path);
		samples = null;
		checkExistsAndIsFile();
	}

	/**
	 * Sets file to the given file
	 * 
	 * Creates an instance of a MusicFile using the given file
	 * 
	 * @param file A file
	 */
	public MusicFile(File file){
		this.file = file;
		samples = null;
		checkExistsAndIsFile();
	}

	/**
	 * Checks that file exists and is readable
	 */
	private void checkExistsAndIsFile(){
		if(!file.exists()){
			System.out.println("ERROR: " + 
					file.getName() + 
					" does not exist");
			System.exit(1);
		} else if (!file.isFile()){
			System.out.println("ERROR: " + 
					file.getName() +
					" is not a file");
			System.exit(1);
		}
	}

	/**
	 * Creates an array of raw data samples from file
	 * 
	 * @return An array of raw data samples from file
	 */
	public double[] getSamples(){
		if(samples == null){
			makeSamples();
		} 
		return samples;
	}

	/**
	 * Gets the name of file
	 * 
	 * @return The name of file
	 */
	public String getName(){
		return file.getName();
	}

	/**
	 * Makes an array of raw data samples from file
	 */
	public abstract void makeSamples();

	/**
	 * Gets the sampling rate of file
	 * 
	 * @return The sampling rate of file
	 */
	public abstract float getSamplingRate();
}
