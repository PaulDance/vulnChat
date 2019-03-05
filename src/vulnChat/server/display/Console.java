package vulnChat.server.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import vulnChat.data.LinePrinter;


/**
 * This class extends the JFrame graphical frame class to create a small window containing a
 * simple text area on black background you can print to using the generated PrintStream.
 * @see JFrame
 * @see JTextArea
 * @see PrintStream
 * @author Paul Mabileau
 * @version 0.1
 */
public class Console extends JFrame {
	private static final long serialVersionUID = -5480714930071858422L;
	/**
	 * The print stream attached to the window frame's text area. Printing to it will result in printing to the text area.
	 * @see PrintStream
	 */
	//public final PrintWriter printStream;
	public final LinePrinter printStream;
	
	/**
	 * Initializes a Console object with "Console" as a title, 20 rows and 90 columns.
	 * @throws UnsupportedEncodingException 
	 */
	public Console() throws UnsupportedEncodingException {
		this("Console", 20, 90);
	}
	
	/**
	 * Initializes a Console object with a custom title and window sizes.
	 * @param title - The window title
	 * @param rows - The number of character rows for the window's text area format
	 * @param columns - The number of character columns for the window's text area format
	 * @throws UnsupportedEncodingException 
	 */
	public Console(String title, int rows, int columns) throws UnsupportedEncodingException {
		super(title);																// Parent constructor initializes the frame titled <title>.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);						// Sets the behavior of the window to kill itself when closed (when cross button clicked).
		
		JTextArea printArea = new JTextArea(rows, columns);							// Print area settings with (<rows>, <columns>) format,
		printArea.setBackground(Color.BLACK);										// black background color,
		printArea.setForeground(Color.LIGHT_GRAY);									// light gray color,
		printArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));				// monospaced 17pts font,
		printArea.setCaretColor(Color.WHITE);										// white caret color,
		printArea.setLineWrap(true);												// line breaks automatically added when the line is too long for the window,
		printArea.setEditable(false);												// and is not editable: it is meant to be just a console, so no input.
		
//		this.printStream = new PrintWriter(new OutputStreamWriter(new OutputStream() {						// Creates and connects the console's print stream
//			@Override public void write(int b) throws IOException {					// to write every character it receives
//				printArea.append(String.valueOf((char) b));							// to the console's print area
//				printArea.setCaretPosition(printArea.getDocument().getLength());	// and scrolling it down automatically.
//			}
//		}, "UTF8"), true);
		this.printStream = new LinePrinter() {
			@Override public void println(String text) {
				this.print(text + "\n");
			}
			
			@Override public void print(String text) {
				printArea.append(text);
				printArea.setCaretPosition(printArea.getDocument().getLength());
			}
		};
		
		// Adds a scroll bar to the right of the text area that activates only when text is overflowing.
		JScrollPane scrollPane = new JScrollPane(printArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setResizable(true);
	}
	
	/**
	 * Builds the frame graphics and shows the window to the foreground.
	 */
	public void start() {
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		final ThreadLocalRandom randThread = ThreadLocalRandom.current();
		
		this.pack();
		//this.setLocationRelativeTo(null);
		this.setLocation(randThread.nextInt(0, screenDim.width - this.getSize().width), randThread.nextInt(0, screenDim.height - this.getSize().height));
		this.setVisible(true);
		this.requestFocus();
	}
	
	/**
	 * Stops the console and frees resources associated with it.
	 */
	public void stop() {
		//this.printStream.close();
		this.dispose();
	}
}
