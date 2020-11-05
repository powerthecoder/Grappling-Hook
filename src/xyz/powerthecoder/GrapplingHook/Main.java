package xyz.powerthecoder.GrapplingHook;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	// CoolDown HashMap
	Map<String, Long> cooldowns = new HashMap<String, Long>();
	
	@Override
	public void onEnable() {
		Bukkit.addRecipe(getRecipe());
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		//Stop
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Hook
		if (label.equalsIgnoreCase("hook")) {
			if (!(sender instanceof Player)) {
				//Console
				sender.sendMessage("You need to be in game my guy");
				return true;
			}
			//Player
			Player player = (Player) sender;
			if (player.getInventory().firstEmpty() == -1) {
				// inv is full
				player.sendMessage("No room in your inventory");
			}
			// inv is not full
			player.getInventory().addItem(getItem());
			player.sendMessage(ChatColor.GOLD + "You have been given a grappling hook");
			return true;
		}
		return false;
	}
	
	public ShapedRecipe getRecipe() {
		// Custom Crafting Table
		ItemStack hook = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = hook.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Grappling Hook");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		hook.setItemMeta(meta);
		
		NamespacedKey key = new NamespacedKey(this, "grappling_hook");
		ShapedRecipe recipe = new ShapedRecipe(key, hook);
		recipe.shape("III", " S ", " W ");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('W', Material.STICK);
		return recipe;
	}
	
	
	public ItemStack getItem() {
		// Create Grappling Hook
		ItemStack hook = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = hook.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "Grappling Hook");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		hook.setItemMeta(meta);
		return hook;
	}
	

	@EventHandler()
	public void onPunch(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// On Click and check if item is hook
		if (player.getEquipment().getItemInMainHand().hasItemMeta() && player.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Grappling Hook") && player.getEquipment().getItemInMainHand().getType().equals(Material.IRON_PICKAXE)){
			// Cooldown
			if (cooldowns.containsKey(player.getName())) {
				// Player is inside HashMap
				if (cooldowns.get(player.getName()) > System.currentTimeMillis()) {
					// still on cooldown
					long timeleft = (cooldowns.get(player.getName()) - System.currentTimeMillis() / 1000);
					player.sendMessage(ChatColor.RED + "Hook ready in " + timeleft + " second(s)");
					return;
				}
			}
			cooldowns.put(player.getName(), System.currentTimeMillis() + (5 * 1000));
			
			// Teleport
			Block block = player.getTargetBlock(null, 10);
			Location loc = block.getLocation();
			player.teleport(loc);
		}
	}
}



















