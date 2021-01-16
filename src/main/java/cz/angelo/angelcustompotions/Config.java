package cz.angelo.angelcustompotions;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Config {

	public static FileConfiguration config = null;
	public static File file = null;

	public static void reload() {
		if (config == null){
			file = new File(Main.instance.getDataFolder(), "config.yml");
		}

		config = YamlConfiguration.loadConfiguration(file);

		Reader defConfigStream = null;

		if (!file.exists()){
			Main.instance.saveResource("config.yml", false);
		}

		try {
			defConfigStream = new InputStreamReader(Main.instance.getResource("config.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (defConfigStream != null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	public static FileConfiguration get() {
		if (config == null){
			reload();
		}
		return config;
	}

	public static void save(){
		if (config == null || file == null){
			return;
		}
		try {
			get().save(file);
		}catch (IOException e){
		}
	}

}
