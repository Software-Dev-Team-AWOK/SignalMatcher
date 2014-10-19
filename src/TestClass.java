
// A class that runs tests
public class TestClass {

	public static void main(String[] args) {

		// Example file paths 
		String path1 = "C:\\Users\\Rani\\Desktop\\Samples\\z03.wav";
		String path2 = "C:\\Users\\Rani\\Desktop\\Samples\\z04.wav";
		String path3 = "/course/cs4500f14/Assignments/A4/bad_guy_in_yer_bar.mp3";
		String path4 = "/course/cs4500f14/Assignments/A4/Sor3508.mp3";

		try {
		// Examples WAVE files

			int testmode = 0;
		    switch(testmode) {
		        case 0: {
		            CopyOfWav cwav1 = new CopyOfWav(path1);
		            CopyOfSpectrogramImage cimg = new CopyOfSpectrogramImage(cwav1.getSamples());
		            CopyOfWav cwav2 = new CopyOfWav(path2);
		            CopyOfSpectrogramImage cimg2 = new CopyOfSpectrogramImage(cwav2.getSamples());
		            System.out.println(cimg.equals(cimg2));
		            break;
		        }
		        case 1: {
		            SimpleWav swav1 = new SimpleWav(path1);
		            SimpleSpectrogramImage simg = new SimpleSpectrogramImage(swav1.getSamples());
		            SimpleWav swav2 = new SimpleWav(path2);
		            SimpleSpectrogramImage simg2 = new SimpleSpectrogramImage(swav2.getSamples());
		            System.out.println(simg.equals(simg2));
		            break;

		        }
		        
		        case 2: {
		            SimpleWav swav1 = new SimpleWav(path1);
		            CopyOfSpectrogramImage cimg = new CopyOfSpectrogramImage(swav1.getSamples());
		            SimpleWav swav2 = new SimpleWav(path2);
		            CopyOfSpectrogramImage cimg2 = new CopyOfSpectrogramImage(swav2.getSamples());
                    System.out.println(cimg.equals(cimg2));
                    break;
		        }
		    }

		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		try {
			//Wav wav3 = new Wav(path3);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		try {
			//Wav wav4 = new Wav(path4);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
}
