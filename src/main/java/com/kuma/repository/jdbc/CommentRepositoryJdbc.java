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

@Repository("CommentRepositoryJdbc")
public class CommentRepositoryJdbc implements CommentRepository {
	@Autowired
	JdbcTemplate jdbc;

	@Override
	public int insert(CommentModel comment, UserModel user, BookModel book) throws DataAccessException {
		int rowNumber= jdbc.update("INSERT INTO comment_table(user, "+"book, "+"comment) "+" VALUES(?,?,?)",
				user, book, comment.getComment());
		return rowNumber;
	}

	@Override
	public List<CommentModel> selectMany(String bookId) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT FROM comment_table"+" WHERE book_id=?", bookId);
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
	public int delete(String userId, String bookId) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM comment WHERE user_id=?, book_id=?", userId, bookId);
		return bookRowNumber;
	}
}
