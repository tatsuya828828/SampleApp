package com.kuma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			Model model, HttpServletRequest httpServletRequest
			, @PathVariable("bookId") int bookId, @RequestParam("num") int num) {
		System.out.println("数値:"+ num);
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		BookModel book = bookService.selectOne(bookId);
		CommentModel comment = new CommentModel();
		int id = 0;
		String word = "登録";
		if(form.getId()>0) {
			id = form.getId();
			word = "更新";
		}
		comment.setId(id);
		comment.setUser(user);
		comment.setBook(book);
		comment.setComment((String) form.getComment());
		comment.setEvaluation(num);
		boolean result = commentService.selectAction(comment);
		if(result == true) {
			System.out.println(word +"成功");
		} else {
			System.out.println(word +"処理失敗");
		}
		boolean result2 = bookService.updateEvaluation(comment.getBook().getId());
		if(result2 == true) {
			System.out.println("平均評価更新成功");
		} else {
			System.out.println("平均評価更新失敗");
		}
		return "redirect:/bookDetail/{bookId}";
	}
}
