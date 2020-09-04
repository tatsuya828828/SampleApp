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
import com.kuma.model.CommentForm;
import com.kuma.model.CommentModel;
import com.kuma.model.UserModel;
import com.kuma.model.ValidGroup;
import com.kuma.service.BookService;
import com.kuma.service.CommentService;
import com.kuma.service.UserService;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;

	@GetMapping("/bookNew")
	public String getBookNew(@ModelAttribute BookForm bookForm, Model model) {
		model.addAttribute("contents", "book/new :: bookNew_contents");
		return "/header";
	}

	@PostMapping("/bookNew")
	public String postBookNew(@ModelAttribute @Validated(ValidGroup.class) BookForm form
			, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest) {
		if(bindingResult.hasErrors()) {
			return getBookNew(form, model);
		}
		String userId = httpServletRequest.getRemoteUser();
		UserModel user = userService.selectOne(userId);
		BookModel book = new BookModel();
		book.setTitle(form.getTitle());
		book.setBody(form.getBody());
		book.setAuthor(form.getAuthor());
		book.setUser(user);
		boolean result = bookService.insert(book);
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
	public String getBookDetail(@ModelAttribute CommentForm form, Model model, @PathVariable("title") String title) {
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		if(title != null && title.length()>0) {
			BookModel book = bookService.selectOne(title);
			model.addAttribute("book", book);
			List<CommentModel> comments = commentService.selectMany(title);
			model.addAttribute("comments", comments);
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
			if(book.getUser().equals(user)) {
				form.setTitle(book.getTitle());
				form.setNewTitle(book.getTitle());
				form.setBody(book.getBody());
				form.setAuthor(book.getAuthor());
				form.setUser(book.getUser());
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
		book.setNewTitle(form.getNewTitle());
		book.setBody(form.getBody());
		book.setAuthor(form.getAuthor());
		book.setUser(form.getUser());
		try {
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

	@PostMapping(value = "/bookEdit", params = "delete")
	public String postBookDelete(@ModelAttribute BookForm form, Model model) {
		boolean result = bookService.deleteOne(form.getTitle());
		if(result == true) {
			model.addAttribute("result", "削除成功");
		} else {
			model.addAttribute("result", "削除失敗");
		}
		return getBookList(model);
	}
}
