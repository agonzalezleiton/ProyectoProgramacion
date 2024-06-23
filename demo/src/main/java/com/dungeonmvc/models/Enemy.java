package com.dungeonmvc.models;

import com.dungeonmvc.utils.Vector2;

import java.util.Random;

public class Enemy extends Character {
    private Board board;

    public Enemy(String image, String name, int Health, int strength, int defense, int speed, Vector2 position,
            Board board) {
        super(image, name, Health, strength, defense, speed, position);
        this.board = board;
    }

    @Override
    public void interactWith(Character character) {
        if (character instanceof Player) {
            Combate((Player) character);
        }
    }

    public void Combate(Player player) {
        System.out.println(this.getName() + " interactua con " + player.getName());
        // Combatee
        player.setHealth(player.getHealth() - this.getStrenght());
        this.setHealth(this.getHealth() - player.getStrenght());

        if (player.getHealth() <= 0) {
            System.out.println(player.getName() + " fue derrotado por" + this.getName());
        }

        if (this.getHealth() <= 0) {
            System.out.println(this.getName() + " fue derrotado por " + player.getName());
            board.removeEnemy(this);
            board.getCell(this.getPosition()).setInteractuable(null);
            return;
        }

        player.notifyObservers();
    }

    public void moverEnemigo() {
        // Verificar si el enemigo está sin vida
        if (this.getHealth() <= 0) {
            return;
        }
        Vector2 posicionJugador = board.getPlayer().getPosition();
        Vector2 posicionEnemigo = this.getPosition();

        double distancia = Vector2.distancia(posicionJugador, posicionEnemigo);

        if (distancia <= 3) {
            moverHaciaJugador(posicionJugador, posicionEnemigo);
        } else {
            moverAleatoriamente();
        }
        board.notifyObservers();
    }

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
        } else {
        }
    }

    private void moverHaciaJugador(Vector2 posicionJugador, Vector2 posicionEnemigo) {
        Vector2 mejorMovimiento = null;
        double mejorDistancia = Double.MAX_VALUE;

        Vector2[] posiblesMovimientos = {
                new Vector2(posicionEnemigo.getX() - 1, posicionEnemigo.getY()),
                new Vector2(posicionEnemigo.getX() + 1, posicionEnemigo.getY()),
                new Vector2(posicionEnemigo.getX(), posicionEnemigo.getY() - 1),
                new Vector2(posicionEnemigo.getX(), posicionEnemigo.getY() + 1)
        };

        for (Vector2 movimiento : posiblesMovimientos) {
            if (board.movimientoValido(movimiento)) {
                double nuevaDistancia = Vector2.distancia(posicionJugador, movimiento);
                if (nuevaDistancia < mejorDistancia) {
                    mejorDistancia = nuevaDistancia;
                    mejorMovimiento = movimiento;
                }
            }
        }

        if (mejorMovimiento != null) {
            Cell destinationCell = board.getCell(mejorMovimiento);
            if (destinationCell.getInteractuable() instanceof Player) {
                // Si la celda esta ocupada, interactua con el player
                destinationCell.getInteractuable().interactWith(this);
            } else {
                board.getCell(this.getPosition()).setInteractuable(null);
                this.setPosition(mejorMovimiento);
                board.getCell(mejorMovimiento).setInteractuable(this);
            }
        }
    }

    public Board getBoard() {
        return this.board;
    }
}
