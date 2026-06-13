package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import database.DatabaseConnection;
import model.User;

public class UserController {
	private DatabaseConnection db = DatabaseConnection.getInstance();
	private ArrayList<User> users;
	
	public UserController() {
		this.users = new ArrayList<>();
	}
	
	// method untuk validasi credentials user
	public String Login(String username, String password) {
		String validation = validateLogin(username, password);
		// khusus admin, karena pada dokumen soal dibilang bahwa tidak perlu masuk database, maka kami menggunakan hard code
		if (username.equals("admin") && password.equals("admin")) {
			return "admin";
		}
		else if (!validation.equals("")) { // apabila validasi ada yang salah maka akan mengembalikan pesan error sesuai dari validateLogin()
			return validation;
		}
		else {
			String query = "SELECT * FROM user WHERE username = ?";
			PreparedStatement prepQuery = db.prepStatement(query);
			try {
				prepQuery.setString(1, username);
				ResultSet rs = prepQuery.executeQuery();
				if (rs.next()) {
					String passwordDatabase = rs.getString("password");
					// jika username dan password sesuai, maka akan mengembalikan userRole untuk kepentingan pengarahan dashboard
					if (passwordDatabase.equals(password)) {
						return rs.getString("role");
					}
					else {
						return "Password does not match with database";
					}
				}
				else {
					return "Username does not match with database";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return "Database error: " + e.getMessage();
			}
		}
	}
		
	// method untuk membuat user baru berdasarkan role yang mereka pilih
	public String Register(String username, String password, String phoneNumber, String address, String role) {
		// perlu melakukan validation dari informasi yang diberikan terlebih dahulu
		String validation = checkAccountValidation(username, password, phoneNumber, address, role);
		if (!validation.equals("")) {
			return validation;
		}
		else {
			// apabila sudah sesuai, maka akan melakukan create row baru pada user
			String query = "INSERT INTO user (userId, username, password, phoneNumber, address, role) " + "VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement prepQuery = db.prepStatement(query);
			try {
				prepQuery.setString(1, generateId());
				prepQuery.setString(2, username);
				prepQuery.setString(3, password);
				prepQuery.setString(4, phoneNumber);
				prepQuery.setString(5, address);
				prepQuery.setString(6, role);
				int affected = prepQuery.executeUpdate();
				if (affected > 0) {
					return "";
				} else {
					return "Registration failed";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return "Insert to database failed " + e.getMessage();
			}
		}
	}
	
	// karena user harus memilih rolenya sendiri, maka kami asumsikan ada passing parameter baru yakni role dibandingkan pada Class Diagram
	public String checkAccountValidation(String username, String password, String phoneNumber, String address, String role) {
		if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || role.isEmpty()) {
			return "All fields must be filled and cannot be empty";
		}
		else if (username.length() < 3) {
			return "Username must at least be 3 characters long";
		}
		else if (!nameUniqueness(username)) {
			return "Username must be unique";
		}
		else if (password.length() < 8) {
			return "Password must at least be 8 characters long";
		}
		else if (!passwordValid(password)) {
			return "Password must include special characters (!,@,#,$,%,^,&,*)";
		}
		else if (!phoneNumberValid(phoneNumber)) {
			return "Phone Number must at least contains a +62 and 10 numbers long which +62 counts as one, Example: +62123456789";
		}
		return "";
	}
	
	// method untuk membuat userId
	public String generateId() {
		Random random = new Random();
		String id;
		do {
			int number = random.nextInt(1000);
			id = "ID" + String.format("%03d", number);
		} while (idUniqueness(id));
		return id;
	}
	
	// method untuk memastikan bahwa id yang digenerate adalah unik
	public boolean idUniqueness(String id) {
		for (User user : users) {
			if (user.getUserId().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	// method untuk memeriksa bahwa username harus unik
	public boolean nameUniqueness(String username) {
		String query = "SELECT COUNT(*) FROM user WHERE username = ?";
		try (PreparedStatement ps = db.prepStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count == 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// method untuk memeriksa bahwa password harus memiliki special characters
	public boolean passwordValid(String password) {
		String specialCharacters = "!@#$%^&*";
		for (int i = 0; i < password.length(); i++) {
			char pos = password.charAt(i);
			if (specialCharacters.indexOf(pos) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	// method untuk memastikan bahwa phone number dimulai dengan +62 dan memiliki 9 angka lainnya
	public boolean phoneNumberValid(String phoneNumber) {
		if (phoneNumber.startsWith("+62") && phoneNumber.length() >= 12) {
			String noCountryCode = phoneNumber.substring(3);
			for (int i = 0; i < noCountryCode.length(); i++) {
				if (!Character.isDigit(noCountryCode.charAt(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	// method untuk memastikan informasi login tidak boleh kosong
	public String validateLogin(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
			return "All fields must be filled and cannot be empty";
		}
		return "";
	}
	
	// method untuk mendapatkan role user berdasarkan username
	public String getRole(String username) {
		String query = "SELECT role FROM user WHERE username = ?";
		try (PreparedStatement ps = db.prepStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("role");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	// method untuk mendapatkan id user berdasarkan username
	public String getUserId(String username) {
		String query = "SELECT userId FROM user WHERE username = ?";
		try (PreparedStatement ps = db.prepStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("userId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
