package com.Soara.Light.LightBinding;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

import com.projectkorra.projectkorra.BendingPlayer;

public class LBListener implements Listener {
	
	
	      @EventHandler
	      public void onSwing(PlayerAnimationEvent event) {
	    	  Player player = event.getPlayer();
	    	  BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
	    	  if (event.isCancelled() || bPlayer == null) {
	    		  return;
	    	  } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
	    		  return;
	    	  } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("LightBinding")) {
	    		  new LightBinding(player);
	    
	    	  }
	     }
    	  
}