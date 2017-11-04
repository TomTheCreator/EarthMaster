package me.TomTheDeveloper.Attacks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Tom on 1/08/2014.
 */
public class FireCircle extends PlayerAttack {

    private int x = 0, y = 0, z = 0;
    private Location loc = null;



    public FireCircle(int ticks, Player player) {
        super(ticks, player);
        this.loc = player.getLocation();
        this.x = (int) Math.floor(loc.getX());
        this.y = (int) Math.floor(loc.getY());
        this.z = (int) Math.floor(loc.getZ());
        setCounter(0);
    }

    @Override
    public void run() {
        if(getCounter() == 0){


        }

        if(getCounter() == 1){


        }





    }
}
