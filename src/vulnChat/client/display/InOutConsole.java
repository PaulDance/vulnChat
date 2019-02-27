package vulnChat.client.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import vulnChat.client.data.LineOutputStream;


/**
 * This class, extending {@link JFrame}, defines all the necessary things for a basic but
 * functional input/output console. The frame created has two different sections: the first one
 * is a {@link JTextArea} that as input for one can print to using the {@link PrintStream} called
 * {@code inputStream}, and a second, also a {@link JTextArea}, is used by the final user
 * to enter text; this text is sent outside the console as an output to the {@code outputStream}
 * when the user strikes the enter key. To configure correctly the output, one should give the constructor
 * a {@link PrintStream} object this console can print to. See the {@link #InOutConsole InOutConsole}
 * constructor for more details about this.
 * @author Paul Mabileau
 * @version 0.1
 */
public class InOutConsole extends JFrame {
	private static final long serialVersionUID = -8174803948016946910L;
	public final PrintStream inputStream;
	protected final PrintStream outputStream;
	
	/**
	 * Default constructor for this class. It uses the two arguments to create the window, but does not start it yet.
	 * To actually do that, use the {@link #start()} method.<br>
	 * In order to configure the {@code outputStream} of the soon-to-become console, there is a simple way using a
	 * customized {@link LineOutputStream} instance that one should see as an example. When building an instance of
	 * the current class, the following should do nicely:
	 * <pre><code>
	 * InOutConsole myConsole = new InOutConsole("myConsole's title", new LineOutputStream() {
	 *    public void write(String line) {
	 *       System.out.println("The user of myConsole just wrote: " + line);
	 *    }
	 * });
	 *</code></pre><br>
	 * Of course, using an {@link OutputStream} instead works just as well, depending on one's actual use of the user
	 * key strikes, but one should know that the information is sent by the console on the given stream when the user
	 * presses the enter key, that is to say line by line.
	 * @param title - the title as a {@link String} that will be given to the {@link JFrame}
	 * @param outputStream - the {@link PrintStream} object the console should be able to use as an output
	 * @see InOutConsole
	 * @see LineOutputStream
	 */
	public InOutConsole(String title, PrintStream outputStream) {
		super(title);
		this.outputStream = outputStream;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JTextArea outArea = new JTextArea(24, 80);					// Output text area where text can be printed.
		outArea.setBackground(Color.BLACK);
		outArea.setForeground(Color.LIGHT_GRAY);
		outArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
		outArea.setCaretColor(Color.WHITE);
		outArea.setLineWrap(true);
		outArea.setEditable(false);
		
		this.inputStream = new PrintStream(new OutputStream() {				// Give this ability to the caller.
			public void write(int b) throws IOException {
				outArea.append(String.valueOf((char) b));
				outArea.setCaretPosition(outArea.getDocument().getLength());
			}
		});
		
		this.add(outArea, BorderLayout.CENTER);
		
		final JTextArea inArea = new JTextArea(5, 80);						// The text area in which the user can write and send text.
		inArea.setBackground(Color.BLACK);
		inArea.setForeground(Color.LIGHT_GRAY);
		inArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		inArea.setCaretColor(Color.WHITE);
		inArea.setLineWrap(true);
		inArea.setAutoscrolls(true);
		
		inArea.addKeyListener(new KeyListener() {							// Detect the user's key strikes in order to
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					String text = inArea.getText();
					
					if (!text.equals("\n")) {
						InOutConsole.this.outputStream.println(text);		// write to the output when the enter key is pressed.
					}
					
					inArea.setText("");
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		
		this.add(inArea, BorderLayout.SOUTH);
		
		JScrollPane outScrollPane = new JScrollPane(outArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane inScrollPane = new JScrollPane(inArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outScrollPane, inScrollPane);
		splitPane.setEnabled(false);
		this.add(splitPane);
	}
	
	/**
	 * Computes the graphics for the console window, starts it and makes it visible to the user.
	 * If one wants to override it in order to add customized behavior upon startup, then one should
	 * add the following instructions at the beginning of the definition: {@code super.start();}
	 */
	public void start() {
		this.pack();
		this.setVisible(true);
		this.requestFocus();
	}
	
	/**
	 * Stops completely this console, frees the occupied resources and disposes the frame.
	 * @throws IOException
	 */
	public void stop() throws IOException {
		this.inputStream.close();
		this.outputStream.close();
		this.dispose();
	}
}
