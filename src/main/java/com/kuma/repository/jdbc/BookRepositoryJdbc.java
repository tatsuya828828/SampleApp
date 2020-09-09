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
import com.kuma.service.UserService;

@Repository("BookRepositoryJdbc")
public class BookRepositoryJdbc implements BookRepository {
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserService userService;

	@Override
	public int insert(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update("INSERT INTO book(title, "+"body, "+"author, "+"user_id) "+"VALUES(?,?,?,?)",
				book.getTitle(), book.getBody(), book.getAuthor(), book.getUser().getId());
		return bookRowNumber;
	}

	@Override
	public BookModel selectOne(int id) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM book"+" WHERE id=?", id);
		BookModel book = new BookModel();
		book.setId((int) map.get("id"));
		book.setTitle((String) map.get("title"));
		book.setBody((String) map.get("body"));
		book.setAuthor((String) map.get("author"));
		book.setUser(userService.selectOne((int)map.get("user_id")));
		book.setEvaluation((int) map.get("evaluation"));
		return book;
	}

	@Override
	public List<BookModel> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book");
		List<BookModel> bookList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setId((int) map.get("id"));
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setAuthor((String) map.get("author"));
			book.setUser(userService.selectOne((int)map.get("user_id")));
			book.setEvaluation((int) map.get("evaluation"));
			bookList.add(book);
		}
		return bookList;
	}

	@Override
	public int updateOne(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update("UPDATE book "+"SET "+"title=?, "+"body=?, "+"author=?, "+"user_id=? "+"WHERE id=?",
							book.getTitle(), book.getBody(), book.getAuthor(), book.getUser().getId(), book.getId());
		return bookRowNumber;
	}

	@Override
	public int deleteOne(int id) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM book WHERE id=?", id);
		return bookRowNumber;
	}

	@Override
	public int insertEvaluation(com.kuma.model.EvaluationModel evaluation) throws DataAccessException{
		int rowNumber = jdbc.update("INSERT INTO evaluation(evaluation, "+"user_id, "+"book_id) "+"VALUES(?,?,?)",
				evaluation.getEvaluation(), evaluation.getUser().getId(), evaluation.getBook().getId());
		return rowNumber;
	}

	@Override
	public int evaluationAvg(int bookId)throws DataAccessException {
		int num = jdbc.queryForObject("SELECT AVG(evaluation) FROM evaluation WHERE book_id="+ bookId, Integer.class);
		int rowNumber = jdbc.update("UPDATE book SET evaluation=? "+"WHERE id=?", num, bookId);
		return rowNumber;
	}
}
