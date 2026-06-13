package view;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
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
import view.buyer.BuyerDashboardPage;
import view.seller.SellerDashboardPage;
import view.admin.AdminDashboardPage;

public class BrowseItemPage {
    public Scene scene;
    private Stage stage;
    private String username;
    private String userRole; // menampung role user terkait karena BrowseItemPage dapat diakses oleh Buyer, Seller, dan Admin
    private ObservableList<Item> itemList;
    private Main main;

    // stage dan username menjadi passing parameter
    public BrowseItemPage(Stage stage, String username) {
        main = new Main();
        this.stage = stage;
        this.username = username;
        UserController uc = new UserController();
        ItemController ic = new ItemController();

        // mendapatkan role user yang mengakses saat ini
        this.userRole = uc.getRole(username);

        // membuat tabel dengan observableArrayList()
        itemList = FXCollections.observableArrayList();
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

        TableColumn<Item, String> itemStatusColumn = new TableColumn<>("Status");
        itemStatusColumn.setCellValueFactory(new PropertyValueFactory<>("itemStatus"));

        TableColumn<Item, String> itemWishlistColumn = new TableColumn<>("Offer Price");
        itemWishlistColumn.setCellValueFactory(new PropertyValueFactory<>("itemWishlist"));

        TableColumn<Item, String> itemOfferStatusColumn = new TableColumn<>("Offer Reason");
        itemOfferStatusColumn.setCellValueFactory(new PropertyValueFactory<>("itemOfferStatus"));

        itemTable.getColumns().addAll(itemIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, itemStatusColumn, itemWishlistColumn, itemOfferStatusColumn);

        // meminta user terkait untuk memasukkan keyword item yang akan dicari
        TextField searchField = new TextField();
        searchField.setPromptText("Enter item name keyword to search");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String searchKeyword = searchField.getText(); // mendapat hasil ketikan keyword
            if (searchKeyword.isEmpty()) {
                main.showAlert("Error", "Please enter a keyword to search.");
                return;
            }

            // menggunakan method browseItem() berdasarkan itemName yang ada di ItemController
            ArrayList<Item> searchResults = ic.browseItem(searchKeyword);

            // sewaktu validasi tadi, apabila tidak ada yang sesuai maka akan return "No Items Matched with Keyword" maka akan show alert
            if (!searchResults.isEmpty() && searchResults.get(0).getItemName().equals("No Items Matched with Keyword")) {
                main.showAlert("No Results", "No items matched your search.");
            } else {
                // apabila keyword ada yang sesuai dengan item, maka akan menampilkan item tersebut dalam tabel
                ObservableList<Item> resultsList = FXCollections.observableArrayList(searchResults);
                itemTable.setItems(resultsList);
                main.showSuccess("Search Completed", searchResults.size() + " item(s) found."); // menampilkan sukses
            }
        });

        itemTable.setItems(itemList);

        // tombol Back akan mengembalikan user berdasarkan rolenya masing-masing
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
        	// karena admin tidak memiliki tempat di database, maka kami hard code berdasarkan usernamenya
            if ("admin".equals(username)) {
                AdminDashboardPage adminDashboardPage = new AdminDashboardPage(stage);
                stage.setScene(adminDashboardPage.getScene());
            } else {
            	// selain dari admin, userRole sudah kita tentukan di awal, jadi Buyer ataupun Seller akan kembali ke dashboard mereka
                switch (userRole) {
                    case "Buyer":
                        BuyerDashboardPage buyerDashboardPage = new BuyerDashboardPage(stage, username);
                        stage.setScene(buyerDashboardPage.getScene());
                        break;
                    case "Seller":
                        SellerDashboardPage sellerDashboardPage = new SellerDashboardPage(stage, username);
                        stage.setScene(sellerDashboardPage.getScene());
                        break;
                    default:
                        break;
                }
            }
        });

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, searchBox, itemTable, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 24px;");

        scene = new Scene(layout, 1100, 550);
    }

    public Scene getScene() {
        return scene;
    }
}
