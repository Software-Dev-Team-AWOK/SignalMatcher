import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


import org.omg.CORBA.DoubleSeqHelper;
// Represents a given WAVE file
public class CopyOfWav {
	// File Path
	private String path;
	// This WAVE file
	private File file;
	// This WAVE file's type, length in bytes, 
	// length in sample frames, and format
	private AudioFileFormat format;
	// This WAVE file's type
	private AudioFileFormat.Type type;
	// The arrangement of data in this WAVE file
	private AudioFormat audioFormat;
	// The sample size of this WAVE file
	private int sampleSize;
	// The number of channels in this WAVE file
	private int channels;
	// The sample rate of this WAVE file
	private float sampleRate;
	// The PCM data (bytes) from this WAVE file
	private byte[] thePCM;
	// The PCM data (bytes) from this WAVE file with only 1 channel
	private byte[] theMonoPCM;
	// The PCM data samples (floats) from this WAVE file with only 1 channel
	//private int[] samples;
	// The PCM data samples (floats) from this WAVE file with only 1 channel
	//private short[] samples;
    // The PCM data samples (floats) from this WAVE file with only 1 channel
	private double[] samples;
	// Constructs a WAVE file, and throws an exception
	// if the given audio file is not of type WAVE
	CopyOfWav(String path) throws Exception {
		this.file = new File(path);
		this.path = path;
		if (!this.isWavFile() || !this.isCDQuality()) {
			throw new Exception("ERROR: " + file.getName() + 
					" is not a supported format");
		}
		else {
			readFile();
			makeMono();
			makeSamples();
		}
	}

	// Is the given audio file a WAVE file?
	// *This method also sets the format, time, and audioFormat
	// of this WAVE file*
	private boolean isWavFile() {
		try {
			format = AudioSystem.getAudioFileFormat(file);
			type = format.getType();
			audioFormat =  format.getFormat();	
			return type.toString().equals("WAVE");
		}
		catch(Exception e) {
			return false;
		}
	}

	// Is this WAVE file of CD quality?
	private boolean isCDQuality() {
		sampleSize = audioFormat.getSampleSizeInBits();
		channels = audioFormat.getChannels();
		sampleRate = audioFormat.getSampleRate();
		return sampleSize == 16 &&
				channels == 2 &&
				sampleRate == 44100;
	}

	// Sets the PCM data from this WAVE file
	private void readFile() {
		try {
			AudioInputStream fileIn =  AudioSystem.getAudioInputStream(file);
			int subChunk2Size = (int) (fileIn.getFrameLength() * audioFormat.getChannels() * (audioFormat.getSampleSizeInBits()/8));
			byte[] pcm = new byte[subChunk2Size];
			fileIn.skip(44);
			fileIn.read(pcm);
			thePCM = pcm;
		} 
		catch(Exception e) {
			System.out.println(e);
		}
	}

	// Sets the PCM data from this WAVE file with only one channel
	private void makeMono() {
		int bitsPerSample = sampleSize / 8;
		theMonoPCM = new byte[thePCM.length/2];
		int monoPCMIndex = 0;
		for (int i = 0; i < thePCM.length; i++) {
			if (i%4 < 2) {
				theMonoPCM[monoPCMIndex] = thePCM[i];
				monoPCMIndex = monoPCMIndex + 1;
			}
		}
	}
	//clarify endianness
	// Reads the sample from the mono PCM data.
	private void makeSamples() {
	    byte[] sampleBytes;
	    samples = new double[theMonoPCM.length/2];

	    //long preSample;
	    //double sample;
	    int preSample;
	    int sample;
	    for(int i = 0; i < theMonoPCM.length; i+=2) {
	        ByteBuffer bb = ByteBuffer.allocate(2);
	        bb.order(ByteOrder.LITTLE_ENDIAN);
	        bb.put(theMonoPCM[i]);
	        bb.put(theMonoPCM[i+1]);
           

	        samples[i/2] = (double) bb.getShort(0); 
	        if(Double.isNaN(samples[i/2])) {
	            
	        }
	    }
	}
	// Gets the mono PCM data from this WAVE file
	public byte[] getMonoPCM() {
	    return theMonoPCM;
	}
	
	// Gets the float from this WAVE file
    public double[] getSamples() {
	//public double[] getSamples() {
        return samples;
    }
}
