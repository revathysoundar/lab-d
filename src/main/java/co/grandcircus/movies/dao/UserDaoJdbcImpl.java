package co.grandcircus.movies.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import co.grandcircus.movies.exception.NotFoundException;
import co.grandcircus.movies.model.Movie;
import co.grandcircus.movies.model.User;



@Repository
@Primary
public class UserDaoJdbcImpl implements UserDao{
	
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	
	@Autowired
	JdbcConnectionFactory connectionFactory;
	
	@Override
	public List<User> getAllUsers()
	{
		
		String sql = "SELECT * FROM Users";
		try(Connection connection = connectionFactory.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql)){
			
			List<User> users = new ArrayList<User>();
			while(result.next()){
				Integer id = result.getInt("id");
				String firstName = result.getString("firstName");
				String lastName = result.getString("lastName");
				String email = result.getString("email");
				String password = result.getString("password");
				users.add(new User(id,firstName, lastName, email, password));
			}
				return users;
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		
				
	}

	@Override
	public List<User> getAllUsersSortedBy(String sort) throws IllegalArgumentException {
		List<User> users = new ArrayList<User>();
		if(sort.equalsIgnoreCase("firstName") || sort.equalsIgnoreCase("lastName") || sort.equalsIgnoreCase("email")){
		String sql = "SELECT * FROM Users ORDER BY "+sort;
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
				ResultSet result = statement.executeQuery();

			
			while (result.next()) {
				Integer id = result.getInt("id");
				String firstName = result.getString("firstName");
				String lastName = result.getString("lastName");
				String email = result.getString("email");
				String password = result.getString("password");
				users.add(new User(id,firstName, lastName, email,password));
			}
		}
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		}
			return users;
		
	}

	@Override
	public User getUser(int id) throws NotFoundException {
		String sql = "SELECT * FROM Users WHERE id = ?";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				String firstName = result.getString("firstName");
				String lastName = result.getString("lastName");
				String email = result.getString("email");
				String password = result.getString("password");

				return new User(id, firstName, lastName, email, password);
			} else {
				throw new NotFoundException("No such user.");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public User getUserByEmailAndPassword(String mail, String passwrd) {
		String sql = "SELECT * FROM Users WHERE email = ? AND password =?";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, mail);
			statement.setString(2, passwrd);
			ResultSet result = statement.executeQuery();
			User user = new User();
			if (result.next()) {
				Integer id = result.getInt("id");
				String firstName = result.getString("firstName");
				String lastName = result.getString("lastName");
				String email = result.getString("email");
				String password = result.getString("password");
				user = new User(id,firstName, lastName,email,password);
			}

			return user;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public int addUser(User user) {
		String sql = "INSERT INTO Users (firstName, lastName, email, password) VALUES (?, ?,?, ?)";
		try (Connection connection = connectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getPassword());

			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating movie failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					user.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating movie failed, no ID obtained.");
				}
			}

			return user.getId();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void updateUser(int id, User user) throws NotFoundException {
		String sql = "UPDATE Users SET firstName = ?, lastName = ? WHERE id = ?";
		try (Connection conn = connectionFactory.getConnection();
				PreparedStatement statement = conn
						.prepareStatement(sql)) {
			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setInt(3, user.getId());

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated != 1) {
				throw new NotFoundException("No such user");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void deleteUser(int id) throws NotFoundException {
		String sql = "DELETE FROM Users WHERE id = ?";
		try (Connection conn = connectionFactory.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, id);

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated != 1) {
				throw new NotFoundException("No such user");
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
		
	}
		
	
	
	
	
	
	
	
	

}
