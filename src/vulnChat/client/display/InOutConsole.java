package vulnChat.client.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import vulnChat.data.LinePrinter;


/**
 * This class, extending {@link JFrame}, defines all the necessary things for a basic but
 * functional input/output console. The frame created has two different sections: the first one
 * is a {@link JTextArea} that as input for one can print to using the {@link LinePrinter} called
 * {@link #inputPrinter}, and a second, also a {@link JTextArea}, is used by the final user
 * to enter text; this text is sent outside the console as an output to the {@code outputPrinter}
 * when the user strikes the enter key. To configure correctly the output, one should give the constructor
 * a {@link LinePrinter} object this console can print to. See the {@link #InOutConsole InOutConsole}
 * constructor for more details about this.
 * 
 * @author Paul Mabileau
 * @version 0.2
 */
public class InOutConsole extends JFrame {
	private static final long serialVersionUID = -8174803948016946910L;
	/**
	 * The {@link LinePrinter} instance connected to the console's output text area.
	 * Printing to it will result in appending the text to this area.
	 */
	public final LinePrinter inputPrinter;
	private final LinePrinter outputPrinter;
	private int charLimit = 0;
	
	/**
	 * Default constructor for this class. It uses the two arguments to create the window, but does not start it yet.
	 * To actually do that, use the {@link #start()} method.<br>
	 * In order to configure the {@code outputPrinter} of the console, there is a simple way using a
	 * customized {@link LinePrinter} instance that one should see as an example. When building an instance of
	 * the current class, the following should do nicely:
	 * 
	 * <pre><code>
	 *	InOutConsole myConsole = new InOutConsole("myConsole's title", new LinePrinter() {
	 *		public void print(String line) {
	 *			System.out.print("The console printed '" + line + "' without a newline.");
	 *		}
	 *		public void println(String line) {
	 *			System.out.println("The console printed '" + line + "' with a newline.");
	 *		}
	 *	});
	 *</code></pre><br>
	 *
	 * Using an {@link java.io.OutputStream OutputStream} would kind of work, depending on one's actual use of the user
	 * key strikes, but one should know that such streams cut the characters up byte by byte, which disables the correct
	 * use of non-ASCII characters.
	 * 
	 * @param title The title as a {@link String} that will be given to the {@link JFrame}
	 * @param outputPrinter The {@link LinePrinter} object the console should be able to use as an output
	 * @see InOutConsole
	 */
	public InOutConsole(String title, LinePrinter outputPrinter) {
		super(title);
		this.outputPrinter = outputPrinter;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		final JTextArea outArea = new JTextArea(24, 80);	// Output text area where text can be printed.
		outArea.setBackground(Color.BLACK);
		outArea.setForeground(Color.LIGHT_GRAY);
		outArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
		outArea.setCaretColor(Color.WHITE);
		outArea.setLineWrap(true);
		outArea.setEditable(false);
		
		this.inputPrinter = new LinePrinter() {				// The LinePrinter the caller can use to
			@Override public void println(String line) {
				this.print(line + "\n");
			}
			
			@Override public void print(String line) {
				outArea.append(line);						// append text at the end of the output text area.
				outArea.setCaretPosition(outArea.getDocument().getLength());
			}
		};
		
		final JTextArea inArea = new JTextArea(5, 80);		// The text area in which the user can write and send text.
		inArea.setBackground(Color.BLACK);
		inArea.setForeground(Color.LIGHT_GRAY);
		inArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		inArea.setCaretColor(Color.WHITE);
		inArea.setLineWrap(true);
		inArea.setAutoscrolls(true);
		
		inArea.addKeyListener(new KeyListener() {			// Detect the user's key strikes in order to manage the keyboard inputs:
			public void keyTyped(KeyEvent event) {
				final String text = inArea.getText().trim();
				
				if (text.length() > InOutConsole.this.charLimit) {
					event.setKeyChar('\00');				// if limit is reached, change the char to null, so that it does not display after this method returns;
				}
				
				if (event.getKeyChar() == '\n') {			// if return is pressed
					if (!text.equals("\n")) {				// and the line is not empty, then send the text to the caller,
						InOutConsole.this.outputPrinter.println(text);
					}
					
					inArea.setText("");						// and finally void the input text area.
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		
		// Scroll panes that enable adding scroll bars on the side of the text areas when too much lines are created.
		final JScrollPane outScrollPane = new JScrollPane(outArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		final JScrollPane inScrollPane = new JScrollPane(inArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outScrollPane, inScrollPane);
		splitPane.setEnabled(true);
		splitPane.setResizeWeight(1);
		mainPanel.add(splitPane);
		
		this.add(mainPanel);
	}
	
	
	/**
	 * Computes the graphics for the console window, starts it and makes it visible to the user.
	 * If one wants to override it in order to add customized behavior upon startup, then one should
	 * add the following instructions at the beginning of the definition: {@code super.start();}
	 */
	public void start() {
		final Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		final ThreadLocalRandom randThread = ThreadLocalRandom.current();
		
		this.pack();
		this.setVisible(true);								// Spawn the window at a random location:
		this.setLocation(randThread.nextInt(0, screenDim.width - this.getSize().width), randThread.nextInt(0, screenDim.height - this.getSize().height));
		this.requestFocus();
	}
	
	/**
	 * Stops completely this console, frees the occupied resources and disposes the frame.
	 * @throws IOException
	 */
	public void stop() throws IOException {
		this.dispose();
	}
	
	/**
	 * Sets a limit to the length of messages the client can send.
	 * @param limit The maximum length as an integer
	 */
	public void setCharLimit(int limit) {
		if (limit > 0) {
			this.charLimit = limit;
		}
	}
}
