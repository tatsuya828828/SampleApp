package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.BookModel;
import com.kuma.model.UserModel;

public interface UserRepository {
	public int insert(UserModel user) throws DataAccessException;
	public UserModel selectOne(String userId) throws DataAccessException;
	public List<BookModel> hasBook(String userId) throws DataAccessException;
	public List<UserModel> selectMany() throws DataAccessException;
	public int updateOne(UserModel user) throws DataAccessException;
	public int deleteOne(String userId) throws DataAccessException;
}
