package htt.layeredstructures;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.FileWriter;
import java.io.IOException;


public class SaveLayer {
    private static Map<String, userProcedureInfo> procedures = new HashMap<String, userProcedureInfo>();
    SaveLayer(){}

    public static void initProcedure(Player player){

        String username = player.getName();


        if(!SaveLayer.getProcedures().containsKey(username)){
            SaveLayer.getProcedures().put(username, new userProcedureInfo());
            SaveLayer.getProcedures().get(username).setUserName(username);
        } else {
            LayeredStructures.sendPlayerErrorMessage(player, "You already have a procedure. Export or cancel it to start a new one please.");

        }
    }



   public static void savec1(Player player){
        String name = player.getName();
       Location playerLoc = player.getLocation();

        if(procedures.containsKey(name)){
            procedures.get(name).setCorner1(playerLoc);
            LayeredStructures.sendPlayerMessage(player, "Corner 1 saved");

        } else {
            LayeredStructures.sendPlayerErrorMessage(player, "You don't have any started procedure. Use /ls help for more info");
        }

   }

    public static void savec2(Player player){
        String name = player.getName();
        Location playerLoc = player.getLocation();

        if(procedures.containsKey(name)){
            procedures.get(name).setCorner2(playerLoc);
            LayeredStructures.sendPlayerMessage(player, "Corner 2 saved");
        } else {
            LayeredStructures.sendPlayerErrorMessage(player, "You don't have any started procedure. Use /ls help for more info");
        }

    }


    public static Map<String, userProcedureInfo> getProcedures() {
        return procedures;
    }

    public static void saveLayerStructure(Player player) {

        String userName = player.getName();


        ArrayList<Block> newLayer = new ArrayList<>();

        ArrayList<Block> previsLayer = procedures.get(userName).getPreviusLayer();

        int currentNumLayer = procedures.get(userName).getCurrentNumLayer();

        if (previsLayer != null && currentNumLayer != 0) {
            for (Block block : previsLayer) {
                block.setType(Material.AIR);
            }
        }

        Location loc1 = procedures.get(userName).getCorner1();
        Location loc2 = procedures.get(userName).getCorner2();


        String PATH = "plugins/LayeredStructures/" + userName + "Procedure.json";

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(PATH)));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gson.fromJson(fileContent, JsonObject.class);

            // Verificar si ya existen capas en el JSON
            if (!jsonObject.has("Layers") || !jsonObject.get("Layers").isJsonObject()) {
                jsonObject.add("Layers", new JsonObject());
            }


            // Agregar las capas si no existen

            if (!jsonObject.getAsJsonObject("Layers").has(String.valueOf(currentNumLayer))) {
                jsonObject.getAsJsonObject("Layers").add(String.valueOf(currentNumLayer), new JsonObject());
            }

            Cuboid cuboid = new Cuboid(loc1, loc2);
            Iterator<Block> blockIterator = cuboid.blockList();

            JsonObject layer = jsonObject.getAsJsonObject("Layers").getAsJsonObject(String.valueOf(currentNumLayer));

            layer.add("blocks", new JsonArray());

            JsonArray blocks = layer.getAsJsonArray("blocks");

            int x_reference = loc1.getBlockX();
            int y_reference = loc1.getBlockY();
            int z_reference = loc1.getBlockZ();




            while (blockIterator.hasNext()) {
                Block block = blockIterator.next();

                if(!block.getType().equals(Material.AIR)){
                    newLayer.add(block);

                    JsonArray blockInfo = new JsonArray();

                    blockInfo.add(block.getX() - x_reference);

                    blockInfo.add(block.getY() - y_reference);


                    blockInfo.add(block.getZ() - z_reference);
                    blockInfo.add(block.getType().toString());
                    blocks.add(blockInfo);


                }
            }


            FileWriter fileWriter = new FileWriter(PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        procedures.get(userName).setCurrentNumLayer(procedures.get(userName).getCurrentNumLayer() + 1);
        procedures.get(userName).setPreviusLayer(newLayer);

        LayeredStructures.sendPlayerMessage(player, "Layer saved");

    }













}
