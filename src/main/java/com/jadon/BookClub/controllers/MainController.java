package com.jadon.BookClub.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jadon.BookClub.models.Book;
import com.jadon.BookClub.models.LoginUser;
import com.jadon.BookClub.models.User;
import com.jadon.BookClub.services.BookService;
import com.jadon.BookClub.services.UserService;



@Controller
public class MainController {
    
    @Autowired
    private UserService userServ;
    
    @Autowired
    private BookService bookService;
    
    ///////////BOOKS////////////
    
//	show all books
    //I ADDED THE book AND MODEL ATTRIBUTE TO THE SUCCESS ROUTE AND IT WORKS
    @RequestMapping("/home")
    public String showAll(Model model) {
        List<Book> books = bookService.allBooks();
        model.addAttribute("books", books);
        return "/login/landing.jsp";
    }
    
    //FIND ONE AND RETURN TO JSP THROUGH ADD ATTRIBUTE
    @GetMapping("/books/{bookId}")
    public String getOneBook(@PathVariable("bookId") Long id, Model model) {
    	//FINDING
    	Book thisBook = bookService.findBook(id); 
    	//PASS TO JSP
    	model.addAttribute("thisBook", thisBook);
    	model.addAttribute("user", thisBook.getUser());
    	return "/books/show.jsp";
    }
    
//  render create page
   @RequestMapping("/books/new")
   public String newBook(@ModelAttribute("book") Book book, Model model) {
       model.addAttribute("books", bookService.allBooks());
	   return "/books/new.jsp";
   }
   
//   create method
   @RequestMapping(value="/books", method=RequestMethod.POST)
   //Include session to find which user is logged in
   public String create(@Valid @ModelAttribute("book") Book book, BindingResult result, HttpSession s) {
	   //reaching in session to get users id
	   Long userID = (Long) s.getAttribute("user_id");
	   //finding the user by Id
	   User thisUser = userServ.findOne(userID);
	   System.out.println(userID);
	   if (result.hasErrors()) {
           return "/books/new.jsp";
       } else {
    	   //session the new book to have a user_id of the logged users ID
    	   book.setUser(thisUser);
    	   //crate the book!
           bookService.createBook(book);
           return "redirect:/home";
       }
   }
  
    ////////////USER//////////
    //CREATE USER
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("newLogin", new LoginUser());
        return "login/index.jsp";
    }
    
    //CREATE USER
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") User newUser, 
            BindingResult result, Model model, HttpSession session) {
        userServ.register(newUser, result);
        if(result.hasErrors()) {
            model.addAttribute("newLogin", new LoginUser());
            return "login/index.jsp";
        }
        session.setAttribute("user_id", newUser.getId());
        return "redirect:/home";
    }
    
    //SUCCESS
	@GetMapping("/home")
	public String home(HttpSession s, Model model) {
//		retrieve what is in session
		Long userID = (Long) s.getAttribute("user_id");
		List<Book> books = bookService.allBooks();
//		Book thisBook = bookService.findBook(id); 
//		route guard
//		check if userID is or is not null
		if (userID == null) {
			return "redirect:/";
		} else {			
			User thisUser = userServ.findOne(userID);
			System.out.println(thisUser);
			model.addAttribute("name", thisUser.getUserName());
			model.addAttribute("books", books);
			return "login/landing.jsp";
		}
	}
    
    //LOGIN USER
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
            BindingResult result, Model model, HttpSession session) {
        User user = userServ.login(newLogin, result);
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "login/index.jsp";
        }
        session.setAttribute("user_id", user.getId());
        return "redirect:/home";
    }
    
    //LOGOUT
	@GetMapping("/logout")
	public String logout(HttpSession s) {
		s.invalidate();
		return "redirect:/";
	}
    
}
