package htt.layeredstructures;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public final class LayeredStructures extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        registerCommands();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "LayeredStructures started correctly");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands(){
        Objects.requireNonNull(this.getCommand("ls")).setExecutor(new ls(this));

    }
}
