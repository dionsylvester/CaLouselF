package view;

import controller.ItemController;
import controller.UserController;
import controller.WishlistController;
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
import view.seller.SellerDashboardPage;
import view.seller.UploadItemPage;
import view.buyer.BuyerDashboardPage;

public class ViewItemPage {
    public Scene scene;
    private Stage stage;
    private Main main;
    private String username;
    private String currentRole;
    private String currentId;

    // Halaman ini dapat diakses oleh Buyer dan Seller
    // Jika Buyer yang mengakses, maka ada Action Button di setiap row table untuk Offer Price, Wishlist, dan Purchase Item
    // Sedangkan jika Seller yang mengakses, maka hanya ada Button untuk Upload Item
    public ViewItemPage(Stage stage, String username) {
        this.stage = stage;
        this.main = new Main();
        UserController uc = new UserController();
        ItemController ic = new ItemController();
        WishlistController wc = new WishlistController();
        TransactionController tc = new TransactionController();
        
        currentRole = uc.getRole(username);  // mendapatkan role user
        currentId = uc.getUserId(username); // mendapatkan id user

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

        // kolom Actions awalnya kosong, nanti diisi berdasarkan user role
        TableColumn<Item, Void> actionColumn = new TableColumn<>("Actions");

        if (currentRole.equals("Buyer")) {
        	// jika role adalah Buyer
            actionColumn.setCellFactory(col -> new TableCell<Item, Void>() {
                private final Button offerPriceButton = new Button("Offer Price");
                private final Button addItemToWishlistButton = new Button("Wishlist");
                private final Button purchaseItemButton = new Button("Purchase Item");

                {
                    HBox buttonBox = new HBox(10, offerPriceButton, addItemToWishlistButton, purchaseItemButton);
                    buttonBox.setAlignment(Pos.CENTER);
                    offerPriceButton.setMinWidth(50);
                    addItemToWishlistButton.setMinWidth(50);
                    purchaseItemButton.setMinWidth(50);

                    setGraphic(buttonBox);

                    offerPriceButton.setOnAction(e -> {
                        Item selectedItem = getTableView().getItems().get(getIndex());
                        // pada dokumen soal, diberitahu bahwa setiap kali Buyer menawarkan harga akan ditampilkan sebuah form
                        TextInputDialog offerDialog = new TextInputDialog();
                        offerDialog.setTitle("Make an Offer");
                        offerDialog.setHeaderText("Enter your " + selectedItem.getItemName() + " offer");
                        offerDialog.setContentText("Offer Price:");
                        offerDialog.getEditor().setText(selectedItem.getItemPrice()); // inisialisasi harga berdasarkan harga original
                        offerDialog.showAndWait().ifPresent(inputPrice -> {
                            try {
                                String result = ic.offerPrice(selectedItem.getItemId(), inputPrice, currentId); // panggil method offerPrice()
                                if (result.equals("")) {
                                    main.showSuccess("Offer Successful", "Your offer has been submitted.");
                                } else {
                                    main.showAlert("Offer Failed", result);
                                }
                            } catch (NumberFormatException ex) {
                                main.showAlert("Invalid Price", "Please enter a valid number for the offer price.");
                            }
                        });
                    });
                    
                    addItemToWishlistButton.setOnAction(e -> {
                        Item selectedItem = getTableView().getItems().get(getIndex());
                        // user dapat membuat wishlist berdasarkan row item yang dikliknya
                        String result = wc.addWishlist(selectedItem.getItemId(), currentId);
                        if (result.equals("")) {
                            main.showSuccess("Item Added to Wishlist", "The item has been successfully added to your wishlist.");
                        } else {
                            main.showAlert("Add to Wishlist Failed", result);
                        }
                    });
                    
                    purchaseItemButton.setOnAction(e -> {
                        Item selectedItem = getTableView().getItems().get(getIndex());
                        // pada dokumen soal, diberitahu bahwa setiap kali Buyer membeli item, akan ada confirmation pop up terlebih dahulu
                        Alert confirmPurchaseAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmPurchaseAlert.setTitle("Confirm Purchase");
                        confirmPurchaseAlert.setHeaderText("Are you sure you want to purchase this item?");
                        confirmPurchaseAlert.setContentText("Item: " + selectedItem.getItemName() + "\nPrice: " + selectedItem.getItemPrice());
                        confirmPurchaseAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                // jika user mengonfirmasi pembelian item, maka method purchaseItem() pada TransactionController akan dipanggil
                                String purchaseResult = tc.purchaseItem(currentId, selectedItem.getItemId());
                                if (purchaseResult.equals("")) { // pembelian berhasil
                                    main.showSuccess("Purchase Successful", "You have successfully purchased the item.");
                                } else {
                                    main.showAlert("Purchase Failed", purchaseResult);
                                }
                            }
                        });
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : getGraphic());
                }
            });
        }

        // menambahkan item tampilan pada tabel
        itemTable.getColumns().addAll(itemIdColumn, itemNameColumn, itemCategoryColumn, itemSizeColumn, itemPriceColumn, actionColumn);
        ObservableList<Item> itemsList = FXCollections.observableArrayList(ic.viewItem());
        itemTable.setItems(itemsList);

        // Button Back akan mengembalikan user berdasarkan role mereka
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            if (currentRole.equals("Seller")) {
                SellerDashboardPage sellerDashboardPage = new SellerDashboardPage(stage, username);
                stage.setScene(sellerDashboardPage.getScene());
            } else if (currentRole.equals("Buyer")) {
                BuyerDashboardPage buyerDashboardPage = new BuyerDashboardPage(stage, username);
                stage.setScene(buyerDashboardPage.getScene());
            }
        });

        // Tambahan untuk role Seller, terdapat button Upload Item
        if (currentRole.equals("Seller")) {
            Button uploadItemButton = new Button("Upload Item");
            uploadItemButton.setOnAction(e -> {
            	// Seller akan diarahkan ke UploadItemPage
                UploadItemPage uploadItemPage = new UploadItemPage(stage, username);
                stage.setScene(uploadItemPage.scene);
            });

            // Button Upload Item ini berada di sebelah Button Back
            HBox buttonLayout = new HBox(10, uploadItemButton, backButton);
            buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);
            buttonLayout.setStyle("-fx-padding: 20px;");

            // tampilan tabel dengan buttonLayout eksklusif untuk Seller
            VBox layout = new VBox(15, itemTable, buttonLayout);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-padding: 24px;");
            scene = new Scene(layout, 1100, 550);
        } else {
            // tampilan tabel dengan backButton biasa untuk Buyer
            VBox layout = new VBox(15, itemTable, backButton);
            layout.setAlignment(Pos.BOTTOM_RIGHT);
            layout.setStyle("-fx-padding: 24px;");
            scene = new Scene(layout, 1100, 550);
        }
    }

    public Scene getScene() {
        return scene;
    }
}
