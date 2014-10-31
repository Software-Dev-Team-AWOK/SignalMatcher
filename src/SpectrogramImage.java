import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SpectrogramImage {

    double[] real_part;
    double[] imag_part;
    double[][] raw_amplitudes;
    double[][] amplitudes;
    double[][] chunks;
    final float ACCEPTABLEDIFFERENCE = (float)0.03;


    public boolean equals(Object o) {
    /*
    * Find average difference between the frequency domain images of these two signals.
    */
        float avgDifference = 0;
        float totalSize = (float) amplitudes.length*amplitudes[1].length;
        if(!(o instanceof SpectrogramImage)){
            return false;
        } 
        
        else {
            SpectrogramImage s = (SpectrogramImage) o;
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
        System.out.println(avgDifference);
        return avgDifference <= ACCEPTABLEDIFFERENCE;
    }


}

    SpectrogramImage(double[] samples) {
        int samples_to_read = 2048;
        int scale_divisor = samples.length/samples_to_read;
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
                amplitudes[i] = new double[samples_to_read/2];  
        }
        /*
         * Loop Invariant: create here
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
           FFT.transform(this.chunks[m],real_part,imag_part,raw_amplitudes[m]);
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
    }
    


}
