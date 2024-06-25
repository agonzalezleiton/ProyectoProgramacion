package com.dungeonmvc.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dungeonmvc.App;
import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Board;
import com.dungeonmvc.models.Enemy;
import com.dungeonmvc.models.Entorno;
import com.dungeonmvc.models.Character;
import com.dungeonmvc.utils.Vector2;
import com.dungeonmvc.utils.Vector2Double;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardViewController implements Observer {
    @FXML
    private Pane pane;
    @FXML
    private GridPane grid;

    private Board board;
    private double cellSize;
    private double boardSize;
    private Map<Character, ImageView> characterImageViews;
    private Map<Entorno, ImageView> entornoImageViews;
    private ImageView playerImg;

    @FXML
    private void initialize() {
        System.out.println("Board controller loaded");
        board = GameManager.getInstance().getBoard();
        board.suscribe(this);
        characterImageViews = new HashMap<>();
        entornoImageViews = new HashMap<>();
    }
    // metodos para añadir las imagenes tanto de player como enemigos como entornos u objetos.
    public void setUp() {
        int cellNumber = board.getSize();
        cellSize = boardSize / cellNumber;

        for (int i = 0; i < cellNumber; i++) {
            grid.addRow(i);
            grid.addColumn(i);
        }

        for (int row = 0; row < cellNumber; row++) {
            for (int col = 0; col < cellNumber; col++) {
                ImageView boardImg = new ImageView();
                boardImg.setFitWidth(cellSize);
                boardImg.setFitHeight(cellSize);
                boardImg.setSmooth(false);

                String imagePath = board.isFloor(row, col) ? "images/" + board.getFloorImage() + ".png"
                        : "images/" + board.getWallImage() + ".png";
                URL resource = App.class.getResource(imagePath);
                if (resource == null) {
                    System.err.println("Resource not found: " + imagePath);
                    continue;
                }

                boardImg.setImage(new Image(resource.toExternalForm(), cellSize, cellSize, true, false));
                grid.add(boardImg, row, col);
            }
        }
            // imagenes de los personajes incluidos que sean png
        for (Character character : GameManager.getInstance().getCharacters()) {
            ImageView characterImg = new ImageView();
            characterImg.setFitWidth(cellSize);
            characterImg.setFitHeight(cellSize);

            String characterImagePath = "images/" + character.getImage() + ".png";
            URL resource = App.class.getResource(characterImagePath);
            if (resource == null) {
                continue;
            }

            characterImg.setImage(new Image(resource.toExternalForm(), cellSize, cellSize, true, false));
            characterImg.setSmooth(false);
            characterImageViews.put(character, characterImg);
            pane.getChildren().add(characterImg);
        }
        // imagenes del entorno que sean gifs 
        for (Entorno entorno : board.getEntornos()) {
            ImageView entornoImg = new ImageView();
            entornoImg.setFitWidth(cellSize);
            entornoImg.setFitHeight(cellSize);

            String entornoImagePath = "images/" + entorno.getImage() + ".gif";
            URL resource = App.class.getResource(entornoImagePath);
            if (resource == null) {
                continue;
            }
            entornoImg.setImage(new Image(resource.toExternalForm(), cellSize, cellSize, true, false));
            entornoImg.setSmooth(false);
            Vector2Double entornoPos = matrixToInterface(entorno.getPosition());
            entornoImg.setLayoutX(entornoPos.getX());
            entornoImg.setLayoutY(entornoPos.getY());
            entornoImageViews.put(entorno, entornoImg);
            pane.getChildren().add(entornoImg);
        }

        onChange();
    }
    // metodo para que se interactue con los objetos entorno
    public void interactWithEntorno(int row, int col) {
        com.dungeonmvc.models.Cell cell = board.getCell(new Vector2(row, col));
        if (cell.getInteractuable() instanceof Entorno) {
            Entorno entorno = (Entorno) cell.getInteractuable();
            GameManager.getInstance().consumeEntorno(row, col);

            ImageView entornoImg = entornoImageViews.get(entorno);
            if (entornoImg != null) {
                pane.getChildren().remove(entornoImg);
                entornoImageViews.remove(entorno);
            }
        }
    }
    //iterador para los personajes
    @Override
    public void onChange() {
        Iterator<Map.Entry<Character, ImageView>> iterator = characterImageViews.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, ImageView> entry = iterator.next();
            Character character = entry.getKey();
            ImageView imageView = entry.getValue();
            if (character instanceof Enemy && character.getHealth() <= 0) {
                pane.getChildren().remove(imageView);
                iterator.remove();
            } else {
                Vector2Double newPos = matrixToInterface(character.getPosition());
                imageView.setLayoutX(newPos.getX());
                imageView.setLayoutY(newPos.getY());
            }
        }
        //iterador para el entorno
        Iterator<Map.Entry<Entorno, ImageView>> entornoIterator = entornoImageViews.entrySet().iterator();
        while (entornoIterator.hasNext()) {
            Map.Entry<Entorno, ImageView> entry = entornoIterator.next();
            Entorno entorno = entry.getKey();
            ImageView entornoImg = entry.getValue();
            if (board.getCell(entorno.getPosition()).getInteractuable() == null) {
                pane.getChildren().remove(entornoImg);
                entornoIterator.remove();
            }
        }
    }

    @Override
    public void onChange(String... args) {
        if (args.length > 0 && args[0].equals("Health")) {
            int newHealth = Integer.parseInt(args[1]);
            System.out.println("la vida del enemigo cambio a: " + newHealth);
        }
    }

    private Vector2Double matrixToInterface(Vector2 coord) {
        return new Vector2Double(cellSize * coord.getX(), cellSize * coord.getY());
    }

    public void setBoardSize(double boardSize) {
        this.boardSize = boardSize;
    }
}
