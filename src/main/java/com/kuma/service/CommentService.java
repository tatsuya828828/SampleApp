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

	public boolean insert(CommentModel comment) {
		int rowNumber = commentRepository.insert(comment);
		return result(rowNumber);
	}

	public CommentModel selectOne(int commentId) {
		return commentRepository.selectOne(commentId);
	}

	public boolean update(CommentModel comment) {
		int rowNumber = commentRepository.update(comment);
		return result(rowNumber);
	}

	public boolean confirmComment(int userId, int bookId) {
		boolean result = commentRepository.confirmComment(userId, bookId);
		return result;
	}

	public List<CommentModel> selectMany(int bookId) {
		return commentRepository.selectMany(bookId);
	}

	public boolean delete(int commentId) {
		int rowNumber = commentRepository.delete(commentId);
		return result(rowNumber-1);
	}
}
