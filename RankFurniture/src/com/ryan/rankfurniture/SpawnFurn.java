package com.ryan.rankfurniture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.ryan.rankfurniture.api.FurnPlaceEvent;
import com.ryan.rankfurniture.data.FurnData;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
public class SpawnFurn implements Listener {

	RankFurniture plugin;
	public static Map<String, FurnData> furniture = new HashMap<String, FurnData>();

	public SpawnFurn(RankFurniture plugin) {
		this.plugin = plugin;

	}

	//=====================================================================================================================================================//
	// 														SpawnFurn Functionality																		// 
	//=====================================================================================================================================================//

	//spawns the select item at the player's current location
	public void spawnItem(String furnID, Player p, String name) {

		FurnPlaceEvent fe = new FurnPlaceEvent(p, furnID, furniture.get(furnID).getPermission(), furniture.get(furnID).getCost(), p.getLocation());
		Bukkit.getPluginManager().callEvent(fe);
		if(fe.isCancelled()) return;

		FurnData fd = furniture.get(furnID);
		ItemStack furn = RankFurniture.itemMaker(1, true, false, fd.getItem(), fd.getTitle(), fd.getLore(), fd.getModelData()[0]);

		//gets the user's location and adds .5 to x and y to make it in the center of the block.
		World world = p.getWorld();
		double x = p.getLocation().getBlock().getLocation().getX()+0.5;
		double y = p.getLocation().getBlock().getLocation().getY();
		double z = p.getLocation().getBlock().getLocation().getZ()+0.5;

		Location loc;
		ArmorStand stand;

		loc = new Location(world, x, y, z);

		//Checks to make sure there are no nearby entities that could mess up thefurniture being placed. Also prevents multiple furniture pieces in the samelocation. 
		List<Entity> nearbyEnt = (List<Entity>)p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 0.5, 0.5, 0.5);
		for(int i = 0; i < nearbyEnt.size(); i++) {
			if(nearbyEnt.get(i).getType().equals(EntityType.ARMOR_STAND)) {
				ArmorStand nearbyStand = (ArmorStand) nearbyEnt.get(i);
				if(nearbyStand.isSmall() && !nearbyStand.isVisible()) {
					nearbyEnt.remove(i);
				}
			}
		}
		if(nearbyEnt.size() > 1) {
			p.sendMessage("§cYou cannot place that furniture here.");
			return;
		}

		if(!loc.getBlock().getType().equals(Material.AIR)) { 
			p.sendMessage("§cYou cannot place that furniture here.");
			return;
		}

		if(!loc.getBlock().getRelative(0, 1, 0).getType().equals(Material.AIR)) {
			p.sendMessage("§cYou cannot place that furniture here.");
			return;
		}

		if(p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SPECTATOR)) return;

		world.getBlockAt(loc).setType(Material.BARRIER); //sets barrier at location

		stand = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND); //spawns armor stand
		stand.setVisible(false);
		stand.setGravity(false);
		stand.setSmall(true);
		stand.setSilent(true);
		if(name != null) {
			stand.setCustomName(name);
			stand.setCustomNameVisible(true);
		}
		// "Put the little thingy on the thing man eh"  -Boomhauer 
		stand.getEquipment().setHelmet(furn);
		
		String message = ("§7You have placed " + fd.getTitle());
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));


	}
}
