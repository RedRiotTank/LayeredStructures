package htt.layeredstructures;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

public class ls implements CommandExecutor, TabCompleter {
    private final LayeredStructures plugin;

    public ls(LayeredStructures plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] commandArgs) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.DARK_AQUA + "[LayeredStructures]:" + ChatColor.WHITE + "This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) commandSender;

        if(commandArgs.length < 1){
            LayeredStructures.sendPlayerErrorMessage(player, "Not enough arguments. Use /ls help for more info");
            return true;
        }

        String order = commandArgs[0];

        switch (order) {
            case "help":
                printHelp(player);
                break;
            case "generate":
                if(commandArgs.length >= 5){
                    String jsonFileName = commandArgs[1];
                    String directionBuild = commandArgs[2];
                    String delay = commandArgs[3];
                    String tickCounter = commandArgs[4];
                    LayeredStructuresAPI.generateLayeredStructure(jsonFileName, player.getLocation(), directionBuild, Integer.parseInt(delay), Integer.parseInt(tickCounter));
                } else LayeredStructures.sendPlayerErrorMessage(player, "Not enough arguments. Use /ls help for more info");

               break;
            case "startSave":

                if(commandArgs.length >= 2){
                    String startOption = commandArgs[1];
                    switch (startOption){
                        case "initFile":
                            if(commandArgs.length >= 3){
                                String direction = commandArgs[2];
                                jsonManager.createJsonFile(player,direction);
                                SaveLayer.initProcedure(player);
                            } else LayeredStructures.sendPlayerErrorMessage(player, "Not enough arguments. Use /ls help for more info");
                            break;
                        case "c1":
                            SaveLayer.savec1(player);
                            break;
                        case "c2":
                            SaveLayer.savec2(player);
                            break;
                        default:
                            LayeredStructures.sendPlayerMessage(player, "Invalid command");
                            break;
                    }
                } else LayeredStructures.sendPlayerErrorMessage(player, "Not enough arguments. Use /ls help for more info");
                break;

            case "saveLayer":
                if (SaveLayer.getProcedures().containsKey(player.getName()))
                    SaveLayer.saveLayerStructure(player);
                else LayeredStructures.sendPlayerErrorMessage(player, "You don't have any started procedure. Use /ls help for more info");

                break;
            case "exportProcedure":
                if(commandArgs.length >= 2){
                    String fileName = commandArgs[1];
                    jsonManager.exportProcedure(player, fileName);
                } else
                    LayeredStructures.sendPlayerErrorMessage(player, "Not enough arguments. Use /ls help for more info");
                break;
            default:
                LayeredStructures.sendPlayerErrorMessage(player, "Invalid command. use /ls help for more info");
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            list.add("help");
            list.add("startSave");
            list.add("saveLayer");
            list.add("generate");
            list.add("exportProcedure");
        }

        if(args.length == 2){

            if(args[0].equalsIgnoreCase("exportProcedure")){
                list.add("filename");
            }

            if(args[0].equalsIgnoreCase("startSave")){
                list.add("initFile");
                list.add("c1");
                list.add("c2");


            }

            if(args[0].equalsIgnoreCase("generate")){
                list.add("filename");

            }

        }

        if(args.length == 3){
            if(args[0].equalsIgnoreCase("startSave") && args[1].equalsIgnoreCase("initFile")){
                list.add("north");
                list.add("south");
                list.add("east");
                list.add("west");
            }

            if(args[0].equalsIgnoreCase("generate")){
                list.add("north");
                list.add("south");
                list.add("east");
                list.add("west");
            }

        }

        if(args.length == 4){
            if(args[0].equalsIgnoreCase("generate")){
                list.add("delay");
            }
        }

        if(args.length == 5){
            if(args[0].equalsIgnoreCase("generate")){
                list.add("tickCounter");
            }
        }

        return list;
    }

    public void printHelp(Player player){
        LayeredStructures.sendPlayerMessage(player, "Help");
    }
}
