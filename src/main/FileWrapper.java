package main;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

//Represents an input file and converts the file to the canonical form
public abstract class FileWrapper {
	public File file;
	//Constructor
	public FileWrapper(File f){
		checkExistsAndIsFile(f);
		checkTypeReqs(f);
		this.file = f;
	}

	//Makes sure the file both exists and actually is a file
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

	//Checks requirements for specific file types
	protected abstract void checkTypeReqs(File file);

	//Converts this file to be of the canonical file type
	public abstract CanonicalFile convert(File targetDirectory);

}
