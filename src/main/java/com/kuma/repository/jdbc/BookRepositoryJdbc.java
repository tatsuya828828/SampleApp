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
import com.kuma.repository.BookRepository;

@Repository("BookRepositoryJdbc")
public class BookRepositoryJdbc implements BookRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public int insert(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update("INSERT INTO book_table(title, "+"body, "+"user_id) "+"VALUES(?,?,?)",
				book.getTitle(), book.getBody(), book.getUserId());
		return bookRowNumber;
	}

	@Override
	public BookModel selectOne(String title) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM book_table"+" WHERE title=?", title);
		BookModel book = new BookModel();
		book.setTitle((String) map.get("title"));
		book.setBody((String) map.get("body"));
		book.setUserId((String) map.get("user_id"));
		return book;
	}

	@Override
	public List<BookModel> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book_table");
		List<BookModel> bookList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setUserId((String) map.get("user_id"));
			bookList.add(book);
		}
		return bookList;
	}

	@Override
	public int updateOne(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update("UPDATE book_table "+"SET "+"body=?, "+"userId=? "+"WHERE title=?",
							book.getBody(),book.getUserId(),book.getTitle());
		return bookRowNumber;
	}

	@Override
	public int deleteOne(String title) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM book_table WHERE title=?", title);
		return bookRowNumber;
	}
}
