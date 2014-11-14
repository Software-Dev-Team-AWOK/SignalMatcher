package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FingerPrint {
    //We are keeping this value constant for now
	private double[] bands; 	//Array of cut down chunk.
	private double[] amplitudes;
	private int[] testData; 
	private String name;	// Name of the song
	private int location; 	//Location in a file as the index of a chunk
	public FingerPrint(double[] samples, String name, int location, List<Integer> constants) {
		amplitudes = new double[samples.length];
		bands = new double[(int)Utils.lg(samples.length)];
		FFT.transform(samples, new double[samples.length], new double[samples.length], amplitudes);
		bandFilter();
		this.name = name;
		this.location = location;
	}
	/*
	public FingerPrint(double[] amplitudes) {
	    this.amplitudes = new double[amplitudes.length];
	       for(int i = 0; i<amplitudes.length; i++)
	            this.amplitudes[i] = amplitudes[i];
	       

	}
	
	public FingerPrint(int[] testData) {
	    this.testData = new int[testData.length];
	    for(int i = 0; i<testData.length; i++)
	        this.testData[i] = testData[i];
	}
	*/
	public int[] getTestData() {
	    return testData;
	}
	/*
	 * 
	 * 
	 * sums of amplitudes over ten bands of frequencies.
	 * So:
	 * band[0] = amplitudes[0]
	 * band[1] = amplitudes[1] + amplitudes[2];
	 * band[2] = amplitudes[3]+[4]+[5]+[6]
	 * ...
	 * band[10] = amplitudes[511] +...+amplitudes[1023]
	 */
	public void bandFilter() {
	    int window = 1;
	    int pointer = 1;
	    for(int i = 0; i < Utils.lg(amplitudes.length); i++){
	    bands[i] = addBand(pointer, window, amplitudes);
	    pointer += window;
	    window *= 2;
	    }

	    }

	    private double addBand(int start, int length, double[] amplitudes){
	    double acc = 0;
	    for(int i = start; i < start + length; i++){
	    acc += amplitudes[i];
	    }
	    return acc;
	    }
	
	public double[] getBands() {
		return bands;
	}

	public void setBands(double[] bands) {
		this.bands = bands;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}
	
	//sum-products: bands[0]*10 + bands[1]*100 + bands[2]*1000
	//!=bands[2]*10 + bands[1]*100 + bands[0]*1000
	//which means our hash will *probably* 
	//reflect the content and the order.
	public int hashCode() {
		double result = 0;
		
		for (int i = 0; i < testData.length; i++) {
			result =  ((10*result) + testData[i])%1031;  
		}
        //System.out.println(result);
		return (int)result;
	}
	
	//next, override the equals() method 
	//so that we're comparing bands and then FFTs.


}
