package model;

public class Transaction {
	private String userId;
	private String itemId;
	private String transactionId;
	
	public Transaction(String userId, String itemId, String transactionId) {
		super();
		this.userId = userId;
		this.itemId = itemId;
		this.transactionId = transactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
