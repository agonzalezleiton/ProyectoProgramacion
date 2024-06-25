package com.dungeonmvc.models;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.utils.Vector2;

public class Entorno implements Interactuable {
    private String image;
    private Vector2 position;

    public Entorno(String image, Vector2 position) {
        this.image = image;
        this.position = position;
    }

    @Override
    public void interactWith(Character character) {
        if (character instanceof Player) {
            Player player = (Player) character;
            // Curar al jugador
            player.setHealth(100);
            // Eliminar la poción del tablero y de la vista
            GameManager.getInstance().consumeEntorno(this.getPosition().getX(), this.getPosition().getY());
        }
    }
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
