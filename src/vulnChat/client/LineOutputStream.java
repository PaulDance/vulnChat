package vulnChat.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;


public abstract class LineOutputStream extends OutputStream {
	private final ArrayList<String> charBuffer;
	private boolean newLineRead = false;
	
	public LineOutputStream() {
		this.charBuffer = new ArrayList<String>();
	}

	@Override
	public void write(int b) throws IOException {
		if (b == '\n') {
			if (!newLineRead) {
				this.newLineRead = true;
				this.write(this.charBuffer.stream().collect(Collectors.joining("")));
				this.charBuffer.clear();
			}
		}
		else {
			this.newLineRead = false;
			this.charBuffer.add(String.valueOf((char) b));
		}
	}
	
	public abstract void write(String line) throws IOException;
}
