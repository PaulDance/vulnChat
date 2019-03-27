/**
 * Includes the classes making the display part of the client:
 * 
 * <ul>
 *		<li>The {@link vulnChat.client.display.ConfigDialog ConfigDialog}: the starting
 *			point of the display</li>
 *		<li>The {@link vulnChat.client.display.InOutConsole InOutConsole} extension of
 *			{@link javax.swing.JFrame JFrame} creating an input-output console</li>
 *		<li>The {@link vulnChat.client.display.ChatWindow ChatWindow} extension of
 *			{@code InOutConsole} specializing it for chat purposes, started by the
 *			{@code ConfigDialog}</li>
 * </ul>
 * 
 * @author Paul Mabileau
 * @version 0.2
 */
package vulnChat.client.display;