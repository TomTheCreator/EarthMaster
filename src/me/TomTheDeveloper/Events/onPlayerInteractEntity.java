package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * Created by Tom on 27/07/2014.
 */
public class onPlayerInteractEntity implements Listener {

    private GameAPI gameAPI;

    public onPlayerInteractEntity(GameAPI gameAPI) {
        this.gameAPI = gameAPI;
    }

    @EventHandler
    public void onFallingBlockInteract(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof FallingBlock))
            return;
        if(event.getPlayer().isSneaking())
            return;
        if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        GameInstance gameInstance = gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer());
        if(gameInstance.getGameState() != GameState.INGAME)
            return;
        if(UserManager.getUser(event.getPlayer().getUniqueId()).getPower() < 2){
            event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough power! You need 2 power to perform this attack!");
            return;
        }
        event.getPlayer().getWorld().playSound(event.getRightClicked().getLocation(), Sound.WITHER_SHOOT, 1F, 1F);
        event.getRightClicked().setVelocity(event.getRightClicked().getVelocity().add(event.getPlayer().getLocation().getDirection().multiply(1.5).add(new Vector(0,0.1F,0))));
        UserManager.getUser(event.getPlayer().getUniqueId()).removePower(2);


    }
}
