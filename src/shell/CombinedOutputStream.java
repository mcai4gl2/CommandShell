package shell;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

public class CombinedOutputStream extends OutputStream {
	
	private List<OutputStream> streams;
	
	public CombinedOutputStream(OutputStream[] output) {
		streams = new ArrayList<OutputStream>();
		for (int x=0;x< output.length;x++) {
			streams.add(output[x]);
		}
	}
	
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		for (int x = 0; x < streams.size();x++) {
			streams.get(x).write(b);
		}
	}

}
