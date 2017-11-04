package me.TomTheDeveloper.Attacks;

import me.TomTheDeveloper.Attacks.Attack;
import me.TomTheDeveloper.Handlers.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by Tom on 30/07/2014.
 */
public class EarthWaveAttack extends Attack {

    private Player p;
    private Block clickedblock;
    private Block block[];
    private int i = 0;
    private FallingBlock[] fallingBlocks;
    private Material[] material;
    private AttackDirection attackDirection;


    public EarthWaveAttack( Player whoAttacks, Block clickedblock) {
        super(3);
        this.p = whoAttacks;
        this.clickedblock = clickedblock;
        plugin.getAttackManager().registerAttack(this);
        attackDirection = Attack.getDirection(p.getLocation());

    }

    @Override
    public void run() {

        if (i == 30)
            plugin.getAttackManager().unregisterAttack(this);


        if(i == 0) {
            if ((attackDirection == AttackDirection.NORTH
                    || attackDirection == AttackDirection.SOUTH
                    || attackDirection == AttackDirection.WEST
                    || attackDirection == AttackDirection.EAST)){
                UserManager.getUser(p.getUniqueId()).removePower(15);
            }else {
                plugin.getAttackManager().unregisterAttack(this);
                p.sendMessage(ChatColor.RED + "This attack needs to be performed straight! You aren't that good to perform it slantwise!");
                return;

            }

        }

        if (attackDirection == AttackDirection.SOUTH || attackDirection == AttackDirection.NORTH) {



            if (i == 0) {
                fallingBlocks = new FallingBlock[]{null, null, null};
                block = new Block[]{null, null, null};
                material = new Material[]{null, null, null};
                for (int x = -1; x < 2; x++) {
                    block[x + 1] = clickedblock.getLocation().add(x, 0, 0).getBlock();

                    material[x + 1] = block[x + 1].getType();
                    block[x + 1].setType(Material.AIR);

                    if(block[x+1].getType() == Material.TNT){
                        TNTPrimed tntPrimed = block[x+1].getWorld().spawn(block[x+1].getLocation(), TNTPrimed.class);
                        tntPrimed.setVelocity(new Vector(0, 1, 0));
                    }else{
                        fallingBlocks[x + 1] = p.getWorld().spawnFallingBlock(block[x + 1].getLocation(), material[x + 1], (byte) 0);
                        fallingBlocks[x + 1].setVelocity(new Vector(0, 0.5, 0.0));
                        fallingBlocks[x+1].setDropItem(false);
                    }

                }
            }
            if ((i % 2) == 0 && i != 0) {

                for (int x = -1; x < 2; x++) {

                    block[x + 1] = block[x + 1].getRelative((attackDirection == AttackDirection.NORTH) ? BlockFace.NORTH : BlockFace.SOUTH);
                    if(block[x+1].getRelative(BlockFace.UP).getType() != Material.AIR)
                        block[x+1] = block[x+1].getRelative(BlockFace.UP);
                    //if(block[x+1].getRelative(BlockFace.DOWN).getType() == Material.AIR)
                      //  block[x+1] = block[x+1].getRelative(BlockFace.DOWN);
                    material[x + 1] = block[x + 1].getType();
                    block[x + 1].setType(Material.AIR);
                    if(block[x+1].getType() == Material.TNT){
                        fallingBlocks[x+1] =  (FallingBlock) block[x+1].getWorld().spawn(block[x+1].getLocation(), TNTPrimed.class);
                        fallingBlocks[x+1].setVelocity(new Vector(0, 0.5, 0));
                    }else{
                        fallingBlocks[x + 1] = p.getWorld().spawnFallingBlock(block[x + 1].getLocation(), material[x + 1], (byte) 0);
                        fallingBlocks[x + 1].setVelocity(new Vector(0, 0.5, 0.0));
                        fallingBlocks[x+1].setDropItem(false);
                    }
                }

            }
            if ((i % 2) != 0 && i != 0) {
                for (int x = -1; x < 2; x++) {
                    fallingBlocks[x + 1].setVelocity(fallingBlocks[x + 1].getVelocity().add(new Vector(0, 0, (attackDirection == AttackDirection.NORTH) ? -0.09 : 0.09)));
                    //block[x+1].setType(material[x+1]);

                }
            }


        }
        if(attackDirection == AttackDirection.WEST || attackDirection == AttackDirection.EAST){
            if (i == 0) {
                fallingBlocks = new FallingBlock[]{null, null, null};
                block = new Block[]{null, null, null};
                material = new Material[]{null, null, null};
                for (int x = -1; x < 2; x++) {
                    block[x + 1] = clickedblock.getLocation().add(0, 0,x ).getBlock();

                    material[x + 1] = block[x + 1].getType();
                    block[x + 1].setType(Material.AIR);
                    if(block[x+1].getType() == Material.TNT){
                        fallingBlocks[x+1] =  (FallingBlock) block[x+1].getWorld().spawn(block[x+1].getLocation(), TNTPrimed.class);
                        fallingBlocks[x+1].setVelocity(new Vector(0, 0.5, 0));
                    }else{
                        fallingBlocks[x + 1] = p.getWorld().spawnFallingBlock(block[x + 1].getLocation(), material[x + 1], (byte) 0);
                        fallingBlocks[x + 1].setVelocity(new Vector(0, 0.5, 0.0));
                        fallingBlocks[x+1].setDropItem(false);
                    }

                }
            }
            if ((i % 2) == 0 && i != 0) {

                for (int x = -1; x < 2; x++) {

                    block[x + 1] = block[x + 1].getRelative((attackDirection == AttackDirection.WEST) ? BlockFace.WEST : BlockFace.EAST);
                    if(block[x+1].getRelative(BlockFace.UP).getType() != Material.AIR)
                        block[x+1] = block[x+1].getRelative(BlockFace.UP);
                   // if(block[x+1].getRelative(BlockFace.DOWN).getType() == Material.AIR)
                     //   block[x+1] = block[x+1].getRelative(BlockFace.DOWN);
                    material[x + 1] = block[x + 1].getType();
                    block[x + 1].setType(Material.AIR);
                    if(block[x+1].getType() == Material.TNT){
                        fallingBlocks[x+1] =  (FallingBlock) block[x+1].getWorld().spawn(block[x+1].getLocation(), TNTPrimed.class);
                        fallingBlocks[x+1].setVelocity(new Vector(0, 0.5, 0));
                    }else{
                        fallingBlocks[x + 1] = p.getWorld().spawnFallingBlock(block[x + 1].getLocation(), material[x + 1], (byte) 0);
                        fallingBlocks[x + 1].setVelocity(new Vector(0, 0.5, 0.0));
                        fallingBlocks[x+1].setDropItem(false);
                    }
                }

            }
            if ((i % 2) != 0 && i != 0) {
                for (int x = -1; x < 2; x++) {
                    fallingBlocks[x + 1].setVelocity(fallingBlocks[x + 1].getVelocity().add(new Vector((attackDirection == AttackDirection.WEST) ? -0.09 : 0.09, 0,0 )));
                    //block[x+1].setType(material[x+1]);

                }
            }
        }




     i++;
    }
}
