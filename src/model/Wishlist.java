package model;

public class Wishlist {
	private String wishlistId;
	private String itemId;
	private String userId;
	
	public Wishlist(String wishlistId, String itemId, String userId) {
		super();
		this.wishlistId = wishlistId;
		this.itemId = itemId;
		this.userId = userId;
	}

	public String getWishlistId() {
		return wishlistId;
	}

	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
