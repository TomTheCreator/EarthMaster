package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.GameInstanceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Tom on 27/07/2014.
 */
public class onBlockBreak implements Listener {

    private GameAPI gameAPI;

    public onBlockBreak(GameAPI gameAPI){
      this.gameAPI = gameAPI;
    }

        @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
            if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
                return;
            event.setCancelled(true);

        }


}
