package view.admin;

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

public class ViewRequestItemPage {
    public Scene scene;
    private Stage stage;
    private Main main;

    public ViewRequestItemPage(Stage stage) {
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
        	// Button Action, yakni Approve dan Decline untuk setiap Pending status item
            private final Button approveButton = new Button("Approve");
            private final Button declineButton = new Button("Decline");

            {
                HBox buttonBox = new HBox(10, approveButton, declineButton);
                buttonBox.setAlignment(Pos.CENTER);
                approveButton.setMinWidth(80);
                declineButton.setMinWidth(80);

                setGraphic(buttonBox);

                approveButton.setOnAction(e -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    String itemId = selectedItem.getItemId();
                    // menggunakan method approveItem pada ItemController
                    String response = ic.approveItem(itemId);
                    if (response.equals("")) {
                        main.showSuccess("Item Approved", "The item has been approved successfully.");
                        refreshTable(ic, itemTable);
                    } else {
                        main.showAlert("Approval Failed", response);
                    }
                });

                declineButton.setOnAction(e -> {
                    Item selectedItem = getTableView().getItems().get(getIndex());
                    if (selectedItem != null) { // mencegah NullPointerException
                        String itemId = selectedItem.getItemId(); // mendapat itemId untuk dipassing
                        // apabila admin menolak item ini, pada dokumen soal diberitahukan bahwa admin harus menyertakan alasannya
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Decline Item");
                        dialog.setHeaderText("Provide a reason for declining the item");
                        dialog.setContentText("Reason:");
                        dialog.showAndWait().ifPresent(reason -> {
                            if (!reason.isEmpty()) { // pertanda bahwa admin sudah mengisi alasannya
                                String response = ic.declineItem(itemId, reason); // method declineItem dari ItemController
                                if (response.equals("")) {
                                    main.showSuccess("Item Declined", "The item has been declined successfully.");
                                    refreshTable(ic, itemTable); // refreshTable setiap kali ada perubahan row
                                } else {
                                    main.showAlert("Declinal Failed", response);
                                }
                            } else { // pertanda bahwa admin belum mengisi alasannya
                                main.showAlert("Error", "You must provide a reason for declining.");
                            }
                        });
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
        ObservableList<Item> pendingItems = FXCollections.observableArrayList(ic.viewRequestedItem());
        itemTable.setItems(pendingItems);

        // fungsionalitas untuk Back Button, yakni mengembalikan Seller ke halaman dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            AdminDashboardPage adminDashboardPage = new AdminDashboardPage(stage);
            stage.setScene(adminDashboardPage.getScene());
        });

        VBox layout = new VBox(15, itemTable, backButton);
        layout.setAlignment(Pos.BOTTOM_RIGHT);
        layout.setStyle("-fx-padding: 24px;");

        scene = new Scene(layout, 1100, 550);
    }

    public Scene getScene() {
        return scene;
    }

    private void refreshTable(ItemController ic, TableView<Item> itemTable) {
        ObservableList<Item> updatedItems = FXCollections.observableArrayList(ic.viewRequestedItem());
        itemTable.setItems(updatedItems);
    }
}
