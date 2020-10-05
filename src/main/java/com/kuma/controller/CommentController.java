package com.kuma.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		comment.setUser(user);
		comment.setBook(book);
		comment.setComment((String) form.getComment());
		comment.setEvaluation(num);
		boolean result = commentService.insert(comment);
		if(result == true) {
			System.out.println("登録成功");
		} else {
			System.out.println("登録処理失敗");
		}
		boolean result2 = bookService.updateEvaluation(comment.getBook().getId());
		if(result2 == true) {
			System.out.println("平均評価更新成功");
		} else {
			System.out.println("平均評価更新失敗");
		}
		return "redirect:/bookDetail/{bookId}";
	}

	@GetMapping("/bookDetail/{id}/editComment/{commentId}")
	public String getEditComment(@ModelAttribute CommentForm form, Model model, @PathVariable("id") int id
			, HttpServletRequest httpServletRequest, @PathVariable("commentId") int commentId) {
		System.out.println("0");
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		System.out.println("1");
		if(String.valueOf(id).length() > 0) {
			BookModel book = bookService.selectOne(id);
			UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
			CommentModel comment = commentService.selectOne(commentId);
			if(comment.getUser().equals(user)) {
				form.setId(id);
				form.setCreatedAt((Date) comment.getCreatedAt());
				form.setComment((String) comment.getComment());
				form.setEvaluation((int) comment.getEvaluation());
				form.setBook(book);
				form.setUser(user);
				model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
				model.addAttribute("bookForm", form);
				model.addAttribute("editId", commentId);
			}
			model.addAttribute("book", book);
			List<CommentModel> comments = commentService.selectMany(id);
			model.addAttribute("comments", comments);
			int count = bookService.evaluationCount(id);
			model.addAttribute("count", count);
			boolean result = commentService.confirmComment(user.getId(), book.getId());
			model.addAttribute("result", result);
		}
		return "/header";
	}

	@PostMapping("/bookDetail/{id}/editComment/{commentId}")
	public String postEditComment(@ModelAttribute CommentForm form, Model model, @RequestParam("num") int num) {
		CommentModel comment = new CommentModel();
		comment.setId((int) form.getId());
		comment.setComment((String) form.getComment());
		comment.setEvaluation((int) num);
		comment.setUser((UserModel) form.getUser());
		comment.setBook((BookModel) form.getBook());
		boolean result = commentService.update(comment);
		if(result == true) {
			System.out.println("コメント更新成功");
		} else {
			System.out.println("コメント更新失敗");
		}
		boolean result2 = bookService.updateEvaluation(form.getId());
		if(result2 == true) {
			System.out.println("平均評価更新成功");
		} else {
			System.out.println("平均評価更新失敗");
		}
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		return "redirect:/bookDetail/{id}";
	}
}
