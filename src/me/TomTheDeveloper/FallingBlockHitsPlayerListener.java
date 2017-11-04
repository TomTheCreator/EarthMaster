package me.TomTheDeveloper;

import me.TomTheDeveloper.Game.GameInstance;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;


/**
 * Created by Tom on 27/07/2014.
 */
public class FallingBlockHitsPlayerListener extends BukkitRunnable {

    public static EarthMaster plugin;


    public void start(){
        this.runTaskTimer(plugin, 1L, 1L);
    }


    @Override
    public void run() {
       for(World world: plugin.getServer().getWorlds()){
           for(Entity entity:world.getEntities()){
               if(!(entity instanceof FallingBlock))
                   continue;
                   FallingBlock fallingBlock = (FallingBlock) entity;
                   List<Entity> entities = fallingBlock.getNearbyEntities(0.1,0.1,0.1);
                   for(Entity entity1:entities){
                      if(!(entity1 instanceof Player))
                          continue;
                       entity1.setVelocity(fallingBlock.getVelocity().multiply(2));
                       ((Player) entity1).damage(3.0);
                       fallingBlock.setVelocity(fallingBlock.getVelocity().multiply(0.5));

                   }



             /*  Attack.AttackDirection attackDirection = Attack.getDirection(fallingBlock.getLocation());
               Block block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.DOWN);

               System.out.print(fallingBlock.getVelocity().angle(new Vector(1,fallingBlock.getVelocity().getY(),0)));
               switch (attackDirection){
                  case NORTH:
                      block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.NORTH);
                      break;
                   case SOUTH:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.SOUTH);
                       break;
                   case WEST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.WEST);
                       break;
                   case EAST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.EAST);
                       break;
                   case NORTH_EAST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST);
                       break;
                   case NORTH_WEST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.NORTH_WEST);
                       break;
                   case SOUTH_EAST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.SOUTH_EAST);
                       break;
                   case SOUTH_WEST:
                       block = fallingBlock.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST);
                       break;
                   default:
                       block = fallingBlock.getLocation().getBlock();

               }
               if(block.getType() != Material.AIR && fallingBlock.getMaterial() == Material.TNT){

                   fallingBlock.getWorld().createExplosion(fallingBlock.getLocation(), 2F);
                   fallingBlock.remove();

               }*/
           }
       }
    }
}
