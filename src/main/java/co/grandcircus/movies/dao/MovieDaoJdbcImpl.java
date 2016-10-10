package co.grandcircus.movies.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import co.grandcircus.movies.exception.NotFoundException;
import co.grandcircus.movies.model.Movie;

/**
 * Provides access to movies by connecting to a SimpleMovie table in a SQL database.
 */
@Repository
@Primary
public class MovieDaoJdbcImpl implements MovieDao {
	
	
	private static final Logger logger = LoggerFactory.getLogger(MovieDao.class);

	@Autowired
	JdbcConnectionFactory connectionFactory;

	@Override
	public List<Movie> getAllMovies() {
		String sql = "SELECT * FROM SimpleMovie";
		try (Connection connection = connectionFactory.getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql)) {

			List<Movie> movies = new ArrayList<Movie>();
			while (result.next()) {
				Integer id = result.getInt("id");
				String title = result.getString("title");
				String category = result.getString("category");

				movies.add(new Movie(id, title, category));
			}

			return movies;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<Movie> getMoviesByCategory(String cat) {
		String sql = "SELECT * FROM SimpleMovie WHERE category = ?";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, cat);
			ResultSet result = statement.executeQuery();

			List<Movie> movies = new ArrayList<Movie>();
			while (result.next()) {
				Integer id = result.getInt("id");
				String title = result.getString("title");
				String category = result.getString("category");

				movies.add(new Movie(id, title, category));
			}

			return movies;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Movie getMovie(int id) throws NotFoundException {
		String sql = "SELECT * FROM SimpleMovie WHERE id = ?";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				String title = result.getString("title");
				String category = result.getString("category");

				return new Movie(id, title, category);
			} else {
				throw new NotFoundException("No such movie.");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public int addMovie(Movie movie) {
		String sql = "INSERT INTO SimpleMovie (title, category) VALUES (?, ?)";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, movie.getTitle());
			statement.setString(2, movie.getCategory());

			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating movie failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					movie.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating movie failed, no ID obtained.");
				}
			}

			return movie.getId();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void updateMovie(int id, Movie movie) throws NotFoundException {
		String sql = "UPDATE SimpleMovie SET title = ?, category = ? WHERE id = ?";
		try (Connection conn = connectionFactory.getConnection();
				PreparedStatement statement = conn
						.prepareStatement(sql)) {
			statement.setString(1, movie.getTitle());
			statement.setString(2, movie.getCategory());
			statement.setInt(3, movie.getId());

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated != 1) {
				throw new NotFoundException("No such movie");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void deleteMovie(int id) throws NotFoundException {
		String sql = "DELETE FROM SimpleMovie WHERE id = ?";
		try (Connection conn = connectionFactory.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, id);

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated != 1) {
				throw new NotFoundException("No such movie");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Set<String> getAllCategories() {
		String sql = "SELECT DISTINCT category from SimpleMovie";
		try (Connection connection = connectionFactory.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			Set<String> categories = new TreeSet<>();
			while (result.next()) {
				String category = result.getString("category");
				categories.add(category);
			}

			return categories;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean isValidCategory(String category) {
		return getAllCategories().contains(category);
	}

}
