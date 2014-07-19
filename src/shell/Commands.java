package shell;

import java.io.*;

public interface Commands {
	public void buildCommands(String path);
	public File exists(String command);
	public void update();
	public String[] getAllCommands();
}
