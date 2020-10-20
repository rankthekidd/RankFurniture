package com.ryan.rankfurniture.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
public class FurnClickEvent extends Event implements Cancellable{

	
	private boolean isCancelled;
	private Player player;
	private String furnID;
	private String permission;
	private Location location;
	private String[] tags;
	private ItemStack itemStack;
	private Integer[] modelDatas;
	private int index;
	
	public FurnClickEvent(Player player, String furnID, String permission, Location location, String[] tags, ItemStack itemStack, Integer[] modelDatas, int index) {
		isCancelled = false;
		this.setPlayer(player);
		this.setFurnID(furnID);
		this.setPermission(permission);
		this.setLocation(location);
		this.setTags(tags);
		this.setItemStack(itemStack);
		this.setModelDatas(modelDatas);
		this.setIndex(index);
		
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		isCancelled = arg0;
		
	}
	
	
	
    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getFurnID() {
		return furnID;
	}

	public void setFurnID(String furnID) {
		this.furnID = furnID;
	}


	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Integer[] getModelDatas() {
		return modelDatas;
	}

	public void setModelDatas(Integer[] modelDatas) {
		this.modelDatas = modelDatas;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
