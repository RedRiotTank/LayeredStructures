package htt.layeredstructures;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class jsonManager {

    static String PATH = "plugins/LayeredStructures/"; //+ userName + "Procedure.json";
    static String procedureExtension = "Procedure.json";
    static String extension = ".json";
    public static void createJsonFile(Player player, String direction){
        String username = player.getName();
        File file = new File(PATH + username + procedureExtension);


        if(file.exists()){
            System.out.println(username + "just tried starting a new Layered Structures save procedure" +
                    "but something went wrong. Error: File already exists");
        } else {
            String layers = "{}";
            String jsonContent = "{\n" +
                    "  \"Direction\": \"" + direction + "\",\n" +
                    "  \"Layers\": " + layers + "\n" +
                    "}";

            try {
                FileWriter fileWriter = new FileWriter(PATH + username + procedureExtension);
                fileWriter.write(jsonContent);
                fileWriter.close();

                LayeredStructures.sendPlayerMessage(player, "New procedure started. File created correctly");
                LayeredStructures.sendConsoleMessage(username + " just started a new Layered Structures save procedure");


            } catch (IOException e) {
                System.out.println(username + "just tried starting a new Layered Structures save procedure" +
                        "but something went wrong. Error: " + e.getMessage());
            }


        }


    }


    public static void deleteCurrentProcedureFile(String username){

        File file = new File(PATH + username + procedureExtension);

        if(file.exists()){
            file.delete();
        }

    }

    public static void exportProcedure(Player player, String fileName){
        String userName = player.getName();

        File file = new File(PATH + userName + procedureExtension);

        if(file.exists()){
            file.renameTo(new File(PATH + fileName + extension));
            SaveLayer.getProcedures().remove(userName);

            LayeredStructures.sendPlayerMessage(player, "Exported successfully");
            LayeredStructures.sendConsoleMessage(player.getName() + " exported a new layered structure successfully");

        } else {
            LayeredStructures.sendConsoleMessage(player.getName() + " just tried exporting a Layered Structures save procedure" +
                    "but something went wrong. Error: File does not exist");
            LayeredStructures.sendPlayerMessage(player, "You don't have any started procedure to export. Use /ls help for more info");

        }

    }

}
