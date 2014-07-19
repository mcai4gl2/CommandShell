package shell;


import java.util.*;
import java.io.*;


public class CommandList implements Commands {
	
	private File path;
	private List<File> heap;
	
	public CommandList() {
		heap = new ArrayList<File>();
	}
	
	@Override
	public void buildCommands(String path) {
		// TODO Auto-generated method stub
		this.path = new File(path);
		if (this.path.isDirectory()) addCommands(this.path);
	}
	
	private void addCommands(File path) {
		File[] commands = path.listFiles();
		for (int x = 0; x < commands.length; x++) {
			if (commands[x].isDirectory()) {
				addCommands(commands[x]);
			} else {
				heap.add(commands[x]);
			}
		}
	}
	
	@Override
	public File exists(String command) {
		// TODO Auto-generated method stub
		for (int x = 0; x < heap.size(); x++) {
			if (heap.get(x).getName().equals(command+".py")) {
				return heap.get(x);
			}
		}
		
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		buildCommands(path.getName());
	}
	
	public String[] getAllCommands() {
		ArrayList<String> names = new ArrayList<String>();
		for (int x = 0; x < heap.size(); x++) {
			names.add(heap.get(x).getName());
		}
		return names.toArray(new String[] {});
	}

}
