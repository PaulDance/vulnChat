/**
 * This package is the starting point for the vulnChat project.<br><br>
 * 
 * This pure Java development project creates an (very) vulnerable chat application which
 * enables one to practice network security as it allows choosing from different types of
 * difficulties, ranging from the very easy non-ciphered TCP to harder home-made-ciphered
 * serialized communication. It aims at being a cool tool for training one's interception
 * skills.<br><br>
 * 
 * As of now, the project sources are separated in three package categories belonging to
 * the {@link vulnChat.client client} side or the {@link vulnChat.server server} side or
 * {@link vulnChat.data both} sides of the chat application, thus making a sort of custom
 * standardized structure (so nerdy, right?):
 * 
 * <ul>
 *		<li>{@code main}: is responsible for the main functionalities of the concerned package</li>
 *		<li>{@code display}: enables graphically interacting with the user</li>
 *		<li>{@code data}: defines the required data structures</li>
 * </ul>
 * 
 * @author Paul Mabileau
 * @version 0.3
 */
package vulnChat;