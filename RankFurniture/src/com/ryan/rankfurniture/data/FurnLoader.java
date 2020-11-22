package com.ryan.rankfurniture.data;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import com.ryan.rankfurniture.RankFurniture;
import com.ryan.rankfurniture.SpawnFurn;

public class FurnLoader {

	RankFurniture plugin;
	public FurnLoader(RankFurniture plugin) {
		this.plugin = plugin;
	}


	private String color(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	private List<String> color(List<String> lore){
		List<String> clore = new ArrayList<>();
		for(String s : lore){
			clore.add(color(s));
		}
		return clore;
	}

	public void loadFurniture()
	{
		plugin.saveDefaultConfig();
		SpawnFurn.furniture.clear();
		if(plugin.getConfig().contains("furniture"))
		{
			for(String key : plugin.getConfig().getConfigurationSection("furniture").getKeys(false))
			{
				FurnData fd = new FurnData(Material.DIRT, "error" , "error", new String[] {"broken?"}, new Integer[] {0000}, "error", 0L, new String[] {"error"});

				//Item
				fd.setItem(Material.getMaterial(plugin.getConfig().getString("furniture." + key + ".item")));

				//ID
				fd.setFurnID(key);
				
				//Title
				fd.setTitle(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("furniture." + key + ".title")));

				//Lore
				List<String> configLore = color(plugin.getConfig().getStringList("furniture." + key + ".lore"));
				String[] lore = new String[configLore.size()];
				lore = configLore.toArray(lore);
				fd.setLore(lore);

				//Model Data List
				List<Integer> configModelData = (plugin.getConfig().getIntegerList("furniture." + key + ".modelData"));
				Integer[] modelData = new Integer[configModelData.size()];
				modelData = configModelData.toArray(modelData);
				fd.setModelData(modelData);

				//Permissions
				fd.setPermission(plugin.getConfig().getString("furniture." + key + ".permission"));

				//Cost
				fd.setCost(plugin.getConfig().getLong("furniture." + key + ".cost"));

				//Tags (for custom furniture possibities)
				List<String> configTags = plugin.getConfig().getStringList("furniture." + key + ".tags");
				String[] tags = new String[configTags.size()];
				tags = configTags.toArray(tags);
				fd.setTags(tags);

				SpawnFurn.furniture.put(key, fd);
			}
		}
		plugin.saveConfig();
	}
}
