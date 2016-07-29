/**
 * 
 */
package tk.ccbluex.AdvancePing.commands;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
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
import org.bukkit.inventory.meta.SkullMeta;

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
				if(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("@a") || args[0].equalsIgnoreCase("@all")) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
						player.openInventory(getPlayersMenu(1));
					}else
						sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("YouMustBeAPlayer"));
					return true;
				}
				
				
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(offlinePlayer != null) {
					File file = new File(AdvancePing.FILEPATH, "data.yml");
					FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
					
					if(configuration.contains(offlinePlayer.getUniqueId().toString())) {
						int lastPing = configuration.getInt(offlinePlayer.getUniqueId().toString() + ".lastPing");
						List<Integer> pings = configuration.getIntegerList(offlinePlayer.getUniqueId().toString() + ".pings");
						int averagePing = MathUtils.AverageCalculate(pings);
						
						if(sender instanceof Player) {
							Player player = (Player) sender;
							player.openInventory(getPlayerMenu(offlinePlayer, lastPing, averagePing, false, player.hasPermission(AdvancePing.NAME + ".see.ip")));
							return true;
						}
						
						sender.sendMessage("§e--------------- " + AdvancePing.PREFIX + "§e---------------");
						sender.sendMessage("§c" + AdvancePing.advancePing.getMessage("Lastping") + ": " + lastPing);
						sender.sendMessage("§c" + AdvancePing.advancePing.getMessage("Averageping") + ": " + averagePing);
						sender.sendMessage("§e--------------- v" + AdvancePing.VERSION + " §e------------------------");
					}else
						sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("NotInDatabase"));
				}else
					sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("PlayerNotFound"));
			}else
				sender.sendMessage(AdvancePing.PREFIX + "/ping <player>");
		}else
			sender.sendMessage(AdvancePing.PREFIX + AdvancePing.advancePing.getMessage("NoPermissionsToExecute"));
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		
		if(event.getCurrentItem() != null) {
			if(inventory.getName().startsWith(AdvancePing.PREFIX)) {
				event.setCancelled(true);
				
				if(event.getCurrentItem().getItemMeta() == null)
					return;
				
				String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
				if(displayName != null && displayName.equalsIgnoreCase("§c" + AdvancePing.advancePing.getMessage("Close")))
					event.getWhoClicked().closeInventory();
				
				if(displayName != null && displayName.equalsIgnoreCase("§c" + AdvancePing.advancePing.getMessage("Back")))
					event.getWhoClicked().openInventory(getPlayersMenu(1));
				
				if(inventory.getName().equalsIgnoreCase(AdvancePing.PREFIX + " | " + AdvancePing.advancePing.getMessage("Players"))) {
					if(displayName != null && displayName.equalsIgnoreCase("§c" + AdvancePing.advancePing.getMessage("NextPage"))) {
						int page = Integer.parseInt(inventory.getItem(47).getItemMeta().getDisplayName().split(": ")[1]);
						page++;
						event.getWhoClicked().openInventory(getPlayersMenu(page));
					}
					
					if(displayName != null && displayName.equalsIgnoreCase("§c" + AdvancePing.advancePing.getMessage("PreviousPage"))) {
						int page = Integer.parseInt(inventory.getItem(47).getItemMeta().getDisplayName().split(": ")[1]);
						page--;
						if(page == 0)
							page++;
						event.getWhoClicked().openInventory(getPlayersMenu(page));
					}
					
					if(event.getCurrentItem().getType() == Material.SKULL_ITEM) {
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(displayName);
						File file = new File(AdvancePing.FILEPATH, "data.yml");
						FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
						
						if(configuration.contains(offlinePlayer.getUniqueId().toString())) {
							int lastPing = configuration.getInt(offlinePlayer.getUniqueId().toString() + ".lastPing");
							List<Integer> pings = configuration.getIntegerList(offlinePlayer.getUniqueId().toString() + ".pings");
							int averagePing = MathUtils.AverageCalculate(pings);
							event.getWhoClicked().openInventory(getPlayerMenu(Bukkit.getOfflinePlayer(displayName), lastPing, averagePing, true, event.getWhoClicked().hasPermission(AdvancePing.NAME + ".see.ip")));
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private Inventory getPlayerMenu(OfflinePlayer offlinePlayer, int lastPing, int averagePing, boolean fromPlayersMenu, boolean withIP) {
		Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, AdvancePing.PREFIX + " | " + offlinePlayer.getName());
		
		ItemStack playerStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 3);
		ItemMeta playerMeta = playerStack.getItemMeta();
		playerMeta.setDisplayName("§7" + AdvancePing.advancePing.getMessage("Player") + ": " + offlinePlayer.getName());
		playerStack.setItemMeta(playerMeta);
		inventory.setItem(3, playerStack);
		
		ItemStack lastPingStack = new ItemStack(Material.PAPER);
		ItemMeta lastPingMeta = lastPingStack.getItemMeta();
		lastPingMeta.setDisplayName("§e" + AdvancePing.advancePing.getMessage("Lastping") + ": " + lastPing);
		lastPingStack.setItemMeta(lastPingMeta);
		inventory.setItem(0, lastPingStack);
		
		ItemStack averagepingStack = new ItemStack(Material.PAPER);
		ItemMeta averagepingMeta = averagepingStack.getItemMeta();
		averagepingMeta.setDisplayName("§e" + AdvancePing.advancePing.getMessage("Averageping") + ": " + averagePing);
		averagepingStack.setItemMeta(averagepingMeta);
		inventory.setItem(1, averagepingStack);
		
		ItemStack infoStack = new ItemStack(Material.PAPER);
		ItemMeta infoMeta = averagepingStack.getItemMeta();
		infoMeta.setDisplayName("§e" + AdvancePing.advancePing.getMessage("Informations"));
		List<String> informations = new ArrayList<>();
		if(offlinePlayer.isOnline()) {
			Player player2 = Bukkit.getPlayer(offlinePlayer.getName());
			try{
				InetSocketAddress inetAddress = player2.getAddress();
				if(withIP)
					informations.add("§b" + AdvancePing.advancePing.getMessage("IPAdress") + ": " + inetAddress.getHostName());
			}catch(Exception exception) {
				informations.add("§cInformations can't load");
				informations.add("§cExeption: ");
				informations.add(exception.getMessage());
			}
		}else{
			informations.add("§c" + AdvancePing.advancePing.getMessage("informationscantload"));
		}
		infoMeta.setLore(informations);
		infoStack.setItemMeta(infoMeta);
		inventory.setItem(2, infoStack);
		
		ItemStack closeStack = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = closeStack.getItemMeta();
		closeMeta.setDisplayName(fromPlayersMenu ? "§c" + AdvancePing.advancePing.getMessage("Back") : "§c" + AdvancePing.advancePing.getMessage("Close"));
		closeStack.setItemMeta(closeMeta);
		inventory.setItem(4, closeStack);
		
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null) {
				ItemStack darkGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15);
				ItemMeta darkGlassMeta = darkGlass.getItemMeta();
				darkGlassMeta.setDisplayName("§f");
				darkGlass.setItemMeta(darkGlassMeta);
				inventory.setItem(i, darkGlass);
			}
		}
		return inventory;
	}
	
	@SuppressWarnings("deprecation")
	private Inventory getPlayersMenu(int page) {
		Inventory inventory = Bukkit.createInventory(null, 54, AdvancePing.PREFIX + " | " + AdvancePing.advancePing.getMessage("Players"));
		
		ItemStack closeStack = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = closeStack.getItemMeta();
		closeMeta.setDisplayName("§c" + AdvancePing.advancePing.getMessage("Close"));
		closeStack.setItemMeta(closeMeta);
		inventory.setItem(53, closeStack);
		
		ItemStack nextPageStack = new ItemStack(Material.ARROW);
		ItemMeta nextPageMeta = nextPageStack.getItemMeta();
		nextPageMeta.setDisplayName("§c" + AdvancePing.advancePing.getMessage("NextPage"));
		nextPageStack.setItemMeta(nextPageMeta);
		inventory.setItem(45, nextPageStack);
		
		ItemStack previousPageStack = new ItemStack(Material.ARROW);
		ItemMeta previousPageMeta = previousPageStack.getItemMeta();
		previousPageMeta.setDisplayName("§c" + AdvancePing.advancePing.getMessage("PreviousPage"));
		previousPageStack.setItemMeta(previousPageMeta);
		inventory.setItem(46, previousPageStack);
		
		ItemStack pageStack = new ItemStack(Material.PAPER);
		ItemMeta pageMeta = pageStack.getItemMeta();
		pageMeta.setDisplayName("§c" + AdvancePing.advancePing.getMessage("messages.Page") + ": " + page);
		pageStack.setItemMeta(pageMeta);
		inventory.setItem(47, pageStack);
		
		for(int i = 0; i < inventory.getSize() - 9; i++) {
			int Intplayer = ((inventory.getSize() - 10) * page) - i;
			
			Object[] players = Bukkit.getOnlinePlayers().toArray();
			if(players.length <= Intplayer)
				continue;
			Player player2 = (Player) players[Intplayer];
			
			if(player2 != null) {
				ItemStack itemSkull = new ItemStack(Material.SKULL_ITEM);
				SkullMeta skullMeta = (SkullMeta) itemSkull.getItemMeta();
				skullMeta.setOwner(player2.getName());
				skullMeta.setDisplayName(player2.getName());
				itemSkull.setItemMeta(skullMeta);
				inventory.setItem(inventory.getSize() - 10 - i, itemSkull);
			}
		}
		
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null) {
				ItemStack darkGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15);
				ItemMeta darkGlassMeta = darkGlass.getItemMeta();
				darkGlassMeta.setDisplayName("§f");
				darkGlass.setItemMeta(darkGlassMeta);
				inventory.setItem(i, darkGlass);
			}
		}
		
		return inventory;
	}
}