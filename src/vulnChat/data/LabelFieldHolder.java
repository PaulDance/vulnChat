package vulnChat.data;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This class defines a type of {@link JPanel} that can be used in order to store a
 * matrix-like formation of {@link JLabel}s and {@link JTextField}s. Those are put
 * line by line in the panel by the {@link #addLine(String, String, String, int, int)}
 * method where the created text label will be on the left and the text field on the
 * right. It is meant to be used as a simple utility that wraps the {@link JPanel} and
 * {@link GroupLayout} functionalities up together, so that creating user input may
 * not be a monstrous headache.
 * 
 * @author Paul Mabileau
 * @version 0.1
 */
public class LabelFieldHolder extends JPanel {
	private static final long serialVersionUID = -2686170976928363398L;
	private final GroupLayout 		layout			;							// A more configurable grid layout, thus a bit more complex;
	private final ParallelGroup		labelsGroup		;							// the group that will receive the labels and will be the first column;
	private final ParallelGroup		fieldsGroup		;							// the group that will receive the fields and will be the second column;
	private final SequentialGroup	verticalGroup	;							// the group that will receive the lines upon adding;
	private final HashMap<String, JTextField> fieldsMap;						// a hashmap which will be used to save fields instances and get them later.
	
	/**
	 * Main constructor for this class, builds up the internal {@link GroupLayout} layout manager
	 * that will be used to control the matrix-like formation of label-field lines of components.
	 */
	public LabelFieldHolder() {
		super();
		this.fieldsMap = new HashMap<String, JTextField>();
		
		this.layout = new GroupLayout(this);
		this.labelsGroup = this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		this.fieldsGroup =	this.layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		this.verticalGroup = this.layout.createSequentialGroup();
		
		this.layout.setHorizontalGroup(											// Sets the horizontal group that contains
			this.layout.createSequentialGroup()									// a sequence made of
				.addGroup(this.labelsGroup)										// the first
				.addGroup(this.fieldsGroup)										// and second columns;
		);
		
		this.layout.setVerticalGroup(this.verticalGroup);						// sets the vertical one which has the lines.
		this.layout.setAutoCreateGaps(true);									// The group layout manages gaps between group elements
		this.layout.setAutoCreateContainerGaps(true);							// and between it and its container just fine.
		this.setLayout(this.layout);
	}
	
	/**
	 * Adds a new label-field line of components to the holder by creating a {@link ParallelGroup}
	 * for the line that will contain the {@link JLabel} with the given text and the {@link JTextField}
	 * with the given initial text, the given size in character columns and the given width in pixels.
	 * It then respectively adds these to the first and second columns of the matrix-like formation of
	 * the panel, and adds the line to a group holding all of them. It also associates the created
	 * {@link JTextField} with the given tag to be stored in an internal {@link HashMap} in order to
	 * access the field later with {@link #getField(String)}.
	 * 
	 * @param labelText The text that the label will be made of
	 * @param fieldText The {@link String} the text field will initially contain
	 * @param fieldTag The tag that will be associated with the text field in order to get it later
	 * @param fieldColumns The number of character columns the field text should contain
	 * @param fieldWidth The width size of the text field in pixels
	 * @return The current {@link LabelFieldHolder} instance in order to enable one chaining actions.
	 */
	public LabelFieldHolder addLine(String labelText, String fieldText, String fieldTag, int fieldColumns, int fieldWidth) {
		final ParallelGroup lineGroup = this.layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		final JLabel label = new JLabel(labelText);
		final JTextField field = new JTextField(fieldText, fieldColumns);
		field.setMaximumSize(new Dimension(fieldWidth, 1));						// Set a max width, otherwise the field is extended to the border;
		this.fieldsMap.put(fieldTag, field);									// save the field reference for later use;
		
		this.labelsGroup.addComponent(label);									// add the label to the first column, right aligned;
		this.fieldsGroup.addComponent(field);									// add the label to the second column, left aligned;
		lineGroup.addComponent(label);											// add both the label
		lineGroup.addComponent(field);											// and the field to the current line,
		this.verticalGroup.addGroup(lineGroup);									// which is in turn added in the vertical group.
		
		return this;															// Finish by returning this object in order to enable chaining actions.
	}
	
	/**
	 * Enables one to retrieve the {@link JTextField} instance associated with a given tag
	 * upon adding a new label-field line with {@link #addLine(String, String, String, int, int)}.
	 * 
	 * @param fieldTag The {@link String} tagging the desired text field
	 * @return The text field that is associated with the given tag
	 */
	public JTextField getField(String fieldTag) {
		return this.fieldsMap.get(fieldTag);
	}
}
