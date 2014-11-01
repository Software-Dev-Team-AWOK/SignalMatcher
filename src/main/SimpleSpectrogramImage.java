package main;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//This class represents a spectrogram image of a audio file
//It describes the data of a file both in the frequency domain 
//as well as the time domain
public class SimpleSpectrogramImage {

	String name;
    double[] real_part; 
    double[] imag_part;
    double[] amplitudes;
    float samplingRate;
    double maxFreq;
    double deltaF;
    //TEST VALUE REMOVE AFTER DEBUGGING
    double[] test_amplitudes;
    //double[][] samples;
    double[] samples;
    final float ACCEPTABLEDIFFERENCE = (float)0.7;
    
    //Constructor, transforms data if transform variable is true
    SimpleSpectrogramImage(String name, double[] inputs, 
    						float samplingRate, boolean transform) {
    	this.name = name;
    	if(transform){
    		this.samples = inputs;
    		this.samplingRate = samplingRate;
    		this.maxFreq = samplingRate / 2.0;
    		this.deltaF = maxFreq/inputs.length;
    		real_part = new double[inputs.length];
    		imag_part = new double[inputs.length];
    		amplitudes = new double[inputs.length];
    		transform(this.samples,real_part,imag_part,amplitudes);
    	} else {
    		this.amplitudes = inputs;
    		this.samplingRate = samplingRate;
    	}

    }

   //This downsamples a file to a specific sampling rate by combining the 
    //various components of the frequency domain to appear as if it is 
    //of a lower sampling rate. 
    public SimpleSpectrogramImage downsample(float newSampleRate, int len){
    	//freq = sampling rate /2
    	//System.out.println("Downsampling");
    	//Nyquist frequency of new rate
    	double newMaxFrequency = newSampleRate / 2.0;
    	double newDeltaFreq = newMaxFrequency / (double)len;
    	
    	//the amplitudes to be returned
    	double[] newAmplitudes = new double[len];
    	
    	//Go through new array and combine elements of original array 
    	// to match length and change in frequency
    	double currentIndexMinFrequency = 0;
    	double currentIndexMaxFrequency;
    	for(int i = 0; i < len; i++){
    		currentIndexMaxFrequency = currentIndexMinFrequency 
    									+ newDeltaFreq;
    		int[] tofrom = getIndices(currentIndexMinFrequency, 
    								   currentIndexMaxFrequency);
    		double acc = 0;
    		for(int j = tofrom[0]; j < tofrom[1]; j++){
    			acc += amplitudes[j];
    		}
    		newAmplitudes[i] = acc;    			
    		currentIndexMinFrequency += newDeltaFreq;
    	}
    	
    	return new SimpleSpectrogramImage(this.name, 
    										newAmplitudes, 
    										newSampleRate, false);
    }
    
    //Finds the indices of the original array between which the requested
    //values are located
    public int[] getIndices(double fromFreq, double toFreq){
    	int[] tofrom = new int[2];
    	tofrom[0] = (int)Math.floor(fromFreq/deltaF);
    	tofrom[1] = (int)Math.ceil(toFreq/deltaF);
    	return tofrom;
    }			

    //Gets the number of samples
    public int getSampleLength(){
    	return this.samples.length;
    }
    
    //Compares this sectrogram to another and prints a match if they match
    public void compareSpectrogram(SimpleSpectrogramImage s){
    	boolean comp = this.avgDifferenceInAmplitudes(s);
    	if(comp){
    		System.out.println("MATCH " + this.name + " " +  s.name);
    	}
    }
    
    public boolean avgDifferenceInAmplitudes(SimpleSpectrogramImage s) {
        /*
         * Find average difference between the frequency 
         * domain images of these two signals.
         */
    	 //System.out.println(this.name + " " + s.name);
            if(s.amplitudes.length != this.amplitudes.length){
            	//System.out.println("Lengths are off");
            	return false;
            }
            
            float difference = 0;
            for(int i = 0; i < amplitudes.length; i++){
                difference += Math.abs(this.amplitudes[i] - s.amplitudes[i]); 
            }
            float avgDifference = difference / ((float) amplitudes.length);
        	//System.out.println(avgDifference);
        	//System.out.println(avgDifference <= ACCEPTABLEDIFFERENCE);

            return avgDifference <= ACCEPTABLEDIFFERENCE;
      }


