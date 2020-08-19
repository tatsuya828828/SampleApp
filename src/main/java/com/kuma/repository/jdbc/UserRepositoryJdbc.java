package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kuma.model.UserModel;
import com.kuma.repository.UserRepository;

@Repository("UserRepositoryJdbc")
public class UserRepositoryJdbc implements UserRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public int insert(UserModel user) throws DataAccessException {
		// パスワードを暗号化
		String password = passwordEncoder.encode(user.getPassword());
		// DBにデータを登録
		int userRowNumber = jdbc.update("INSERT INTO user_table(user_id, "+"password, "+"name)"
							+" VALUES(?,?,?)",
							user.getUserId(), password, user.getName());
		return userRowNumber;
	}

	@Override
	public UserModel selectOne(String userId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM user_table"+" WHERE user_id = ?", userId);
		UserModel user = new UserModel();
		user.setUserId((String) map.get("user_id"));
		user.setPassword((String) map.get("password"));
		user.setName((String) map.get("hero_name"));
		return user;
	}

	@Override
	public List<UserModel> selectMany() throws DataAccessException {
		// テーブルのデータを全権取得
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM user_table");
		// 結果返却用の変数
		List<UserModel> userList = new ArrayList<>();
		// 取得した結果をListに格納していく
		for(Map<String, Object> map: getList) {
			// Userインスタンスの生成
			UserModel user = new UserModel();
			// Userインスタンスに取得したデータをセット
			user.setUserId((String) map.get("user_id"));
			user.setPassword((String) map.get("password"));
			user.setName((String) map.get("name"));
			// 結果返却用のListに追加
			userList.add(user);
		}
		return userList;
	}

	@Override
	public int updateOne(UserModel user) throws DataAccessException {
		// パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());
		int userRowNumber = jdbc.update("UPDATE user_table "+"SET "+"password=?, "+"name=? "+"WHERE user_id=?",
							password, user.getName(), user.getUserId());
		return userRowNumber;
	}

	@Override
	public int deleteOne(String userId) throws DataAccessException {
		// 1件削除
		int userRowNumber = jdbc.update("DELETE FROM user_table WHERE user_id=?", userId);
		return userRowNumber;
	}
}
