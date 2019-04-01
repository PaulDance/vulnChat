package vulnChat.server.data;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vulnChat.data.LabelFieldHolder;
import vulnChat.data.MutableBoolean;


/**
 * Defines a holding object for {@link JCheckBox} objects, while extending {@link JPanel}.
 * The idea for this class was to create a simpler way of adding rules settings in the
 * {@link vulnChat.client.display.ConfigDialog ConfigDialog}, which was before a bit tedious
 * to do by hand. So to use this class, instantiate it then add check box entries as a
 * label {@link String} and a reference to a {@link MutableBoolean} setting. See
 * {@link #addNew(String, MutableBoolean)} for a detailed description. Finally, invoke
 * {@link #createItemListener()} in order to create and add the {@link ItemListener} that
 * synchronizes settings and check boxes states to the check boxes stored.
 * 
 * @see #sync(ItemEvent)
 * @see #syncAll()
 * @see #selectAll()
 * @see #deselectAll()
 * @author Paul Mabileau
 * @version 0.1
 */
public class CheckBoxHolder extends JPanel {
	private static final long serialVersionUID = -2182041975067798177L;
	private final ArrayList<OptionEntry> entries;
	
	/**
	 * Defines a tuple containing four references:
	 * 
	 *<ul>
	 *		<li>a check box label as a {@link String}</li>
	 *		<li>a {@link MutableBoolean} server setting</li>
	 *		<li>a {@link JCheckBox} built using the last two objects</li>
	 *		<li>and a {@link CheckBoxListener} that is activated upon clicking the check box</li>
	 * </ul>
	 * 
	 * It is used by the {@link CheckBoxHolder} class in order to store the entries got
	 * from the template creator in an {@link ArrayList} and manage them at a later time.
	 * 
	 * @author Paul Mabileau
	 * @version 0.2
	 */
	private final class OptionEntry {
		public final String label;
		public final MutableBoolean setting;
		public final JCheckBox checkBox;
		private final CheckBoxListener listener;
		
		/**
		 * Saves the given parameters, builds the {@link JCheckBox} obtained from them and saves it also.
		 * 
		 * @param label The {@link String} that will the check box' label text
		 * @param setting The {@link MutableBoolean} server setting associated with the label
		 * @see OptionEntry
		 * @see CheckBoxHolder
		 */
		public OptionEntry(String label, MutableBoolean setting, CheckBoxListener listener) {
			this.label = label;
			this.setting = setting;
			this.checkBox = new JCheckBox(label, setting.getValue());
			this.listener = listener;
		}
		
		public void activateListener() {
			if (this.listener != null) {
				this.listener.onClick(this.checkBox);
			}
		}
	}
	
	/**
	 * Defines a way for the caller of {@link CheckBoxHolder} to add custom behavior when a check box
	 * which is registered in the holder is clicked by the end user. This interface provides one method:
	 * {@link #onClick(JCheckBox)} which is called upon clicking the check box.
	 * 
	 * @author Paul Mabileau
	 * @version 0.1
	 */
	public interface CheckBoxListener {
		/**
		 * Defines the behavior to follow when the given check box is clicked by the user.
		 * @param checkBox The {@link JCheckBox} that generated the click event. Use it to get its current state.
		 */
		public void onClick(JCheckBox checkBox);
	}
	
	/**
	 * Initializes the storage.
	 * @see CheckBoxHolder
	 * @see OptionEntry
	 */
	public CheckBoxHolder() {
		super();
		this.entries = new ArrayList<OptionEntry>();
	}
	
	/**
	 * Adds a entry to the inner storage, builds a JCheckBox from it and adds it to the current panel.
	 * 
	 * @param label The {@link String} that will the check box' label text
	 * @param setting The {@link MutableBoolean} server setting associated with the label
	 * @see OptionEntry
	 * @see CheckBoxHolder
	 */
	public void addNew(String label, MutableBoolean setting) {
		this.addNew(label, setting, null, null);
	}
	
	/**
	 * Adds a entry to the inner storage, builds a JCheckBox from it and adds it to the current panel.
	 * It also registers the given listener to the option entry.
	 * 
	 * @param label The {@link String} that will the check box' label text
	 * @param setting The {@link MutableBoolean} server setting associated with the label
	 * @param listener The {@link CheckBoxListener} that will be activated on state change
	 * @see OptionEntry
	 * @see CheckBoxHolder
	 */
	public void addNew(String label, MutableBoolean setting, CheckBoxListener listener) {
		this.addNew(label, setting, listener, null);
	}
	
