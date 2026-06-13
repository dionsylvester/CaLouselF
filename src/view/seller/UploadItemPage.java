package view.seller;

import controller.ItemController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.Main;
import view.ViewItemPage;

public class UploadItemPage extends Application implements EventHandler<ActionEvent> {
	private BorderPane root;
	private GridPane grid;
	private Label title, itemNameLabel, itemCategoryLabel, itemSizeLabel, itemPriceLabel;
	private TextField itemNameField, itemCategoryField, itemSizeField, itemPriceField;
	private Button submitButton, backButton;
	private HBox hbBtn;
	public Scene scene;
	private ItemController ic;
	private Main main;
	private Stage stage;
	private String username;
	
	public void init() {
		root = new BorderPane();
		title = new Label("Upload Item");
		grid = new GridPane();
		itemNameLabel = new Label("Item Name: ");
		itemNameField = new TextField();
		itemCategoryLabel = new Label("Item Category: ");
		itemCategoryField = new TextField();
		itemSizeLabel = new Label("Item Size: ");
		itemSizeField = new TextField();
		itemPriceLabel = new Label("Item Price: ");
		itemPriceField = new TextField();
		submitButton = new Button("Submit");
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
	
	private void events() {
		submitButton.setOnAction(e -> handle(e));
		backButton.setOnAction(e -> handle(e));
	}

	// page ada passing parameter stage dan username. Berbeda dengan EditItemPage yang ada tambahan parameter objek Item
	public UploadItemPage(Stage stage, String username) {
		this.stage = stage;
		this.username = username;
		init();
		setPosition();
		setStyle();
		events();
		view.Main.redirect(scene);
	}
	
	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == submitButton) {
			String itemName = itemNameField.getText();
			String itemCategory = itemCategoryField.getText();
			String itemSize = itemSizeField.getText();
			String itemPrice = itemPriceField.getText();
			// kita menggunakan method uploadItem() dari ItemController untuk membuat item terkait dengan status default "Pending"
			String result = ic.uploadItem(itemName, itemCategory, itemSize, itemPrice);
			if (result.equals("")) {
				main.showSuccess("Success", "Your respective item has been created");
				clearFields(); // ketika upload berhasil, akan redirect kembali ke ViewItemPage
				view.Main.redirect(new ViewItemPage(stage, username).scene);
			}
			else {
				main.showAlert("Validation Error", result);
			}
		}
        // Seller akan kembali ke halaman ViewItem
		else if (e.getSource() == backButton) {
			clearFields();
			view.Main.redirect(new ViewItemPage(stage, username).scene);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Upload Item Page");
		stage.setScene(scene);
		stage.show();
	}

}
