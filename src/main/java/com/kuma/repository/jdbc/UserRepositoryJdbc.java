package com.kuma.repository.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.kuma.model.BookModel;
import com.kuma.model.UserModel;
import com.kuma.repository.UserRepository;

@Repository("UserRepositoryJdbc")
public class UserRepositoryJdbc implements UserRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	PasswordEncoder passwordEncoder;

	public int insertAndGetId(UserModel user) throws DataAccessException {
		String password = passwordEncoder.encode(user.getPassword());
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbc.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement("INSERT INTO user(created_at, last_login, self_id, password, name"
	          		+ " VALUES(CURRENT_DATE, LOCALTIME,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, user.getSelfId());
	          ps.setString(2, password);
	          ps.setString(3, user.getName());
	          return ps;
	    }, keyHolder);
	    return (int) keyHolder.getKeys().get("id");
	}

	@Override
	public int insert(UserModel user, MultipartFile multipartFile) throws DataAccessException {
		int id = insertAndGetId(user);
		String imageName = ImageUpload.postImage(multipartFile, String.valueOf(id));
		int userRowNumber = jdbc.update("UPDATE book SET image=?"+" WHERE id=?", imageName, id);
		return userRowNumber;
	}

	@Override
	public UserModel selectOne(int id) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM user"+" WHERE id = ?", id);
		UserModel user = new UserModel();
		user.setId((int) map.get("id"));
		user.setCreatedAt((Date) map.get("created_at"));
		user.setLastLogin((Date) map.get("last_login"));
		user.setSelfId((String) map.get("self_id"));
		user.setPassword((String) map.get("password"));
		user.setName((String) map.get("name"));
		return user;
	}

	@Override
	public UserModel currentUser(String selfId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM user "+"WHERE self_id=?", selfId);
		UserModel user = new UserModel();
		user.setId((int) map.get("id"));
		user.setCreatedAt((Date) map.get("created_at"));
		user.setLastLogin((Date) map.get("last_login"));
		user.setSelfId((String) map.get("self_id"));
		user.setPassword((String) map.get("password"));
		user.setName((String) map.get("name"));
		return user;
	}

	public List<BookModel> hasBook(int id) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book"+" WHERE user_id=?", id);
		UserModel user = selectOne(id);
		List<BookModel> books = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setId((int) map.get("id"));
			book.setCreatedAt((Date) map.get("created_at"));
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setAuthor((String) map.get("author"));
			book.setGenre((String) map.get("genre"));
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
			user.setId((int) map.get("id"));
			user.setCreatedAt((Date) map.get("created_at"));
			user.setLastLogin((Date) map.get("last_login"));
			user.setSelfId((String) map.get("self_id"));
			user.setPassword((String) map.get("password"));
			user.setName((String) map.get("name"));
			// 結果返却用のListに追加
			userList.add(user);
		}
		return userList;
	}

	@Override
	public int updateOne(UserModel user, MultipartFile multipartFile) throws DataAccessException {
		// パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());
		String id = String.valueOf(user.getId());
		String imageName = null;
		if(!multipartFile.isEmpty()) {
			imageName = ImageUpload.postImage(multipartFile, id);
		} else {
			imageName = user.getImage();
		}
		int userRowNumber = jdbc.update("UPDATE user SET self_id=?, password=?,"
				+ " name=?, image=? WHERE id=?",
				user.getSelfId(), password, user.getName(), imageName, user.getId());
		return userRowNumber;
	}

	@Override
	public int deleteOne(int id) throws DataAccessException {
		// 1件削除
		int userRowNumber = jdbc.update("DELETE FROM user WHERE id=?", id);
		return userRowNumber;
	}

	@Override
	public int saveLoginTime(String selfId) throws DataAccessException {
		int userRowNumber = jdbc.update("UPDATE user SET last_login = LOCALTIME WHERE self_id=?", selfId);
		return userRowNumber;
	}
}
