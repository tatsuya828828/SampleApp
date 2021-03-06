package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.CommentModel;

public interface CommentRepository {
	public int insert(CommentModel comment) throws DataAccessException;
	public CommentModel selectOne(int commentId) throws DataAccessException;
	public int update(CommentModel comment) throws DataAccessException;
	public boolean confirmComment(int userId, int bookId) throws DataAccessException;
	public List<CommentModel> selectMany(int bookId) throws DataAccessException;
	public int delete(int commentId) throws DataAccessException;
}
