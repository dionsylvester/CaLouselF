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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RegisterPage extends Application implements EventHandler<ActionEvent>{
	private BorderPane root;
	private GridPane grid;
	private Label title, usernameLabel, passwordLabel, phoneNumberLabel, addressLabel, roleLabel;
	private TextField usernameField, phoneNumberField, addressField;
	private PasswordField passwordField;
	private ToggleGroup role;
	private RadioButton buyerRadioButton, sellerRadioButton;
	private Button loginButton, submitButton;
	private HBox hbBtn;
	public Scene scene;
	private UserController uc;
	private Main main;
	private Stage stage; 
	
	public void init() {
		root = new BorderPane();
		title = new Label("Register");
		grid = new GridPane();
		usernameLabel = new Label("Username: ");
		usernameField = new TextField();
		passwordLabel = new Label("Password: ");
		passwordField = new PasswordField();
		phoneNumberLabel = new Label("Phone Number: ");
		phoneNumberField = new TextField();
		addressLabel = new Label("Address: ");
		addressField = new TextField();
		roleLabel = new Label("Role: ");
		buyerRadioButton = new RadioButton("Buyer");
		sellerRadioButton = new RadioButton("Seller");
		role = new ToggleGroup();
		buyerRadioButton.setToggleGroup(role);
		sellerRadioButton.setToggleGroup(role);
		loginButton = new Button("Login");
		submitButton = new Button("Submit");
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
		grid.add(phoneNumberLabel, 0, 2);
		grid.add(phoneNumberField, 1, 2);
		grid.add(addressLabel, 0, 3);
		grid.add(addressField, 1, 3);
		grid.add(roleLabel, 0, 4);
		grid.add(buyerRadioButton, 1, 4);
		grid.add(sellerRadioButton, 2, 4);
		
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginButton);
		hbBtn.getChildren().add(submitButton);
		grid.add(hbBtn, 1, 8);
	}
	
	public void setStyle() {
		title.setStyle("-fx-font-size: 36px;");
	}
	
	public void clearFields() {
		usernameField.clear();
		passwordField.clear();
		phoneNumberField.clear();
		addressField.clear();
		role.selectToggle(null);
	}
	
	private void events() {
		submitButton.setOnAction(e -> handle(e));
		loginButton.setOnAction(e -> handle(e));
	}

	// keseluruhan page ada passing parameter stage
	public RegisterPage(Stage stage) {
		this.stage = stage;
		init();
		setPosition();
		setStyle();
		events();
		view.Main.redirect(scene);
	}
	
	@Override
	public void handle(ActionEvent e) {
		// ketika Submit Button ditekan akan menjalankan method Register() pada UserController
		if (e.getSource() == submitButton) {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String phoneNumber = phoneNumberField.getText();
			String address = addressField.getText();
			RadioButton selected = (RadioButton) role.getSelectedToggle();
			String role = (selected != null) ? selected.getText() : ""; // menghindari NullPointerException
			
			String result = uc.Register(username, password, phoneNumber, address, role);
			// validasi untuk berhasil akan mengembalikan ""
			if (result.equals("")) {
				main.showSuccess("Success", "Your account has been created!");
				clearFields();
				view.Main.redirect(new LoginPage(stage).scene);
			}
			else {
				main.showAlert("Validation Error", result);
			}
		}
		// apabila Login Button ditekan, maka akan diarahkan ke LoginPage
		else if (e.getSource() == loginButton) {
			clearFields();
			view.Main.redirect(new LoginPage(stage).scene);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Register Page");
		stage.setScene(scene);
		stage.show();
	}

}
