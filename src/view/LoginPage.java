package view;

import controller.UserController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.admin.AdminDashboardPage;
import view.buyer.BuyerDashboardPage;
import view.seller.SellerDashboardPage;

public class LoginPage extends Application implements EventHandler<ActionEvent> {
	private BorderPane root;
	private GridPane grid;
	private Label title, usernameLabel, passwordLabel;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button registerButton, loginButton;
	private HBox hbBtn;
	public Scene scene;
	private UserController uc;
	private Main main;
	private Stage stage; 
	
	public void init() {
		root = new BorderPane();
		title = new Label("Login");
		grid = new GridPane();
		usernameLabel = new Label("Username: ");
		usernameField = new TextField();
		passwordLabel = new Label("Password: ");
		passwordField = new PasswordField();
		registerButton = new Button("Register");
		loginButton = new Button("Login");
		hbBtn = new HBox(10);
		scene = new Scene(root, 1100, 550);
		uc = new UserController();
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
		grid.add(usernameLabel, 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(passwordLabel, 0, 1);
		grid.add(passwordField, 1, 1);
		
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerButton);
		hbBtn.getChildren().add(loginButton);
		grid.add(hbBtn, 1, 8);
	}
	
	public void setStyle() {
		title.setStyle("-fx-font-size: 36px;");
	}
	
	public void clearFields() {
		usernameField.clear();
		passwordField.clear();
	}

	private void events() {
		registerButton.setOnAction(e -> handle(e));
		loginButton.setOnAction(e -> handle(e));
	}
	
	// keseluruhan page ada passing parameter stage
	public LoginPage(Stage stage) {
		this.stage = stage;
		init();
		setPosition();
		setStyle();
		events();
		view.Main.redirect(scene);
	}

	@Override
	public void handle(ActionEvent e) {
		// apabila Login Button ditekan, maka akan menjalankan method Login() pada UserController
		if (e.getSource() == loginButton) {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String result = uc.Login(username, password);
			
			// user akan dialokasikan ke dashboard masing-masing sesuai role mereka
			if (result.equals("admin")) {
				main.showSuccess("Success", "Welcome " + username);
				clearFields();
				view.Main.redirect(new AdminDashboardPage(stage).scene);
			}
			else if (result.equals("Buyer")) {
				main.showSuccess("Success", "Welcome " + username);
				clearFields();
				view.Main.redirect(new BuyerDashboardPage(stage, username).scene);
			}
			else if (result.equals("Seller")) {
				main.showSuccess("Success", "Welcome " + username);
				clearFields();
				view.Main.redirect(new SellerDashboardPage(stage, username).scene);
			}
			else {
				main.showAlert("Validation Error", result);
			}
		}
		// apabila Register Button ditekan, maka akan diarahkan ke RegisterPage
		else if (e.getSource() == registerButton) {
			clearFields();
			view.Main.redirect(new RegisterPage(stage).scene);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Login Page");
		stage.setScene(scene);
		stage.show();
	}

    public Scene getScene() {
        return scene;
    }
}
