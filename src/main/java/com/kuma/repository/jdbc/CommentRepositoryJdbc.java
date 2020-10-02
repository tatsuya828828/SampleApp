package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
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

	public int insert(CommentModel comment) throws DataAccessException {
		int rowNumber= jdbc.update("INSERT INTO comment(created_at, user_id, book_id, comment, evaluation) "
				+"VALUES(CURRENT_DATE,?,?,?,?)",
				comment.getUser().getId(), comment.getBook().getId(), comment.getComment(), comment.getEvaluation());
		return rowNumber;
	}

	public int update(CommentModel comment) throws DataAccessException {
		int rowNumber= jdbc.update("UPDATE comment SET "
				+"comment=?, evaluation=? WHERE user_id=? AND book_id=?"
				, comment.getComment(), comment.getEvaluation()
				, comment.getUser().getId(), comment.getBook().getId());
		return rowNumber;
	}

	@Override
	public int selectComment(CommentModel comment) throws DataAccessException {
		int rowNumber = 0;
		if(comment.getId() == 0) {
			rowNumber = insert(comment);
		} else {
			rowNumber = update(comment);
		}
		return rowNumber;
	}

	@Override
	public List<CommentModel> selectMany(int bookId) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM comment"+" WHERE book_id=?", bookId);
		List<CommentModel> commentList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			CommentModel comment = new CommentModel();
			comment.setCreatedAt((Date) map.get("created_at"));
			comment.setUser(userService.selectOne((int) map.get("user_id")));
			comment.setBook(bookService.selectOne((int) map.get("book_id")));
			comment.setComment((String) map.get("comment"));
			comment.setEvaluation((int) map.get("evaluation"));
			commentList.add(comment);
		}
		return commentList;
	}

	@Override
	public int delete(int userId, int bookId) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM comment WHERE user_id=? AND book_id=?", userId, bookId);
		return bookRowNumber;
	}
}
