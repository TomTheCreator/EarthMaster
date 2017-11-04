package me.TomTheDeveloper;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Game.GameState;
import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.Utils.*;
import me.confuser.barapi.BarAPI;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 27/07/2014.
 */
public class EarthMasterInstance extends GameInstance {

    private ScoreboardManager scoreboardManager;



    @Override
    public boolean needsPlayers() {
        if((getGameState() == GameState.STARTING || getGameState() == GameState.WAITING_FOR_PLAYERS) && getPlayers().size() < getMAX_PLAYERS()){
           return true;
        }else
            return false;
    }

    public EarthMasterInstance(String ID) {
        super(ID);


        setMIN_PLAYERS(1);


    }

    public GameInstance getGameInstance(){
        return this;
    }


    public void start(){
        runTaskTimer(plugin, 20L, 20L);
        getStartLocation().getWorld().setGameRuleValue("keepInventory", "on");
        getStartLocation().getWorld().setPVP(false);

        this.setGameState(GameState.RESTARTING);




    }

    @Override
    public void run() {
        updateScoreboard();
        if(plugin.isBarEnabled())
            updateBar();
        for(Player player:getPlayers()){
            if (player.getLocation().getY() < 0)
                player.setHealth(0);
        }

        switch (getGameState()){

            case WAITING_FOR_PLAYERS:


                if(getPlayers().size() < getMIN_PLAYERS()){

                    if(getTimer() <= 0){
                        setTimer(15);
                        getChatManager().broadcastMessage("Waiting for players... We need at least " + ChatManager.HIGHLIGHTED + getMIN_PLAYERS()+ ChatManager.NORMAL +" players to start.");
                        return;
                    }
                }else{
                    getChatManager().broadcastMessage("We now have enough players. The game is starting soon!");
                    setGameState(GameState.STARTING);
                    setTimer(30);
                    this.showPlayers();

                }
                setTimer(getTimer()-1);
                break;
            case STARTING:
                if(getTimer() == 30 || getTimer() == 15 || (getTimer() <10 && getTimer() != 0)){
                    getChatManager().broadcastMessage("The game starts in " + ChatManager.HIGHLIGHTED + getTimer() + ChatManager.NORMAL + " seconds!");
                }
                if(getTimer() == 15){
                    this.teleportAllToStartLocation();
                    this.sendInfoMessage();
                }
                if(getTimer() == 2)
                    this.teleportAllToStartLocation();
                if(getTimer() <= 0){
                    setGameState(GameState.INGAME);
                    setTimer(500);
                    getChatManager().broadcastMessage("Let the battle begin!");
                    this.showPlayers();

                    for(User user:UserManager.getUsers(this)){
                        user.setPower(15);
                        user.toPlayer().setGameMode(GameMode.SURVIVAL);
                        user.setFakeDead(false);
                        user.setSpectator(false);
                        user.toPlayer().getInventory().clear();
                        if(user.isPremium())
                            user.allowDoubleJump();

                    }

                }

                setTimer(getTimer()-1);
                break;
            case INGAME:
                if(getTimer() <=0){
                    getChatManager().broadcastMessage("Time's up! Nobody has proven to be a good earth master!");
                    setGameState(GameState.ENDING);
                    setTimer(10);


                }

                if(getPlayersLeft().size() == 1){
                    getChatManager().broadcastMessage(ChatManager.HIGHLIGHTED + getLastPlayerLeft().getName() + ChatManager.NORMAL + " has won!");
                    setTimer(10);
                    getChatManager().broadcastMessage("You will be teleported to the lobby in " + ChatManager.HIGHLIGHTED + getTimer() + ChatManager.NORMAL + " seconds");
                    setGameState(GameState.ENDING);



                }

                if(getPlayersLeft().size() <=0){
                    getChatManager().broadcastMessage("Nobody has won!");
                    setTimer(10);
                    getChatManager().broadcastMessage("You will be teleported to the lobby in " + ChatManager.HIGHLIGHTED + getTimer() + ChatManager.NORMAL + " seconds");
                    setGameState(GameState.ENDING);
                    for(User user: UserManager.getUsers(this)){
                        user.removeScoreboard();
                        BarAPI.removeBar(user.toPlayer());
                    }
                }


                for(Player p: getPlayers()){
                    if(!UserManager.getUser(p.getUniqueId()).isFakeDead())
                     UserManager.getUser(p.getUniqueId()).addPower();

                    if(UserManager.getUser(p.getUniqueId()).isSpectator() && p.getInventory().getItem(0)  == null){
                        for(int slot = 0; slot<9; slot++){
                            p.getInventory().setItem(slot, Items.getSpecatorItemStack());
                        }
                    }

                }
                setTimer(getTimer()-1);

                break;
            case ENDING:
                for(Player p: getPlayers()){
                    if(!UserManager.getUser(p.getUniqueId()).isSpectator())
                        Util.spawnRandomFirework(p.getLocation());
                }
                if(getTimer() <= 0){
                    for(User user:UserManager.getUsers(this)){
                        user.setFakeDead(false);
                        user.setSpectator(false);
                        user.removeScoreboard();
                        user.setAllowDoubleJump(false);


                    }
                    setGameState(GameState.RESTARTING);
                    for(Player p: getPlayers()){

                        p.getInventory().clear();
                        p.updateInventory();

                        if(BarAPI.hasBar(p))
                            BarAPI.removeBar(p);
                        teleportAllToEndLocation();
                        this.removeAllPlayers();

                    }
                }
                setTimer(getTimer()-1);
                break;
            case RESTARTING:
                for(Player p : getPlayers()){
                    this.removePlayer(p);
                    UserManager.getUser(p.getUniqueId()).removeScoreboard();
                    this.teleportToLobby(p);
                }
                getChatManager().broadcastMessage("Reloading map! You could experience some lagg!");
                reInitMap();
                getChatManager().broadcastMessage("Map reloaded!");
                if(!GameAPI.getRestart()) {
                    setGameState(GameState.WAITING_FOR_PLAYERS);
                    plugin.getSignManager().addToQueue(this);
                }
                break;
            default:
                setGameState(GameState.WAITING_FOR_PLAYERS);
        }
    }



