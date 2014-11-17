package main;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
public class WavWrapper extends FileWrapper {

	public WavWrapper(File f) {
		super(f);
	}

	// Is this WAVE file of acceptable quality?
	/* If a <pathname> preceded by the "-f" option ends in ".wav",
	   that file must be:
	  - in little-endian (RIFF) WAVE format
	  - PCM encoding (AudioFormat 1) ~~~
	  - stereo or mono
	  - 8- or 16-bit samples, 
	  - sampling rate of 11.025, 22.050, 44.100, or	48000 kHz.
	 */
	protected void checkTypeReqs(File file) {
		try{
			AudioFileFormat fileFormat = 
					AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();
			AudioFileFormat.Type type = fileFormat.getType();
			int sampleSize = format.getSampleSizeInBits();
			int channels = format.getChannels();
			float sampleRate = format.getSampleRate();

			if 	(!((!format.isBigEndian()) &&
					(channels == 1 || channels == 2) &&
					(sampleSize == 8 || sampleSize == 16) &&
					(sampleRate == 11025 || sampleRate == 22050 ||
					sampleRate == 44100 || sampleRate == 48000) &&
					type.toString().equals("WAVE")))  {
				System.err.println("Error: " +
						file.getName() + 
						" is not a supported format");
				System.exit(1);
			}
		} catch (Exception e){
			System.err.println("Error: " +
					file.getName() + 
					" is not a supported format");
			System.exit(1);
		}
	}

	@Override
	public CanonicalFile convert(File targetDirectory) {
		File newFile = new File(targetDirectory, file.getName());
		LAME.convertWAVtoCanonical(file.getAbsolutePath(),
				newFile.getAbsolutePath());
		return new CanonicalFile(file.getName(), newFile);
	}

}
