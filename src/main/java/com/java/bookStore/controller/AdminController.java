package com.java.bookStore.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.bookStore.helper.Helper;
import com.java.bookStore.pojo.Book;
import com.java.bookStore.pojo.Order;
import com.java.bookStore.pojo.Publisher;
import com.java.bookStore.pojo.Role;
import com.java.bookStore.pojo.Stock;
import com.java.bookStore.pojo.User;

@Controller
@RequestMapping("/adminHome")
public class AdminController {
	
	@GetMapping("")
	public ModelAndView home() {
		ModelAndView andView = new ModelAndView("adminHome");
		
		return andView;
	}

	@GetMapping("/allBook")
	public ModelAndView allBook() {
		ModelAndView andView = new ModelAndView("allBook");
		
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] books = mapper.readValue(respone, Book[].class);
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
	        List<String> formattedPrices = new ArrayList<>();
	        for (Book book : books) {
	            double price = book.getPrice();
	            String formattedPrice = decimalFormat.format(price);
	            formattedPrices.add(formattedPrice);
	        }
	        andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("books", books);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/addBook")
	public ModelAndView addBook() {
		ModelAndView andView = new ModelAndView("addBook");
		
//		Get list Stock
		String responeStock = Helper.getDataTypeGet("http://localhost:8080/api/stock/allStock");
		String responePublisher = Helper.getDataTypeGet("http://localhost:8080/api/publisher/allPublisher");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Stock[] stocks = mapper.readValue(responeStock, Stock[].class);
			Publisher[] publishers = mapper.readValue(responePublisher, Publisher[].class);
			andView.addObject("publishers", publishers);
			andView.addObject("stocks", stocks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/editBook")
	public ModelAndView editBook(@RequestParam(name = "id", required = false) Integer id) {
		ModelAndView andView = new ModelAndView("editBook");
		
		String responeBook = Helper.getDataTypeGet("http://localhost:8080/api/book/getBookById?id=" + id);
		String responeStock = Helper.getDataTypeGet("http://localhost:8080/api/stock/allStock");
		String responePublisher = Helper.getDataTypeGet("http://localhost:8080/api/publisher/allPublisher");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book book = mapper.readValue(responeBook, Book.class);
			Stock[] stocks = mapper.readValue(responeStock, Stock[].class);
			Publisher[] publishers = mapper.readValue(responePublisher, Publisher[].class);
			andView.addObject("publishers", publishers);
			andView.addObject("stocks", stocks);
			andView.addObject("book", book);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
//	Stock Controller
	@GetMapping("/stock")
	public ModelAndView allStock() {
		ModelAndView andView = new ModelAndView("stock");
		
		String responeStock = Helper.getDataTypeGet("http://localhost:8080/api/stock/allStock");
		ObjectMapper mapper = new ObjectMapper();
		try {
			Stock[] stocks = mapper.readValue(responeStock, Stock[].class);
			andView.addObject("stocks", stocks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/addStock")
	public ModelAndView addStock() {
		ModelAndView andView = new ModelAndView("addStock");
		
		return andView;
	}
	
	@GetMapping("/updateStock")
	public ModelAndView updateStock(@RequestParam(name = "id", required = false) Integer id) {
		ModelAndView andView = new ModelAndView("editStock");
		
		String responeStock = Helper.getDataTypeGet("http://localhost:8080/api/stock/getStockById?id=" + id);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Stock stock = mapper.readValue(responeStock, Stock.class);
			andView.addObject("stock", stock);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
//	Order Controller
	
	@GetMapping("/orders")
	public ModelAndView orders() {
		ModelAndView andView = new ModelAndView("adminOrders");
		
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
	
//	User Controller
	@GetMapping("/users")
	public ModelAndView allUser() {
		ModelAndView andView = new ModelAndView("users");
		
		String responeUser = Helper.getDataTypeGet("http://localhost:8080/api/user/allUser");
		ObjectMapper mapper = new ObjectMapper();
		try {
			User[] users = mapper.readValue(responeUser, User[].class);
			andView.addObject("users", users);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return andView;
	}
	
	@GetMapping("/addUser")
	public ModelAndView addUser() {
		ModelAndView andView = new ModelAndView("addUser");
		
		String responeRole = Helper.getDataTypeGet("http://localhost:8080/api/role/allRole");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Role[] roles = mapper.readValue(responeRole, Role[].class);
			andView.addObject("roles", roles);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return andView;
	}
	
	@GetMapping("/updateUser")
	public ModelAndView updateUser(@RequestParam(name = "id", required = false) Integer id) {
		ModelAndView andView = new ModelAndView("editUser");
		
		String responeUser = Helper.getDataTypeGet("http://localhost:8080/api/user/getUserById?id=" + id);
		String responeRole = Helper.getDataTypeGet("http://localhost:8080/api/role/allRole");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue(responeUser, User.class);
			Role[] roles = mapper.readValue(responeRole, Role[].class); 
			andView.addObject("user", user);
			andView.addObject("roles", roles);
			
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
