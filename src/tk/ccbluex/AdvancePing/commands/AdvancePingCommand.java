/**
 * 
 */
package tk.ccbluex.AdvancePing.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tk.ccbluex.AdvancePing.AdvancePing;
import tk.ccbluex.AdvancePing.utils.MathUtils;

/**
 * @author Marco
 *
 */
public class AdvancePingCommand implements CommandExecutor, Listener {
	
	/**
	 * 
	 */
	public AdvancePingCommand() {
		Bukkit.getPluginManager().registerEvents(this, AdvancePing.advancePing);
	}
	
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
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, AdvancePing.PREFIX + " | " + offlinePlayer.getName());
							ItemStack lastPingStack = new ItemStack(Material.PAPER);
							ItemMeta lastPingMeta = lastPingStack.getItemMeta();
							lastPingMeta.setDisplayName("§eLast Ping: " + lastPing);
							lastPingStack.setItemMeta(lastPingMeta);
							inventory.addItem(lastPingStack);
							
							ItemStack averagepingStack = new ItemStack(Material.PAPER);
							ItemMeta averagepingMeta = averagepingStack.getItemMeta();
							averagepingMeta.setDisplayName("§eAverage Ping: " + average);
							averagepingStack.setItemMeta(averagepingMeta);
							inventory.addItem(averagepingStack);
							
							ItemStack closeStack = new ItemStack(Material.BARRIER);
							ItemMeta closeMeta = closeStack.getItemMeta();
							closeMeta.setDisplayName("§cClose");
							closeStack.setItemMeta(closeMeta);
							inventory.setItem(inventory.getSize() - 1, closeStack);
							
							for(int i = 0; i < inventory.getSize(); i++) {
								if(inventory.getItem(i) == null) {
									ItemStack darkGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15);
									ItemMeta darkGlassMeta = darkGlass.getItemMeta();
									darkGlassMeta.setDisplayName("§f");
									darkGlass.setItemMeta(darkGlassMeta);
									inventory.setItem(i, darkGlass);
								}
							}
							
							player.openInventory(inventory);
							return true;
						}
						
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
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		
		if(event.getCurrentItem() != null) {
			if(inventory.getName().startsWith(AdvancePing.PREFIX)) {
				event.setCancelled(true);
				String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
				if(displayName != null && displayName.equalsIgnoreCase("§cClose")) {
					event.getWhoClicked().closeInventory();
				}
			}
		}
	}
}