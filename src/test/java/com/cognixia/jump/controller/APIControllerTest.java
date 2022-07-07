package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cognixia.jump.model.Cart;
import com.cognixia.jump.model.Product;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(APIController.class)
public class APIControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	UserRepository userRepo;
	
	@InjectMocks
	private APIController controller;
	
	@Test
	void testGetAllUsers() throws Exception{
		
		
		String uri = "/api/user";
		
		
		List<User> users = new ArrayList<User>();
		
		users.add(new User(1, "sam@gmail.com","samsam",Role.ROLE_ADMIN,true));
		users.add(new User(2, "joey@gmail.com","joeyjoey",Role.ROLE_USER,true));
		
		when( controller.getUsers() ).thenReturn(users);
		
		
		mvc.perform( get(uri) )   // perform get request
				.andDo( print() ) // print request sent/response given
				.andExpect( status().isOk() ) // expect a 200 status code
				.andExpect( (ResultMatcher) content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) // checks content type is json
				.andExpect( jsonPath( "$.length()" ).value( users.size() ) ) // length of the list matches one above
				.andExpect( jsonPath("$[0].id").value(users.get(0).getId()) ) // check each column value for the cars in list
				.andExpect( jsonPath("$[1].username").value(users.get(0).getUsername()) )
				.andExpect( jsonPath("$[2].password").value(users.get(0).getPassword()) )
				.andExpect( jsonPath("$[3].role").value(users.get(1).getRole()) )
				.andExpect( jsonPath("$[4].enabled").value(users.get(1).isEnabled()) );
		
		((APIController) verify( users, times(1) )).getUsers(); // getAllCars() from service called once
		verifyNoMoreInteractions( users );
		
	}
	
	
	@Test
	void testGetPurchases() throws Exception{
		
		
		String uri = "/api/cart";
		
		
		List<Cart> cart = new ArrayList<Cart>();
		User user = new User();
		List<Product> products = new ArrayList<Product>();
		cart.add(new Cart(1,LocalDateTime.now().toString(), 0.0, user, products));
		cart.add(new Cart(2,LocalDateTime.now().toString(), 0.0, user, products));
		
		
		when( controller.getPurchases() ).thenReturn(cart);
		
		
		mvc.perform( get(uri) )   // perform get request
				.andDo( print() ) // print request sent/response given
				.andExpect( status().isOk() ) // expect a 200 status code
				.andExpect( (ResultMatcher) content().contentType( MediaType.APPLICATION_JSON_VALUE ) ) // checks content type is json
				.andExpect( jsonPath( "$.length()" ).value( cart.size() ) ) // length of the list matches one above
				.andExpect( jsonPath("$[0].id").value(cart.get(0).getId()) ) // check each column value for the cars in list
				.andExpect( jsonPath("$[1].date").value(cart.get(0).getDate()) )
				.andExpect( jsonPath("$[2].amount").value(cart.get(0).getAmount()) )
				.andExpect( jsonPath("$[3].user").value(cart.get(0).getUser()) );
		
		((APIController) verify( cart, times(1) )).getUsers(); // getAllCars() from service called once
		verifyNoMoreInteractions( cart );
		
	}
	
}
