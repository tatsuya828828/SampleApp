package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kuma.model.CommentModel;
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
		int rowNumber= jdbc.update("INSERT INTO comment(user_id, "+"book_id, "+"comment) "+" VALUES(?,?,?)",
				comment.getUser().getId(), comment.getBook().getTitle(), comment.getComment());
		return rowNumber;
	}

	@Override
	public List<CommentModel> selectMany(String bookTitle) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM comment"+" WHERE book_id=?", bookTitle);
		List<CommentModel> commentList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			CommentModel comment = new CommentModel();
			comment.setUser(userService.selectOne((String) map.get("user_id")));
			comment.setBook(bookService.selectOne((String) map.get("book_id")));
			comment.setComment((String) map.get("comment"));
			commentList.add(comment);
		}
		return commentList;
	}

	@Override
	public int delete(String userId, String bookTitle) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM comment WHERE user_id=? AND book_id=?", userId, bookTitle);
		return bookRowNumber;
	}
}
