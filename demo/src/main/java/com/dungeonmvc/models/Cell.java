package com.dungeonmvc.models;

public class Cell {
    private boolean isFloor;
    private Interactuable interactuable;

    public Cell(boolean isFloor) {
        this.isFloor = isFloor;
        this.interactuable = null;
    }

    public boolean isIsFloor() {
        return this.isFloor;
    }

    public boolean getIsFloor() {
        return this.isFloor;
    }

    public void setIsFloor(boolean isFloor) {
        this.isFloor = isFloor;
    }

    public Interactuable getInteractuable() {
        return this.interactuable;
    }

    public void setInteractuable(Interactuable interactuable) {
        this.interactuable = interactuable;
    }
}
