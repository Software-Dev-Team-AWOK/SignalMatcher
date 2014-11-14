package main;

public class EuclidianValue {

	//TAKES TWO 10 BYTE ARRAYS AND PRODUCES THE EUCLIDIAN VALUE.
		
	public static int getEuclidianValue(int[] array1, int[] array2) {
		
		int output = 0 ;
		
		for (int i = 0; i < array1.length; i++) {
			 
			int diffSquare = (array1[i] - array2[i])^2;
			
			 output = output + diffSquare;
			 
		}
		
		output = (int) Math.sqrt(output);
		
		return output;
	}
	
}
