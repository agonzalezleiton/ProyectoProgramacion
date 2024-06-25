package com.dungeonmvc.models;

import com.dungeonmvc.utils.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Cazador extends Character {
    private Enemy presa;
    private Board board;

    public Cazador(String image, String name, int health, int strength, int defense, int speed, Vector2 position,
                   Board board, Enemy presa) {
        super(image, name, health, strength, defense, speed, position);
        this.board = board;
        this.presa = presa;
        seleccionarNuevaPresa(); 
    }

    @Override
    public void interactWith(Character character) {
        if (character instanceof Enemy && character.equals(presa)) {
            combate((Enemy) character);
        }
    }

    // Realiza el combate 
    private void combate(Enemy enemy) {
        enemy.setHealth(enemy.getHealth() - this.getStrenght());
        if (enemy.getHealth() <= 0) {
            System.out.println(enemy.getName() + " fue derrotado por " + this.getName());
            seleccionarNuevaPresa();
        }
    }

    private void seleccionarNuevaPresa() {
        ArrayList<Enemy> enemigos = (ArrayList<Enemy>) board.getEnemies();
        if (!enemigos.isEmpty()) {
            Random random = new Random();
            presa = enemigos.get(random.nextInt(enemigos.size()));
        } else {
            presa = null;
        }
    }

    public void moverCazador() {
        if (presa != null) {
            Vector2 posicionPresa = presa.getPosition();
            Vector2 posicionCazador = this.getPosition();
            double distancia = Vector2.distancia(posicionPresa, posicionCazador);
            if (distancia <= 6) {
                moverHaciaPosicion(posicionPresa);
            } else {
                moverAleatoriamente();
            }
        } else {
            moverAleatoriamente();
        }
        board.notifyObservers();
    }
    // Movimiento aleatorio del cazador
    private void moverAleatoriamente() {
        Random random = new Random();
        Vector2[] posiblesMovimientos = {
                new Vector2(this.getPosition().getX() - 1, this.getPosition().getY()),
                new Vector2(this.getPosition().getX() + 1, this.getPosition().getY()),
                new Vector2(this.getPosition().getX(), this.getPosition().getY() - 1),
                new Vector2(this.getPosition().getX(), this.getPosition().getY() + 1)
        };

        Vector2 movimientoAleatorio = posiblesMovimientos[random.nextInt(posiblesMovimientos.length)];

        if (board.movimientoValido(movimientoAleatorio)) {
            board.getCell(this.getPosition()).setInteractuable(null);
            this.setPosition(movimientoAleatorio);
            board.getCell(movimientoAleatorio).setInteractuable(this);
        }
    }

    // Movimiento hacia una posición específica
    private void moverHaciaPosicion(Vector2 posicion) {
        Vector2 mejorMovimiento = null;
        double mejorDistancia = Double.MAX_VALUE;

        Vector2[] posiblesMovimientos = {
                new Vector2(this.getPosition().getX() - 1, this.getPosition().getY()),
                new Vector2(this.getPosition().getX() + 1, this.getPosition().getY()),
                new Vector2(this.getPosition().getX(), this.getPosition().getY() - 1),
                new Vector2(this.getPosition().getX(), this.getPosition().getY() + 1)
        };

        for (Vector2 movimiento : posiblesMovimientos) {
            if (board.movimientoValido(movimiento)) {
                double nuevaDistancia = Vector2.distancia(posicion, movimiento);
                if (nuevaDistancia < mejorDistancia) {
                    mejorDistancia = nuevaDistancia;
                    mejorMovimiento = movimiento;
                }
            }
        }

        if (mejorMovimiento != null) {
            board.getCell(this.getPosition()).setInteractuable(null);
            this.setPosition(mejorMovimiento);
            board.getCell(mejorMovimiento).setInteractuable(this);
        }
    }
}
