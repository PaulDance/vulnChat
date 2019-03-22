package vulnChat.client.data;

import vulnChat.data.MutableBoolean;

/**
 * Defines an object holding {@link MutableBoolean}s for the {@link vulnChat.client.main.Client Client}
 * to use as settings.
 *  
 * @author Paul Mabileau
 * @version 0.1
 */
public class Settings {
	/**
	 * This settings tells if the {@link vulnChat.client.main.Client Client} should use serialized object communication or not.
	 */
	public final MutableBoolean objTransmit = new MutableBoolean(false);
}
