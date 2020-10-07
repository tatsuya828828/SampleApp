package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kuma.model.ReplyModel;
import com.kuma.repository.ReplyRepository;
import com.kuma.service.CommentService;
import com.kuma.service.UserService;

@Repository("ReplyRepositoryJdbc")
public class ReplyRepositoryJdbc implements ReplyRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	UserService userService;
	@Autowired
	CommentService commentService;

	public ReplyModel getReply(Map<String, Object> map) throws DataAccessException {
		ReplyModel reply = new ReplyModel();
		reply.setId((int) map.get("id"));
		reply.setCreatedAt((Date) map.get("created_at"));
		reply.setUser(userService.selectOne((int)map.get("user_id")));
		reply.setComment(commentService.selectOne((int) map.get("comment_id")));
		reply.setReply((String) map.get("reply"));
		return reply;
	}

	@Override
	public int insert(ReplyModel reply) throws DataAccessException{
		int rowNumber = jdbc.update("INSERT INTO reply(created_at, user_id, comment_id, reply) "
				+ "VALUES(CURRENT_DATE,?,?,?)"
				, reply.getUser().getId(), reply.getComment().getId(), reply.getReply());
		return rowNumber;
	}

	@Override
	public List<ReplyModel> hasReply(int commentId) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList(
				"SELECT * FROM reply WHERE comment_id=?", commentId);
		List<ReplyModel> replyList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			ReplyModel reply = getReply(map);
			replyList.add(reply);
		}
		return replyList;
	}

	@Override
	public ReplyModel selectOne(int replyId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM reply WHERE id=?", replyId);
		ReplyModel reply = getReply(map);
		return reply;
	}

	@Override
	public int update(ReplyModel reply) throws DataAccessException {
		int rowNumber = jdbc.update("UPDATE reply SET reply=? WHERE id=?"
				, reply.getReply(), reply.getId());
		return rowNumber;
	}

	@Override
	public int delete(int replyId) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM reply WHERE id=?", replyId);
		return bookRowNumber;
	}
}
