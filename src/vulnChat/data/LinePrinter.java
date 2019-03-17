package vulnChat.data;


/**
 * This interface defines a way for classes to communicate strings containing non-
 * ASCII characters that fail to pass into a Java stream. It enables the customization
 * of functionalities such as user input parsing for a given text console, without
 * having to receive the text character by character, but instead receive a whole
 * {@link String}.<br>
 * In a practical way, a {@code LinePrinter} implementation has two methods :
 * <ul>
 * 		<li>{@link #print(String)}: decide what printing a line of text should do.</li>
 * 		<li>{@link #println(String)}: decide what printing a newline-ended line of text should do.</li>
 * </ul>
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public interface LinePrinter {
	/**
	 * Sends a {@link String} to be printed without a newline character at the end.
	 * @param text The text to be printed
	 * @see #println(String)
	 */
	public void print(String text);// throws IOException;
	
	/**
	 * Sends a {@link String} to be printed with a newline character at the end.
	 * @param text The text to be printed
	 * @see #print(String)
	 */
	public void println(String text);// throws IOException;
}
