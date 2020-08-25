package com.kuma.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kuma.model.BookForm;
import com.kuma.model.BookModel;
import com.kuma.model.UserModel;
import com.kuma.model.ValidGroup;
import com.kuma.service.BookService;
import com.kuma.service.UserService;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;

	@GetMapping("/bookNew")
	public String getBookNew(@ModelAttribute BookForm bookForm, Model model) {
		model.addAttribute("contents", "book/new :: bookNew_contents");
		return "/header";
	}

	@PostMapping("/bookNew")
	public String postBookNew(@ModelAttribute @Validated(ValidGroup.class) BookForm bookForm
			, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
		if(bindingResult.hasErrors()) {
			return getBookNew(bookForm, model);
		}
		String userId = httpServletRequest.getRemoteUser();
		UserModel user = userService.selectOne(userId);
		BookModel book = new BookModel();
		book.setTitle(bookForm.getTitle());
		book.setBody(bookForm.getBody());
		book.setUserId((String)user.getUserId());
		boolean result = bookService.insert(book);
		System.out.println(book);
		if(result == true) {
			System.out.println("登録成功");
		} else {
			System.out.println("登録失敗");
		}
		return "redirect:/mypage";
	}

	@GetMapping("/bookList")
	public String getBookList(Model model) {
		model.addAttribute("contents", "book/bookList :: bookList_contents");
		List<BookModel> bookList = bookService.selectMany();
		model.addAttribute("bookList", bookList);
		return "/header";
	}
}
