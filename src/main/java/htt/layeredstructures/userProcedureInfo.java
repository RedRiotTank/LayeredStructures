package htt.layeredstructures;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class userProcedureInfo{
    private int currentNumLayer;
    private String userName;

    private Location corner1, corner2;

    private int xReference, yReference, zReference;

    private ArrayList<Block> previusLayer;

    public userProcedureInfo(Location loc){
        currentNumLayer = 0;
        userName = "";
        corner1 = null;
        corner2 = null;
        xReference = loc.getBlockX();
        yReference = loc.getBlockY();
        zReference = loc.getBlockZ();
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
    public void setReference(Location loc){
        xReference = loc.getBlockX();
        yReference = loc.getBlockY();
        zReference = loc.getBlockZ();
    }

    public int getxReference() {
        return xReference;
    }

    public int getyReference() {
        return yReference;
    }

    public int getzReference() {
        return zReference;
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
