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

	// 成否を知らせるメソッド
	public void result(boolean result, String word) {
		if(result == true) {
			System.out.println(word+"成功");
		} else {
			System.out.println(word+"失敗");
		}
	}

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
		result(result, "登録");
		boolean result2 = bookService.updateEvaluation(comment.getBook().getId());
		result(result2, "評価平均更新");
		return "redirect:/bookDetail/{bookId}";
	}

	@GetMapping("/bookDetail/{id}/editComment/{commentId}")
	public String getEditComment(@ModelAttribute CommentForm form, Model model, @PathVariable("id") int id
			, HttpServletRequest httpServletRequest, @PathVariable("commentId") int commentId) {
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
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
				model.addAttribute("editId", commentId);
			} else {
				return "redirect:/bookDetail/{id}";
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
		result(result, "コメント更新");
		boolean result2 = bookService.updateEvaluation(form.getId());
		result(result2, "評価平均更新");
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		return "redirect:/bookDetail/{id}";
	}
}
