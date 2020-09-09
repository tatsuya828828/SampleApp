package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuma.model.BookModel;
import com.kuma.model.EvaluationModel;
import com.kuma.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;

	public boolean insert(BookModel book) {
		int rowNumber = bookRepository.insert(book);
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

	public boolean updateOne(BookModel book) {
		int rowNumber = bookRepository.updateOne(book);
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
}
