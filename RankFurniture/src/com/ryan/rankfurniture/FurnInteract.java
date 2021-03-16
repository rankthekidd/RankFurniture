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


public class FurnInteract implements Listener{

	RankFurniture plugin;
	public FurnInteract(RankFurniture plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void clickFurnitureStand(PlayerArmorStandManipulateEvent e) {
		if(e.getRightClicked().getScoreboardTags().contains("furniture")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void furnInteract(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();

		if(action.name().contains("_CLICK_BLOCK")) {
			if(e.getHand() == EquipmentSlot.HAND &&
					e.getClickedBlock().getType().equals(Material.BARRIER)) {
				
				//These had to be declared down here after the check to see if this is a furniture item.
				Block block = e.getClickedBlock();
				Location blockLoc = getBlockLocation(block, p);

				//These had to be declared out of the loop. The check to see if they are null is located in the loop itself.
				ArmorStand stand = null;
				FurnData fd = null;
				ItemStack onHead = null;

				List<Entity> nearbyEnt = (List<Entity>) blockLoc.getWorld().getNearbyEntities(blockLoc, 0.5, 0.5, 0.5);
				for(Entity ent : nearbyEnt) {
					if(ent.getType().equals(EntityType.ARMOR_STAND)) {
						stand = (ArmorStand) ent;
						onHead = stand.getEquipment().getHelmet();
						if(onHead == null) continue;
						
						fd = getFurnDataFromItemStack(onHead);
						break;
					}
				}
				
				if(stand == null) return;
				//All furniture armor stands are tagged with "furniture." This is easy to see if the given furniture item 
				if(!stand.getScoreboardTags().contains("furniture")) return;
				
				if(fd == null) return;

				if (p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SPECTATOR)) return; 

				// Right Click w/ sneaking is PICKING UP the furniture
				if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
					if(p.isSneaking()) {
						FurnPickupEvent fpe = new FurnPickupEvent(p, fd.getFurnID(), fd.getPermission(), blockLoc);
						Bukkit.getPluginManager().callEvent(fpe);
						if(fpe.isCancelled()) return;

						stand.remove();
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						String message = ("§7You have picked up " + fd.getTitle());
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
						block.getLocation().getWorld().spawnParticle(Particle.SPELL_INSTANT, blockLoc, 20);
						block.getLocation().getWorld().getBlockAt(block.getLocation()).setType(Material.AIR);
						e.setCancelled(true);

						// Right Click w/o sneaking is CLICKING the furniture
					}else {
						int index = 0;
						for(int i = 0; i < fd.getModelData().length; i++) {
							if(onHead.getItemMeta().getCustomModelData() == fd.getModelData()[i]) {
								index = i;
							}
						}
						FurnClickEvent fce = new FurnClickEvent(p, fd.getFurnID(), fd.getPermission(), blockLoc, fd.getTags(), onHead, fd.getModelData(), index);
						Bukkit.getPluginManager().callEvent(fce);
						if(fce.isCancelled()) return;
						stand.getEquipment().setHelmet(onHead = RankFurniture.setCustomModelData(onHead, fd.getModelData()[fce.getIndex()]));
					}

					//Left click is ROTATING the furniture
				} else if(action.equals(Action.LEFT_CLICK_BLOCK)) {
					FurnRotateEvent fre = new FurnRotateEvent(p, fd.getFurnID(), fd.getPermission(), blockLoc);
					Bukkit.getPluginManager().callEvent(fre);
					if(fre.isCancelled()) return;
					int rotation = p.isSneaking() ? 45 : 15;
					stand.setRotation(stand.getLocation().getYaw() + rotation, 0);
					e.setCancelled(true);
				}
			}
		} else return;
	}

	public Location getBlockLocation(Block block, Player p) {
		World world = p.getWorld();
		double x = block.getLocation().getX()+0.5;
		double y = block.getLocation().getY()+0.5;
		double z = block.getLocation().getZ()+0.5;

		Location blockLoc = new Location(world, x, y, z);
		return blockLoc;
	}

	public FurnData getFurnDataFromItemStack(ItemStack onHead) {
		if(!onHead.hasItemMeta()) return null;
		if(!onHead.getItemMeta().hasCustomModelData()) return null;
		for(String key : SpawnFurn.furniture.keySet()) {
			FurnData fd = SpawnFurn.furniture.get(key);
			for(int i = 0; i < fd.getModelData().length; i++) {
				if(onHead.getItemMeta().getCustomModelData() == fd.getModelData()[i] && onHead.getType() == fd.getItem()) {
					return fd;
				}
			}
		}
		return null;
	}
}
