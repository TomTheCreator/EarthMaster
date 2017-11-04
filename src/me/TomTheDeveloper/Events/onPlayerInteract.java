package me.TomTheDeveloper.Events;

import me.TomTheDeveloper.Attacks.EarthWaveAttack;
import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.GameAPI;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.Utils.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 * Created by Tom on 27/07/2014.
 */
public class onPlayerInteract implements Listener {

    private GameAPI gameAPI;

    public onPlayerInteract(GameAPI gameAPI) {
        this.gameAPI = gameAPI;
    }

    @EventHandler
    public void onBlockLift(PlayerInteractEvent event){

        if(!(event.getAction() == Action.LEFT_CLICK_BLOCK))
            return;
        if(event.getPlayer().isSneaking())
            return;
        if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        GameInstance gameInstance = gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer());
        if(gameInstance.getGameState() != GameState.INGAME)
            return;
        event.setCancelled(true);
        if(UserManager.getUser(event.getPlayer().getUniqueId()).getPower() < 1){
            event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough power! You need 1 power to perform this attack!");
            return;
        }

        if(event.getClickedBlock().getType() == Material.TNT){
            TNTPrimed tnt = event.getClickedBlock().getWorld().spawn(event.getClickedBlock().getLocation(), TNTPrimed.class);
            tnt.setVelocity(new Vector(0, 0.7F,0));
            tnt.getWorld().playEffect(tnt.getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getType());
            ParticleEffect.FLAME.display(tnt.getLocation(), 1,2,1,1, 20);
            event.getClickedBlock().setType(Material.AIR);

        }

