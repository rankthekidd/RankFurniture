package com.ryan.rankfurniture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.ryan.rankfurniture.commands.FurnitureCommand;
import com.ryan.rankfurniture.data.FurnData;
import com.ryan.rankfurniture.data.FurnLoader;

public class RankFurniture extends JavaPlugin{

	//All them public things that need to accessed elsewhere
	//public Map<String, ItemStack> furnList = new HashMap<String, ItemStack>();

	//My classes that need Main.java
	public SpawnFurn spawnFurn = new SpawnFurn(this);
	public FurnManager furnManager = new FurnManager(this);
	public FurnitureCommand command = new FurnitureCommand(this);
	public FurnLoader furnLoader = new FurnLoader(this);


	public void onEnable() {

		furnLoader.loadFurniture();

		//register commands here
		getCommand("furniture").setExecutor(command);

		//register listeners here
		Bukkit.getServer().getPluginManager().registerEvents(spawnFurn, this);
		Bukkit.getServer().getPluginManager().registerEvents(furnManager, this);

		Bukkit.getConsoleSender().sendMessage("\r\n" + 
				" §6 _____  §a______ \r\n" + 
				" §6|  __ \\§a|  ____|\r\n" + 
				" §6| |__) §a| |__   " + "     [" + getDescription().getName() + "] " + getDescription().getName() + "\r\n" +
				" §6|  _  /§a|  __|  " + "     V" + getDescription().getVersion() + " has been enabled successfully!\r\n" +
				" §6| | \\ §a\\| |     \r\n" + 
				" §6|_|  \\_§a\\_|     \r\n");
	}

	public void onDisable() {
		
		Bukkit.getServer().broadcastMessage("§6Rank Furniture is Disabled!");
	}




	public static ItemStack setCustomModelData(ItemStack i, int modelData)
	{
		ItemStack item = new ItemStack(i);
		if(item != null)
		{
			if(item.getType() != Material.AIR)
			{
				ItemMeta m = item.getItemMeta();
				m.setCustomModelData(modelData);
				item.setItemMeta(m);
			}
		}

		return item;
	}

	public static ItemStack itemMaker(int count, boolean hideAttributes, boolean glowing, Material material, String name, String[] lore, int... data)
	{
		ItemStack item = new ItemStack(material, count);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(name);
		List<String> newLore = new ArrayList<String>();
		for(String line : lore)
		{
			newLore.add(line);
		}
		m.setLore(newLore);

		if(hideAttributes)
		{
			m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		else
		{
			if(m.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
			{
				m.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
		}

		if(glowing)
		{
			m.addEnchant(Enchantment.KNOCKBACK, 1, true);
			m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		else
		{
			if(m.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
			{
				m.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
		}

		item.setItemMeta(m);

		if(data != null)
		{
			if(data.length > 0)
			{
				if(data[0] != 0)
				{
					item = RankFurniture.setCustomModelData(item, data[0]);
				}
			}
		}

		return item;
	}

	public static Map<String, FurnData> getFurnItems() {
		return SpawnFurn.furniture; 
	}

	public String locationToString(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getPitch() + "," + l.getYaw();
	}
}
