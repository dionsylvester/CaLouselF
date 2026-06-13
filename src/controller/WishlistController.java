package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import database.DatabaseConnection;

public class WishlistController {
    private DatabaseConnection db = DatabaseConnection.getInstance();

    // method untuk menampilkan keseluruhan wishlist dari user terkait
    // menggunakan Object[] karena tidak ada model yang sesuai untuk menampung keseluruhan
    public ArrayList<Object[]> viewWishlist(String userId) {
        ArrayList<Object[]> itemList = new ArrayList<>();
        String query = "SELECT i.itemId, i.itemName, i.itemCategory, i.itemSize, i.itemPrice " +
                       "FROM wishlist w " +
                       "JOIN item i ON w.itemId = i.itemId " +
                       "WHERE w.userId = ?";
        PreparedStatement prepQuery = db.prepStatement(query);
        try {
            prepQuery.setString(1, userId);
            ResultSet rs = prepQuery.executeQuery();
            while (rs.next()) {
                Object[] item = new Object[5];
                item[0] = rs.getString("itemId");
                item[1] = rs.getString("itemName");
                item[2] = rs.getString("itemCategory");
                item[3] = rs.getString("itemSize");
                item[4] = rs.getString("itemPrice");
                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }
    
    // method untuk Buyer menambahkan item terkait ke dalam wishlistnya
    public String addWishlist(String itemId, String userId) {
        String wishlistId = generateWishlistId();
        String query = "INSERT INTO wishlist (wishlistId, itemId, userId) VALUES (?, ?, ?)";
        PreparedStatement prepQuery = db.prepStatement(query);
        try {
            prepQuery.setString(1, wishlistId);
            prepQuery.setString(2, itemId);
            prepQuery.setString(3, userId);
            // apabila ada penambahan row, maka add item to wishlist berhasil
            int affected = prepQuery.executeUpdate();
            if (affected > 0) {
                return "";
            } else {
                return "Failed to add item to wishlist.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }
    
    // karena pada viewWishlist() yang akan ditampilkan adalah komponen Item, maka removeWishlist mendapat passing parameter itemId
    public String removeWishlist(String itemId) {
        String query = "DELETE FROM wishlist WHERE itemId = ?";
        PreparedStatement prepQuery = db.prepStatement(query);
        try {
            prepQuery.setString(1, itemId);
            // apabila ada row yang berubah, maka remove item from wishlist berhasil
            int affected = prepQuery.executeUpdate();
            if (affected > 0) {
                return "";
            } else {
                return "Failed to remove item from wishlist.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }

    // method untuk membuat wishlist Id
    private String generateWishlistId() {
        Random random = new Random();
        String id;
        do {
            int number = random.nextInt(1000);
            id = "WL" + String.format("%03d", number);
        } while (!wishlistIdUniqueness(id));
        return id;
    }

    // method untuk memastikan bahwa wishlistId adalah unik
    private boolean wishlistIdUniqueness(String wishlistId) {
        String query = "SELECT COUNT(*) FROM wishlist WHERE wishlistId = ?";
        try {
            PreparedStatement prepQuery = db.prepStatement(query);
            prepQuery.setString(1, wishlistId);
            ResultSet rs = prepQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}