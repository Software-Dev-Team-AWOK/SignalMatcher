package main;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class SimpleWav extends MusicFile {
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
		
		SimpleWav(){}
		
		SimpleWav(String path) {
			super(path);
			initializeFields();
		}
		
		SimpleWav(File file){
			super(file);
			initializeFields();
		}
		
		private void initializeFields() {
			try{
				format = AudioSystem.getAudioFileFormat(file);
				type = format.getType();
				audioFormat =  format.getFormat();
				isCDQuality();
			} catch (Exception e) {
				System.out.println("Error: " + this.file.getName() + " is not a supported format");
				System.exit(1);
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
		private void makeMonoPCM() {
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
		public void makeSamples() {
			readFile();
			
		    byte[] sampleBytes;
		    int halfMonoLen = theMonoPCM.length / 2;
		    int paddedLength = findNextBiggestPowerOfTwo(halfMonoLen);
		    System.out.println("HalfMonolen = " + halfMonoLen);
		    System.out.println("Padded = " + paddedLength );
		    samples = new double[paddedLength];

		    //long preSample;
		    //double sample;
		    int preSample;
		    int sample;
		    int counter = 0;
		    for(int i = 0; i < theMonoPCM.length; i+=2) {
		        ByteBuffer bb = ByteBuffer.allocate(2);
		        bb.order(ByteOrder.LITTLE_ENDIAN);
		        bb.put(theMonoPCM[i]);
		        bb.put(theMonoPCM[i+1]);
		        
		        samples[i/2] = (double)bb.getShort(0);
		        counter = i / 2;
		    }
		    for(int i = counter; i < paddedLength; i++){
		    	samples[i] = 0;
		    }
		}
		
		private int findNextBiggestPowerOfTwo(int n){
			int powerOfTwo = 1;
			while(powerOfTwo < n){
				powerOfTwo = powerOfTwo << 1;
			}
			return powerOfTwo;
		}
}

		
