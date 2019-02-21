package vulnChat.client;

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


class InOutConsole extends JFrame {
	private static final long serialVersionUID = -8174803948016946910L;
	public final PrintStream inputStream;
	protected final PrintStream outputStream;
	
	public InOutConsole(String title, PrintStream outputStream) {
		super(title);
		this.outputStream = outputStream;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JTextArea outArea = new JTextArea(24, 80);
		outArea.setBackground(Color.BLACK);
		outArea.setForeground(Color.LIGHT_GRAY);
		outArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 17));
		outArea.setCaretColor(Color.WHITE);
		outArea.setLineWrap(true);
		outArea.setEditable(false);
		
		this.inputStream = new PrintStream(new OutputStream() {
			public void write(int b) throws IOException {
				outArea.append(String.valueOf((char) b));
			}
		});
		
		this.add(outArea, BorderLayout.CENTER);
		
		final JTextArea inArea = new JTextArea(5, 80);
		inArea.setBackground(Color.BLACK);
		inArea.setForeground(Color.LIGHT_GRAY);
		inArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		inArea.setCaretColor(Color.WHITE);
		inArea.setLineWrap(true);
		inArea.setAutoscrolls(true);
		
		inArea.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				if (event.getKeyChar() == '\n') {
					String text = inArea.getText();
					
					if (!text.equals("\n")) {
						InOutConsole.this.outputStream.println(text);
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
	
	public void start() {
		this.pack();
		this.setVisible(true);
		this.requestFocus();
	}
	
	public void stop() throws IOException {
		this.inputStream.flush();
		this.inputStream.close();
		this.outputStream.flush();
		this.outputStream.close();
		this.dispose();
	}
}
