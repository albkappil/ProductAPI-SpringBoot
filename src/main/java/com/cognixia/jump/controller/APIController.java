package com.cognixia.jump.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import com.cognixia.jump.exception.NoRecordExistsException;
import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.Cart;
import com.cognixia.jump.model.Product;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.CartRepository;
import com.cognixia.jump.repository.ProductRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.util.JwtUtil;


@RequestMapping("/api")
@RestController
public class APIController {
	
	@Autowired 
	AuthenticationManager authManager;
	 
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CartRepository cartRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	
	@Autowired JwtUtil jwtUtil;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	
	// user Methods
	
	@GetMapping("/user")
	public List<User> getUsers() {
		return userRepo.findAll();
	}
	
	@GetMapping("/user/admin={role}")
	public List<User> getAllAdminUsers(@PathVariable Role role ) {
		List<User> admins = userRepo.findAdminUserByID(role);
		
		
		return admins;
	}
	
	@GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userRepo.findByUsername(username).get();
        if (user == null) {
         throw new NoRecordExistsException();
         }
       return new ResponseEntity<>(user, HttpStatus.OK); 
	}
	
	@GetMapping("/user/cost")
	public ResponseEntity<?> getUserWithMaxCostPurchase(){
		User costly = userRepo.getUserWithMaxCost();
		if(costly == null) {
			return ResponseEntity.status(404).body("No user Found");
		}
		else {
			
			return ResponseEntity.status(201).body(costly);
		}
		
		
	}
	
	@GetMapping("/user/cost={userId}")
	public ResponseEntity<?> getAllPurchaseCostById(@PathVariable("userId") int userId){
		Optional<User> userOP = userRepo.findById(userId);
		User user = userOP.get();
		
		if(user.getCartList().isEmpty() || user.getCartINList(0) == null)
			return ResponseEntity.status(404).body("No Carts Found");
		double totalAmount = 0;
		for(Cart e : user.getCartList()) {
			
			totalAmount += e.getAmount();
		}
		String expenseName =user.getUsername() +" has a Total Expense of $" + totalAmount;
		return ResponseEntity.status(201).body(expenseName);
		
		
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> createUser( @Valid @RequestBody User user ) {
		
		user.setId(null);
		
		// encode the password before it gets saved to the db, if not password will be stored
		// as plain text and cause issues
		// security won't encode our password for us when we do our POST, so we do it here on this line
		user.setPassword( encoder.encode( user.getPassword() ) );
		
		User created = userRepo.save(user);
		
		return ResponseEntity.status(201).body(created);
		
	}
	
	
	@PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
             
        } catch (BadCredentialsException e) {
        	throw new Exception("Incorrect username or password");
        }
    		
    		// generate the token for that user
            final UserDetails user = userDetailsService.loadUserByUsername( request.getUsername() );
    		String accessToken = jwtUtil.generateTokens(user);
    	
    		AuthenticationResponse response = new AuthenticationResponse(user.getUsername(), accessToken);
             
            return ResponseEntity.status(201).body(response);
             
       
    }
	
	
	
	@PostMapping("/auth/login/{username}/{password}")
    public ResponseEntity<?> loginURI(@PathVariable("username") String username, @PathVariable("password") String password) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		username, password)
            );
             
        } catch (BadCredentialsException e) {
        	throw new Exception("Incorrect username or password");
        }
    		
    		// generate the token for that user
            final UserDetails user = userDetailsService.loadUserByUsername(username);
    		String accessToken = jwtUtil.generateTokens(user);
    	
    		AuthenticationResponse response = new AuthenticationResponse(user.getUsername(), accessToken);
             
            return ResponseEntity.status(201).body(response);
             
       
    }
	

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") int id) {
		try {
			if(userRepo.findById(id) == null)
				return ResponseEntity.status(404).body("Nothing to Delete, User not Found");
			userRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body("Failed to delete");
		}
		

	        return ResponseEntity.status(201).body("User Deleted");
	}
	
	
	
	
	
	
	
	//Cart methods
	
	@GetMapping("/cart")
	public List<Cart> getPurchases() {
		return cartRepo.findAll();
	}
	
	
	@PostMapping("/user={userId}/cart")
	public ResponseEntity<?> CreateCart(@PathVariable("userId") int userId) {
		User user = userRepo.findById(userId).get();
		Cart cart = new Cart();
		cart.setDate(LocalDateTime.now().toString());
		cart.setUser(user);
		user.getCartList().add(cart);
		Cart created = cartRepo.save(cart);
		
		return ResponseEntity.status(201).body(created);
		
		
	}
	
	
	@PostMapping("user={userId}/cart={cartId}/{pId}")
	public ResponseEntity<?> makePurchaseByUserID(@PathVariable("userId") int userId, @PathVariable("cartId") int cartId, @PathVariable("pId") int pId) {
		
		Optional<User> user = userRepo.findById(userId);
		Optional<Product> product = productRepo.findById(pId);
		Optional<Cart> cartOp = cartRepo.findById(cartId);
		
		
		if(user.isEmpty() || product .isEmpty() || cartOp.isEmpty()) {
			return ResponseEntity.status(404).body("Cant make purchase, User,Cart,or product can't be found");
		}
		//List<Product> p = cart.getAllProducts();
		//p.add(product);
		//cart.setAllProducts(p);
		Cart cart = cartOp.get();
		
		cart.setDate(LocalDateTime.now().toString());
		
		cart.addProduct(product.get());
		product.get().addCart(cart);
		
		double amount = cart.getAmount();
		amount += product.get().getPrice();
		cart.setAmount(amount);
		
		
		Cart created = cartRepo.save(cart);
		

		
		return ResponseEntity.status(201).body(created);
		
	}
	
	@GetMapping("/cart/cost")
	public List<Cart> getTotalCost() {
		return cartRepo.findAll();
	}
	
	
	@GetMapping("/cart{cartId}/product")
	public List<Product> getProductFromCart(@PathVariable("cartId") int cartId){
		Cart cart = cartRepo.findById(cartId).get();
		
		
		return cart.getAllProducts();
	}
	
	
	
	;
	// product methods
	
	
	@GetMapping("/product")
	public List<Product> getProducts() {
		return productRepo.findAll();
	}
	
	@PostMapping("/product")
	public ResponseEntity<?> createProduct(@RequestBody Product product) {
		
		Product created = productRepo.save(product);
		
		if(created == null) {
			return ResponseEntity.status(404).body("Nothing to Update, Product not Found");
		}
		else {
			
			return ResponseEntity.status(201).body(created);
		}
		
	}
	
	@PutMapping("/product={id}")
	public ResponseEntity<?> updateProductById(@PathVariable("id") int id,@RequestBody Product product) {
		Product update = productRepo.findById(id).get();
		update = product;
		Product updated = productRepo.save(update);
		
		if(updated == null) {
			return ResponseEntity.status(404).body("Nothing to Update, Product not Found");
		}
		else {
			
			return ResponseEntity.status(201).body(updated);
		}
	}
	
	@DeleteMapping("/delete/product/{id}")
	public ResponseEntity<?> deleteProductById(@PathVariable("id") int id) {
	    try {
			if(cartRepo.findById(id) == null)
				return ResponseEntity.status(404).body("Nothing to Delete, Cart not Found");
			
			productRepo.deleteById(id);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body("Failed to delete");
			
		}
	        return ResponseEntity.status(201).body("Product Deleted");
	        
 }
	
	 
	
	@DeleteMapping("/delete/cart/{id}")
	public ResponseEntity<?> deleteCartById(@PathVariable("id") int id) {
		try {
			
			if(cartRepo.findById(id) == null )
				return ResponseEntity.status(404).body("Nothing to Delete, Cart not Found");
			cartRepo.deleteById(id);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(404).body("Failed to delete");
		}
		

	        return ResponseEntity.status(201).body("Cart Deleted");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
