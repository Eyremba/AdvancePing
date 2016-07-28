/**
 * 
 */
package tk.ccbluex.AdvancePing;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import tk.ccbluex.AdvancePing.commands.AdvancePingCommand;

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
		configuration.options().copyDefaults(true);
		saveConfig();
		
		scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PingReceiver(), 0, configuration.getInt("checkPerTicks"));
		getCommand("ping").setExecutor(new AdvancePingCommand());
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
}