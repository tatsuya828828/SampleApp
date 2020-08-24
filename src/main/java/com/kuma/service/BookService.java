package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuma.model.BookModel;
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

	public BookModel selectOne(String title) {
		return bookRepository.selectOne(title);
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

	public boolean deleteOne(String title) {
		int rowNumber = bookRepository.deleteOne(title);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}
}
