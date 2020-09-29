package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.model.EvaluationModel;
import com.kuma.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;

	public boolean insert(BookModel book, MultipartFile multipartFile) {
		int rowNumber = bookRepository.insert(book, multipartFile);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public BookModel selectOne(int id) {
		return bookRepository.selectOne(id);
	}

	public List<BookModel> selectMany(){
		return bookRepository.selectMany();
	}

	public boolean updateOne(BookModel book, MultipartFile multipartFile) {
		int rowNumber = bookRepository.updateOne(book, multipartFile);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public boolean deleteOne(int id) {
		int rowNumber = bookRepository.deleteOne(id);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public void selectEvaluation(EvaluationModel evaluation) {
		bookRepository.selectEvaluation(evaluation);
	}

	public boolean insertEvaluation(EvaluationModel evaluation) {
		int rowNumber = bookRepository.insertEvaluation(evaluation);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public boolean evaluationAvg(int bookId) {
		int rowNumber = bookRepository.evaluationAvg(bookId);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public int evaluationCount(int bookId) {
		return bookRepository.evaluationCount(bookId);
	}

	public List<BookModel> searchAuthor(String author) {
		return bookRepository.searchAuthor(author);
	}

	public List<BookModel> selectGenre(String genre) {
		return bookRepository.selectGenre(genre);
	}

	public List<String> selectGenres() {
		return bookRepository.selectGenres();
	}

	public int count(String column, String word) {
		return bookRepository.count(column, word);
	}

	public List<BookModel> searchBook(String word) {
		return bookRepository.searchBook(word);
	}
}
