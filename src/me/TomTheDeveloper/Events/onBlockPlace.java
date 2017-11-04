package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.GameAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Tom on 27/07/2014.
 */
public class onBlockPlace implements Listener {

    private GameAPI gameAPI;

    public onBlockPlace(GameAPI gameAPI) {
        this.gameAPI = gameAPI;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        if(event.getPlayer().isOp())
            return;
        event.setCancelled(true);
    }
}
