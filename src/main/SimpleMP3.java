package main;

import java.io.File;
import java.util.ArrayList;

public class SimpleMP3 extends MusicFile{

	private SimpleWav convertedWav;
	
	public SimpleMP3(String filepath){
		super(filepath);
		convertedWav = convertToWav(this.file);
	}
	
	public SimpleMP3(File file){
		super(file);
		convertedWav = convertToWav(file);
	}
	
	private SimpleWav convertToWav(File file){
		File convertedFile = new File("convertedWavs/"+this.file.getName().replace(".mp3", ".wav"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("--decode");
		LAME.convert(this.file.getAbsolutePath(), convertedFile.getAbsolutePath(), args);
		
		return new SimpleWav(convertedFile);
	}
	
	@Override
	public void makeSamples() {
		convertedWav.makeSamples();
		samples = convertedWav.getSamples();		
	}
	
	public float getSamplingRate(){
		return convertedWav.getSamplingRate();
	}
}
