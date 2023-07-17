package htt.layeredstructures;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class saveLayer {
    private Map<String, userProcedureInfo> procedures;
    saveLayer(){
        procedures = new HashMap<String, userProcedureInfo>();
    }

    public void createJsonFile(String fileName){



        String layers = "{}";

        String jsonContent = "{\n" +
                "  \"Layers\": " + layers + "\n" +
                "}";

        try {
            FileWriter fileWriter = new FileWriter("plugins/LayeredStructures/" + fileName);
            fileWriter.write(jsonContent);
            fileWriter.close();
            System.out.println("File " + fileName + " has been created. correctly");
        } catch (IOException e) {
            System.out.println("Error creating JSON file: " + e.getMessage());
        }
    }

   public void savec1(String name){

        if(!procedures.containsKey(name)){
            procedures.put(name, new userProcedureInfo());
            procedures.get(name).userName = name;
            procedures.get(name).corner1 = Bukkit.getPlayer(name).getLocation();
            Bukkit.getPlayer(name).sendMessage("Corner 1 saved");
        } else {
            Bukkit.getPlayer(name).sendMessage("You already have a procedure. Finish it to start a new one please.");
        }

   }

   public void savec2(String name){
        if(procedures.containsKey(name)){
            procedures.get(name).corner2 = Bukkit.getPlayer(name).getLocation();
            Bukkit.getPlayer(name).sendMessage("Corner 2 saved");
        } else {
            Bukkit.getPlayer(name).sendMessage("You don't have a procedure. Start one with /ls startSave c1");
        }
   }

    public Map<String, userProcedureInfo> getProcedures() {
        return procedures;
    }

    public  void saveLayerStructure(String user) {
        World world = Bukkit.getPlayer(user).getWorld();

        ArrayList<Block> newLayer = new ArrayList<>();

        ArrayList<Block> previsLayer = procedures.get(user).previusLayer;

        int currentNumLayer = procedures.get(user).currentNumLayer;

        if (previsLayer != null && currentNumLayer != 0) {
            for (Block block : previsLayer) {
                block.setType(Material.AIR);
            }
        }

        Location loc1 = procedures.get(user).corner1;
        Location loc2 = procedures.get(user).corner2;

        System.out.println("loc1: " + loc1);
        System.out.println("loc2: " + loc2);



        String PATH = "plugins/LayeredStructures/" + user + "Procedure.json";

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


            //System.out.println("Las medidas son: " + x + " " + y + " " + z);

            while (blockIterator.hasNext()) {
                Block block = blockIterator.next();

                if(!block.getType().equals(Material.AIR)){
                    newLayer.add(block);

                    JsonArray blockInfo = new JsonArray();

                    blockInfo.add(block.getX() - x_reference);

                    //System.out.println("Block y: " + block.getY() + "% " +  y + " = " + block.getY() % y);
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







        procedures.get(user).currentNumLayer++;
        procedures.get(user).previusLayer = newLayer;


    }


    public void generateLayeredStructure(String jsonFileName, LayeredStructures plugin, Player player){

        World world = player.getWorld();

        String PATH = "plugins/LayeredStructures/" + jsonFileName + ".json";

        ArrayList<ArrayList<Block>> layers = new ArrayList<>();



        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(PATH)));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gson.fromJson(fileContent, JsonObject.class);
            AtomicInteger layerCounter = new AtomicInteger();

            jsonObject.getAsJsonObject().get("Layers").getAsJsonObject().entrySet().forEach(entry -> {
                layers.add(new ArrayList<>());

                entry.getValue().getAsJsonObject().get("blocks").getAsJsonArray().forEach(block -> {
                    JsonArray blockInfo = block.getAsJsonArray();

                    int x = blockInfo.get(0).getAsInt();
                    int y = blockInfo.get(1).getAsInt();
                    int z = blockInfo.get(2).getAsInt();

                    String type = blockInfo.get(3).getAsString();

                    Block newBlock = player.getWorld().getBlockAt(x + player.getLocation().getBlockX(), y + player.getLocation().getBlockY(), z + player.getLocation().getBlockZ());
                    newBlock.setType(Material.getMaterial(type));

                    layers.get(layerCounter.get()).add(newBlock);
                });

                layerCounter.getAndIncrement();
            });




        } catch (IOException e) {
            e.printStackTrace();
        }



        new BukkitRunnable(){
            @Override
            public void run() {

                for(ArrayList<Block> layer : layers){
                    for(Block block : layer){
                        world.getBlockAt(block.getLocation()).setType(block.getType());

                    }
                }



            }
        }.runTaskTimer(plugin, 0, 60);


    }



    public class userProcedureInfo{
        private int currentNumLayer;
        private String userName;

        private Location corner1, corner2;

        private ArrayList<Block> previusLayer;

        public userProcedureInfo(){
            currentNumLayer = 0;
            userName = "";
            corner1 = null;
            corner2 = null;
            previusLayer = new ArrayList<>();
        }

        public int getCurrentNumLayer() {
            return currentNumLayer;
        }

        public String getUserName() {
            return userName;
        }

        public Location getCorner1() {
            return corner1;
        }

        public Location getCorner2() {
            return corner2;
        }

        public ArrayList<Block> getPreviusLayer() {
            return previusLayer;
        }

        public void setCurrentNumLayer(int currentNumLayer) {
            this.currentNumLayer = currentNumLayer;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setCorner1(Location corner1) {
            this.corner1 = corner1;
        }

        public void setCorner2(Location corner2) {
            this.corner2 = corner2;
        }

        public void setPreviusLayer(ArrayList<Block> previusLayer) {
            this.previusLayer = previusLayer;
        }
    }



}
