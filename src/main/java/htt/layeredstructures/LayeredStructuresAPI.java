package htt.layeredstructures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LayeredStructuresAPI {

    private static Plugin plug;

    public static void initialize(Plugin p) {
        plug = p;
    }

    public static void generateLayeredStructure(String jsonFileName, Location loc, String desiredDirection, int delay, int tickCounter) {
        String PATH = "plugins/LayeredStructures/" + jsonFileName + ".json";
        File file = new File(PATH);



        if (file.exists()) {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(PATH)));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject jsonObject = gson.fromJson(fileContent, JsonObject.class);


                String initialDirection = jsonObject.get("Direction").getAsString();


                int rotation = 0;
                switch (initialDirection.toLowerCase()) {
                    case "north":
                        switch (desiredDirection.toLowerCase()) {
                            case "north": rotation = 0; break;
                            case "east":  rotation = 90; break;
                            case "south": rotation = 180; break;
                            case "west":  rotation = 270; break;
                        }
                        break;
                    case "east":
                        switch (desiredDirection.toLowerCase()) {
                            case "north": rotation = 270; break;
                            case "east":  rotation = 0; break;
                            case "south": rotation = 90; break;
                            case "west":  rotation = 180; break;
                        }
                        break;
                    case "south":
                        switch (desiredDirection.toLowerCase()) {
                            case "north": rotation = 180; break;
                            case "east":  rotation = 270; break;
                            case "south": rotation = 0; break;
                            case "west":  rotation = 90; break;
                        }
                        break;
                    case "west":
                        switch (desiredDirection.toLowerCase()) {
                            case "north": rotation = 90; break;
                            case "east":  rotation = 180; break;
                            case "south": rotation = 270; break;
                            case "west":  rotation = 0; break;
                        }
                        break;
                }


                int finalRotation = rotation;
                new BukkitRunnable() {
                    int layerCounter = 0;
                    int maxLayers = jsonObject.getAsJsonObject("Layers").size();
                    World world = loc.getWorld();


                    private int[] rotatePosition(int x, int z, int rotation) {
                        double angle = Math.toRadians(rotation);
                        int[] rotatedPos = new int[2];
                        rotatedPos[0] = (int) Math.round(x * Math.cos(angle) - z * Math.sin(angle));
                        rotatedPos[1] = (int) Math.round(x * Math.sin(angle) + z * Math.cos(angle));
                        return rotatedPos;
                    }


                    @Override
                    public void run() {
                        if (layerCounter == maxLayers) {
                            this.cancel();
                            return;
                        }

                        JsonObject layer = jsonObject.getAsJsonObject("Layers").getAsJsonObject(Integer.toString(layerCounter));

                        layer.getAsJsonArray("blocks").forEach(block -> {
                            JsonArray blockInfo = block.getAsJsonArray();

                            int x = blockInfo.get(0).getAsInt();
                            int y = blockInfo.get(1).getAsInt();
                            int z = blockInfo.get(2).getAsInt();
                            String type = blockInfo.get(3).getAsString();

                            // Rotate the position based on the specified rotation
                            int[] rotatedPos = rotatePosition(x, z, finalRotation);

                            Block newBlock = world.getBlockAt(rotatedPos[0] + loc.getBlockX(), y + loc.getBlockY(), rotatedPos[1] + loc.getBlockZ());
                            newBlock.setType(Material.getMaterial(type));
                        });


                        layerCounter++;
                    }
                }.runTaskTimer(plug, delay, tickCounter);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            LayeredStructures.sendConsoleMessage("The file " + jsonFileName + ".json does not exist in the LayeredStructures folder.");
        }
    }
    public static void test(){
        System.out.println("test");

    }



}

