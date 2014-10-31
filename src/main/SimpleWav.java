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
		
		//constructors
		SimpleWav(){}

		//path is the path to the WAV file
		SimpleWav(String path) {
			super(path);
			initializeFields();
		}
		
		//file is the File representation of the WAV
		SimpleWav(File file){
			super(file);
			initializeFields();
		}
		
		//Initializes important fields
		private void initializeFields() {
			try{
				format = AudioSystem.getAudioFileFormat(file);
				type = format.getType();
				audioFormat =  format.getFormat();
				if(!isAcceptableQuality()){
					System.out.println("Error: " + this.file.getName() + " is not a supported format");
					System.exit(1);
				}
			} catch (Exception e) {
				System.out.println("Error: " + this.file.getName() + " is not a supported format");
				System.exit(1);
			}			
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
		private boolean isAcceptableQuality() {
			sampleSize = audioFormat.getSampleSizeInBits();
			channels = audioFormat.getChannels();
			sampleRate = audioFormat.getSampleRate();
			return 	(!audioFormat.isBigEndian()) &&
					(channels == 1 || channels == 2) &&
					(sampleSize == 8 || sampleSize == 16) &&
					(sampleRate == 11025 || sampleRate == 22050 ||
					 sampleRate == 44100 || sampleRate == 48000);
					
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
			if(audioFormat.getChannels() == 1){
				theMonoPCM = thePCM;
				return;
			} else {
				theMonoPCM = new byte[thePCM.length/2];
				int bytesPerSample = audioFormat.getSampleSizeInBits()/8;
				int bytesPerFrame = bytesPerSample * 2;
				int monoPCMIndex = 0;
				for (int i = 0; i < thePCM.length; i++) {
					if (i%bytesPerFrame < bytesPerSample) {
						theMonoPCM[monoPCMIndex] = thePCM[i];
						monoPCMIndex = monoPCMIndex + 1;
					}
				}
			}
		}
		//clarify endianness
		// Reads the sample from the mono PCM data.
		public void makeSamples() {
			readFile();
			makeMonoPCM();
			int bytesPerSample = audioFormat.getSampleSizeInBits() / 8;
		    int sampleArrayLength = theMonoPCM.length / bytesPerSample;
		    int paddedLength = findNextBiggestPowerOfTwo(sampleArrayLength);
		    samples = new double[paddedLength];

		    //long preSample;
		    //double sample;
		    int counter = 0;
		    for(int i = 0; i < theMonoPCM.length; i+=bytesPerSample) {
		    	double doubleSample = bytesToDouble(theMonoPCM, i, bytesPerSample);
		        samples[i/bytesPerSample] = doubleSample;
		        counter = i / bytesPerSample;
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
		
		private double bytesToDouble(byte[] arr, int startIndex, int len){
			 ByteBuffer bb = ByteBuffer.allocate(len);
		     bb.order(ByteOrder.LITTLE_ENDIAN);
		     for(int i = 0; i < len; i++){
		    	 bb.put(arr[startIndex+i]);
		     }
		     
		     return (double)bb.getShort(0);   	 
		}
		
		public float getSamplingRate(){
			return sampleRate;
		}
}

		
