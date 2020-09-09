package com.kuma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kuma.model.BookModel;
import com.kuma.model.CommentForm;
import com.kuma.model.CommentModel;
import com.kuma.model.UserModel;
import com.kuma.service.BookService;
import com.kuma.service.CommentService;
import com.kuma.service.UserService;

@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;

	@PostMapping("/bookDetail/{bookId}/postComment")
	public String postComment(@ModelAttribute CommentForm form,
			Model model, HttpServletRequest httpServletRequest, @PathVariable("bookId") int bookId) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		BookModel book = bookService.selectOne(bookId);
		CommentModel comment = new CommentModel();
		comment.setUser(user);
		comment.setBook(book);
		comment.setComment((String) form.getComment());
		boolean result = commentService.insert(comment);
		if(result == true) {
			System.out.println("登録成功");
		} else {
			System.out.println("登録失敗");
		}
		return "redirect:/bookDetail/{bookId}";
	}
}
