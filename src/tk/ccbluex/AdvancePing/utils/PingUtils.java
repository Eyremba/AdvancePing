/**
 * 
 */
package tk.ccbluex.AdvancePing.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

/**
 * @author Marco
 *
 */
public class PingUtils {

	/**
	 * Get the ping of the player.
	 * @param player
	 * @return
	 */
	public static int getPing(Player player) {
		try{
			Object nms_player = player.getClass().getMethod("getHandle").invoke(player);
			Field fieldPing = nms_player.getClass().getDeclaredField("ping");
			fieldPing.setAccessible(true);
			return fieldPing.getInt(nms_player);
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		}catch(NoSuchFieldException e) {
			e.printStackTrace();
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}catch(IllegalAccessException e) {
			e.printStackTrace();
		}catch(InvocationTargetException e) {
			e.printStackTrace();
		}catch(SecurityException e) {
			e.printStackTrace();
		}
		return 0;
	}
}