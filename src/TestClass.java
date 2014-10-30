
// A class that runs tests
public class TestClass {

	public static void main(String[] args) {

		// Example file paths 
		String path1 = "C:\\Users\\Rani\\Desktop\\Samples\\z02.wav";
		String path2 = "C:\\Users\\Rani\\Desktop\\Samples\\z04.wav";
		String path3 = "/course/cs4500f14/Assignments/A4/bad_guy_in_yer_bar.mp3";
		String path4 = "/course/cs4500f14/Assignments/A4/Sor3508.mp3";

		try {
		// Examples WAVE files

			int testmode = 0;
		    switch(testmode) {
		        case 0: {
		            Wav cwav1 = new Wav(path1);
		            SpectrogramImage cimg = new SpectrogramImage(cwav1.getSamples());
		            Wav cwav2 = new Wav(path2);
		            SpectrogramImage cimg2 = new SpectrogramImage(cwav2.getSamples());
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
		            SpectrogramImage cimg = new SpectrogramImage(swav1.getSamples());
		            SimpleWav swav2 = new SimpleWav(path2);
		            SpectrogramImage cimg2 = new SpectrogramImage(swav2.getSamples());
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
