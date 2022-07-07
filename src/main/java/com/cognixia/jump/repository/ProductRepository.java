package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Cart;
import com.cognixia.jump.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	public Optional<Product> findById(int id);
	
	//@Query("select p from Product p where p.id = (Select product_id from Cart_Product b where cart_id = ?1 )")
	//public Optional<Product> getPurchasedProductsByCartId(int cart_id);
}


