package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Represents the fingerprint of a chunk
public class Fingerprint {
	
	//We are keeping this value constant for now
	private static final double LOG2 = Math.log(2);
	//Points to the next fingerprint or to null
	private Fingerprint next;
	private int[] fingerprint; 	//Array of cut down chunk.
	private String name;	// Name of the song
	private int location; 	//Location in a file as the index of a chunk
	private static final int NUMBER_OF_BANDS = 10;
	private static final double PRIMARY_SCALE_MAX = 50000;
	private static final double HASH_SCALE_MAX = 100;
	
	public Fingerprint(double[] samples, String name, int location) {
		fingerprint = new int[NUMBER_OF_BANDS];
		double[] amplitudes = new double[samples.length];
		FFT.transform(samples, new double[samples.length],
					new double[samples.length], amplitudes);
		bandFilter(amplitudes);
		scale();
		this.name = name;
		this.location = location;
		this.next = null;
	}
	
	//Adds a pointer to the next fingerprint chunk
	public void addNext(Fingerprint f){
		next = f;
	}
	
	// returns the next fingerprint chunk
	public Fingerprint getNext(){
		return next;
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
	public void bandFilter(double[] amplitudes) {
		int window = 1;
		int pointer = 1;
		for(int i = 0; i < fingerprint.length; i++){
			fingerprint[i] = addBand(pointer, window, amplitudes);
			pointer += window;
			window *= 2;
		}

	}
	
	//Helper for above, adds up a given number of elements
	private int addBand(int start, int length, double[] amplitudes){
		int acc = 0;
		for(int i = start; i < start + length; i++){
			acc += amplitudes[i];
		}
		return acc;
	}
	
	//Scales the band-filtered fingerprint to be between 0 and 
	//PRIMARY_SCALE_MAX
	private void scale(){
		int largest = findLargest(fingerprint);
		double scaleFactor = largest / PRIMARY_SCALE_MAX;
		for(int i = 0; i < fingerprint.length; i++){
			fingerprint[i] *= scaleFactor;
		}
	}

	//Gets the bands
	public int[] getBands() {
		return fingerprint;
	}

	//Sets the bands
	public void setBands(int[] bands) {
		this.fingerprint = bands;
	}

	//Gets the name of the file
	public String getName() {
		return name;
	}

	//Sets the name of the file
	public void setName(String name) {
		this.name = name;
	}

	//Gets the index of the file
	public int getLocation() {
		return location;
	}

	//which means our hash will *probably* 
	//reflect the content and the order.
	//Creates the hash code
	private static final int[] HASH_CONSTANTS = 
			new int[]{10, 12, 21, -22, -21, 23, 25, -22, 24, 22};
	public int hashCode() {
		int result = 0;
		
		int l = findLargest(fingerprint);

		for (int i = 0; i < fingerprint.length; i++) {
			result +=  HASH_CONSTANTS[i] + (fingerprint[i] * 
											((double)(l/HASH_SCALE_MAX)));  
		}

		return (int)result;
	}
	
	//Returns the largest element in the fingerprint
	private int findLargest(int[] target){
		int largest = target[0];
		for(int i = 1; i < target.length; i++){
			if (target[i] > largest)
				largest = target[i];
		}
		return largest;
	}

	//next, override the equals() method 
	//so that we're comparing bands and then FFTs
	public boolean equals(Object o){
		if(o instanceof Fingerprint){
			Fingerprint f = (Fingerprint)o;
			if(this.hashCode() == f.hashCode()){
				return true;
			} else {
				//do something
			}
			
			return true;
	}
		return true;


}
}
