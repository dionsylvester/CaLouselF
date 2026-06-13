package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import database.DatabaseConnection;
import model.Item;

public class ItemController {
	private DatabaseConnection db = DatabaseConnection.getInstance();
	
	// ini adalah method untuk menambahkan item ke database yang akan dipakai Seller pada UploadItemPage
	public String uploadItem(String itemName, String itemCategory, String itemSize, String itemPrice) {
		// method ini untuk mengecek apakah informasi item yang ingin dibuat Seller sudah sesuai atau belum
		String validation = checkItemValidation(itemName, itemCategory, itemSize, itemPrice);
		// hampir di keseluruhan validasi kami menggunakan passing string "" untuk menyatakan bahwa pengecekan benar
		if (!validation.equals("")) {
	    	return validation;
		} else {
	    	// kami menggunakan (?, ?) atau PreparedStatement untuk mempersingkat query
	    	String query = "INSERT INTO item (itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    	PreparedStatement prepQuery = db.prepStatement(query);
	        try {
	        	prepQuery.setString(1, generateItemId()); // unique id untuk item akan dibuat
	            prepQuery.setString(2, itemName);
	            prepQuery.setString(3, itemSize);
	            prepQuery.setString(4, itemPrice);
	            prepQuery.setString(5, itemCategory);
	            prepQuery.setString(6, "Pending"); // item baru yang dibuat akan otomatis pending
	            prepQuery.setString(7, "");
	            prepQuery.setString(8, "");
	            prepQuery.setString(9, "");
	            int affected = prepQuery.executeUpdate();
	            // apabila ada affected rows pada tabel database berarti proses insert berhasil
	            if (affected > 0) { 
	                return "";
	            } else {
	            	// untuk menampilkan alert error
	                return "Item addition failed";
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return "Database error: " + e.getMessage();
	        }
	    }
	}

	// method ini digunakan Seller ketika mengklik button pada ViewSellerItemPage
	public String editItem(String itemId, String itemName, String itemCategory, String itemSize, String itemPrice) {
		// ketika Seller sudah melakukan update barang, kita perlu mengecek apakah informasi yang diberikan sesuai ketentuan atau tidak
	    String validation = checkItemValidation(itemName, itemCategory, itemSize, itemPrice);
	    if (!validation.equals("")) {
	        return validation;
	    } else {
	    	// query yang digunakan untuk edit item adalah UPDATE, tetap dengan PreparedStatement
	        String query = "UPDATE item SET itemName = ?, itemSize = ?, itemPrice = ?, itemCategory = ?, itemStatus = ? WHERE itemId = ?";
	        PreparedStatement prepQuery = db.prepStatement(query);
	        try {
	            prepQuery.setString(1, itemName);
	            prepQuery.setString(2, itemSize);
	            prepQuery.setString(3, itemPrice);
	            prepQuery.setString(4, itemCategory);
	            prepQuery.setString(5, "Approved"); // karena seller hanya bisa edit item yang telah diterima admin, otomatis item statusnya tetap Approved
	            prepQuery.setString(6, itemId);
	            int affected = prepQuery.executeUpdate();
	            if (affected > 0) {
	                return "";
	            } else {
	                return "Item update failed";
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return "Database error: " + e.getMessage();
	        }
	    }
	}

	// method ini digunakan untuk menghapus berdasarkan baris yang diklik oleh pengguna pada saat tabel ditampilkan
	public String deleteItem(String itemId) {
		// query yang digunakan untuk menghapus item adalah DELETE
	    String query = "DELETE FROM item WHERE itemId = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    try {
	        prepQuery.setString(1, itemId);
	        int affected = prepQuery.executeUpdate();
	        if (affected > 0) {
	            return "";
	        } else {
	            return "Item deletion failed";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error: " + e.getMessage();
	    }
	}

	// karena pada dokumen soal tidak dijelaskan fungsionalitas dari browseItem, maka kami improvisasi
	public ArrayList<Item> browseItem(String searchItem) {
		// query ini untuk mencari nama barang dari pengguna apakah merupakan bagian dari nama lengkap item pada database
        String query = "SELECT * FROM item WHERE LOWER(itemName) LIKE LOWER(?)";
        PreparedStatement prepQuery = db.prepStatement(query);
        ArrayList<Item> foundItems = new ArrayList<>();
        try {
            prepQuery.setString(1, "%" + searchItem + "%"); // untuk memungkinkan keyword yang dicari bisa merupakan nama depan, tengah, atau belakang
            ResultSet rs = prepQuery.executeQuery();
            while (rs.next()) {
                String itemId = rs.getString("itemId");
                String itemName = rs.getString("itemName");
                String itemSize = rs.getString("itemSize");
                String itemPrice = rs.getString("itemPrice");
                String itemCategory = rs.getString("itemCategory");
                String itemStatus = rs.getString("itemStatus");
                String itemWishlist = rs.getString("itemWishlist");
                String userId = rs.getString("userId");
                String itemOfferStatus = rs.getString("itemOfferStatus");
                foundItems.add(new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus));
            }
            // apabila item yang dicari tidak ada
            if (foundItems.isEmpty()) {
                foundItems.add(new Item("-", "No Items Matched with Keyword", "-", "-", "-", "-", "-", "-", "-"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundItems;
    }
	
	// method ini untuk menampilkan item dengan status Approved saja
	public ArrayList<Item> viewItem() {
	    String query = "SELECT * FROM item WHERE itemStatus = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    ArrayList<Item> approvedItems = new ArrayList<>();
	    try {
	        prepQuery.setString(1, "Approved"); // itemStatus harus Approved
	        ResultSet rs = prepQuery.executeQuery();
	        while (rs.next()) {
	            String itemId = rs.getString("itemId");
	            String itemName = rs.getString("itemName");
	            String itemSize = rs.getString("itemSize");
	            String itemPrice = rs.getString("itemPrice");
	            String itemCategory = rs.getString("itemCategory");
	            String itemStatus = rs.getString("itemStatus");
	            String itemWishlist = rs.getString("itemWishlist");
	            String userId = rs.getString("userId");
	            String itemOfferStatus = rs.getString("itemOfferStatus");
	            approvedItems.add(new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return approvedItems;
	}
	
	// method ini untuk mengecek keabsahan informasi pada saat INSERT ataupun UPDATE disertai dengan alasan mengapa masih belum sesuai
	public String checkItemValidation(String itemName, String itemCategory, String itemSize, String itemPrice) {
	    if (itemName.isEmpty() || itemName.length() < 3) {
	        return "Item name cannot be empty and minimum 3 character long";
	    }
	    if (itemCategory.isEmpty() || itemCategory.length() < 3) {
	        return "Item category cannot be empty and minimum 3 character long";
	    }
	    if (itemSize.isEmpty()) {
	        return "Item stock cannot be empty";
	    }
	    if (itemPrice.isEmpty() || itemPrice.length() < 3) {
	        return "Item price cannot be empty and minimum 3 character long";
	    }
	    try {
	        Double.parseDouble(itemPrice);
	    } catch (NumberFormatException e) {
	        return "Item price must be a valid number";
	    }
	    return ""; // kalau sudah benar semua, akan return ""
	}
	
	// method ini digunakan oleh admin untuk melihat semua item yang masih berstatus Pending
	public ArrayList<Item> viewRequestedItem() {
	    String query = "SELECT * FROM item WHERE itemStatus = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    ArrayList<Item> pendingItems = new ArrayList<>();
	    try {
	        prepQuery.setString(1, "Pending"); // mengecek itemStatus = "Pending"
	        ResultSet rs = prepQuery.executeQuery();
	        while (rs.next()) {
	            String itemId = rs.getString("itemId");
	            String itemName = rs.getString("itemName");
	            String itemSize = rs.getString("itemSize");
	            String itemPrice = rs.getString("itemPrice");
	            String itemCategory = rs.getString("itemCategory");
	            String itemStatus = rs.getString("itemStatus");
	            String itemWishlist = rs.getString("itemWishlist");
                String userId = rs.getString("userId");
	            String itemOfferStatus = rs.getString("itemOfferStatus");
	            pendingItems.add(new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return pendingItems;
	}
	
	// method ini terlalu abstrak pada Class Diagram, karena tidak ada tempat untuk menampung offer price sementara, maka kami alihkan ke itemWishlist
	public String offerPrice(String itemId, String itemOfferPrice, String userId) {
	    // mengecek apakah item terkait sudah memiliki harga offer (pada itemWishlist) dan mengambil harga aslinya untuk dibandingkan
	    String query = "SELECT itemWishlist, itemPrice FROM item WHERE itemId = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    try {
	        prepQuery.setString(1, itemId);
	        ResultSet rs = prepQuery.executeQuery();
	        if (rs.next()) {
	            String currentWishlist = rs.getString("itemWishlist"); // harga yang tertera pada itemWishlist
	            double originalPrice = rs.getDouble("itemPrice"); // harga asli barang yang ditetapkan Seller
	            double newOfferPrice = Double.parseDouble(itemOfferPrice); // harga tawar barang oleh Buyer
	            // berhubung kami menggunakan double untuk mempermudah perbandingan harga, maka perlu menghapus ".0" di belakang stringnya
	            String formattedOfferPrice = newOfferPrice % 1 == 0 ? String.format("%.0f", newOfferPrice) : String.valueOf(newOfferPrice);
	            // If no offer exists (itemWishlist is empty), create the offer
	            // kondisinya adalah sebagai berikut:
	            // 1. jika itemWishlist belum memiliki penawaran harga, maka Buyer dapat membuat harga yang lebih rendah daripada harga asli
	            // 2. jika itemWishlist sudah terisi, maka Buyer tidak boleh membuat harga yang lebih rendah daripada harga pada itemWishlist sekarang
	            if (currentWishlist == null || currentWishlist.isEmpty()) {
	                if (newOfferPrice <= originalPrice) {
	                    // karena memenuhi, maka penawaran harga yang dibuat masuk ke database beserta dengan userId yang menawar
	                    String updateQuery = "UPDATE item SET itemWishlist = ?, userId = ? WHERE itemId = ?";
	                    PreparedStatement updatePrepQuery = db.prepStatement(updateQuery);
	                    updatePrepQuery.setString(1, formattedOfferPrice);
	                    updatePrepQuery.setString(2, userId);
	                    updatePrepQuery.setString(3, itemId);
	                    // penambahan berhasil
	                    int affected = updatePrepQuery.executeUpdate();
	                    if (affected > 0) {
	                        return "";
	                    } else {
	                        return "Failed to place the offer.";
	                    }
	                } else {
	                    return "Offer price must be lower than or equal to the original price.";
	                }
	            } else {
	                double currentWishlistPrice = Double.parseDouble(currentWishlist);
	                if (newOfferPrice < currentWishlistPrice) {
	                    // karena penawaran lebih rendah daripada harga pada itemWishlist saat ini, otomatis ditolak
	                    return "Your offer is too low. The current offer is higher.";
	                } else {
	                	// karena memenuhi, maka penawaran harga yang baru diterima
	                    String updateOfferQuery = "UPDATE item SET itemWishlist = ?, userId = ? WHERE itemId = ?";
	                    PreparedStatement updateOfferPrepQuery = db.prepStatement(updateOfferQuery);
	                    updateOfferPrepQuery.setString(1, formattedOfferPrice);
	                    updateOfferPrepQuery.setString(2, userId);
	                    updateOfferPrepQuery.setString(3, itemId);
	                    // update berhasil
	                    int affected = updateOfferPrepQuery.executeUpdate();
	                    if (affected > 0) {
	                        return "";
	                    } else {
	                        return "Failed to update the offer.";
	                    }
	                }
	            }
	        } else {
	            return "Item not found.";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Database error: " + e.getMessage();
	    } catch (NumberFormatException e) {
	        return "Offer price must be a valid number.";
	    }
	}
	
	// apabila offer diterima oleh Seller, maka akan menghapus itemWishlist (offer price) dan userId yang membuat penawaran
	public void acceptOffer(String itemId) {
	    String query = "UPDATE item SET itemWishlist = '', userId = '' WHERE itemId = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    try {
	    	prepQuery.setString(1, itemId);
	    	prepQuery.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// berhubung Class Diagram terlalu abstrak dan tidak ada tempat untuk menaruh "reason" penolakan
	// sedangkan Seller wajib memberi alasan mengapa offer ditolak, maka kita taruh di itemOfferStatus
	public void declineOffer(String itemId, String reason) {
	    String query = "UPDATE item SET itemWishlist = '', userId = '', itemOfferStatus = ? WHERE itemId = ?";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    try {
	    	prepQuery.setString(1, reason); // alasan penolakan ditaruh pada itemOfferStatus
	    	prepQuery.setString(2, itemId);
	    	prepQuery.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// method untuk admin dalam ViewRequestItemPage apakah akan di approve atau decline
	public String approveItem(String itemId) {
        String query = "UPDATE item SET itemStatus = ? WHERE itemId = ?";
        PreparedStatement prepQuery = db.prepStatement(query);
        try {
            prepQuery.setString(1, "Approved"); // item di-Approved
            prepQuery.setString(2, itemId);
            int affected = prepQuery.executeUpdate();
            if (affected > 0) {
                return "";
            } else {
                return "Item not found or status already updated.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error while updating item status.";
        }
    }
	
	// karena declineItem juga membutuhkan alasan penolakan, jadi kami taruh di itemOfferStatus seperti reason untuk declineOffer()
	public String declineItem(String itemId, String reason) {
        String query = "UPDATE item SET itemStatus = ?, itemOfferStatus = ? WHERE itemId = ?";
        PreparedStatement prepQuery = db.prepStatement(query);
        try {
            prepQuery.setString(1, "Declined");
            prepQuery.setString(2, reason);
            prepQuery.setString(3, itemId);
            int affected = prepQuery.executeUpdate();
            if (affected > 0) {
                return "";
            } else {
                return "Item not found or status already updated.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error while updating item status.";
        }
    }
	
	public void viewAcceptedItem(String itemId) {
		// fungsional ini sama dengan viewItem() secara method viewItem() sendiri harus menampilkan itemStatus = "Approved" yang mana berarti Accepted
	}
	
	// method ini untuk menampilkan semua item yang ada ditawar oleh Buyer
	public ArrayList<Item> viewOfferItem() {
		// berhubung kami menaruh offer price pada itemWishlist, maka query digunakan untuk mencari itemWishlist yang ada isinya
	    String query = "SELECT * FROM item WHERE itemWishlist IS NOT NULL AND itemWishlist != ''";
	    PreparedStatement prepQuery = db.prepStatement(query);
		ArrayList<Item> offeredItems = new ArrayList<>();
		try {
	        ResultSet rs = prepQuery.executeQuery();
	        while (rs.next()) {
	            String itemId = rs.getString("itemId");
	            String itemName = rs.getString("itemName");
	            String itemSize = rs.getString("itemSize");
	            String itemPrice = rs.getString("itemPrice");
	            String itemCategory = rs.getString("itemCategory");
	            String itemStatus = rs.getString("itemStatus");
	            String itemWishlist = rs.getString("itemWishlist");
	            String userId = rs.getString("userId");
	            String itemOfferStatus = rs.getString("itemOfferStatus");
	            offeredItems.add(new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return offeredItems;
	}
	
	// method untuk membuat itemId unik dalam fungsional create
	public String generateItemId() {
	    Random random = new Random();
	    String id;
	    do {
	        int number = random.nextInt(1000);
	        id = "IT" + String.format("%03d", number);
	    } while (!itemIdUniqueness(id));
	    return id;
	}

	// method untuk memastikan bahwa setiap item memiliki ID yang unik
	public boolean itemIdUniqueness(String itemId) {
	    String query = "SELECT COUNT(*) FROM item WHERE itemId = ?";
	    try (PreparedStatement ps = db.prepStatement(query)) {
	        ps.setString(1, itemId);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) == 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// method tambahan untuk menampilkan semua item tanpa melihat itemStatusnya
	public ArrayList<Item> viewAllItem() {
	    String query = "SELECT * FROM item";
	    PreparedStatement prepQuery = db.prepStatement(query);
	    ArrayList<Item> allItems = new ArrayList<>();
	    try {
	        ResultSet rs = prepQuery.executeQuery();
	        while (rs.next()) {
	            String itemId = rs.getString("itemId");
	            String itemName = rs.getString("itemName");
	            String itemSize = rs.getString("itemSize");
	            String itemPrice = rs.getString("itemPrice");
	            String itemCategory = rs.getString("itemCategory");
	            String itemStatus = rs.getString("itemStatus");
	            String itemWishlist = rs.getString("itemWishlist");
	            String userId = rs.getString("userId");
	            String itemOfferStatus = rs.getString("itemOfferStatus");
	            allItems.add(new Item(itemId, itemName, itemSize, itemPrice, itemCategory, itemStatus, itemWishlist, userId, itemOfferStatus));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return allItems;
	}
	
}
