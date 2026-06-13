package view.seller;

import controller.ItemController;
import controller.TransactionController;
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

public class ViewOfferItemPage {
    public Scene scene;
    private Stage stage;
    private Main main;

    public ViewOfferItemPage(Stage stage, String username) {
        main = new Main();
        ItemController ic = new ItemController();
        TransactionController tc = new TransactionController();
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

        TableColumn<Item, String> itemWishlistColumn = new TableColumn<>("Offer Price");
        itemWishlistColumn.setCellValueFactory(new PropertyValueFactory<>("itemWishlist"));

        TableColumn<Item, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(col -> new TableCell<Item, Void>() {
        	// Button Action, yakni Accpet dan Decline untuk setiap item yang memiliki offer (mirip seperti konteks ViewRequestItemPage milik admin)
            private final Button acceptButton = new Button("Accept");
            private final Button declineButton = new Button("Decline");

            {
                HBox buttonBox = new HBox(10, acceptButton, declineButton);
                buttonBox.setAlignment(Pos.CENTER);
                acceptButton.setMinWidth(80);
                declineButton.setMinWidth(80);

                setGraphic(buttonBox);

                acceptButton.setOnAction(e -> {
                    Item item = getTableRow().getItem();
                    if (item != null) { // mencegah NullPointerException
                        String itemId = item.getItemId(); // mendapat itemId untuk dipassing
                        String userId = item.getUserId(); // mendapat userId untuk dipassing
                        // pada dokumen soal diberitahu bahwa setiap offer yang diterima akan otomatis membuat transaksi, maka
                        // method purchaseItem() dari Transaction Controller dengan parameter userId dan itemId
                        String result = tc.purchaseItem(userId, itemId);
                        if (result.isEmpty()) {
                            ic.acceptOffer(itemId);  // apabila pembuatan transaksi berhasil, maka offer yang bersangkutan akan dihapus
                            itemTable.setItems(FXCollections.observableArrayList(ic.viewOfferItem())); // Refresh Table
                            main.showSuccess("Offer Accepted", "The offer has been successfully accepted.");
                        } else {
                        	main.showAlert("Error", result);
                        }
                    }
                });
                
                declineButton.setOnAction(e -> {
                    Item item = getTableRow().getItem();
                    if (item != null) { // mencegah NullPointerException
                        String itemId = item.getItemId(); // mendapat itemId untuk dipassing
                        // apabila Seller menolak offer, pada dokumen soal diberitahukan bahwa Seller harus menyertakan alasannya
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Decline Offer");
                        dialog.setHeaderText("Provide a reason for declining the offer");
                        dialog.setContentText("Reason:");
                        dialog.showAndWait().ifPresent(reason -> {
                            if (!reason.isEmpty()) {
                                ic.declineOffer(itemId, reason); // method declineOffer dari ItemController
                                itemTable.setItems(FXCollections.observableArrayList(ic.viewOfferItem()));
                                main.showSuccess("Offer Declined", "The offer has been declined successfully.");
                            } else {
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
        itemTable.getColumns().addAll(itemIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, itemWishlistColumn, actionColumn);
        ObservableList<Item> offeredItems = FXCollections.observableArrayList(ic.viewOfferItem());
        itemTable.setItems(offeredItems);

        // fungsionalitas untuk Back Button, yakni mengembalikan Seller ke halaman dashboard
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
