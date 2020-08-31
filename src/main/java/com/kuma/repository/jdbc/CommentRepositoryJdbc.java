package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kuma.model.BookModel;
import com.kuma.model.CommentModel;
import com.kuma.model.UserModel;
import com.kuma.repository.CommentRepository;
import com.kuma.service.BookService;
import com.kuma.service.UserService;

@Repository("CommentRepositoryJdbc")
public class CommentRepositoryJdbc implements CommentRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	BookService bookService;
	@Autowired
	UserService userService;

	@Override
	public int insert(CommentModel comment) throws DataAccessException {
		int rowNumber= jdbc.update("INSERT INTO comment_table(user, "+"book, "+"comment) "+" VALUES(?,?,?)",
				comment.getUser(), comment.getBook(), comment.getComment());
		return rowNumber;
	}

	@Override
	public List<CommentModel> selectMany(String bookTitle) throws DataAccessException {
		BookModel book = bookService.selectOne(bookTitle);
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT FROM comment_table"+" WHERE book=?", book);
		List<CommentModel> commentList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			CommentModel comment = new CommentModel();
			comment.setUser((UserModel) map.get("user"));
			comment.setBook((BookModel) map.get("book"));
			comment.setComment((String) map.get("comment"));
			commentList.add(comment);
		}
		return commentList;
	}

	@Override
	public int delete(String userId, String bookTitle) throws DataAccessException {
		UserModel user = userService.selectOne(userId);
		BookModel book = bookService.selectOne(bookTitle);
		int bookRowNumber = jdbc.update("DELETE FROM comment WHERE user=?, book=?", user, book);
		return bookRowNumber;
	}
}
