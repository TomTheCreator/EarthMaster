package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.KitAPI.BaseKits.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Tom on 27/07/2014.
 */
public class AirMaster extends Kit implements Listener{

    private GameAPI plugin;

    public AirMaster(GameAPI plugin) {
        setName("Air");
        setDescription(new String[] {"You control the air!"});
        this.plugin = plugin;
        plugin.getKitHandler().registerKit(this);
    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        return true;
    }

    @Override
    public void giveKitItems(Player player) {

    }

    @Override
    public Material getMaterial() {
        return Material.SUGAR;
    }

    @Override
    public void reStock(Player player) {

    }
}
