package shell;


import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.StringTokenizer;


public class Shell {

	private PythonInterpreterWrapper usrInterp;
	private PythonInterpreterWrapper utilInterp;

	private static Shell shell = new Shell();
	private String prompt;
	private String welcomeString;
	private String exitString;
	private String commandDir;
	private String utilDir;
	private String clearPyClassFiles;
	private String debug;
	private String utilmapPath;
	private Commands usrCommands;
	private Commands utilCommands;
	private Hashtable<String, String> utilMap;
	
	private OutputStream std;
	private OutputStream err;
	
	private Shell() {
		std = System.out;
		err = System.err;
		
		usrInterp = new PythonInterpreterWrapper("user interpreter",std,err);
		utilInterp = new PythonInterpreterWrapper("utility interpreter",std,err);
			
		setupEnvironmentVariables();
		
		usrCommands.buildCommands(commandDir);
		utilCommands.buildCommands(utilDir);
		
		utilMap = new Hashtable<String,String>();
		buildCommandMap(utilMap, utilmapPath);
		
		utilInterp.exec("import sys");
		usrInterp.exec("import sys");
		usrInterp.exec("sys.path.append(\""+commandDir+"\")");
		utilInterp.exec("sys.path.append(\""+utilDir+"\")");
	}
	
	public void synchronize() {
		Field[] envariable = ReflectionUtil.getVariablesOfType(
				this.getClass(), String.class);
		
		try
		{
			for (int index = 0; index < envariable.length; index++) {
				utilInterp.getInterpreter().set(envariable[index].getName(), 
						envariable[index].get(this));
			}
		} catch (IllegalAccessException iae) {
			outputln(err, iae.getMessage());
		}
		
		utilInterp.getInterpreter().set("usrCommands",usrCommands);
		utilInterp.getInterpreter().set("utilCommands",utilCommands);
		
		utilInterp.getInterpreter().set("utilMap", utilMap);
	}
	
	public void start() {
		if (clearPyClassFiles.equals("true")) {
			execUtil("deletepyclass");
		}
		
		if (debug.equals("true")) {
			Field[] envariable = ReflectionUtil.getVariablesOfType(
					this.getClass(), String.class);
			
			outputln(std,"Environment Variables:");
			
			try
			{					
				for (int index = 0; index < envariable.length; index++) {
					outputln(std, (envariable[index].getName() + 
							"=" + envariable[index].get(this)));
				}
				outputln(std,"Jython path=");
				utilInterp.exec("print sys.path");
			} catch (IllegalAccessException iae) {
				outputln(err,iae.getMessage());
			}
			
			outputln(std,"");
		}
	}
	
	public void buildCommandMap(Hashtable<String,String> map, String file) {
		XMLUtil.buildMap(map,file);
	}
	
