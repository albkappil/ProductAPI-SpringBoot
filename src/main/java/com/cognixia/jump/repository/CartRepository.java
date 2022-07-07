package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Cart;
import com.cognixia.jump.model.Product;
import com.cognixia.jump.model.User;



@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
 
	public Optional<Cart> findById(int id);
	
	
	
	//@Query("select u from user u where u.id in ( Select user.id from cart c where c.amount = ?1)")
	//public User getUserWithCost(double cost);
}
