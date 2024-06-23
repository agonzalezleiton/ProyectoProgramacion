package com.dungeonmvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.dungeonmvc.models.*;
import com.dungeonmvc.models.Board.Direction;
import com.dungeonmvc.models.Character;
import com.dungeonmvc.utils.DiceRoll;
import com.dungeonmvc.utils.DiceRoll.Dice;
import com.dungeonmvc.utils.Vector2;

import javafx.application.Platform;

public class GameManager {
    private static GameManager instance;
    private Player player;
    private Board board;
    private Enemy enemy;
    private List<Character> characters;
    private List<Entorno> entornos;

    private GameManager() {
        characters = new ArrayList<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public List<Character> getCharacters() {
        return this.characters;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void newTurn(Direction direction) {
        for (Character character : characters) {
            if (character instanceof Player) {
                ((Player) character).move(board, direction);
                System.out.println("Turno de jugador");
            } else if (character instanceof Enemy) {
                ((Enemy) character).moverEnemigo();
                System.out.println("Turno de enemy");
            }
        }
        checkPlayerDefeat();
    }

    public void testGame() {
        if (board == null) {
            System.out.println("Error: El tablero no está inicializado.");
            return;
        }
        // añadir el player
        player = new Player("portrait", "player", "Asesino", 100, 15, 10, 7, "item7", "item6", new Vector2(0, 0));
        player.getInventory().addItem("item1");
        player.getInventory().addItem("item2");
        player.getInventory().addItem("item3");
        player.getInventory().addItem("item4");
        player.getInventory().addItem("item5");
        addCharacter(player);
        board.setPlayer(player);
        Collections.sort(characters);

        boolean[][] boardMatrix = {
                { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true },
                { true, false, false, false, true, false, false, false, false, false, false, false, false, false,
                        true },
                { true, false, true, false, true, false, true, true, true, true, true, true, false, true, true },
                { true, false, true, false, true, false, false, false, false, false, false, false, false, false, true },
                { true, false, true, false, true, false, true, true, true, true, true, true, false, true, true },
                { true, false, false, false, true, false, false, false, false, false, false, false, false, false,
                        true },
                { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true },
                { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true },
                { true, false, false, false, true, false, false, false, false, false, false, false, false, false,
                        true },
                { true, false, true, false, true, false, true, true, true, true, true, true, false, true, true },
                { true, false, true, false, true, false, false, false, false, false, false, false, false, false, true },
                { true, false, true, false, true, false, true, true, true, true, true, true, false, true, true },
                { true, false, false, false, true, false, false, false, false, false, false, false, false, false,
                        true },
                { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true },
                { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true }
        };
        board = new Board(boardMatrix.length, "floor", "wall");
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {
                board.newCell(new Vector2(i, j), boardMatrix[i][j]);
            }
        }
        // crear enemigo y añadirlo
        enemy = new Enemy("enemy", "Sombra", 100, DiceRoll.roll(Dice.d4), 10, 5, new Vector2(7, 0), board);
        board.addEnemy(enemy);
        addCharacter(enemy);
        // añadimos el objeto o entorno aqui
        Entorno health = new Entorno("health", new Vector2(14, 0));
        board.addEntorno(health);
        board.getCell(new Vector2(2, 13)).setInteractuable(health);
    }

    public void removeEnemy(Enemy enemy) {
        characters.remove(enemy);
        board.removeEnemy(enemy);
    }

    public void endGame() {
        System.out.println("El jugador ha sido derrotado. Fin del juego.");
    }

    public void checkPlayerDefeat() {
        if (player.getHealth() <= 0) {
            System.out.println("¡Has sido derrotado, perdiste!");
            Platform.exit();
        }
    }

    public void Victory() {
        System.out.println("¡Has derrotado a todos los enemigos! ¡Ganaste!");
        Platform.exit();
    }

    public void consumeEntorno(int row, int col) {
        Cell cell = board.getCell(new Vector2(row, col));
        if (cell.getInteractuable() instanceof Entorno) {
            Entorno entorno = (Entorno) cell.getInteractuable();
            // Curar al jugador
            player.setHealth(100);
            player.notifyObservers();
            board.quitarEntorno(entorno);
        }
    }
}
