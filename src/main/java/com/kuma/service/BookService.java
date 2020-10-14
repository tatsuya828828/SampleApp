package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;

	// 共通処理をまとめたメソッド
	public boolean result(int rowNumber) {
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public boolean insert(BookModel book, MultipartFile multipartFile) {
		int rowNumber = bookRepository.insert(book, multipartFile);
		return result(rowNumber);
	}

	public BookModel selectOne(int id) {
		return bookRepository.selectOne(id);
	}

	public List<BookModel> selectMany(){
		return bookRepository.selectMany();
	}

	public boolean updateOne(BookModel book, MultipartFile multipartFile) {
		int rowNumber = bookRepository.updateOne(book, multipartFile);
		return result(rowNumber);
	}

	public boolean deleteOne(int id) {
		int rowNumber = bookRepository.deleteOne(id);
		System.out.println(rowNumber);
		return result(rowNumber-2);
	}

	public boolean updateEvaluation(int bookId) {
		int rowNumber = bookRepository.updateEvaluation(bookId);
		return result(rowNumber);
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
