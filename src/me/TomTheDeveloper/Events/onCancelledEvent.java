package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.Utils.Items;
import me.confuser.barapi.BarAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Tom on 3/08/2014.
 */
public class onCancelledEvent implements Listener {

    private GameAPI plugin;

    public onCancelledEvent(GameAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if(plugin.getGameInstanceManager().getGameInstance(player ) == null)
            return;
        event.setCancelled(true);
        player.setFoodLevel(20);
    }

    @EventHandler
    public void onFallDamage(EntityDamageByBlockEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if(plugin.getGameInstanceManager().getGameInstance(player ) == null)
            return;
        if(event.getEntity().getLocation().getY() < 1)
            return;
        event.setCancelled(true);
        player.setHealth(player.getMaxHealth());
    }

   @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(plugin.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
       if(event.getPlayer().getLocation().getY() >0)
           return;
       GameInstance gameInstance = plugin.getGameInstanceManager().getGameInstance(event.getPlayer());
        if(gameInstance.getGameState() == GameState.WAITING_FOR_PLAYERS || (gameInstance.getGameState() == GameState.STARTING && gameInstance.getTimer() > 15)){
          event.getPlayer().teleport(gameInstance.getLobbyLocation());
            return;
        }
       if(gameInstance.getGameState() == GameState.STARTING){
           event.getPlayer().teleport(gameInstance.getStartLocation());
           return;
       }
       if(gameInstance.getGameState() == GameState.ENDING){
           event.getPlayer().teleport(gameInstance.getEndLocation());
           event.getPlayer().sendMessage(ChatColor.GRAY + "Teleported to the lobby!");
           gameInstance.removePlayer(event.getPlayer());
           UserManager.getUser(event.getPlayer().getUniqueId()).removeScoreboard();
           if(plugin.isBarEnabled())
             BarAPI.removeBar(event.getPlayer());
       }






   }


}
