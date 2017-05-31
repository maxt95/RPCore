package com.maxt95.core.RPCore;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import Listeners.playerListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


public class Main extends JavaPlugin {
	
	public static Inventory beginnerDrug;
	public static Inventory experiencedDrug;
	public static Inventory expertDrug;
	public static Inventory beginnerPrinter;
	public static Inventory experiencedPrinter;
	public static Inventory expertPrinter;
	public static Economy economy = null;
	public static Permission permission = null;
	
	public void onEnable() {
		
		setupEconomy();
		setupPermissions();
		playerListener pl;
		//ArrayList<Object> doorInfo = new ArrayList<Object>();
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add("Ingredients:");
		lore.add("Apple x1");
		lore.add("Sugar x1");
		
		ItemStack itemstack = new ItemStack(Material.ANVIL);
		ItemMeta metas = itemstack.getItemMeta();
		metas.setDisplayName("Results");
		itemstack.setItemMeta(metas);
		
		ItemStack item = new ItemStack(Material.YELLOW_FLOWER);
		ItemStack close = new ItemStack(Material.COMPASS);
		
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName("Close");
		close.setItemMeta(closeMeta);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Meth");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		createDrugInventoryGUI();
		createBeginnerDrugContents();
		createExperiencedDrugContents();
		createExpertDrugContents();
		
		getServer().getPluginManager().registerEvents(pl = new playerListener(this, item, close, itemstack), this);
		
	}
	public void onDisable() {
		
	}
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	public void createDrugInventoryGUI()
	{
		beginnerDrug = Bukkit.createInventory(null, 9, "Simple Drug");
		experiencedDrug = Bukkit.createInventory(null, 9, "Experienced Drug");
		expertDrug = Bukkit.createInventory(null, 9, "Expert Drug");
		
		
	}

	public void createBeginnerDrugContents()
	{
		beginnerDrug.setItem(0, new ItemStack(Material.SAPLING, 1));
		beginnerDrug.setItem(1, new ItemStack(Material.SAPLING, 1, (short) 1));
		//beginnerDrug.setItem(2, new ItemStack(Material.COMPASS, 1));
		
	}
	public void createExperiencedDrugContents()
	{
		experiencedDrug.setItem(0, new ItemStack(Material.SAPLING, 1, (short) 2));
		experiencedDrug.setItem(1, new ItemStack(Material.SAPLING, 1, (short) 3));
	}
	public void createExpertDrugContents()
	{
		expertDrug.setItem(0, new ItemStack(Material.SAPLING, 1, (short) 4));
		expertDrug.setItem(1, new ItemStack(Material.SAPLING, 1, (short) 5));
	}
}
