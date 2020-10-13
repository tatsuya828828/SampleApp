package com.kuma.controller;

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
		return "redirect:/bookDetail/{bookId}#bottom";
	}

	@GetMapping("/bookDetail/{bookId}/editComment/{commentId}")
	public String getEditComment(@ModelAttribute CommentForm form, Model model, @PathVariable("bookId") int bookId
			, HttpServletRequest httpServletRequest, @PathVariable("commentId") int commentId) {
		if(String.valueOf(bookId).length() > 0 && String.valueOf(commentId).length()>0) {
			BookModel book = bookService.selectOne(bookId);
			UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
			CommentModel comment = commentService.selectOne(commentId);
			if(comment.getUser().getId() == user.getId()) {
				form.setId(commentId);
				form.setComment((String) comment.getComment());
				form.setEvaluation((int) comment.getEvaluation());
				model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
				model.addAttribute("commentList", "book/commentList :: comment_list");
				model.addAttribute("replyList", "book/replyList :: reply_list");
				model.addAttribute("commentArea", "book/commentArea :: comment_area");
				model.addAttribute("editComment", "book/editComment :: edit_comment");
				model.addAttribute("editId", commentId);
			} else {
				return "redirect:/bookDetail/{bookId}";
			}
			model.addAttribute("book", book);
			List<CommentModel> comments = commentService.selectMany(bookId);
			model.addAttribute("comments", comments);
			int count = bookService.evaluationCount(bookId);
			model.addAttribute("count", count);
			boolean result = commentService.confirmComment(user.getId(), book.getId());
			model.addAttribute("result", result);
		}
		return "/header";
	}

	@PostMapping("/bookDetail/{bookId}/editComment/{commentId}")
	public String postEditComment(@ModelAttribute CommentForm form, Model model
			, @PathVariable("commentId") int commentId, @PathVariable("bookId") int bookId
			, @RequestParam("num") int num) {
		CommentModel comment = new CommentModel();
		comment.setId(commentId);
		comment.setComment(form.getComment());
		comment.setEvaluation(num);
		boolean result = commentService.update(comment);
		result(result, "コメント更新");
		boolean result2 = bookService.updateEvaluation(bookId);
		result(result2, "評価平均更新");
		return "redirect:/bookDetail/{bookId}#comment{commentId}";
	}

	@PostMapping(value="/bookDetail/{bookId}/editComment/{commentId}", params="delete")
	public String deleteComment(@PathVariable("commentId") int commentId
			, @PathVariable("bookId") int bookId) {
		if(String.valueOf(bookId).length()>0 && String.valueOf(commentId).length()>0) {
			boolean result = commentService.delete(commentId);
			result(result, "コメント削除");
		}
		return "redirect:/bookDetail/{bookId}";
	}
}