    public void updateScoreboard(){
        if(getPlayers().size() == 0)
            return;
         for(Player p: getPlayers()) {
             User user = UserManager.getUser(p.getUniqueId());
             if(user.getScoreboard().getObjective("waiting") == null){
                 user.getScoreboard().registerNewObjective("waiting","dummy");
                 user.getScoreboard().registerNewObjective("starting", "dummy");
                 user.getScoreboard().registerNewObjective("ingame", "dummy");

             }
            switch (getGameState()) {
                 case WAITING_FOR_PLAYERS:

                 case STARTING:
                     Objective startingobj = user.getScoreboard().getObjective("starting");
                     startingobj.setDisplayName(ChatManager.PREFIX + plugin.getGameName());
                     startingobj.setDisplaySlot(DisplaySlot.SIDEBAR);
                     if(!plugin.isBarEnabled()) {
                      Score timerscore = startingobj.getScore(ChatManager.HIGHLIGHTED + "Starting in");
                     timerscore.setScore(getTimer());
                     }
                     Score playerscore = startingobj.getScore(ChatManager.HIGHLIGHTED + "Players :");
                     playerscore.setScore(getPlayers().size());
                     Score minplayerscore = startingobj.getScore(ChatManager.HIGHLIGHTED + "Min Players:");
                     minplayerscore.setScore(getMIN_PLAYERS());


                     break;
                 case INGAME:
                     Objective ingameobj = user.getScoreboard().getObjective("ingame");
                     ingameobj.setDisplayName( ChatManager.PREFIX + plugin.getGameName());
                     ingameobj.setDisplaySlot(DisplaySlot.SIDEBAR);
                     Score playerleftscore = ingameobj.getScore(ChatManager.HIGHLIGHTED + "Players left:");
                     playerleftscore.setScore(this.getPlayersLeft().size());
                     if(!plugin.isBarEnabled()) {
                         Score timeleftscore = ingameobj.getScore(ChatManager.HIGHLIGHTED + "Time left:");
                         timeleftscore.setScore(getTimer());
                     }
                     Score powerscore = ingameobj.getScore(ChatManager.HIGHLIGHTED + "Power: ");
                     powerscore.setScore(user.getPower());
                     break;
                 case ENDING:
                     break;
                 case RESTARTING:

                     break;
                 default:
                     setGameState(GameState.WAITING_FOR_PLAYERS);
             }
             user.setScoreboard(user.getScoreboard());
         }
    }

