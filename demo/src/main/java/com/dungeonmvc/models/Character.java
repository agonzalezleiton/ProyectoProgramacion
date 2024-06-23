package com.dungeonmvc.models;

import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;

import java.util.ArrayList;

public class Character implements Interactuable, Comparable<Character> {
    String image;
    String name;
    int Health;
    int strength;
    int Defense;
    int speed;
    Vector2 position;

    public Character(String image, String name, int Health, int strength, int Defense, int speed, Vector2 position) {
        this.image = image;
        this.name = name;
        this.Health = Health;
        this.strength = strength;
        this.Defense = Defense;
        this.speed = speed;
        this.position = position;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.Health;
    }

    public void setHealth(int Health) {
        this.Health = Health;
    }

    public int getStrenght() {
        return this.strength;
    }

    public void setStrenght(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return this.Defense;
    }

    public void setDefense(int Defense) {
        this.Defense = Defense;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int compareTo(Character character) {
        return Integer.compare(character.getSpeed(), getSpeed());
    }

    public void interactWith(Character character) {
    }
}
