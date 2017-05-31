package com.maxt95.core.RPCore;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class PlayerD {
	ArrayList<ItemStack> drugs = new ArrayList<ItemStack>();
	Location location;
	public PlayerD()
	{
		
	}
	
	public void setInventories()
	{
		
	}
	public ArrayList<ItemStack> getDrugs()
	{
		return drugs;
	}
	public void setDrugs(int d1, int d2)
	{
		
	}
	public void addDrugs(ItemStack item)
	{
		drugs.add(item);
	}
	public void setLocation(Location l)
	{
		location = l;
	}
	public Location getLocation()
	{
		return location;
	}
}
