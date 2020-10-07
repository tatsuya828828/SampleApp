package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.ReplyModel;

public interface ReplyRepository {
	public int insert(ReplyModel reply) throws DataAccessException;
	public List<ReplyModel> hasReply(int commentId) throws DataAccessException;
	public ReplyModel selectOne(int replyId) throws DataAccessException;
	public int countReply(int commentId) throws DataAccessException;
	public int update(ReplyModel reply) throws DataAccessException;
	public int delete(int replyId) throws DataAccessException;
}
