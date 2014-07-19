package shell;


import org.python.util.PythonInterpreter;
import org.python.core.PyException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;


public class PythonInterpreterWrapper {
	private PythonInterpreter interp;
	private String id;
	
	public PythonInterpreterWrapper(String id, OutputStream std, OutputStream err) {
		interp = new PythonInterpreter();
		this.id = id;
		interp.setOut(std);
		interp.setErr(err);
	}
	
	public PythonInterpreter getInterpreter() {
		return interp;
	}
	
	public void exec(String command) {
		try {
			interp.exec(command);
		} catch (PyException e) {
			e.printStackTrace();
		}
	}
	
	public void execfile(FileInputStream stream) throws FileNotFoundException {
		try {
			interp.execfile(stream);
		} catch (PyException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		interp = new PythonInterpreter();
	}
	
	public String getId() {
		return id;
	}
	
	public void setStdOut(OutputStream std) {
		interp.setOut(std);
	}
	
	public void setErrOut(OutputStream err) {
		interp.setErr(err);
	}
}
