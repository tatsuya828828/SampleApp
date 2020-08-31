package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.CommentModel;

public interface CommentRepository {
	public int insert(CommentModel comment) throws DataAccessException;
	public List<CommentModel> selectMany(String bookTitle) throws DataAccessException;
	public int delete(String userId, String bookTitle) throws DataAccessException;
}
