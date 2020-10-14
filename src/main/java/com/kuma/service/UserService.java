package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.model.UserModel;
import com.kuma.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	// 共通処理をまとめたメソッド
	public boolean result(int rowNumber) {
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}
	// insert用
	public boolean insert(UserModel user, MultipartFile multipartFile) {
		int rowNumber = userRepository.insert(user, multipartFile);
		return result(rowNumber);
	}

	// 1件取得用
	public UserModel selectOne(int id) {
		// selectOne実行
		return userRepository.selectOne(id);
	}

	public UserModel currentUser(String selfId) {
		return userRepository.currentUser(selfId);
	}

	// 紐づいている本を取得
	public List<BookModel> hasBook(int id) {
		return userRepository.hasBook(id);
	}

	// 全権取得用
	public List<UserModel> selectMany() {
		// selectMany実行
		return userRepository.selectMany();
	}

	// 1件更新用
	public boolean updateOne(UserModel user, MultipartFile multipartFile) {
		int rowNumber = userRepository.updateOne(user, multipartFile);
		return result(rowNumber);
	}

	// 1件削除用
	public boolean deleteOne(int id) {
		int rowNumber = userRepository.deleteOne(id);
		return result(rowNumber-3);
	}

	public boolean saveLoginTime(String selfId) {
		int rowNumber = userRepository.saveLoginTime(selfId);
		return result(rowNumber);
	}
}
