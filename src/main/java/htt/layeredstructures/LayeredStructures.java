package htt.layeredstructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public final class LayeredStructures extends JavaPlugin {



    @Override
    public void onEnable() {
        // Plugin startup logic

        getConfig().options().copyDefaults();
        saveDefaultConfig();



        saveLayer saveLayer = new saveLayer();

        registerCommands(saveLayer);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "LayeredStructures started correctly");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands(saveLayer saveLayer){
        Objects.requireNonNull(this.getCommand("ls")).setExecutor(new ls(this, saveLayer));

    }



}
