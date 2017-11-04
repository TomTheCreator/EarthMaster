package me.TomTheDeveloper;

import me.TomTheDeveloper.Events.*;
import me.TomTheDeveloper.Handlers.ConfigurationManager;
import me.TomTheDeveloper.Handlers.SignManager;
import me.TomTheDeveloper.Kits.AirMaster;
import me.TomTheDeveloper.Kits.EarthKit;
import me.TomTheDeveloper.Kits.FireMaster;
import me.TomTheDeveloper.Kits.WaterMaster;
import me.TomTheDeveloper.Utils.Items;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Tom on 25/07/2014.
 */
public class EarthMaster extends GameAPI {


    private FallingBlockHitsPlayerListener fallingBlockHitsPlayerListener;





    public EarthMaster() {


    }

    @Override
    public void setupMessageConfig() {
        
    }


    @Override
    public void onPreStart(){
        this.disalbeKits();
        this.setNeedsMapRestore(true);
        setGameName( "EarthMaster");
    }


    @Override
    public void onStart() {


        FallingBlockHitsPlayerListener.plugin = this;



        this.getServer().getPluginManager().registerEvents(new onBlockBreak(this), this);
        this.getServer().getPluginManager().registerEvents(new onBlockPlace(this), this);
        this.getServer().getPluginManager().registerEvents(new onPlayerInteract(this), this);
        this.getServer().getPluginManager().registerEvents(new onPlayerInteractEntity(this), this);
        this.getServer().getPluginManager().registerEvents(new onDie(this), this);
        this.getServer().getPluginManager().registerEvents(new onCancelledEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new onSpectateMenu(this), this);
        this.getServer().getPluginManager().registerEvents(new onRespawn(this), this);
        this.getServer().getPluginManager().registerEvents(new onTest(this), this);

       // EarthKit earthMaster = new EarthKit(this);
        //FireMaster fireMaster = new FireMaster(this);
        //WaterMaster waterMaster = new WaterMaster(this);
        //AirMaster airMaster = new AirMaster(this);



        this.getSignManager().start();

        getKitMenuHandler().setMenuName("Kit Menu");
        getKitMenuHandler().setItemName("Open Kit Menu");
        getKitMenuHandler().setMaterial(Material.NETHER_STAR);
        getKitMenuHandler().setDescription(new String[]{"Right click to open menu!"});
        fallingBlockHitsPlayerListener = new FallingBlockHitsPlayerListener();
        fallingBlockHitsPlayerListener.start();

        this.getAttackListener().start();
        loadInstances();
        System.out.print(  getGameInstanceManager().getGameInstances().size() + " Game Instances loaded!");





        System.out.print(getGameInstanceManager().getGameInstances());


    }

    @Override
    public void onStop() {

    }



    private void loadInstances(){
        for(String ID:this.getConfig().getConfigurationSection("instances").getKeys(false)){
            EarthMasterInstance earthMasterInstance;
            String s = "instances." + ID + ".";
            if(s.contains("default"))
                continue;


                earthMasterInstance = new EarthMasterInstance(ID );


            if(getConfig().contains(s + "minimumplayers"))
                earthMasterInstance.setMIN_PLAYERS(getConfig().getInt(s +"minimumplayers"));
            else
                earthMasterInstance.setMIN_PLAYERS(getConfig().getInt("instances.default.minimumplayers"));
            if(getConfig().contains(s + "maximumplayers"))
                earthMasterInstance.setMAX_PLAYERS(getConfig().getInt(s + "maximumplayers"));
            else
                earthMasterInstance.setMAX_PLAYERS(getConfig().getInt("instances.default.maximumplayers"));
            if(getConfig().contains(s + "mapname"))
                earthMasterInstance.setMapName(getConfig().getString(s + "mapname"));
            else
                earthMasterInstance.setMapName(getConfig().getString("instances.default.mapname"));
            if(getConfig().contains(s + "lobbylocation"))
                earthMasterInstance.setLobbyLocation(getLocation(s + "lobbylocation"));
            if(getConfig().contains(s + "Startlocation"))
                earthMasterInstance.setStartLocation(getLocation(s + "Startlocation"));
            if(getConfig().contains(s + "Endlocation"))
                earthMasterInstance.setEndLocation(getLocation(s + "Endlocation"));

            if(needsMapRestore() && getConfig().contains(s + "schematic")){
                if(!getConfig().getString(s + "schematic").contains(" schematic")) {
                    earthMasterInstance.setSchematicName(getConfig().getString(s + "schematic"));
                }else{
                    System.out.print("You need to assign a schematic file to the arena" + s+ ". You can do this in the config or with the ingame-command /earthmaster <arena> set schematic <name of file without .schematic!>");
                    continue;

                }
            }else{
                if(needsMapRestore()){
                    System.out.print("No schematic found for arena " + s + ". You need to assign an schematic file to that arena! You can do this with the ingame-command /earthmaster <arena> set schematic <name of file without .schematic!>");
                    continue;
                }
            }
            this.getGameInstanceManager().registerGameInstance(earthMasterInstance);
             earthMasterInstance.start();


        }
    }



}
