package com.cognixia.jump.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JwtRequestFilter;
import com.cognixia.jump.repository.UserRepository;
 
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
 
 	@Autowired
    private UserRepository userRepo;
 	
 	@Autowired JwtRequestFilter jwtRequestFilter;
 	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(
            username -> userRepo.findByUsername(username)
                .orElseThrow(
                    () -> new UsernameNotFoundException("User " + username + " not found.")));
    }
 
   @Bean
   public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
   }
   
 
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	
//    	http.csrf().disable();
//    	http.authorizeRequests().anyRequest().permitAll();
//    	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	
    	//got authenation to work for get all user 
        http.csrf().disable()
        		.authorizeRequests()
        		.antMatchers("/api/authenticate").permitAll()
        		.antMatchers("/api/auth/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/user").hasRole("ADMIN")
    			.antMatchers(HttpMethod.DELETE,"/api/delete/cart/{id}").hasRole("ADMIN")
    			.and().httpBasic();
              
        
//        http.exceptionHandling()
//        .authenticationEntryPoint(
//            (request, response, ex) -> {
//                response.sendError(
//                    HttpServletResponse.SC_UNAUTHORIZED,
//                    ex.getMessage()
//                );
//            }
//        );
 
      http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
 
 
}
