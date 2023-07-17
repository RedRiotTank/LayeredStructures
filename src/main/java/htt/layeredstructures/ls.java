package htt.layeredstructures;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ls implements CommandExecutor, TabCompleter {
    private final LayeredStructures plugin;
    private final saveLayer saveLayer;
    public ls(LayeredStructures plugin, saveLayer saveLayer){
        this.plugin = plugin;
        this.saveLayer = saveLayer;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) commandSender;

        String order = strings[0];


        if(order.equals("generate")){
            String jsonFileName = strings[1];
            saveLayer.generateLayeredStructure(jsonFileName, plugin, player);
        } else if(order.equals("startSave")){
            System.out.println("startSave executed");

            String startOption = strings[1];


            if(startOption.equalsIgnoreCase("initFile")){

                saveLayer.createJsonFile(player.getName() + "Procedure.json");

            }

            else if(startOption.equalsIgnoreCase("c1")){
                saveLayer.savec1(player.getName());
            }

            else if(startOption.equalsIgnoreCase("c2")){
                saveLayer.savec2(player.getName());
            } else {
                player.sendMessage("Invalid command option");
            }




        } else if(order.equals("saveLayer")){
            System.out.println("saveLayer executed");


            if(saveLayer.getProcedures().containsKey(player.getName())){
                saveLayer.saveLayerStructure(player.getName());

            } else {
               player.sendMessage("You have to initialize the file first");
            }



        } else {
            player.sendMessage("Invalid command");
        }



        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            list.add("startSave");
            list.add("saveLayer");
            list.add("generate");
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("startSave")){
                list.add("initFile");
                list.add("c1");
                list.add("c2");
            }

            if(args[0].equalsIgnoreCase("generate"))
                list.add("filename");



        }
        return list;
    }
}
