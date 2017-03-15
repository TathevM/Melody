package com.team_red.melody.Models;


public class Composition {

    private int compositionID;
    private String compositionName;
    private int compositorID;
    private String jsonFileName;
    private String path;


    public Composition(int compositionID, String compositionName, int compositorID, String jsonFileName, String path) {
        this.compositionID = compositionID;
        this.compositionName = compositionName;
        this.compositorID = compositorID;
        this.jsonFileName = jsonFileName;
        this.path = path;
    }

    public int getCompositionID() {
        return compositionID;
    }

    public void setCompositionID(int compositionID) {
        this.compositionID = compositionID;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public int getCompositorID() {
        return compositorID;
    }

    public void setCompositorID(int compositorID) {
        this.compositorID = compositorID;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
