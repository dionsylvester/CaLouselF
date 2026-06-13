package view.admin;

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

public class AdminDashboardPage extends Application implements EventHandler<ActionEvent> {
    private BorderPane root;
    private GridPane grid;
    private Label title;
    private Button browseItemButton, viewRequestItemButton, logoutButton;
    private HBox hbBtn;
    public Scene scene;
    private Stage stage; 

    public void init() {
        root = new BorderPane();
        title = new Label("Admin Dashboard");
        grid = new GridPane();
        browseItemButton = new Button("Browse Item");
        viewRequestItemButton = new Button("View Request Item");
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
        hbBtn.getChildren().add(viewRequestItemButton);
        hbBtn.getChildren().add(logoutButton);
        grid.add(hbBtn, 1, 8);
    }

    public void setStyle() {
        title.setStyle("-fx-font-size: 36px;");
    }

    private void events() {
        browseItemButton.setOnAction(e -> handle(e));
        viewRequestItemButton.setOnAction(e -> handle(e));
        logoutButton.setOnAction(e -> handle(e));
    }

	// keseluruhan page ada passing parameter stage
    public AdminDashboardPage(Stage stage) {
        this.stage = stage;
        init();
        setPosition();
        setStyle();
        events();
        view.Main.redirect(scene);
    }

    @Override
    public void handle(ActionEvent e) {
    	// apabila admin menekan Browse Item, maka passing "admin" secara hard code, berbeda dengan BuyerDashboard dan SellerDashboard
        if (e.getSource() == browseItemButton) {
             view.Main.redirect(new BrowseItemPage(stage, "admin").scene);
        }
        // apabila admin menekan View Request Item, maka akan meredirect admin ke ViewRequestItemPage
        else if (e.getSource() == viewRequestItemButton) {
        	ViewRequestItemPage viewRequestItemPage = new ViewRequestItemPage(stage);
        	stage.setScene(viewRequestItemPage.getScene());
        }
        // akan logout dan kembali ke halaman Login
        else if (e.getSource() == logoutButton) {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(loginPage.getScene());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Admin Dashboard Page");
        stage.setScene(scene);
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }
}
