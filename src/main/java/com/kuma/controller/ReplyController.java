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

import com.kuma.model.BookModel;
import com.kuma.model.CommentModel;
import com.kuma.model.ReplyForm;
import com.kuma.model.ReplyModel;
import com.kuma.model.UserModel;
import com.kuma.service.BookService;
import com.kuma.service.CommentService;
import com.kuma.service.ReplyService;
import com.kuma.service.UserService;

@Controller
public class ReplyController {
	@Autowired
	ReplyService replyService;
	@Autowired
	BookService bookService;
	@Autowired
	CommentService commentService;
	@Autowired
	UserService userService;

	@GetMapping("/bookDetail/{bookId}/postReply/{commentId}")
	public String getPostReply(@ModelAttribute ReplyForm form, Model model
			, @PathVariable("bookId") int bookId, @PathVariable("commentId") int commentId
			, HttpServletRequest httpServletRequest) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		model.addAttribute("commentList", "book/commentList :: comment_list");
		model.addAttribute("replyArea", "book/replyArea :: reply_area");
		if(String.valueOf(bookId).length() > 0) {
			BookModel book = bookService.selectOne(bookId);
			model.addAttribute("book", book);
			List<CommentModel> comments = commentService.selectMany(bookId);
			model.addAttribute("comments", comments);
			int count = bookService.evaluationCount(bookId);
			model.addAttribute("count", count);
			boolean result = commentService.confirmComment(user.getId(), book.getId());
			model.addAttribute("result", result);
			model.addAttribute("userId", user.getId());
			model.addAttribute("reply", true);
			model.addAttribute("commentId", commentId);
		}
	return "/header";
	}

	@PostMapping("/bookDetail/{bookId}/postReply/{commentId}")
	public String postReply(@ModelAttribute ReplyForm form, HttpServletRequest httpServletRequest
			, @PathVariable("commentId") int commentId, @PathVariable("bookId") int bookId) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		CommentModel comment = commentService.selectOne(commentId);
		ReplyModel reply = new ReplyModel();
		reply.setReply(form.getReply());
		reply.setUser(user);
		reply.setComment(comment);
		boolean result = replyService.insert(reply);
		if(result == true) {
			System.out.println("返信成功");
		} else {
			System.out.println("返信失敗");
		}
		return "redirect:/bookDetail/{bookId}";
	}
}
