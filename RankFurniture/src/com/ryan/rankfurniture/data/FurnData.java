package com.ryan.rankfurniture.data;

import org.bukkit.Material;

public class FurnData {
	
	private Material item;
	private String furnID;
	private String category;
	private String title;
	private String[] lore;
	private Integer[] modelData;
	private String permission;
	private Long cost = 0L;
	private String[] tags;


	public FurnData(Material item, String furnID, String category, String title, String[] lore, Integer[] modelData, String permission, Long cost, String[] tags) {
		this.setItem(item);
		this.setFurnID(furnID);
		this.setTitle(title);
		this.setLore(lore);
		this.setModelData(modelData);
		this.setPermission(permission);
		this.setCost(cost);
		this.setTags(tags);
	}
	

	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getLore() {
		return lore;
	}
	public void setLore(String[] lore) {
		this.lore = lore;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Material getItem() {
		return item;
	}

	public void setItem(Material item) {
		this.item = item;
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Integer[] getModelData() {
		return modelData;
	}

	public void setModelData(Integer[] modelData) {
		this.modelData = modelData;
	}




	public String getFurnID() {
		return furnID;
	}




	public void setFurnID(String furnID) {
		this.furnID = furnID;
	}




	public String getCategory()
	{
		return category;
	}




	public void setCategory(String category)
	{
		this.category = category;
	}

	
}
