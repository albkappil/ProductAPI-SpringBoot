package com.cognixia.jump.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
@Entity
public class Cart implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	private String date; 
	
	private double amount; 
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;


	@ManyToMany(mappedBy = "cart",cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
	private  List<Product> allProducts =  new ArrayList<Product>();
	

	public Cart() {
		
		
	}

	
	public Cart(Integer id, String date, double amount, User user, List<Product> allProducts) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.user = user;
		this.allProducts = allProducts;
	}
	


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}


	public double getAmount() {
		return this.amount;
	}

	

	public void setAmount(double amount) {
		this.amount = amount;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}

	

	public List<Product> getAllProducts() {
		return allProducts;
	}


	public void setAllProducts(List<Product> allProducts) {
		this.allProducts = allProducts;
	}
	
	public void addProduct(Product product) {
		this.allProducts.add(product);
	}
	
	///public Product getProduct(int i) {
	//	this.allProducts.add(product);
	//}
	

	@Override
	public String toString() {
		return "Cart [cart_id=" + id + ", date=" + date + ", amount=" + amount + ", user=" + user
				+ ", allProducts=" + "]";
	}
	
	
}
