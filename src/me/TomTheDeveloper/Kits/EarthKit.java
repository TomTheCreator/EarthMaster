package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.KitAPI.BaseKits.Kit;
import me.TomTheDeveloper.KitAPI.KitHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Tom on 27/07/2014.
 */
public class EarthKit extends Kit {

    private GameAPI plugin;



    public EarthKit(GameAPI plugin) {
        setName("Earth");
        setDescription(new String[]{ChatManager.PREFIX + "You are the earth master!",
                                     ChatColor.DARK_GREEN  +  "INFO",
                                     ChatManager.HIGHLIGHTED + "LEFT CLICK: " + ChatManager.NORMAL + " Lifts the clicked block!",
                                     ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "2",
                                     ChatManager.HIGHLIGHTED + "RIGHT CLICK: " + ChatManager.NORMAL + " Shoots the  clicked flying block.",
                                     ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "2",
                                     ChatManager.HIGHLIGHTED + "SHIFTED LEFT CLICK: " + ChatManager.NORMAL + " Builds a wall infront of you.",
                                     ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "15",});
        this.plugin = plugin;
        plugin.getKitHandler().registerKit(this);


    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.DIRT;
    }

    @Override
    public void reStock(Player player) {

    }

    @Override
    public void giveKitItems(Player player) {
        ItemStack Ground_Shooter_ItemStack = new ItemStack(Material.DIRT);
        setItemNameAndLore(Ground_Shooter_ItemStack,
                ChatManager.PREFIX + "How to use (RIGHT CLICK)",
                new String[]{ChatManager.HIGHLIGHTED + "LEFT CLICK: " + ChatManager.NORMAL + " Lifts the clicked block!",
                              ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "2",
                        ChatManager.HIGHLIGHTED + "RIGHT CLICK: " + ChatManager.NORMAL + " Shoots the  clicked flying block.",
                         ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "2",
                        ChatManager.HIGHLIGHTED + "SHIFTED RIGHT CLICK: " + ChatManager.NORMAL + " Builds a wall infront of you.",
                         ChatManager.PREFIX + "Power Cost: " + ChatManager.NORMAL + "15",});

        player.getInventory().addItem(Ground_Shooter_ItemStack);

    }
}
