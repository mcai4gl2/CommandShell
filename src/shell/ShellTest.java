package shell;


import java.io.*;


public class ShellTest {
	public static void main(String[] args) throws IOException {	
				
		Shell shell = Shell.getInstance();
		String input = new String();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		BufferedOutputStream fileout = new BufferedOutputStream(
			new FileOutputStream(new File("lastSession.log"),false));
		
		CombinedOutputStream std = new CombinedOutputStream(
				new OutputStream[] {System.out, fileout});
				
		CombinedOutputStream err = new CombinedOutputStream(
				new OutputStream[] {System.err,fileout});
		
		shell.setStdOut(std);
		shell.setErrOut(err);
		
		shell.start();
		Shell.outputln(std, shell.getWelcomeString());
		
		do {
			Shell.outputln(std, shell.getPrompt());
			try {
				input = in.readLine();
				input = input.trim();
				Shell.outputln(fileout,input);
				shell.getInput(input);
			} catch (IOException ioe) {
				Shell.outputln(err, ioe.getMessage());
				break;
			}	
		} while (!input.equals("exit"));
		
		Shell.outputln(std, shell.getExitString());
		
		in.close();
		fileout.flush();
		fileout.close();
	}
}
