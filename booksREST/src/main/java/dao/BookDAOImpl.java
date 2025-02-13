package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Author;
import model.Book;
import model.Category;

public class BookDAOImpl implements BookDAO {
	
	private String dbPath;
	
	 public BookDAOImpl(String dbPath) {
	        this.dbPath = dbPath;
	    }

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}


	private Connection getConnection() throws SQLException {
		try {
	        // Locate the database file in the webapp folder
	        String path = this.getClass().getClassLoader().getResource("../../Books.db").getPath();

	        // Decode the path in case of spaces or special characters
	        path = java.net.URLDecoder.decode(path, "UTF-8");

	        // Connect to the SQLite database
	        return DriverManager.getConnection("jdbc:sqlite:" + path);
	    } catch (Exception e) {
	        throw new SQLException("Failed to locate or connect to the database file", e);
	    }
	}
	
	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} catch (SQLException ex) {
		}
	}

	
	public List<Book> findAllBooks() {
		List<Book> result = new ArrayList<Book>();
		
        // join 3 tables to get needed info
		String sql = """
            SELECT 
                b.ID AS book_id, 
                b.BOOK_TITLE, 
                b.CATEGORY_ID, 
                c.CATEGORY_DESCRIPTION, 
                a.ID AS author_id, 
                a.FIRST_NAME, 
                a.LAST_NAME
            FROM 
                BOOK b
            LEFT JOIN 
                AUTHOR a ON b.ID = a.BOOK_ID
            LEFT JOIN 
                CATEGORY c ON b.CATEGORY_ID = c.ID
        """;
				
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				// populate book and author beans with needed info, and then set author to book
				
				Book book = new Book();
	            book.setId(resultSet.getLong("book_id"));
	            book.setBookTitle(resultSet.getString("BOOK_TITLE"));
	            book.setCategoryId(resultSet.getLong("CATEGORY_ID"));
	            book.setCategory(resultSet.getString("CATEGORY_DESCRIPTION"));

	            // Check if author details exist
	            Long authorId = resultSet.getLong("author_id");
	            if (authorId != 0) { // If `author_id` is not null
	                Author author = new Author();
	                author.setId(authorId);
	                author.setBookId(resultSet.getLong("book_id"));
	                author.setFirstName(resultSet.getString("FIRST_NAME"));
	                author.setLastName(resultSet.getString("LAST_NAME"));
	                book.setAuthor(author);
	            } else {
	                book.setAuthor(null); // No author available
	            }
				 				
				result.add(book);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}

	
	@Override
	public List<Book> searchBooksByKeyword(String keyWord) {
		List<Book> result = new ArrayList<Book>();
		
		String sql = "SELECT "
	               + "b.ID AS book_id, "
	               + "b.BOOK_TITLE, "
	               + "b.CATEGORY_ID, "
	               + "c.CATEGORY_DESCRIPTION, "
	               + "a.ID AS author_id, "
	               + "a.FIRST_NAME, "
	               + "a.LAST_NAME "
	               + "FROM BOOK b "
	               + "LEFT JOIN AUTHOR a ON b.ID = a.BOOK_ID "
	               + "LEFT JOIN CATEGORY c ON b.CATEGORY_ID = c.ID "
	               + "WHERE b.BOOK_TITLE LIKE '%" + keyWord.trim() + "%' "
	               + "OR a.FIRST_NAME LIKE '%" + keyWord.trim() + "%' "
	               + "OR a.LAST_NAME LIKE '%" + keyWord.trim() + "%' "
	               + "OR c.CATEGORY_DESCRIPTION LIKE '%" + keyWord.trim() + "%'";
		
		Connection connection = null;
		try {

			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
			
				// populate book and author with needed info, and then set author to book
				Book book = new Book();
				book.setId(resultSet.getLong("book_id"));
	            book.setBookTitle(resultSet.getString("BOOK_TITLE"));
	            book.setCategoryId(resultSet.getLong("CATEGORY_ID"));
	            book.setCategory(resultSet.getString("CATEGORY_DESCRIPTION"));
	            
				Author author = new Author();
				author.setId(resultSet.getLong("author_id"));
	            author.setBookId(resultSet.getLong("book_id"));
	            author.setFirstName(resultSet.getString("FIRST_NAME"));
	            author.setLastName(resultSet.getString("LAST_NAME"));
				
				book.setAuthor(author);
				
				result.add(book);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}

		return result;
	}

	
	public List<Category> findAllCategories() {
		List<Category> result = new ArrayList<>();
		String sql = "select * from category";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				// populate category bean with needed info
				Category category = new Category();				
				category.setId(resultSet.getLong("ID"));
	            category.setCategoryDescription(resultSet.getString("CATEGORY_DESCRIPTION"));


				result.add(category);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}

	
	
	
	public List<Book> findBooksByCategory(String category) {
		List<Book> result = new ArrayList<Book>();
		 

		String sql = "SELECT BOOK.ID AS BOOK_ID, BOOK.BOOK_TITLE, BOOK.CATEGORY_ID, CATEGORY.CATEGORY_DESCRIPTION, " +
                "AUTHOR.ID AS AUTHOR_ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME " +
                "FROM BOOK " +
                "INNER JOIN CATEGORY ON BOOK.CATEGORY_ID = CATEGORY.ID " +
                "INNER JOIN AUTHOR ON BOOK.ID = AUTHOR.BOOK_ID " +
                "WHERE CATEGORY.CATEGORY_DESCRIPTION = ?";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, category);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
			
				// populate book and author beans with needed info, and then set author to book
	            Book book = new Book();
	            book.setId(resultSet.getLong("BOOK_ID"));
	            book.setBookTitle(resultSet.getString("BOOK_TITLE"));
	            book.setCategoryId(resultSet.getLong("CATEGORY_ID"));
	            book.setCategory(resultSet.getString("CATEGORY_DESCRIPTION"));

	            Author author = new Author();
	            author.setId(resultSet.getLong("AUTHOR_ID"));
	            author.setFirstName(resultSet.getString("FIRST_NAME"));
	            author.setLastName(resultSet.getString("LAST_NAME"));

	            book.setAuthor(author);

				
				result.add(book);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return result;
	}
	
	
	public void insert(Book book) {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"insert into Book (book_title) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, book.getBookTitle());
			statement.execute();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				book.setId(generatedKeys.getLong(1));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	}

	
	public void delete(Long bookId) {
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement statement = connection
					.prepareStatement("delete from book where id=?");
			statement.setLong(1, bookId);
			statement.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(connection);
		}
	}
	

}