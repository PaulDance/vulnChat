package vulnChat.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * This {@code abstract} class, extending {@link OutputStream}, enables the same principle, but instead
 * of receiving the data stream character by character with the {@code public void write(int b)} method
 * it is given line by line to the {@code public void write(String line)} method. This is achieved internally
 * by overriding the {@code write(int b)} method and record in an {@link ArrayList} the characters
 * received until a newline occurs. In this case, the {@code write(String line)} method is called on the line
 * obtained by concatenating the previous characters together into a {@code String}.
 * @author Paul Mabileau
 * @version 0.1
 * @see InOutConsole
 */
public abstract class LineOutputStream extends OutputStream {
	private final ArrayList<String> charBuffer;
	private boolean newLineRead = false;
	
	/**
	 * Default and only constructor for a {@link LineOutputStream} object. It initializes the internal character buffer.
	 */
	public LineOutputStream() {
		this.charBuffer = new ArrayList<String>();
	}
	
	/**
	 * Collects the received character (as an {@code int}) in the character buffer if it is not a new line character.
	 * If it is and it is the first one encountered after a series on non new line characters, then the {@code write(String line}
	 * is called on the {@code String} obtained by concatenating the previous characters together.
	 */
	@Override
	public void write(int b) throws IOException {
		if (b == '\n') {																// If a new line occurs,
			if (!newLineRead) {															// and it is the first one after other characters,
				this.newLineRead = true;												// then remember that an new line was encountered,
				this.write(this.charBuffer.stream().collect(Collectors.joining()));		// call the write() method on the concatenated line, 
				this.charBuffer.clear();												// and empty the character buffer;
			}
		}
		else {																			// otherwise (a different character),
			this.newLineRead = false;													// remember no new line has occurred yet,
			this.charBuffer.add(String.valueOf((char) b));								// and add the current one to the buffer.
		}
	}
	
	/**
	 * Defines the action to follow when a line of characters is sent in the stream.
	 * @param line - the {@code String} representing the line of characters sent in the stream
	 * @throws IOException
	 */
	public abstract void write(String line) throws IOException;
}
