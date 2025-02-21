package com.ecommerce.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.config.JwtHelper;
import com.ecommerce.entity.JwtRequest;
import com.ecommerce.entity.JwtResponse;
import com.ecommerce.entity.User;



@RestController
@CrossOrigin("*")
public class Authentication {


		 @Autowired
		    private com.ecommerce.Service.UserDetailsSeriveImpl userDetailsService;

		    @Autowired
		    private AuthenticationManager manager;


		    @Autowired
		    private JwtHelper helper;
		
		    
		    @PostMapping("/generate-token")
		    public ResponseEntity<?> generatetokenlogin(@RequestBody JwtRequest request) throws Exception {

		    	System.out.println(request.getUsername());
		    	 try {
		             this.doAuthenticate(request.getUsername(), request.getPassword());
		         } catch (UsernameNotFoundException e) {
		             e.printStackTrace();
		             return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
		         } catch (BadCredentialsException e) {
		             e.printStackTrace();
		             return new ResponseEntity<>("Invalid Username or Password", HttpStatus.BAD_REQUEST);
		         }
		        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		        
		        String token = this.helper.generateToken(userDetails);

		    return ResponseEntity.ok(new JwtResponse(token));
		    }
		
		    
		//this method check for username and password    
		private void doAuthenticate(String email, String password) {

	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	        try {
	            manager.authenticate(authentication);


	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException(" Invalid Username or Password  !!");
	        }

	    }

	    @ExceptionHandler(BadCredentialsException.class)
	    public String exceptionHandler() {
	        return "Credentials Invalid !!";
	    }
		
	    @GetMapping("/current-user")
	    public User getCuurentUser(Principal pricipal) {
	     
	    	return (User)this.userDetailsService.loadUserByUsername(pricipal.getName());
	    }
		    
}
