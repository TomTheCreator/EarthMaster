package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.EarthMaster;
import me.TomTheDeveloper.EarthMasterInstance;
import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.Handlers.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Tom on 29/07/2014.
 */
public class onDie implements Listener {

    public EarthMaster plugin;



    public onDie(EarthMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDie(final PlayerDeathEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;
        final Player p = event.getEntity();
        if (plugin.getGameInstanceManager().getGameInstance(p) == null)
            return;



        final GameInstance gameInstance = plugin.getGameInstanceManager().getGameInstance(p);
        event.setDeathMessage("");
        if (gameInstance.getGameState() == GameState.WAITING_FOR_PLAYERS || (gameInstance.getGameState() == GameState.STARTING && gameInstance.getTimer() >15)){
            gameInstance.teleportToLobby(p);
            p.sendMessage(ChatColor.GRAY + "You are crazy! The game isn't started yet and you are already died!");
            p.sendMessage(ChatColor.GRAY + "You are lucky that I am so good humoured today! I gave you a new live for free!");
            this.respawn(p);
            return;
        }


        if (gameInstance.getGameState() != GameState.INGAME && gameInstance.getGameState() != GameState.ENDING){

            if(gameInstance.getGameState() != GameState.STARTING && gameInstance.getGameState() != GameState.WAITING_FOR_PLAYERS)
                p.teleport(gameInstance.getEndLocation());
                this.respawn(p);
            return;
        }
        EarthMasterInstance avatarInstance = (EarthMasterInstance) gameInstance;
        avatarInstance.onDeath(p);
        UserManager.getUser(p.getUniqueId()).setPower(0);

                p.teleport(gameInstance.getStartLocation());

        this.respawn(p);





    }

    public void respawn(final Player p) {

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (p.isDead())
                    p.setHealth(20);
            }
        });

    }




}
