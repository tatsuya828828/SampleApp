package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kuma.model.ReplyModel;
import com.kuma.repository.ReplyRepository;

public class ReplyService {
	@Autowired
	ReplyRepository replyRepository;
	// 共通処理をまとめたメソッド
	public boolean result(int rowNumber) {
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	public boolean insert(ReplyModel reply) {
		int rowNumber = replyRepository.insert(reply);
		return result(rowNumber);
	}

	public List<ReplyModel> hasReply(int commentId) {
		return replyRepository.hasReply(commentId);
	}

	public ReplyModel selectOne(int replyId) {
		return replyRepository.selectOne(replyId);
	}

	public boolean update(ReplyModel reply) {
		int rowNumber = replyRepository.update(reply);
		return result(rowNumber);
	}

	public boolean delete(int replyId) {
		int rowNumber = replyRepository.delete(replyId);
		return result(rowNumber);
	}
}
