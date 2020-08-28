package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.BookModel;
import com.kuma.model.CommentModel;
import com.kuma.model.UserModel;

public interface CommentRepository {
	public int insert(CommentModel comment, UserModel user, BookModel book) throws DataAccessException;
	public List<CommentModel> selectMany(String bookId) throws DataAccessException;
	public int delete(String userId, String bookId) throws DataAccessException;
}
