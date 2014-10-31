package main;

public class SignalMatcher {
	public static void main(String[] args){
		SimpleWav w = new SimpleWav("/course/cs4500f14/Assignments/A5/D2/curieuse.wav");
		//SimpleMP3 m = new SimpleMP3("/home/jkantor/t1/a.mp3");
		double[] dw = w.getSamples();
		SimpleMP3 m = new SimpleMP3("/course/cs4500f14/Assignments/A5/D1/curieuse.mp3");
		double[] dm = m.getSamples();
		int i = 0;
		compare(dw, dm);
		System.out.println(dw + "" + dm + "" + i);
	}
	
	public static void compare(double[] d1, double[] d2){
		int s = d1.length;
		double acc = 0;
		double x1, x2;
		for(int i = 0; i < s; i++){
			x1 = d1[i];
			x2 = d2[i];
			if(x1 != x2){
				System.out.println(d1[i] + "," + d2[i]);
				acc += Math.abs(d1[i] - d2[i]);
			}
		}
		
		System.out.println(acc / s);
		
	}

}
