package com.dungeonmvc.controllers;

import com.dungeonmvc.App;
import com.dungeonmvc.GameManager;
import com.dungeonmvc.interfaces.Observer;
import com.dungeonmvc.models.Inventory;
import com.dungeonmvc.models.Character;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class InventoryViewController implements Observer {

    @FXML
    GridPane grid;

    private Inventory inventory;
    private final int cellSize = 50;
    private final int itemsPerRow = 4;

    private ContextMenu menu;

    @FXML
    private void initialize() {
        System.out.println("Inventory controller loaded");

        inventory = GameManager.getInstance().getPlayer().getInventory();
        inventory.suscribe(this);

        menu = new ContextMenu();

        // Opciones del menú
        MenuItem equip = new MenuItem("Equipar");
        MenuItem consume = new MenuItem("Consumir");
        MenuItem delete = new MenuItem("Tirar");

        // Manejadores de eventos para cada opción
        equip.setOnAction(e -> equipItem());
        consume.setOnAction(e -> consumeItem());
        delete.setOnAction(e -> deleteItem());

        // Agregar opciones al menú
        menu.getItems().addAll(equip, consume, delete);

        for (int col = 0; col < itemsPerRow; col++) {
            grid.addColumn(col);
        }

        for (int row = 0; row < inventory.getInventoryMaxSize() / itemsPerRow; row++) {
            grid.addRow(row);
        }

        for (int i = 0; i < inventory.getInventorySize(); i++) {
            ImageView itemImgView = new ImageView();
            itemImgView.setFitWidth(cellSize);
            itemImgView.setFitHeight(cellSize);
            itemImgView.setSmooth(false);
            itemImgView.setPickOnBounds(true);
            itemImgView.setViewport(new Rectangle2D(0, 0, cellSize, cellSize));
            itemImgView.setImage(
                    new Image(App.class.getResource("images/" + inventory.getItem(i) + ".png").toExternalForm(),
                            cellSize, cellSize, true, false));
            itemImgView.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    showMenu(event, itemImgView);
                }
            });
            grid.add(itemImgView, i % itemsPerRow, i / itemsPerRow);
        }
    }

    private void showMenu(MouseEvent event, ImageView imageView) {
        menu.getItems().get(0).setDisable(true);
        menu.hide();
        // Mostrar el menú en la posición del clic
        menu.show(imageView, event.getScreenX(), event.getScreenY());
    }

    public void equipItem() {
        // TODO
        System.out.println("Objeto equipado");
    }

    public void consumeItem() {
        // TODO
        System.out.println("Objeto consumido");
    }

    public void deleteItem() {
        // TODO
        System.out.println("Objeto eliminado");
    }

    @Override
    public void onChange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onChange'");
    }

    @Override
    public void onChange(String... args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onChange'");
    }

}
