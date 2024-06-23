package com.dungeonmvc.models;

import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private ArrayList<Observer> observers;

    public enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    private int size;
    private Cell[][] board;
    private String floorImage;
    private String wallImage;
    private Player player;
    private List<Enemy> enemies;
    private List<Entorno> entornos;

    public Board(int size, String floorImage, String wallImage) {
        this.size = size;
        this.board = new Cell[size][size];
        this.floorImage = floorImage;
        this.wallImage = wallImage;
        this.enemies = new ArrayList<>();
        this.player = GameManager.getInstance().getPlayer();
        observers = new ArrayList<>();
        this.entornos = new ArrayList<>();

    }

    public void suscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsuscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.onChange();
        }
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFloorImage() {
        return this.floorImage;
    }

    public void setFloorImage(String floorImage) {
        this.floorImage = floorImage;
    }

    public String getWallImage() {
        return this.wallImage;
    }

    public void setWallImage(String wallImage) {
        this.wallImage = wallImage;
    }

    public boolean isFloor(Vector2 position) {
        return board[position.getX()][position.getY()].getIsFloor();
    }

    public boolean isFloor(int x, int y) {
        return board[x][y].getIsFloor();
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void newCell(Vector2 position, boolean isFloor) {
        Cell cell = new Cell(isFloor);
        board[position.getX()][position.getY()] = cell;
    }

    public Cell getCell(Vector2 position) {
        return board[position.getX()][position.getY()];
    }

    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        board[enemy.getPosition().getX()][enemy.getPosition().getY()].setInteractuable(enemy);
    }

    // movimiento del enemigo
    public void moveEnemy() {
        for (Enemy enemy : enemies) {
            enemy.moverEnemigo();
        }
    }

    // quitar img del enemigo
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        // Actualizar la celda del tablero
        Cell cell = getCell(enemy.getPosition());
        if (cell != null) {
            cell.setInteractuable(null);
        }
        if (enemies.isEmpty()) {
            GameManager.getInstance().Victory();
        }
    }

    // verificar si el movimiento es valido
    public boolean movimientoValido(Vector2 destination) {
        if (destination.getX() < 0 || destination.getX() >= size || destination.getY() < 0
                || destination.getY() >= size) {
            return false;
        }
        return board[destination.getX()][destination.getY()].getIsFloor();
    }

    public Vector2 getDestination(Vector2 position, Direction direction) {
        int destinoX = position.getX();
        int destinoY = position.getY();
        switch (direction) {
            case UP:
                destinoY--;
                break;
            case RIGHT:
                destinoX++;
                break;
            case DOWN:
                destinoY++;
                break;
            case LEFT:
                destinoX--;
                break;
            default:
                break;
        }
        return new Vector2(destinoX, destinoY);
    }

    public List<Entorno> getEntornos() {
        return this.entornos;
    }

    public void addEntorno(Entorno entorno) {
        this.entornos.add(entorno);
        board[entorno.getPosition().getX()][entorno.getPosition().getY()].setInteractuable(entorno);
    }

    // metodo para eliminar la imagen del objeto / entorno
    public void quitarEntorno(Entorno entorno) {
        Cell cell = getCell(entorno.getPosition());
        if (cell != null) {
            cell.setInteractuable(null);
        }
        this.entornos.remove(entorno);
        notifyObservers();
    }

}
