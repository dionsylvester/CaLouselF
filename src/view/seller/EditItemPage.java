package view.seller;

import controller.ItemController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Item;
import view.Main;

public class EditItemPage implements EventHandler<ActionEvent> {
    private BorderPane root;
    private GridPane grid;
    private Label title, itemNameLabel, itemCategoryLabel, itemSizeLabel, itemPriceLabel;
    private TextField itemNameField, itemCategoryField, itemSizeField, itemPriceField;
    private Button submitButton, backButton;
    private HBox hbBtn;
    private ItemController ic;
    private Main main;
    private Stage stage;
    private Item item;
    private String username;
    public Scene scene;

    public void init() {
        root = new BorderPane();
        title = new Label("Edit Item");
        grid = new GridPane();
        itemNameLabel = new Label("Item Name: ");
        itemNameField = new TextField();
        itemCategoryLabel = new Label("Item Category: ");
        itemCategoryField = new TextField();
        itemSizeLabel = new Label("Item Size: ");
        itemSizeField = new TextField();
        itemPriceLabel = new Label("Item Price: ");
        itemPriceField = new TextField();
        submitButton = new Button("Update");
        backButton = new Button("Back");
        hbBtn = new HBox(10);
        scene = new Scene(root, 1100, 550);
        ic = new ItemController();
        main = new Main();
    }

    public void setPosition() {
        root.setCenter(title);
        root.setBottom(grid);

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPrefHeight(350);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        title.setAlignment(Pos.TOP_CENTER);
        grid.add(itemNameLabel, 0, 0);
        grid.add(itemNameField, 1, 0);
        grid.add(itemCategoryLabel, 0, 1);
        grid.add(itemCategoryField, 1, 1);
        grid.add(itemSizeLabel, 0, 2);
        grid.add(itemSizeField, 1, 2);
        grid.add(itemPriceLabel, 0, 3);
        grid.add(itemPriceField, 1, 3);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(submitButton);
        hbBtn.getChildren().add(backButton);
        grid.add(hbBtn, 1, 8);
    }

    public void setStyle() {
        title.setStyle("-fx-font-size: 36px;");
    }

    public void clearFields() {
        itemNameField.clear();
        itemCategoryField.clear();
        itemSizeField.clear();
        itemPriceField.clear();
    }

    // membuat inisialisasi nilai berdasarkan informasi original item yang akan diedit
    private void populateFields() {
        itemNameField.setText(item.getItemName());
        itemCategoryField.setText(item.getItemCategory());
        itemSizeField.setText(item.getItemSize());
        itemPriceField.setText(item.getItemPrice());
    }

    private void events() {
        submitButton.setOnAction(e -> handle(e));
        backButton.setOnAction(e -> handle(e));
    }

	// page ada passing parameter stage, username, dan tambahan objek Item yang akan diedit
    public EditItemPage(Stage stage, String username, Item item) {
        this.stage = stage;
        this.username = username;
        this.item = item;
        
        init();
        setPosition();
        setStyle();
        events();
        populateFields();
    }
    
    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String itemName = itemNameField.getText();
            String itemCategory = itemCategoryField.getText();
            String itemSize = itemSizeField.getText();
            String itemPrice = itemPriceField.getText();
            // kita menggunakan method editItem() dari ItemController untuk memperbarui informasi item
            String result = ic.editItem(item.getItemId(), itemName, itemCategory, itemSize, itemPrice);
            if (result.equals("")) {
                main.showSuccess("Success", "Item has been updated successfully.");
                clearFields(); // ketika update berhasil, akan redirect kembali ke ViewSellerItemPage
                view.Main.redirect(new ViewSellerItemPage(stage, username).scene);
            } else {
                main.showAlert("Error", result);
            }
        }
        // Seller akan kembali ke halaman ViewSellerItemPage
        else if (e.getSource() == backButton) {
        	ViewSellerItemPage viewSellerItemPage = new ViewSellerItemPage(stage, username);
            stage.setScene(viewSellerItemPage.getScene());
        }
    }

    public void start(Stage stage) throws Exception {
        stage.setTitle("Edit Item Page");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}
