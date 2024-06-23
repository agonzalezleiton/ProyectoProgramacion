package com.dungeonmvc.models;

import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;

import java.util.ArrayList;

public class Player extends Character {
    ArrayList<Observer> observers;
    String portrait;
    String leftHand;
    String rightHand;
    Inventory inventory;

    public Player(String portrait, String image, String name, int Health, int strength, int defense, int speed,
            String leftHand, String rightHand, Vector2 position) {
        super(image, name, Health, strength, defense, speed, position);
        observers = new ArrayList<>();
        this.portrait = portrait;
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.inventory = new Inventory();
    }

    @Override
    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    @Override
    public void setHealth(int Health) {
        this.Health = Health;
        notifyObservers();
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

    public String getPortrait() {
        return this.portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
        notifyObservers();
    }

    public String getLeftHand() {
        return this.leftHand;
    }

    public void setLeftHand(String leftHand) {
        this.leftHand = leftHand;
        notifyObservers();
    }

    public String getRightHand() {
        return this.rightHand;
    }

    public void setRightHand(String rightHand) {
        this.rightHand = rightHand;
        notifyObservers();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void move(Board board, Board.Direction direction) {
        Vector2 destination = board.getDestination(this.getPosition(), direction);
        if (board.movimientoValido(destination)) {
            Cell destinationCell = board.getCell(destination);
            if (destinationCell.getInteractuable() != null) {
                destinationCell.getInteractuable().interactWith(this);
            } else {
                board.getCell(this.getPosition()).setInteractuable(null);
                this.setPosition(destination);
                board.getCell(destination).setInteractuable(this);
            }
            board.notifyObservers();
        }
    }

    public void Combate(Enemy enemy) {
        System.out.println(this.getName() + " interactua con " + enemy.getName());
        // Combate
        enemy.setHealth(enemy.getHealth() - this.getStrenght());
        this.setHealth(this.getHealth() - enemy.getStrenght());

        if (enemy.getHealth() <= 0) {
            System.out.println(enemy.getName() + " fue derrotado por " + this.getName());
            // quitar la img del enemigo del tablero
            enemy.getBoard().removeEnemy(enemy);
            enemy.getBoard().getCell(enemy.getPosition()).setInteractuable(null);
        }
        if (this.getHealth() <= 0) {
            System.out.println(this.getName() + " fue derootado por" + enemy.getName());
        }
        notifyObservers();
    }

    @Override
    public void interactWith(Character character) {
        if (character instanceof Enemy) {
            Combate((Enemy) character);
        }
    }

}