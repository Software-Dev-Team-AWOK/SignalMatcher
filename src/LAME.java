import java.util.ArrayList;
import java.util.List;

public class LAME {
    public static void convert(String infile, String outfile, List<String> opts){
        ProcessBuilder lamePB = new ProcessBuilder();
        ArrayList<String> lameargs = new ArrayList<String>();
                
        String lamePath = "/course/cs4500f14/bin/lame";
        lameargs.add(lamePath);
        lameargs.addAll(opts);
        lameargs.add(infile);
        lameargs.add(outfile);
        
        lamePB.command(lameargs);
        
        Process lameP = null;
        try{
            lameP = lamePB.start();
            lameP.waitFor();
        }catch (Exception e){
            System.out.println("ERROR: Error converting MP3 to WAV: "+ e.getMessage());
            System.exit(1);
        }
    }

}
