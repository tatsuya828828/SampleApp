package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.CommentModel;

public interface CommentRepository {
	public int selectAction(CommentModel comment) throws DataAccessException;
	public boolean selectOne(int userId, int bookId) throws DataAccessException;
	public List<CommentModel> selectMany(int bookId) throws DataAccessException;
	public int delete(int userId, int bookId) throws DataAccessException;
}