    public void onDeath( final Player p){
        if(getGameState() == GameState.RESTARTING){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    getGameInstance().removePlayer(p);
                    getGameInstance().teleportToEndLocation(p);
                }
            });

        }
        getChatManager().broadcastDeathMessage(p);
         User user = UserManager.getUser(p.getUniqueId());
        user.setFakeDead(true);
        user.setSpectator(true);
        p.setAllowFlight(true);
        p.setFlying(true);

        p.getInventory().clear();
        p.setGameMode(GameMode.ADVENTURE);

        p.sendMessage(ChatColor.RED + "You died! You are now in spectator mode! You are able to fly now!");
        for(int i = 0; i<=8; i++){
            p.getInventory().setItem(i, Items.getSpecatorItemStack());
        }

        p.updateInventory();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                getGameInstance().teleportToStartLocation(p);
            }
        });
        hidePlayer(p);
    }

    public Player getLastPlayerLeft(){
        Player p = null;
        int i = 0;
        for(User user:UserManager.getUsers(this)){
            if(!user.isFakeDead()){
                p = user.toPlayer();
                i++;
            }
        }
        if(i >1)
            throw new NullPointerException("More than one winner?");
        if(p == null)
            throw new NullPointerException("No winner found!");
        return p;

    }





    public void reInitMap(){

        SchematicPaster.pasteSchematic(getSchematicName(), getStartLocation());
    }

    private void updateBar() {
        switch (getGameState()) {
            case WAITING_FOR_PLAYERS:
                for (Player player : getPlayers()) {
                    if (BarAPI.hasBar(player))
                        BarAPI.removeBar(player);
                }
                break;

            case STARTING:
                for (Player player : getPlayers()) {
                    float percentage = (float) Math.ceil((double) (100 * getTimer() / 30));

                    BarAPI.setMessage(player, ChatManager.NORMAL + "Starting in: " + ChatManager.HIGHLIGHTED + getTimer(), percentage);

                }
                break;
            case INGAME:
                for (Player p : getPlayers()) {
                    BarAPI.setMessage(p, ChatManager.NORMAL + "Time left -> " + ChatManager.HIGHLIGHTED + Util.formatIntoMMSS(getTimer()), (float) Math.ceil((double) getTimer() / 500 * 100));
                }
                break;
            default:
                for (Player player : getPlayers()) {
                    BarAPI.removeBar(player);
                }
        }
    }



    private void sendInfoMessage(){
        for(Player player:getPlayers()){
            player.sendMessage(ChatColor.BOLD + "----------- INFORMATION ------------");
            player.sendMessage(ChatManager.HIGHLIGHTED + "LEFT CLICK: "  );
            player.sendMessage(ChatManager.NORMAL + "  Lifts the clicked block!" + ChatManager.PREFIX + " (Cost: 1)");
            player.sendMessage(ChatManager.HIGHLIGHTED + "RIGHT CLICK:  "  );
            player.sendMessage(ChatManager.NORMAL + "  Shoots the flying block!"+ ChatManager.PREFIX + " (Cost: 0)");
            player.sendMessage(ChatManager.HIGHLIGHTED + "SHIFTED LEFT CLICK: " );
            player.sendMessage(ChatManager.NORMAL + "  Builds a wall in front of you!"+ ChatManager.PREFIX + " (Cost: 5)");
            player.sendMessage(ChatManager.HIGHLIGHTED + "SHIFTED RIGHT CLICK:  "  );
            player.sendMessage(ChatManager.NORMAL + "  Causes an earth wave in front of you!"+ ChatManager.PREFIX + " (Cost: 15)");
            player.sendMessage(ChatColor.BOLD + "-----------------------------------");
        }
    }

}
