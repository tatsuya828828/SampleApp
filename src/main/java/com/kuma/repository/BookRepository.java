package com.kuma.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.model.EvaluationModel;

public interface BookRepository {
	public int insert (BookModel book, MultipartFile multipartFile) throws DataAccessException;
	public BookModel selectOne(int id) throws DataAccessException;
	public List<BookModel> selectMany() throws DataAccessException;
	public int updateOne(BookModel book, MultipartFile multipartFile) throws DataAccessException;
	public int deleteOne(int id) throws DataAccessException;
	public void selectEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int insertEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int updateEvaluation(EvaluationModel evaluation) throws DataAccessException;
	public int evaluationAvg(int bookId)throws DataAccessException;
	public int evaluationCount(int bookId) throws DataAccessException;
	public List<BookModel> searchAuthor(String author) throws DataAccessException;
	public List<BookModel> selectGenre(String genre) throws DataAccessException;
	public List<String> selectGenres() throws DataAccessException;
	public int count(String column, String word) throws DataAccessException;
	public List<BookModel> searchBook(String word) throws DataAccessException;
}
