package com.java.bookStore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.bookStore.helper.Helper;
import com.java.bookStore.pojo.Customer;
import com.java.bookStore.pojo.Order;

@Controller
@RequestMapping("/staffHome")
public class StaffController {
	
	@GetMapping("")
	public ModelAndView staffHome() {
		ModelAndView andView = new ModelAndView("staffHome");
		
		return andView;
	}
	
	@GetMapping("/customers")
	public ModelAndView customer() {
		ModelAndView andView = new ModelAndView("staffCustomer");
		
		String responeCustomer = Helper.getDataTypeGet("http://localhost:8080/api/customer/allCustomer");
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			Customer[] customers = mapper.readValue(responeCustomer, Customer[].class);
			
			andView.addObject("customers", customers);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/orders")
	public ModelAndView orders() {
		ModelAndView andView = new ModelAndView("staffOrders");
		
		String responeOrder = Helper.getDataTypeGet("http://localhost:8080/api/order/getOrders");
		ObjectMapper mapper = new ObjectMapper();
		try {
			Order[] orders = mapper.readValue(responeOrder, Order[].class);
			
			andView.addObject("orders", orders);
			
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
