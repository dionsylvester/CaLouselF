package view.seller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.LoginPage;
import view.BrowseItemPage;
import view.ViewItemPage;

public class SellerDashboardPage extends Application implements EventHandler<ActionEvent> {
    private BorderPane root;
    private GridPane grid;
    private Label title;
    private Button browseItemButton, viewItemButton, viewSellerItemButton, viewOfferItemButton, logoutButton;
    private HBox hbBtn;
    public Scene scene;
    private Stage stage; 
    private String username;

    public void init() {
        root = new BorderPane();
        title = new Label("Seller Dashboard");
        grid = new GridPane();
        browseItemButton = new Button("Browse Item");
        viewItemButton = new Button("View Item");
        viewSellerItemButton = new Button("View Seller Item");
        viewOfferItemButton = new Button("View Offer Item");
        logoutButton = new Button("Logout");
        hbBtn = new HBox(10);
        scene = new Scene(root, 1100, 550);
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
        
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(browseItemButton);
        hbBtn.getChildren().add(viewItemButton);
        hbBtn.getChildren().add(viewSellerItemButton);
        hbBtn.getChildren().add(viewOfferItemButton);
        hbBtn.getChildren().add(logoutButton);
        grid.add(hbBtn, 1, 8);
    }

    public void setStyle() {
        title.setStyle("-fx-font-size: 36px;");
    }
    
    private void events() {
        browseItemButton.setOnAction(e -> handle(e));
        viewItemButton.setOnAction(e -> handle(e));
        viewSellerItemButton.setOnAction(e -> handle(e));
        viewOfferItemButton.setOnAction(e -> handle(e));
        logoutButton.setOnAction(e -> handle(e));
    }

	// keseluruhan page ada passing parameter stage, tambahan username untuk mendapat userRole atau userId ke depannya
    public SellerDashboardPage(Stage stage, String username) {
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
    	// apabila Seller menekan Browse Item, maka passing username untuk mendapatkan Role-nya kembali ketika Back Button
        if (e.getSource() == browseItemButton) {
             view.Main.redirect(new BrowseItemPage(stage, username).scene);
        }
        // apabile Seller menekan View Item, maka akan merujuk ke halaman ViewItem
        else if (e.getSource() == viewItemButton) {
            view.Main.redirect(new ViewItemPage(stage, username).scene);
        }
        // apabile Seller menekan View Seller Item, maka akan merujuk ke halaman ViewSellerItem
        else if (e.getSource() == viewSellerItemButton) {
            view.Main.redirect(new ViewSellerItemPage(stage, username).scene);
        }
        // apabile Seller menekan View Offer Item, maka akan merujuk ke halaman ViewOfferItem
        else if (e.getSource() == viewOfferItemButton) {
              view.Main.redirect(new ViewOfferItemPage(stage, username).scene);
        }
        // akan logout dan kembali ke halaman Login
        else if (e.getSource() == logoutButton) {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(loginPage.getScene());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Seller Dashboard Page");
        stage.setScene(scene);
        stage.show();
    }
    
    public Scene getScene() {
        return scene;
    }

}
