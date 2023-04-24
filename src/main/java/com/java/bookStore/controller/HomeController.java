package com.java.bookStore.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.java.bookStore.pojo.BookStock;
import com.java.bookStore.pojo.Customer;
import com.java.bookStore.pojo.Order;
import com.java.bookStore.pojo.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/home")
public class HomeController {

//	Lấy nhưng quyển sách theo category
	private List<Book> filterBooksByCategory(Book[] allBooks, String category, int limit) {
		List<Book> filteredBooks = new ArrayList<>();
		int count = 0;

		for (Book book : allBooks) {
			if (book.getCategory_name().contains(category) && count < limit) {
				filteredBooks.add(book);
				count++;
			}
		}

		return filteredBooks;
	}

//	Kiểm tra quantity book
	public Map<String, Integer> getBookQuantityMap(BookStock[] bookQuantity) {
	    Map<String, Integer> bookQuantityMap = new HashMap<>();
	    for (BookStock bookStock : bookQuantity) {
	        bookQuantityMap.put(String.valueOf(bookStock.getId_book()), bookStock.getQuantity());
	    }
	    return bookQuantityMap;
	}

	@GetMapping("")
	public ModelAndView index() {
		ModelAndView andView = new ModelAndView("index.html");

		String responeBooks = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(responeBooks, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
//			Get book by category
			List<String> categories = Arrays.asList("Classic Books", "Romance", "Kids", "Thrillers");
			Map<String, List<Book>> booksByCategory = new HashMap<>();
			for (String category : categories) {
				List<Book> filteredBooks = filterBooksByCategory(allBooks, category, 4);
				booksByCategory.put(category, filteredBooks);
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : allBooks) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("booksByCategory", booksByCategory);

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return andView;
	}

	@GetMapping("/register")
	public ModelAndView register() {
		ModelAndView andView = new ModelAndView("register");

		return andView;
	}

	@GetMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView andView = new ModelAndView("login");
		
		String responeUser = Helper.getDataTypeGet("http://localhost:8080/api/user/allUser");
		ObjectMapper mapper = new ObjectMapper();
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				String cookieValue = cookie.getValue();
				
				if (cookieName.equalsIgnoreCase("email")) {
					try {
						User[] users = mapper.readValue(responeUser, User[].class);
						User user = null;
						for (User userIndex : users) {
							if (userIndex.getEmail().equals(cookieValue)) {
								user = userIndex;
								break;
							}
						}
						response.sendRedirect("http://localhost:8081/home?userName=" + user.getUsername());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return andView;
	}
	
	@GetMapping("/allbooks")
	public ModelAndView allBook() {
		ModelAndView andView = new ModelAndView("allBooks");
		
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] books = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
//			Show sách ngẫu nhiên
			List<Book> bookList = Arrays.asList(books);
			Collections.shuffle(bookList);
			
//			Format price
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
	        List<String> formattedPrices = new ArrayList<>();
	        for (Book book : books) {
	            double price = book.getPrice();
	            String formattedPrice = decimalFormat.format(price);
	            formattedPrices.add(formattedPrice);
	        }
	        
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
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
	
	@GetMapping("/Classic Books")
	public ModelAndView allClassicBooks() {
		ModelAndView andView = new ModelAndView("allClassicBooks");
		
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> listClassicBooks = new ArrayList<>();
			for (Book book : allBooks) {
				if (book.getCategory_name().contains("Classic Books")) {
					listClassicBooks.add(book);
				}
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : listClassicBooks) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("AllClassicBooks", listClassicBooks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/Romance")
	public ModelAndView allRomance() {
		ModelAndView andView = new ModelAndView("allRomance");
		
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> listRomanceBooks = new ArrayList<>();
			for (Book book : allBooks) {
				if (book.getCategory_name().contains("Romance")) {
					listRomanceBooks.add(book);
				}
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : listRomanceBooks) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("AllRomanceBooks", listRomanceBooks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/Thrillers")
	public ModelAndView allThrillers() {
		ModelAndView andView = new ModelAndView("allThrillers");
		
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> listThrillersBooks = new ArrayList<>();
			for (Book book : allBooks) {
				if (book.getCategory_name().contains("Thrillers")) {
					listThrillersBooks.add(book);
				}
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : listThrillersBooks) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("AllThrillersBooks", listThrillersBooks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/Kids")
	public ModelAndView allKids() {
		ModelAndView andView = new ModelAndView("allKids");

		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> listKidsBooks = new ArrayList<>();
			for (Book book : allBooks) {
				if (book.getCategory_name().contains("Kids")) {
					listKidsBooks.add(book);
				}
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : listKidsBooks) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("AllKidsBooks", listKidsBooks);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/searchResults")
	public ModelAndView searchBooks(@RequestParam("searchName") String searchName) {
	    ModelAndView andView = new ModelAndView("searchResults");
	    
		String respone = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
		String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(respone, Book[].class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> listBooksBySearch = new ArrayList<>();
			for (Book book : allBooks) {
				if (book.getName().toLowerCase().contains(searchName.toLowerCase())) {
					listBooksBySearch.add(book);
				}
			}
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			for (Book book : listBooksBySearch) {
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			}
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("listBooksBySearch", listBooksBySearch);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return andView;
	}
	
	@GetMapping("/bookDetail")
	public ModelAndView bookDetail(@RequestParam(name = "id", required = false) Integer id) {
	    ModelAndView andView = new ModelAndView("bookDetail");
		
	    String responeAllBook = Helper.getDataTypeGet("http://localhost:8080/api/book/allBook");
	    String responeBook = Helper.getDataTypeGet("http://localhost:8080/api/book/getBookById?id=" + id);
	    String responeQuantity = Helper.getDataTypeGet("http://localhost:8080/api/stock/getQuantity");
	    
		ObjectMapper mapper = new ObjectMapper();
		try {
			Book[] allBooks = mapper.readValue(responeAllBook, Book[].class);
			Book book = mapper.readValue(responeBook, Book.class);
			BookStock[] bookQuantity = mapper.readValue(responeQuantity, BookStock[].class);
			
			List<Book> relatedProducts = new ArrayList<>();
			for (Book indexBook : allBooks) {
				if (indexBook.getCategory_name().contains(book.getCategory_name())) {
					relatedProducts.add(indexBook);
				}
			}
			andView.addObject("book", book);
			
//			Format Price
			List<String> formattedPrices = new ArrayList<>();
			
				double price = book.getPrice();
				String formattedPrice = Helper.formatPrice(price);
				formattedPrices.add(formattedPrice);
			
//			Kiểm tra quantity của book
			Map<String, Integer> bookQuantityMap = getBookQuantityMap(bookQuantity);
			
			andView.addObject("id", id);
			andView.addObject("bookQuantityMap", bookQuantityMap);
			andView.addObject("formattedPrices", formattedPrices);
			andView.addObject("relatedProducts", relatedProducts);
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
	
	@GetMapping("/orderResult")
	public ModelAndView orderResult() {
		ModelAndView andView = new ModelAndView("orderResult");
		
		return andView;
	}
	
	@GetMapping("/userSetting")
	public ModelAndView userSetting() {
		ModelAndView andView = new ModelAndView("userSetting");
		
		return andView;
	}
	
	@GetMapping("/changePassword")
	public ModelAndView changePassword() {
		ModelAndView andView = new ModelAndView("changePassword");
		
		return andView;
	}
	
	@GetMapping("/editProfile")
	public ModelAndView editProfile(@RequestParam("userName") String userName) {
		ModelAndView andView = new ModelAndView("editProfile");
		
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
	
	@GetMapping("/myOrders")
	public ModelAndView myOrders(@RequestParam("userName") String userName) {
		ModelAndView andView = new ModelAndView("myOrders");
		
		String responeOrder = Helper.getDataTypeGet("http://localhost:8080/api/order/getOrders");
		String responeCustomer = Helper.getDataTypeGet("http://localhost:8080/api/customer/allCustomer");
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Order[] orders = mapper.readValue(responeOrder, Order[].class);
			Customer[] customers = mapper.readValue(responeCustomer, Customer[].class);
			
			Customer customer = null;
			for (Customer customerIndex : customers) {
				if (customerIndex.getFull_name().equalsIgnoreCase(userName)) {
					customer = customerIndex;
				}
			}
			
			List<Order> orderOfCustomer = new ArrayList<>();
			if (customer != null) {
				for (Order orderIndex : orders) {
					if (orderIndex.getId_customer() == customer.getId()) {
						orderOfCustomer.add(orderIndex);
					}
				}
			}
			
			andView.addObject("orders", orderOfCustomer);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return andView;
	}
	
	@GetMapping("/cancelPayment")
	public ModelAndView cancelPayment() {
		ModelAndView andView = new ModelAndView("cancelPayment");
		
		return andView;
	}
	
}
