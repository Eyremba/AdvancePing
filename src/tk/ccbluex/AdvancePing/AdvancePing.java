/**
 * 
 */
package tk.ccbluex.AdvancePing;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import tk.ccbluex.AdvancePing.commands.AdvancePingCommand;
import tk.ccbluex.AdvancePing.commands.PingCommand;

/**
 * @author Marco
 *
 */
public class AdvancePing extends JavaPlugin {
	
	public static AdvancePing advancePing;
	
	public static String NAME;
	public static String VERSION;
	public static String FILEPATH;
	public static String PREFIX = "§7[§cADVANCEPING§7] §e";
	
	private int scheduler;
	
	/**
	 * 
	 */
	public AdvancePing() {
		advancePing = this;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		NAME = getDescription().getName();
		VERSION = getDescription().getVersion();
		FILEPATH = "plugins/" + NAME + "/";
		System.out.println("[" + NAME + "] Plugin is starting.");
		FileConfiguration configuration = getConfig();
		configuration.options().header(NAME + " by CCBlueX");
		configuration.addDefault("checkPerTicks", 20 * 60);
		//Messages
		configuration.addDefault("messages.prefix", "&7[&cADVANCEPING&7]");
		configuration.addDefault("messages.NoPermissionsToExecute", "You don't have permmission to execute this command.");
		configuration.addDefault("messages.PlayerNotFound", "The player can't found.");
		configuration.addDefault("messages.NotInDatabase", "The player is not in the database.");
		configuration.addDefault("messages.YouMustBeAPlayer", "You must be a Player.");
		configuration.addDefault("messages.Lastping", "Last Ping");
		configuration.addDefault("messages.Averageping", "Average Ping");
		configuration.addDefault("messages.Close", "Close");
		configuration.addDefault("messages.Back", "Back");
		configuration.addDefault("messages.Player", "Player");
		configuration.addDefault("messages.Players", "Players");
		configuration.addDefault("messages.Informations", "Informations");
		configuration.addDefault("messages.IPAddress", "IP-Address");
		configuration.addDefault("messages.informationscantload", "Informations can't load because the player is not online.");
		configuration.addDefault("messages.NextPage", "Next Page");
		configuration.addDefault("messages.PreviousPage", "Previous Page");
		configuration.addDefault("messages.Page", "Page");
		configuration.addDefault("messages.Startreload", "Reloading...");
		configuration.addDefault("messages.Successfulreload", "Configs was successful reloaded.");
		configuration.options().copyDefaults(true);
		saveConfig();
		
		PREFIX = ChatColor.translateAlternateColorCodes('&', configuration.getString("messages.prefix")) + " §e";
		
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PingReceiver(), 0, configuration.getInt("checkPerTicks"));
		getCommand("advanceping").setExecutor(new AdvancePingCommand());
		getCommand("ping").setExecutor(new PingCommand());
		System.out.println("[" + NAME + "] Plugin started.");
		super.onEnable();
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		System.out.println("[" + NAME + "] Plugin is stopping.");
		saveConfig();
		Bukkit.getScheduler().cancelTask(scheduler);
		System.out.println("[" + NAME + "] Plugin stopped.");
		super.onDisable();
	}
	
	public String getMessage(String name) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + name));
	}
}