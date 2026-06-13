package view.buyer;

import controller.TransactionController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Main;

public class ViewPurchaseHistoryPage {
    public Scene scene;
    private Stage stage;
    private String userId;
    private Main main;

	// page ada passing parameter stage dan username
    public ViewPurchaseHistoryPage(Stage stage, String username) {
        this.stage = stage;
        this.main = new Main();
        UserController uc = new UserController();
        TransactionController tc = new TransactionController();
        this.userId = uc.getUserId(username); // mendapat userId terkait dari usernamenya

        // kami passing objek ke TableView berdasarkan Controller, yakni Object[] sehingga ambil value untuk tabel sedikit berbeda
        TableView<Object[]> purchaseHistoryTable = new TableView<>();
        
        TableColumn<Object[], String> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setCellValueFactory(data -> {
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

        // menambahkan item tampilan pada tabel
        purchaseHistoryTable.getColumns().addAll(transactionIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn);
        ObservableList<Object[]> purchaseHistoryData = FXCollections.observableArrayList(tc.viewHistory(userId)); // history berdasarkan userId
        purchaseHistoryTable.setItems(purchaseHistoryData);

        // Button Back akan mengembalikan Buyer ke halaman Buyer Dashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            BuyerDashboardPage buyerDashboardPage = new BuyerDashboardPage(stage, username);
            stage.setScene(buyerDashboardPage.getScene());
        });

        VBox layout = new VBox(15, purchaseHistoryTable, backButton);
        layout.setAlignment(Pos.BOTTOM_RIGHT);
        layout.setStyle("-fx-padding: 24px;");

        scene = new Scene(layout, 1100, 550);
    }

    public Scene getScene() {
        return scene;
    }
}
