package com.java.bookStore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.bookStore.helper.Helper;
import com.java.bookStore.pojo.Customer;
import com.java.bookStore.pojo.User;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@GetMapping("")
	public ModelAndView viewCart(@RequestParam("userName") String userName) {
		ModelAndView andView = new ModelAndView("cart");
		
		String responeUser = Helper.getDataTypeGet("http://localhost:8080/api/user/allUser");
		String responeCustomer = Helper.getDataTypeGet("http://localhost:8080/api/customer/allCustomer");
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			User[] users = mapper.readValue(responeUser, User[].class);
			Customer[] customers  = mapper.readValue(responeCustomer, Customer[].class);
			
			User user = null;
			for (User userIndex : users) {
				if (userIndex.getUsername().equals(userName)) {
					user = userIndex;
					break;
				}
			}
			
			Customer customer = new Customer();
			if (user != null) {
				for (Customer customerIndex : customers) {
					if (customerIndex.getId_user() == user.getId()) {
						customer = customerIndex;
						break;
					}
				}
			}
			
			andView.addObject("customer", customer);
			andView.addObject("user", user);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
}
