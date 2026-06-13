package view.buyer;

import controller.WishlistController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Main;

public class ViewWishlistPage {

    public Scene scene;
    private Stage stage;
    private String userId;
    private Main main;

	// page ada passing parameter stage dan username
    public ViewWishlistPage(Stage stage, String username) {
        this.stage = stage;
        this.main = new Main();
        UserController uc = new UserController();
        WishlistController wc = new WishlistController();
        this.userId = uc.getUserId(username); // mendapat userId terkait dari usernamenya

        // kami passing objek ke TableView berdasarkan Controller, yakni Object[] sehingga ambil value untuk tabel sedikit berbeda
        TableView<Object[]> wishlistTable = new TableView<>();

        TableColumn<Object[], String> itemIdColumn = new TableColumn<>("Item ID");
        itemIdColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue()[0].toString());
        });

        TableColumn<Object[], String> itemNameColumn = new TableColumn<>("Name");
        itemNameColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue()[1].toString());
        });

        TableColumn<Object[], String> itemCategoryColumn = new TableColumn<>("Category");
        itemCategoryColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue()[2].toString());
        });

        TableColumn<Object[], String> itemSizeColumn = new TableColumn<>("Size");
        itemSizeColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue()[3].toString());
        });

        TableColumn<Object[], String> itemPriceColumn = new TableColumn<>("Price");
        itemPriceColumn.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty(data.getValue()[4].toString());
        });

        TableColumn<Object[], Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellValueFactory(col -> new javafx.beans.property.SimpleObjectProperty<>(null));
        actionColumn.setCellFactory(col -> new TableCell<Object[], Void>() {
            private final Button removeButton = new Button("Remove"); // button untuk menghapus wishlist item user

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                    // fungsionalitas untuk Button Remove
                    removeButton.setOnAction(e -> {
                        Object[] rowData = getTableRow().getItem(); // karena ini Object[], jadi kami mengambil itemId yang berada di kolom pertama
                        String itemId = rowData[0].toString();
                        String result = wc.removeWishlist(itemId); // panggil method removeWishlist() dari WishlistController
                        if (result.isEmpty()) {
                            main.showSuccess("Item Removed", "The item has been successfully removed from your wishlist.");
                            getTableView().getItems().remove(rowData); // setiap item akan diremove barisnya
                        } else {
                            main.showAlert("Remove Error", result);
                        }
                    });
                }
            }
        });

        // menambahkan item tampilan pada tabel
        wishlistTable.getColumns().addAll(itemIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, actionColumn);
        ObservableList<Object[]> wishlistData = FXCollections.observableArrayList(wc.viewWishlist(userId));
        wishlistTable.setItems(wishlistData);

        // Button Back akan mengembalikan Buyer ke halaman Buyer Dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            BuyerDashboardPage buyerDashboardPage = new BuyerDashboardPage(stage, username);
            stage.setScene(buyerDashboardPage.getScene());
        });

        VBox layout = new VBox(15, wishlistTable, backButton);
        layout.setAlignment(Pos.BOTTOM_RIGHT);
        layout.setStyle("-fx-padding: 24px;");

        scene = new Scene(layout, 1100, 550);
    }

    public Scene getScene() {
        return scene;
    }
}
