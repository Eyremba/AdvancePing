/**
 * 
 */
package tk.ccbluex.AdvancePing.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.ccbluex.AdvancePing.AdvancePing;
import tk.ccbluex.AdvancePing.utils.MathUtils;

/**
 * @author Marco
 *
 */
public class AdvancePingCommand implements CommandExecutor {

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission(AdvancePing.NAME + ".command.ping")) {
			if(args.length > 0) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(offlinePlayer != null) {
					File file = new File(AdvancePing.FILEPATH, "data.yml");
					FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
					
					if(configuration.contains(offlinePlayer.getUniqueId().toString())) {
						int lastPing = configuration.getInt(offlinePlayer.getUniqueId().toString() + ".lastPing");
						List<Integer> pings = configuration.getIntegerList(offlinePlayer.getUniqueId().toString() + ".pings");
						int average = MathUtils.AverageCalculate(pings);
						
						sender.sendMessage("§e--------------- " + AdvancePing.PREFIX + "§e---------------");
						sender.sendMessage("§cLast Ping: " + lastPing);
						sender.sendMessage("§cAverage Ping: " + average);
						sender.sendMessage("§e--------------- v" + AdvancePing.VERSION + " §e------------------------");
					}else
						sender.sendMessage(AdvancePing.PREFIX + "The player is not in the database.");
				}else
					sender.sendMessage("AdvancePing.PREFIX + The player can't found.");
			}else
				sender.sendMessage(AdvancePing.PREFIX + "/ping <player>");
		}else
			sender.sendMessage(AdvancePing.PREFIX + "You don't have permmission to execute this command.");
		return true;
	}
}