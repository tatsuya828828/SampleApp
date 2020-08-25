package com.kuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuma.model.BookModel;
import com.kuma.model.UserModel;
import com.kuma.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	// insert用
	public boolean insert(UserModel user) {
		// insert実行
		int rowNumber = userRepository.insert(user);
		// 判定用変数
		boolean result = false;
		// 戻り値が0より大きければ、insert成功
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	// 1件取得用
	public UserModel selectOne(String userId) {
		// selectOne実行
		return userRepository.selectOne(userId);
	}

	// 紐づいている本を取得
	public List<BookModel> hasBook(String userId) {
		return userRepository.hasBook(userId);
	}

	// 全権取得用
	public List<UserModel> selectMany() {
		// selectMany実行
		return userRepository.selectMany();
	}

	// 1件更新用
	public boolean updateOne(UserModel user) {
		// 1件更新
		int rowNumber = userRepository.updateOne(user);
		// 判定用変数
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}

	// 1件削除用
	public boolean deleteOne(String userId) {
		// 1件削除
		int rowNumber = userRepository.deleteOne(userId);
		// 判定用変数
		boolean result = false;
		if(rowNumber>0) {
			result = true;
		}
		return result;
	}
}
