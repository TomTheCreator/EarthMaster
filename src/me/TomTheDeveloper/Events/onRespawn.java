package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.EarthMasterInstance;
import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by Tom on 6/08/2014.
 */
public class onRespawn implements Listener{

    private GameAPI plugin;

    public onRespawn(GameAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){



        Player p = event.getPlayer();
        if (plugin.getGameInstanceManager().getGameInstance(p) == null)
            return;

        final GameInstance gameInstance = plugin.getGameInstanceManager().getGameInstance(p);

        if (gameInstance.getGameState() != GameState.INGAME && gameInstance.getGameState() != GameState.ENDING && gameInstance.getGameState() != GameState.RESTARTING){
            gameInstance.teleportToLobby(p);
            p.sendMessage(ChatColor.GRAY + "You are crazy! The game isn't started yet and you are already died!");
            p.sendMessage(ChatColor.GRAY + "You are lucky that I am so good humoured today! I gave you a new live for free!");
            return;
        }

        if (gameInstance.getGameState() != GameState.INGAME && gameInstance.getGameState() != GameState.ENDING){
            System.out.print("Is going to teleport to EndLocation");
            if(gameInstance.getGameState() != GameState.STARTING && gameInstance.getGameState() != GameState.WAITING_FOR_PLAYERS)
                p.teleport(gameInstance.getEndLocation());

            return;
        }
        EarthMasterInstance avatarInstance = (EarthMasterInstance) gameInstance;
        avatarInstance.onDeath(p);
        System.out.println("OnDeath() is called!");
        UserManager.getUser(p.getUniqueId()).setPower(0);
        event.setRespawnLocation(gameInstance.getStartLocation());

        p.teleport(gameInstance.getStartLocation());







    }







}
