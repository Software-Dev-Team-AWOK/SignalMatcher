package main;

import java.io.File;
import java.util.ArrayList;
//A class to represent MP3 files
// When an mp3 object is created, it is immediately
// converted to a WAV file. The methods of this object
// mostly delegate to the methods of the created WAV file
public class SimpleMP3 extends MusicFile{
	//Converted WAV
	private SimpleWav convertedWav;
	
	//Constructors
	public SimpleMP3(String filepath){
		super(filepath);
		convertedWav = convertToWav(this.file);
	}
	
	public SimpleMP3(File file){
		super(file);
		convertedWav = convertToWav(file);
	}
	
	//Convert the given MP3 file to a WAV
	private SimpleWav convertToWav(File file){
		File convertedFile = new File("convertedWavs/"+
									   this.file.getName()
									   			.replace(".mp3", ".wav"));
		
		ArrayList<String> args = new ArrayList<String>();
		args.add("--decode");
		args.add("--silent");
		
		//System.out.println("Converting "+ file.getName());
		
		LAME.convert(this.file.getAbsolutePath(), 
					 convertedFile.getAbsolutePath(), args);
		System.out.println("done");
		return new SimpleWav(convertedFile);
	}
	
	//From MusicFile, make samples from the converted WAV
	@Override
	public void makeSamples() {
		convertedWav.makeSamples();
		samples = convertedWav.getSamples();		
	}
	
	//Returns the converted wav's sample rate
	public float getSamplingRate(){
		return convertedWav.getSamplingRate();
	}
}
