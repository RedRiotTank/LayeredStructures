package htt.layeredstructures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface LayeredStructuresAPI {


     static void generateLayeredStructure(String jsonFileName, LayeredStructures plugin, Player player, String desiredDirection, int delay, int tickCounter) {
        String PATH = "plugins/LayeredStructures/" + jsonFileName + ".json";
        File file = new File(PATH);

        System.out.println(file);

        if (file.exists()) {
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(PATH)));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject jsonObject = gson.fromJson(fileContent, JsonObject.class);

                // Get the initial direction from the JSON
                String initialDirection = jsonObject.get("Direction").getAsString();

                // Calculate the rotation needed to align the initial direction with the desired direction
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

                // Apply the rotation to each block in the structure
                int finalRotation = rotation;
                new BukkitRunnable() {
                    int layerCounter = 0;
                    int maxLayers = jsonObject.getAsJsonObject("Layers").size();
                    Location playerLaunchedLocation = player.getLocation();

                    // Helper method to rotate a position based on the specified rotation
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

                            Block newBlock = player.getWorld().getBlockAt(rotatedPos[0] + playerLaunchedLocation.getBlockX(), y + playerLaunchedLocation.getBlockY(), rotatedPos[1] + playerLaunchedLocation.getBlockZ());
                            newBlock.setType(Material.getMaterial(type));
                        });

                        System.out.println("A");
                        layerCounter++;
                    }
                }.runTaskTimer(plugin, delay, tickCounter);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            player.sendMessage("The file " + jsonFileName + " doesn't exist");
        }
    }

}
