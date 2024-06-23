package com.dungeonmvc.models;

import java.util.ArrayList;

import com.dungeonmvc.interfaces.Observer;

public class Inventory {
    private ArrayList<Observer> observers;

    private final int inventoryMaxSize = 12;
    private ArrayList<String> items;

    public Inventory() {
        this.observers = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public int getInventoryMaxSize() {
        return this.inventoryMaxSize;
    }

    public int getInventorySize() {
        return this.items.size();
    }

    public void addItem(String item) {
        if (items.size() < inventoryMaxSize)
            this.items.add(item);
    }

    public String getItem(int index) {
        return this.items.get(index);
    }

    public void suscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsuscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        observers.forEach(x -> x.onChange());
    }
}