	/**
	 * Adds a entry to the inner storage, builds a JCheckBox from it and adds it to the current panel.
	 * It also registers the given listener to the option entry and adds the given label-field holder
	 * to the panel so that it will appear below the check box.
	 * 
	 * @param label The {@link String} that will the check box' label text
	 * @param setting The {@link MutableBoolean} server setting associated with the label
	 * @param listener The {@link CheckBoxListener} that will be activated on state change
	 * @param holder The {@link LabelFieldHolder} to be put right below the check box
	 * @see OptionEntry
	 * @see CheckBoxHolder
	 */
	public void addNew(String label, MutableBoolean setting, CheckBoxListener listener, LabelFieldHolder holder) {
		final OptionEntry entry = new OptionEntry(label, setting, listener);
		this.entries.add(entry);
		this.add(entry.checkBox);
		
		if (holder != null) {
			this.add(holder, SwingConstants.SOUTH);
		}
	}
	
	/**
	 * @param index The index of an internal array's element
	 * @return The {@link JCheckBox} instance reference stored in {@link OptionEntry} at index {@literal <index>}
	 * @see CheckBoxHolder
	 */
	public JCheckBox getCheckBox(int index) {
		return this.entries.get(index).checkBox;
	}
	
	/**
	 * @param index The index of an internal array's element
	 * @return The text label stored in {@link OptionEntry} at index {@literal <index>}
	 * @see CheckBoxHolder
	 */
	public String getLabel(int index) {
		return this.entries.get(index).label;
	}
	
	/**
	 * @param index The index of an internal array's element
	 * @return The {@link MutableBoolean} instance reference stored in {@link OptionEntry} at index {@literal <index>}
	 * @see CheckBoxHolder
	 */
	public MutableBoolean getSetting(int index) {
		return this.entries.get(index).setting;
	}
	
	/**
	 * @return The size of the internal {@link ArrayList}.
	 * @see CheckBoxHolder
	 */
	public int length() {
		return this.entries.size();
	}
	
	/**
	 * Synchronizes the {@link JCheckBox} that generated the {@link ItemEvent} with the associated server setting.
	 * @param event The {@linkplain ItemEvent} generated by a {@link JCheckBox} and usually caught by an {@link ItemListener}
	 * @see Settings
	 * @see MutableBoolean
	 */
	public void sync(ItemEvent event) {
		final Object source = event.getItemSelectable();
		
		for (OptionEntry entry: this.entries) {
			if (source == entry.checkBox) {
				entry.setting.setValue(entry.checkBox.isSelected());
				entry.activateListener();
			}
		}
	}
	
	/**
	 * Synchronizes all the {@link JCheckBox} instances stored with their respective server setting.
	 * @see Settings
	 * @see MutableBoolean
	 */
	public void syncAll() {
		for (OptionEntry entry: this.entries) {
			entry.setting.setValue(entry.checkBox.isSelected());
			entry.activateListener();
		}
	}
	
	/**
	 * @return The {@link ItemListener} created to synchronized the stored {@link JCheckBox} instances
	 * with their respective server settings and adds it to them.
	 * @see Settings
	 * @see CheckBoxHolder
	 */
	public ItemListener createItemListener() {
		final ItemListener itemListener = new ItemListener() {
			@Override public void itemStateChanged(ItemEvent event) {
				CheckBoxHolder.this.sync(event);
			}
		};
		
		for (OptionEntry entry: this.entries) {
			entry.checkBox.addItemListener(itemListener);
		}
		
		return itemListener;
	}
	
	/**
	 * Sets the state to selected for all the {@link JCheckBox} instances stored and their respective server setting.
	 * @see Settings
	 * @see CheckBoxHolder
	 */
	public void selectAll() {
		for (OptionEntry entry: this.entries) {
			entry.setting.setValue(true);
			entry.checkBox.setSelected(true);
		}
	}
	
	/**
	 * Sets the state to deselected for all the {@link JCheckBox} instances stored and their respective server setting.
	 * @see Settings
	 * @see CheckBoxHolder
	 */
	public void deselectAll() {
		for (OptionEntry entry: this.entries) {
			entry.setting.setValue(false);
			entry.checkBox.setSelected(false);
		}
	}
}
