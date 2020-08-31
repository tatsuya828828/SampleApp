package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuma.model.CommentModel;
import com.kuma.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;

	public boolean insert(CommentModel comment) {
		int rowNumber = commentRepository.insert(comment);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public List<CommentModel> selectMany(String bookTitle) {
		return commentRepository.selectMany(bookTitle);
	}

	public boolean delete(String userId, String bookTitle) {
		int rowNumber = commentRepository.delete(userId, bookTitle);
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}
}
