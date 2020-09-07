package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.BookModel;
import com.kuma.model.EvaluationModel;

public interface BookRepository {
	public int insert (BookModel book) throws DataAccessException;
	public BookModel selectOne(String title) throws DataAccessException;
	public List<BookModel> selectMany() throws DataAccessException;
	public int updateOne(BookModel book) throws DataAccessException;
	public int deleteOne(String title) throws DataAccessException;
	public int insertEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int evaluationAvg(String bookTitle)throws DataAccessException;
}
