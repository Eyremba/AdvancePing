/**
 * 
 */
package tk.ccbluex.AdvancePing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.ccbluex.AdvancePing.AdvancePing;

/**
 * @author Marco
 *
 */
public class AdvancePingCommand implements CommandExecutor {
	
	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission(AdvancePing.NAME + ".command.advanceping")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("reload")) {
					sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("Startreload"));
					AdvancePing.advancePing.reloadConfig();
					sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("Successfulreload"));
					return true;
				}
			}
			
			sender.sendMessage(AdvancePing.PREFIX + "/advanceping <reload>");
		}else
			sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("NoPermissionsToExecute"));
		return true;
	}
}