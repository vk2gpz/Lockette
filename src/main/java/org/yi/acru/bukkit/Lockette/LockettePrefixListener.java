//
// This file is a component of Lockette for Bukkit, and was written by Acru Jovian.
// Distributed under the The Non-Profit Open Software License version 3.0 (NPOSL-3.0)
// http://www.opensource.org/licenses/NOSL3.0
//


package org.yi.acru.bukkit.Lockette;

// Imports.

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;



public class LockettePrefixListener implements Listener{
	private static Lockette		plugin;
	
	
	public LockettePrefixListener(Lockette instance){
		plugin = instance;
	}
	
	
	protected void registerEvents(){
		PluginManager	pm = plugin.getServer().getPluginManager();
		
		pm.registerEvents(this, plugin);
	}
	
	
	//********************************************************************************************************************
	// Start of event section
	
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event){
		if (Lockette.DEBUG) {
			Lockette.log.info("[Lockette] onSignChange (LockettePrefixListenre)");
		}


		Block		block = event.getBlock();
		Player		player = event.getPlayer();
		int         blockType = block.getTypeId();
		boolean		typeWallSign = (blockType == Material.WALL_SIGN.getId());
		boolean		typeSignPost = (blockType == Material.SIGN_POST.getId());
		
		// Check to see if it is a sign change packet for an existing protected sign.
		// No longer needed in builds around 556+, but I am leaving this here for now.
		// Needed again as of build 1093...  :<

		if(typeWallSign){
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0));
			
			if((text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate) || text.equalsIgnoreCase("[More Users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) && LocketteBlockListener.isEmptyChange(event)) {
				// Okay, sign already exists and someone managed to send an event to replace.
				// Cancel it!  Also, set event text to sign text, just in case.
				// And check for this later in queue.
				if (Lockette.DEBUG) {
					Lockette.log.info("[Lockette] Sign already exists, resetting");
				}

				event.setCancelled(true);
				event.setLine(0, sign.getLine(0));
				event.setLine(1, sign.getLine(1));
				event.setLine(2, sign.getLine(2));
				event.setLine(3, sign.getLine(3));
				Lockette.log.info("[" + plugin.getDescription().getName() + "] " + player.getName() + " just tried to change a non-editable sign. (Bukkit bug, or plugin conflict?)");
				return;
			}
		}
		else if(typeSignPost){
			
		}
		else{
			// Not a sign, wtf!
			event.setCancelled(true);
			Lockette.log.info("[" + plugin.getDescription().getName() + "] " + player.getName() + " just tried to set text for a non-sign. (Bukkit bug, or hacked client?)");
			return;
		}


		//// Alternative: Enforce a blank sign, as bukkit catches spoofed packets now.
		//// No longer needed, as the findOwner now has an ignore block.
		//
		//if(typeWallSign || (block.getTypeId() == Material.SIGN_POST.getId())){
		//	Sign		sign = (Sign) block.getState();
		//	String		text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
		//
		//	if(text.equals("[private]") || text.equals(Lockette.altPrivate) || text.equals("[more users]") || text.equals(Lockette.altMoreUsers)){
		//		sign.setLine(0, "");
		//		sign.setLine(1, "");
		//		sign.setLine(2, "");
		//		sign.setLine(3, "");
		//		sign.update(true);
		//	}
		//}

		

		// Colorizer code.
		
		if (Lockette.colorTags) {
			event.setLine(0, ChatColor.translateAlternateColorCodes('&', event.getLine(0)));
			event.setLine(1, ChatColor.translateAlternateColorCodes('&', event.getLine(1)));
			event.setLine(2, ChatColor.translateAlternateColorCodes('&', event.getLine(2)));
			event.setLine(3, ChatColor.translateAlternateColorCodes('&', event.getLine(3)));
		}

		if (Lockette.DEBUG) {
			Lockette.log.info(" line 0 : " + event.getLine(0));
			Lockette.log.info(" line 1 : " + event.getLine(1));
			Lockette.log.info(" line 2 : " + event.getLine(2));
			Lockette.log.info(" line 3 : " + event.getLine(3));
		}
	}
}

