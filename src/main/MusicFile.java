package main;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public abstract class MusicFile {
		// This WAVE file
		public File file;
		// The samples 
		protected double[] samples;
		
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
		
		private void checkExistsAndIsFile(){
			if(!file.exists()){
				System.out.println("ERROR: " + file.getName() + " does not exist");
				System.exit(1);
			} else if (!file.isFile()){
				System.out.println("ERROR: " + file.getName() + " is not a file");
				System.exit(1);
			}
		}
		
		public double[] getSamples(){
			if(samples == null){
				makeSamples();
			} 
			return samples;
		}
		
		public abstract void makeSamples();
		
		public abstract float getSamplingRate();
		

}
