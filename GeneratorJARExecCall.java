
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

//This class calls the generator .jar file and records the output to a txt file.
public class GeneratorJARExecCall {

	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
		String filePath = "/Users/msbrown/Desktop/gp_gen1/Generator1.jar ";
		
		PrintWriter writer = new PrintWriter("Generator1Runs.txt", "UTF-8");
		
		for(double i = -1000;i<=1000;i++){
			//System.out.println(i/100.);
			
			try {
				Process p = Runtime.getRuntime().exec(
						"java -jar " + filePath + Double.toString(i/100.));
				BufferedReader in = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				//System.out.println(in.readLine());
				writer.print(i/100.); //the input.
				writer.println(" " + in.readLine()); //the result from the run.
				
			} catch (IOException e) {
				System.out.println("ERROR");
				e.printStackTrace();
			}
			
			
			
		}
		writer.close();
		System.out.println("done.");
		
	}
}
