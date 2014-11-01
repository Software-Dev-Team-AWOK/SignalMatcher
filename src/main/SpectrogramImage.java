package main;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class SpectrogramImage {

    double[] real_part;
    double[] imag_part;
    double[][] raw_amplitudes;
    double[][] amplitudes;
    double[][] chunks;
    String name;
    
    final float ACCEPTABLEDIFFERENCE = (float)0.09;

    public void compareSpectrogram(SpectrogramImage s){
    	boolean comp = this.avgDifferenceInAmplitudesCheck(s);
    	if(comp){
    		System.out.println("MATCH " + this.name + " " +  s.name);
    	}
    }

    public boolean avgDifferenceInAmplitudesCheck(SpectrogramImage s) {
    /*
    * Find average difference between the frequency domain images of these two signals.
    */
    	//System.out.println(this.name + " " + s.name);
        float avgDifference = 0;
        float totalSize = (float) amplitudes.length*amplitudes[1].length;
        
        
            if(s.amplitudes.length != this.amplitudes.length) //Need additional check for 'width'
            	return false;
            float difference = 0;
            for(int i = 0; i < amplitudes.length; i++) {
                for(int j = 0; j<amplitudes[i].length; j++) {
                    difference += Math.abs
                            (this.amplitudes[i][j] - s.amplitudes[i][j]);
                }
            }
        avgDifference = difference / totalSize;
        if(avgDifference <= ACCEPTABLEDIFFERENCE)
        	System.out.println("MATCH "+avgDifference);
        //System.out.println(avgDifference);
        return avgDifference <= ACCEPTABLEDIFFERENCE;
}

    SpectrogramImage(String name, double[] samples) {

        
    	this.name = name;
        int samples_to_read = 2048;
        int scale_divisor = 1;//samples.length/samples_to_read;
        real_part = new double[samples_to_read];
        imag_part = new double[samples_to_read];
        int samplesread = 0;
        chunks = new double[(samples.length/(samples_to_read))+1][];
        amplitudes = new double[(samples.length/(samples_to_read))+1][];
        raw_amplitudes = new double[(samples.length/(samples_to_read))+1][];
        /*
         * Loop Invariant:
         * 0<=i<chunks.length
         * 
         */
        for(int i = 0; i<chunks.length; i++) {
                chunks[i] = new double[samples_to_read];
                raw_amplitudes[i] = new double[samples_to_read];
                amplitudes[i] = new double[samples_to_read];  
        }
        /*
         * Loop Invariant:
         * 
         * 
         */
        for(int bin = 0; bin < chunks.length; bin++) {
        	int count;
        	for(count = 0; 
        	    count<chunks[bin].length||count+samplesread > samples.length;
        	    count++)
        		chunks[bin][count] = samples[count+samplesread];

        	for(; count< chunks[bin].length; count++)
        		chunks[bin][count] = 0;
        		
        }
        

        for(int i = 0; i<samples_to_read; i++) {
     	   real_part[i] = 0;
     	   imag_part[i] = 0;
        }
     	   
        //note: define invariants for every for loop.
        //used for Infinity/NaN checking.
        //boolean breakflag = false;
        for(int m = 0; m<chunks.length; m++) {
        	//amplitudes[m] = fft(chunks[m], imag_part, true);
           transform(this.chunks[m],real_part,imag_part,raw_amplitudes[m]);
           amplitudes[m] = Arrays.copyOfRange(raw_amplitudes[m],
                                               0, 
                                               samples_to_read/2);
           for(int i = 0; i<samples_to_read; i++) {
         	   real_part[i] = 0;
         	   imag_part[i] = 0;
            }

        }
        
        for(int i = 0; i<amplitudes.length; i++) {
            
        	for(int j = 0; j<amplitudes[i].length; j++)
            	//System.out.println(amplitudes[i][j]);
        		amplitudes[i][j] = amplitudes[i][j]/scale_divisor;
        }

        
       
        //transform(dats, real_part, imag_part, amplitudes);

        
    }
    

    /**
     * Javadocs description of code at 
     * "http://www.developer.com/java/other/article.php/3380031/
     * Spectrum-Analysis-using-Java-Sampling-Frequency-Folding-
     * Frequency-and-the-FFT-Algorithm.htm"
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
