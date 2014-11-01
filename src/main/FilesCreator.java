package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


//This is a static helper class to create ArrayLists of MusicFiles
//from the input to the dan program
public class FilesCreator {
	//given a mode (-f|-d) and an inout (relative or absolute path)
	//returns a list of MuicFiles from the file/files specified
	public static ArrayList<MusicFile> makeMusicFileList
										(String mode, String input){
		File inFile = new File(input);
		checkExistsAndReadable(inFile);
		
		if(mode.equals("-f")){
			return makeSingleFileList(inFile);
		} else if (mode.equals("-d")){
			return makeDirectoryList(inFile);
		} else {
			System.out.println("ERROR: Incorrect command line arguments");
			System.exit(1);
			return null;
		}
		
	}
	
	//Checks to make sure that the file/directory specified 
	//both exists and is readable
	public static void checkExistsAndReadable(File inFile){
		if(!inFile.exists()){
			System.out.println("ERROR: "+ 
					inFile.getName() + " does not exist");
			System.exit(1);
		} else if (!inFile.canRead()){
			System.out.println("ERROR: " + 
					inFile.getName() + " can not be read");
			System.exit(1);
		}
	}
	
	//Given a file, returns an ArrayList containing a 
	//single MusicFile, representing the input file
	public static ArrayList<MusicFile> makeSingleFileList(File file){
		ArrayList<MusicFile> result = new ArrayList<MusicFile>();
		result.add(makeMusicFile(file));
		return result;		
	}
	
	//Given a directory, returns an ArrayList containing MusicFiles 
	//representing the files inside the given directory
	public static ArrayList<MusicFile> makeDirectoryList(File dir){
				
		if (!dir.isDirectory()) {
			System.out.println("ERROR: "+ 
					dir.getName() + " is not a directory");
			System.exit(1);
		} 
		
		File[] files = dir.listFiles();
		if (files.length == 0){
			// One directory is empty, there are no comparisons 
			// to do, so we can immediately exit
			System.exit(0);
		}
				
		ArrayList<MusicFile> result = new ArrayList<MusicFile>();
		for(File file : Arrays.asList(files)){
			result.add(makeMusicFile(file));
		}
		
		return result;
		
	}
	
	//Make a specific type of musicfile based on the
	//file extension
	public static MusicFile makeMusicFile(File file){
		String fileName = file.getName();
		MusicFile musicFile = null;
		if(fileName.endsWith(".wav")){
			musicFile = new SimpleWav(file.getAbsolutePath());
		} else if (fileName.endsWith(".mp3")){
			musicFile = new SimpleMP3(file.getAbsolutePath());
		} else {
			System.out.println("ERROR: "+ 
					file.getName() + " is not a supported format");
			System.exit(1);
		}
		
		return musicFile;
	}

}
