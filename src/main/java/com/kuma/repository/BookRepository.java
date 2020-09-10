package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.kuma.model.BookModel;
import com.kuma.model.EvaluationModel;

public interface BookRepository {
	public int insert (BookModel book) throws DataAccessException;
	public BookModel selectOne(int id) throws DataAccessException;
	public List<BookModel> selectMany() throws DataAccessException;
	public int updateOne(BookModel book) throws DataAccessException;
	public int deleteOne(int id) throws DataAccessException;
	public void selectEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int insertEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int updateEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int evaluationAvg(int bookId)throws DataAccessException;
	public int evaluationCount(int bookId) throws DataAccessException;
}
