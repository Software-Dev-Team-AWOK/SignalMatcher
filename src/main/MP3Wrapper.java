package main;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;

public class MP3Wrapper extends FileWrapper {

	public MP3Wrapper(File f){
		super(f);
	}

	@Override
	protected void checkTypeReqs(File file) {
		return;
	}

	@Override
	public CanonicalFile convert(File targetDirectory) {
		File newFile = new File(targetDirectory, 
				file.getName().concat(".wav"));
		LAME.convertMP3toCanonical(file.getAbsolutePath(),
				newFile.getAbsolutePath());
		return new CanonicalFile(file.getName(), newFile);
	}

}
