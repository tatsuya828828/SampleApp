package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.BookModel;

public interface BookRepository {
	public int insert (BookModel book) throws DataAccessException;
	public BookModel selectOne(String title) throws DataAccessException;
	public List<BookModel> selectMany() throws DataAccessException;
	public int updateOne(BookModel book) throws DataAccessException;
	public int deleteOne(String title) throws DataAccessException;
}
