package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import database.DatabaseConnection;

public class TransactionController {
	private DatabaseConnection db = DatabaseConnection.getInstance();

	// method ini digunakan oleh Buyer untuk membeli item terkait
    public String purchaseItem(String userId, String itemId) {
    	// membuat id untuk transactionId
    	String transactionId = generateTransactionId();
        try {
        	String query = "INSERT INTO transaction (userId, itemId, transactionId) VALUES (?, ?, ?)";
        	PreparedStatement prepQuery = db.prepStatement(query);
        	prepQuery.setString(1, userId);
        	prepQuery.setString(2, itemId);
        	prepQuery.setString(3, transactionId);
        	int affected = prepQuery.executeUpdate();
        	if (affected > 0) {
        		// pada dokumen soal diberitahu bahwa "the item will also be deleted from every user's wishlist"
	         	String deleteWishlistQuery = "DELETE FROM wishlist WHERE itemId = ?";
	          	PreparedStatement deleteWishlistStatement = db.prepStatement(deleteWishlistQuery);
            	deleteWishlistStatement.setString(1, itemId);
                deleteWishlistStatement.executeUpdate();
                return "";
            } else {
                return "Purchase failed. Item not found.";
            }
        } catch (SQLException e) {
        	e.printStackTrace();
           	return "Database error: " + e.getMessage();
      	}
	}
    
	// method untuk menampilkan history belanja dari user
    // menggunakan Object[] karena pada model tidak ada tipe objek yang menampung keseluruhan ini
    public ArrayList<Object[]> viewHistory(String userId) {
    	ArrayList<Object[]> itemList = new ArrayList<>();
    	String query = "SELECT t.transactionId, i.itemName, i.itemCategory, i.itemSize, i.itemPrice " +
                "FROM transaction t " +
                "JOIN item i ON t.itemId = i.itemId " +
                "WHERE t.userId = ?";
    	try {
    		PreparedStatement prepQuery = db.prepStatement(query);
    		prepQuery.setString(1, userId);
    		ResultSet rs = prepQuery.executeQuery();
    		while (rs.next()) {
    			Object[] list = new Object[5];
    			list[0] = rs.getString("transactionId");
    			list[1] = rs.getString("itemName");
    			list[2] = rs.getString("itemCategory");
    			list[3] = rs.getString("itemSize");
    			list[4] = rs.getString("itemPrice");
    			itemList.add(list);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return itemList;
    }
    
    public void createTransaction(String transactionId) {
    	// method ini sudah digabung bersama purchaseItem untuk mempersingkat waktu
    }
    
    // method untuk membuat ID transaksi
    private String generateTransactionId() {
        Random random = new Random();
        String id;
        do {
        	int number = random.nextInt(1000);
        	id = "TR" + String.format("%03d", number);
        } while (!transactionIdUniqueness(id));
        return id;
    }
    
    // memastikan bahwa transactionId tidak duplikat
    public boolean transactionIdUniqueness(String transactionId) {
    	String query = "SELECT COUNT(*) FROM transaction WHERE transactionId = ?";
    	PreparedStatement ps = db.prepStatement(query);
    	try {
    		ps.setString(1, transactionId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) == 0;
	        }
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
}