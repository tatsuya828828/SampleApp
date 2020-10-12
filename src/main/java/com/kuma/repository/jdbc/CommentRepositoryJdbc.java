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
import com.kuma.model.ReplyModel;
import com.kuma.repository.CommentRepository;
import com.kuma.service.BookService;
import com.kuma.service.ReplyService;
import com.kuma.service.UserService;

@Repository("CommentRepositoryJdbc")
public class CommentRepositoryJdbc implements CommentRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	BookService bookService;
	@Autowired
	UserService userService;
	@Autowired
	ReplyService replyService;

	public CommentModel getComment(Map<String, Object> map) throws DataAccessException {
		CommentModel comment = new CommentModel();
		comment.setId((int) map.get("id"));
		comment.setCreatedAt((Date) map.get("created_at"));
		comment.setUser(userService.selectOne((int) map.get("user_id")));
		comment.setBook(bookService.selectOne((int) map.get("book_id")));
		comment.setComment((String) map.get("comment"));
		comment.setEvaluation((int) map.get("evaluation"));
		return comment;
	}

	@Override
	public int insert(CommentModel comment) throws DataAccessException {
		int rowNumber= jdbc.update("INSERT INTO comment(created_at, user_id, book_id, comment, evaluation) "
				+"VALUES(CURRENT_DATE,?,?,?,?)",
				comment.getUser().getId(), comment.getBook().getId(), comment.getComment(), comment.getEvaluation());
		return rowNumber;
	}

	@Override
	public CommentModel selectOne(int commentId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM comment WHERE id=?", commentId);
		CommentModel comment = getComment(map);
		return comment;
	}

	@Override
	public int update(CommentModel comment) throws DataAccessException {
		int rowNumber= jdbc.update("UPDATE comment SET "
				+"comment=?, evaluation=? WHERE id=?"
				, comment.getComment(), comment.getEvaluation()
				, comment.getId());
		return rowNumber;
	}

	@Override
	public boolean confirmComment(int userId, int bookId) throws DataAccessException {
		int num = jdbc.queryForObject("SELECT COUNT(*) FROM comment"
				+ " WHERE user_id="+ userId +" AND book_id="+ bookId
									, Integer.class);
		boolean result = false;
		if(num >= 1) {
			result = true;
		}
		return result;
	}

	@Override
	public List<CommentModel> selectMany(int bookId) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM comment"+" WHERE book_id=?", bookId);
		List<CommentModel> commentList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			CommentModel comment = getComment(map);
			List<ReplyModel> reply = replyService.hasReply(comment.getId());
			comment.setReply(reply);
			int num = replyService.countReply(comment.getId());
			comment.setCountReply(num);
			commentList.add(comment);
		}
		return commentList;
	}

	@Override
	public int delete(int commentId) throws DataAccessException {
		int rowNumber = jdbc.update("DELETE FROM reply WHERE comment_id=?", commentId);
		rowNumber += jdbc.update("DELETE FROM comment WHERE id=?", commentId);
		return rowNumber;
	}
}
