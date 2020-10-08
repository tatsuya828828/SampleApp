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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
			, BindingResult bindingResult, Model model, HttpServletRequest httpServletRequest
			, @RequestParam("image") MultipartFile multipartFile) {
		if(bindingResult.hasErrors()) {
			return getBookNew(form, model);
		}
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		BookModel book = new BookModel();
		book.setTitle(form.getTitle());
		book.setBody(form.getBody());
		book.setAuthor(form.getAuthor());
		book.setGenre(form.getGenre());
		book.setUser(user);
		boolean result = bookService.insert(book, multipartFile);
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
		List<String> genres = bookService.selectGenres();
		model.addAttribute("genres", genres);
		return "/header";
	}

	@GetMapping("/bookDetail/{id}")
	public String getBookDetail(@ModelAttribute CommentForm form, Model model, @PathVariable("id") int id
			, HttpServletRequest httpServletRequest) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		model.addAttribute("contents", "book/bookDetail :: bookDetail_contents");
		model.addAttribute("commentList", "book/commentList :: comment_list");
		if(String.valueOf(id).length() > 0) {
			BookModel book = bookService.selectOne(id);
			model.addAttribute("book", book);
			List<CommentModel> comments = commentService.selectMany(id);
			model.addAttribute("comments", comments);
			int count = bookService.evaluationCount(id);
			model.addAttribute("count", count);
			boolean result = commentService.confirmComment(user.getId(), book.getId());
			model.addAttribute("result", result);
			model.addAttribute("userId", user.getId());
		}
		return "/header";
	}

	@GetMapping("/bookEdit/{id}")
	public String getBookEdit(@ModelAttribute BookForm form, Model model
			, @PathVariable("id") int id, HttpServletRequest httpServletRequest) {
		UserModel user = userService.currentUser(httpServletRequest.getRemoteUser());
		BookModel book = bookService.selectOne(id);
		if(String.valueOf(id).length() > 0) {
			if(book.getUser().equals(user)) {
				form.setId(id);
				form.setTitle(book.getTitle());
				form.setBody(book.getBody());
				form.setAuthor(book.getAuthor());
				form.setGenre(book.getGenre());
				form.setUser(book.getUser());
				model.addAttribute("contents", "book/bookEdit :: bookEdit_contents");
				model.addAttribute("bookForm", form);
				model.addAttribute("image", book.getImage());
				return "/header";
			}
		}
		return "redirect:/bookDetail/{id}";
	}

	@PostMapping(value="/bookEdit", params="update")
	public String postBookEdit(@ModelAttribute BookForm form, Model model
			, @RequestParam("image") MultipartFile multipartFile) {
		BookModel book = new BookModel();
		book.setId(form.getId());
		book.setTitle(form.getTitle());
		book.setBody(form.getBody());
		book.setAuthor(form.getAuthor());
		book.setGenre(form.getGenre());
		book.setUser(form.getUser());
		if(multipartFile.isEmpty()) {
			String image = bookService.selectOne(form.getId()).getImage();
			book.setImage(image);
		}
		try {
			boolean result = bookService.updateOne(book, multipartFile);
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
		boolean result = bookService.deleteOne(form.getId());
		if(result == true) {
			model.addAttribute("result", "削除成功");
		} else {
			model.addAttribute("result", "削除失敗");
		}
		return getBookList(model);
	}

	@GetMapping("/author/{author}")
	public String getAuthor(Model model, @PathVariable("author") String author) {
		model.addAttribute("contents", "search :: search_contents");
		List<BookModel> bookList = bookService.searchAuthor(author);
		model.addAttribute("bookList", bookList);
		model.addAttribute("word", author);
		model.addAttribute("word2", "の作品一覧");
		int count = bookService.count("author", author);
		model.addAttribute("count", count);
		return "/header";
	}

	@GetMapping("/genre/{genre}")
	public String getGenre(Model model, @PathVariable("genre") String genre) {
		model.addAttribute("contents", "search :: search_contents");
		List<BookModel> bookList = bookService.selectGenre(genre);
		model.addAttribute("bookList", bookList);
		model.addAttribute("word", genre);
		model.addAttribute("word2", "一覧");
		int count = bookService.count("genre", genre);
		model.addAttribute("count", count);
		return "/header";
	}

	@GetMapping("/search/{word}")
	public String getSearch(Model model, @RequestParam("word") String word) {
		model.addAttribute("contents", "search :: search_contents");
		model.addAttribute("word", word);
		model.addAttribute("word2", "の検索結果");
		int count = bookService.count("", word);
		List<BookModel> bookList = bookService.searchBook(word);
		model.addAttribute("bookList", bookList);
		model.addAttribute("count", count);
		return "/header";
	}
}
