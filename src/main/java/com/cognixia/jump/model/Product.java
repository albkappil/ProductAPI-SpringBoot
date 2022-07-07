package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;



	public static enum CPUType {
		INTEL3, INTEL5, INTEL7, INTEL9, RYZEN3, RYZEN5, RYZEN7,  RYZEN9, // roles should start with capital ROLE_ and be completely capital letters
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String brand;
	
	@Column(nullable = false)
	private String model;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CPUType cpuType;
	
	
	@Column(nullable = false)
	private int memory;
	
	@Min(10) 
	@Max(20)
	@Column(nullable = false)
	private int size; 
	
	
	 
	@Column(nullable = false)
	private double price;

	@ManyToMany 
	@JoinTable(name = "cart_product", joinColumns = @JoinColumn(name =
	"product_id", referencedColumnName = "id", nullable = true),
	inverseJoinColumns = @JoinColumn(name = "cart_id",
	referencedColumnName = "id", nullable = true))
	private List<Cart> cart = new ArrayList<Cart>();
	
	public Product() {
		
	}


	
	public Product(Integer id, String name, String brand, String model, CPUType cpuType, int memory,
			@Min(10) @Max(20) int size, double price, List<Cart> cart) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.model = model;
		this.cpuType = cpuType;
		this.memory = memory;
		this.size = size;
		this.price = price;
		this.cart = cart;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public CPUType getCpuType() {
		return cpuType;
	}

	public void setCpuType(CPUType cpuType) {
		this.cpuType = cpuType;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String brand, String model) {
		this.name = brand + " " + model;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@JsonIgnore
	@JsonProperty(value = "cart")
	public List<Cart> getCart() {
		return cart;
	}
	@JsonIgnore
	public void setCart(List<Cart> carts) {
		this.cart = carts;
	}
	
	
	public void addCart(Cart singleCart) {
		this.cart.add(singleCart);
	}

	@Override
	public String toString() {
		return "ProductComputers [id=" + id + ", brand=" + brand + ", model=" + model + ", cpuType=" + cpuType
				+ ", memory=" + memory + ", size=" + size + ", name=" + name + ", price=" + price + ", qtyAvailable="
			 + "]";
	}
	
	
	
	
	
}
