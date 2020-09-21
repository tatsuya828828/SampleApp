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
import com.kuma.model.EvaluationModel;
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

	public List<BookModel> getList(List<Map<String, Object>> getList) {
		List<BookModel> bookList = new ArrayList<>();
		for(Map<String, Object> map: getList) {
			BookModel book = new BookModel();
			book.setId((int) map.get("id"));
			book.setTitle((String) map.get("title"));
			book.setBody((String) map.get("body"));
			book.setAuthor((String) map.get("author"));
			book.setGenre((String) map.get("genre"));
			book.setUser(userService.selectOne((int) map.get("user_id")));
			book.setEvaluation((int) map.get("evaluation"));
			bookList.add(book);
		}
		return bookList;
	}

	@Override
	public int insert(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update(
				"INSERT INTO book(title, "+"body, "+"author, "+"genre,"+"user_id, "+"evaluation) "
				+"VALUES(?,?,?,?,?,?)",
				book.getTitle(), book.getBody(), book.getAuthor(), book.getGenre(), book.getUser().getId(), 0);
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
		book.setGenre((String) map.get("genre"));
		book.setUser(userService.selectOne((int)map.get("user_id")));
		book.setEvaluation((int) map.get("evaluation"));
		return book;
	}

	@Override
	public List<BookModel> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book");
		return getList(getList);
	}

	@Override
	public int updateOne(BookModel book) throws DataAccessException {
		int bookRowNumber = jdbc.update("UPDATE book "+"SET "+"title=?, "+"body=?, "+"author=?, "+"genre=?, "+"user_id=? "+"WHERE id=?",
							book.getTitle(), book.getBody(), book.getAuthor(), book.getGenre(), book.getUser().getId(), book.getId());
		return bookRowNumber;
	}

	@Override
	public int deleteOne(int id) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM book WHERE id=?", id);
		return bookRowNumber;
	}

	@Override
	public void selectEvaluation(EvaluationModel evaluation) throws DataAccessException {
		int num = jdbc.queryForObject("SELECT COUNT(user_id) FROM evaluation"
	+" WHERE user_id="+ evaluation.getUser().getId() +"AND book_id="+ evaluation.getBook().getId(), Integer.class);
		System.out.println("1");
		System.out.println(num);
		if(num == 0) {
			System.out.println("評価登録中");
			insertEvaluation(evaluation);
			System.out.println("評価登録成功");
		} else {
			System.out.println("評価更新中");
			updateEvaluation(evaluation);
			System.out.println("評価更新成功");
		}
	}

	@Override
	public int insertEvaluation(com.kuma.model.EvaluationModel evaluation) throws DataAccessException {
		int rowNumber = jdbc.update("INSERT INTO evaluation(evaluation, "+"user_id, "+"book_id) "+"VALUES(?,?,?)",
				evaluation.getEvaluation(), evaluation.getUser().getId(), evaluation.getBook().getId());
		return rowNumber;
	}

	@Override
	public int updateEvaluation(EvaluationModel evaluation) throws DataAccessException {
		int rowNumber = jdbc.update("UPDATE evaluation "+"SET "+"evaluation=? "+"WHERE user_id=?"+"AND book_id=?"
					, evaluation.getEvaluation(), evaluation.getUser().getId(), evaluation.getBook().getId());
		return rowNumber;
	}

	@Override
	public int evaluationAvg(int bookId) throws DataAccessException {
		int num = jdbc.queryForObject("SELECT AVG(evaluation) FROM evaluation WHERE book_id="+ bookId, Integer.class);
		int rowNumber = jdbc.update("UPDATE book SET evaluation=? "+"WHERE id=?", num, bookId);
		return rowNumber;
	}

	@Override
	public int evaluationCount(int bookId) throws DataAccessException {
		int num = jdbc.queryForObject("SELECT COUNT(evaluation) FROM evaluation WHERE book_id="+ bookId, Integer.class);
		return num;
	}

	@Override
	public List<BookModel> searchAuthor(String author) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book"+" WHERE author=?", author);
		return getList(getList);
	}


	@Override
	public List<BookModel> selectGenre(String genre) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book "+"WHERE genre=?", genre);
		return getList(getList);
	}

	@Override
	public List<String> selectGenres() throws DataAccessException {
		List<Map<String, Object>> getGenre = jdbc.queryForList("SELECT DISTINCT genre FROM book");
		List<String> genreList = new ArrayList<>();
		for(Map<String, Object> map: getGenre) {
			genreList.add((String) map.get("genre"));
		}
		return genreList;
	}

	@Override
	public int count(String column, String word) throws DataAccessException {
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM book "
				+ "WHERE "+ column +" LIKE "+"'%"+ word +"%'", Integer.class);
		return count;
	}

	@Override
	public List<BookModel> searchBook(String word) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book "
				+ "WHERE title LIKE ?", "%"+ word +"%");
		return getList(getList);
	}
}
