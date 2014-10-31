
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public abstract class MusicFile {
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
    private double[] samples;
    // Constructs a WAVE file, and throws an exception
    // if the given audio file is not of type WAVE

    // This Music file
    public File file;
    
    public MusicFile(){}
    
    public MusicFile(String path){
        file = new File(path);
        samples = null;
        checkExistsAndIsFile();
    }
    
    public MusicFile(File file){
        this.file = file;
        samples = null;
        checkExistsAndIsFile();
    }
    
    private void checkExistsAndIsFile(){
        if(!file.exists()){
            System.out.println("ERROR: " + file.getName() + " does not exist");
            System.exit(1);
        } else if (!file.isFile()){
            System.out.println("ERROR: " + file.getName() + " is not a file");
            System.exit(1);
        }
    }
    
    public double[] getSamples(){
        if(samples == null){
            makeSamples();
        } 
        return samples;
    }
    
    public abstract void makeSamples();
    

}
