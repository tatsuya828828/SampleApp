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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	// 成否を知らせるメソッド
	public void result(boolean result, String word) {
		if(result == true) {
			System.out.println(word+"成功");
		} else {
			System.out.println(word+"失敗");
		}
	}

	@GetMapping("/bookDetail/{bookId}/postReply/{commentId}")
	public String getPostReply(@ModelAttribute ReplyForm form, Model model
			, @PathVariable("bookId") int bookId, @PathVariable("commentId") int commentId
			, HttpServletRequest httpServletRequest) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		model.addAttribute("commentList", "book/commentList :: comment_list");
		model.addAttribute("replyArea", "book/replyArea :: reply_area");
		model.addAttribute("replyList", "book/replyList :: reply_list");
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
			, @PathVariable("commentId") int commentId, @PathVariable("bookId") int bookId
			, RedirectAttributes redirectAttributes) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		CommentModel comment = commentService.selectOne(commentId);
		ReplyModel reply = new ReplyModel();
		reply.setReply(form.getReply());
		reply.setUser(user);
		reply.setComment(comment);
		boolean result = replyService.insert(reply);
		result(result, "返信");
		redirectAttributes.addFlashAttribute("commentId", commentId);
		return "redirect:/bookDetail/{bookId}#bottom{commentId}";
	}

	@GetMapping("/bookDetail/{bookId}/editReply/{replyId}")
	public String getEditReply(@ModelAttribute ReplyForm form, Model model
			, HttpServletRequest httpServletRequest, @PathVariable("replyId") int replyId
			, @PathVariable("bookId") int bookId) {
		if(String.valueOf(replyId).length()>0) {
			ReplyModel reply = replyService.selectOne(replyId);
			CommentModel comment = commentService.selectOne(reply.getComment().getId());
			BookModel book = bookService.selectOne(bookId);
			UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
			if(reply.getUser().getId() == user.getId()) {
				form.setComment(comment);
				form.setUser(reply.getUser());
				form.setReply(reply.getReply());
				model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
				model.addAttribute("commentList", "book/commentList :: comment_list");
				model.addAttribute("editReply", "book/editReply :: edit_reply");
				model.addAttribute("replyList", "book/replyList :: reply_list");
				model.addAttribute("editReplyId", replyId);
				model.addAttribute("commentId", reply.getComment().getId());
			} else {
				return "redirect:/bookDetail/{id}";
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

	@PostMapping("/bookDetail/{bookId}/editReply/{replyId}")
	public String postEditComment(@ModelAttribute ReplyForm form, Model model
			, @PathVariable("replyId") int replyId, @PathVariable("bookId") int bookId
			, RedirectAttributes redirectAttributes) {
		int commentId = replyService.selectOne(replyId).getComment().getId();
		ReplyModel reply = new ReplyModel();
		reply.setId(replyId);
		reply.setReply(form.getReply());
		boolean result = replyService.update(reply);
		result(result, "更新");
		redirectAttributes.addFlashAttribute("commentId", commentId);
		return "redirect:/bookDetail/{bookId}#reply{replyId}";
	}

	@PostMapping(value="/bookDetail/{bookId}/editReply/{replyId}", params="delete")
	public String deleteReply(@PathVariable("bookId") int bookId, @PathVariable("replyId") int replyId) {
		if(String.valueOf(bookId).length()>0 && String.valueOf(replyId).length()>0) {
			boolean result = replyService.delete(replyId);
			result(result, "リプライ削除");
		}
		return "redirect:/bookDetail/{bookId}";
	}
}
