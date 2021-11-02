package com.justserver.apocalypse;

import com.justserver.apocalypse.base.Base;
import com.justserver.apocalypse.base.BaseCommand;
import com.justserver.apocalypse.base.BaseHandler;
import com.justserver.apocalypse.commands.AddItemCommand;
import com.justserver.apocalypse.commands.DungeonCommand;
import com.justserver.apocalypse.commands.DungeonCommand;
import com.justserver.apocalypse.dungeons.DungeonHandler;
import com.justserver.apocalypse.dungeons.dungs.GeneratedDungeon;
import com.justserver.apocalypse.dungeons.dungs.SuperDungeon;
import com.justserver.apocalypse.gui.GuiManager;
import com.justserver.apocalypse.items.GunHandler;
import com.justserver.apocalypse.overworld.OverworldHandler;
import com.justserver.apocalypse.utils.CustomConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;

public final class Apocalypse extends JavaPlugin {

    public SuperDungeon superDungeon = new SuperDungeon(this);
    public HashMap<Player, GeneratedDungeon> inDungeon = new HashMap<>();
    public CustomConfiguration bases = new CustomConfiguration(this, "bases.yml");
    public CustomConfiguration lockers = new CustomConfiguration(this, "lockers.yml");
    public GuiManager guiManager = new GuiManager();
    public ArrayList<Base> loadedBases = new ArrayList<>();

    @Override
    public void onEnable() {
        Registry.init(this);
        getCommand("startdungeon").setExecutor(new DungeonCommand(this));
        getCommand("base").setExecutor(new BaseCommand(this));
        getCommand("additem").setExecutor(new AddItemCommand());
        Bukkit.getPluginManager().registerEvents(new DungeonHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new GunHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new BaseHandler(this), this);
        Bukkit.getPluginManager().registerEvents(guiManager, this);
        Bukkit.getPluginManager().registerEvents(new OverworldHandler(this), this);
        for(String key : bases.config.getConfigurationSection("bases").getKeys(false)){
            Base base = new Base(this);
            base.id = bases.config.getString("bases." + key + ".id");
            base.owner = UUID.fromString(bases.config.getString("bases." + key + ".owner"));
            List<String> stringUUIDs = bases.config.getStringList("bases." + key + ".players");
            for(String stringUUID : stringUUIDs){
                base.players.add(UUID.fromString(stringUUID));
            }
            base.location = bases.config.getLocation("bases." + key + ".location");
            base.blocks = (ArrayList<HashMap<String, Object>>) bases.config.getList("bases." + key + ".blocks");
            base.duration = Instant.ofEpochSecond(bases.config.getLong("bases." + key + ".duration"));
            System.out.println(base.duration);
            loadedBases.add(base);
        }
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < loadedBases.size(); i++) {
                    Base loadedBase = loadedBases.get(i);
                    if(loadedBase.duration.isBefore(Instant.now())){
                        loadedBase.remove();
                        i--;
                    }
                }

            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        World lastWorld = null;
        for(Map.Entry<UUID, ItemStack> entry : Registry.FLYING_AXE.getThrownAxes().entrySet()){
            if(Bukkit.getPlayer(entry.getKey()) != null){
                Bukkit.getPlayer(entry.getKey()).getInventory().addItem(entry.getValue());
            }
            lastWorld = Bukkit.getPlayer(entry.getKey()).getWorld();
        }
        if(lastWorld == null) return;
        for(Entity entity : lastWorld.getEntities()){
            if(entity instanceof ArmorStand){
                entity.remove();
            }
        }
    }
}
