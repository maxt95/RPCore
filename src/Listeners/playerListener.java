package Listeners;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.maxt95.core.RPCore.Main;
import com.maxt95.core.RPCore.PlayerD;

import net.md_5.bungee.api.ChatColor;

public class playerListener implements Listener {
	JavaPlugin plugin;
	ArrayList<Object> doorInfo = new ArrayList<Object>();
	ArrayList<Object> doorBought = new ArrayList<Object>();
	ItemStack item;
	ItemStack close;
	ItemStack[] stack = new ItemStack[2];
	PlayerD p;
	ItemStack itemstack;
	Inventory inv;
	ArrayList<Object> beginnerDrugInfo = new ArrayList<Object>();
	//TextLine textLine;
	//Hologram hologram;
	ArrayList<Player> playerLocations = new ArrayList<Player>();
	ScoreboardManager score;
	Scoreboard board;
	Team t;
	double doorBuyPrice = 1;
	double doorSellPrice = 100;
	public playerListener(JavaPlugin plugin, ItemStack item, ItemStack close, ItemStack itemstack)
	{
		this.plugin = plugin;
		this.item = item;
		this.close = close;
		this.itemstack = itemstack;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.p = new PlayerD();
		playerLocations.add(e.getPlayer());	
		score = Bukkit.getScoreboardManager();
		board = score.getNewScoreboard();
		t = board.registerNewTeam(e.getPlayer().getName());
		t.addEntry(e.getPlayer().getName());
	}
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e)
	{
		 for(int i = 0; i < doorInfo.size(); i++)
		 {
			 Object[] info = (Object[]) doorInfo.get(i);
			 Player p = e.getPlayer();
			 Player player = (Player) info[0];
			 if(p == player)
			 {
				 doorInfo.remove(i);
			 }
		 }
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		Location loc;
		
		loc = e.getTo();
		double locX = loc.getX();
		double locY = loc.getY();
		double locZ = loc.getZ();
		String currentPlayer = e.getPlayer().getName();
		String s = "";
		Location tmp;
		for(int i = 0; i < playerLocations.size(); i++)
		{
			s = playerLocations.get(i).getName();
			
			if(s.equals(currentPlayer))
			{
				playerLocations.set(i, player);
			}
			else {
				tmp = playerLocations.get(i).getLocation();
				double tmpX = tmp.getX();
				double tmpY = tmp.getY();
				double tmpZ = tmp.getZ();
				
				double resultX = Math.abs(tmpX - locX);
				double resultY = Math.abs(tmpY - locY);
				double resultZ = Math.abs(tmpZ - locZ);
				
				if(resultX < 10 || resultY < 3 || resultZ < 10)
				{
					t.addEntry(s);
				}
				else
				{
					t.removeEntry(s);
				}
			}
		}
		
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		Object[] begDrugContents = new Object[5];
		if(e.getBlock().getType() == Material.CAULDRON) { 
			if(Main.permission.playerInGroup(e.getPlayer(), "police") || Main.permission.playerInGroup(e.getPlayer(), "mayor") || Main.permission.playerInGroup(e.getPlayer(), "policechief") 
					|| Main.permission.playerInGroup(e.getPlayer(), "hobo")) {
				e.setCancelled(true);
			}
			else {
				Inventory resultInv = Bukkit.createInventory(null, 9, "Product");
				
				Location loc = e.getBlockPlaced().getLocation();
				System.out.println("block location: " + loc);
				begDrugContents[0] = resultInv;
				begDrugContents[1] = loc;
				begDrugContents[2] = e.getBlock();
				begDrugContents[3] = 0; // amount
				begDrugContents[4] = false; // Counter
				
				beginnerDrugInfo.add(begDrugContents);
			}
			
			
			
		}
		
		
		
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if(e.getBlock().getType() == Material.CAULDRON) {
			Location loc = e.getBlock().getLocation();
			for(int i =0; i < beginnerDrugInfo.size(); i++)
			{
				Object[] info = (Object[]) beginnerDrugInfo.get(i);
				Location tmpLoc = (Location) info[1];
				if(loc == tmpLoc)
				{
					try {
						beginnerDrugInfo.remove(i);
					}
					catch(NullPointerException ex)
					{
						
					}
					
				}
				else
				{
					
				}
			}
		}
		else {
			
		}
	}
	
	@EventHandler
	public void moveItemInInventory(InventoryMoveItemEvent e)
	{
		if(e.getItem().getItemMeta().getDisplayName().equals("Meth"))
		{
			
			e.setCancelled(true);
			e.getSource().clear();
			
			
			System.out.println("Found weed");
		}
	}
	private boolean isOn = false;
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e)
	{
		
		Player currentPlayer = (Player) e.getWhoClicked();	//gets the player
		ItemStack clicked = e.getCurrentItem(); // gets the item they clicked
		Inventory inventory = e.getInventory();
			
		if(inventory.getName().equals(Main.beginnerDrug.getName())){
			Inventory productInv = null;
			System.out.println(clicked.getDurability());
			
			if(clicked.getDurability() == 0 && clicked.getType() == Material.SAPLING) //oak sapling Start machine
			{
					isOn = true;
					Location loc = p.getLocation();
					Location originalLocation = p.getLocation();
					
				    for(int i = 0; i < beginnerDrugInfo.size(); i++)
				    {
				    	Object[] info = (Object[]) beginnerDrugInfo.get(i);
				    	Block block = (Block) info[2];
						Location blockLoc = block.getLocation();
						
						//System.out.println(loc + " : " + blockLoc);
						if(loc.getX() == blockLoc.getX() && loc.getY() == blockLoc.getY() && loc.getZ() == blockLoc.getZ())
						{
							System.out.println("updating new inventory");
							productInv = (Inventory) info[0];
							loc.setX(loc.getX()+0.5);
							loc.setY(loc.getY()+2);
							loc.setZ(loc.getZ()+.5);
						}
				    }
				    e.setCancelled(true);
					createMoney(0, productInv, loc, originalLocation, isOn);
		
			}
			else if(clicked.getDurability() == 1) //spruce sapling Stop machine and collect
			{
				Location loc = p.getLocation();
				for(int i = 0; i < beginnerDrugInfo.size(); i++)
				    {
				    	Object[] info = (Object[]) beginnerDrugInfo.get(i);
				    	Block block = (Block) info[2];
						Location blockLoc = block.getLocation();
						int amount = (int) info[3]; // should get amount made
						
						//System.out.println(loc + " : " + blockLoc);
						if(loc.getX() == blockLoc.getX() && loc.getY() == blockLoc.getY() && loc.getZ() == blockLoc.getZ())
						{
							System.out.println("Withdrawn amount:" + amount);
							Main.economy.depositPlayer(currentPlayer, amount);
							info[4] = true; //reset the counter
						}
						
				    }
				e.setCancelled(true);
			}
			else if(clicked.getType() == Material.COMPASS){
				
				isOn = false;
				e.setCancelled(true);
				
			}
			else {
				
			}
			
		}
		if(inventory.getName().equals(Main.experiencedDrug.getName())){
			System.out.println(clicked.getDurability());
			if(clicked.getDurability() == 2) //
			{
				e.setCancelled(true);
			}
			else if(clicked.getDurability() == 3)
			{
				e.setCancelled(true);
			}
			
		}
		if(inventory.getName().equals(Main.expertDrug.getName())){
			System.out.println(clicked.getDurability());
			if(clicked.getDurability() == 4) //
			{
				e.setCancelled(true);
			}
			else if(clicked.getDurability() == 5)
			{
				e.setCancelled(true);
			}
			
		}

		
		
	}
	int amt1 = 0;
	int amt2 = 0;
	private int money;
	public void createMoney(int type, Inventory inv, Location loc, Location originalLocation, boolean isOn)
	{
		
		//System.out.println(loc + " : " + originalLocation);
		//originalLocation.setX(originalLocation.getX()-.5);
		//originalLocation.setZ(originalLocation.getZ()-.5);

		new BukkitRunnable() {
			int counter = 0;
			Hologram hologram;
			TextLine textLine;
			@Override
			public void run() {
				System.out.println("running");
				if(type == 0) //basic money machine
				{
					for(int i = 0; i < beginnerDrugInfo.size(); i++)
					{
						Object[] info = (Object[]) beginnerDrugInfo.get(i);
				    	Block block = (Block) info[2];
						Location blockLoc = block.getLocation();
						boolean c = (boolean) info[4]; // if true then transafction
						if(originalLocation.getX()-.5 == blockLoc.getX() && originalLocation.getY()-2 == blockLoc.getY() && originalLocation.getZ()-.5 == blockLoc.getZ()){
							if(c) { // if withdrawn
								System.out.println("withdrawn and reset counter");
								info[4] = false;
								
								counter = 0;
								hologram.delete();
							}		
						}
					}	
					if(counter < 101) {
						if (counter == 0){
							hologram = HologramsAPI.createHologram(plugin, loc);
							textLine = hologram.appendTextLine(ChatColor.GREEN + "Amount: " + counter);
							//hologram.clearLines();
						}
						else {
							hologram.clearLines();
							hologram = HologramsAPI.createHologram(plugin, loc);
							textLine = hologram.appendTextLine(ChatColor.GREEN + "Amount: " + counter);
						}
						counter++;
						for(int i = 0; i < beginnerDrugInfo.size(); i++){
							System.out.println("does this work");
							Object[] info = (Object[]) beginnerDrugInfo.get(i);
					    	Block block = (Block) info[2];
							Location blockLoc = block.getLocation();
							System.out.println((originalLocation.getX()-.5) + " " + blockLoc.getX() +" " +  (originalLocation.getY()-2) +" " +  blockLoc.getY() +" " +  (originalLocation.getZ()-.5) +" " +  blockLoc.getZ());
							if(originalLocation.getX()-.5 == blockLoc.getX() && originalLocation.getY()-2 == blockLoc.getY() && originalLocation.getZ()-.5 == blockLoc.getZ()){
								info[3] = counter; //set amount in array
								System.out.println("info[3]: " + info[3]);
							}
						}
					}
					else {
						counter = 0;
						this.cancel();
					}
				}
				else if(type == 1) //experienced money machine
				{
					if(counter < 1001)
					{
						hologram.clearLines();
						hologram = HologramsAPI.createHologram(plugin, loc);
						textLine = hologram.appendTextLine(ChatColor.GREEN + "Amount: " + counter);
						
						counter++;
					}
					else
					{
						counter = 0;
						this.cancel();
					}
				}
				else if(type == 2) //expert money machine
				{
					if(counter < 10001)
					{
						hologram.clearLines();
						hologram = HologramsAPI.createHologram(plugin, loc);
						textLine = hologram.appendTextLine(ChatColor.GREEN + "Amount: " + counter);
						
						counter++;
					}
					else
					{
						this.cancel();
					}
				}
				else {
					
				}
				
			}
		}.runTaskTimer(this.plugin, 0, 100);
		
		
		//hologram = HologramsAPI.createHologram(plugin, loc);
		if(type == 0)
		{
			//BukkitTask task = new MoneyTimer(type, this.plugin, loc).runTaskTimer(this.plugin, 0, 100); // every 5 seconds
		}
		if(type == 1)
		{
			//BukkitTask task = new MoneyTimer(type, this.plugin, loc).runTaskTimer(this.plugin, 0, 100); // every 5 seconds
		}
		if(type == 2)
		{
			//BukkitTask task = new MoneyTimer(type, this.plugin, loc).runTaskTimer(this.plugin, 0, 100); // every 5 seconds
		}

	}
	
	@EventHandler
	public void openInventory(InventoryOpenEvent e)
	{
		if(e.getInventory().getTitle().equals("Machine"))
		{
			
		}
	}
	@EventHandler
	public void playerInteract(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();   // gets the player that interacted
		if(e.getClickedBlock() != null) // if clicked block is not air
		{
			if(e.getClickedBlock().getType() == Material.CAULDRON)  // if the clicked block is a cauldron
			{	
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {		// if it was a right click
					Inventory inv = Main.beginnerDrug;
					p.setLocation(e.getClickedBlock().getLocation());
					player.openInventory(inv);
				}	
			}
			else if(e.getClickedBlock().getType().name().equalsIgnoreCase("wooden_door") && e.getClickedBlock() != null)  // if the clicked block is a door
			{
				Location current = e.getClickedBlock().getLocation();	// get the location of the clicked block
				if(e.getItem() != null)									// check to see if item in hand
				{
					String item = e.getItem().getType().name();
					if(e.getItem().getType() == Material.PAPER) {
						if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							
								BlockState state = e.getClickedBlock().getState();
								MaterialData data = state.getData();
								Openable openable = (Openable) data;
								if(doorInfo.size() > 0) {
									for(int i = 0; i < doorInfo.size(); i++) {
										Object[] info = (Object[]) doorInfo.get(i);
										Player p = (Player) info[0];
										Location loc = (Location) info[1];
										double diff = current.getY() - loc.getY();
										boolean b = true;
										if(diff < 2)
										{
											b = true;
										}
										else {
											b = false;
										}
										if(current.getX() == loc.getX() && b == true && current.getZ() == loc.getZ()) {
											System.out.println(e.getItem().getItemMeta());
											if(e.getItem().getItemMeta().getDisplayName().equals(p.getName()+"'s Warrant")) {
												openable.setOpen(true);
												state.update();
											}
											else {
												e.getPlayer().sendMessage("Warrant does not match owner of door");
										}
									}
								}
							}
							
						}
					}
					else if(item.equalsIgnoreCase("tripwire_hook"))
					{
							if(e.getAction() == Action.LEFT_CLICK_BLOCK)	// if left click door with tripwirehook
							{
								try {														
										BlockState state = e.getClickedBlock().getState();
										MaterialData data = state.getData();
										Openable openable = (Openable) data;
										if(doorInfo.size() > 0)
										{
											for(int i = 0; i < doorInfo.size(); i++)
											{
												Object[] info = (Object[]) doorInfo.get(i);
												Player p = (Player) info[0];
												Location loc = (Location) info[1];
												
												double diff = current.getY() - loc.getY();
												boolean b = true;
												if(diff < 2)
												{
													b = true;
												}
												else {
													b = false;
												}
												if(current.getX() == loc.getX() && b == true && current.getZ() == loc.getZ()) {
													if(player != p && p != null) { // belongs to another player
														if(Main.permission.playerInGroup(e.getPlayer(), "thief")) {
															if(openable.isOpen() == false) {
																e.getPlayer().sendMessage("You are now lockpicking the door");
																new BukkitRunnable() {
																	@Override
																	public void run() {
																		openable.setOpen(true);
																		state.update();
																		this.cancel();
																		
																	}
																}.runTaskTimer(plugin, 0, 100);
															}
															else {
																e.setCancelled(true);
															}
														}
														
													}
													else if(player.getName().equals(p.getName())) // if owner of door
													{
														p.sendMessage("Unlocked Door");
														System.out.println("Unlocked Door: current owner");
														openable.setOpen(true);
														state.update();
													}
													else
													{
														e.setCancelled(true);
													}
												}
											}
										}
										else 
										{
											e.getPlayer().sendMessage("You have used the warrant to get in");
											System.out.println("opened");
											openable.setOpen(true);
											state.update();									
										}			
									}
		
								catch(NullPointerException ex)
								{
									
								}
							}
							else if(e.getAction() == Action.RIGHT_CLICK_BLOCK)  // if right clicked with hook
							{
								BlockState state = e.getClickedBlock().getState();
								MaterialData data = state.getData();
								Openable openable = (Openable) data;
								
								if(openable.isOpen() || openable.isOpen() == false) // if door open 
								{
									for(int i = 0; i < doorBought.size(); i++) {
										
										
										Object[] infoBought = (Object[]) doorBought.get(i);
										Player owner = (Player) infoBought[0];
										Location targetLoc = (Location) infoBought[1];
										
										double diff = current.getY() - targetLoc.getY();
										boolean b = true;
										if(diff < 2)
										{
											b = true;
										}
										else {
											b = false;
										}
										if(current.getX() == targetLoc.getX() && b == true && current.getZ() == targetLoc.getZ()) { // if current location equals the door in array
											if(owner == e.getPlayer()) {
												Object[] info = new Object[2];
												info[0] = e.getPlayer();
												info[1] = current;
												doorInfo.add(info);
												e.getPlayer().sendMessage("Door Locked");
												System.out.println("Registered door to " + e.getPlayer().getName());
												System.out.println("Locked");
											}
											else {
												e.getPlayer().sendMessage("You cannot open someone else's door");
												e.setCancelled(true);
											}
											
										}
										else {
											e.setCancelled(true);
										}
									}
									
									
									
								}
								else {
									e.setCancelled(true);
								}
							}
					}
						else {
						e.setCancelled(true);
					}
				}
				else { //if you don't have an item in your hand
					
					
					if(e.getClickedBlock().getType().name().equalsIgnoreCase("wooden_door"))
					{
						if(e.getAction() == Action.LEFT_CLICK_BLOCK)  // if left click
						{
							e.setCancelled(true);
						}
						else if(e.getPlayer().isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {  // if shift right click
							System.out.println("Trying to buy door");
							Location loc = e.getClickedBlock().getLocation();
							Player p = e.getPlayer();
							boolean justSold = false;
							for(int i = 0; i < doorBought.size(); i++) { // look through list of bought doors
								Object[] info = (Object[]) doorBought.get(i);
								//Object[] info = new Object[2];
								Player owner = (Player) info[0];
								Location targetLoc = (Location) info[1];
								double diff = current.getY() - targetLoc.getY();
								boolean b = true;
								if(diff < 2)
								{
									b = true;
								}
								else {
									b = false;
								}
								if(current.getX() == targetLoc.getX() && b == true && current.getZ() == targetLoc.getZ()) { // if current location equals the door in array
									if(owner == null) { //if not owned by anyone
										double balance = Main.economy.getBalance(p);
										Object[] infoDoor = new Object[2];
										infoDoor[0] = p;
										infoDoor[1] = loc;
										if(balance > doorBuyPrice) {
											Main.economy.withdrawPlayer(p, doorBuyPrice);
											p.sendMessage("You have successfully bought the door.");
											doorBought.add(infoDoor);
											e.setCancelled(true);
										}
										else {
											p.sendMessage("You do not have enough money to buy the door.");
											e.setCancelled(true);
										}
									}
									else if(p != owner) { // if owned by someone else
										p.sendMessage("Someone else currently owns this door!");
									}
									else { // if owned by current player
										doorBought.remove(i);
										p.sendMessage("You have sold the door");
										for(int j = 0; j < doorInfo.size(); j++) { // remove lock info from current player
											Object[] inf = (Object[]) doorInfo.get(j);
											Player pl = (Player) inf[0];
											Location l = (Location) inf[1];
											System.out.println(p + " " + pl + " : " + l + " " + current);
											if(p.getName().equals(pl.getName()) && l.getX() == current.getX() && l.getY() == current.getY() && l.getZ() == current.getZ()) {
												p.sendMessage("Removed Lock");
												doorInfo.remove(j);
											}
											
										}
										Main.economy.depositPlayer(p, doorSellPrice);
										justSold = true;
										e.setCancelled(true);
									}
								}
								
							}
							if(doorBought.size() == 0 && justSold == false) {
								double bal = Main.economy.getBalance(p);
								Object[] infDoor = new Object[2];
								infDoor[0] = p;
								infDoor[1] = loc;
								if(bal > 1) {
									Main.economy.withdrawPlayer(p, 1);
									p.sendMessage("You have successfully bought the door.");
									doorBought.add(infDoor);
									e.setCancelled(true);
								}
								else {
									p.sendMessage("You do not have enough money to buy the door.");
									e.setCancelled(true);
								}
							}
							
							
								
							
						}
						else { // if right click
							BlockState state = e.getClickedBlock().getState();
							MaterialData data = state.getData();
							Openable openable = (Openable) data;
							
							for(int i = 0; i< doorInfo.size(); i++)
							{
								Object[] info = (Object[]) doorInfo.get(i);
								Player p = (Player) info[0];
								Location loc = (Location) info[1];
								System.out.println(current + " : " + loc);
								System.out.println(player + " : " + p);
								double diff = current.getY() - loc.getY();
								boolean b = true;
								if(diff < 2)
								{
									b = true;
								}
								else {
									b = false;
								}
								if(current.getX() == loc.getX() && b == true && current.getZ() == loc.getZ()) {
									if(player != p && p != null) { // if owned by someone else
										
										if(openable.isOpen() == false) // if door is closed and belongs to another player
										{
											System.out.println("Current owner " + p.getName());
											p.sendMessage("Door is locked by another.");
											e.setCancelled(true);
										}
									}
									else if(player.getName().equals(p.getName())) // if owner of door
									{
										if(openable.isOpen() == true) // if door is opened
										{
											doorInfo.remove(i); 				// if closed without a hook, door is unregistered
											openable.setOpen(true);
											state.update();
										}
										else
										{
											e.setCancelled(true);
										}
										
									}
									else
									{
										openable.setOpen(true);
										state.update();
									}
								}
							}
						}
						
						
					}
					
				}
				
				
			}
			else {
				
			}
		}
		
		
		
	

	}
	
}
