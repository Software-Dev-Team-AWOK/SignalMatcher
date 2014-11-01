package main;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
//Abstract class to represtent all music files.
public abstract class MusicFile {
		public File file;
		protected double[] samples;
		//constructors
		public MusicFile(){}
		
		public MusicFile(String path){
			file = new File(path);
			samples = null;
			checkExistsAndIsFile();
		}
		
		public MusicFile(File file){
			this.file = file;
			samples = null;
			checkExistsAndIsFile();
		}
		//Checks that the given file both exists and is readble.
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
		
		//Returns the samples from this music file
		// creating them if they have not already been created
		public double[] getSamples(){
			if(samples == null){
				makeSamples();
			} 
			return samples;
		}
		//returns the name of the music file
		public String getName(){
			return file.getName();
		}
		
		//Create the samples array from the raw data of the file
		public abstract void makeSamples();
		
		//Get the sampling rate of the file.
		public abstract float getSamplingRate();
}
