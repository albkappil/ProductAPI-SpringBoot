package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
 
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	 
	public Optional<User> findByUsername(String username);
	 
	@Query("select u from User u where role = ?1")
	public List<User> findAdminUserByID(Role role);
	
	@Query("select u from User u where u.id in ( Select user.id from Cart c where c.amount = ( select Max(amount) from Cart))")
	public User getUserWithMaxCost();
}