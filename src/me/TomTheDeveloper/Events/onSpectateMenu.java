package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.MenuAPI.SpectatorMenu;
import me.TomTheDeveloper.Utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * Created by Tom on 2/08/2014.
 */
public class onSpectateMenu implements Listener {

    private GameAPI plugin;


    public onSpectateMenu(GameAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onOpenMenu(PlayerInteractEvent event){

        if(!event.hasItem())
            return;

        if(event.getItem().getType() == Material.COMPASS)

        if(!(event.getItem().hasItemMeta()))
            return;
        if(!(event.getItem().getItemMeta().hasDisplayName()))
            return;
        if(!(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Items.getSpecatorItemStack().getItemMeta().getDisplayName())))
            return;
        if(plugin.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        SpectatorMenu spectatorMenu = new SpectatorMenu(plugin.getGameInstanceManager().getGameInstance(event.getPlayer())) {
            @Override
            public String[] getDescription(Player player) {
                return new String[]{ChatColor.GRAY + "Click to teleport to " + ChatColor.GOLD + player.getName()};
            }


        };

        spectatorMenu.open(event.getPlayer());
    }


    @EventHandler
    public void onSpectatorOptionClick(InventoryClickEvent event){
        if(!(event.getInventory().getName().contains("Spectator Menu"))){

            return;
        }
        event.setCancelled(true);
        if(!(event.getWhoClicked() instanceof Player))
            return;

        Player whoclicked = (Player) event.getWhoClicked();
        if(plugin.getGameInstanceManager().getGameInstance(whoclicked) == null)
            return;
        if(!(event.getCurrentItem().getType() == Material.SKULL_ITEM))
            return;
        SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();

        String playername = skullMeta.getOwner();
        GameInstance gameInstance = plugin.getGameInstanceManager().getGameInstance(whoclicked);
        Player p = null;
        for(Player player: gameInstance.getPlayers()){
            if(player.getName() == whoclicked.getName()){
               p = player;
                break;
            }

        }
        if(p == null) {
            whoclicked.closeInventory();
            whoclicked.sendMessage(ChatColor.RED + "It seems like the player your looking for left the game!");
        }

        event.getWhoClicked().teleport(p);

        whoclicked.sendMessage(ChatManager.NORMAL + "Teleported to " + ChatManager.HIGHLIGHTED +  p.getName());

        whoclicked.closeInventory();

    }
}













