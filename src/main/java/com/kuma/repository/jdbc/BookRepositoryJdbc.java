package com.kuma.repository.jdbc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
			book.setImage((String) map.get("image"));
			book.setCreatedAt((Date) map.get("created_at"));
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

	public int insertAndGetId(BookModel book) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbc.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement("INSERT INTO book(created_at, title, body, author, "
	          		+ "genre, user_id, evaluation)"
				+"VALUES(CURRENT_DATE,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, book.getTitle());
	          ps.setString(2,book.getBody());
	          ps.setString(3, book.getAuthor());
	          ps.setString(4, book.getGenre());
	          ps.setInt(5, book.getUser().getId());
	          ps.setInt(6, 0);
	          return ps;
	    }, keyHolder);
	    return (int) keyHolder.getKeys().get("id");
	}

	public static String postImage(MultipartFile multipartFile, String id) {
		if(!multipartFile.isEmpty()) {
			try {
				String uploadPath = "src/main/resources/static/images/";
				byte[] bytes = multipartFile.getBytes();
				File file = new File(uploadPath+ id+"_"+ multipartFile.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(file));
				stream.write(bytes);
				stream.close();
				System.out.println("画像登録成功");
			} catch(Exception e) {
				System.out.println(e);
			}
			return "/images/"+ id +"_"+ multipartFile.getOriginalFilename();
		}
		return "/images/NOIMAGE.png";
	}

	@Override
	public int insert(BookModel book, MultipartFile multipartFile) throws DataAccessException {
		int id = insertAndGetId(book);
		String imageName = postImage(multipartFile, String.valueOf(id));
		int bookRowNumber = jdbc.update("UPDATE book SET image=?"+" WHERE id=?", imageName, id);
		return bookRowNumber;
	}

	@Override
	public BookModel selectOne(int id) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM book"+" WHERE id=?", id);
		BookModel book = new BookModel();
		book.setId((int) map.get("id"));
		book.setCreatedAt((Date) map.get("created_at"));
		book.setImage((String) map.get("image"));
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
	public int updateOne(BookModel book, MultipartFile multipartFile) throws DataAccessException {
		String id = String.valueOf(book.getId());
		String imageName = null;
		if(!multipartFile.isEmpty()) {
			imageName = postImage(multipartFile, id);
		} else {
			System.out.println(book.getImage());
			imageName = book.getImage();
		}
		int bookRowNumber = jdbc.update("UPDATE book SET title=?, body=?, "
				+"author=?, genre=?, user_id=?, image=? WHERE id=?",
				book.getTitle(), book.getBody(), book.getAuthor(), book.getGenre()
				, book.getUser().getId(), imageName, book.getId());
		return bookRowNumber;
	}

	@Override
	public int deleteOne(int id) throws DataAccessException {
		int bookRowNumber = jdbc.update("DELETE FROM book WHERE id=?", id);
		return bookRowNumber;
	}

	@Override
	public int updateEvaluation(int bookId) throws DataAccessException {
		int rowNumber = jdbc.update("UPDATE book SET evaluation="
				+ "(SELECT AVG(evaluation) FROM comment WHERE book_id=?) "
				+ "WHERE id=?", bookId, bookId);
		return rowNumber;
	}

	@Override
	public int evaluationCount(int bookId) throws DataAccessException {
		int num = jdbc.queryForObject("SELECT COUNT(evaluation) FROM comment WHERE book_id="+ bookId, Integer.class);
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
		if(column.length() == 0) {
			int count = jdbc.queryForObject("SELECT COUNT(*) FROM book "
					+ "WHERE author LIKE "+"'%"+ word +"%' "+"OR title LIKE "+"'%"+ word +"%'", Integer.class);
			return count;
		}
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM book "
					+ "WHERE "+ column +" LIKE "+"'%"+ word +"%'", Integer.class);
		return count;
	}

	@Override
	public List<BookModel> searchBook(String word) throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM book "
				+ "WHERE title LIKE ? OR author LIKE ?", "%"+ word +"%", "%"+ word +"%");
		return getList(getList);
	}
}