        Material material = event.getClickedBlock().getType();
        event.getClickedBlock().setType(Material.AIR);        //
        FallingBlock fallingblock = event.getClickedBlock().getWorld().spawnFallingBlock(event.getClickedBlock().getLocation(), material, (byte) 0);
        fallingblock.setVelocity(new Vector(0,0.7F,0));
        fallingblock.getWorld().playEffect(fallingblock.getLocation(), Effect.STEP_SOUND,material);
        fallingblock.setDropItem(false);
        UserManager.getUser(event.getPlayer().getUniqueId()).removePower(1);
    }


    @EventHandler
    public void onWallMake(PlayerInteractEvent event){
        if(!(event.getAction() == Action.LEFT_CLICK_BLOCK))
            return;
        if(!event.getPlayer().isSneaking())
            return;
        if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        GameInstance gameInstance = gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer());
        if(gameInstance.getGameState() != GameState.INGAME)
            return;
        if(event.getClickedBlock().getType() == Material.WATER || event.getClickedBlock().getType() == Material.STATIONARY_WATER
                || event.getClickedBlock().getType() == Material.DEAD_BUSH || event.getClickedBlock().getType() == Material.GRASS){
            return;

        }
        event.setCancelled(true);
        if(UserManager.getUser(event.getPlayer().getUniqueId()).getPower() < 5){
            event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough power! You need 5 power to perform this attack!");
            return;
        }
        Block clickedblock = event.getClickedBlock();
        if(Math.floor(event.getPlayer().getLocation().getX()) == Math.floor(clickedblock.getLocation().getX())
                && Math.floor(event.getPlayer().getLocation().getZ()) != Math.floor(clickedblock.getLocation().getZ())) {
            for (int x = -1; x < 2; x++) {
                Block block = clickedblock.getLocation().add((double) x, 0.0, 0.0).getBlock();
                if(block.getType() == Material.AIR)
                    block = block.getLocation().add(0,-1,0).getBlock();
                if(clickedblock.getLocation().add(0,1,x).getBlock().getType() != Material.AIR)
                    block = clickedblock.getLocation().add(0, 1, x ).getBlock();
                if(block.getType() == Material.WATER)
                    continue;

                Material material = block.getType();
                ParticleEffect.SMOKE.display(block.getLocation(), 1, 2, 1, 1,20);
                FallingBlock fallingBlock = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                        :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                fallingBlock.setVelocity(new Vector(0, 0.6F, 0));
                FallingBlock fallingBlock1 = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                        :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                fallingBlock1.setVelocity(new Vector(0, 0.5F, 0));
                fallingBlock.setDropItem(false);
                fallingBlock1.setDropItem(false);
            }
            UserManager.getUser(event.getPlayer().getUniqueId()).removePower(5);
        }

        if(Math.floor(event.getPlayer().getLocation().getZ()) == Math.floor(clickedblock.getLocation().getZ())
                && Math.floor(event.getPlayer().getLocation().getX()) != Math.floor(clickedblock.getLocation().getX())) {
            for (int z = -1; z < 2; z++) {
                Block block = clickedblock.getLocation().add((double) 0, 0,  z).getBlock();
                if(block.getType() == Material.AIR)
                    block = block.getLocation().add(0,-1,0).getBlock();
                if(clickedblock.getLocation().add(0,1,z).getBlock().getType() != Material.AIR)
                    block = clickedblock.getLocation().add(0, 1, z ).getBlock();
                Material material = block.getType();
                ParticleEffect.SMOKE.display(block.getLocation(), 1, 2, 1, 1,20);
                FallingBlock fallingBlock = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                        :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                fallingBlock.setVelocity(new Vector(0, 0.6F, 0));
                FallingBlock fallingBlock1= (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                        :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                fallingBlock1.setVelocity(new Vector(0, 0.5F, 0));
                fallingBlock.setDropItem(false);
                fallingBlock1.setDropItem(false);
            }
            UserManager.getUser(event.getPlayer().getUniqueId()).removePower(5);
        }
        int z ;
        int x;
        if(Math.floor(event.getPlayer().getLocation().getZ()) != Math.floor(clickedblock.getLocation().getZ())
                && Math.floor(event.getPlayer().getLocation().getX()) != Math.floor(clickedblock.getLocation().getX())){
                if((Math.floor(event.getPlayer().getLocation().getZ()) > Math.floor(clickedblock.getLocation().getZ())
                        && Math.floor(event.getPlayer().getLocation().getX()) > Math.floor(clickedblock.getLocation().getX()))
                        || (Math.floor(event.getPlayer().getLocation().getZ()) < Math.floor(clickedblock.getLocation().getZ())
                        && Math.floor(event.getPlayer().getLocation().getX()) < Math.floor(clickedblock.getLocation().getX()))) {

                     z = -1;
                     x = 1;

                    while (z<2) {

                        Block block = clickedblock.getLocation().add(x, 0, z).getBlock();
                        if (block.getType() == Material.AIR)
                            block = block.getLocation().add(0, -1, 0).getBlock();
                        if (block.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR)
                            block = block.getLocation().add(0, 1, 0).getBlock();
                        ParticleEffect.SMOKE.display(block.getLocation(), 1, 2, 1, 1,20);

                        Material material = block.getType();
                        FallingBlock fallingBlock = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                                :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                        fallingBlock.setVelocity(new Vector(0, 0.6F, 0));
                        FallingBlock fallingBlock1 = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                                :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                        fallingBlock1.setVelocity(new Vector(0, 0.5F, 0));
                        fallingBlock.setDropItem(false);
                        fallingBlock1.setDropItem(false);

                        x--;
                        z++;
                    }


                }else{
                    z = -1;
                    x = -1;

                    while (x<2) {

                        Block block = clickedblock.getLocation().add(x, 0, z).getBlock();
                        if (block.getType() == Material.AIR)
                            block = block.getLocation().add(0, -1, 0).getBlock();
                        if (block.getLocation().add(0, 1, 0).getBlock().getType() != Material.AIR)
                            block = block.getLocation().add(0, 1, 0).getBlock();
                        Material material = block.getType();
                        ParticleEffect.SMOKE.display(block.getLocation(), 1, 2, 1, 1,20);
                        FallingBlock fallingBlock = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                                                                     :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                        fallingBlock.setVelocity(new Vector(0, 0.6F, 0));
                        FallingBlock fallingBlock1 = (material == Material.TNT ? (FallingBlock) block.getWorld().spawn(block.getLocation(), TNTPrimed.class)
                                :block.getWorld().spawnFallingBlock(block.getLocation(), material, (byte) 0));
                        fallingBlock1.setVelocity(new Vector(0, 0.5F, 0));
                        fallingBlock.setDropItem(false);
                        fallingBlock1.setDropItem(false);

                        x++;
                        z++;
                    }
                }
            UserManager.getUser(event.getPlayer().getUniqueId()).removePower(5);
        }




    }

    @EventHandler
    public void onEarthWaveMake(PlayerInteractEvent event){
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK))
            return;
        if(!event.getPlayer().isSneaking())
            return;
        if(gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer()) == null)
            return;
        GameInstance gameInstance = gameAPI.getGameInstanceManager().getGameInstance(event.getPlayer());
        if(gameInstance.getGameState() != GameState.INGAME)
            return;

        if(event.getClickedBlock().getType() == Material.WATER || event.getClickedBlock().getType() == Material.STATIONARY_WATER
                || event.getClickedBlock().getType() == Material.DEAD_BUSH || event.getClickedBlock().getType() == Material.GRASS){
            return;
        }

        event.setCancelled(true);
        if(UserManager.getUser(event.getPlayer().getUniqueId()).getPower() < 15){
            event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough power! You need 15 power to perform this attack!");
            return;
        }

        EarthWaveAttack earthWaveAttack = new EarthWaveAttack(event.getPlayer(), event.getClickedBlock());

    }


    @EventHandler
    public void onFire(PlayerInteractEvent event)
    {
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
            return;
        if(!(event.getPlayer().getItemInHand().getType() == Material.BLAZE_POWDER))
            return;
        //event.setCancelled(true);
        //FireBlastAttack fireBlastAttack = new FireBlastAttack(2, event.getPlayer());





    }



}