    //returns the sampole rate
    public float getSamplingRate(){
    	return this.samplingRate;
    }
    /**
     * Javadocs description of code at 
     * http://www.developer.com/java/other/article.php/3380031/
     * 		Spectrum-Analysis-using-Java-Sampling-
     * 		Frequency-Folding-Frequency-and-the-FFT-Algorithm.htm
     * Used with Professor William Clinger's permission on Piazza post :
     * 
     */
    
    public static void transform(
            double[] data,
            double[] realOut,
            double[] imagOut,
            double[] magnitude){
    double pi = Math.PI;//for convenience
    int dataLen = data.length;
    //The complexToComplex FFT method does an
    // in-place transform causing the output
    // complex data to be stored in the arrays
    // containing the input complex data.
    // Therefore, it is necessary to copy the
    // input data to this method into the real
    // part of the complex data passed to the
    // complexToComplex method.
    System.arraycopy(data,0,realOut,0,dataLen);
    
    //Perform the spectral analysis.  The results
    // are stored in realOut and imagOut. The +1
    // causes it to be a forward transform. A -1
    // would cause it to be an inverse transform.
    complexToComplex(1,dataLen,realOut,imagOut);
    
    //Compute the magnitude and the phase angle
    // in degrees.
    for(int cnt = 0;cnt < dataLen;cnt++){
    magnitude[cnt] =
    (Math.sqrt(realOut[cnt]*realOut[cnt]
    + imagOut[cnt]*imagOut[cnt]))/dataLen;
    }

}//end transform method
//-------------------------------------------//

//This method computes a complex-to-complex
// FFT.  The value of sign must be 1 for a
// forward FFT.
    public static void complexToComplex(
                     int sign,
                     int len,
                     double real[],
                     double imag[]){
        double scale = 1.0;
        //Reorder the input data into reverse binary
        // order.
        int i,j;
        for (i=j=0; i < len; ++i) {
        if (j>=i) {
            double tempr = real[j]*scale;
            double tempi = imag[j]*scale;
            real[j] = real[i]*scale;
            imag[j] = imag[i]*scale;
            real[i] = tempr;
            imag[i] = tempi;
            }//end if
        int m = len/2;
        while (m>=1 && j>=m) {
            j -= m;
            m /= 2;
            }//end while loop
            j += m;
           }//end for loop
    
        //Input data has been reordered.
        int stage = 0;
        int maxSpectraForStage,stepSize;
        //Loop once for each stage in the spectral
        // recombination process.
        for(maxSpectraForStage = 1,
            stepSize = 2*maxSpectraForStage;
            maxSpectraForStage < len;
            maxSpectraForStage = stepSize,
            stepSize = 2*maxSpectraForStage){
                double deltaAngle =
                        sign*Math.PI/maxSpectraForStage;
                //Loop once for each individual spectra
                for (int spectraCnt = 0; spectraCnt < maxSpectraForStage;
                        ++spectraCnt){
                    double angle = spectraCnt*deltaAngle;
                    double realCorrection = Math.cos(angle);
                    double imagCorrection = Math.sin(angle);
                    int right = 0;
                    for (int left = spectraCnt;
                      left < len;left += stepSize){
                        right = left + maxSpectraForStage;
                        double tempReal =
                           realCorrection*real[right]
                           - imagCorrection*imag[right];
                        double tempImag =
                           realCorrection*imag[right]
                           + imagCorrection*real[right];
                        real[right] = real[left]-tempReal;
                        imag[right] = imag[left]-tempImag;
                        real[left] += tempReal;
                        imag[left] += tempImag;
                        }//end for loop
                    }//end for loop for individual spectra
                    maxSpectraForStage = stepSize;
                }//end for loop for stages
        }//end complexToComplex method
}