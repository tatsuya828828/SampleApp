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

	// 共通処理をまとめたメソッド
	public boolean result(int rowNumber) {
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public boolean selectComment(CommentModel comment) {
		int rowNumber = commentRepository.selectComment(comment);
		return result(rowNumber);
	}

	public List<CommentModel> selectMany(int bookId) {
		return commentRepository.selectMany(bookId);
	}

	public boolean delete(int userId, int bookId) {
		int rowNumber = commentRepository.delete(userId, bookId);
		return result(rowNumber);
	}
}
