package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.GameAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

/**
 * Created by Tom on 9/08/2014.
 */
public class onTest implements Listener {


    private GameAPI plugin;

    public onTest(GameAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        e.setCancelled(true);

        for(Block b : e.blockList())
        {
            bounceBlock(b);
        }
    }

    public void bounceBlock(Block b)
    {
        Object fb;
        if(b == null) return;
        if(b.getType() == Material.TNT){
            fb =  b.getWorld().spawn(b.getLocation().add(0,1,0), TNTPrimed.class);


        }else {
            fb =  b.getWorld()
                    .spawnFallingBlock(b.getLocation().add(0, 1, 0), b.getType(), b.getData());
            ((FallingBlock) fb).setDropItem(false);
        }


        b.setType(Material.AIR);

        float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
        float y =  (float)0.5;//(float) -5 + (float)(Math.random() * ((5 - -5) + 1));
        float z = (float) -0.3 + (float)(Math.random() * ((0.3 - -0.3) + 1));

        ((Entity)fb).setVelocity(new Vector(x, y, z));
    }

}
