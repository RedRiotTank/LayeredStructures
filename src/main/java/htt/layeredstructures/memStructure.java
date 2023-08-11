package htt.layeredstructures;

import java.util.ArrayList;

public class memStructure {
    private ArrayList<ArrayList<TemporalBlock>> layerBlocksList;
    private String initialDirection;

    public memStructure(ArrayList<ArrayList<TemporalBlock>> layerBlocksList, String initialDirection) {
        this.layerBlocksList = layerBlocksList;
        this.initialDirection = initialDirection;
    }

    public ArrayList<ArrayList<TemporalBlock>> getLayerBlocksList() {
        return layerBlocksList;
    }

    public void setLayerBlocksList(ArrayList<ArrayList<TemporalBlock>> layerBlocksList) {
        this.layerBlocksList = layerBlocksList;
    }

    public String getInitialDirection() {
        return initialDirection;
    }

    public void setInitialDirection(String initialDirection) {
        this.initialDirection = initialDirection;
    }
}
