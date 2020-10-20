package com.ryan.rankfurniture.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ryan.rankfurniture.RankFurniture;
import com.ryan.rankfurniture.SpawnFurn;
import com.ryan.rankfurniture.data.FurnData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class FurnitureCommand implements CommandExecutor{

	RankFurniture plugin;
	public FurnitureCommand(RankFurniture plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(command.getName().equalsIgnoreCase("furniture")) {
				if(args.length > 0) {
					switch(args[0]) {
					case "spawn":
						if(args.length > 1) {
							if(SpawnFurn.furniture.containsKey(args[1])) {
								plugin.spawnFurn.spawnItem(args[1], p, null);
							} else p.sendMessage("§4This item does not exist."); return true;
						}
						break;
					case "info":
						p.sendMessage("§9RankFurniture §8§l>>§r§a Version: §2" + plugin.getDescription().getVersion() + "\n"
								+ "§aDescription: §2A Furniture Plugin Made Specifically For MOGL");
						if(p.isOp()) {

							final ComponentBuilder msg = new ComponentBuilder();
							for(String key : SpawnFurn.furniture.keySet()) {
								msg.append(ChatColor.GREEN + key.toUpperCase() + ChatColor.WHITE + ", ");
								msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/furniture about " + key));
								msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to see more info about §a" + key.toUpperCase())));

							}
							p.sendMessage("\n§eLoaded Furniture Items §8§l>>§r §4(OP VIEW)");
							p.spigot().sendMessage(msg.create());

						}
						break;
					case "about":
						if(p.isOp()) {
							if(args.length > 1) {
								if(SpawnFurn.furniture.containsKey(args[1])) {
									FurnData fd = SpawnFurn.furniture.get(args[1]);

									String tags = "";		
									for(String line : fd.getTags()) {
										tags += line + ", ";
									}

									p.sendMessage("\n§eInfo about " + args[1].toUpperCase() +
											"\n§r§2Name: " + fd.getTitle() +
											"\n§r§2Permission: §f" + fd.getPermission() + 
											"\n§r§2Cost: §f" + fd.getCost() + 
											"\n§r§2Tags: §f" + tags + "\n");

								}
							}
						}
						break;
					}
				}
			}
		}
		return true;
	}
}

