package com.cognixia.jump.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Albka
 *
 
@Entity
@Table(name = "cart")
public class ShoppingCart {
	
	@Id
	@Column(name = "user_id")
	private Integer user_id;
	
	@Column(name = "username")
	private String username;
	
	private int numProducts; 
	
	private double totalPrice; 
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private User user;
	
	/
	
	
	public ShoppingCart() {
	}

	public ShoppingCart(String username, int numProducts, double totalPrice) {
		this.username = username;
		this.numProducts = numProducts;
		this.totalPrice = totalPrice;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		this.username = user.getUsername();
		return this.username;
	}

	public void setUsername(String username) {
		user.setUsername(username);
		this.username = user.getUsername();
	}

	public int getNumProducts() {
		this.numProducts = allProducts.size();
		return numProducts;
	}

	public void setNumProducts(int numProducts) {
		this.numProducts = numProducts;
	}

	public double getTotalPrice() {
		for(int i = 0; i < allProducts.size(); i++) {
			this.totalPrice += allProducts.get(i).getPrice();
		}
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ProductPC> getAllProducts() {
		return allProducts;
	}

	public void setAllProducts(List<ProductPC> allProducts) {
		this.allProducts = allProducts;
	}

	
	
	
	
}
*/