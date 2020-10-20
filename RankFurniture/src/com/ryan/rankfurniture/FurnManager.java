package com.ryan.rankfurniture;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.ryan.rankfurniture.api.FurnClickEvent;
import com.ryan.rankfurniture.api.FurnPickupEvent;
import com.ryan.rankfurniture.api.FurnRotateEvent;
import com.ryan.rankfurniture.data.FurnData;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class FurnManager implements Listener{

	RankFurniture plugin;

	public FurnManager(RankFurniture plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void manipulate(PlayerArmorStandManipulateEvent e) {
		if (!e.getRightClicked().isVisible() && 
				e.getRightClicked().isSmall())
			e.setCancelled(true); 
	}


	@EventHandler
	public void playerInteract(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		Block block = e.getClickedBlock();
		if (p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SPECTATOR)) return; 
		if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.isSneaking()) { 
				if(e.getHand() == EquipmentSlot.HAND && 
						block.getType().equals(Material.BARRIER)) {
					Location blockLoc = getBlockLocation(block, p);
					List<Entity> nearbyEnt = (List<Entity>) blockLoc.getWorld().getNearbyEntities(blockLoc, 0.5, 0.5, 0.5);
					for(Entity ent : nearbyEnt) {
						if(ent.getType().equals(EntityType.ARMOR_STAND)) {
							ArmorStand stand = (ArmorStand) ent;
							if(stand.isVisible()) return;
							ItemStack onHead = stand.getEquipment().getHelmet();
							for(String key : SpawnFurn.furniture.keySet())
							{
								FurnData fd = SpawnFurn.furniture.get(key);
								for(int i = 0; i < fd.getModelData().length; i++) {
									if(onHead.getItemMeta().getCustomModelData() == fd.getModelData()[i] && onHead.getType() == fd.getItem())
									{
										FurnPickupEvent fpe = new FurnPickupEvent(p, key, fd.getPermission(), blockLoc);
										Bukkit.getPluginManager().callEvent(fpe);
										if(fpe.isCancelled()) return;

										stand.remove();
										p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
										String message = ("§7You have picked up " + fd.getTitle());
										p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
										block.getLocation().getWorld().spawnParticle(Particle.SPELL_INSTANT, blockLoc, 20);
										block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
										e.setCancelled(true);
										break;
									}
								}
							}
						}
					}
				} 
			} else if(!p.isSneaking()) {
				if(e.getHand() == EquipmentSlot.HAND && 
						block.getType().equals(Material.BARRIER)) {
					Location blockLoc = getBlockLocation(block, p);
					List<Entity> nearbyEnt = (List<Entity>) blockLoc.getWorld().getNearbyEntities(blockLoc, 0.5, 0.5, 0.5);
					for(Entity ent : nearbyEnt) {
						if(ent.getType().equals(EntityType.ARMOR_STAND)) {
							ArmorStand stand = (ArmorStand) ent;
							if(stand.isVisible()) return;
							if(stand.getEquipment().getHelmet() == null) return;
							ItemStack onHead = stand.getEquipment().getHelmet();
							for(String key : SpawnFurn.furniture.keySet())
							{
								FurnData fd = SpawnFurn.furniture.get(key);
								for(int i = 0; i < fd.getModelData().length; i++) {
									if(onHead.getItemMeta().getCustomModelData() == fd.getModelData()[i] && onHead.getType() == fd.getItem())
									{
										FurnClickEvent fce = new FurnClickEvent(p, key, fd.getPermission(), blockLoc, fd.getTags(), onHead, fd.getModelData(), i);
										Bukkit.getPluginManager().callEvent(fce);
										if(fce.isCancelled()) return;
										stand.getEquipment().setHelmet(onHead = RankFurniture.setCustomModelData(onHead, fd.getModelData()[fce.getIndex()]));
										break;
									}
								}
							}
						}
					}
				} 
			}
		} else if (action.equals(Action.LEFT_CLICK_BLOCK) && 
				e.getHand() == EquipmentSlot.HAND && 
				block.getType().equals(Material.BARRIER)) {
			Location blockLoc = getBlockLocation(block, p);
			List<Entity> nearbyEnt = (List<Entity>) blockLoc.getWorld().getNearbyEntities(blockLoc, 0.5, 0.5, 0.5);
			for(Entity ent : nearbyEnt) {
				if(ent.getType().equals(EntityType.ARMOR_STAND)) {
					ArmorStand stand = (ArmorStand) ent;
					if(stand.isVisible()) return;
					ItemStack onHead = stand.getEquipment().getHelmet();
					for(String key : SpawnFurn.furniture.keySet())
					{
						FurnData fd = SpawnFurn.furniture.get(key);
						for(int i = 0; i < fd.getModelData().length; i++) {
							if(onHead.getItemMeta().getCustomModelData() == fd.getModelData()[i] && onHead.getType() == fd.getItem())
							{
								FurnRotateEvent fre = new FurnRotateEvent(p, key, fd.getPermission(), blockLoc);
								Bukkit.getPluginManager().callEvent(fre);
								if(fre.isCancelled()) return;
								int rotation = p.isSneaking() ? 45 : 15;
								stand.setRotation(stand.getLocation().getYaw() + rotation, 0);
								e.setCancelled(true);
								break;
							}
						}
					}
				}
			}
		} 
	}




	public Location getBlockLocation(Block block, Player p) {
		World world = p.getWorld();
		double x = block.getLocation().getX()+0.5;
		double y = block.getLocation().getY()+0.5;
		double z = block.getLocation().getZ()+0.5;

		Location blockLoc = new Location(world, x, y, z);
		return blockLoc;
	}
}
