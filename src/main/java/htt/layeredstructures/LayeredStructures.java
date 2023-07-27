package htt.layeredstructures;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


 public final class LayeredStructures extends JavaPlugin {
    @Override
    public void onEnable() {


        getConfig().options().copyDefaults();
        saveDefaultConfig();

        registerCommands();

        LayeredStructuresAPI.initialize(this);


        sendConsoleMessage("LayeredStructures started correctly");
    }

    public LayeredStructures getLayered(){
        return this;
    }


    @Override
    public void onDisable() {
        deleteProcedures();
        sendConsoleMessage("LayeredStructures stopped correctly");
    }

    public void deleteProcedures(){
        for(String user : SaveLayer.getProcedures().keySet()){
            jsonManager.deleteCurrentProcedureFile(user);
        }

    }

    public void registerCommands(){
        Objects.requireNonNull(this.getCommand("ls")).setExecutor(new ls(this));
    }

    public static void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[LayeredStructures]: " + ChatColor.WHITE +  message);
    }

    public static void sendPlayerMessage(Player player, String message){
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[LayeredStructures]: " + ChatColor.GREEN + "" + message);
    }

    public static void sendPlayerErrorMessage(Player player, String message){
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "[LayeredStructures]: " + ChatColor.RED + "" +  message);
    }



}
