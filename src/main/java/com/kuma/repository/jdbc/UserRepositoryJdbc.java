package com.kuma.repository.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kuma.model.BookModel;
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
		System.out.println("暗号化されたパスワードは: "+ password);
		// DBにデータを登録
		int userRowNumber = jdbc.update("INSERT INTO user(id, "+"password, "+"name)"
							+" VALUES(?,?,?)",
							user.getId(), password, user.getName());
		return userRowNumber;
	}

	@Override
	public UserModel selectOne(String id) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM user"+" WHERE id = ?", id);
		UserModel user = new UserModel();
		user.setId((String) map.get("id"));
		user.setPassword((String) map.get("password"));
		user.setName((String) map.get("name"));
		return user;
	}

	public List<BookModel> hasBook(String id) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book"+" WHERE user_id=?", id);
		UserModel user = selectOne(id);
		List<BookModel> books = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setAuthor((String) map.get("author"));
			book.setUser(user);
			books.add(book);
		}
		return books;
	}

	@Override
	public List<UserModel> selectMany() throws DataAccessException {
		// テーブルのデータを全権取得
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM user");
		// 結果返却用の変数
		List<UserModel> userList = new ArrayList<>();
		// 取得した結果をListに格納していく
		for(Map<String, Object> map: getList) {
			// Userインスタンスの生成
			UserModel user = new UserModel();
			// Userインスタンスに取得したデータをセット
			user.setId((String) map.get("id"));
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
		int userRowNumber = jdbc.update("UPDATE user "+"SET "+"password=?, "+"name=? "+"WHERE id=?",
							password, user.getName(), user.getId());
		return userRowNumber;
	}

	@Override
	public int deleteOne(String id) throws DataAccessException {
		// 1件削除
		int userRowNumber = jdbc.update("DELETE FROM user WHERE id=?", id);
		return userRowNumber;
	}
}
