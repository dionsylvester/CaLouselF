package view.seller;

import controller.ItemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import view.Main;

public class ViewSellerItemPage {
    public Scene scene;
    private Stage stage;
    private String username;
    private Main main;

    public ViewSellerItemPage(Stage stage, String username) {
    	main = new Main();
        ItemController ic = new ItemController();
        TableView<Item> itemTable = new TableView<>();

        TableColumn<Item, String> itemIdColumn = new TableColumn<>("Item ID");
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        TableColumn<Item, String> itemNameColumn = new TableColumn<>("Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Item, String> itemCategoryColumn = new TableColumn<>("Category");
        itemCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));

        TableColumn<Item, String> itemSizeColumn = new TableColumn<>("Size");
        itemSizeColumn.setCellValueFactory(new PropertyValueFactory<>("itemSize"));

        TableColumn<Item, String> itemPriceColumn = new TableColumn<>("Price");
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        TableColumn<Item, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(col -> new TableCell<Item, Void>() {
        	// Button Action, yakni untuk mengedit item atau menghapus item
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                HBox buttonBox = new HBox(10, editButton, deleteButton);
                buttonBox.setAlignment(Pos.CENTER);
                editButton.setMinWidth(50);
                deleteButton.setMinWidth(50);

                setGraphic(buttonBox);

                editButton.setOnAction(e -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    // Edit Button akan mengarahkan Seller ke EditItemPage dengan inisialisasi objek selectedItem
                	EditItemPage editItemPage = new EditItemPage(stage, username, selectedItem);
                	stage.setScene(editItemPage.getScene());
                });
                
                deleteButton.setOnAction(e -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    String itemId = selectedItem.getItemId(); // mendapat itemId untuk dipassing
                    String response = ic.deleteItem(itemId); // memanggil method deleteItem() dari ItemController
                    if (response.equals("")) {
                        getTableView().getItems().remove(selectedItem); // menghapus barisnya
                        main.showSuccess("Item Deleted", "The item has been deleted successfully.");
                    } else {
                    	 main.showAlert("Deletion Failed", response);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        });

        // menambahkan item tampilan pada tabel
        itemTable.getColumns().addAll(itemIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, actionColumn);
        ObservableList<Item> approvedItems = FXCollections.observableArrayList(ic.viewItem());
        itemTable.setItems(approvedItems);

        // fungsionalitas untuk Back Button, yakni mengembalikan admin ke halaman dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            SellerDashboardPage sellerDashboardPage = new SellerDashboardPage(stage, username);
            stage.setScene(sellerDashboardPage.getScene());
        });

        VBox layout = new VBox(15, itemTable, backButton);
        layout.setAlignment(Pos.BOTTOM_RIGHT);
        layout.setStyle("-fx-padding: 24px;");

        scene = new Scene(layout, 1100, 550);
    }

    public Scene getScene() {
        return scene;
    }
}
