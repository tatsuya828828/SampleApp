package com.kuma.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/bookDetail/{title}")
	public String getBookDetail(Model model, @PathVariable("title") String title) {
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		if(title != null && title.length()>0) {
			BookModel book = bookService.selectOne(title);
			model.addAttribute("book", book);
		}
		return "/header";
	}

	@GetMapping("/bookEdit/{title}")
	public String getBookEdit(@ModelAttribute BookForm form, Model model
			, @PathVariable("title") String title, HttpServletRequest httpServletRequest) {
		String userId = httpServletRequest.getRemoteUser();
		UserModel user = userService.selectOne(userId);
		BookModel book = bookService.selectOne(title);
		if(title != null && title.length()>0) {
			if(book.getUserId().equals(user.getUserId())) {
				form.setTitle(book.getTitle());
				form.setBody(book.getBody());
				form.setUserId(book.getUserId());
				model.addAttribute("contents", "book/bookEdit :: bookEdit_contents");
				model.addAttribute("bookForm", form);
				return "/header";
			}
		}
		return "redirect:/bookDetail/{title}";
	}
	@PostMapping(value="/bookEdit", params="update")
	public String postBookEdit(@ModelAttribute BookForm form, Model model) {
		BookModel book = new BookModel();
		book.setTitle(form.getTitle());
		book.setBody(form.getBody());
		book.setUserId(form.getUserId());
		try {
			// 更新実行
			boolean result = bookService.updateOne(book);
			if(result == true) {
				model.addAttribute("result", "更新成功");
			} else {
				model.addAttribute("result", "更新失敗");
			}
		} catch(DataAccessException e) {
			model.addAttribute("result", "更新失敗(トランザクションテスト)");
		}
		return getBookList(model);
	}
}
