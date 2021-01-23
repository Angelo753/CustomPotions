package cz.angelo.angelcustompotions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

	public static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		Config.reload();
	}

	@Override
	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("apotions")){
			if (sender.hasPermission("*") || sender.isOp() || sender.hasPermission("apotions.admin")){
				if (args.length > 0){
					if (args.length == 4){
						if (args[0].equalsIgnoreCase("give")) {
							Player target = Bukkit.getPlayer(args[3]);
							if (target != null) {
								if (Config.get().getString("potions." + args[1]) != null) {
									target.getInventory().addItem(this.createPotion(args[1], Integer.parseInt(args[2])));
									sender.sendMessage(this.color(Config.get().getString("messages.give")).replace("{PLAYER}", args[3]).replace("{AMOUNT}", args[2]).replace("{POTION}", args[1]));
									target.sendMessage(this.color(Config.get().getString("messages.giveTarget")).replace("{AMOUNT}", args[2]).replace("{POTION}", args[1]));
								} else {
									sender.sendMessage(this.color(Config.get().getString("messages.potionDoesntExists")));
								}
							}else {
								sender.sendMessage(this.color(Config.get().getString("messages.playerIsOffline")).replace("{PLAYER}", args[3]));
							}
						}else {
							sender.sendMessage(this.color(Config.get().getString("messages.arguments")));
						}
					}else {
						sender.sendMessage(this.color(Config.get().getString("messages.arguments")));
					}
				}else {
					sender.sendMessage(this.color(Config.get().getString("messages.arguments")));
				}
			}else {
				sender.sendMessage(this.color(Config.get().getString("messages.permission")));
			}
		}
		return false;
	}

	public ItemStack createPotion(String potion, int amount){
		ItemStack itemStack = new ItemStack(Material.POTION, amount);
		PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
		List<String> lore = new ArrayList<>();
		for (String lores : Config.get().getStringList("potions." + potion + ".lore")){
			lore.add(this.color(lores));
		}
		for (String effects : Config.get().getStringList("potions." + potion + ".effects")){
			String[] effect = effects.split(";");
			PotionEffectType potionEffectType = PotionEffectType.getByName(effect[0].toUpperCase());
			int level = Integer.parseInt(effect[1]);
			PotionEffect potionEffect = new PotionEffect(potionEffectType, Integer.parseInt(effect[2]) * 20, level, true);
			potionMeta.addCustomEffect(potionEffect, true);
		}
		potionMeta.setLore(lore);
		potionMeta.setDisplayName(this.color(Config.get().getString("potions." + potion + ".name")));
		PotionType potionType = PotionType.valueOf(Config.get().getString("potions." + potion + ".type"));
		potionMeta.setBasePotionData(new PotionData(potionType, false, false));
		itemStack.setItemMeta(potionMeta);
		return itemStack;
	}

	public String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
