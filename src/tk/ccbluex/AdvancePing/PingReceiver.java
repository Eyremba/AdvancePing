/**
 * 
 */
package tk.ccbluex.AdvancePing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.ccbluex.AdvancePing.utils.PingUtils;

/**
 * @author Marco
 *
 */
public class PingReceiver implements Runnable {
	
	/**
	 * 
	 */
	public PingReceiver() {
		File file = new File(AdvancePing.FILEPATH, "data.yml");
		if(!file.exists()) {
			try{
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
				System.err.println(AdvancePing.PREFIX + "File can't create.");
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		File file = new File(AdvancePing.FILEPATH, "data.yml");
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		for(Player player : Bukkit.getOnlinePlayers()) {
			int ping = PingUtils.getPing(player);
			
			configuration.set(player.getUniqueId().toString() + ".lastPing", ping);
			List<Integer> pings = configuration.getIntegerList(player.getUniqueId() + ".pings");
			if(pings == null)
				pings = new ArrayList<>();
			pings.add(ping);
			configuration.set(player.getUniqueId().toString() + ".pings", pings);
			try{
				configuration.save(file);
			}catch(IOException e) {
				e.printStackTrace();
				System.err.println(AdvancePing.PREFIX + "File can't save.");
			}
		}
	}
}