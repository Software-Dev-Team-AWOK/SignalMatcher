package main;

import java.util.ArrayList;
import java.util.List;

public class FingerPrint {

	private double[] bands = new double[10]; 	//Array of cut down chunk.
	private String name;	// Name of the song
	private int location; 	//Location in a file as the index of a chunk
	private List<Integer> constants ; //Constants used in the hashing function

	public FingerPrint(double[] bands, String name, int location,
			List<Integer> constants) {
		this.bands = bands;
		this.name = name;
		this.location = location;
		this.constants = constants;
	}

	public FingerPrint() {}

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

	public List<Integer> getConstants() {
		return constants;
	}

	public void setConstants(List<Integer> constants) {
		this.constants = constants;
	}

	public int hashCode() {
		double result = 0;
		
		for (int i = 0; i < bands.length; i++) {
			result =  result + (bands[i] * constants.get(i));
		}
		
		return (int)result;
	}

	public boolean equals(Object o) {
		if (o instanceof FingerPrint) {
			FingerPrint fp = (FingerPrint)o;
			return fp.hashCode() == hashCode();
		}
		else {
			return false;
		}
	}

}
