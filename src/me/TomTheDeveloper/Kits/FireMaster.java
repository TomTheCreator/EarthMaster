package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.KitAPI.BaseKits.FreeKit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Tom on 27/07/2014.
 */
public class FireMaster extends FreeKit {

    private GameAPI plugin;

    public FireMaster(GameAPI plugin) {
        setDescription(new String[]{"You control fire! ", "You are able to shoot fireballs,", "to set wood on fire"});
        setName("Fire");
        this.plugin = plugin;
        plugin.getKitHandler().registerKit(this);
    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.FIRE;
    }

    @Override
    public void reStock(Player player) {

    }

    @Override
    public void giveKitItems(Player player) {

    }





}
