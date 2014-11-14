package main;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;


public abstract class FileWrapper {
	public File file;
	public FileWrapper(File f){
		checkExistsAndIsFile(f);
	
		try { 
			AudioFileFormat format = AudioSystem.getAudioFileFormat(f);
			checkTypeReqs(f, format);
		} catch (Exception e) {
				System.err.println("Error: " +
									f.getName() + 
									" is not a supported format");
				System.exit(1);
		}	
		
		this.file = f;
	}
		
	private void checkExistsAndIsFile(File file){
		if(!file.exists()){
			System.err.println("ERROR: " + 
								file.getName() + 
								" does not exist");
			System.exit(1);
		} else if (!file.isFile()){
			System.err.println("ERROR: " + 
								file.getName() +
								" is not a file");
			System.exit(1);
		}
	}
	
	protected abstract void checkTypeReqs(File file, AudioFileFormat format);
	
	public abstract CanonicalFile convert(File targetDirectory);

}