	public static void outputln(OutputStream stream, String message) {
		try {
			stream.write((message + "\n").getBytes());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void setupEnvironmentVariables() {	
		try {
			FileInputStream config = new FileInputStream("./config.py");
			
			Field[] envariable = ReflectionUtil.getVariablesOfType(
					this.getClass(), String.class);
			
			try
			{
				for (int index = 0; index < envariable.length; index++) {
					utilInterp.getInterpreter().set(envariable[index].getName(), 
							envariable[index].get(this));
				}
			} catch (IllegalAccessException iae) {
				outputln(err, iae.getMessage());
			}
			
			utilInterp.getInterpreter().set("usrCommands",usrCommands);
			utilInterp.getInterpreter().set("utilCommands",utilCommands);
			
			utilInterp.execfile(config);
			
			usrCommands = (Commands) utilInterp.getInterpreter().get("usrCommands")
				.__tojava__(Commands.class);
			utilCommands = (Commands) utilInterp.getInterpreter().get("utilCommands")
				.__tojava__(Commands.class);
			
			try
			{
				for (int index = 0; index < envariable.length; index++) {
					envariable[index].set(this, 
							(String) utilInterp.getInterpreter()
							.get(envariable[index].getName())
							.__tojava__(String.class));
				}
			} catch (IllegalAccessException iae) {
				outputln(err,iae.getMessage());
			}
		} catch (IOException ioe) {
			outputln(err, "Cannot open config file, use default configuration");
			usrCommands = new CommandList();
			utilCommands = new CommandList();
			
			prompt = new String(">>>");
			welcomeString = new String("Welcome to command shell");
			exitString = new String("See you");
			utilDir = new String("./util/");
			commandDir = new String("./command/");
			clearPyClassFiles = new String("false");
			debug = new String("false");
			utilmapPath = new String("./utilMap.xml");
		}
	}
	
	public void getInput(String s) {
		StringTokenizer tokens = new StringTokenizer(s);
		if (!tokens.hasMoreTokens()) {
			outputln(err,"Empty command. Please input command");
			return;
		}
		String comm = tokens.nextToken();
		File command = usrCommands.exists(comm);
		if (command != null) 
		{
			try {
				int x = 0;			
				
				while (tokens.hasMoreTokens()) {
					String arg = tokens.nextToken();
					
					if (arg.startsWith("\"")) {
						arg = arg.substring(1);
						Inner: while (tokens.hasMoreTokens()) {
							arg += " " + tokens.nextToken();
							if (arg.endsWith("\"")) {
								arg = arg.substring(0,arg.length()-1);
								break Inner;
							}
						}
					}
					
					if (arg.endsWith("\"")) {
						arg = arg.substring(0,arg.length()-1);
					}
					
					usrInterp.getInterpreter().set("myarg"+x, arg);
					x++;
				}
				if (debug.equals("true")) {
					usrInterp.execfile(new FileInputStream(command));
				} else {
					usrInterp.exec("import "+comm);
				}
				for (int y = x - 1; y >=0; y--)
					usrInterp.exec("del globals()['myarg" + y + "']");
			} catch (FileNotFoundException fnfe) {
				outputln(err,"This command has been deleted.");
				usrCommands.update();
			}
		} else {
			execUtil(s);
		}
	}
	
	public void execUtil(String s) {
		StringTokenizer tokens = new StringTokenizer(s);
		String comm = tokens.nextToken();
		String filename = utilMap.get(comm);
		if (filename != null) {
			try {
				File command = utilCommands.exists(filename);
				int x = 0;
				while (tokens.hasMoreTokens()) {
					String arg = tokens.nextToken();

					if (arg.startsWith("\"")) {
						arg = arg.substring(1);
						Inner: while (tokens.hasMoreTokens()) {
							arg += " " + tokens.nextToken();
							if (arg.endsWith("\"")) {
								arg = arg.substring(0,arg.length()-1);
								break Inner;
							}
						}
					}
					
					if (arg.endsWith("\"")) {
						arg = arg.substring(0,arg.length()-1);
					}
					
					utilInterp.getInterpreter().set("myarg"+x, arg);
					x++;
				}		
				utilInterp.execfile(new FileInputStream(command));
				
				for (int y = x - 1; y >=0; y--)
					utilInterp.exec("del globals()['myarg" + y + "']");
			} catch (FileNotFoundException fnfe) {
				outputln(err,"This command has been deleted.");
				//utilCommands.update();
			}
		} else {
			outputln(err, "Command " + s + " not recognised.");
		}
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	public String getWelcomeString() {
		return welcomeString;
	}
	
	public String getExitString() {
		return exitString;
	}
	
	public String getCommandDir() {
		return commandDir;
	}
	
	public static Shell getInstance() {
		return shell;
	}
	
	public void setStdOut(OutputStream stream) {
		std = stream;
		utilInterp.setStdOut(std);
		usrInterp.setStdOut(std);
	}
	
	public void setErrOut(OutputStream stream) {
		err = stream;
		utilInterp.setErrOut(err);
		usrInterp.setErrOut(err);
	}
}
